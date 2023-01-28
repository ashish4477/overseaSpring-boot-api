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

<div id="eod-corrections" class="body-content page-form column">
    <div class="hd">
		<div class="hd-inner">
			<h1 class="title">Election Official Directory</h1>
		</div>

    </div>
    <div class="bd">
		<div class="bd-inner">
			<div class="message">
				<spring:message code="${messageCode}"/><br/><br/>
			</div>
			<p><a href="<c:url value="/eod.htm"/>">&larr; Back to Election Official Directory</a></p>
			<p><a href="<c:url value='/home.htm'/>">&larr; Back to Home Page</a></p>
		</div>
    </div>
    <div class="ft">
			<div class="ft-inner"></div>
	</div>
</div>