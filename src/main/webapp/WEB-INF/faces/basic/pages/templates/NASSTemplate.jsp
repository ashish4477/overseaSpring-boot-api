<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 14, 2007
  Time: 7:25:46 PM
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

	<title>NASS EOD</title>
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value='/css/eod-nass.css'/>" />

	<!-- IE-specific stylesheet to work around the browser's lack of standards-compliance -->
	<!--[if IE]>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ie-bugfix.css"/>" />
	<![endif]-->

	<script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/element-min.js"/>" type="text/javascript"></script>

	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>

</head>
<body id="nass">
	<div id="logo"><div><a href="http://canivote.org" ><img src="<c:url value="/img/nass/NASS-Logo.jpg"/>" alt="National Association of Secretaries of State" /></a></div></div>
    <div id="wrapper" class="nass">
         <div id="body">
            <c:import url="${content}"/>
            <p id="poweredby"><em>Powered by <a href="https://www.usvotefoundation.org<c:url value='/'/>"> U.S. Vote Foundation</a></em></p>
        </div>
    </div>

	<script src="https://ssl.google-analytics.com/urchin.js" type="text/javascript">
	</script>
	<script type="text/javascript">
		_uacct = "UA-355872-2";
		urchinTracker();
	</script>
  </body>
</html>