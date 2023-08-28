package com.clevertec.bankmanager.store.dao.factory;

import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.dao.BankDao;
import com.clevertec.bankmanager.store.dao.TransactionDao;
import com.clevertec.bankmanager.store.dao.UserDao;
import com.clevertec.bankmanager.store.dao.connection.DataSourceManager;
import com.clevertec.bankmanager.store.dao.impl.AccountDaoImpl;
import com.clevertec.bankmanager.store.dao.impl.BankDaoImpl;
import com.clevertec.bankmanager.store.dao.impl.TransactionDaoImpl;
import com.clevertec.bankmanager.store.dao.impl.UserDaoImpl;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public enum DaoFactory {
    INSTANCE;
    private final Map<Class<?>, Object> map;

    DaoFactory() {
        map = new HashMap<>();
        DataSource dataSource = DataSourceManager.INSTANCE.getDataSource();

        map.put(UserDao.class, new UserDaoImpl(dataSource));
        map.put(AccountDao.class, new AccountDaoImpl(dataSource));
        map.put(BankDao.class, new BankDaoImpl(dataSource));
        map.put(TransactionDao.class, new TransactionDaoImpl(dataSource));
    }

    @SuppressWarnings("unchecked")
    public <T> T getDao(Class<T> clazz) {
        T dao = (T) map.get(clazz);
        if (dao == null) {
            throw new RuntimeException("Class " + clazz + " is not constructed.");
        }
        return dao;
    }
}
