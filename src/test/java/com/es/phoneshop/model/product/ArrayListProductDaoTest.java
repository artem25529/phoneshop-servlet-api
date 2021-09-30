package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private Product testProduct;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        Currency usd = Currency.getInstance("USD");
        testProduct = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts(null, null, null).isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        productDao.save(testProduct);
        assertTrue(testProduct.getId() > 0);
        Product result = productDao.getProduct(testProduct.getId());
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test
    public void testProductWithZeroStock() {
        List<Product> products = productDao.findProducts(null, null, null);
        long count = products.stream()
                .filter(product -> product.getStock() == 0)
                .count();
        assertFalse(count > 0);

    }

    @Test
    public void testDeleteProduct() throws ProductNotFoundException {
        productDao.save(testProduct);
        assertTrue(testProduct.getId() >= 0);
        assertNotNull(productDao.getProduct(testProduct.getId()));
        productDao.delete(testProduct.getId());
        Throwable thrown = catchThrowable(() -> productDao.getProduct(testProduct.getId()));
        assertThat(thrown).isInstanceOf(ProductNotFoundException.class);
    }
}
