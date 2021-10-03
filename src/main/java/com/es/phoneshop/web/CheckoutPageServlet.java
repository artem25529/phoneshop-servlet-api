package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class CheckoutPageServlet extends HttpServlet {
    protected static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute("order", orderService.getOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        Map<String, String> errors = new HashMap<>();

        setRequiredParameter(request, "firstName", errors, order::setFirstName);
        setRequiredParameter(request, "lastName", errors, order::setLastName);
        setRequiredParameter(request, "phone", errors, order::setPhone);
        setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress);
        setPaymentMethod(request, errors, order);
        setDeliveryDate(request, errors, order);

        handleErrors(request, response, errors, order);
    }

    private LocalDate getDeliveryDate(HttpServletRequest request) {
        String deliveryDateString = request.getParameter("deliveryDate");
        int day = Integer.parseInt(deliveryDateString.substring(0, 2));
        int month = Integer.parseInt(deliveryDateString.substring(3, 5));
        int year = Integer.parseInt(deliveryDateString.substring(6, 10));
        return LocalDate.of(year, month, day);
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value name is required");
        } else {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String paymentMethod = request.getParameter("paymentMethod");
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            errors.put("paymentMethod", "Value is required");
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String deliveryDate = request.getParameter("deliveryDate");
        if (deliveryDate == null || deliveryDate.isEmpty()) {
            errors.put("deliveryDate", "Value is required");
        } else {
            try {
                int day = Integer.parseInt(deliveryDate.substring(0, 2));
                int month = Integer.parseInt(deliveryDate.substring(3, 5));
                int year = Integer.parseInt(deliveryDate.substring(6, 10));
                order.setDeliveryDate(LocalDate.of(year, month, day));
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                errors.put("deliveryDate", "Wrong date format");
            }
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

    private void handleErrors(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors,
                              Order order) throws ServletException, IOException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
            Cart cart = cartService.getCart(request);
            cartService.clearCart(cart);
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
    }
}
