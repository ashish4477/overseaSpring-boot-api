<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Overseas Vote Ohio | ${title}</title>
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/frame.css"/>" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/main.css"/>" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/home.css"/>" />
	<!-- IE-specific stylesheet to work around the browser's lack of standards-compliance -->
	<!--[if IE]>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ie-bugfix.css"/>" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ie-frame.css"/>" />
	<![endif]-->
	<!-- page-specific stylesheet -->
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/ohio.css"/>" />
</head>
<body>
	<div id="fx-shadow"></div>
	<div id="fx-frame-layer">
		<div id="fx-frame-main"><div id="fx-frame">
            <c:import url="${content}">
            		<c:param name="cStateId" value="" />
		       		<c:param name="cStateAbbr" value="" />
            		<c:param name="cStateName" value="" />
            </c:import>
		</div></div>
	</div>

            	<c:import url="/WEB-INF/${relativePath}/pages/templates/MainTemplate.jsp">
            		<c:param name="cStateId" value="" />
		       		<c:param name="cStateAbbr" value="" />
            		<c:param name="cStateName" value="" />
            	</c:import>

		<script type="text/javascript">
		
		  var _gaq = _gaq || [];
		  _gaq.push(['_setAccount', 'UA-355872-15']);
		  _gaq.push(['_setDomainName', 'usvotefoundation.org']);
		  _gaq.push(['_trackPageview']);
		
		  (function() {
		    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		  })();
		
		</script>
</body>
</html>
