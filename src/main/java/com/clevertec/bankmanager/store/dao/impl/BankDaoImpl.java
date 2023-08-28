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

@RequiredArgsConstructor
public class BankDaoImpl implements BankDao {

    private static final String SELECT_BANK = "SELECT b.id, b.name ";
    private static final String FROM_BANK = "FROM banks b ";
    private static final String INSERT_BANK = "INSERT INTO banks (name) VALUES (?)";
    private static final String SELECT_BANK_BY_ID = SELECT_BANK + FROM_BANK + "WHERE b.id = ?";
    private static final String SELECT_ALL_BANKS = SELECT_BANK + FROM_BANK;
    private static final String UPDATE_BANK = "UPDATE banks SET name = ? WHERE id = ? ";
    private static final String DELETE_BANK = "DELETE banks b WHERE b.id = ?";

    private final DataSource dataSource;

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

    @Override
    public Bank update(Bank bank) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_BANK);
            statement.setString(1, bank.getName());
            statement.executeUpdate();
            return getById(bank.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

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

    private Bank processBank(ResultSet result) throws SQLException {
        Bank bank = new Bank();
        bank.setId(result.getLong("id"));
        bank.setName(result.getString("name"));
        return bank;
    }
}
