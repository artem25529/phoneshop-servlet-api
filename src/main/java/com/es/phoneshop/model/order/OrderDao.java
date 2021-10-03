package com.es.phoneshop.model.order;

import com.es.phoneshop.model.dao.AbstractDao;
import com.es.phoneshop.model.dao.GenericDao;

public interface OrderDao extends GenericDao<Order> {
   // Order getOrder(Long id) throws OrderNotFoundException;
    Order getOrderBySecureId(String secureId) throws OrderNotFoundException;
    //void save(Order order);

}
