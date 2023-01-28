<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jul 3, 2007
  Time: 2:54:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<span>Choose a ${regionLabel}:</span>
<select name="regionId" id="region-select" class="field">
    <c:if test="${empty regions}">
        <option value="" selected="selected">Region..</option>
    </c:if>
    <c:if test="${not empty regions}">
	    <option value="">Region..</option>
	    <c:forEach items="${regions}" var="region">
	        <option value="${region.id}" <c:if test="${not empty selectedRegion and selectedRegion.id eq region.id}">selected="selected"</c:if>>${region.name}</option>
	    </c:forEach>
    </c:if>
    <optgroup label=""></optgroup>
</select>
