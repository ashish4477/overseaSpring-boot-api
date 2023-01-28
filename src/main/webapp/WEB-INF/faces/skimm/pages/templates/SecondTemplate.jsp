<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <link rel="shortcut icon" href="<c:url value="/img/favicon.ico"/>" />
  <meta http-equiv="X-UA-Compatible" content="IE=Edge">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="pragma" content="no-cache" />

  <!-- <title>U.S. Vote Foundation | ${title}</title> -->
  <title>Vote with theSkimm | Powered by U.S. Vote Foundation | ${title}</title>

  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/yahoo-dom-event/yahoo-dom-event.js"></script>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/connection/connection-min.js"></script> 
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/element/element-min.js"></script>

  <link type="text/css" rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" media="all" />

  <link rel="stylesheet" type="text/css" media="all" href="https://www.usvotefoundation.org/sites/all/themes/usvote_bootstrap_subtheme/css/style.css" />

  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/main.css"/>" />
  <!-- page-specific stylesheet -->
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
  <c:if test="${not empty externalCss }">
    <link rel="stylesheet" type="text/css" media="screen" href="<c:out value="${externalCss}"/>" />
  </c:if>

  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
  <script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
</head>
<c:choose>
  <c:when test="${not empty userDetails and userDetails.id ne 0}">
    <c:set var="logged" value=" logged-in " />
  </c:when>
  <c:otherwise>
    <c:set var="logged" value=" not-logged-in " />
  </c:otherwise>
</c:choose>

<body class="html not-front ${sectionName}${logged}">
  <!-- Google Tag Manager -->
  <noscript><iframe src="//www.googletagmanager.com/ns.html?id=GTM-K3DD34"
  height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
  <script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
  new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
  j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
  '//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
  })(window,document,'script','dataLayer','GTM-K3DD34');</script>
  <!-- End Google Tag Manager -->

  <div id="skip-link" style="display:none;">
    <a href="#main-content" class="element-invisible element-focusable">Skip to main content</a>
  </div>
  <div class="container-fluid">
<div class="row top">
  <div class="container">
    <div class="col-xs-4 col-sm-3 col-md-4 hidden-xs">&nbsp;</div>
    <div class="col-xs-12 col-sm-9 col-md-8 site-login">
        <ul class="nav menu site-tools pull-right">
        <c:choose>
          <c:when test="${not empty userDetails and userDetails.id ne 0 }">
            <li class="mva expanded dropdown">
            <a href="#" data-toggle="dropdown" role="button" id="drop4" class="dropdown-toggle">Voter Account <span class="caret"></span></a>
            <ul role="menu" class="dropdown-menu" id="menu1">
              <li role="presentation" class="first leaf"><a href="<c:url value="/Login.htm"/>" tabindex="-1" role="menuitem">Voter Account</a></li>
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
              <a href="<c:url value='/Login.htm'/>" class="login-link">Voter Account (Login)</a>
            </li>
          </c:otherwise>
          </c:choose>
        </ul>
    </div>
  </div>
</div>


<div class="row top">
  <div class="container">
        <div class="col-xs-4 col-sm-3 col-md-4"><a class="logo navbar-btn" href="/" title="Home">
  <img class="hidden-xs hidden-sm pull-left" src="/sites/all/themes/usvote_bootstrap_subtheme/logo.png" alt="Home">
  <img class="visible-xs visible-sm center-block img-responsive" src="/sites/all/themes/usvote_bootstrap_subtheme/logo_sm.png" alt="Home">
  </a></div>
      <div class="col-xs-8 col-sm-9 col-md-8 pull-right slogan">
      <h1 class="lead center-block"><span>Absentee Ballot Request and Voter Registration Services for All U.S. Voters in All States at Home and Abroad</span></h1>
      </div>
</div>
</div>

<div class="row navigation main-menu">
  <div class="container">
    <c:import url="/WEB-INF/${relativePath}/pages/blocks/MainMenu.jsp" />
  </div>
</div>

