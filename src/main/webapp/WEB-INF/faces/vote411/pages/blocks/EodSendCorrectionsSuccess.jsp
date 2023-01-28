<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jul 20, 2007
  Time: 5:46:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="eod-corrections" class="column-form">
    <div class="hd">
        <h1 class="title">Election Official Directory</h1>
    </div>
    <div class="bd">
      <div class="message">
          <div class="alert alert-success" role="alert">
          <spring:message code="${messageCode}"/>
          </div>
        </div>
        <p><a href="<c:url value="/eod.htm"/>">&larr; Back to Election Official Directory</a></p>
        <p><a href="<c:url value='/home.htm'/>">&larr; Back to Home Page</a></p>
    </div>
</div>