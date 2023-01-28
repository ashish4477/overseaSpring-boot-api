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

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script src="<c:url value="/js/bootstrap/bootstrap.min.js"/>" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/bootstrap/bootstrap.css"/>"/>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.swfobject.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.metadata.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.color.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.ceebox.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ceebox/js/jquery.ceeboxrun.js"/>"></script>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/scripts/ceebox/css/ceebox.css"/>" />

    <!-- page-specific stylesheet -->
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/shs.css"/>" />
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/newyork.css"/>" />
    <script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/element-min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
</head>
<body class="${sectionName} fixed newyork">
<div class="state-seal">
    <div class="container wrapper">
        <div class="navbar header">
            <div class="navbar-inner">
                <div class="container">
                    <div class="row heading"><a class="logo" href="<c:url value="/"/>">&nbsp;</a></div>
                </div><!--/.nav-collapse -->
            </div>
        </div>

        <div class="container">
            <div class="navbar">
                <ul class="nav site-tools pull-right">
                    <li class="vhd"><a
                            href="https://vhd.overseasvotefoundation.org/index.php?/newyork/Knowledgebase/List" rel="iframe width:800 height:450" class="ceebox2">Voter
                        Help Desk</a>
                    </li>
                    <li class="divider-vertical"></li>
                    <c:choose>
                        <c:when test="${not empty userDetails and userDetails.id ne 0 }">
                            <ul class="nav nav-pills">
                                <li class="mva dropdown active">
                                    <a href="#" data-toggle="dropdown" role="button" id="drop4" class="dropdown-toggle">Hi<c:if test="${fn:length(userDetails.name.firstName) gt 0}">, ${userDetails.name.firstName}</c:if> <b class="caret caret-override"></b></a>
                                    <ul aria-labelledby="drop4" role="menu" class="dropdown-menu" id="menu1">
                      <li role="presentation"><a href="<c:url value="/Login.htm"/>" tabindex="-1" role="menuitem">Voter Account</a></li>
                        <li role="presentation"><a href="<c:url value="/UpdateAccount.htm"/>" tabindex="-1" role="menuitem">Edit Profile</a></li>
                                        <li role="presentation"><a href="<c:url value="/ChangePassword.htm"/>" tabindex="-1" role="menuitem">Change Password</a></li>
                                        <li role="presentation"><a href="<c:url value="/RemoveAccount.htm"/>" tabindex="-1" role="menuitem">Remove Account</a></li>
                                        <li class="divider" role="presentation"></li>
                                        <li role="presentation"><a href="<c:url value="/logout"/>" tabindex="-1" role="menuitem">Log Out</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <li class="mva">
                                <a href="<c:url value='/Login.htm'/>" class="login-link">My Voter Account</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>

        <div class="container" id="container">
            <div class="row">
                <div class="span3">
                    <c:import url="/WEB-INF/${relativePath}/pages/statics/LeftMenu.jsp">
                        <c:param name="cStateId" value="35" />
                        <c:param name="cStateName" value="New York" />
                    </c:import>
                </div><!--/span-->
                <div class="span9">

                    <!-- import home page content when login screen is displayed -->
                    <c:choose>
                        <c:when test="${sectionName eq 'home' || sectionName eq 'login'}">
                            <c:import url="/WEB-INF/${relativePath}/pages/blocks/Home.jsp" />
                        </c:when>
                        <c:otherwise>
                            <div class="well featured">
                                <c:import url="${content}">
                                    <c:param name="cStateId" value="35" />
                                    <c:param name="cStateName" value="New York" />
                                </c:import>
                            </div>
                        </c:otherwise>
                    </c:choose>

                </div><!--/span9-->
            </div><!--/row-->
            <div class="container">
                <div class="row">
                    <div class="span12">
                        <hr class="footerLine" />
                        <div class="navbar">
                            <c:import url="/WEB-INF/${relativePath}/pages/statics/Footer.jsp" />
                        </div>
                    </div>
                </div>

            </div>
        </div> <!--/container-->
    </div> <!--/container fixed-->
</div>
<c:import url="/WEB-INF/faces/basic/pages/statics/BrowserCheck.jsp" />

<%-- Google Analytics --%>
<script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-355872-20']);
    _gaq.push(['_trackPageview']);

    (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();

</script>


</body>
</html>
