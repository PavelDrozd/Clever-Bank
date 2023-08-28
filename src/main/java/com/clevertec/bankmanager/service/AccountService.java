package com.clevertec.bankmanager.service;

import com.clevertec.bankmanager.data.dto.AccountDto;

public interface AccountService extends AbstractService<Long, AccountDto> {

    AccountDto deposit(Long id, Double value);

    AccountDto withdraw(Long id, Double value);
}
