package com.es.phoneshop.model.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class GenericDaoImpl<T extends AbstractDao> implements GenericDao<T> {
    protected List<T> itemsList = new ArrayList<>();
    protected ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected Lock readLock = readWriteLock.readLock();
    protected Lock writeLock = readWriteLock.writeLock();
    protected long maxId;

    @Override
    public T get(Long id) throws RuntimeException{
        readLock.lock();
        T result;
        try {
            result = itemsList.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(RuntimeException::new);
        } finally {
            readLock.unlock();
        }
        return result;
    }

    @Override
    public void save(T item){
        writeLock.lock();
        try {
            if (item.getId() == null) {
                item.setId(maxId++);
            }
            itemsList.add(item);
        } finally {
            writeLock.unlock();
        }

    }
}
