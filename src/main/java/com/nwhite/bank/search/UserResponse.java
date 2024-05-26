package com.nwhite.bank.search;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private LocalDate birthDate;
    private String fio;
}
