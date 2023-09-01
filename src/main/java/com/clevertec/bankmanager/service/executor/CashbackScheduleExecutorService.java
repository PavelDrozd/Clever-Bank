package com.clevertec.bankmanager.service.executor;

import com.clevertec.bankmanager.config.ConfigurationYamlManager;
import com.clevertec.bankmanager.service.impl.CashbackAccountServiceImpl;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.dao.factory.DaoFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class used as box of ScheduleExecutorService and created for initialize CashbackAccountService
 * by SingleThreadScheduleExecutor.
 * CashbackScheduleExecutorService use singleton pattern with thread safe structure.
 */
public class CashbackScheduleExecutorService {

    /** ScheduledExecutorService needed for the periodic launch of the class. */
    public final ScheduledExecutorService scheduler;

    /**
     * Private constructor use ConfigurationYamlManager for get properties to initialize CashbackAccountServiceImpl
     * by executor.
     * Use DaoFactory for initialize service impl class with schedule at fixed rate.
     */
    private CashbackScheduleExecutorService() {
        long delay = Long.parseLong(ConfigurationYamlManager.INSTANCE.getProperty("cashback.delay"));
        long period = Long.parseLong(ConfigurationYamlManager.INSTANCE.getProperty("cashback.period"));
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new CashbackAccountServiceImpl(
                DaoFactory.INSTANCE.getDao(AccountDao.class)), delay, period, TimeUnit.SECONDS);
    }

    /**
     * Private static method for hold instance.
     */
    private static final class InstanceHolder {
        private static final CashbackScheduleExecutorService instance = new CashbackScheduleExecutorService();
    }

    /**
     * Public static method for getting instance of this class.
     *
     * @return instance of this class.
     */
    public static CashbackScheduleExecutorService getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * Public method for shutdown ScheduledExecutorService class.
     */
    public void shutdown() {
        scheduler.shutdown();
    }
}
