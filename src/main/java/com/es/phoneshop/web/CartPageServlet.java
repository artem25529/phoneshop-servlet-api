package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class CartPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Map<Long, String> errors = new HashMap<>();
        Cart cart = cartService.getCart(request);
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            int quantity;
            try {
                quantity = getQuantity(quantities[i], request);
                cartService.update(cart, productId, quantity);
            } catch (ParseException | OutOfStockException e) {
                handleError(errors, productId, e);
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityString).intValue();
    }

    private void handleError(Map<Long, String> errors, Long productId, Exception e) throws ServletException, IOException {
        if (e.getClass().equals(ParseException.class)) {
            errors.put(productId, "not a number");
        } else {
            OutOfStockException outOfStockException = (OutOfStockException) e;
            if (outOfStockException.getStockRequested() <= 0) {
                errors.put(productId, "Invalid value");
            } else {
                errors.put(productId, "Not enough stock, available: " + outOfStockException.getStockAvailable());
            }
        }
    }
}
