package com.clevertec.bankmanager.service;

import com.clevertec.bankmanager.data.dto.AccountDto;

/**
 * This interfaces used for work with account DTO classes.
 * Extends AbstractService with CRUD operations.
 * Set Long as K - key and AccountDto as T - type.
 */
public interface AccountService extends AbstractService<Long, AccountDto> {

    /**
     * This method is used to deposit the amount to the account.
     *
     * @param accountDto expected the account DTO class to which the amount is deposited.
     * @param value      expected amount to be credited to the account DTO class.
     * @return updated accountDto with new amount from DAO.
     */
    AccountDto deposit(AccountDto accountDto, Double value);

    /**
     * This method is used to withdraw the amount from the account.
     *
     * @param accountDto expected the account DTO class from which the amount will be withdrawn.
     * @param value      expected amount to be debited from the account DTO class.
     * @return updated accountDto with new amount from DAO.
     */
    AccountDto withdraw(AccountDto accountDto, Double value);
}
