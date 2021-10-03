package com.es.phoneshop.model.order;

import com.es.phoneshop.model.dao.Dao;

public interface OrderDao extends Dao<Order> {
    Order getOrderBySecureId(String secureId) throws OrderNotFoundException;
}
