package com.clevertec.bankmanager.service;

import com.clevertec.bankmanager.data.dto.TransactionDto;

/**
 * This interfaces used for work with transaction DTO classes.
 * Extends AbstractService with CRUD operations.
 * Set Long as K - key and TransactionDto as T - type.
 */
public interface TransactionService extends AbstractService<Long, TransactionDto> {

    /**
     * This method is used to make transactions to transfer the amount from one account DTO class to the second.
     *
     * @param senderAccount    expected the accountDto who sent amount.
     * @param recipientAccount expected the accountDto who receive amount.
     * @param value            expected transaction amount.
     * @return new transactionDto object.
     */
    TransactionDto transfer(Long senderAccountId, Long recipientAccountId, Double value);
}
