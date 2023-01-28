<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 28, 2007
  Time: 6:19:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<div class="well sidebar-nav">
     <ul class="nav nav-list">
        <li><a class="ballot-access" href="https://www.secureballotusa.com/NY">Ballot Access System</a></li>
        <li><a class="rava" href="<c:url value='/w/rava.htm?votingRegion.state=NY'/>">Voter Registration /<br/>Absentee Ballot Request</a></li>
        <li><a class="eod" href="<c:url value="/eod.htm"><c:param name="stateId" value="35"/></c:url>">Election Official Directory</a></li>
		<li><a class="svid" href="<c:url value="/svid.htm"><c:param name="stateId" value="35"/><c:param name="submission" value="true"/></c:url>">State Voter<br>Information Directory</a></li>
        <li><a class="fwab" href="<c:url value='/FwabStart.htm?votingRegion.state=NY'/>">Federal Write-in<br /><span>Absentee Ballot</span></a></li>
     </ul>
<div class="sidebar-bottom">
	<h3><a class="ceebox video-link" target="_blank" href="https://www.youtube.com/watch?v=m3nehKjBFQs">How to Request Your<br/>Absentee Ballot</a></h3>
	<a class="ceebox video-link" target="_blank" href="https://www.youtube.com/watch?v=m3nehKjBFQs"><img alt="Request your absentee ballot video" src="<c:url value='/img/sidebar/rava-absentee-voting-video.jpg'/>" /></a><br/>
	<a class="ceebox video-link" target="_blank" href="https://www.youtube.com/watch?v=m3nehKjBFQs">The A-Z of Overseas Voting</a>
</div>
       </div><!--/.well -->
       