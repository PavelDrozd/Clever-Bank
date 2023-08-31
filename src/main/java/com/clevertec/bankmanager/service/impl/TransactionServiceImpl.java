package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.data.dto.AccountDto;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;
    private final EntityDtoMapper mapper;

    public TransactionServiceImpl(TransactionDao transactionDao, AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
        this.mapper = EntityDtoMapper.getInstance();
    }

    @Override
    public TransactionDto create(TransactionDto transactionDto) {
        try {
            transactionDto.setDateTime(LocalDateTime.now());
            Transaction transaction = transactionDao.create(mapper.mapToTransaction(transactionDto));
            TransactionDto created = mapper.mapToTransactionDto(transaction);
            ChequeWriter.writeCheck(created);
            return created;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<TransactionDto> getAll() {
        try {
            return transactionDao.getAll().stream().map(mapper::mapToTransactionDto).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TransactionDto getById(Long id) {
        try {
            if (transactionDao.getById(id) == null) {
                throw new ServiceValidationException("Transaction with ID:" + id + " is null.");
            }
            return mapper.mapToTransactionDto(transactionDao.getById(id));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TransactionDto update(TransactionDto transactionDto) {
        try {
            Transaction transaction = transactionDao.update(mapper.mapToTransaction(transactionDto));
            TransactionDto updated = mapper.mapToTransactionDto(transaction);
            ChequeWriter.writeCheck(updated);
            return updated;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            if (transactionDao.delete(id)) {
                throw new ServiceValidationException("Can't delete bank by id: " + id);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TransactionDto transfer(AccountDto senderAccount, AccountDto recipientAccount, Double value) {
        try {
            Transaction transaction = accountDao.transfer(
                    mapper.mapToAccount(senderAccount), mapper.mapToAccount(recipientAccount), value);
            return create(mapper.mapToTransactionDto(transaction));
        } catch (DaoException e) {
            throw new ServiceException("The transfer for the amount of " + value + " was canceled by reason: " + e);
        }
    }
}
