package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.data.dto.AccountDto;
import com.clevertec.bankmanager.service.AccountService;
import com.clevertec.bankmanager.shared.exception.service.ServiceException;
import com.clevertec.bankmanager.shared.exception.service.ServiceValidationException;
import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.shared.util.mapper.EntityDtoMapper;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.entity.Account;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementation of service interface for process account DTO objects.
 */
@Slf4j
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
        log.debug("SERVICE CREATE ACCOUNT: " + accountDto);
        try {
            Random random = new Random();
            long randomNumber = (long) (random.nextDouble() * 9_000_000_000_000L) + 1_000_000_000_000L;
            accountDto.setNumber(randomNumber);
            Account account = accountDao.create(mapper.mapToAccount(accountDto));
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - CREATE ACCOUNT: " + e.getMessage());
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
        log.debug("SERVICE GET ALL ACCOUNTS: ");
        try {
            return accountDao.getAll().stream().map(mapper::mapToAccountDto).collect(Collectors.toList());
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - GET ALL ACCOUNTS: " + e.getMessage());
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
        log.debug("SERVICE GET ACCOUNT BY ID: " + id);
        try {
            if (accountDao.getById(id) == null) {
                log.error("SERVICE VALIDATION EXCEPTION - GET ACCOUNT BY ID ");
                throw new ServiceValidationException("Account with ID:" + id + " is null.");
            }
            return mapper.mapToAccountDto(accountDao.getById(id));
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - GET ACCOUNT BY ID: " + e.getMessage());
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
        log.debug("SERVICE UPDATE ACCOUNT: " + accountDto);
        try {
            Account account = accountDao.update(mapper.mapToAccount(accountDto));
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - UPDATE ACCOUNT: " + e.getMessage());
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
        log.debug("SERVICE DELETE ACCOUNT BY ID: " + id);
        try {
            if (!accountDao.delete(id)) {
                log.error("SERVICE VALIDATION EXCEPTION - DELETE ACCOUNT ");
                throw new ServiceValidationException("Can't delete account by id: " + id);
            }
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - DELETE ACCOUNT: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * This method is used to deposit the amount to the account and update data in database by DAO.
     *
     * @param id    expected account id to which the amount is deposited.
     * @param value expected amount to be credited to the account.
     * @return updated AccountDto object.
     */
    @Override
    public AccountDto deposit(Long id, Double value) {
        log.debug("SERVICE ACCOUNT ID DEPOSIT: " + id + " VALUE: " + value);
        try {
            Account account = accountDao.deposit(accountDao.getById(id), value);
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - ACCOUNT DEPOSIT: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * This method is used to withdraw the amount from the account and update the data in database by DAO.
     *
     * @param id    expected account id from which the amount will be withdrawn.
     * @param value expected amount to be debited from the account.
     * @return updated AccountDto object.
     */
    @Override
    public AccountDto withdraw(Long id, Double value) {
        log.debug("SERVICE ACCOUNT ID WITHDRAW: " + id + " VALUE: " + value);
        try {
            Account account = accountDao.withdraw(accountDao.getById(id), value);
            return mapper.mapToAccountDto(account);
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - ACCOUNT WITHDRAW: " + e.getMessage());
            throw new ServiceException(e);
        }
    }
}
