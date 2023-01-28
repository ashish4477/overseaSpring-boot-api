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
				<h4>Candidate Biography</h4>
				<div class="container" style="width: 625px;">
					<div class="row">

						<div class="span5">
							<p>
								<c:out value="${candidateBio.candidate.name}" />
								<c:if test="${not empty candidateBio.candidate.party}">
									<br />
									<c:out value="(${candidateBio.candidate.party})" />
								</c:if>
							</p>

							<h4>Contact Information</h4>
							<p>
								<c:choose>
									<c:when test="${candidateBio.contactInformationEmpty}">
										There is currently no contact information available for this candidate.
									</c:when>
									<c:otherwise>
										<c:if test="${!candidateBio.candidateUrlEmpty}">
											<b>Candidate URL:</b>
											<a class="womb" target="_blank" href='<c:url value="${candidateBio.candidateUrl}"/>'> <c:out value="${candidateBio.candidateUrl}" />
											</a>
											<br />
										</c:if>
										<c:if test="${!candidateBio.photoUrlEmpty}">
											<b>Photo URL:</b>
											<a class="womb" target="_blank" href='<c:url value="${candidateBio.photoUrl}"/>'> <c:out value="${candidateBio.photoUrl}" />
											</a>
											<br />
										</c:if>
										<c:if test="${!candidateBio.filedMailingAddressEmpty}">
											<br />
											<b>Filed Mailing Address:</b>
											<br />
											${candidateBio.filedMailingAddress.multilineAddress}
											<br />
										</c:if>
										<c:if test="${!candidateBio.emailEmpty}">
											<b>Email:</b>
											<c:out value="${candidateBio.email}" />
											<br />
										</c:if>
										<c:if test="${!candidateBio.phoneEmpty}">
											<b>Phone:</b>
											<c:out value="${candidateBio.phone}" />
											<br />
										</c:if>
									</c:otherwise>
								</c:choose>
							</p>

							<h4>Background Information</h4>
							<c:choose>
								<c:when test="${candidateBio.biographyEmpty}">
									There is currently no background information available for this candidate.
								</c:when>
								<c:otherwise>
									<p>
										<b>Biography:</b> <br />
										<c:out value="${candidateBio.biography}" />
									</p>
								</c:otherwise>
							</c:choose>
						</div>

						<div class="span3">
							<div class="your-address">
								<h4>Your Voting Address</h4>
								<c:out value="${whatsOnMyBallot.address.street1}" />
								<p>
									<c:out value="${whatsOnMyBallot.address.city}, ${whatsOnMyBallot.address.state} ${whatsOnMyBallot.address.zip}" /><c:if test="${not empty address.address.zip4}">-<c:out value="${address.address.zip4}" />
									</c:if>
								</p>
							</div>
							<a class="change-address"
								href='<c:url value="WhatsOnMyBallot.htm"><c:param name="vrState" value="${whatsOnMyBallot.votingState.abbr}"/></c:url>'
							>Change Address</a>
						</div>
					</div>
				</div>

				<div class="back-button pull-right">
					<a href="<c:url value='/WhatsOnMyBallotList.htm'/>"><img src="<c:url value='/img/buttons/back-button.gif'/>" alt="Back" /></a>
				</div>
				<div class="break"></div>
				<p style="font-size: 80%; font-style: italic; text-align: center; margin-top: 20px;">Data Source:
					${candidateBio.candidate.source.name}</p>
			</div>


			<div class="ft">
				<div class="ft-inner"></div>
			</div>
		</div>
	</div>
</div>