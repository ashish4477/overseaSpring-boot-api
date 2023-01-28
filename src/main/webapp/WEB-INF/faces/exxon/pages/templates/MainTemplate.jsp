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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<title>Online Voter Tools</title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/basic.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ovf.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/calendar.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />

	<!-- IE-specific stylesheet to work around the browser's lack of standards-compliance -->
	<!--[if IE]>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ie-bugfix.css"/>" />
	<![endif]-->
	
	<!-- ceebox and prereqs-->	
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
  <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.swfobject.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.metadata.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.color.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.ceebox.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.ceeboxrun.js"/>"></script>
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/scripts/ceebox/css/ceebox.css"/>" />
	

  <script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/element-min.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>

    <%--<script src="<c:url value="/js/calendar-min.js"/>" type="text/javascript"></script>--%>

</head>
<body id="overseas-vote-foundation" class="${sectionName} exxon">
	<div id="wrapper">
        <%-- ADMIN MENU --%>
        <c:import url="/WEB-INF/faces/basic/pages/statics/AdminMenu.jsp" />
        <div id="head">
           <!-- Express Your Vote
          <div class="express-your-vote-banner" style="float:right;">
            <a href="<c:url value="/ExpressYourVote.htm"/>">
                <img alt="Express Your Vote" src="<c:url value="/img/buttons/express-your-vote-button.png"/>">
            </a>
          </div>
         -->
			<a id="home-link" href="http://exxonmobilcat.com">&nbsp;</a>
			<div class='rounded-corner-box'>
				<div class='rounded-corner-left'><!-- valid --></div>
				<div class='rounded-corner-content'><h1>Overseas Citizen Absentee Voter Registration</h1></div>
				<div class='rounded-corner-right'><!-- valid --></div>
			</div>
		</div>

		<div id="body">
            <c:import url="/WEB-INF/${relativePath}/pages/statics/LeftMenu.jsp" />

			<div id="content">
            	<c:import url="${content}"/>
			</div>
		</div>
		<div id="foot">
			<div class='rounded-corner-box'>
				<div class='rounded-corner-left'><!-- valid --></div>
				<div class='rounded-corner-content'><c:import url="/WEB-INF/${relativePath}/pages/statics/Footer.jsp" /></div>
				<div class='rounded-corner-right'><!-- valid --></div>
			</div>
		</div>
	</div>
	
	<c:import url="/WEB-INF/faces/basic/pages/statics/BrowserCheck.jsp" />

    <%-- Google Analytics --%>
	<script src="https://ssl.google-analytics.com/urchin.js" type="text/javascript">
	</script>
	<script type="text/javascript">
	_uacct = "UA-355872-7";
	urchinTracker();
	</script>

</body>
</html>