<c:choose>
  <c:when test="${not empty userDetails and userDetails.id ne 0 and fn:contains(sectionName, 'mva')}">
  <div class="row mva-account">
    <div class="container">
      <div class="col-xs-12 account-nav">
        <div class="row">
        <div class="col-xs-2 col-sm-2 col-md-1">
          <img class="memberBadge" width="97" height="89" src="<c:url value="/img/icons/membership.png"/>">
          <fmt:formatDate value="${user.created}" pattern="yyyy" var="userCreated"/>
          <fmt:formatDate value="${user.lastUpdated}" pattern="yyyy" var="lastUpdated"/>
          <c:choose>
            <c:when test="${userCreated == 1900 && lastUpdated == 1900}">
              &nbsp;
            </c:when>
            <c:when test="${userCreated == 1900 && lastUpdated > 1900}">
              <br/>
               <small>Since ${lastUpdated}</small>
            </c:when>
            <c:otherwise>
              <br/>
              <small>Since ${userCreated}</small>
            </c:otherwise>
          </c:choose>
         </div>
        <div class="col-xs-10 col-sm-5 user">
            <h1>My Voter Account</h1>
            <h3><c:if test="${not empty user.name.firstName}">${user.name.firstName}</c:if> <c:if test="${not empty user.name.lastName}">${user.name.lastName}</c:if></h3>
        </div>
        <div class="col-xs-12 col-sm-6 thanks hidden-xs hidden-sm">
            <c:if test="${not empty membershipStat}">
                <h3>Thank YOU for Voting!</h3>
                <c:forEach var="myear" items="${membershipStat}"><div class="membership stat"> Election ${myear}</div></c:forEach>
            </c:if>
        </div>
        </div>

      </div>
      <div class="col-xs-12 account-sub-nav">
         <div class="row">
            <div class="col-xs-12 col-sm-10 col-sm-offset-1">
                <ul>
                    <li class="profile"><a href="<c:url value="/Login.htm"/>">Profile</a></li>
                    <li class="contacts"><a href="<c:url value='/MyKeyContacts.htm'/>">Contacts</a></li>
                    <li class="dates"><a href="<c:url value='/MyDates.htm'/>">Dates &amp; Deadlines</a></li>
                    <li class="my-reps"><a href="<c:url value='/MyReps.htm'/>">My Reps</a></li>
                    <li class="key-votes"><a href="<c:url value='/KeyVotes.htm'/>">Key Votes</a></li>
                    <!-- <li class="in-the-news"><a href="<c:url value='/InTheNews.htm'/>">In The News</a></li> -->
                    <li class="donate"><a href="/donate">Donate</a></li>
                </ul>
            </div>
          </div>
      </div>

    </div>
  </div>
  </c:when>
</c:choose>
<div class="row content">
<div class="main-container container">
  <header role="banner" id="page-header">
      </header> <!-- /#page-header -->
  <div class="row main-content">
    <section class="col-sm-12">
      <a id="main-content"></a>
      <div class="content">
      <!-- content goes here -->
           <c:import url="${content}" />
   </div>
    </section>
  </div>
</div>
</div>
<footer class="footer">
<div class="region region-footer">
  <section id="block-menu-menu-footer" class="block block-menu row clearfix">
    <ul class="menu nav"><li class="first leaf"><a href="/" title="">Home</a></li>
    <li class="leaf"><a href="<c:url value='TermsOfUse.htm'/>">Terms of Use</a></li>
    <li class="last leaf"><a href="/privacy-policy">Privacy &amp; Security Policy</a></li>
  </ul>
  </section> <!-- /.block -->
  <section id="block-block-2" class="block block-block row clearfix">
    <section class="copyright">
  &copy; <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>  U.S. Vote Foundation. All rights reserved. <a href="https://www.overseasvotefoundation.org" target="_blank">Overseas Vote</a> is an Initiative of U.S. Vote Foundation.
    </section>
  </section> <!-- /.block -->
  </div>
</footer>
</div>

</body>
</html>
  