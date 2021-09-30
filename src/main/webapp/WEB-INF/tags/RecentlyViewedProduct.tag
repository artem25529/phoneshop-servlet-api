<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="recentlyViewedProducts" type="java.util.ArrayList" scope="session"/>

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