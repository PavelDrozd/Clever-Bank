package com.clevertec.bankmanager.store.dao.impl;

import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.dao.TransactionDao;
import com.clevertec.bankmanager.store.entity.Transaction;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of DAO interface for process transaction objects.
 * Using datasource for connect to the database.
 */
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

    /** SELECT part of query with transaction parameters in the database. */
    private static final String SELECT_TRANSACTION = "SELECT t.id, t.amount, t.date_time, t.recipient_id, t.sender_id ";
    /** FROM part of query with transactions table. */
    private static final String FROM_TRANSACTION = "FROM transactions t ";
    /** INSERT query to create a new row in the database. */
    private static final String INSERT_TRANSACTION =
            "INSERT INTO transactions (amount, date_time, recipient_id, sender_id) VALUES (?, ?, ?, ?)";
    /** SELECT query to find transaction by ID */
    private static final String SELECT_TRANSACTION_BY_ID = SELECT_TRANSACTION + FROM_TRANSACTION +
                                                           "WHERE t.id = ? AND t.deleted = false";
    /** SELECT query to get all transactions from the database */
    private static final String SELECT_ALL_TRANSACTIONS = SELECT_TRANSACTION + FROM_TRANSACTION +
                                                          "WHERE t.deleted = false";
    /** UPDATE query for set new values in fields of transaction entity. */
    private static final String UPDATE_TRANSACTION =
            "UPDATE transactions SET amount = ?, date_time = ?, recipient_id = ?, sender_id = ? " +
            "WHERE id = ? AND deleted = false";
    /** DELETE query by set deleted value true in transaction row by ID from the database. */
    private static final String DELETE_TRANSACTION = "UPDATE transactions t SET deleted = true WHERE t.id = ?";

    /** DataSource for create connection with database. */
    private final DataSource dataSource;
    /** Used account DAO for account entity as part of transaction entity. */
    private final AccountDao accountDao;

    /**
     * Method for create new entity in database.
     *
     * @param transaction expected object of type Transaction to create it.
     * @return new created Transaction object.
     */
    @Override
    public Transaction create(Transaction transaction) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(//
                    INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
            statement.setDouble(1, transaction.getAmount());
            statement.setObject(2, transaction.getDateTime());
            statement.setLong(3, transaction.getRecipientAccount().getId());
            statement.setLong(4, transaction.getSenderAccount().getId());
            statement.executeUpdate();
            ResultSet key = statement.getGeneratedKeys();
            Transaction created = new Transaction();
            if (key.next()) {
                created = getById(key.getLong("id"));
            }
            return created;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method for getting all transactions entities from database.
     *
     * @return List of Transaction objects.
     */
    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                transactions.add(processTransaction(result));
            }
            return transactions;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method find entity in database by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return Transaction object.
     */
    @Override
    public Transaction getById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTION_BY_ID);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            Transaction transaction = new Transaction();
            if (result.next()) {
                transaction = processTransaction(result);
            }
            return transaction;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method update entity data in database.
     *
     * @param transaction expected updated object of type Transaction.
     * @return updated Transaction object.
     */
    @Override
    public Transaction update(Transaction transaction) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION);
            statement.setDouble(1, transaction.getAmount());
            statement.setObject(2, transaction.getDateTime());
            statement.setLong(3, transaction.getRecipientAccount().getId());
            statement.setLong(4, transaction.getSenderAccount().getId());
            statement.setLong(5, transaction.getId());
            statement.executeUpdate();
            return getById(transaction.getId());
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
            PreparedStatement statement = connection.prepareStatement(DELETE_TRANSACTION);
            statement.setLong(1, id);
            int rowDeleted = statement.executeUpdate();
            return rowDeleted == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method for processing Transaction object to create a new.
     *
     * @param result expected ResultSet object.
     * @return new Transaction object.
     * @throws SQLException default exception by using ResultSet methods.
     */
    private Transaction processTransaction(ResultSet result) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(result.getLong("id"));
        transaction.setAmount(result.getDouble("amount"));
        transaction.setDateTime(result.getObject("date_time", LocalDateTime.class));
        transaction.setRecipientAccount(accountDao.getById(result.getLong("recipient_id")));
        transaction.setSenderAccount(accountDao.getById(result.getLong("sender_id")));
        return transaction;
    }
}
