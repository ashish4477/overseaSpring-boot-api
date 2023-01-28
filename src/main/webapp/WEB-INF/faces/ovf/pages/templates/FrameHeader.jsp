<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="title" value="${param.title}" />

<c:choose>
	<c:when test="${not empty param.icon}"><h1 class="${param.icon} title">${title}</h1></c:when>
	<c:otherwise><h1 class="title">${title}</h1></c:otherwise>
</c:choose>

