<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Jul 17, 2007
	Time: 9:53:04 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	pageContext.setAttribute("newLineChar", "\n");
%>
<script type="text/javascript" language="JavaScript">
	//<!--
	YAHOO.ovf.sendSnapshotUrl = "<c:url value="/ajax/EmailToOfficer.htm"/>";
	//-->
</script>

<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/admin.css"/>" />
<div id="eod-corrections" class="column-form">

	<div class="hd">
		<h2>${svid.state.name} State Voting Requirements &amp; Information</h2>
    <div class="last-updated">
    <strong>Last Updated:</strong> <fmt:formatDate value="${svid.updated}" pattern="MM/dd/yyyy" />
    <br/>
    <select onChange="window.location.href=this.value">
      <option selected>Edit Election</option>
      <c:forEach items="${svid.elections}" var="election" varStatus="indx">
        <option value="<c:url value="/admin/ElectionEdit.htm"><c:param name="electionId" value="${election.id}"/></c:url>"><c:out value="${election.title}" /></option>
      </c:forEach>
      <option value="<c:url value="/admin/ElectionEdit.htm"><c:param name="stateId" value="${svid.state.id}"/></c:url>"><c:out value="${election.title}" />Create a New Election</option>
    </select>
  </div>
	</div>

	<div class="bd">
  <c:url value='/admin/SvidEdit.htm' var="actionUrl"/>
		<form:form action="${actionUrl}" commandName="svid" name="eodForm" method="post" id="eodForm">
			<div class="sect svid-website first">
				<div class="corrected">
					<h4>State Website</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.website}">
								<c:out value='${svid.website}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">URL</span> <spring:bind path="svid.website">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
			<div class="sect overseas-services">
				<div class="corrected">
					<h4>Overseas Voter Services</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.overseasVoterServicesSite}">
								<c:out value='${svid.overseasVoterServicesSite}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">URL</span> <spring:bind path="svid.overseasVoterServicesSite">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
			</div>
			<div class="sect military-services">
				<div class="corrected">
					<h4>Military Voter Services</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.militaryVoterServicesSite}">
								<c:out value='${svid.militaryVoterServicesSite}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">URL</span> <spring:bind path="svid.militaryVoterServicesSite">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
			</div>
			<div class="sect registration-finder">
				<div class="corrected">
					<h4>Registration Finder</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.registrationFinderSite}">
								<c:out value='${svid.registrationFinderSite}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">URL</span> <spring:bind path="svid.registrationFinderSite">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
			</div>
			<div class="sect online-registration-site">
				<div class="corrected">
					<h4>Online Voter Registration Site</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.onlineVoterRegistrationSite}">
								<c:out value='${svid.onlineVoterRegistrationSite}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">URL</span> <spring:bind path="svid.onlineVoterRegistrationSite">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
			</div>
			<div class="sect ballot-tracking-website">
				<div class="corrected">
					<h4>Ballot Tracking Website</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.ballotTrackingSite}">
								<c:out value='${svid.ballotTrackingSite}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">URL</span> <spring:bind path="svid.ballotTrackingSite">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
			</div>
			<div class="sect early-voting-information">
				<div class="corrected">
					<h4>Early Voting Information</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.earlyVotingInformationSite}">
								<c:out value='${svid.earlyVotingInformationSite}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">URL</span> <spring:bind path="svid.earlyVotingInformationSite">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
			</div>
			<div class="sect mailing-address">
				<div class="corrected">
					<h4>Mailing Address</h4>
					<div class="ctrl">
						<p>
							<c:out value='${svid.mailing.addressTo}' />
							<br />
							<c:out value='${svid.mailing.street1}' />
							<br />
							<c:out value='${svid.mailing.street2}' />
							<br />
							<c:out value='${svid.mailing.city}, ${svid.mailing.state} ${svid.mailing.zip}' />
							<c:if test="${not empty svid.mailing.zip4}">-<c:out value='${svid.mailing.zip4}' />
							</c:if>
						</p>
					</div>
					<fieldset class="pnl">
						<label> <span class="field-name">Addressee</span> <spring:bind path="svid.mailing.addressTo">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label> <label> <span class="field-name">Street Line 1</span> <spring:bind path="svid.mailing.street1">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label> <label> <span class="field-name">Street Line 2</span> <spring:bind path="svid.mailing.street2">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label> <label> <span class="field-name">City, State, Zip</span> <spring:bind path="svid.mailing.city">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" class="city" />
							</spring:bind> <spring:bind path="svid.mailing.state">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" class="state" maxlength="2" />
							</spring:bind>, <spring:bind path="svid.mailing.zip">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" class="zip" maxlength="5" />
							</spring:bind> - <spring:bind path="svid.mailing.zip4">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" class="zip4" maxlength="4" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
			<div class="sect physical-address">
				<div class="corrected">
					<h4>Physical Address</h4>
					<div class="ctrl">
						<c:out value='${svid.physical.addressTo}' />
						<br />
						<c:out value='${svid.physical.street1}' />
						<br />
						<c:out value='${svid.physical.street2}' />
						<br />
						<c:out value='${svid.physical.city}, ${svid.physical.state} ${svid.physical.zip}' />
						<c:if test="${not empty svid.mailing.zip4}">-<c:out value='${svid.physical.zip4}' />
						</c:if>
					</div>
					<fieldset class="pnl">
						<label> <span class="field-name">Addressee</span> <spring:bind path="svid.physical.addressTo">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label> <label> <span class="field-name">Street Line 1</span> <spring:bind path="svid.physical.street1">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label> <label> <span class="field-name">Street Line 2</span> <spring:bind path="svid.physical.street2">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label> <label> <span class="field-name">City, State, Zip</span> <spring:bind path="svid.physical.city">
								<input type="text" name="${status.expression}" class="city" value="<c:out value='${status.value}'/>" />
							</spring:bind> <spring:bind path="svid.physical.state">
								<input type="text" name="${status.expression}" class="state" value="<c:out value='${status.value}'/>" maxlength="2" />
							</spring:bind>, <spring:bind path="svid.physical.zip">
								<input type="text" name="${status.expression}" class="zip" value="<c:out value='${status.value}'/>" maxlength="5" />
							</spring:bind> - <spring:bind path="svid.physical.zip4">
								<input type="text" name="${status.expression}" class="zip4" value="<c:out value='${status.value}'/>" maxlength="4" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
			<div class="sect state-contacts">
				<div class="corrected">
					<h4>Telephone</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.statePhone}">
								<c:out value='${svid.statePhone}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Phone Number</span> <spring:bind path="svid.statePhone">
								<input type="phone" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Fax</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.stateFax}">
								<c:out value='${svid.stateFax}' />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Fax Number</span> <spring:bind path="svid.stateFax">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Admin Email Address (not public)</h4>
					<p class="ctrl">
						<c:choose><c:when test="${not empty svid.stateEmail}">${svid.stateEmail}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
					</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Admin Email (not public)</span>
							<spring:bind path="svid.stateEmail">
								<input type="text" name="${status.expression}" value="${status.value}" />
							</spring:bind>
						</label>
					</fieldset>
                    <c:if test="${not empty svid.stateEmail}"><br/><button class="snapshot" onclick="YAHOO.ovf.sendSnapshot('svidId=${svid.state.id}','leoEmailFeedback'); return false;">Send Snapshot to State Contact</button>
                    </c:if>
				</div>
                <span class="success" id="leoEmailFeedback"></span>
				<div class="break"></div>
                <div class="corrected">
            					<h4>Admin Contact Title (not public)</h4>
            					<p class="ctrl">
            						<c:choose><c:when test="${not empty svid.stateContact.title}">${svid.stateContact.title}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
            					</p>
            					<fieldset class="pnl">
            						<label>
            							<span class="field-name">Contact person title (not public)</span>
            							<spring:bind path="svid.stateContact.title">
            								<input type="text" name="${status.expression}" value="${status.value}" />
            							</spring:bind>
            						</label>
            					</fieldset>
            				</div>
                <div class="corrected">
            					<h4>Admin Contact First Name (not public)</h4>
            					<p class="ctrl">
            						<c:choose><c:when test="${not empty svid.stateContact.firstName}">${svid.stateContact.firstName}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
            					</p>
            					<fieldset class="pnl">
            						<label>
            							<span class="field-name">Contact person first name (not public)</span>
            							<spring:bind path="svid.stateContact.firstName">
            								<input type="text" name="${status.expression}" value="${status.value}" />
            							</spring:bind>
            						</label>
            					</fieldset>
            				</div>
                <div class="corrected">
            					<h4>Admin Contact Last Name (not public)</h4>
            					<p class="ctrl">
            						<c:choose><c:when test="${not empty svid.stateContact.lastName}">${svid.stateContact.lastName}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
            					</p>
            					<fieldset class="pnl">
            						<label>
            							<span class="field-name">Contact person last name (not public)</span>
            							<spring:bind path="svid.stateContact.lastName">
            								<input type="text" name="${status.expression}" value="${status.value}" />
            							</spring:bind>
            						</label>
            					</fieldset>
            				</div>
			</div>
			<div class="sect contact-notes">
				<div class="corrected">
					<h4>Contact Notes</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.contactNotes}">${fn:replace(fn:escapeXml(svid.contactNotes),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span>
						<spring:bind path="svid.contactNotes">
							<textarea name="${status.expression}"><c:out value='${svid.contactNotes}' /></textarea>
						</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
			<div class="sect registration-status">
				<div class="corrected">
					<h4>Voter Registration Status Inquiry</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.confirmationOrStatus}">
								<c:out value="${svid.confirmationOrStatus}" />
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <spring:bind path="svid.confirmationOrStatus">
								<input type="text" name="${status.expression}" value="<c:out value="${status.value}" />" />
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>

			<div class="sect domestic-voter-options ">
				<div class="corrected">
					<h4>US Voter Registration Requirements</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.voterRegistrationRequirements}">${fn:replace(fn:escapeXml(svid.voterRegistrationRequirements),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span> <spring:bind path="svid.voterRegistrationRequirements">
						<textarea name="${status.expression}"><c:out value='${svid.voterRegistrationRequirements}' /></textarea>
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>US Early Voting Requirements</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.earlyVotingRequirements}">${fn:replace(fn:escapeXml(svid.earlyVotingRequirements),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span> <spring:bind path="svid.earlyVotingRequirements">
						<textarea name="${status.expression}"><c:out value='${svid.earlyVotingRequirements}' /></textarea>
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>US Absentee Ballot Request Requirements</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.absenteeBallotRequestRequirements}">${fn:replace(fn:escapeXml(svid.absenteeBallotRequestRequirements),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span> <spring:bind path="svid.absenteeBallotRequestRequirements">
						<textarea name="${status.expression}"><c:out value='${svid.absenteeBallotRequestRequirements}' /></textarea>
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>US Voter Registration - Identification Requirements</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.voterRegistrationIdentificationRequirements}">${fn:replace(fn:escapeXml(svid.voterRegistrationIdentificationRequirements),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span> <spring:bind path="svid.voterRegistrationIdentificationRequirements">
						<textarea name="${status.expression}"><c:out value='${svid.voterRegistrationIdentificationRequirements}' /></textarea>
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>US Voting In-Person - Identification Requirements</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.votingInPersonIdentificationRequirements}">${fn:replace(fn:escapeXml(svid.votingInPersonIdentificationRequirements),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span> <spring:bind path="svid.votingInPersonIdentificationRequirements">
						<textarea name="${status.expression}"><c:out value='${svid.votingInPersonIdentificationRequirements}' /></textarea>
							</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>US Early Voting - Identification Requirements</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.earlyVotingIdentificationRequirements}">${fn:replace(fn:escapeXml(svid.earlyVotingIdentificationRequirements),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span>
						<spring:bind path="svid.earlyVotingIdentificationRequirements">
							<textarea name="${status.expression}"><c:out value='${svid.earlyVotingIdentificationRequirements}' /></textarea>
						</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>US Absentee Ballot Request - Identification Requirements</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.absenteeBallotRequestIdentificationRequirements}">${fn:replace(fn:escapeXml(svid.absenteeBallotRequestIdentificationRequirements),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label><span class="field-name">Note</span>
						<spring:bind path="svid.absenteeBallotRequestIdentificationRequirements">
							<textarea name="${status.expression}"><c:out value='${svid.absenteeBallotRequestIdentificationRequirements}' /></textarea>
						</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>US VMTO New Voter Registration</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticRegistration.inPerson}">checked="checked" </c:if>>
						In-Person
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticRegistration.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticRegistration.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticRegistration.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticRegistration.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="domesticRegistration.inPerson" cssClass="check"/><form:label path="domesticRegistration.inPerson"> In-Person</form:label>
          <form:checkbox path="domesticRegistration.post" cssClass="check"/><form:label path="domesticRegistration.post"> Post</form:label>
          <form:checkbox path="domesticRegistration.fax" cssClass="check"/><form:label path="domesticRegistration.fax"> Fax</form:label>
          <form:checkbox path="domesticRegistration.email" cssClass="check"/><form:label path="domesticRegistration.email"> Email</form:label>
          <form:checkbox path="domesticRegistration.tel" cssClass="check"/><form:label path="domesticRegistration.tel"> Online</form:label>

					</fieldset>
				</div>
				<div class="corrected">
					<h4>US VMTO Ballot Request</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotRequest.inPerson}">checked="checked" </c:if>>
						In-Person
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotRequest.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotRequest.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotRequest.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotRequest.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="domesticBallotRequest.inPerson" cssClass="check"/><form:label path="domesticBallotRequest.inPerson"> In-Person</form:label>
          <form:checkbox path="domesticBallotRequest.post" cssClass="check"/><form:label path="domesticBallotRequest.post"> Post</form:label>
          <form:checkbox path="domesticBallotRequest.fax" cssClass="check"/><form:label path="domesticBallotRequest.fax"> Fax</form:label>
          <form:checkbox path="domesticBallotRequest.email" cssClass="check"/><form:label path="domesticBallotRequest.email"> Email</form:label>
          <form:checkbox path="domesticBallotRequest.tel" cssClass="check"/><form:label path="domesticBallotRequest.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>US VMTO Ballot Return</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotReturn.inPerson}">checked="checked" </c:if>>
						In-Person
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotReturn.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotReturn.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotReturn.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBallotReturn.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="domesticBallotReturn.inPerson" cssClass="check"/><form:label path="domesticBallotReturn.inPerson"> In-Person</form:label>
          <form:checkbox path="domesticBallotReturn.post" cssClass="check"/><form:label path="domesticBallotReturn.post"> Post</form:label>
          <form:checkbox path="domesticBallotReturn.fax" cssClass="check"/><form:label path="domesticBallotReturn.fax"> Fax</form:label>
          <form:checkbox path="domesticBallotReturn.email" cssClass="check"/><form:label path="domesticBallotReturn.email"> Email</form:label>
          <form:checkbox path="domesticBallotReturn.tel" cssClass="check"/><form:label path="domesticBallotReturn.tel"> Online</form:label>
					</fieldset>
				</div>
				<%--
				<div class="corrected">
					<h4>US VMTO Early Voting</h4>
					<p class="ctrl">
						In-Person
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticEarlyVoter.inPerson}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticEarlyVoter.post}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticEarlyVoter.fax}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticEarlyVoter.email}">checked="checked" </c:if>>
						Online
						<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticEarlyVoter.tel}">checked="checked" </c:if>>
					</p>
					<fieldset class="pnl">
                        <span class="field-name">&nbsp;</span>
                        <form:checkbox path="domesticEarlyVoter.inPerson" cssClass="check"/><form:label path="domesticEarlyVoter.inPerson">In-Person</form:label>
                        <form:checkbox path="domesticEarlyVoter.post" cssClass="check"/><form:label path="domesticEarlyVoter.post">Post</form:label>
                        <form:checkbox path="domesticEarlyVoter.fax" cssClass="check"/><form:label path="domesticEarlyVoter.fax">Fax</form:label>
                        <form:checkbox path="domesticEarlyVoter.email" cssClass="check"/><form:label path="domesticEarlyVoter.email">Email</form:label>
                        <form:checkbox path="domesticEarlyVoter.tel" cssClass="check"/><form:label path="domesticEarlyVoter.tel">Online</form:label>
					</fieldset>
				</div>
				--%>
                    <div class="corrected">
                        <h4>US VMTO Blank Ballot to Voter</h4>
                        <p class="ctrl">
                            <input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBlankBallot.inPerson}">checked="checked" </c:if>>
                            In-Person
                             <input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBlankBallot.post}">checked="checked" </c:if>>
                            Post
                             <input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBlankBallot.fax}">checked="checked" </c:if>>
                            Fax
                           	<input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBlankBallot.email}">checked="checked" </c:if>>
                            Email
                           <input type="checkbox" disabled="disabled" <c:if test="${svid.domesticBlankBallot.tel}">checked="checked" </c:if>>
                            Online
                        </p>
                        <fieldset class="pnl">
                            <span class="field-name">&nbsp;</span>
                            <form:checkbox path="domesticBlankBallot.inPerson" cssClass="check"/>
                            <form:label path="domesticBlankBallot.inPerson">In-Person</form:label>

                            <form:checkbox path="domesticBlankBallot.post" cssClass="check"/>
                            <form:label path="domesticBlankBallot.post">Post</form:label>
                           
                           	<form:checkbox path="domesticBlankBallot.fax" cssClass="check"/>
                            <form:label path="domesticBlankBallot.fax">Fax</form:label>
                          	
                          	<form:checkbox path="domesticBlankBallot.email" cssClass="check"/>
                            <form:label path="domesticBlankBallot.email">Email</form:label>
                            
                            <form:checkbox path="domesticBlankBallot.tel" cssClass="check"/>
                            <form:label path="domesticBlankBallot.tel">Online</form:label>
                        </fieldset>
                    </div>
				<div class="corrected">
					<h4>US VMTO Notes</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.domesticNotes}">${fn:replace(fn:escapeXml(svid.domesticNotes),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span>
						<spring:bind path="svid.domesticNotes">
							<textarea name="${status.expression}"><c:out value='${svid.domesticNotes}' /></textarea>
						</spring:bind>
						</label>
					</fieldset>
				</div>
				<div>
					<h4>US New Voter Registration Witness and Notarization Requirements</h4>
					<div class="corrected">
						<h4>Notes</h4>
						<p class="ctrl">
							<c:choose>
								<c:when test="${not empty svid.newVoterRegistrationWitnessesOrNotarization.notarizationWitnessRequirements}">${fn:replace(fn:escapeXml(svid.newVoterRegistrationWitnessesOrNotarization.notarizationWitnessRequirements),newLineChar,"<br />")}</c:when>
								<c:otherwise>none on record</c:otherwise>
							</c:choose>
						</p>
						<fieldset class="pnl">
							<label> <span class="field-name">Note</span>
							<spring:bind path="svid.newVoterRegistrationWitnessesOrNotarization.notarizationWitnessRequirements">
								<textarea name="${status.expression}"><c:out value='${status.value}' /></textarea>
							</spring:bind>
							</label>
						</fieldset>
					</div>
				</div>
				<div>
					<h4>US Absentee Ballot Affidavit Witness and Notarization Requirements</h4>
					<div class="corrected">
						<h4>Notes</h4>
						<p class="ctrl">
							<c:choose>
								<c:when test="${not empty svid.absenteeBallotAffidavitWitnessesOrNotarization.notarizationWitnessRequirements}">${fn:replace(fn:escapeXml(svid.absenteeBallotAffidavitWitnessesOrNotarization.notarizationWitnessRequirements),newLineChar,"<br />")}</c:when>
								<c:otherwise>none on record</c:otherwise>
							</c:choose>
						</p>
						<fieldset class="pnl">
							<label> <span class="field-name">Note</span>
							<spring:bind path="svid.absenteeBallotAffidavitWitnessesOrNotarization.notarizationWitnessRequirements">
								<textarea name="${status.expression}"><c:out value='${status.value}' /></textarea>
							</spring:bind>
							</label>
						</fieldset>
					</div>
				</div>
				<div>
					<h4>US Early Voting Witness and Notarization Requirements</h4>
					<div class="corrected">
						<h4>Notes</h4>
						<p class="ctrl">
							<c:choose>
								<c:when test="${not empty svid.earlyVotingWitnessesOrNotarization.notarizationWitnessRequirements}">${fn:replace(fn:escapeXml(svid.earlyVotingWitnessesOrNotarization.notarizationWitnessRequirements),newLineChar,"<br />")}</c:when>
								<c:otherwise>none on record</c:otherwise>
							</c:choose>
						</p>
						<fieldset class="pnl">
							<label> <span class="field-name">Note</span>
							<spring:bind path="svid.earlyVotingWitnessesOrNotarization.notarizationWitnessRequirements">
								<textarea name="${status.expression}"><c:out value='${status.value}' /></textarea>
							</spring:bind>
							</label>
						</fieldset>
					</div>
				</div>
				<div class="break"></div>
			</div>

			<div class="sect citizen-voter-options ">
				<div class="corrected">
					<h4>Citizen Registration</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenRegistration.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenRegistration.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenRegistration.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenRegistration.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="citizenRegistration.post" cssClass="check"/><form:label path="citizenRegistration.post"> Post</form:label>
          <form:checkbox path="citizenRegistration.fax" cssClass="check"/><form:label path="citizenRegistration.fax"> Fax</form:label>
          <form:checkbox path="citizenRegistration.email" cssClass="check"/><form:label path="citizenRegistration.email"> Email</form:label>
          <form:checkbox path="citizenRegistration.tel" cssClass="check"/><form:label path="citizenRegistration.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Citizen Ballot request</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBallotRequest.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBallotRequest.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBallotRequest.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBallotRequest.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="citizenBallotRequest.post" cssClass="check"/><form:label path="citizenBallotRequest.post"> Post</form:label>
          <form:checkbox path="citizenBallotRequest.fax" cssClass="check"/><form:label path="citizenBallotRequest.fax"> Fax</form:label>
          <form:checkbox path="citizenBallotRequest.email" cssClass="check"/><form:label path="citizenBallotRequest.email"> Email</form:label>
          <form:checkbox path="citizenBallotRequest.tel" cssClass="check"/><form:label path="citizenBallotRequest.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Citizen Blank Ballot receipt</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBlankBallot.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBlankBallot.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBlankBallot.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBlankBallot.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="citizenBlankBallot.post" cssClass="check"/><form:label path="citizenBlankBallot.post"> Post</form:label>
          <form:checkbox path="citizenBlankBallot.fax" cssClass="check"/><form:label path="citizenBlankBallot.fax"> Fax</form:label>
          <form:checkbox path="citizenBlankBallot.email" cssClass="check"/><form:label path="citizenBlankBallot.email"> Email</form:label>
          <form:checkbox path="citizenBlankBallot.tel" cssClass="check"/><form:label path="citizenBlankBallot.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Citizen Absentee Ballot Return</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBallotReturn.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBallotReturn.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBallotReturn.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.citizenBallotReturn.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="citizenBallotReturn.post" cssClass="check"/><form:label path="citizenBallotReturn.post"> Post</form:label>
          <form:checkbox path="citizenBallotReturn.fax" cssClass="check"/><form:label path="citizenBallotReturn.fax"> Fax</form:label>
          <form:checkbox path="citizenBallotReturn.email" cssClass="check"/><form:label path="citizenBallotReturn.email"> Email</form:label>
          <form:checkbox path="citizenBallotReturn.tel" cssClass="check"/><form:label path="citizenBallotReturn.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Citizens Notes</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.citizenNotes}">${fn:replace(fn:escapeXml(svid.citizenNotes),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span>
						<spring:bind path="svid.citizenNotes">
							<textarea name="${status.expression}"><c:out value='${svid.citizenNotes}' /></textarea>
						</spring:bind>
						</label>
					</fieldset>
				</div>

				<div class="break"></div>
			</div>
			<div class="sect military-voter-options ">
				<div class="corrected">
					<h4>Military Registration</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryRegistration.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryRegistration.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryRegistration.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryRegistration.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="militaryRegistration.post" cssClass="check"/><form:label path="militaryRegistration.post"> Post</form:label>
          <form:checkbox path="militaryRegistration.fax" cssClass="check"/><form:label path="militaryRegistration.fax"> Fax</form:label>
          <form:checkbox path="militaryRegistration.email" cssClass="check"/><form:label path="militaryRegistration.email"> Email</form:label>
          <form:checkbox path="militaryRegistration.tel" cssClass="check"/><form:label path="militaryRegistration.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Military Ballot request</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBallotRequest.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBallotRequest.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBallotRequest.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBallotRequest.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="militaryBallotRequest.post" cssClass="check"/><form:label path="militaryBallotRequest.post"> Post</form:label>
          <form:checkbox path="militaryBallotRequest.fax" cssClass="check"/><form:label path="militaryBallotRequest.fax"> Fax</form:label>
          <form:checkbox path="militaryBallotRequest.email" cssClass="check"/><form:label path="militaryBallotRequest.email"> Email</form:label>
          <form:checkbox path="militaryBallotRequest.tel" cssClass="check"/><form:label path="militaryBallotRequest.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Military Blank Ballot receipt</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBlankBallot.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBlankBallot.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBlankBallot.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBlankBallot.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="militaryBlankBallot.post" cssClass="check"/><form:label path="militaryBlankBallot.post"> Post</form:label>
          <form:checkbox path="militaryBlankBallot.fax" cssClass="check"/><form:label path="militaryBlankBallot.fax"> Fax</form:label>
          <form:checkbox path="militaryBlankBallot.email" cssClass="check"/><form:label path="militaryBlankBallot.email"> Email</form:label>
          <form:checkbox path="militaryBlankBallot.tel" cssClass="check"/><form:label path="militaryBlankBallot.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Military Absentee Ballot Return</h4>
					<p class="ctrl">
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBallotReturn.post}">checked="checked" </c:if>>
						Post
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBallotReturn.fax}">checked="checked" </c:if>>
						Fax
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBallotReturn.email}">checked="checked" </c:if>>
						Email
						<input type="checkbox" disabled="disabled" <c:if test="${svid.militaryBallotReturn.tel}">checked="checked" </c:if>>
            Online
					</p>
					<fieldset class="pnl">
          <span class="field-name">&nbsp;</span>
          <form:checkbox path="militaryBallotReturn.post" cssClass="check"/><form:label path="militaryBallotReturn.post"> Post</form:label>
          <form:checkbox path="militaryBallotReturn.fax" cssClass="check"/><form:label path="militaryBallotReturn.fax"> Fax</form:label>
          <form:checkbox path="militaryBallotReturn.email" cssClass="check"/><form:label path="militaryBallotReturn.email"> Email</form:label>
          <form:checkbox path="militaryBallotReturn.tel" cssClass="check"/><form:label path="militaryBallotReturn.tel"> Online</form:label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Military Notes</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.militaryNotes}">${fn:replace(fn:escapeXml(svid.militaryNotes),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label> <span class="field-name">Note</span>
						<spring:bind path="svid.militaryNotes">
							<textarea name="${status.expression}"><c:out value='${svid.militaryNotes}' /></textarea>
						</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>

			<div class="sect deadlines">
				<c:forEach items="${svid.elections}" var="election" varStatus="indx">
					<div class="corrected">
             <div class="dates-deadlines">
            <div class="election-title">
              <h2><c:out value="${election.title}" /></h2>
              <a href="<c:url value="/admin/ElectionEdit.htm"><c:param name="electionId" value="${election.id}"/></c:url>" title="Edit this Election">Edit this Election</a>
            </div>

						<h4>Type of Election</h4>
						<p class="">
							<c:out value="${election.typeOfElection}" />
						</p>
						<h4>Election Date</h4>
						<p class="">
							<c:out value="${election.heldOn}" />
						</p>
						<table>
							<tr>
								<td class="first-col">&nbsp;</td>
								<th>Citizen</th>
								<th>Military</th>
								<th>US</th>
							</tr>
							<tr>
								<th class="first-col">Absentee Voter Registration</th>
								<td><c:out value='${election.citizenRegistration}' /></td>
								<td><c:out value='${election.militaryRegistration}' /></td>
								<td><c:out value='${election.domesticRegistration}' /></td>
							</tr>
							<tr>
								<th class="first-col">Ballot Request</th>
								<td><c:out value='${election.citizenBallotRequest}' /></td>
								<td><c:out value='${election.militaryBallotRequest}' /></td>
								<td><c:out value='${election.domesticBallotRequest}' /></td>
							</tr>
							<tr>
								<th class="first-col">Ballot Return</th>
								<td><c:out value='${election.citizenBallotReturn}' /></td>
								<td><c:out value='${election.militaryBallotReturn}' /></td>
								<td><c:out value='${election.domesticBallotReturn}' /></td>
							</tr>
							<tr>
								<th class="first-col">Early Voting Dates</th>
								<td></td>
								<td></td>
								<td>${election.domesticEarlyVoting}</td>
							</tr>
              <tr>
                <th>Notes</th>
                <td colspan=3>${fn:replace(fn:escapeXml(election.notes),newLineChar,"<br />")}</td>
              </tr>
              <tr>
              <th>Order</th>
              <td colspan=3><c:out value="${election.order}" /></td>
            </tr>
						</table>
            </div>
            </div>
				</c:forEach>

        <div class="sect admin-notes last">
				<div class="corrected">
           <br/>
					<h4>Admin Notes (not public)</h4>
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty svid.adminNotes}">${fn:replace(fn:escapeXml(svid.adminNotes),newLineChar,"<br />")}</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label><span class="field-name">Admin Notes</span>
						<spring:bind path="svid.adminNotes">
							<textarea name="${status.expression}"><c:out value='${svid.adminNotes}' /></textarea>
						</spring:bind>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>

			</div>

			<%-- todo --%>

			<%--<div class="sect complete-status last">
				<h4>Completion status</h4>
				<spring:bind path="svid.status">
					<label>
						<span>Complete</span>
						<input type="radio" name="${status.expression}" <c:if test="${status.value eq 1}">checked="checked"</c:if> value="1" />
					</label>
					<label>
						<span>Incomplete</span>
						<input type="radio" name="${status.expression}" <c:if test="${status.value eq 0}">checked="checked"</c:if> value="0" />
					</label>
				</spring:bind>
				<div class="break"></div>
			</div>--%>
			<%-- There is no status for SSVID--%>


			</div>
			<div id="continue">
				<input type="hidden" name="stateId" value="${svid.state.id}" />
				<input type="hidden" name="save" value="Save" />
				<a href="<c:url value="/admin/EodVotingRegions.htm"><c:param name="stateId" value="${svid.state.id}"/></c:url>"> &larr;
					Return to ${svid.state.name} </a>
				<button  style="float:right;" type="image" onclick="document.eodForm.submission.value = true;" /> Save Changes to ${svid.state.name}</button>
				<div class="break"></div>
			</div>
		</form:form>
        <form:form action="${actionUrl}" method="get">
            <label for="gotoState">To state</label> <select name="stateId" id="gotoState">
            <c:forEach items="${states}" var="state">
                <option value="${state.id}">${state.name}</option>
            </c:forEach>
            </select> &nbsp;
            <input type="submit" value="Go">
        </form:form>
   <p>&nbsp;</p>
	</div>

	<div class="ft"></div>
</div>
<form action="<c:url value="/ajax/EmailToOfficer.htm"/>" method="get" id="emptyForm" ></form>



