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
	<div class="page-form fwab-start" id="fwab-start-box">
		<div class="hd">
			<div class="hd-inner">
				<h2 class="title">${title}</h2>
			</div>
		</div>

		<div class="bd">
			<div class="bd-inner">
				<div class="container" style="width: 625px;">
					<div class="row">
						<div class="span5">
							<p></p>
							<h4>Referendum Description</h4>
							<p></p>
							<p></p>
							<h4>
								<c:out value="${referendumDetail.referendum.title}" />
								<c:if test="${not empty referendumDetail.referendum.subTitle}">
									<br />
									<c:out value="${referendumDetail.referendum.subTitle}" />
								</c:if>
							</h4>
							<p></p>

							<p>
								<c:if test="${not empty referendumDetail.referendum.brief}">
									<b>Brief:</b>
									<c:out value="${referendumDetail.referendum.brief}" />
									<br />
								</c:if>
								<b>Text:</b> <em><c:out value="${referendumDetail.referendum.text}" /></em><br />
							</p>
							<p>

								<c:if test="${not empty referendumDetail.proStatement && not empty referendumDetail.conStatement }">
									<h4>Statements</h4>
									<b>Pro:</b>
									<c:out value="${referendumDetail.proStatement}" />
									<br />
									<b>Con:</b>
									<c:out value="${referendumDetail.conStatement}" />
									<br />
								</c:if>
							</p>
							<c:if test=""></c:if>
							<p></p>
							<h4>Additional Information</h4>

							<c:if test="${not empty referendumDetail.passageThreshold}">
								<b>Threshold for Passage:</b>
								<c:out value="${referendumDetail.passageThreshold}" />
								<br />
							</c:if>
							<c:if test="${not empty referendumDetail.effectOfAbstain}">
								<b>Effect of Abstaining:</b>
								<c:out value="${referendumDetail.effectOfAbstain}" />
								<br />
							</c:if>
							<p></p>
						</div>
						<div class="span3">
							<div class="your-address">
								<h4>Your Voting Address</h4>
								<c:out value="${whatsOnMyBallot.address.street1}" />
								<br />
								<c:out value="${whatsOnMyBallot.address.city}, ${whatsOnMyBallot.address.state} ${whatsOnMyBallot.address.zip}" /><c:if test="${not empty address.address.zip4}">-<c:out value="${address.address.zip4}" />
								</c:if>
							</div>
							<br /> <a class="change-address"
								href='<c:url value="WhatsOnMyBallot.htm"><c:param name="vrState" value="${whatsOnMyBallot.votingState.abbr}"/></c:url>'
							>Change Address</a>
						</div>

					</div>
				</div>

				<div class="back-button pull-right">
					<a href='<c:url value="/WhatsOnMyBallotList.htm"/>'><img src='<c:url value="/img/buttons/back-button.gif"/>' alt="Back" /></a>
				</div>
				<div class="break"></div>
				<p style="font-size: 80%; font-style: italic; text-align: center; margin-top: 20px;">Data Source:
					${referendumDetail.referendum.source.name}</p>
			</div>
		</div>
		<div class="ft">
			<div class="ft-inner"></div>
		</div>
	</div>
</div>