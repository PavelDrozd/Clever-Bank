package com.clevertec.bankmanager.store.dao;

import com.clevertec.bankmanager.store.entity.Transaction;

/**
 * This interfaces used for work with transaction entities
 * Extends AbstractDao with CRUD operations
 * Set Long as K - key and Transaction as T - type
 */
public interface TransactionDao extends AbstractDao<Long, Transaction> {
}
