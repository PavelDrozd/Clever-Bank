package com.clevertec.bankmanager.service;

import com.clevertec.bankmanager.data.dto.BankDto;

/**
 * This interfaces used for work with bank DTO classes.
 * Extends AbstractService with CRUD operations.
 * Set Long as K - key and BankDto as T - type.
 */
public interface BankService extends AbstractService<Long, BankDto> {
}
