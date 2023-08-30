package com.clevertec.bankmanager.store.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transaction {

    private Long id;

    private Double amount;

    private LocalDateTime dateTime;

    private Account recipientAccount;

    private Account senderAccount;
}
