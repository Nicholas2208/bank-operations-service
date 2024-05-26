package com.nwhite.bank.account;

import com.nwhite.bank.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToOne(mappedBy = "account")
    private User user;
}
