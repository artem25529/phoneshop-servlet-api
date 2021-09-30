package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.feature.RecentlyViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import static com.es.phoneshop.web.ProductListPageServlet.PRODUCT_LIST_JSP;


public class ProductDetailsPageServlet extends HttpServlet {
    protected static final String PRODUCT_JSP = "/WEB-INF/pages/product.jsp";
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
        Long productId = parseProductId(request);
        request.setAttribute("id", productId);
        RecentlyViewedProductsService instance = RecentlyViewedProductsService.getInstance();
        List<Product> recentlyViewedProducts = instance.getRecentlyViewedProducts(request);
        instance.add(productId, recentlyViewedProducts);
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute("product", productDao.getProduct(productId));
        request.getRequestDispatcher(PRODUCT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        String quantityString;
        quantityString = request.getParameter("quantity");
        Cart cart = cartService.getCart(request);
        int quantity;
        try {
            quantity = getQuantity(quantityString, request);
            cartService.add(cart, productId, quantity);
        } catch (ParseException | OutOfStockException e) {
            handleError(e, request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityString).intValue();
    }

    private void handleError(Exception e, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (e instanceof OutOfStockException) {
            OutOfStockException outOfStockException = (OutOfStockException) e;
            if (outOfStockException.getStockRequested() <= 0) {
                request.setAttribute("error", "Invalid value");
            } else {
                request.setAttribute("error", "Out of stock, available: " + outOfStockException.getStockAvailable());
            }
        } else {
            request.setAttribute("error", "Not a number");
        }
        doGet(request, response);
    }
}
