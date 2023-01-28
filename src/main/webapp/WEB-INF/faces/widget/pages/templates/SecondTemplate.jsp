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
    <!-- Global site tag (gtag.js) - Google Analytics -->
  <script async src="https://www.googletagmanager.com/gtag/js?id=UA-117344282-1"></script>
  <script>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', 'UA-117344282-1');
    // Loads the Linker plugin to track the iframe
    ga('require', 'linker');

    // Instructs the Linker plugin to automatically add linker parameters
    // to all links and forms pointing to the domain "destination.com".
    ga('linker:autoLink', ['http://register.overseasvotefoundation.org'], false, true);
  </script>
  <meta name="robots" content="noindex">
  <link rel="shortcut icon" href="<c:url value="/img/favicon.ico"/>" />
  <meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="pragma" content="no-cache" />

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

  <!-- HTML5 element support for IE6-8 -->
  <!--[if lt IE 9]>
    <script src="https://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/main.css"/>" />
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/widget.css"/>" />
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
  <div class="row header">
    <div class="container">
      <div class="col-xs-12 col-sm-10 col-sm-offset-1">
         <a href="<c:url value="/home.htm"/>"> <img class="logo pull-left" src="<c:url value="/img/ovf_logo.svg"/>" /></a>
        <h2><a href="<c:url value="/home.htm"/>">Nonpartisan Voter Services for U.S. Citizens Overseas and Uniformed Services Members</a></h2>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
    <c:import url="${content}" />
  </div>
</div>

  
<footer class="footer"> 
  <ul class="list-inline text-center">
    <li><a href="<c:url value="/home.htm"/>" title="">Home</a></li>
    <li><a href="<c:url value='/TermsOfUse.htm'/>">Terms of Use</a></li>
    <li><a href="<c:url value='/PrivacyPolicy.htm'/>">Privacy &amp; Security Policy</a></li>
    <li><a href="https://www.usvotefoundation.org/donate">Donate</a></li>
  </ul>

    <div class="col-xs-12 col-sm-10 col-sm-offset-1 text-center copyright">
  &copy; <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%> <a href="https://www.usvotefoundation.org" target="_blank">U.S. Vote Foundation</a>.
    <c:choose>
      <c:when test="${sectionName != 'home'}">
       All rights reserved. Overseas Vote is an Initiative of U.S. Vote Foundation.
           </c:when>
    </c:choose>
  </div>
<p>&nbsp;</p>
</footer>

  <script type="text/javascript" defer src="<c:url value="/js/iframeResizer.contentWindow.min.js"/>"></script>
  <script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
  <script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
  <script>
    $(function(){

      if ($('.uibutton')[0] || $('.uitooltip')[0]){
        /*** Handle jQuery plugin naming conflict between jQuery UI and Bootstrap ***/
        $.widget.bridge('uibutton', $.ui.button);
        $.widget.bridge('uitooltip', $.ui.tooltip);
      }

    });
  </script>
    <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

  <script type="text/javascript"> 
    $(function(){
        $('.vhd-link').on('click', function(){
          $('#vhd-iframe').attr('src', 'https://vhd.overseasvotefoundation.org/index.php?/ovf/Knowledgebase/List'); 

          $('#vhd-iframe').on('load', function(){
            $('#vhd-modal').modal('show');
          });    
        });

<c:if test="${sectionName eq 'home'}">
        function show_modal(){
          $('#donationModal').modal();
        }
        window.setTimeout(show_modal, 1000); // 3 second delay before it calls the modal function
</c:if>

    });
  </script>

</body>
</html>
  