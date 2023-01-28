<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <link rel="shortcut icon" href="<c:url value="/img/favicon.ico"/>" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
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
    <link rel="stylesheet" type="text/css" media="all" href="https://www.usvotefoundation.org/vote/css/rava.css" />

    <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/main.css"/>" />
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/select2.css"/>" />
    <c:if test="${not empty externalCss }">
      <link rel="stylesheet" type="text/css" media="screen" href="<c:out value="${externalCss}"/>" />
    </c:if>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
    <script>
      $(function(){
        if ($('.uibutton')[0] || $('.uitooltip')[0]){
          /*** Handle jQuery plugin naming conflict between jQuery UI and Bootstrap ***/
          $.widget.bridge('uibutton', $.ui.button);
          $.widget.bridge('uitooltip', $.ui.tooltip);
        }
      });
    </script>
    <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-37719054-1"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());
      gtag('config', 'UA-37719054-1');
    </script>
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
  <div id="skip-link" style="display:none;">
    <a href="#main-content" class="element-invisible element-focusable">Skip to main content</a>
  </div>
  <div class="container-fluid">

  <div class="row top header">
    <div class="container">
        <div class="col-xs-12 col-sm-8 col-md-9">
            <a class="logo navbar-btn" href="https://skimmth.is/2ZLwmoq" title="Home">
                <img class="pull-left" src="/vote/img/theSkimmxUSVF_Logo.png" alt="Home">
            </a>
         </div>
        <div class="col-xs-12 col-sm-4 col-md-3 slogan">
            <h1 class="lead center-block">
                <span>U.S. Vote Foundation is a nonpartisan nonprofit dedicated to bringing state-specific voter services to millions of American citizens.</span>
            </h1>
        </div>
    </div>
  </div>

  <div class="row navigation main-menu">
    <div class="container">
      <c:import url="/WEB-INF/${relativePath}/pages/blocks/MainMenu.jsp" />
    </div>
  </div>

  <div class="row content">
   <div class="main-container container">
         <header role="banner" id="page-header">
             </header> <!-- /#page-header -->
         <div class="row main-content">
            <section class="col-sm-12">
            <a id="main-content"></a>
           <div class="content">
             <!-- content goes here -->
               <c:choose>
                   <c:when test="${sectionName eq 'home' || sectionName eq 'login'}">
                       <c:import url="/WEB-INF/${relativePath}/pages/blocks/Home.jsp" />
                   </c:when>
                   <c:otherwise>
                           <c:import url="${content}" />
                   </c:otherwise>
               </c:choose>
          </div>
           </section>
         </div>
       </div>
  </div>

  <footer class="footer">
    <div class="region region-footer">

      <section id="block-menu-menu-footer" class="block block-menu row clearfix">
        <ul class="menu nav">
          <li class="first leaf"><a href="https://skimmth.is/2Zb2D6S" target="_blank" title="">Skimm2020</a></li>
          <li class="leaf"><a href="https://www.theskimm.com/general/terms-of-service-1sK3tL1DJeMcIKGWYkUeo2-post" target="_blank">Terms of Use</a></li>
          <li class="last leaf"><a href="https://www.theskimm.com/general/privacy-notice-2qAl8zEbeImGG8CokaYI0m-post" target="_blank">Privacy &amp; Security Policy</a></li>  
        </ul>
      </section> <!-- /.block -->
      <section id="block-block-2" class="block block-block row clearfix">
        <section class="copyright">
          &copy; <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>  U.S. Vote Foundation. All rights reserved.
        </section>
      </section> <!-- /.block -->
    </div>
  </footer>
</div>

<c:import url="/WEB-INF/faces/basic/pages/statics/BrowserCheck.jsp" />
</body>
</html>