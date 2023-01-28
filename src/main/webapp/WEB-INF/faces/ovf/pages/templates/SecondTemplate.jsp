<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <link rel="shortcut icon" href="<c:url value="/img/favicon.ico"/>" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="pragma" content="no-cache" />
    <meta name="facebook-domain-verification" content="ffw9jahiph4sj1pl64merpq8ecjalh" />

    <title>Overseas Vote | ${title}</title>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/yahoo-dom-event/yahoo-dom-event.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/connection/connection-min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/element/element-min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/animation/animation-min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/button/button-min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/calendar/calendar-min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/dragdrop/dragdrop-min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/container/container-min.js"></script>

  <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css" rel="stylesheet">

  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>


  <!-- HTML5 element support for IE6-8 -->
  <!--[if lt IE 9]>
    <script src="https://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/main.css"/>" />
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/ovf.css"/>" />
  <!-- page-specific stylesheet -->
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
  <c:if test="${not empty externalCss }">
      <link rel="stylesheet" type="text/css" media="screen" href="<c:out value="${externalCss}"/>" />
  </c:if>
</head>

<c:choose>
  <c:when test="${not empty userDetails and userDetails.id ne 0}">
    <c:set var="logged" value=" logged-in" />
  </c:when>
  <c:otherwise>
    <c:set var="logged" value=" not-logged-in" />
  </c:otherwise>
</c:choose>
<authz:authorize ifAllGranted="ROLE_ADMIN">
  <c:set var="admin" value=" admin" />
