package com.clevertec.bankmanager.data.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountDto {

    private Long id;

    private Long number;

    private Double amount;

    private LocalDate cashbackLastDate;

    private BankDto bank;

    private UserDto user;
}
