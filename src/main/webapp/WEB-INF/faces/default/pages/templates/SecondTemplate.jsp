<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 22, 2007
  Time: 9:06:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<title>Overseas Vote | ${title}</title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/basic.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ovf.css"/>" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/frame.css"/>" />
	
	<!--
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/calendar.css"/>" />
	-->
	
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/home.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />

	<!-- IE-specific stylesheet to work around the browser's lack of standards-compliance -->
	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ie-bugfix.css"/>" />
	<![endif]-->
	
	<script src="<c:url value="/js/yui-rollup.js" />" type="text/javascript"></script>
	
	<!--
	<script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/element-min.js"/>" type="text/javascript"></script>
	-->

	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
</head>
<body id="overseas-vote-foundation" class="${sectionName} secondary">
	<div id="container">
        <%-- ADMIN MENU --%>

        <div id="head">
			<div class="head-inner">
                <h1 id="title" class="site-title"><a href="<c:url value="/home.htm" />"><span>Overseas Vote</span></a></h1>
				
				<div id="site-tools">
					<div>
						<ul>
							<li class="voter-help-desk"><a href="https://vhd.overseasvotefoundation.org/unified/index.php?group=ovf">Voter Help Desk</a></li>
							&nbsp; | &nbsp;
							<li class="my-voter-account">
							<c:choose>
								<c:when test="${not empty userDetails}">
									Hi<c:if test="${fn:length(userDetails.name.firstName) gt 0}">, ${userDetails.name.firstName}</c:if>
									(<a class="logout-link" href="<c:url value="/logout"/>">log out</a>)
								</c:when>
								<c:otherwise>
									<a href="<c:url value='/Login.htm'/>" class="login-link">My Voter Account</a>
								</c:otherwise>
							</c:choose></li>
						</ul>
					</div>
					<div id="search">
					<form action="<c:url value="/search.htm"/>" method="get" class="bc-form" id="search-form-top">
						<fieldset>
							<input type="text" class="search" name="q" id="search-terms-top" value="" />
							<input type="image" class="submit" name="op" alt="Search" src="<c:url value='/img/backgrounds/search-submit.gif'/>" />
						</fieldset>
					</form>
					</div>
				</div>
			</div>
		</div>
		
		<c:import url="/WEB-INF/faces/basic/pages/statics/AdminMenu.jsp" />
		
		<div id="primary-content">
			<div class="primary-content-inner">
	            <c:import url="/WEB-INF/${relativePath}/pages/statics/LeftMenu.jsp" />
	            <c:import url="/WEB-INF/${relativePath}/pages/blocks/Home.jsp"/>
	
				<div class="clear"></div>
			</div>
		</div>

		<c:if test="${sectionName eq 'home'}">
        	<c:import url="/WEB-INF/${relativePath}/pages/statics/SecondaryContent.jsp" />
		</c:if>
		
		<div id="foot">
			<c:import url="/WEB-INF/${relativePath}/pages/statics/Footer.jsp" />
		</div>
	</div>
	
	<div class="overlay">
		<div class="overlay-underlay" id="underlay"></div>
		<div class="overlay-content">
			<c:import url="${content}"/>
		</div>
	</div>



</body>
</html>
