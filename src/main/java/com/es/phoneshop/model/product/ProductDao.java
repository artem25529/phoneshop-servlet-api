package com.es.phoneshop.model.product;

import com.es.phoneshop.model.dao.Dao;
import com.es.phoneshop.web.SearchType;

import java.util.List;

public interface ProductDao extends Dao<Product> {
    List<Product> findProductsAdvancedSearch(String description, SearchType searchType, int minPrice, int maxPrice);
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    void delete(Long id) throws Exception;
}
