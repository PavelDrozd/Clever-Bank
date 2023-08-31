package com.clevertec.bankmanager.service;

import com.clevertec.bankmanager.data.dto.AccountDto;
import com.clevertec.bankmanager.data.dto.TransactionDto;

public interface TransactionService extends AbstractService<Long, TransactionDto> {

    TransactionDto transfer(AccountDto senderAccount, AccountDto recipientAccount, Double value);
}
