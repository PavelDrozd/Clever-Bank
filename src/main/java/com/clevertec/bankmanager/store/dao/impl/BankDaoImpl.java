package com.clevertec.bankmanager.store.dao.impl;

import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.store.dao.BankDao;
import com.clevertec.bankmanager.store.entity.Bank;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of DAO interface for process bank objects.
 * Using datasource for connect to the database.
 */
@RequiredArgsConstructor
public class BankDaoImpl implements BankDao {

    /** SELECT part of query with bank parameters in the database. */
    private static final String SELECT_BANK = "SELECT b.id, b.name ";
    /** FROM part of query with banks table. */
    private static final String FROM_BANK = "FROM banks b ";
    /** INSERT query to create a new row in the database. */
    private static final String INSERT_BANK = "INSERT INTO banks (name) VALUES (?)";
    /** SELECT query to find bank by ID */
    private static final String SELECT_BANK_BY_ID = SELECT_BANK + FROM_BANK + "WHERE b.id = ?";
    /** SELECT query to get all banks from the database */
    private static final String SELECT_ALL_BANKS = SELECT_BANK + FROM_BANK;
    /** UPDATE query for set new values in fields of bank entity. */
    private static final String UPDATE_BANK = "UPDATE banks SET name = ? WHERE id = ? ";
    /** DELETE query for delete bank row by ID from the database. */
    private static final String DELETE_BANK = "DELETE FROM banks b WHERE b.id = ?";

    /** DataSource for create connection with database. */
    private final DataSource dataSource;

    /**
     * Method for create new entity in database.
     *
     * @param bank expected object of type Bank to create it.
     * @return new created Bank object.
     */
    @Override
    public Bank create(Bank bank) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(//
                    INSERT_BANK, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, bank.getName());
            statement.executeUpdate();
            ResultSet key = statement.getGeneratedKeys();
            Bank created = new Bank();
            if (key.next()) {
                created = getById(key.getLong("id"));
            }
            return created;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method for getting all bank entities from database.
     *
     * @return List of Bank objects.
     */
    @Override
    public List<Bank> getAll() {
        List<Bank> banks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BANKS);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                banks.add(processBank(result));
            }
            return banks;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method find entity in database by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return Bank object.
     */
    @Override
    public Bank getById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_BANK_BY_ID);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            Bank bank = new Bank();
            if (result.next()) {
                bank = processBank(result);
            }
            return bank;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method update entity data in database.
     *
     * @param bank expected updated object of type Bank.
     * @return updated Bank object.
     */
    @Override
    public Bank update(Bank bank) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_BANK);
            statement.setString(1, bank.getName());
            statement.setLong(2, bank.getId());
            statement.executeUpdate();
            return getById(bank.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method delete row in database by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return boolean value as result of deleted row.
     */
    @Override
    public boolean delete(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_BANK);
            statement.setLong(1, id);
            int rowDeleted = statement.executeUpdate();
            return rowDeleted == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method for processing Bank object to create a new.
     *
     * @param result expected ResultSet object.
     * @return new Bank object.
     * @throws SQLException default exception by using ResultSet methods.
     */
    private Bank processBank(ResultSet result) throws SQLException {
        Bank bank = new Bank();
        bank.setId(result.getLong("id"));
        bank.setName(result.getString("name"));
        return bank;
    }
}
