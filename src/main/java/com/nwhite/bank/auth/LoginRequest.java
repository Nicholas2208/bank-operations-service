package com.nwhite.bank.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {

    @NotEmpty(message = "Введите логин")
    @NotNull(message = "Введите логин")
    private String login;

    @NotEmpty(message = "Введите пароль")
    @NotNull(message = "Введите пароль")
    private String password;
}
