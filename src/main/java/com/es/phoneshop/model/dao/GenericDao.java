package com.es.phoneshop.model.dao;

import java.util.ArrayList;
import java.util.List;

public interface GenericDao<T> {
    T get (Long id) throws RuntimeException;
    void save (T item);
}
