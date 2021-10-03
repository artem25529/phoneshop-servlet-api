<%@ page import="com.es.phoneshop.model.order.PaymentMethod" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.message}">
    <div class="success">
      ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty errors}">
    <div class="error">
      Error occured while placing order
    </div>
  </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
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
        <tags:orderFormRow name="firstName" label="First Name" order="${order}" errors="${errors}"/>
        <tags:orderFormRow name="lastName" label="Last Name" order="${order}" errors="${errors}"/>
        <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"/>
        <tags:orderFormRow name="deliveryDate" label="Delivery Date" order="${order}" errors="${errors}"/>
        <tags:orderFormRow name="deliveryAddress" label="Delivery Address" order="${order}" errors="${errors}"/>
        <tr>
          <td>Payment method<span style="color: red">*</span></td>
          <td>
            <select name="paymentMethod">
              <option></option>
              <c:forEach var="paymenyMethod" items="${paymentMethods}">
                <c:choose>
                  <c:when test="${paymenyMethod eq order.paymentMethod}">
                    <option selected>${paymenyMethod}</option>
                  </c:when>
                  <c:otherwise><option>${paymenyMethod}</option></c:otherwise>
                </c:choose>
              </c:forEach>
            </select>
            <c:set var="error" value="${errors['paymentMethod']}"/>
            <c:if test="${not empty error}">
              <div class="error">${error}</div>
            </c:if>
          </td>
        </tr>
      </table>
        <p>
          <button>Place order</button>
        </p>
    </form>
  <form id="deleteCartItem" method="post">
  </form>
</tags:master>