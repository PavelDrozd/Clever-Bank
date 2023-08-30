package com.clevertec.bankmanager.service.executor;

import com.clevertec.bankmanager.config.ConfigurationYamlManager;
import com.clevertec.bankmanager.service.impl.CashbackAccountServiceImpl;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.dao.factory.DaoFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CashbackScheduleExecutorService {
    public final ScheduledExecutorService scheduler;

    private CashbackScheduleExecutorService() {
        long delay = Long.parseLong(ConfigurationYamlManager.INSTANCE.getProperty("cashback.delay"));
        long period = Long.parseLong(ConfigurationYamlManager.INSTANCE.getProperty("cashback.period"));
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new CashbackAccountServiceImpl(
                DaoFactory.INSTANCE.getDao(AccountDao.class)), delay, period, TimeUnit.SECONDS);
    }

    private static final class InstanceHolder {
        private static final CashbackScheduleExecutorService instance = new CashbackScheduleExecutorService();
    }

    public static CashbackScheduleExecutorService getInstance() {
        return InstanceHolder.instance;
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
