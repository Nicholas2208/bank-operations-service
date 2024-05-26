package com.nwhite.bank.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "Введите номер телефона.")
    @NotNull(message = "Введите номер телефона.")
    private List<String> phoneNumbers;
    @NotEmpty(message = "Введите email.")
    @NotNull(message = "Введите email.")
    private List<String> emails;
    @Min(value = 0, message = "Баланс не может быть отрицательным.")
    private BigDecimal initialBalance;
    @NotNull(message = "Введите дату рождения.")
    private LocalDate birthDate;
    @NotEmpty(message = "Введите ФИО.")
    @NotNull(message = "Введите ФИО.")
    private String fio;
    @NotEmpty(message = "Логин не может быть пустым.")
    @NotNull(message = "Логин не может быть пустым.")
    private String login;
    @NotEmpty(message = "Пароль не может быть пустым.")
    @NotNull(message = "Пароль не может быть пустым.")
    private String password;
}
