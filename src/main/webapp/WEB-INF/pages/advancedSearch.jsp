<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced Search">
  <form action="advancedSearch">
    <table class="info">
      <tr>
        <td class="info">Description</td>
        <td class="info">
          <input name="description">
          ${errors['description']}
        </td>
        <td class="info">
          <select name="searchType">
            <option value="all_words">all words</option>
            <option value="any_word" selected>any word</option>
          </select>
        </td>
      </tr>
      <tr>
        <td class="info">Min price</td>
        <td class="info">
          <input name="minPrice">
        </td>
      </tr>
      <tr>
        <td class="info">Max price</td>
        <td class="info">
          <input name="maxPrice">
        </td>
      </tr>
    </table>
    <button>Search</button>
  </form>


  <table>
    <tr>
      <th>Image</th>
      <th>Description</th>
      <th>Price</th>
    </tr>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>image</td>
        <td>${product.description}</td>
        <td>${product.price}</td>
      </tr>
    </c:forEach>
  </table>






</tags:master>