package com.es.phoneshop.model.product;

import com.es.phoneshop.model.dao.AbstractDao;
import com.es.phoneshop.model.dao.GenericDao;

import java.util.List;

public interface ProductDao extends GenericDao<Product> {
    //Product get(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    //void save(Product product);
    void delete(Long id) throws Exception;
}
