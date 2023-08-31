package com.clevertec.bankmanager.service;

import com.clevertec.bankmanager.data.dto.AccountDto;

public interface AccountService extends AbstractService<Long, AccountDto> {

    AccountDto deposit(AccountDto accountDto, Double value);

    AccountDto withdraw(AccountDto accountDto, Double value);
}
