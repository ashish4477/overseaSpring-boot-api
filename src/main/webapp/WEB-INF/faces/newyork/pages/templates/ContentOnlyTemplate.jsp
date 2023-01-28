<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Overseas Vote New York | ${title}</title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/bootstrap/bootstrap.css"/>"/>
	<!-- page-specific stylesheet -->
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/shs.css"/>" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/vermont.css"/>" />
	<script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/element-min.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
</head>
<body class="${sectionName} standAlone">

 <div class="container header navbar span10">
      <div class="navbar-inner">
        <div class="container">
			<div class="row heading">
				<div class="span2">
				<a class="logo" href="<c:url value="/home.htm"/>"><img src="<c:url value='/img/vermont-logo.png' />" alt="vermont-logo"/></a>
				</div>
				<div class="span7">
					<a  href="<c:url value="/home.htm"/>" class="brand"><h1>Overseas and Military Voter Services</h1>
					<h2>Vermont Secretary of State - Jim Condos</h2>
					</a>
				</div>
			</div>
          </div><!--/.nav-collapse -->
        </div>
      </div>

	
	<div class="container span10" id="container">
	<div class="row spacer">
		<div>&nbsp;</div>
	</div>
      <div class="row">
		<div class="bodyContent">   
			<div class="well featured" id="container">
				<div class="container span9">
					<c:import url="${content}">
					<c:param name="cStateId" value="50" />
					<c:param name="cStateName" value="Vermont"/>
					</c:import>
				</div>
			</div>
		</div><!--/span10-->
      </div><!--/row-->
       <div class="container span10">
			<div class="row">
				<div>

					<hr class="footerLine" />

					<div class="navbar">
						<div class="container footer">
							<div class="nav-collapse">
								<ul class="nav">
									<li><a href="/home.htm">Home</a>
									</li>
									<li class="divider-vertical"></li>
									<li><a href="<c:url value="TermsOfUse.htm"/>">Terms of Use</a>
									</li>
									<li class="divider-vertical"></li>
									<li><a
										href="<c:url value='PrivacyPolicy.htm'/>">Privacy &amp; Security Policy</a>
									</li>
									<li class="divider-vertical"></li>
									<li><a href="https://www.overseasvotefoundation.org" target="_blank">Site Provided By Overseas Vote</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
</div>
		</div><!--/container-->

    
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
