package com.clevertec.bankmanager.data.dto;

import com.clevertec.bankmanager.store.entity.Account;
import lombok.Data;

@Data
public class TransactionDto {

    private Long id;

    private Double amount;

    private Account recipient;

    private Account sender;
}
