<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentlyViewedProducts" type="java.util.ArrayList" scope="session"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <p>
    Cart: ${cart}
  </p>
  <c:if test="${not empty param.message}">
    <p class="success">${param.message}</p>
  </c:if>
  <c:if test="${not empty error}">
    <p class="error"> There was an error updating cart</p>
  </c:if>
  <form action="${pageContext.servletContext.contextPath}/products">
      <input name="query" value="${param.query}" placeholder="Search product...">
      <button>Search</button>
  </form>
  <table>
    <thead>
    <tr>
      <td>Image</td>
      <td>
        Description
        <tags:sortLink sort="description" order="asc"/>
        <tags:sortLink sort="description" order="desc"/>
      </td>
      <td class="quantity">Quantity</td>
      <td class="price">
        Price
        <tags:sortLink sort="price" order="asc"/>
        <tags:sortLink sort="price" order="desc"/>
      </td>
      <td></td>
    </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
              ${product.description}
          </a>
        </td>
        <td>
          <input value="${not empty error and errorId eq product.id ? wrongValue : 1}" class="quantity" size="5px" form="addToCart${product.id}" name="quantity${product.id}">
            <input form="addToCart${product.id}" type="hidden" name="productId" value="${product.id}">
          <c:if test="${not empty error and errorId eq product.id}">
            <div class="error">${error}</div>
          </c:if>
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/priceHistories/${product.id}"><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/></a><br>
        </td>
        <td>
            <form id="addToCart${product.id}" method="post">
                <button form="addToCart${product.id}">Add to cart</button>
            </form>
        </td>
      </tr>
    </c:forEach>
  </table>

  <tags:RecentlyViewedProduct/>
</tags:master>