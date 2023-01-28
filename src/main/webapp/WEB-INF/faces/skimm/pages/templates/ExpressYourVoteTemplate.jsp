<%--
Created by IntelliJ IDEA.
User: Leo
Date: Jun 19, 2008
Time: 10:03:19 PM
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Overseas Vote | ${title}</title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

  <meta name="Description" content="Express Your Vote, Express Ballot Return with FedEx Express: Reduced Rates Available from Overseas Vote for Express Ballot Return to the U.S. Available Now through Oct 31 2012 in 94 Countries!"/>

	<link rel="stylesheet" type="text/css" media="all" href="css/reset-fonts.css" />
	<link rel="stylesheet" type="text/css" media="all" href="css/eyv-main.css" />

	<script src="<c:url value="/scripts/jquery.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/yui-rollup.js" />" type="text/javascript"></script>
	<script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>

	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/scripts/eyv.js"/>" type="text/javascript"></script>

</head>

<body>
	<div id="wrapper">
		<div id="header" class="middle">
			<a href="https://www.overseasvotefoundation.org" class="ovf-link"><img src="img/logo-ovf-eyv.gif" alt="Overseas Vote" /></a>
			<h1><a href="<c:url value="/ExpressYourVote" />">Express Your Vote</a></h1>
			<a href="http://www.fedex.com" class="fedex-link"><img src="img/logo-fedex.gif" alt="FedEx" /></a>
		</div>
    <div id="subheader" class="middle">
      <h2 style="margin-left:-95px; color:#B90000;">Offer Expired  November 1, 2012*</h2>
      <%-- if Program Running<p style="margin:0 0 0 -57px; font-size:12px;">*Closing dates and times for this offer vary by country.</p> --%>
    </div>
		<div id="content" class="middle">
			<c:if test="${not empty loggedUser}"><div id="userInfo">&nbsp;You are logged in as <c:out value="${loggedUser.username }"/> | <a href="<c:url value="/logout"/>" target="_top">log out</a></div></c:if>
			<c:import url="${content}"/>
		</div>

		<div id="footer" class="middle"><a href="<c:url value="/eyvCms.htm?uri=/expressyourvote-terms" />" target="_blank" class="frame-link">Disclaimer</a> | <a class="frame-link" href="https://vhd.overseasvotefoundation.org/unified/index.php?group=eyv"  target="_blank">Contact Express Help Desk</a> | <a href="<c:url value="/w/rava.htm" />">Register to Vote</a></div>
	</div>

    <%-- Google Analytics --%>
    <script type="text/javascript">

      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-355872-26']);
      _gaq.push(['_trackPageview']);

      (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();

    </script>

</body>
</html>
