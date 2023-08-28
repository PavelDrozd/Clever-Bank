package com.clevertec.bankmanager.store.entity;

import lombok.Data;

@Data
public class Transaction {

    private Long id;

    private Double amount;

    private Account recipient;

    private Account sender;
}
