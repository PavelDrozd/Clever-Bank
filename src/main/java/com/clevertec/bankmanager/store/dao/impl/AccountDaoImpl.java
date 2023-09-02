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

/**
 * Implementation of DAO interface for process account objects.
 * Using datasource for connect to the database.
 */
@RequiredArgsConstructor
public class AccountDaoImpl implements AccountDao {

    /** SELECT part of query with account parameters in the database. */
    private static final String SELECT_ACCOUNT = "SELECT a.id, a.number, a.amount, a.cashback_last_date, a.bank_id, a.user_id ";
    /** FROM part of query with accounts table. */
    private static final String FROM_ACCOUNT = "FROM accounts a ";
    /** INSERT query to create a new row in the database. */
    private static final String INSERT_ACCOUNT = "INSERT INTO accounts (number, amount, cashback_last_date, bank_id, user_id) VALUES (?, ?, ?, ?, ?)";
    /** SELECT query to find account by ID */
    private static final String SELECT_ACCOUNT_BY_ID = SELECT_ACCOUNT + FROM_ACCOUNT + "WHERE a.id = ? AND a.deleted = false";
    /** SELECT query to get all accounts from the database */
    private static final String SELECT_ALL_ACCOUNTS = SELECT_ACCOUNT + FROM_ACCOUNT + "WHERE a.deleted = false";
    /** UPDATE query for set new values in fields of account entity. */
    private static final String UPDATE_ACCOUNT = "UPDATE accounts SET number = ?, amount = ?, cashback_last_date = ?, bank_id = ?, user_id = ? WHERE id = ? AND deleted = false";
    /** DELETE query by set deleted value true in account row by ID from the database. */
    private static final String DELETE_ACCOUNT = "UPDATE accounts a SET  deleted = true WHERE a.id = ?";

    /** DataSource for create connection with database. */
    private final DataSource dataSource;
    /** Used bank DAO for bank entity as part of account entity. */
    private final BankDao bankDao;
    /** Used user DAO for user entity as part of account entity. */
    private final UserDao userDao;

    /**
     * Method for create new entity in database.
     *
     * @param account expected object of type Account to create it.
     * @return new created Account object.
     */
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

    /**
     * Method for getting all account entities from database.
     *
     * @return List of Account objects.
     */
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

    /**
     * Method find entity in database by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return Account object.
     */
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

    /**
     * Method update entity data in database.
     *
     * @param account expected updated object of type Account.
     * @return updated Account object.
     */
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

    /**
     * Method delete row in database by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return boolean value as result of deleted row.
     */
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

    /**
     * This method is used to deposit the amount to the account and update data in database.
     *
     * @param account expected the account to which the amount is deposited.
     * @param value   expected amount to be credited to the account.
     * @return updated Account object.
     */
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

    /**
     * This method is used to withdraw the amount from the account and update the data in database.
     *
     * @param account expected the account from which the amount will be withdrawn.
     * @param value   expected amount to be debited from the account.
     * @return updated account with new amount from database.
     */
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

    /**
     * This method is used to make transactions to transfer the amount from one account to the second.
     *
     * @param senderAccount    expected the account who sent amount.
     * @param recipientAccount expected the account who receive amount.
     * @param value            expected transaction amount.
     * @return new transaction object.
     */
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

    /**
     * This method add a percent of amount on account.
     *
     * @param account          expected the account for cashback.
     * @param cashbackLastDate expected the date for last cashback.
     * @param percent          expected the percent of cashback.
     * @return updated account with new amount from database.
     */
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

    /**
     * Method check amount value of account for cashback.
     *
     * @param current expected Account object.
     * @return Double value of amount.
     */
    private Double getValidAmount(Account current) {
        double currentAmount;
        if (current.getAmount() < 0) {
            currentAmount = 0;
        } else {
            currentAmount = current.getAmount();
        }
        return currentAmount;
    }

    /**
     * Method for setting auto commit parameter true.
     *
     * @param connection expected used Connection object.
     */
    private void setAutoCommitTrue(Connection connection) {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Method for processing Account object to create a new.
     *
     * @param result expected ResultSet object.
     * @return new Account object.
     * @throws SQLException default exception by using ResultSet methods.
     */
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
