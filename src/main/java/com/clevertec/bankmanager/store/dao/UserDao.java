package com.clevertec.bankmanager.store.dao;

import com.clevertec.bankmanager.store.entity.User;

/**
 * This interfaces used for work with user entities
 * Extends AbstractDao with CRUD operations
 * Set Long as K - key and User as T - type
 */
public interface UserDao extends AbstractDao<Long, User> {
}
