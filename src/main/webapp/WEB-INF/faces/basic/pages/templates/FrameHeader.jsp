<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="block-page-title-block">
  <c:set var="title" value="${param.title}" />

  <c:choose>
    <c:when test="${not empty param.icon}">
      <h1 class="${param.icon} title">${title}</h1>
    </c:when>
    <c:otherwise>
      <h1 class="title">${title}</h1>
    </c:otherwise>
  </c:choose>
  <c:if test="${not empty param.showCloser }">
    <div id="fx-close-frame"><a href="<c:url value='/home.htm'/>" id="x-close-button"><img src="<c:url value='/img/buttons/close.gif'/>" alt="Close Button" /></a></div>
  </c:if>
</div>
