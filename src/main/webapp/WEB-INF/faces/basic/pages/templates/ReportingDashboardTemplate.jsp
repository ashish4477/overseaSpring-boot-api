<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta content="" name="description">
<meta content="" name="author">
<title>Reporting Dashboard | ${title}</title>
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/bootstrap/bootstrap.css"/>"/>

<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/bootstrap/bootstrap-responsive.css"/>"/>
<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/bootstrap/ReportingDashboard/reports-style.css"/>"/>
      <!--[if lt IE 9]>
<script type="text/javascript" src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<script type="text/javascript" src="<c:url value="/js/bootstrap/plugins/flot/excanvas.min.js"/>"></script>
    <![endif]-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

<script src="<c:url value="/js/bootstrap/bootstrap.min.js"/>" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
 <style type="text/css">
        body {
            padding-top: 0;
            padding-bottom: 40px;
            height: 100%; /* make the percentage height on placeholder work */
        }
   </style>
 </head>
<body class="${sectionName}">
<div class="menu">
    <div id="upperNavWrap" class="container clearfix">
        <div class="header">
            <div id="stateTitle" class="pull-left">
             <a class="brand" href="ReportingDashboard.htm">
             <c:choose>
             	<c:when test="${relativePath == 'faces/minnesota'}"><h2>State of Minnesota</h2></c:when>
             	<c:otherwise>
				 <h2>Overseas Vote</h2>
				</c:otherwise>
			</c:choose>
            <h3>Military and Overseas Voter Reporting</h3>
            </a>
            </div>
        <div id="login" class="btn-toolbar clearfix pull-right">
            <div class="btn-group">
                <c:choose>
                    <c:when test="${not empty userDetails and userDetails.id ne 0 }">
                        <a href="<c:url value='/Login.htm'/>" title="View My Voter Account"
                           class="btn btn-primary">Hello<c:if
                                test="${fn:length(userDetails.name.firstName) gt 0}">, ${userDetails.name.firstName}</c:if></a>
                        <a class="btn btn" href="<c:url value="/logout"/>">log out</a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/Login.htm'/>" class="login-link">My Voter Account</a>
                    </c:otherwise>
                </c:choose></div>
        </div>
         </div>
    </div>
</div>
<div id="main" class="container shadow rnd4">
    <div class="row">
        <c:import url="/WEB-INF/faces/basic/pages/statics/ReportingDashboardLeftMenu.jsp"/>
        <div id="mainContent" class="span9">
            <c:import url="${content}">
                <c:param name="cStateId" value="NEED_STATE_ID" />
                <c:param name="cStateName" value="NEED_STATE_NAME" />
            </c:import>
        </div>
        <%--end BodyContent right--%>
    </div>
    <%--end main content row --%>
</div>
<%--end main container--%>
</body>
</html>
