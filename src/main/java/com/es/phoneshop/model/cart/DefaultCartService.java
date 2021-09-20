package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private final ProductDao productDao;

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
        List<CartItem> items = cart.getItems();
        Product product = productDao.getProduct(productId);
        CartItem newItem = new CartItem(product, quantity);
        int index;
        if ((index = items.indexOf(newItem)) != -1) {
            CartItem oldItem = items.get(index);
            if (oldItem.getQuantity() + quantity > product.getStock()) {
                throw new OutOfStockException(product, quantity, product.getStock() - oldItem.getQuantity());
            }
            oldItem.setQuantity(oldItem.getQuantity() + quantity);
        } else {
            if (quantity > product.getStock()) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            items.add(newItem);
        }
    }
}
