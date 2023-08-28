package com.clevertec.bankmanager.store.dao.connection;

import com.clevertec.bankmanager.config.ConfigurationYamlManager;
import com.clevertec.bankmanager.shared.exception.store.dao.connection.ConnectionException;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataSourceManager {

    public final static DataSourceManager INSTANCE = new DataSourceManager();
    private static final String DB_DRIVER = "database.driver";
    private static final String DB_URL = "database.url";
    private static final String DB_USER = "database.user";
    private static final String DB_PASSWORD = "database.password";
    private static final String DB_MIN_POOL = "database.min";
    private static final String DB_MAX_POOL = "database.max";
    private static final String DB_AUTO_COMMIT = "database.autocommit";
    private static final String DB_LOGIN_TIMEOUT = "database.timeout";
    private final HikariDataSource dataSource;

    private DataSourceManager() {
        try {
            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(ConfigurationYamlManager.INSTANCE.getProperty(DB_DRIVER));
            dataSource.setJdbcUrl(ConfigurationYamlManager.INSTANCE.getProperty(DB_URL));
            dataSource.setUsername(ConfigurationYamlManager.INSTANCE.getProperty(DB_USER));
            dataSource.setPassword(ConfigurationYamlManager.INSTANCE.getProperty(DB_PASSWORD));
            dataSource.setMinimumIdle(Integer.parseInt(ConfigurationYamlManager.INSTANCE.getProperty(DB_MIN_POOL)));
            dataSource.setMaximumPoolSize(Integer.parseInt(ConfigurationYamlManager.INSTANCE.getProperty(DB_MAX_POOL)));
            dataSource.setAutoCommit(Boolean.parseBoolean(ConfigurationYamlManager.INSTANCE.getProperty(DB_AUTO_COMMIT)));
            dataSource.setLoginTimeout(Integer.parseInt(ConfigurationYamlManager.INSTANCE.getProperty(DB_LOGIN_TIMEOUT)));
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
