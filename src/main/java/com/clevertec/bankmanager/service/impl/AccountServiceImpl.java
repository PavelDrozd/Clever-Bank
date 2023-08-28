package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.data.dto.AccountDto;
import com.clevertec.bankmanager.service.AccountService;
import com.clevertec.bankmanager.shared.exception.service.ServiceException;
import com.clevertec.bankmanager.shared.exception.service.ServiceValidationException;
import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.shared.util.mapper.EntityDtoMapper;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.entity.Account;

import java.util.List;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final EntityDtoMapper mapper;

    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
        mapper = EntityDtoMapper.getInstance();
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        try {
            Account account = accountDao.create(mapper.mapToAccount(accountDto));
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<AccountDto> getAll() {
        try {
            return accountDao.getAll().stream().map(mapper::mapToAccountDto).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AccountDto getById(Long id) {
        try {
            if (accountDao.getById(id) == null) {
                throw new ServiceValidationException("Account with ID:" + id + " is null.");
            }
            return mapper.mapToAccountDto(accountDao.getById(id));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        try {
            Account account = accountDao.update(mapper.mapToAccount(accountDto));
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            if (accountDao.delete(id)) {
                throw new ServiceValidationException("Can't delete account by id: " + id);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AccountDto deposit(Long id, Double value) {
        try {
            Account account = accountDao.deposit(id, value);
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AccountDto withdraw(Long id, Double value) {
        try {
            Account account = accountDao.withdraw(id, value);
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
