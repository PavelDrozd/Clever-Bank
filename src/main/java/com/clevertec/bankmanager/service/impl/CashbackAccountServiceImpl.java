package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.config.ConfigurationYamlManager;
import com.clevertec.bankmanager.service.CashbackAccountService;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of service interface for process cashback in accounts.
 */
@RequiredArgsConstructor
@Slf4j
public class CashbackAccountServiceImpl implements CashbackAccountService {

    /** AccountDao is used to get objects from DAO module. */
    private final AccountDao accountDao;
    /** Cashback percent property in configuration file. */
    private final Double cashbackPercent = Double.parseDouble(
            ConfigurationYamlManager.INSTANCE.getProperty("cashback.percent"));
    /**
     * Test mode property in configuration file.
     * If it TRUE start will add cashback to all accounts.
     */
    private boolean isTestCashback = Boolean.parseBoolean(
            ConfigurationYamlManager.INSTANCE.getProperty("cashback.test"));

    /**
     * Method get all accounts from AccountDao and check their amount and cashback last date.
     * If account have amount valid amount for cashback and last cashback time were in past month
     * then use cashback method by AccountDao to give them cashback
     */
    @Override
    public void run() {
        log.trace("SERVICE CASHBACK ");
        LocalDate dateNow = LocalDate.now();
        List<Account> accounts = accountDao.getAll();
        for (Account account : accounts) {
            Double amount = account.getAmount();
            if (amount > 1) {
                LocalDate cashbackLastDate = account.getCashbackLastDate();
                if (!cashbackLastDate.getMonth().equals(dateNow.getMonth()) || isTestCashback) {
                    int daysInMoth = dateNow.lengthOfMonth();
                    if (dateNow.getDayOfMonth() == daysInMoth || isTestCashback) {
                        accountDao.cashback(account, dateNow, cashbackPercent);
                    }
                }
            }
        }
        isTestCashback = false;
    }
}
