package com.clevertec.bankmanager.store.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Account {

    private Long id;

    private Long number;

    private Double amount;

    private LocalDate cashbackLastDate;

    private Bank bank;

    private User user;
}
