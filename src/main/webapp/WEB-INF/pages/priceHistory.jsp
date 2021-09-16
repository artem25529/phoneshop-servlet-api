<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>Price History</p>
    <p>
        ${product.description}
    </p>
    <c:if test="${product.priceHistories.size() != 0}">
    <table>
        <tr>
            <th>Start Date</th>
            <th>Price</th>
        </tr>
        <c:forEach var="priceHistory" items="${product.priceHistories}">
            <tr>
                <td>
                    <fmt:formatDate value="${priceHistory.startDate}" pattern="d MMM y"/>
                </td>
                <td>
                        <fmt:formatNumber value="${priceHistory.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>

    <c:if test="${product.priceHistories.size() == 0}">
        <p>No price history</p>
    </c:if>

</tags:master>