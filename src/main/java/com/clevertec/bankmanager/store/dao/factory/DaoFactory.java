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

/**
 * This enum used as factory for initialize DAO implementation classes.
 * Enum values are available globally, and used as a singleton.
 */
public enum DaoFactory {
    INSTANCE;

    /** Map for store DAO classes */
    private final Map<Class<?>, Object> map;

    /**
     * Initialize HashMap, get DataSource instance from DataSourceManager and use in for initialize DAO implementation
     * classes, put them into the map.
     */
    DaoFactory() {
        map = new HashMap<>();
        DataSource dataSource = DataSourceManager.INSTANCE.getDataSource();

        map.put(UserDao.class, new UserDaoImpl(dataSource));
        map.put(BankDao.class, new BankDaoImpl(dataSource));
        map.put(AccountDao.class, new AccountDaoImpl(dataSource, getDao(BankDao.class), getDao(UserDao.class)));
        map.put(TransactionDao.class, new TransactionDaoImpl(dataSource, getDao(AccountDao.class)));
    }

    /**
     * Public method for get DAO class from factory.
     *
     * @param clazz expected object class type of T
     * @param <T>   expected type T
     * @return object of type T
     */
    @SuppressWarnings("unchecked")
    public <T> T getDao(Class<T> clazz) {
        T dao = (T) map.get(clazz);
        if (dao == null) {
            throw new RuntimeException("Class " + clazz + " is not constructed.");
        }
        return dao;
    }
}
