package com.clevertec.bankmanager.store.dao;

import com.clevertec.bankmanager.store.entity.Account;
import com.clevertec.bankmanager.store.entity.Transaction;

public interface AccountDao extends AbstractDao<Long, Account> {

    Account deposit(Long id, Double value);

    Account withdraw(Long id, Double value);

    Transaction transfer(Long senderAccountId, Long recipientAccountId, Double value);
}
