<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="sidebar">
	<div class="sidebar-inner">
	    <ol class="primary-links">
	        <li class="rava-link"><a href="<c:url value='/w/rava.htm'/>">Voter Registration /<br/>Absentee Ballot Request</a></li>
	 		<li class="fwab-link"><a href="<c:url value='/FwabStart.htm'/>">Federal Write-in <br /><span>Absentee Ballot</span></a></li>
	        <li class="eod-link"><a href="<c:url value='/election-official-directory'/>">Election Official Directory</a></li>
	        <li class="svid-link"><a href="<c:url value='/state-voting-information'/>">State Voter <br /><span>Information Directory</span></a></li>
	        <li class="candidate-finder-link"><a href="<c:url value='/CandidateFinder.htm'/>">Candidate Finder</a></li>
	        <li class="election-dates-link"><a href="/election-dates-deadlines.htm">Election Dates &amp; Deadlines</a></li>
	        <li class="mailing-list-link"><a href="/mailing_list">Join Our Mailing List</a></li>
	    </ol>
	    <div class="secondary-links">
	    <c:if test="${sectionName eq 'home'}">
	    	<h2>Featured Videos</h2>
	    	<h3 style="margin:6px;">How to Request Your Absentee Ballot</h3>
	    	<p style="padding-bottom:6px;"><em>The A-Z of Overseas Voting</em></p>
	   		<a href="https://www.youtube.com/watch?v=ZGy3yxAm-Es" target="_blank" class="ceebox video-link"><img src="<c:url value='/img/sidebar/rava-absentee-voting-video.jpg'/>" width="188" style="padding-bottom:13px;" alt="Request your absentee ballot video" /></a>
	   </c:if>
        <a style="padding:12px; display:block;" href="https://www.usvotefoundation.org/" target="_blank"><img src="<c:url value='/img/us-vote-logo.png'/>" alt="U.S. Vote Foundation Logo" /></a>
        <p align="center"><a href="https://www.usvotefoundation.org/" target="_blank"><em>Experience our full range of services on our U.S. Vote Foundation Website</em></a>
        <p class="divider">&nbsp;</p>
	   	    <a href="/member-states"><img class="member-states" src="<c:url value='/img/member-states.gif'/>"></a>
	   	    <p class="divider">&nbsp;</p>
			<p align="center"><em>Your tax deductible donation to Overseas Vote helps keep this service available to U.S. citizens around the globe.</em><br/>
			<div class="btn donate"><a href="/donate">Donate</a></div>
	    </div>
	</div>
</div>