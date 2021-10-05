package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private final ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingletonHelper {
        private static final CartService INSTANCE = new DefaultCartService();
    }

    public static CartService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart == null) {
            request.getSession().setAttribute("cart", cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.get(productId);
        Optional<CartItem> optional = findCartItemForUpdate(cart, productId, quantity);
        int productsAmount = optional.map(CartItem::getQuantity).orElse(0);
        if (product.getStock() < quantity + productsAmount) {
            throw new OutOfStockException(product, quantity + productsAmount, product.getStock());
        }
        if (optional.isPresent()) {
            optional.get().setQuantity(quantity + productsAmount);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
        recalculateCart(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.get(productId);
        Optional<CartItem> optional = findCartItemForUpdate(cart, productId, quantity);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        if (optional.isPresent()) {
            optional.get().setQuantity(quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
        recalculateCart(cart);
    }

    @Override
    public void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(item ->
                productId.equals(item.getProduct().getId()));
        recalculateCart(cart);
    }

    @Override
    public void clearCart(Cart cart) {
        cart.removeItems();
    }

    private Optional<CartItem> findCartItemForUpdate(Cart cart, Long productId, int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0);
        }
        Product product = productDao.get(productId);
        Optional<CartItem> optional = cart.getItems().stream()
                .filter(item -> product.getId().equals(item.getProduct().getId()))
                .findAny();
        int productsAmount = optional.map(CartItem::getQuantity).orElse(0);
        if (product.getStock() < quantity + productsAmount) {
            throw new OutOfStockException(product, productsAmount + quantity, product.getStock() - productsAmount);
        }
        return optional;
    }

    private void recalculateCart(Cart cart) {
        int totalQuantity = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        cart.setTotalQuantity(totalQuantity);
        BigDecimal totalCost = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
        cart.setTotalCost(totalCost);
    }
}
