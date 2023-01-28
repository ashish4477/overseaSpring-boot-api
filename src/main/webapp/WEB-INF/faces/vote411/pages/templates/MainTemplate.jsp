<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <link rel="shortcut icon" href="https://www.vote411.org/themes/basic/favicon.ico" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="pragma" content="no-cache" />

    <!-- <title>U.S. Vote Foundation | ${title}</title> -->
    <title>VOTE411  | Powered by U.S. Vote Foundation | ${title}</title>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/yahoo-dom-event/yahoo-dom-event.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/connection/connection-min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/element/element-min.js"></script>

    <link rel="stylesheet" type="text/css" media="all" href="https://www.usvotefoundation.org/sites/all/themes/usvote_bootstrap_subtheme/css/style.css" />
    <link rel="stylesheet" type="text/css" media="all" href="https://www.usvotefoundation.org/vote/css/rava.css" />

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
    <script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-861740-3"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());
      gtag('config', 'UA-861740-3');
    </script>

  <!-- Vote411 stuff -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,400;0,500;0,600;0,700;1,400&display=swap" rel="stylesheet">
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/vote411.header.css"/>" />
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/vote411.footer.css"/>" />
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/vote411.usvote.css"/>" />
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
  <header>
    <div class="header-main">
      <div class="container">
        <div class="row">
          <div class="header-left col-12 col-sm-7 col-md-5">
            <a class="header-logo col-12 order-md-2" href="https://www.vote411.org">
              <img class="votelogo" src="<c:url value="/img/Vote411-logo_web_darkbg.svg"/>" alt="Vote 411" />
            </a>
          </div>

          <div class="header-right col-12 col-sm-5 col-md-7">
            <div class="header-links col-12">
              <a class="btn btn-primary" href="/vote/VoterInformation.htm">
                Register to Vote
              </a>

              <a class="btn btn-secondary" href="https://salsa.wiredforchange.com/o/5950/c/10124/p/salsa/donation/common/public/?donate_page_KEY=12592">
                Donate
              </a>

              <a class="btn btn-primary" href="https://www.vote411.org/select-state">
                Voting in my State
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>

  <div class="container-fluid">
    <div class="row navigation main-menu">
      <div class="container">
        <c:import url="/WEB-INF/${relativePath}/pages/blocks/MainMenu.jsp" />
      </div>
    </div>

    <div class="row content">
    <div class="main-container container">
      <header role="banner" id="page-header"></header>
          <a id="main-content"></a>
          <div class="content">
            <c:choose>
              <c:when test="${sectionName eq 'home' || sectionName eq 'login'}">
                <c:import url="/WEB-INF/${relativePath}/pages/blocks/Home.jsp" />
              </c:when>
              <c:otherwise>
                <c:import url="${content}" />
              </c:otherwise>
            </c:choose>
          </div>
    </div>
  </div>
  </div>

  <footer>
    <footer class="container text-center my-3 uvf-footer">
      Absentee Ballot Request and Election Official Directory Provided by U.S. Vote Foundation.
    </footer>

    <footer class="container-fluid election-day-problem-footer-outer">
        <div class="container election-day-problem-footer-content">
          <div class="row">
            <div class="col-md-4">
              <h2 class="election-day-title">
                Election day problems?
                <svg viewBox="0 0 27 27" xmlns="http://www.w3.org/2000/svg">
                  <path d="M10.734 5.663l-2.042 2.78c-.98 1.334-.828 3.37.342 4.541l4.852 4.851c1.164 1.165 3.203 1.325 4.54.343l2.78-2.043 4.503 4.503-2.706 3.018c-1.847 2.058-5.102 2.351-7.236.595 0 0-1.477-.89-6.868-6.28-5.39-5.391-6.28-6.868-6.28-6.868-1.73-2.15-1.465-5.388.595-7.236l3.018-2.706 4.502 4.502z" stroke="#EEF4F8" stroke-width="2" fill="none" fill-rule="evenodd" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </h2>
              <p class="subtitle">
                Report an election problem by calling:
              </p>
            </div>
            <div class="col-md-4">
              <div class="phone-number-row">
                1-866-OUR-VOTE (866-687-8683)
              </div>
              <div class="phone-number-row">
                1-888-VE-Y-VOTA (en Español)
              </div>
            </div>
            <div class="col-md-4">
              <div class="phone-number-row">
                1-888-API-VOTE (Asian multilingual assistance)
              </div>
              <div class="phone-number-row">
                1-844-YALLA-US (Arabic)
              </div>
            </div>
          </div>
        </div>
      </footer>

    <footer class="container-fluid main-footer-outer">
        <div class="container main-footer-content">
          <div class="row">
            <div class="col-md-4">
              <img class="votelogo" src="<c:url value="/img/Vote411-logo_web_darkbg.svg"/>" alt="VOTE411" />
              <p class="small-text">
                VOTE411 is committed to ensuring voters have the information they need to successfully participate in every election.
                Whether it's local, state or federal, every election is important to ensuring our laws and policies reflect the values
                and beliefs of our communities.
              </p>
              <div class="webby-award-logo">
                <img src="<c:url value="/img/webby-2020-banner.svg"/>" />
              </div>
            </div>
            <div class="col-md-8 col-lg-6 offset-lg-2">
              <div class="row">
                <div class="col-6 col-md-3 align-left mb-3 mb-md-0">
                  <ul class="main-footer-list">
                    <li>
                      <a href="mailto:voterinformation@lwv.org" target="_blank">Contact Us</a>
                    </li>
                    <li>
                      <a href="https://www.vote411.org/about" target="_self">About Us</a>
                    </li>
                    <li>
                      <a href="https://www.lwv.org/local-leagues/find-local-league?field_zip_code_value=" target="_blank">Get Involved</a>
                    </li>
                    <li>
                      <a href="https://www.vote411.org/sponsors" target="_self">Sponsors & Partners</a>
                    </li>
                  </ul>
                </div>
                <div class="col-6 col-md-3 align-left mb-3 mb-md-0">
                  <h2>Follow us</h2>
                  <ul class="main-footer-list">
                    <li>
                      <a class="main-footer-list__facebook" href="https://www.facebook.com/vote411/" target="_blank">Facebook</a>
                    </li>
                    <li>
                      <a class="main-footer-list__instagram" href="https://www.instagram.com/vote411/" target="_blank">Instagram</a>
                    </li>
                    <li>
                      <a class="main-footer-list__twitter" href="https://twitter.com/vote411" target="_blank">Twitter</a>
                    </li>
                  </ul>
                </div>
                <div class="col-md-6">
                  <h2>Brought to you by</h2>
                  <a href="https://www.lvw.org/about-us" target="_blank" aria-label="League of Women Voters" rel="noopener">
                    <img class="main-sponsor" src="<c:url value="/img/LWV-logo-knockout.svg"/>" alt="League of Women Voters" />
                  </a>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-md">
              <p class="small-text">
                ©2020 League of Women Voters Education Fund. All rights reserved.
              </p>
            </div>
            <div class="col-md-auto col-6">
              <a class="link-legal" href="https://www.vote411.org/legal">Terms of Use</a>
            </div>
            <div class="col-md-auto col-6">
              <a class="link-legal" href="https://www.vote411.org/legal#privacy-policy">Privacy Policy</a>
            </div>
          </div>
        </div>
      </footer>
  </footer>

  <c:import url="/WEB-INF/faces/basic/pages/statics/BrowserCheck.jsp" />
</body>
</html>