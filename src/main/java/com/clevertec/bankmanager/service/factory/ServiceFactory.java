package com.clevertec.bankmanager.service.factory;

import com.clevertec.bankmanager.service.AccountService;
import com.clevertec.bankmanager.service.BankService;
import com.clevertec.bankmanager.service.TransactionService;
import com.clevertec.bankmanager.service.UserService;
import com.clevertec.bankmanager.service.impl.AccountServiceImpl;
import com.clevertec.bankmanager.service.impl.BankServiceImpl;
import com.clevertec.bankmanager.service.impl.TransactionServiceImpl;
import com.clevertec.bankmanager.service.impl.UserServiceImpl;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.dao.BankDao;
import com.clevertec.bankmanager.store.dao.TransactionDao;
import com.clevertec.bankmanager.store.dao.UserDao;
import com.clevertec.bankmanager.store.dao.factory.DaoFactory;

import java.util.HashMap;
import java.util.Map;

public enum ServiceFactory {
    INSTANCE;

    private final Map<Class<?>, Object> map;

    ServiceFactory() {
        map = new HashMap<>();
        map.put(UserService.class, new UserServiceImpl(DaoFactory.INSTANCE.getDao(UserDao.class)));
        map.put(AccountService.class, new AccountServiceImpl(DaoFactory.INSTANCE.getDao(AccountDao.class)));
        map.put(BankService.class, new BankServiceImpl(DaoFactory.INSTANCE.getDao(BankDao.class)));
        map.put(TransactionService.class, new TransactionServiceImpl(
                DaoFactory.INSTANCE.getDao(TransactionDao.class), DaoFactory.INSTANCE.getDao(AccountDao.class)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) map.get(clazz);
    }
}
