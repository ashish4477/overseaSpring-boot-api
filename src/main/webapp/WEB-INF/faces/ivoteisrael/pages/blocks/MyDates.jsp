<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div id="eod-form" class="row voter-account-page wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form">
	<div class="col-xs-12 col-sm-10 col-sm-offset-1">
		<h2 class="title">My Dates and Deadlines</h2>


		<c:if test="${not empty stateVoterInformation}">

			<h3>For U.S. Based Domestic Voters</h3>
			<div id="federaldeadlines" class="accordian rounded-5">
				<h5 class="ctrl">Upcoming Election Dates and Deadlines</h5>
				<div class="pnl">
					<c:forEach items="${localElections}" var="election" varStatus="inx">
						<table class="accordian-content rounded-5 election-deadlines" cellspacing="0">
							<c:if test="${empty level or election.sortOrder ne level}">
								<c:set var="level" value="${election.sortOrder}"/>
								<tr class="r0">
									<th colspan="3" class="election-level" id="${level.name}"><h4>${level.name} Elections</h4></th>
								</tr>
							</c:if>
							<tr class="r0">
								<td class="first-col" colspan="3"><c:out value='${election.title}' /> held on <c:out value='${election.heldOn}' /></td>
							</tr>
							<tr class="r1">
								<td class="first-col" colspan="2"></td>
								<td class="b-left"><strong>Deadlines</strong></td>
							</tr>
							<tr class="r2">
								<td class="first-col" colspan="2"><h4>New Voter Registration</h4></td>
								<td class="b-left"><c:out value='${election.domesticRegistration}' /></td>
							</tr>
							<tr class="r3">
								<td class="first-col" colspan="2"><h4>Absentee Ballot Request</h4></td>
								<td class="b-left"><c:out value='${election.domesticBallotRequest}' /></td>
							</tr>
							<tr class="r4">
								<td class="first-col" colspan="2"><h4>Absentee Ballot Return</h4></td>
								<td class="b-left"><c:out value='${election.domesticBallotReturn}' /></td>
							</tr>
							<tr class="r5">
								<td class="first-col" colspan="2"></td>
								<td class="b-left"><strong>Date Range</strong></td>
							</tr>
							<tr class="r6">
								<td class="first-col" colspan="2"><h4>Early Voting</h4></td>
								<td class="b-left"><c:choose>
									<c:when test="${not empty election.domesticEarlyVoting}">
										<c:out value='${election.domesticEarlyVoting}' />
									</c:when>
									<c:otherwise>none on record</c:otherwise>
								</c:choose></td>
							</tr>
							<tr class="r7">
								<td colspan="3" class="note">${fn:replace(fn:escapeXml(election.domesticNotes),newLineChar,"<br />")}</td>
							</tr>
						</table>
					</c:forEach>
				</div>
			</div>
			<div id="eligrequires" class="accordian rounded-5">
				<h5 class="ctrl">Eligibility Requirements</h5>
				<div class="pnl">
					<c:forEach items="${eligibilityRequirements.get(\"Domestic Voter\")}" var="requirementList">
						<h4 class="header">${requirementList.header}</h4>
						<ul>
							<c:forEach items="${requirementList.items}" var="requirementItem">
								<li> <c:out value='${requirementItem.name}'/></li>
							</c:forEach>
						</ul>
						<c:if test="${not empty requirementList.footer}">
							${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
						</c:if>
					</c:forEach>

				</div>
			</div>
			<div id="idrequires" class="accordian rounded-5">
				<h5 class="ctrl">Identification Requirements</h5>
				<div class="pnl">
					<c:forEach items="${identificationRequirements}" var="requirementLists">
						<%-- Domestic Only --%>
						<c:if test="${!(fn:contains(requirementLists.key, 'Military') || fn:contains(requirementLists.key, 'Overseas'))}">
							<h4 class="header">${requirementLists.key}</h4>
							<p>
							<c:forEach items="${requirementLists.value}" var="requirementList">
								<h4>${requirementList.header}</h4>
								<ul>
									<c:forEach items="${requirementList.items}" var="requirementItem">
										<li>${requirementItem.name}</li>
									</c:forEach>
								</ul>
								<c:if test="${not empty requirementList.footer}"> <p>
										${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
								</p>
								</c:if>
							</c:forEach>
							</p>
						</c:if>
					</c:forEach>

				</div>
			</div>

			<h3>For Overseas and Military Voters Only</h3>
			<div  id="electiondeadlines" class="accordian rounded-5">
				<h5 class="ctrl">Upcoming Election Dates and Deadlines</h5>
				<div class="pnl" style="display:none">
					<c:forEach items="${localElections}" var="election" varStatus="inx">
						<table class="accordian-content rounded-5 election-deadlines" cellspacing="0">
							<c:if test="${empty level or election.sortOrder ne level}">
								<c:set var="level" value="${election.sortOrder}"/>
								<tr class="r0">
									<th colspan="3" class="election-level" id="${level.name}"><h4>${level.name} Elections</h4></th>
								</tr>
							</c:if>
							<tr class="r0">
								<td class="first-col">${election.title}<br />held on ${election.heldOn}</td>
								<td class="b-left">Overseas Citizens</td>
								<td class="b-left">Uniformed Services</td>
							</tr>
							<tr class="r1">
								<td class="first-col">Absentee Voter Registration</td>
								<td  class="b-left">${election.citizenRegistration}</td>
								<td class="b-left">${election.militaryRegistration}</td>
							</tr>
							<tr class="r2">
								<td class="first-col">Ballot Request for Registered Voter</td>
								<td class="b-left">${election.citizenBallotRequest}</td>
								<td class="b-left">${election.militaryBallotRequest}</td>
							</tr>
							<tr class="r3">
								<td class="first-col">Ballot Return</td>
								<td class="b-left">${election.citizenBallotReturn}</td>
								<td class="b-left">${election.militaryBallotReturn}</td>
							</tr>
							<tr class="r4">
								<td colspan="3" class="note">${fn:replace(fn:escapeXml(election.notes),newLineChar,"<br />")}</td>
							</tr>
						</table>
					</c:forEach>
					<c:if test="${empty localElections}">
						<p class="accordian-content rounded-5 election-deadlines">There are no elections on record for this location. Election dates and
							deadlines will be posted when available. Questions or comments can be
							sent to <a href="mailto:info@overseasvotefoundation.org">info@overseasvotefoundation.org</a></p>
					</c:if>
				</div>
			</div>
			<div  id="transoptions" class="accordian rounded-5">
				<h5 class="ctrl">Voter Materials Transmission Options</h5>
				<div class="pnl" style="display:none">
					<c:forEach items="${stateVoterInformation.transmissionMethods}" var="voterTypeTransmissionMethods">
						<%-- Uocava Only --%>
						<c:if test="${(fn:contains(voterTypeTransmissionMethods.voterType.name, 'Military') || fn:contains(voterTypeTransmissionMethods.voterType.name, 'Overseas'))}">
							<div class="table-responsive">
								<table class="accordian-content rounded-5 trans-options" cellspacing="0">
									<tr class="r0">
										<td class="first-col"><h4 class="header">${voterTypeTransmissionMethods.voterType.name}</h4></td>
										<td><strong>In-Person</strong></td>
										<td><strong>Mail</strong></td>
										<td><strong>Fax</strong></td>
										<td><strong>Email</strong></td>
										<td><strong>Online</strong></td>
									</tr>
									<c:forEach items="${voterTypeTransmissionMethods.groupedItems}" var="items">
										<tr class="r1">
											<td class="first-col"><h4>${items.key}</h4></td>
											<td><c:choose>
												<c:when test="${items.value.get(\"In-Person\")}">
													<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
													<span class="glyphicon-ie" aria-hidden="true"></span>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose></td>
											<td class="odd"><c:choose>
												<c:when test="${items.value.get(\"Mail\")}">
													<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
													<span class="glyphicon-ie" aria-hidden="true"></span>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose></td>
											<td><c:choose>
												<c:when test="${items.value.get(\"Fax\")}">
													<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
													<span class="glyphicon-ie" aria-hidden="true"></span>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose></td>
											<td class="odd"><c:choose>
												<c:when test="${items.value.get(\"Email\")}">
													<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
													<span class="glyphicon-ie" aria-hidden="true"></span>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose></td>
											<td><c:choose>
												<c:when test="${items.value.get(\"Online\")}">
													<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
													<span class="glyphicon-ie" aria-hidden="true"></span>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose></td>
										</tr>
									</c:forEach>
									<c:if test="${not empty voterTypeTransmissionMethods.additionalInfo}">
										<tr>
											<td colspan="5" class="note">
													${voterTypeTransmissionMethods.additionalInfo}
											</td>
										</tr>
									</c:if>
								</table>
							</div>
						</c:if>
					</c:forEach>

				</div>
			</div>
		</c:if>
		<p>&nbsp;</p>
	</div>

</div>