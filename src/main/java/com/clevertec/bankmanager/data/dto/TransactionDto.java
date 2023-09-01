package com.clevertec.bankmanager.data.dto;

import com.clevertec.bankmanager.store.entity.Account;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data transfer object class for bank entity.
 */
@Data
public class TransactionDto {

    /** ID used as primary key. */
    private Long id;

    /** Transaction amount. */
    private Double amount;

    /** Date and time of the transaction. */
    private LocalDateTime dateTime;

    /** Link to the account of the recipient who receives the amount. */
    private Account recipientAccount;

    /** Link to the account of the sender who sent the amount. */
    private Account senderAccount;
}