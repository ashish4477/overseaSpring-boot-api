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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<title>Online Voter Tools</title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/basic.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ovf.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/calendar.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/home.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />

	<!-- IE-specific stylesheet to work around the browser's lack of standards-compliance -->
	<!--[if IE]>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ie-bugfix.css"/>" />
	<![endif]-->
	<script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
    <%--<script src="<c:url value="/js/element-beta.js"/>" type="text/javascript"></script>--%>

	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>

    <%--<script src="<c:url value="/js/calendar-min.js"/>" type="text/javascript"></script>--%>
</head>
<body id="overseas-vote-foundation">
	<div id="fx-shadow"></div>
	<div id="fx-frame-layer">
		<div id="fx-frame-main"><div id="fx-frame">
            <c:import url="${content}"/>
		</div></div>
	</div>
	<div id="wrapper" class="eod">
        <%-- ADMIN MENU --%>
        <c:import url="/WEB-INF/faces/basic/pages/statics/AdminMenu.jsp" />
        <div id="head">
			<a id="home-link" href="http://exxonmobilcat.com">&nbsp;</a>
			<div>Overseas Citizen Absentee Voter Registration</div>
		</div>
		<div id="body">
            <c:import url="/WEB-INF/${relativePath}/pages/statics/LeftMenu.jsp" />

            <c:import url="/WEB-INF/${relativePath}/pages/blocks/Home.jsp"/>
		</div>
		<div id="foot">
			<c:import url="/WEB-INF/${relativePath}/pages/statics/Footer.jsp" />
		</div>		
	</div>
	

    <%-- Google Analytics --%>
	<script src="https://ssl.google-analytics.com/urchin.js" type="text/javascript">
	</script>
	<script type="text/javascript">
	_uacct = "UA-355872-7";
	urchinTracker();
	</script>

</body>
</html>
