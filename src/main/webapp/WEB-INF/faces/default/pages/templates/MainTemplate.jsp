<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
  <meta name="robots" content="noindex" />
  <meta name="google-site-verification" content="zj9XkXxgcXC51-wIIh8Es5v7axsYZTmxL79BI67AeT4" />
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<c:if test="${showMetaKeywords }">
		<c:import url="/WEB-INF/faces/basic/pages/statics/MetaKeywords.jsp" />
	</c:if>

	<title>Overseas Vote | ${title}</title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/basic.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ovf.css"/>" />
	<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css">

	<!--
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/calendar.css"/>" />
	-->
	
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

    <!-- ceebox and prereqs-->
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.swfobject.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.metadata.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.color.js"/>"></script>

    <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>


</head>
<body id="overseas-vote-foundation" class="${sectionName}">
	<div id="container">
        <%-- ADMIN MENU --%>

        <div id="head">
			<div class="head-inner">
                  <%-- Express Your Vote
                <div class="express-your-vote-banner">
					<a href="<c:url value="/ExpressYourVote.htm" />">
						<img src="<c:url value="/img/buttons/express-your-vote-button.png" />" alt="Express Your Vote" />
					</a>
				</div>
				--%>
        <%-- Survey Banner
        <div class="featured survey">
					<a href="https://www.surveymonkey.com/s/2W37JT2" target="_blank">
						<img src="<c:url value="/img/survey.png" />" alt="2012 Post Election Voter Survey" />
					</a>
				</div>
         --%>
         <%--
        <div class="featured summit">
					<a href="/initiatives-UOCAVAsummit-summit2013" target="_blank">
						<img src="<c:url value="/img/summit-banner.png" />" alt="2013 Seventh Annual Summit" />
					</a>
				</div>
				--%>

        <h1 id="title" class="site-title"><a href="<c:url value="/home.htm" />"><span>Overseas Vote</span></a></h1>
				
				<div id="site-tools">
					<div>
						<ul>
							<li class="voter-help-desk"><a href="https://vhd.overseasvotefoundation.org/index.php?/ovf/Knowledgebase/List"  rel="iframe width:800 height:450" class="vhd ceebox2">Voter Help Desk</a></li>
							&nbsp; | &nbsp;
							<li class="my-voter-account">
							<c:choose>
								<c:when test="${not empty userDetails and userDetails.id ne 0 }">
									<a href="<c:url value='/Login.htm'/>" title="View My Voter Account" class="login-link">Hi<c:if test="${fn:length(userDetails.name.firstName) gt 0}">, ${userDetails.name.firstName}</c:if></a>
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
	            <div class="content"><c:import url="${content}"/></div>
			</div>
		</div>

	</div>



</body>
</html>
