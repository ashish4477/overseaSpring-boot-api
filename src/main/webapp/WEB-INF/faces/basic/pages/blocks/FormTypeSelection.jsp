<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<div class="body-content">
	<div class="column ${styleClass} wide-content page-form rava-intro">
		<div class="hd">
			<div class="hd-inner">
				<h1 class="title">${pageTitle}</h1>
			</div>
		</div>
		
		<div class="bd">
			<div class="bd-inner">
				<h2>What Type of Voting Form Would You Like to Prepare?</h2>
				<form class="output-type" action="<c:url value="/voter-registration-absentee-voting.htm"/>" method="post" id="voterRegistrationAbsenteeBallotForm">
				    <c:choose>
						<c:when test="${not empty param.cStateId}">
							<input type="hidden" name="stateId" id="stateId" value="${param.cStateId}" />
							<input type="hidden" name="vrState" id="votingRegionState" value="${param.cStateAbbr}" />
						</c:when>
						<c:when test="${not empty param.vrState}">
							<input type="hidden" name="vrState" id="votingRegionState" value="${param.vrState}" />
							<input type="hidden" name="vrName" id="votingRegionName" value="${param.vrName}" />
						</c:when>
				    </c:choose>
					<fieldset>
						<div class="domestic form-type">
							<h2>U.S. Based Voters</h2>
							<label><input type="radio" name="formType"
								value="domestic_registration" required /> Voter Registration</label> <br />
							<label><input type="radio" name="formType"
								value="domestic_absentee" required /> Absentee Ballot Request</label>
						</div>
						<div class="overseas form-type">
							<h2>Overseas Citizen Voters</h2>
							<label><input type="radio" name="formType" value="rava"
								required /> Voter Registration & <br />Absentee Ballot Request</label>
						</div>
						<div class="military form-type">
							<h2>Military Voters</h2>
							<label><input type="radio" name="formType" value="rava"
								required /> Voter Registration & <br />Absentee Ballot Request</label>
						</div>
						<section>
							<footer>
								<strong>Overseas Citizens & Military Voters:</strong><br />
								<p>
									If you have already requested an absentee ballot but have not
									yet received it, you can still vote by using the back-up
									<a href="/vote/FwabStart.htm">Federal Write-In Absentee Ballot (FWAB).</a>
								</p>
							</footer>
						</section>
					</fieldset>
					<input type="image" src="<c:url value="/img/buttons/continue-button.gif" />" name="submit" class="submit-button" />
				</form>
			</div>
			<div class="break"></div>
			<br />
			<br /> <strong>Please Note:</strong> This website will not store your
			social security number, birth date or driver's license number.
		</div>
	</div>
	<div class="ft">
		<div class="ft-inner"></div>
	</div>
</div>
