package com.es.phoneshop.model.order;

import com.es.phoneshop.model.dao.GenericDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao {

    private final List<Order> orderList;
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;

    private static class SingletonHelper {
        private static final OrderDao INSTANCE = new ArrayListOrderDao();
    }

    public static OrderDao getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private ArrayListOrderDao() {
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        orderList = new ArrayList<>();
    }


    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        readLock.lock();
        Order result;
        try {
            result = itemsList.stream()
                    .filter(order -> secureId.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            readLock.unlock();
        }
        return result;
    }

}
