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

/**
 * Implementation of service interface for process account DTO objects.
 */
public class AccountServiceImpl implements AccountService {

    /** AccountDao is used to get objects from DAO module. */
    private final AccountDao accountDao;
    /** Mapper for mapping DTO and entity objects. */
    private final EntityDtoMapper mapper;

    /**
     * This constructor use AccountDao implementation and set instance of mapper from EntityDtoMapper.
     *
     * @param accountDao expected AccountDao implementation class.
     */
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
        this.mapper = EntityDtoMapper.getInstance();
    }

    /**
     * Method for create new DTO class and transfer it to database by using DAO.
     *
     * @param accountDto expected object of type AccountDto to create it.
     * @return new created AccountDto object.
     */
    @Override
    public AccountDto create(AccountDto accountDto) {
        try {
            Account account = accountDao.create(mapper.mapToAccount(accountDto));
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Method for getting all account DTO objects from DAO.
     *
     * @return List of AccountDto objects.
     */
    @Override
    public List<AccountDto> getAll() {
        try {
            return accountDao.getAll().stream().map(mapper::mapToAccountDto).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Method get account DTO object from DAO by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return AccountDto object.
     */
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

    /**
     * Method update account DTO in database by using DAO.
     *
     * @param accountDto expected updated object of type AccountDto.
     * @return updated AccountDto object.
     */
    @Override
    public AccountDto update(AccountDto accountDto) {
        try {
            Account account = accountDao.update(mapper.mapToAccount(accountDto));
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Method delete object by using DAO by ID.
     *
     * @param id expected object of type Long used as primary key.
     */
    @Override
    public void delete(Long id) {
        try {
            if (!accountDao.delete(id)) {
                throw new ServiceValidationException("Can't delete account by id: " + id);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * This method is used to deposit the amount to the account and update data in database by DAO.
     *
     * @param accountDto expected the account to which the amount is deposited.
     * @param value      expected amount to be credited to the account.
     * @return updated AccountDto object.
     */
    @Override
    public AccountDto deposit(AccountDto accountDto, Double value) {
        try {
            Account account = accountDao.deposit(mapper.mapToAccount(accountDto), value);
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * This method is used to withdraw the amount from the account and update the data in database by DAO.
     *
     * @param account expected the account from which the amount will be withdrawn.
     * @param value   expected amount to be debited from the account.
     * @return updated AccountDto object.
     */
    @Override
    public AccountDto withdraw(AccountDto accountDto, Double value) {
        try {
            Account account = accountDao.withdraw(mapper.mapToAccount(accountDto), value);
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
