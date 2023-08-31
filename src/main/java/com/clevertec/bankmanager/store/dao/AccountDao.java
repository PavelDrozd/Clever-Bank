package com.clevertec.bankmanager.store.dao;

import com.clevertec.bankmanager.store.entity.Account;
import com.clevertec.bankmanager.store.entity.Transaction;

import java.time.LocalDate;

/**
 * This interfaces used for work with account entities
 * Extends AbstractDao with CRUD operations
 * Set Long as K - key and Account as T - type
 */
public interface AccountDao extends AbstractDao<Long, Account> {

    /**
     * This method is used to deposit the amount to the account and update data in database
     * @param account accepts the account to which the amount is deposited
     * @param value amount to be credited to the account
     * @return updated account with new amount from database
     */
    Account deposit(Account account, Double value);

    /**
     * This method is used to withdraw the amount from the account and update the data in database
     * @param account accepts the account from which the amount will be withdrawn
     * @param value amount to be debited from the account
     * @return updated account with new amount from database
     */
    Account withdraw(Account account, Double value);

    /**
     * This method add a percent of amount on account
     * @param account accepts the account for cashback
     * @param cashbackLastDate accepts the date for last cashback
     * @param percent accepts the percent of cashback
     * @return updated account with new amount from database
     */
    Account cashback(Account account, LocalDate cashbackLastDate, Double percent);

    /**
     * This method is used to make transactions to transfer the amount from one account to the second
     * @param senderAccount accepts the account who sent amount
     * @param recipientAccount accepts the account who receive amount
     * @param value transaction amount
     * @return new transaction object
     */
    Transaction transfer(Account senderAccount, Account recipientAccount, Double value);
}
