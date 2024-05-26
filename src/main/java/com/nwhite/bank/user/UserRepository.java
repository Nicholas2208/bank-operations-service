package com.nwhite.bank.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String username);

    @Query(value = "SELECT * FROM _user WHERE ?1 = ANY(phone_numbers) OR ?2 = ANY(emails)", nativeQuery = true)
    Optional<User> findByPhoneNumbersInOrEmailsIn(String phoneNumber, String email);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM _user u WHERE :email = ANY(u.emails))", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM _user u WHERE :phone = ANY(u.phone_numbers))", nativeQuery = true)
    boolean existsByPhone(@Param("phone") String phone);

    @Query(value = "SELECT * FROM _user WHERE ?1 = ANY(phone_numbers)", nativeQuery = true)
    Optional<User> findByPhoneNumbersContaining(String phoneNumber);

    @Query(value = "SELECT * FROM _user WHERE ?1 = ANY(emails)", nativeQuery = true)
    Optional<User> findByEmailContaining(String email);

    List<User> findByBirthDateAfter(LocalDate birthDate);

    @Query("SELECT u FROM User u WHERE LOWER(u.fio) LIKE LOWER(CONCAT(:fio, '%'))")
    Page<User> findByFioLikeIgnoreCase(Pageable pageable, String fio);

}
