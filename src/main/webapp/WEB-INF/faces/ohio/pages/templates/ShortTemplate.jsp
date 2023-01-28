<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 4, 2007
  Time: 8:02:08 PM
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

	<title>Overseas Vote Ohio | ${title}</title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/basic.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ovf.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/calendar.css"/>" />

	<!-- IE-specific stylesheet to work around the browser's lack of standards-compliance -->
	<!--[if IE]>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/ie-bugfix.css"/>" />
	<![endif]-->

	<script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>

	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>

	<script type="text/javascript">
		YAHOO.util.Event.onContentReady('wrapper', function() {
			var iFrameHeight = 0, documentEl = document.getElementById("wrapper");
			if(YAHOO.env.ua.ie > 0 && YAHOO.env.ua.ie < 7) {
				if(window.parent) {
					iFrameHeight = documentEl.offsetHeight;

					if(iFrameHeight == 0) {
						setTimeout(function() {
							window.parent.YAHOO.ovf.ravaOverlay.getPanel().showEvent.subscribe(function() {
								var iFrameHeight = documentEl.offsetHeight;
								if(iFrameHeight < 336) { iFrameHeight = 336; }
								window.parent.YAHOO.ovf.resizeRavaFrame(iFrameHeight);
							}, 500);
						});
					} else {
						if(iFrameHeight < 336) { iFrameHeight = 336; }
						window.parent.YAHOO.ovf.resizeRavaFrame(iFrameHeight);
					}
				}
			} else {
				h = YAHOO.util.Dom.getRegion("wrapper").bottom;
				if(h < 336) { 
						//var cont = YAHOO.util.Dom.getElementsByClassName("continue","*","wrapper")[0];
						//YAHOO.util.Dom.setStyle(cont, 'padding-top', (331-h) + "px");
						h = 336;
				}
				//alert(h);
				if(window.parent) {
					window.parent.YAHOO.ovf.resizeRavaFrame(h);
				}		
			}
		});
		
	</script>

    <style>
		/*
			This is to eliminate the flash of different background
			when the iFrame changes sizes.
		*/
		html {
			background: #FDF9DC;	
		}
	</style>
</head>
<body id="overseas-vote-foundation-short" class="yui-skin-sam">
	<div id="wrapper">
       <c:import url="${content}"/>
	</div>

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
