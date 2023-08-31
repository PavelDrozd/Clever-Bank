package com.clevertec.bankmanager.store.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * This is entity class for transaction object in database
 */
@Data
public class Transaction {

    /** ID used as primary key in database */
    private Long id;

    /** Transaction amount */
    private Double amount;

    /** Date and time of the transaction */
    private LocalDateTime dateTime;

    /** Link to the account of the recipient who receives the amount */
    private Account recipientAccount;

    /** Link to the account of the sender who sent the amount */
    private Account senderAccount;
}
