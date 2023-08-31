package com.clevertec.bankmanager.store.dao;

import com.clevertec.bankmanager.store.entity.Account;
import com.clevertec.bankmanager.store.entity.Transaction;

import java.time.LocalDate;

public interface AccountDao extends AbstractDao<Long, Account> {

    Account deposit(Account id, Double value);

    Account withdraw(Account id, Double value);

    Account cashback(Account account, LocalDate cashbackLastDate, Double percent);

    Transaction transfer(Account senderAccount, Account recipientAccount, Double value);
}
