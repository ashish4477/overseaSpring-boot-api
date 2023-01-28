<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Dec 12, 2007
  Time: 5:33:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<c:choose>
   <c:when test="${errorCode== '500'}">
       <c:set var="curTitle" value="Unable To Process" />
   </c:when>
   <c:when test="${errorCode == '403'}">
       <c:set var="curTitle" value="Access Denied" />
   </c:when>
   <c:when test="${errorCode == '404'}">
       <c:set var="curTitle" value="Page Not Found" />
   </c:when>
   <c:otherwise>
       <c:set var="curTitle" value="Unable To Process" />
   </c:otherwise>
</c:choose>

<div id="error"  class="wide-content">
    <div class="hd">
        <h2 class="title">${curTitle}</h2>
    </div>
    <div class="bd">
<c:choose>
    <c:when test="${errorCode== '500'}">
        <p>We are unable to process this request at this time. Our system administrator has been notified and we hope to rectify this shortly. Thank you for your patience.<br /><br /></p>
    </c:when>
    <c:when test="${errorCode == '403'}">
        <p>You are not authorized to view this page.<br /><br /></p>
    </c:when>
    <c:when test="${errorCode == '404'}">
        <p>We are unable to process this request at this time. Our system administrator has been notified and we hope to rectify this shortly. Thank you for your patience.<br /><br /></p>
    </c:when>
    <c:when test="${errorCode eq '503'}">
        <p>Your session has timed out due to inactivity, please try again.<br /><br /></p>
    </c:when>

    <c:otherwise>
        <p>We are unable to process this request at this time. We apologize for any inconvenience.<br /><br /></p>
    </c:otherwise>
</c:choose>
        <!-- ${errorCode} -->
    </div>
</div>





