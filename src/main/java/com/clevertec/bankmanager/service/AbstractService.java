package com.clevertec.bankmanager.service;

import java.util.List;

public interface AbstractService<K, T> {

    T create(T t);

    List<T> getAll();

    T getById(K id);

    T update(T t);

    void delete(K id);

}
