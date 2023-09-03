package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.data.dto.BankDto;
import com.clevertec.bankmanager.service.BankService;
import com.clevertec.bankmanager.shared.exception.service.ServiceException;
import com.clevertec.bankmanager.shared.exception.service.ServiceValidationException;
import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.shared.util.mapper.EntityDtoMapper;
import com.clevertec.bankmanager.store.dao.BankDao;
import com.clevertec.bankmanager.store.entity.Bank;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of service interface for process bank DTO objects.
 */
@Slf4j
public class BankServiceImpl implements BankService {

    /** BankDao is used to get objects from DAO module. */
    private final BankDao bankDao;
    /** Mapper for mapping DTO and entity objects. */
    private final EntityDtoMapper mapper;

    /**
     * This constructor use BankDao implementation and set instance of mapper from EntityDtoMapper.
     * @param accountDao expected BankDao implementation class.
     */
    public BankServiceImpl(BankDao bankDao) {
        this.bankDao = bankDao;
        mapper = EntityDtoMapper.getInstance();
    }

    /**
     * Method for create new DTO class and transfer it to database by using DAO.
     * @param bankDto expected object of type BankDto to create it.
     * @return new created BankDto object.
     */
    @Override
    public BankDto create(BankDto bankDto) {
        log.debug("SERVICE CREATE BANK: " + bankDto);
        try {
            Bank bank = bankDao.create(mapper.mapToBank(bankDto));
            return mapper.mapToBankDto(bank);
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - CREATE BANK: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method for getting all bank DTO objects from DAO.
     * @return List of BankDto objects.
     */
    @Override
    public List<BankDto> getAll() {
        log.debug("SERVICE GET ALL BANKS: ");
        try {
            return bankDao.getAll().stream().map(mapper::mapToBankDto).collect(Collectors.toList());
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - GET ALL BANKS: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method get bank DTO object from DAO by ID.
     * @param id expected object of type Long used as primary key.
     * @return BankDto object.
     */
    @Override
    public BankDto getById(Long id) {
        log.debug("SERVICE GET BANK BY ID: " + id);
        try {
            if (bankDao.getById(id) == null) {
                log.error("SERVICE VALIDATION EXCEPTION - GET BANK BY ID ");
                throw new ServiceValidationException("Bank with ID:" + id + " is null.");
            }
            return mapper.mapToBankDto(bankDao.getById(id));
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - GET BANK BY ID: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method update bank DTO in database by using DAO.
     * @param bankDto expected updated object of type BankDto.
     * @return updated BankDto object.
     */
    @Override
    public BankDto update(BankDto bankDto) {
        log.debug("SERVICE UPDATE BANK: " + bankDto);
        try {
            Bank bank = bankDao.update(mapper.mapToBank(bankDto));
            return mapper.mapToBankDto(bank);
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - UPDATE BANK: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method delete object by using DAO by ID.
     * @param id expected object of type Long used as primary key.
     */
    @Override
    public void delete(Long id) {
        log.debug("SERVICE DELETE BANK BY ID: " + id);
        try {
            if (!bankDao.delete(id)) {
                log.error("SERVICE VALIDATION EXCEPTION - DELETE BANK BY ID ");
                throw new ServiceValidationException("Can't delete bank by id: " + id);
            }
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - DELETE BANK BY ID: " + e.getMessage());
            throw new ServiceException(e);
        }
    }
}
