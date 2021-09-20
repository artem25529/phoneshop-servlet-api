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
  <form>
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
        <td class="price">
          Price
          <tags:sortLink sort="price" order="asc"/>
          <tags:sortLink sort="price" order="desc"/>
        </td>
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
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/priceHistories/${product.id}"><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/></a>
        </td>
      </tr>
    </c:forEach>
  </table><br>
  <c:choose>
    <c:when test="${recentlyViewedProducts.size() == 0}">
      <p>There isn't any products viewed recently</p>
    </c:when>
    <c:otherwise>
      <table class="info">
        <caption>
          Recently viewed
        </caption>
        <tr>
          <c:forEach var="product" items="${recentlyViewedProducts}">
            <td class="info">
              <div class="recentlyViewedProduct">
                <img src="${product.imageUrl}" class="product-tile" alt="not supported">
                <a href="${pageContext.servletContext.contextPath}/products/${product.id}"><br>
                    ${product.description}
                </a>
                <fmt:formatNumber value="${product.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
              </div>
            </td>
          </c:forEach>
        </tr>
      </table>
    </c:otherwise>
  </c:choose>


</tags:master>