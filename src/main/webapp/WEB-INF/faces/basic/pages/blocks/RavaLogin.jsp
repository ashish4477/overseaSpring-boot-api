<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
    <c:when test="${fn:containsIgnoreCase(param.redirect,'/rava/')}">
        <c:set var="pageTitle" value="Register to Vote / Absentee Ballot Request"/>
        <c:set var="deliverable" value="Voter Registration / Absentee Ballot Request Form"/>
        <c:set var="type" value="Form"/>
        <c:set var="styleClass" value="rava"/>
    </c:when>
    <c:when test="${fn:containsIgnoreCase(param.redirect,'/fwab/')}">
        <c:set var="pageTitle" value="Federal Write-in Absentee Ballot"/>
        <c:set var="deliverable" value="Federal Write-in Absentee Ballot"/>
        <c:set var="type" value="Ballot"/>
        <c:set var="styleClass" value="fwab"/>
    </c:when>
    <c:when test="${fn:containsIgnoreCase(param.redirect,'/domestic_registration/')}">
        <c:set var="pageTitle" value="Register to Vote"/>
        <c:set var="deliverable" value="Voter Registration Form"/>
        <c:set var="type" value="Form"/>
        <c:set var="styleClass" value="dom_reg"/>
    </c:when>
    <c:when test="${fn:containsIgnoreCase(param.redirect,'/domestic_absentee/')}">
        <c:set var="pageTitle" value="Absentee Ballot Request"/>
        <c:set var="deliverable" value="Absentee Ballot Request"/>
        <c:set var="type" value="Absentee Ballot Request"/>
        <c:set var="styleClass" value="dom_abs"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="My Voter Account"/>
        <c:set var="styleClass" value="rava-login"/>
    </c:otherwise>
</c:choose>
<div class="body-content">
<div class="column ${styleClass} wide-content page-form rava-intro">
	<div class="hd">
			<div class="hd-inner">
				<h1 class="title">${pageTitle}</h1>
			</div>
		</div>
	<div class="bd">
		<div class="bd-inner">
		<c:if test="${fn:containsIgnoreCase(param.redirect,'/rava/')}">
			<div class="rava video">
				<strong><a href="https://www.youtube.com/watch?v=m3nehKjBFQs" target="_blank" class="ceebox video-link">How to Request <br />Your Absentee Ballot</a></strong>
				<br />
				<a href="https://www.youtube.com/watch?v=ZGy3yxAm-Es" target="_blank" class="ceebox video-link">
					<img alt="Request your absentee ballot video" src="<c:url value='/img/sidebar/rava-absentee-voting-video.jpg'/>">
				</a>
				<br />
				<a href="https://www.youtube.com/watch?v=ZGy3yxAm-Es" target="_blank" class="ceebox video-link">The A-Z of Overseas Voting</a>
				<p>&nbsp;</p>
			</div>
			</c:if>
			<h2>Generate Your ${type} Now</h2>
			<p>Prepare a print ready<strong> Downloadable ${deliverable}</strong> based on the specific requirements of your State.</p>
			<c:if test="${fn:containsIgnoreCase(param.redirect,'/domestic_absentee/')}">
			<div style="border:solid #c0c9ce 1px; border-width:1px 0; padding:8px; margin:10px 0;">
				<h6 style="display:inline; color:#7C0500;">IMPORTANT:</h6>
				<p style="display:inline;">You must be registered to vote before you submit your absentee ballot request.</p>
				<br/><br/>
				<p style="margin-bottom:0;"> Use the link under "Websites and Resources" in our <a href="<c:url value='/eoddomestic.htm'/>">Election Official &amp; State Voter Information Directory</a> to check your voter registration status or switch to <a href="<c:url value='/w/domestic_registration.htm'/>">Voter Registration</a> if you are not currently registered.</p>
			</div>
			</c:if>
			<c:if test="${fn:containsIgnoreCase(param.redirect,'/domestic_registration/') || fn:containsIgnoreCase(param.redirect,'/domestic_absentee/')}">
				<p class="ovf-link"><strong>Overseas Citizens and Military Voters:</strong><br />
				Please visit <a href="https://www.overseasvotefoundation.org" target="_blank">Overseas Vote</a> to produce your voting forms.</p>
			</c:if>
			<h3>After You Enter Your Information:</h3>
			<ol><li>Download the Completed ${type}</li>
				<li>Verify Your Information for Accuracy</li>
				<li>Print, Sign and Mail the ${type} to the Provided Election Office Address</li>
			</ol><br/>
			<div class="break"></div>
			<div class="column left intro">
				<h3>Returning Users</h3>
				<br />
				<h4>Login to Your Voter Account</h4>
				<br />
				<c:import url="/WEB-INF/faces/basic/pages/statics/LoginForm.jsp" />
			</div>
			<div class="columnDivider">&nbsp;</div>
			<div class="column right intro">
				<h3>New Voters or Non-Account Holders</h3>
				<br />
				<h4>Continue Without an Account</h4>

					<form class="state select" action="<c:url value='${param.redirect}'/>" method="get">
						<c:choose>
							<c:when test="${not empty param.cStateId}">
								<input type="hidden" name="stateId" id="stateId" value="${param.cStateId}" />
								<input type="hidden" name="vrState" id="votingRegionState" value="${param.cStateAbbr}" />
							</c:when>
							<c:otherwise>
								<label class="oneline one-line select select-state-field">
									<span>Choose a state:</span> 
									<select class="field" name="vrState" id="select_state">
										<option value="">- Select State -</option>
										<c:forEach items="${states}" var="state">
											<c:if test="${state.abbr != 'ND' or not fn:containsIgnoreCase(param.redirect,'/domestic_registration/')}">
												<option value="${state.abbr}"
													<c:if test="${votingAddress.state eq state.abbr}">selected="selected"</c:if>>${state.name}</option>
											</c:if>
										</c:forEach>
										<optgroup label=""></optgroup>
								</select> </label>
							</c:otherwise>
						</c:choose>
						<p style="padding-top:10px;">(You will have the option to
							create an account when filling out your application)</p>
						<input type="image" src="<c:url value="/img/buttons/continue-button.gif"/>" name="create" value="Continue" class="submit-button" />
					</form>
				</div>
			<div class="break"></div>
			<br/><br/>
			<strong>Please Note:</strong> This website will not store your social security number, birth date or driver's license number.</div>
	</div>
	<div class="ft">
		<div class="ft-inner"></div>
	</div>
	</div>
</div>