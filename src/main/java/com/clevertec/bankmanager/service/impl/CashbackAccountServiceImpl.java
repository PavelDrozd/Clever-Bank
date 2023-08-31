package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.config.ConfigurationYamlManager;
import com.clevertec.bankmanager.service.CashbackAccountService;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.entity.Account;

import java.time.LocalDate;
import java.util.List;

public class CashbackAccountServiceImpl implements CashbackAccountService {

    private final AccountDao accountDao;
    private final Double cashbackPercent = Double.parseDouble(
            ConfigurationYamlManager.INSTANCE.getProperty("cashback.percent"));
    private boolean isTestCashback = Boolean.parseBoolean(
            ConfigurationYamlManager.INSTANCE.getProperty("cashback.test"));

    public CashbackAccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void run() {
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
