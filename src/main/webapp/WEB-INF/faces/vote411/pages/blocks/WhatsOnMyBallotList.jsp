<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js" type="text/javascript"></script>
<script src='<c:url value="/js/bootstrap/bootstrap.min.js"/>' type="text/javascript"></script>

<div class="body-content wide-content column">
	<div class="page-form">
		<div class="hd">
			<div class="hd-inner">
				<h2 class="title">${title}</h2>
			</div>
		</div>

		<div class="bd">
			<div class="bd-inner">

				<div class="your-address" style="float: right; margin-top: 12px;">
					<h4>Your Voting Address</h4>
					<c:out value="${whatsOnMyBallot.address.street1}" />
					<br />
					<c:out value="${whatsOnMyBallot.address.city}, ${whatsOnMyBallot.address.state} ${whatsOnMyBallot.address.zip}" /><c:if test="${not empty address.address.zip4}">-<c:out value="${address.address.zip4}" />
					</c:if>
					<br /> <a class="change-address"
						href='<c:url value="WhatsOnMyBallot.htm"><c:param name="vState" value="${whatsOnMyBallot.votingState.abbr}"/></c:url>'
					>Change Address</a>
				</div>


				<div>
					<h2>Candidates on Your Ballot</h2>
					<p>
						Listed below are the candidates and questions which will appear on your ballot. They are listed in the order in which you will see them on your ballot.
					</p>

					<overseas:womb_candidate_contests candidateBios="${candidateBios}" inlineBios="false" contestOffice="PRESIDENT AND VICE-PRESIDENT" wombContests="${president}" partisanParty="${whatsOnMyBallot.partisanParty}" />
					<c:choose>
						<c:when test="${empty contests}">
							<overseas:womb_candidate_contests candidateBios="${candidateBios}" inlineBios="false" contestOffice="UNITED STATES SENATOR" wombContests="${senator}" reportNoContests="true" partisanParty="${whatsOnMyBallot.partisanParty}" />
							<overseas:womb_candidate_contests candidateBios="${candidateBios}" inlineBios="false" contestOffice="UNITED STATES REPRESENTATIVE" wombContests="${representative}" reportNoContests="true" partisanParty="${whatsOnMyBallot.partisanParty}" />
						</c:when>
						<c:otherwise>
							<overseas:womb_candidate_contests candidateBios="${candidateBios}" inlineBios="false" contestOffice="UNITED STATES SENATOR" wombContests="${senator}" partisanParty="${whatsOnMyBallot.partisanParty}" />
							<overseas:womb_candidate_contests candidateBios="${candidateBios}" inlineBios="false" contestOffice="UNITED STATES REPRESENTATIVE" wombContests="${representative}" partisanParty="${whatsOnMyBallot.partisanParty}" />
							<c:forEach items="${contests}" var="contest" varStatus="contestStatus">
								<c:choose>
									<c:when test="${not empty contest.ballot.candidates}">
										<overseas:womb_candidate_contest candidateBios="${candidateBios}" inlineBios="false"  contest="${contest}" partisanParty="${whatsOnMyBallot.partisanParty}" />
									</c:when>
									<c:when test="${not empty contest.ballot.referendum}">
										<overseas:womb_referendum_contest referendumDetails="${referendumDetails}" inlineDetails="false" contest="${contest}" />
									</c:when>
									<c:when test="${not empty contest.ballot.customBallot}">
										<overseas:womb_custom_ballot_contest contest="${contest}" />
									</c:when>
								</c:choose>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>