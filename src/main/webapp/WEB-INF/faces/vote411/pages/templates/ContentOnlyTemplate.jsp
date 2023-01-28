<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Overseas Vote | ${title}</title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/main.css"/>" />
	<!-- IE-specific stylesheet to work around the browser's lack of standards-compliance -->
	<!--[if IE]>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ie-bugfix.css"/>" />
	<![endif]-->
	<!-- page-specific stylesheet -->
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
	<script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/element-min.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>

</head>
<body>
	<div id="container">
		<div id="head">
			<a href="<c:url value='/home.htm'/>" id="home-link"><span>STATE_NAME<br />Military &amp; Overseas Voter Services</span></a>
		</div>
		<div id="main">
            <div id="content">
            	<c:import url="${content}">
            		<c:param name="cStateId" value="NEED_STATE_ID" />
            		<c:param name="cStateName" value="NEED_STATE_NAME" />
            	</c:import>
            </div>
		</div>
	</div>
    <%-- Google Analytics --%>
    <script src="https://ssl.google-analytics.com/urchin.js" type="text/javascript">
    </script>
    <script type="text/javascript">
    _uacct = "UA-355872-XX";
    urchinTracker();
    </script>
</body>
</html>

