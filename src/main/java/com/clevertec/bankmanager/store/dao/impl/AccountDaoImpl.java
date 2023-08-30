package com.clevertec.bankmanager.store.dao.impl;

import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.shared.exception.store.dao.DaoValidationException;
import com.clevertec.bankmanager.store.dao.AccountDao;
import com.clevertec.bankmanager.store.dao.BankDao;
import com.clevertec.bankmanager.store.dao.UserDao;
import com.clevertec.bankmanager.store.entity.Account;
import com.clevertec.bankmanager.store.entity.Transaction;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AccountDaoImpl implements AccountDao {

    private static final String SELECT_ACCOUNT = "SELECT a.id, a.number, a.amount, a.cashback_last_date, a.bank_id, a.user_id ";
    private static final String FROM_ACCOUNT = "FROM accounts a ";
    private static final String INSERT_ACCOUNT = "INSERT INTO accounts (number, amount, cashback_last_date, bank_id, user_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ACCOUNT_BY_ID = SELECT_ACCOUNT + FROM_ACCOUNT + "WHERE a.id = ?";
    private static final String SELECT_ALL_ACCOUNTS = SELECT_ACCOUNT + FROM_ACCOUNT;
    private static final String UPDATE_ACCOUNT = "UPDATE accounts SET number = ?, amount = ?, cashback_last_date = ?, bank_id = ?, user_id = ? WHERE id = ? ";
    private static final String DELETE_ACCOUNT = "DELETE FROM accounts a WHERE a.id = ?";

    private final DataSource dataSource;
    private final BankDao bankDao;
    private final UserDao userDao;

    @Override
    public Account create(Account account) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(//
                    INSERT_ACCOUNT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, account.getNumber());
            statement.setDouble(2, account.getAmount());
            statement.setObject(3, account.getCashbackLastDate());
            statement.setLong(4, account.getBank().getId());
            statement.setLong(5, account.getUser().getId());
            statement.executeUpdate();
            ResultSet key = statement.getGeneratedKeys();
            Account created = new Account();
            if (key.next()) {
                created = getById(key.getLong("id"));
            }
            return created;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ACCOUNTS);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                accounts.add(processAccount(result));
            }
            return accounts;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Account getById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_BY_ID);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            Account account = new Account();
            if (result.next()) {
                account = processAccount(result);
            }
            return account;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Account update(Account account) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT);
            statement.setLong(1, account.getNumber());
            statement.setDouble(2, account.getAmount());
            statement.setObject(3, account.getCashbackLastDate());
            statement.setLong(4, account.getBank().getId());
            statement.setLong(5, account.getUser().getId());
            statement.setLong(6, account.getId());
            statement.executeUpdate();
            return getById(account.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_ACCOUNT);
            statement.setLong(1, id);
            int rowDeleted = statement.executeUpdate();
            return rowDeleted == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Account deposit(Long id, Double value) {
        Account account = getById(id);
        Double currentAmount = account.getAmount();
        account.setAmount(currentAmount + value);
        return update(account);
    }

    @Override
    public Account withdraw(Long id, Double value) {
        Account account = getById(id);
        Double currentAmount = account.getAmount();
        if (currentAmount < value) {
            throw new DaoValidationException("Amount on account less than withdraw value: " + value);
        }
        account.setAmount(currentAmount - value);
        return update(account);

    }

    @Override
    public Transaction transfer(Long senderAccountId, Long recipientAccountId, Double value) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            Transaction transaction = new Transaction();
            transaction.setAmount(value);
            transaction.setSenderAccount(withdraw(senderAccountId, value));
            transaction.setRecipientAccount(deposit(recipientAccountId, value));
            return transaction;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            setAutoCommitTrue(connection);
        }
    }

    private void setAutoCommitTrue(Connection connection) {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Account processAccount(ResultSet result) throws SQLException {
        Account account = new Account();
        account.setId(result.getLong("id"));
        account.setNumber(result.getLong("number"));
        account.setAmount(result.getDouble("amount"));
        account.setCashbackLastDate(result.getObject("cashback_last_date", LocalDate.class));
        account.setBank(bankDao.getById(result.getLong("bank_id")));
        account.setUser(userDao.getById(result.getLong("user_id")));
        return account;
    }
}
