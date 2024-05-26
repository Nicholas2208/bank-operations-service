package com.nwhite.bank.auth;

import com.nwhite.bank.constants.Constants;
import com.nwhite.bank.exception.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger
            = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ResponseDto> register(
            @RequestBody @Valid RegistrationRequest request
    ){
        service.register(request);
        logger.info("Зарегистрирован пользователь " + request.getLogin());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(Constants.STATUS_201, Constants.MESSAGE_201));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginRequest request
    ) {
        logger.info("Пользователь " + request.getLogin() + " авторизован");
        return ResponseEntity.ok(service.login(request));
    }
}
