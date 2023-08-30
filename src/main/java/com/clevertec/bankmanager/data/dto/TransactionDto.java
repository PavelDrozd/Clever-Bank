package com.clevertec.bankmanager.data.dto;

import com.clevertec.bankmanager.store.entity.Account;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private Long id;

    private Double amount;

    private LocalDateTime dateTime;

    private Account recipientAccount;

    private Account senderAccount;
}
