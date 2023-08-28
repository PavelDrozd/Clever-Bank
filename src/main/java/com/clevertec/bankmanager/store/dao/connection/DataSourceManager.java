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
    private final ConfigurationYamlManager yaml = ConfigurationYamlManager.INSTANCE;

    private DataSourceManager() {
        try {
            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(yaml.getProperty(DB_DRIVER));
            dataSource.setJdbcUrl(yaml.getProperty(DB_URL));
            dataSource.setUsername(yaml.getProperty(DB_USER));
            dataSource.setPassword(yaml.getProperty(DB_PASSWORD));
            dataSource.setMinimumIdle(Integer.parseInt(yaml.getProperty(DB_MIN_POOL)));
            dataSource.setMaximumPoolSize(Integer.parseInt(yaml.getProperty(DB_MAX_POOL)));
            dataSource.setAutoCommit(Boolean.parseBoolean(yaml.getProperty(DB_AUTO_COMMIT)));
            dataSource.setLoginTimeout(Integer.parseInt(yaml.getProperty(DB_LOGIN_TIMEOUT)));
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void close(){
        dataSource.close();
    }
}
