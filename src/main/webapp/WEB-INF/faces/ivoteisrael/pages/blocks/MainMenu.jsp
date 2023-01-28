<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<header id="navbar" role="banner" class="col-sm-12">
			<div class="navbar-header">
			<!-- .btn-navbar is used as the toggle for collapsed navbar content -->
			  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			  </button>
			</div>

			<div class="navbar-collapse collapse">
				<nav role="navigation">
				 <ul class="menu nav navbar-nav">
           <li class="first leaf"><a href="/vote/voter-registration-absentee-voting.htm" title="">Register to Vote</a></li>
           <li class="leaf"><a href="/vote/voter-registration-absentee-voting.htm" title="">Absentee Ballot</a></li>
           <li class="expanded dropdown"><a href="/vote/eoddomestic.htm" title="" data-target="#" class="dropdown-toggle" data-toggle="dropdown">Get Info <span class="caret"></span></a><ul class="dropdown-menu"><li class="first leaf"><a href="/vote/eoddomestic.htm">State Voting Information</a></li>
<li class="leaf"><a href="/state-elections/state-voting-laws-requirements" title="">Voting Laws &amp; Requirements</a></li>
<li class="last leaf"><a href="/vote/state-elections/state-election-dates-deadlines.htm">State Election Dates &amp; Deadlines</a></li>
</ul></li>
<li class="expanded dropdown"><a href="/vote/CandidateFinder.htm" title="" data-target="#" class="dropdown-toggle" data-toggle="dropdown">Find Answers <span class="caret"></span></a><ul class="dropdown-menu"><li class="first leaf"><a href="/voter-help-desk">Voter Help Desk</a></li>
<li class="last leaf"><a href="/vote/CandidateFinder.htm" title="Identify congressional candidates in your district and their party affiliations">Candidate Finder</a></li>
</ul></li>
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
    <a href="<c:url value='/Login.htm'/>" class="login-link">Voter Account</a>
</li>
</c:otherwise>
</c:choose>
</ul>
</ul>
</nav>
</div>
</header>