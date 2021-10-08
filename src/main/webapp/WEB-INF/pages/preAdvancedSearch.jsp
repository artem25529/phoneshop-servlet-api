<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>



<tags:master pageTitle="Advanced Search">
  <form action="advancedSearch">
    <table class="info">
      <tr>
        <td class="info">Description</td>
        <td class="info">
          <input name="description" value="${param.description}">

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
          <input name="minPrice" value="${param.minPrice}">
          <div class="error">${errors['minPrice']}</div>
        </td>
      </tr>
      <tr>
        <td class="info">Max price</td>
        <td class="info">
          <input name="maxPrice" value="${param.maxPrice}">
          <div class="error">${errors['maxPrice']}</div>
        </td>
      </tr>
    </table>
    <button>Search</button>
  </form>








</tags:master>