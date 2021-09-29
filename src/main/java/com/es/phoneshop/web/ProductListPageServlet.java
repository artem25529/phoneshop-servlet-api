package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.*;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.product.feature.RecentlyViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    protected static final String PRODUCT_LIST_JSP = "/WEB-INF/pages/productList.jsp";
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
        DefaultCartService.getInstance().getCart(request);
        RecentlyViewedProductsService.getInstance().getRecentlyViewedProducts(request);
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)
        ));
        request.getRequestDispatcher(PRODUCT_LIST_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.valueOf(request.getParameter("productId"));
        String quantityString = request.getParameter("quantity" + productId);
        Cart cart = cartService.getCart(request);
        int quantity;
        try {
            quantity = getQuantity(quantityString, request);
            cartService.add(cart, productId, quantity);
        } catch (ParseException | OutOfStockException e) {
            request.setAttribute("errorId", productId);
            request.setAttribute("wrongValue", quantityString);
            handleError(e, request, response);
            return;
        }
        response.sendRedirect("products");
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
