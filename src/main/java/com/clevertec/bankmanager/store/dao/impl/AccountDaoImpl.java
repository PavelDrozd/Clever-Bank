package com.clevertec.bankmanager.store.dao.impl;

import com.clevertec.bankmanager.shared.exception.store.dao.DaoConcurrencyException;
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
import java.util.concurrent.TimeUnit;

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
    public Account deposit(Account account, Double value) {
        Account current = getById(account.getId());
        try {
            if (current.getLock().tryLock(10, TimeUnit.SECONDS)) {
                Double currentAmount = current.getAmount();
                current.setAmount(currentAmount + value);
                return update(current);
            }
        } catch (InterruptedException e) {
            throw new DaoException(e);
        } finally {
            current.getLock().unlock();
        }
        throw new DaoConcurrencyException("Waiting lock");
    }

    @Override
    public Account withdraw(Account account, Double value) {
        Account current = getById(account.getId());
        try {
            if (current.getLock().tryLock(10, TimeUnit.SECONDS)) {
                Double currentAmount = current.getAmount();
                if (currentAmount < value) {
                    throw new DaoValidationException("Amount on account less than withdraw value: " + value);
                }
                current.setAmount(currentAmount - value);
                return update(current);
            }
        } catch (InterruptedException e) {
            throw new DaoException(e);
        } finally {
            current.getLock().unlock();
        }
        throw new DaoConcurrencyException("Waiting lock");
    }

    @Override
    public Transaction transfer(Account senderAccount, Account recipientAccount, Double value) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            Transaction transaction = new Transaction();
            if (senderAccount.getLock().tryLock(10, TimeUnit.SECONDS)) {
                try {
                    if (recipientAccount.getLock().tryLock(10, TimeUnit.SECONDS)) {
                        try {
                            transaction.setAmount(value);
                            transaction.setSenderAccount(withdraw(senderAccount, value));
                            transaction.setRecipientAccount(deposit(recipientAccount, value));
                            return transaction;
                        } finally {
                            recipientAccount.getLock().unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new DaoConcurrencyException(e);
                } finally {
                    senderAccount.getLock().unlock();
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } catch (InterruptedException e) {
            throw new DaoConcurrencyException(e);
        } finally {
            setAutoCommitTrue(connection);
        }
        throw new DaoConcurrencyException("Waiting lock");
    }


    @Override
    public Account cashback(Account account, LocalDate cashbackLastDate, Double percent) {
        Account current = getById(account.getId());
        try {
            if (current.getLock().tryLock(10, TimeUnit.SECONDS)) {
                double currentAmount = getValidAmount(current);
                double cashback = (currentAmount / 100) * percent;
                current.setAmount(Double.sum(currentAmount, percent));
                current.setCashbackLastDate(cashbackLastDate);
                return update(current);
            }
        } catch (InterruptedException e) {
            throw new DaoConcurrencyException(e);
        } finally {
            current.getLock().unlock();
        }
        throw new DaoConcurrencyException("Waiting lock");
    }

    private Double getValidAmount(Account current) {
        double currentAmount;
        if (current.getAmount() < 0) {
            currentAmount = 0;
        } else {
            currentAmount = current.getAmount();
        }
        return currentAmount;
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
