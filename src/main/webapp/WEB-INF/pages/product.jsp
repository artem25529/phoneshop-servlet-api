<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="recentlyViewedProducts" type="java.util.ArrayList" scope="session"/>
<tags:master pageTitle="Product Details">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message and empty error}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">
            There wa an error adding to cart
        </div>
    </c:if>
    <p>${product.description}</p>
    <form method="post">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>Code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <input name="quantity" value="${not empty error ? param.quantity : 1}" class="quantity">
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <button>Add to cart</button>
    </form>
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