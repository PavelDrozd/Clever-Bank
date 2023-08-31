package com.clevertec.bankmanager.store.dao;

import com.clevertec.bankmanager.store.entity.Bank;

/**
 * This interfaces used for work with bank entities
 * Extends AbstractDao with CRUD operations
 * Set Long as K - key and Bank as T - type
 */
public interface BankDao extends AbstractDao<Long, Bank> {
}
