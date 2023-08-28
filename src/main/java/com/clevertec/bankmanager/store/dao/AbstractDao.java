package com.clevertec.bankmanager.store.dao;

import java.util.List;

public interface AbstractDao<K, T> {

    T create(T t);

    List<T> getAll();

    T getById(K id);

    T update(T t);

    boolean delete(K id);
}
