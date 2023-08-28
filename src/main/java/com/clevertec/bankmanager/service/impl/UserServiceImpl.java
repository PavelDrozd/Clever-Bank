package com.clevertec.bankmanager.service.impl;

import com.clevertec.bankmanager.data.dto.UserDto;
import com.clevertec.bankmanager.service.UserService;
import com.clevertec.bankmanager.shared.exception.service.ServiceException;
import com.clevertec.bankmanager.shared.exception.service.ServiceValidationException;
import com.clevertec.bankmanager.shared.exception.store.dao.DaoException;
import com.clevertec.bankmanager.shared.util.mapper.EntityDtoMapper;
import com.clevertec.bankmanager.store.dao.UserDao;
import com.clevertec.bankmanager.store.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final EntityDtoMapper mapper;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
        mapper = EntityDtoMapper.getInstance();
    }

    @Override
    public UserDto create(UserDto userDto) {
        try {
            User user = userDao.create(mapper.mapToUser(userDto));
            return mapper.mapToUserDto(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<UserDto> getAll() {
        try {
            return userDao.getAll().stream().map(mapper::mapToUserDto).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UserDto getById(Long id) {
        try {
            if (userDao.getById(id) == null) {
                throw new ServiceValidationException("User with ID:" + id + " is null.");
            }
            return mapper.mapToUserDto(userDao.getById(id));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UserDto update(UserDto userDto) {
        try {
            User user = userDao.update(mapper.mapToUser(userDto));
            return mapper.mapToUserDto(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            if (userDao.delete(id)) {
                throw new ServiceValidationException("Can't delete user by id: " + id);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
