<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="pragma" content="no-cache" />
    <title>${title} | iVoteIsrael </title>

  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
   <link type="text/css" rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" media="all" />
   
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
  <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/jquery.fancybox.css"/>" />
  <script src="<c:url value="/js/fancybox/jquery.fancybox.js"/>" type="text/javascript"></script>

  <link rel="stylesheet" href="<c:url value="/css/magazinly.css"/>" type="text/css" media="screen">

  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/ivoteisrael.css"/>" />

   <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/main.css"/>" />
  <!-- page-specific stylesheet -->
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />

  <script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/element-min.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>

  <script>
    $(document).ready(function() {
        $('.fancybox').fancybox();
    });
    
    $(document).ready(function() {
    $(".dropdown-toggle").dropdown();
});

  </script>
<%-- Google Analytics from ivoteIsrael --%>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
  ga('create', 'UA-78899-40', 'auto');
  ga('send', 'pageview');
</script>

<%-- Google Analytics --%>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-355872-28', 'auto');
  ga('send', 'pageview');

</script>
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-W3MSHS6');</script>
</head>
<body class="${sectionName} ivoteisrael page page-id-697 page-template-default donate td-custom-background td-custom-background td-custom-background td-custom-background td-custom-background wpb-js-composer js-comp-ver-3.6.8 vc_responsive custom-background">

<div class="container header-wrap td-header-wrap-3">
  <div class="row">
    <div class="col-xs-12 header-logo-wrap">
      <a class="logo" href="http://ivoteisrael.com"><img src="<c:url value='/img/ivoteisrael-logo-new.png' />" alt="ivote israel logo"/></a>
    </div>
  </div>
</div>

<div class="container td-page-wrap">
  <div class="row">
    <div class="col-xs-12">
          <ul class="nav menu site-tools pull-right">
			  <li class="vhd">
				  <a href="https://vhd.overseasvotefoundation.org/index.php?/ivoteisrael/Knowledgebase/List" rel="iframe width:800 height:450" class="fancybox various fancybox.iframe">Voter
				  Help Desk</a>
			  </li>
			  <li class="divider-vertical">|</li>
          <c:choose>
            <c:when test="${not empty userDetails and userDetails.id ne 0 }">
            <li class="mva expanded dropdown">
            <a href="#" data-toggle="dropdown" role="button" id="drop4" class="dropdown-toggle">Voter Account <span class="caret"></span></a>
            <ul role="menu" class="dropdown-menu" id="menu1">
              <li role="presentation" class="first leaf"><a href="<c:url value="/MyVotingInformation.htm"/>" tabindex="-1" role="menuitem">Voter Account</a></li>
                <li role="presentation"><a href="<c:url value="/UpdateAccount.htm"/>" tabindex="-1" role="menuitem">Edit Profile</a></li>
                <li role="presentation"><a href="<c:url value="/ChangePassword.htm"/>" tabindex="-1" role="menuitem">Change Password</a></li>
                <li role="presentation"><a href="<c:url value="/RemoveAccount.htm"/>" tabindex="-1" role="menuitem">Remove Account</a></li>
                <li class="divider" role="presentation"></li>
                <li role="presentation" class="last leaf"><a href="<c:url value="/logout"/>" tabindex="-1" role="menuitem">Log Out</a></li>
            </ul>
            </li>
            </c:when>
        <c:otherwise>
          <li class="mva">
            <a href="<c:url value='/Login.htm'/>" class="login-link">Voter Account</a>
          </li>
        </c:otherwise>
        </c:choose>
		  </ul>
       </div>
    </div>


    <div class="row">

      <c:choose>
        <c:when test="${sectionName eq 'home' || sectionName eq 'login'}">
          <div class="col-xs-12 col-sm-10 col-sm-offset-1">
            <c:import url="/WEB-INF/${relativePath}/pages/blocks/Home.jsp" />
           </div>
        </c:when>
        <c:otherwise>
          <div class="col-xs-12 col-sm-10 col-sm-offset-1">
            <c:import url="${content}" />
          </div>
        </c:otherwise>
      </c:choose>
      </div>
      </div>



<div class="container td-footer-line">
  <div class="row">
    <div class="col-sm-12"></div>
  </div>
</div>

<div class="container td-footer-wrap">
  <div class="row">
    <div class="col-xs-12">
      <div class="td-grid-wrap">
        <div class="container-fluid">
          <div class="wpb_row row-fluid ">
            <div class="col-xs-12 wpb_column column_container"><br/><br/></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div class="container td-sub-footer-wrap">
  <div class="row">
    <div class="col-xs-12">
      <div class="td-grid-wrap">
        <div class="container-fluid">
          <div class="row-fluid ">
            <div class="col-xs-12 col-sm-4 td-sub-footer-copy"> &copy; <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>  Worldwide by <a href="http://ivoteisrael.com" target="_blank">iVoteIsrael</a>. All rights reserved.  Does not support any candidate or candidate’s committee.</div>
            <div class="col-sm-8 td-sub-footer-menu">
             <c:import url="/WEB-INF/${relativePath}/pages/statics/Footer.jsp" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<c:import url="/WEB-INF/faces/basic/pages/statics/BrowserCheck.jsp" />
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-W3MSHS6"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
</body>
</html>
