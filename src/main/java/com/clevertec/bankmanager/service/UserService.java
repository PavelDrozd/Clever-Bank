package com.clevertec.bankmanager.service;

import com.clevertec.bankmanager.data.dto.UserDto;

/**
 * This interfaces used for work with user DTO classes.
 * Extends AbstractService with CRUD operations.
 * Set Long as K - key and UserDto as T - type.
 */
public interface UserService extends AbstractService<Long, UserDto> {
}
