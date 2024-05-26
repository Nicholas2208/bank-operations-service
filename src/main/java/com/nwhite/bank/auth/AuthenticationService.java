package com.nwhite.bank.auth;

import com.nwhite.bank.account.Account;
import com.nwhite.bank.exception.UserAlreadyExistsException;
import com.nwhite.bank.security.JwtService;
import com.nwhite.bank.user.User;
import com.nwhite.bank.user.UserRepository;
import com.nwhite.bank.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegistrationRequest request) {
        var user = User.builder()
                .phoneNumbers(request.getPhoneNumbers())
                .emails(request.getEmails())
                .birthDate(request.getBirthDate())
                .fio(request.getFio())
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .initialBalance(request.getInitialBalance())
                .build();
        var account = Account.builder()
                .balance(user.getInitialBalance())
                .user(user)
                .build();
        user.setAccount(account);

        checkUserUnique(user);

        userRepository.save(user);
    }

    private void checkUserUnique(User user) {
        Optional<User> existingUser
                = userRepository.findByPhoneNumbersInOrEmailsIn(user.getPhoneNumbers().get(0),
                user.getEmails().get(0));
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Аккаунт с указанным телефоном или email уже зарегистрирован.");
        }
    }

    public LoginResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fio", user.getFio());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        return LoginResponse.builder()
                .token(jwtToken)
                .build();
    }
}