</authz:authorize>
<body class="html not-front ${sectionName}${logged}${admin}">
<!-- Google tag (gtag.js) --> <script async src="https://www.googletagmanager.com/gtag/js?id=G-6Y3PF6DD5Z"></script> <script> window.dataLayer = window.dataLayer || []; function gtag(){dataLayer.push(arguments);} gtag('js', new Date()); gtag('config', 'G-6Y3PF6DD5Z'); </script>

  <div id="skip-link" style="display:none;">
    <a href="#main-content" class="element-invisible element-focusable">Skip to main content</a>
  </div>
  <div class="navbar navbar-fixed-top header">
   <div class="row">
    <div class="container">
      <div class="col-xs-12 col-sm-6 col-md-6">
            <a href="<c:url value="/home.htm"/>"> <img class="logo pull-left" src="<c:url value="/img/ovf_logo.svg"/>" /></a>
            <h2>
              <a href="<c:url value="/home.htm"/>">Nonpartisan Voter Services for U.S. Citizens Overseas and Uniformed Services Members</a>
              <div class="usvote-callout-header visible-xs mobile">
                <div class="part-1"><a href="https://www.usvotefoundation.org">VISIT OUR NEW</a></div>
                <div class="part-2"><a href="https://www.usvotefoundation.org">Mobile Optimized Website</a></div>
              </div>
            </h2>
    </div>
    <div class="col-md-2 col-sm-3 hidden-xs">
      <div class="usvote-callout-header">
        <div class="part-1"><a href="https://www.usvotefoundation.org">VISIT OUR NEW</a></div>
        <div class="part-2"><a href="https://www.usvotefoundation.org">Mobile Optimized Website</a></div>
      </div>
    </div>
    <div class="col-md-2 hidden-xs hidden-sm social-header">
      <ul class="nav navbar-nav">
        <li><a href="https://www.facebook.com/overseas.vote.foundation" class="icon-facebook" target="_blank"><img src="<c:url value="/img/icons/f.png"/>"></a></li>
        <li><a href="https://twitter.com/overseasvote" class="icon-twitter" target="_blank"><img src="<c:url value="/img/icons/t.png"/>"></a></li>
        <li><a href="https://www.instagram.com/usvote" class="icon-instagram" target="_blank"><img src="<c:url value="/img/icons/i.png"/>"></a></li>
      </ul>
    </div>
    <div class="col-xs-2 col-sm-3 col-md-2 hidden-xs account">
      <ul class="nav nav-pills nav-stacked">
        <!--<li><a href="#" data-toggle="modal" data-target=".bs-example-modal-lg"><span>Voter Help Desk</span></a></li>-->
          <c:choose>
            <c:when test="${not empty userDetails and userDetails.id ne 0 }">
              <li class="mva expanded dropdown"><a href="#" data-toggle="dropdown" role="button" id="drop4" class="dropdown-toggle">Voter Account <span class="caret"></span></a>
                <ul role="menu" class="dropdown-menu" id="menu1">
                  <li role="presentation" class="first leaf"><a href="<c:url value="/Login.htm"/>" tabindex="-1" role="menuitem">Voter Account</a></li>
                    <li role="presentation"><a href="<c:url value="/UpdateAccount.htm"/>" tabindex="-1" role="menuitem">Edit Profile</a></li>
                    <li role="presentation"><a href="<c:url value="/ChangePassword.htm"/>" tabindex="-1" role="menuitem">Change Password</a></li>
                    <li role="presentation"><a href="<c:url value="/RemoveAccount.htm"/>" tabindex="-1" role="menuitem">Remove Account</a></li>
                    <li class="divider" role="presentation"></li>
                    <authz:authorize ifAllGranted="ROLE_FACE_ADMIN" >
                        <li><a href="<c:url value="/reportingdashboard/ReportingDashboard.htm"/>">Reporting Dashboard</a></li>
                    </authz:authorize>
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
</div>
    <nav class="navbar navbar-inverse navbar-fixed-top second-navbar">
      <div class="container text-center">
        <div class="navbar-header">
             <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#mobile-nav" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>

        <div class="collapse navbar-collapse" id="mobile-nav">
        <div class="container main-navigation">
          <ul class="nav nav-justified" id="navbar">
            <li class="first leaf"><a href="#" data-toggle="modal" data-target="#registration-redirect-popup">Register to Vote<br/>Absentee Ballot</a></li>
            <li class="leaf"><a href="<c:url value='/state-elections/state-election-dates-deadlines.htm'/>">Election Dates<br/> &amp; Deadlines</a></li>
            <li class="leaf"><a href="<c:url value='/svid.htm'/>">State Voting<br/> Requirements</a></li>
            <li class="leaf"><a href="<c:url value='/election-official-directory'/>">Election Official <br/>Directory</a></li>
             <li class="leaf"><a href="https://voterhelpdesk.usvotefoundation.org/" target="_blank">Voter Help<br/> Desk</a></li>
            <li class="leaf visible-xs"><a href="/vote/Login.htm">Voter Account</a></li>
          </ul>
          </div>
        </div><!-- /.navbar-collapse -->
      </div>
    </nav>
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
                    <li class="donate"><a href="https://www.usvotefoundation.org/donate" target="_blank">Donate</a></li>
                </ul>
            </div>
          </div>
      </div>
    </div>
  </div>
  </c:when>
</c:choose>

  <div id="vhd-modal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <iframe id="vhd-iframe" width="100%" style="min-height:450px; height:600px;" frameborder="0" vspace="0" hspace="0" webkitallowfullscreen="" mozallowfullscreen="" allowfullscreen="" scrolling="auto"></iframe>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
    <c:import url="${content}" />
  </div>
</div>

<footer class="footer">
<hr/>
  <ul class="list-inline text-center">
    <li><a href="/" title="">Home</a></li>
    <li><a href="<c:url value='/TermsOfUse.htm'/>">Terms of Use</a></li>
    <li><a href="<c:url value='/PrivacyPolicy.htm'/>">Privacy &amp; Security Policy</a></li>
    <li><a href="https://www.usvotefoundation.org/donate">Donate</a></li>
  </ul>

    <div class="col-xs-12 col-sm-10 col-sm-offset-1 text-center copyright">
  &copy; <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%> <a href="https://www.usvotefoundation.org" target="_blank">U.S. Vote Foundation</a>. All rights reserved. Overseas Vote is an Initiative of U.S. Vote Foundation.
  </div>
<p>&nbsp;</p>
</footer>
<c:import url="/WEB-INF/${relativePath}/pages/blocks/RegistrationRedirectDialog.jsp"/>

  <script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <c:import url="/WEB-INF/faces/basic/pages/statics/ZohoTracking.jsp" />
</body>
</html>
