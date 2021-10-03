package com.es.phoneshop.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class GenericDao<T extends DaoEntity> implements Dao<T> {
    protected final List<T> itemsList;
    protected final ReentrantReadWriteLock readWriteLock;
    protected final Lock readLock;
    protected final Lock writeLock;
    protected long maxId;

    public GenericDao() {
        itemsList = new ArrayList<>();
        readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    @Override
    public T get(Long id) throws RuntimeException{
        readLock.lock();
        T result;
        try {
            result = itemsList.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(() -> itemsList.get(0).getException());
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
