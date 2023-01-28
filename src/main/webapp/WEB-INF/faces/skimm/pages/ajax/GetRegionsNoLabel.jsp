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
<c:if test="${empty regions}">
<select id="${param.selectId}" class="field form-control" name="${param.name}" onclick="alert('Please Select a State First');">
	<option value="0">- Select -</option>
	<optgroup label=""></optgroup>
</select>
</c:if>
<c:if test="${not empty regions}">
<select id="${param.selectId}" class="field form-control" name="${param.name}">
<option value="0">- Select A Voting Region -</option>
	<c:forEach items="${regions}" var="region">
		<option value="${region.id}" <c:if test="${region eq selectedRegion}">selected="selected" </c:if>>${region.name}</option>
	</c:forEach>
	<optgroup label=""></optgroup>
</select>
</c:if>

