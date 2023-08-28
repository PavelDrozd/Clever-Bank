package com.clevertec.bankmanager.store.entity;

import lombok.Data;

@Data
public class Account {

    private Long id;

    private Double amount;

    private Long bankId;

    private Long userId;
}
