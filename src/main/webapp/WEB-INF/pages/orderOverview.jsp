<%@ page import="com.es.phoneshop.model.order.PaymentMethod" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order overview">
  <h1>Order overview</h1>
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.message}">
    <div class="success">
      ${param.message}
    </div>
  </c:if>

      <table>
        <thead>
          <tr>
            <td>Image</td>
            <td>
              Description
            </td>
            <td class="quantity">
              Quantity
            </td>
            <td class="price">
              Price
            </td>
          </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="status">
          <tr>
            <td>
              <img class="product-tile" src="${item.product.imageUrl}">
            </td>
            <td>
              <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                  ${item.product.description}
              </a>
            </td>
            <td class="quantity">
              <fmt:formatNumber var="quantity" value="${item.quantity}"/>
              ${item.quantity}
            </td>
            <td class="price">
              <a href="${pageContext.servletContext.contextPath}/priceHistories/${item.product.id}"><fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/></a><br>
            </td>

          </tr>
        </c:forEach>
        <tr>
          <td></td>
          <td></td>
          <td class="quantity">Subtotal:</td>
          <td class="price">
            <fmt:formatNumber value="${order.subTotal}" type="currency" currencySymbol="${order.currency.symbol}"/>
          </td>
        </tr>

        <tr>
          <td></td>
          <td></td>
          <td class="quantity">Delivery cost:</td>
          <td class="price">
            <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="${order.currency.symbol}"/>
          </td>
        </tr>

        <tr>
          <td></td>
          <td></td>
          <td class="quantity">Total cost:</td>
          <td class="price">
            <fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="${order.currency.symbol}"/>
          </td>
        </tr>
      </table>
      <h2>Your details</h2>
      <table>
        <tags:orderOverviewRow name="firstName" label="First Name" order="${order}"/>
        <tags:orderOverviewRow name="lastName" label="Last Name" order="${order}"/>
        <tags:orderOverviewRow name="phone" label="Phone" order="${order}"/>
        <tags:orderOverviewRow name="deliveryDate" label="Delivery Date" order="${order}"/>
        <tags:orderOverviewRow name="deliveryAddress" label="Delivery Address" order="${order}"/>
        <tr>
          <td>Payment method</td>
          <td>
            ${order.paymentMethod}
          </td>
        </tr>
      </table>

</tags:master>