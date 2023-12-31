package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.data.dto.TransactionDto;
import com.clevertec.bankmanager.service.TransactionService;
import com.clevertec.bankmanager.shared.exception.service.ServiceException;
import com.clevertec.bankmanager.shared.exception.service.ServiceValidationException;
import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.shared.util.mapper.EntityDtoMapper;
import com.clevertec.bankmanager.shared.util.writer.ChequeWriter;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.dao.TransactionDao;
import com.clevertec.bankmanager.store.entity.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of service interface for process transaction DTO objects.
 */
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    /** TransactionDao is used to get objects from DAO module. */
    private final TransactionDao transactionDao;
    /** AccountDao is used to get objects from DAO module. */
    private final AccountDao accountDao;
    /** Mapper for mapping DTO and entity objects. */
    private final EntityDtoMapper mapper;

    /**
     * This constructor use TransactionDao, AccountDao implementation and set instance of mapper from EntityDtoMapper.
     *
     * @param transactionDao expected TransactionDao implementation class.
     * @param accountDao     expected AccountDao implementation class.
     */
    public TransactionServiceImpl(TransactionDao transactionDao, AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
        this.mapper = EntityDtoMapper.getInstance();
    }

    /**
     * Method for create new DTO class and transfer it to database by using DAO.
     *
     * @param transactionDto expected object of type TransactionDto to create it.
     * @return new created TransactionDto object.
     */
    @Override
    public TransactionDto create(TransactionDto transactionDto) {
        log.debug("SERVICE CREATE TRANSACTION: " + transactionDto);
        try {
            transactionDto.setDateTime(LocalDateTime.now());
            Transaction transaction = transactionDao.create(mapper.mapToTransaction(transactionDto));
            TransactionDto created = mapper.mapToTransactionDto(transaction);
            ChequeWriter.writeCheque(created);
            return created;
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - CREATE USER: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method for getting all transaction DTO objects from DAO.
     *
     * @return List of TransactionDto objects.
     */
    @Override
    public List<TransactionDto> getAll() {
        log.debug("SERVICE GET ALL TRANSACTIONS ");
        try {
            return transactionDao.getAll().stream().map(mapper::mapToTransactionDto).collect(Collectors.toList());
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - GET ALL USERS: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method get transaction DTO object from DAO by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return TransactionDto object.
     */
    @Override
    public TransactionDto getById(Long id) {
        log.debug("SERVICE GET TRANSACTION BY ID: " + id);
        try {
            if (transactionDao.getById(id) == null) {
                log.error("SERVICE VALIDATION EXCEPTION - CREATE USER ");
                throw new ServiceValidationException("Transaction with ID:" + id + " is null.");
            }
            return mapper.mapToTransactionDto(transactionDao.getById(id));
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - GET USER BY ID: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method update transaction DTO in database by using DAO.
     *
     * @param transactionDto expected updated object of type TransactionDto.
     * @return updated TransactionDto object.
     */
    @Override
    public TransactionDto update(TransactionDto transactionDto) {
        log.debug("SERVICE UPDATE TRANSACTION: " + transactionDto);
        try {
            Transaction transaction = transactionDao.update(mapper.mapToTransaction(transactionDto));
            TransactionDto updated = mapper.mapToTransactionDto(transaction);
            ChequeWriter.writeCheque(updated);
            return updated;
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - CREATE USER: " + e.getMessage());
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
        log.debug("SERVICE DELETE TRANSACTION BY ID: " + id);
        try {
            if (!transactionDao.delete(id)) {
                log.error("SERVICE VALIDATION EXCEPTION - DELETE TRANSACTION BY ID ");
                throw new ServiceValidationException("Can't delete transaction by id: " + id);
            }
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - DELETE TRANSACTION BY ID: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * This method is used to make transactions to transfer the amount from one account to the second.
     *
     * @param senderAccountId    expected Long account id who sent amount.
     * @param recipientAccountId expected Long account id who receive amount.
     * @param value              expected transaction amount.
     * @return new TransactionDto object.
     */
    @Override
    public TransactionDto transfer(Long senderAccountId, Long recipientAccountId, Double value) {
        log.debug("SERVICE TRANSFER FROM ID: " + senderAccountId + " TO ID: " + recipientAccountId + " VALUE " + value);
        try {
            Transaction transaction = accountDao.transfer(
                    accountDao.getById(senderAccountId), accountDao.getById(recipientAccountId), value);
            return create(mapper.mapToTransactionDto(transaction));
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - TRANSFER: " + e.getMessage());
            throw new ServiceException("The transfer for the amount of " + value + " was canceled by reason: " + e);
        }
    }
}
