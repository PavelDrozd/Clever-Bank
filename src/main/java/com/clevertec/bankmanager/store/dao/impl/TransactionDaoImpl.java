package com.clevertec.bankmanager.store.dao.impl;

import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.store.dao.TransactionDao;
import com.clevertec.bankmanager.store.entity.Transaction;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

    private static final String SELECT_TRANSACTION = "SELECT t.id, t.amount t.recipient_id t.sender_id ";
    private static final String FROM_TRANSACTION = "FROM transactions t ";
    private static final String INSERT_TRANSACTION = "INSERT INTO transactions (name) VALUES (?)";
    private static final String SELECT_TRANSACTION_BY_ID = SELECT_TRANSACTION + FROM_TRANSACTION + "WHERE t.id = ?";
    private static final String SELECT_ALL_TRANSACTIONS = SELECT_TRANSACTION + FROM_TRANSACTION;
    private static final String UPDATE_TRANSACTION = "UPDATE transactions SET name = ? WHERE id = ? ";
    private static final String DELETE_TRANSACTION = "DELETE transactions t WHERE t.id = ?";

    private final DataSource dataSource;

    @Override
    public Transaction create(Transaction transaction) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(//
                    INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
            statement.setDouble(1, transaction.getAmount());
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

    @Override
    public Transaction update(Transaction transaction) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION);
            statement.setDouble(1, transaction.getAmount());
            statement.executeUpdate();
            return getById(transaction.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

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

    private Transaction processTransaction(ResultSet result) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(result.getLong("id"));
        transaction.setAmount(result.getDouble("amount"));
        return transaction;
    }
}
