<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"
	type="text/javascript"></script>
<script src='<c:url value="/js/bootstrap/bootstrap.min.js"/>'
	type="text/javascript"></script>

<%-- This has to be done this way in order to access the errors --%>
<c:set var="modelAttr" value="whatsOnMyBallot" />
<c:if test="${not empty param.modelAttribute}">
	<c:set var="modelAttr" value="${param.modelAttribute}" />
</c:if>

<div class="body-content page-form rava content column wide-content">
	<div class="page-form">
		<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
			<c:param name="title" value="What's on My Ballot?" />
			<c:param name="progressLabel" value="What's on My Ballot? progress" />
		</c:import>

		<div class="bd" id="overseas-vote-foundation-short">
			<div class="bd-inner">
				<form:form class="userFields" modelAttribute="${modelAttr}"
					method="post" name="whatsOnMyBallot" id="whatsOnMyBallot">
          <fieldset class="question-group">
          <div class="legend"><span>Political Party Selection</span></div>
          <p>You are only allowed to vote for the candidates of one political party for partisan offices in a primary election.</p>

          <p>No record will be made of your political party choice. Your selection will only be used to direct you to the proper ballot information and then will be deleted at the end of your session today.</p>

          <p>Please select a political party below. You will only see the names of candidates who are with the political party you choose and the candidates running for non-partisan offices.  To see the names of candidates in other political parties, use the back button to return to this screen and select a different political party.</p>
          </fieldset>
					<fieldset class="question-group">
						<div class="legend">
							<span>Please Select a Party for the Partisan Races: </span>
						</div>

						<div class="question">
              <div class="radio-group single-field no-rava-bubble">
							<c:forEach items="${partisanParties}" var="partisanParty">
								<label class="one-line">
                <span class="radio"><form:radiobutton path="partisanParty" id="${partisanParty}" value="${partisanParty}"/></span>
								<span class="label">${partisanParty}</span><span class="break"></span></label>
								<br/>
							</c:forEach>
							<form:errors path="partisanParty" cssClass="error"/>
             </div>
						</div>
					</fieldset>
					<input type="image"
						src='<c:url value="/img/buttons/continue-button.gif"/>'
						name="create" value="Continue" class="submit-button"
						id="submitButton" />
				</form:form>
			</div>
		</div>
		<div class="ft">
			<div class="ft-inner"></div>
		</div>
	</div>
</div>