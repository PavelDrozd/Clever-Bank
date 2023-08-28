package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.data.dto.BankDto;
import com.clevertec.bankmanager.service.BankService;
import com.clevertec.bankmanager.shared.exception.service.ServiceException;
import com.clevertec.bankmanager.shared.exception.service.ServiceValidationException;
import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.shared.util.mapper.EntityDtoMapper;
import com.clevertec.bankmanager.store.dao.BankDao;
import com.clevertec.bankmanager.store.entity.Bank;

import java.util.List;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final BankDao bankDao;
    private final EntityDtoMapper mapper;

    public BankServiceImpl(BankDao bankDao) {
        this.bankDao = bankDao;
        mapper = EntityDtoMapper.getInstance();
    }

    @Override
    public BankDto create(BankDto bankDto) {
        try {
            Bank bank = bankDao.create(mapper.mapToBank(bankDto));
            return mapper.mapToBankDto(bank);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BankDto> getAll() {
        try {
            return bankDao.getAll().stream().map(mapper::mapToBankDto).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public BankDto getById(Long id) {
        try {
            if (bankDao.getById(id) == null) {
                throw new ServiceValidationException("Bank with ID:" + id + " is null.");
            }
            return mapper.mapToBankDto(bankDao.getById(id));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public BankDto update(BankDto bankDto) {
        try {
            Bank bank = bankDao.update(mapper.mapToBank(bankDto));
            return mapper.mapToBankDto(bank);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            if (bankDao.delete(id)) {
                throw new ServiceValidationException("Can't delete bank by id: " + id);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
