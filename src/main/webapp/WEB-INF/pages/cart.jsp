<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <p>
    Cart: ${cart}, total quantity: ${cart.totalQuantity}
  </p>
  <c:if test="${not empty param.message}">
    <div class="success">
      ${param.message}
    </div>
    <br>
  </c:if>
  <c:if test="${not empty errors}">
    <div class="error">
      There were errors updating cart
    </div>
    <br>
  </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
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
            <td></td>
          </tr>
        </thead>
        <c:forEach var="item" items="${cart.items}" varStatus="status">
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
              <c:set var="error" value="${errors[item.product.id]}"/>

              <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}" class="quantity">
              <c:if test="${not empty error}">
                <div class="error">
                  ${errors[item.product.id]}
                </div>
              </c:if>
              <input type="hidden" value="${item.product.id}" name="productId">
            </td>
            <td class="price">
              <a href="${pageContext.servletContext.contextPath}/priceHistories/${item.product.id}"><fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/></a><br>
            </td>
            <td>
              <button form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">Delete</button>
            </td>
          </tr>
        </c:forEach>
        <tr>
          <td></td>
          <td></td>
          <td class="quantity">Total quantity: ${cart.totalQuantity}</td>
          <td class="price">Total cost: <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${item.product.currency.symbol}"/></td>
        </tr>
      </table><br>
        <button>Update</button>
    </form>
  <form id="deleteCartItem" method="post"></form>
  <form action="${pageContext.servletContext.contextPath}/checkout">
    <button>Checkout</button>
  </form>
</tags:master>