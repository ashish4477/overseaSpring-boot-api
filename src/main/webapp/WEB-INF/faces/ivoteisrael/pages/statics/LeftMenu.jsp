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
     	<li><a class="rava" href="<c:url value='/w/rava.htm'><c:param name='vrState' value='FL'/><c:param name='vrName' value='Okaloosa County'/></c:url>">Voter Registration /<br/>Absentee Ballot Request</a></li>
     	<li><a class="fwab" href="<c:url value='/FwabStart.htm'><c:param name='vrState' value='FL'/><c:param name='vrName' value='Okaloosa County'/></c:url>">Federal Write-in <br/><span> Absentee Ballot</span></a></li>
     	<li><a class="vhd ceebox2" href="https://vhd.overseasvotefoundation.org/index.php?/okaloosa/Knowledgebase/List" rel="iframe width:800 height:450">Voter Help Desk</a></li>
     	<li><a class="eod" href="<c:url value="/eod.htm"><c:param name="submission" value="true"/><c:param name="stateId" value="11"/><c:param name="regionId" value="987"/></c:url>">Election Official Directory</a></li>
		<li><a class="svid" href="<c:url value="/svid.htm"><c:param name="submission" value="true"/><c:param name="stateId" value="11"/></c:url>">State Voter<br>Information Directory</a></li>
		<li><a class="candidate-finder" href="<c:url value='/CandidateFinder.htm'/>">Candidate Finder</a></li>
		<c:if test="${not empty user}">
			<c:set var="assignedFace" value="${user.getAssignedFace()}" />
			<c:if test="${not empty assignedFace and assignedFace.relativePrefix.contains('okaloosa')}">
				<c:set var="pendingVoterRegistrations" value="false" />
				<c:forEach items="${user.roles}" var="role">
					<c:if test="${role.roleName == 'pending_voter_registrations'}">
						<c:set var="pendingVoterRegistrations" value="true" />
					</c:if>
				</c:forEach>
				<c:if test="${pendingVoterRegistrations}">
					<li><a class="pending-voter-registrations"
						href="<c:url value='/onlinedatatransfer/OnlineDataTransfer.htm'><c:param name='vrState' value='FL'/><c:param name='vrName' value='Okaloosa County'/></c:url>"
					>Voter Data Download: Admin Only</a></li>
				</c:if>
			</c:if>
		</c:if>
     </ul>
<div class="sidebar-bottom">
	<img src="<c:url value='/img/sidebar/PaulLux.jpg'/>" alt="Supervisor Elections" /><br/>
	<a href="http://www.govote-okaloosa.com" target="_blank">Okaloosa County<br/>Supervisor of Elections<br/>
	<em class="person">Paul Lux</em></a>
</div>
       </div><!--/.well -->
       