package com.es.phoneshop.model.product.feature;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedProductsService {
    private final ProductDao productDao;

    private RecentlyViewedProductsService() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    private static class SingleTonHelper {
        private static final RecentlyViewedProductsService INSTANCE = new RecentlyViewedProductsService();
    }

    public static RecentlyViewedProductsService getInstance() {
        return SingleTonHelper.INSTANCE;
    }

    public List<Product> getRecentlyViewedProducts (HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Product> recentlyViewedProducts = (List<Product>)session.getAttribute("recentlyViewedProducts");
        if (recentlyViewedProducts == null) {
            recentlyViewedProducts = new ArrayList<>();
            session.setAttribute("recentlyViewedProducts", recentlyViewedProducts);
        }
        return recentlyViewedProducts;
    }

    public void add (Long productId, List<Product> recentlyViewedProducts) {
        Product product = productDao.getProduct(productId);
        recentlyViewedProducts.remove(product);
        recentlyViewedProducts.add(0, product);
        if (recentlyViewedProducts.size() > 3) {
            recentlyViewedProducts.remove(3);
        }
    }
}
