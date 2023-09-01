package com.clevertec.bankmanager.service;

import java.util.List;

/**
 * This interface is abstract for CRUD operations.
 *
 * @param <K> key - used as a primary key parameter.
 * @param <T> type - is the type of class used.
 */
public interface AbstractService<K, T> {

    /**
     * Defauld method create for create new object and send it to database.
     *
     * @param t expected object of type T to create it.
     * @return new created class of type T.
     */
    T create(T t);

    /**
     * Default method for get all object of type T from database.
     *
     * @return list of objects of type T.
     */
    List<T> getAll();

    /**
     * Default method for getting an object of type T by their key from the database.
     *
     * @param id expected object of type K used as primary key.
     * @return an object of type T from database by primary key.
     */
    T getById(K id);

    /**
     * Default method for update existing object type of T in database.
     *
     * @param t expected object of type T.
     * @return updated object of type T from database.
     */
    T update(T t);

    /**
     * Default method for delete existing object from database by primary key.
     *
     * @param id expected object of type K used as primary key.
     */
    void delete(K id);

}
