package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingleTonHelper {
        private static final CartService INSTANCE = new DefaultCartService();
    }

    public static CartService getInstance() {
        return DefaultCartService.SingleTonHelper.INSTANCE;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart)request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        CartItem newItem = new CartItem(product, quantity);
        List<CartItem> items = cart.getItems();
        if (items.contains(newItem)) {
            int index = items.indexOf(newItem);
            CartItem oldItem = items.get(index);

            items.set(index, new CartItem(product, oldItem.getQuantity() + newItem.getQuantity()));
        } else {
            items.add(new CartItem(product, quantity));
        }
        product.setStock(product.getStock() - quantity);


    }
}
