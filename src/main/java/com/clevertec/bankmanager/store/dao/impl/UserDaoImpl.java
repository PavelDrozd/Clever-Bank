package com.clevertec.bankmanager.store.dao.impl;

import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.store.dao.UserDao;
import com.clevertec.bankmanager.store.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of DAO interface for process user objects.
 * Using datasource for connect to the database.
 */
@RequiredArgsConstructor
@Slf4j
public class UserDaoImpl implements UserDao {

    /** SELECT part of query with user parameters in the database. */
    private static final String SELECT_USER = "SELECT u.id, u.first_name, u.last_name ";
    /** FROM part of query with users table. */
    private static final String FROM_USER = "FROM users u ";
    /** INSERT query to create a new row in the database. */
    private static final String INSERT_USER = "INSERT INTO users (first_name, last_name) VALUES (?, ?)";
    /** SELECT query to find user by ID */
    private static final String SELECT_USER_BY_ID = SELECT_USER + FROM_USER + "WHERE u.id = ? AND u.deleted = false";
    /** SELECT query to get all users from the database */
    private static final String SELECT_ALL_USERS = SELECT_USER + FROM_USER + "WHERE u.deleted = false";
    /** UPDATE query for set new values in fields of user entity. */
    private static final String UPDATE_USER = "UPDATE users SET first_name = ?, last_name = ? " +
                                              "WHERE id = ? AND deleted = false";
    /** DELETE query by set deleted value true in user row by ID from the database. */
    private static final String DELETE_USER = "UPDATE users u SET deleted = true WHERE u.id = ?";

    /** DataSource for create connection with database. */
    private final DataSource dataSource;

    /**
     * Method for create new entity in database.
     *
     * @param user expected object of type User to create it.
     * @return new created User object.
     */
    @Override
    public User create(User user) {
        log.debug("DAO CREATE USER: " + user);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(//
                    INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.executeUpdate();
            ResultSet key = statement.getGeneratedKeys();
            User created = new User();
            if (key.next()) {
                created = getById(key.getLong("id"));
            }
            return created;
        } catch (SQLException e) {
            log.error("DAO EXCEPTION - CREATE USER: " + e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * Method for getting all user entities from database.
     *
     * @return List of User objects.
     */
    @Override
    public List<User> getAll() {
        log.debug("DAO GET ALL USERS");
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                users.add(processUser(result));
            }
            return users;
        } catch (SQLException e) {
            log.error("DAO EXCEPTION - GET ALL USERS: " + e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * Method find entity in database by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return User object.
     */
    @Override
    public User getById(Long id) {
        log.debug("DAO GET USER BY ID: " + id);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            User user = new User();
            if (result.next()) {
                user = processUser(result);
            }
            return user;
        } catch (SQLException e) {
            log.error("DAO EXCEPTION - GET USER BY ID: " + e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * Method update entity data in database.
     *
     * @param user expected updated object of type User.
     * @return updated User object.
     */
    @Override
    public User update(User user) {
        log.debug("DAO UPDATE USER: " + user);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setLong(3, user.getId());
            statement.executeUpdate();
            return getById(user.getId());
        } catch (SQLException e) {
            log.error("DAO EXCEPTION - UPDATE USER: " + e.getMessage());
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
        log.debug("DAO DELETE USER BY ID: " + id);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_USER);
            statement.setLong(1, id);
            int rowDeleted = statement.executeUpdate();
            return rowDeleted == 1;
        } catch (SQLException e) {
            log.error("DAO EXCEPTION - DELETE USER: " + e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * Method for processing User object to create a new.
     *
     * @param result expected ResultSet object.
     * @return new User object.
     * @throws SQLException default exception by using ResultSet methods.
     */
    private User processUser(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(result.getLong("id"));
        user.setFirstName(result.getString("first_name"));
        user.setLastName(result.getString("last_name"));
        return user;
    }
}
