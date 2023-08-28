package com.clevertec.bankmanager.service;

import com.clevertec.bankmanager.data.dto.TransactionDto;

public interface TransactionService extends AbstractService<Long, TransactionDto> {

    TransactionDto transfer(Long senderAccountId, Long recipientAccountId, Double value);
}
