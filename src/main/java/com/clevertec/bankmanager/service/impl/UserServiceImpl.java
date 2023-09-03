package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.data.dto.UserDto;
import com.clevertec.bankmanager.service.UserService;
import com.clevertec.bankmanager.shared.exception.service.ServiceException;
import com.clevertec.bankmanager.shared.exception.service.ServiceValidationException;
import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.shared.util.mapper.EntityDtoMapper;
import com.clevertec.bankmanager.store.dao.UserDao;
import com.clevertec.bankmanager.store.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of service interface for process user DTO objects.
 */
@Slf4j
public class UserServiceImpl implements UserService {

    /** UserDao is used to get objects from DAO module. */
    private final UserDao userDao;
    /** Mapper for mapping DTO and entity objects. */
    private final EntityDtoMapper mapper;

    /**
     * This constructor use UserDao implementation and set instance of mapper from EntityDtoMapper.
     *
     * @param userDao expected UserDao implementation class.
     */
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
        mapper = EntityDtoMapper.getInstance();
    }

    /**
     * Method for create new DTO class and transfer it to database by using DAO.
     *
     * @param userDto expected object of type UserDto to create it.
     * @return new created UserDto object.
     */
    @Override
    public UserDto create(UserDto userDto) {
        log.debug("SERVICE CREATE USER: " + userDto);
        try {
            User user = userDao.create(mapper.mapToUser(userDto));
            return mapper.mapToUserDto(user);
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - CREATE USER: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method for getting all user DTO objects from DAO.
     *
     * @return List of UserDto objects.
     */
    @Override
    public List<UserDto> getAll() {
        log.debug("SERVICE GET ALL USERS ");
        try {
            return userDao.getAll().stream().map(mapper::mapToUserDto).collect(Collectors.toList());
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - GET ALL USERS: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method get user DTO object from DAO by ID.
     *
     * @param id expected object of type Long used as primary key.
     * @return UserDto object.
     */
    @Override
    public UserDto getById(Long id) {
        log.debug("SERVICE GET USER BY ID: " + id);
        try {
            if (userDao.getById(id) == null) {
                log.error("SERVICE VALIDATION EXCEPTION - GET USER BY ID ");
                throw new ServiceValidationException("User with ID:" + id + " is null.");
            }
            return mapper.mapToUserDto(userDao.getById(id));
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - GET USER BY ID: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method update user DTO in database by using DAO.
     *
     * @param userDto expected updated object of type UserDto.
     * @return updated userDto object.
     */
    @Override
    public UserDto update(UserDto userDto) {
        log.debug("SERVICE UPDATE USER: " + userDto);
        try {
            User user = userDao.update(mapper.mapToUser(userDto));
            return mapper.mapToUserDto(user);
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - UPDATE USER: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Method delete object by using DAO by ID.
     *
     * @param id expected object of type Long used as primary key.
     */
    @Override
    public void delete(Long id) {
        log.debug("SERVICE DELETE USER BY ID: " + id);
        try {
            if (!userDao.delete(id)) {
                log.error("SERVICE VALIDATION EXCEPTION - DELETE USER ");
                throw new ServiceValidationException("Can't delete user by id: " + id);
            }
        } catch (DaoException e) {
            log.error("SERVICE EXCEPTION - DELETE USER BY ID: " + e.getMessage());
            throw new ServiceException(e);
        }
    }
}
