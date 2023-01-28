<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<div class="column ${styleClass} wide-content page-form rava-intro">
  <h1 class="title">${pageTitle}</h1>
  <h2>What Type of Voting Form Would You Like to Prepare?</h2>
	<form class="output-type" role="form" action="<c:url value="/voter-registration-absentee-voting.htm"/>" method="post" id="voterRegistrationAbsenteeBallotForm">
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
  <div class="row">
    <div class="col-xs-12 form-type-selection">
  <fieldset>
    <div class="row">
      <div class="col-xs-12 col-sm-4 domestic form-type">
      <div><img src="<c:url value="/img/icons/us-map.png"/>"/></div>
      <h3>U.S. Based Voters</h3>
        <div class="radio-container">
            <div class="radio registration">
              <label> <input type="radio" name="formType"
              value="domestic_registration" required="required" /> Voter Registration</label>
            </div>
          </div>
          <div class="radio-container domestic-absentee">
            <div class="radio">
              <label> <input type="radio" name="formType"
              value="domestic_absentee" required="required" /> Absentee Ballot Request</label>
            </div>
        </div>
      </div>
      <div class="col-xs-12 col-sm-4 overseas form-type">
      <div><img src="<c:url value="/img/icons/world-map.png"/>"/></div>
      <h3>Overseas Citizen Voters</h3>
        <div class="radio-container">
          <div class="radio">
          <label><input type="radio" name="formType" value="rava"
    required="required" /> Voter Registration &<br/> Absentee Ballot Request</label>
          </div>
         </div>
      </div>
      <div class="col-xs-12 col-sm-4 military form-type">
      <div><img src="<c:url value="/img/icons/us-flag.png"/>"/></div>
      <h3>Military Voters</h3>
        <div class="radio-container">
          <div class="radio">
          <label><input type="radio" name="formType" value="rava"
    required="required" /> Voter Registration &<br/> Absentee Ballot Request</label>
          </div>
        </div>
      </div>
    </div>
    <br/>
    <div class="row fwab">
      <section class="col-xs-10 col-xs-offset-1">
         <footer>
          <strong>Overseas Citizens & Military Voters:</strong><br />
          <p>
          <small>If you have already requested an absentee ballot but have not
          yet received it, you can still vote by using the back-up
          <a href="<c:url value="/FwabStart.htm"/>">Federal Write-In Absentee Ballot (FWAB).</a> </small>
          </p>
         </footer>
      </section>
    </div>
  </fieldset>
  </div>
</div>    <br/>
					<input type="submit" name="Continue" value="Continue" class="submit-button pull-right" />
				</form>
  <br/><br/>
  <div class="row">
    <div class="col-xs-12">
      <p><strong>Please Note:</strong> This website will not store your
      social security number, birth date or driver's license number.</p>
    </div>
  </div>
  <br/><br/>
</div>

