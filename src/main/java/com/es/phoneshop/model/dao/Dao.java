package com.es.phoneshop.model.dao;

public interface Dao<T> {
    T get (Long id) throws RuntimeException;
    void save (T item);
}
