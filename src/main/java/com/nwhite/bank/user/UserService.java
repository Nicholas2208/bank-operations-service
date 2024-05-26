package com.nwhite.bank.user;

import com.nwhite.bank.account.Account;
import com.nwhite.bank.account.AccountRepository;
import com.nwhite.bank.common.PageResponse;
import com.nwhite.bank.exception.InvalidActionException;
import com.nwhite.bank.exception.UserAlreadyExistsException;
import com.nwhite.bank.search.UserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger
            = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public User addEmail(String email, Authentication authenticatedUser) {
        checkEmailUnique(email);

        User user = (User) authenticatedUser.getPrincipal();
        List<String> emails = user.getEmails();

        for (User otherUser : userRepository.findAll()) {
            if (otherUser != user && otherUser.getEmails().contains(email)) {
                throw new InvalidActionException("Email  " + email + " занята другим пользователем.");
            }
        }

        emails.add(email);
        logger.info("Пользователь " + authenticatedUser.getName() + " добавил email.");
        return userRepository.save(user);
    }

    public User removeEmail(int index, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        List<String> emails = user.getEmails();
        if (emails.size() > 1) {
            emails.remove(index - 1);
        } else {
            throw new InvalidActionException("У пользователя должна быть как минимум одна emai.");
        }

        logger.info("Пользователь " + authenticatedUser.getName() + " удалил email.");
        return userRepository.save(user);
    }

    public User updateEmail(String email, int index, Authentication authenticatedUser) {
        checkEmailUnique(email);
        User user = (User) authenticatedUser.getPrincipal();
        List<String> emails = user.getEmails();
        emails.set(index - 1, email);
        logger.info("Пользователь " + authenticatedUser.getName() + " изменил email.");
        return userRepository.save(user);
    }

    public User addPhoneNumber(String phone, Authentication authenticatedUser) {
        checkPhoneUnique(phone);

        User user = (User) authenticatedUser.getPrincipal();
        List<String> phoneNumbers = user.getPhoneNumbers();

        for (User otherUser : userRepository.findAll()) {
            if (otherUser != user && otherUser.getPhoneNumbers().contains(phone)) {
                throw new InvalidActionException("Другой поьзователь зарегистрирован с номером телефона + " + phone);
            }
        }

        phoneNumbers.add(phone);
        logger.info("Пользователь " + authenticatedUser.getName() + " добавил номер телефона.");
        return userRepository.save(user);
    }

    public User removePhoneNumber(int index, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();
        List<String> phoneNumbers = user.getPhoneNumbers();
        if (phoneNumbers.size() > 1) {
            phoneNumbers.remove(index - 1);
        } else {
            throw new InvalidActionException("У пользователя должен быть как минимум один номер телефона.");
        }

        logger.info("Пользователь " + authenticatedUser.getName() + " удалил номер телефона.");
        return userRepository.save(user);
    }

    public User updatePhoneNumber(String phone, int index, Authentication authenticatedUser) {
        checkPhoneUnique(phone);

        User user = (User) authenticatedUser.getPrincipal();
        List<String> phoneNumbers = user.getPhoneNumbers();
        phoneNumbers.set(index - 1, phone);

        logger.info("Пользователь " + authenticatedUser.getName() + " изменил номер телефона.");
        return userRepository.save(user);
    }

    public Optional<UserResponse> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumbersContaining(phoneNumber)
                .map(user -> UserResponse.builder()
                        .birthDate(user.getBirthDate())
                        .fio(user.getFio())
                        .build());
    }


    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmailContaining(email)
                .map(user -> UserResponse.builder()
                        .birthDate(user.getBirthDate())
                        .fio(user.getFio())
                        .build());
    }

    public List<UserResponse> findByBirthDate(LocalDate birthDate) {
        List<User> usersWithLaterBirthDate = userRepository.findByBirthDateAfter(birthDate);

        return usersWithLaterBirthDate.stream()
                .map(user -> UserResponse.builder()
                        .birthDate(user.getBirthDate())
                        .fio(user.getFio())
                        .build())
                .collect(Collectors.toList());
    }

    public PageResponse<UserResponse> findByFio(int page, int size, String fio) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fio").descending());
        Page<User> users = userRepository.findByFioLikeIgnoreCase(pageable, fio);

        List<UserResponse> userResponse = users.stream()
                .map(user -> UserResponse.builder()
                        .birthDate(user.getBirthDate())
                        .fio(user.getFio())
                        .build())
                .collect(Collectors.toList());


        return  new PageResponse<>(
                userResponse,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast()
        );
    }

    public void checkPhoneUnique(String phone) {
        boolean unique
                = !userRepository.existsByPhone(phone);
        if (!unique) {
            throw new UserAlreadyExistsException("Аккаунт с указанным телефоном уже зарегистрирован.");
        }
    }

    public void checkEmailUnique(String email) {
        boolean unique
                = !userRepository.existsByEmail(email);
        if (!unique) {
            throw new UserAlreadyExistsException("Аккаунт с указанной email уже зарегистрирован.");
        }
    }

    @Scheduled(initialDelay = 60000, fixedRate = 60000)
    @Transactional
    public void updateBalances() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            User user = account.getUser();
            BigDecimal initialBalance = user.getInitialBalance();
            BigDecimal maxBalance = initialBalance.multiply(BigDecimal.valueOf(2.07));
            BigDecimal newBalance = account.getBalance().multiply(BigDecimal.valueOf(1.05));

            if (newBalance.compareTo(maxBalance) <= 0) {
                account.setBalance(newBalance);
            } else {
                account.setBalance(maxBalance);
            }

            accountRepository.save(account);
        }
    }
}
