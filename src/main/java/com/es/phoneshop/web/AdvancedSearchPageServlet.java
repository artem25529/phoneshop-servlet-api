package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
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

public class AdvancedSearchPageServlet extends HttpServlet {
    protected static final String ADVANCED_SEARCH_JSP = "/WEB-INF/pages/advancedSearch.jsp";
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
        Map<String, String> errors = new HashMap<>();
        String description = request.getParameter("description");
        String searchType = request.getParameter("searchType");
        SearchType searchTypeEnum = SearchType.valueOf(searchType);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(request.getLocale());
        String minPriceString = request.getParameter("minPrice");
        int minPrice = 0;
        try {
            minPrice = numberFormat.parse(minPriceString).intValue();
        } catch (ParseException e) {
            errors.put("minPrice", "Invalid value");
        }

        String maxPriceString = request.getParameter("maxPrice");
        int maxPrice = 0;
        try {
            maxPrice = numberFormat.parse(maxPriceString).intValue();
        } catch (ParseException e) {
            errors.put("maxPrice", "Invalid value");
        }

        if (errors.isEmpty()) {
            request.setAttribute("products", productDao.findProductsAdvancedSearch(description, searchTypeEnum, minPrice, maxPrice));
            request.getRequestDispatcher(ADVANCED_SEARCH_JSP).forward(request, response);
        } else {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/pages/preAdvancedSearch.jsp").forward(request, response);
        }

    }

}
