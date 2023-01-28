<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<div class="row">
  <div class="col-xs-12 <c:if test="${empty localElections}">text-center</c:if>">
    <h1 class="title">Election Dates &amp; Deadlines</h1>
  </div>
</div>

<c:if test="${empty showCaptcha}">
  <!-- Overseas &amp Military -->
  <c:if test="${not empty localElections}">
    <c:set var="stateInfoId" value="0"/>
    <c:forEach items="${localElections}" var="election">
      <div class="row show-grid election-dates">
        <c:if test="${election.stateId ne stateInfoId}">
          <c:set var="stateInfoId" value="${election.stateId}"/>
          <div class="col-xs-12 state-name" id="${election.stateAbbr}">
            <h1>State of ${election.stateName} Election Dates &amp; Deadlines <small><a href="<c:url value='/us/state-voting-information/${election.stateAbbr}#uocava'/>">(more info)</a></small></h1>
          </div>
        </c:if>
        <c:if test="${empty level or election.sortOrder ne level}">
          <c:set var="level" value="${election.sortOrder}"/>
          <div class="col-xs-12 election-level" id="${level.name}">
            <h2><h4>${level.name} Elections</h4></h2>
          </div>
        </c:if>
        <div class="col-xs-12 election-title"><h2>${election.heldOn} - ${election.title}</h2></div>
      </div>
      <div class="row show-grid match-my-cols election-dates uocava">
        <div class="col-xs-12 col-sm-4"><h3>Voter Registration Deadline</h3>
          <c:set var="citizenRegistration" value="${fn:trim(election.citizenRegistration)}" />
          <c:set var="militaryRegistration" value="${fn:trim(election.militaryRegistration)}" />
          <c:choose>
            <c:when test="${citizenRegistration eq militaryRegistration}">
              <c:choose>
                <c:when test="${not empty citizenRegistration}">${citizenRegistration}</c:when>
                <c:otherwise>None on Record</c:otherwise>
              </c:choose>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${not empty citizenRegistration}"><strong>Overseas:</strong> ${citizenRegistration}</c:when>
                <c:otherwise><strong>Overseas:</strong> None on Record</c:otherwise>
              </c:choose>
              <c:choose>
                <c:when test="${not empty militaryRegistration}"><strong>Military:</strong> ${militaryRegistration}</c:when>
                <c:otherwise><strong>Military:</strong> None on Record</c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
        </div>
        <div class="col-xs-12 col-sm-4"><h3>Ballot Request Deadline</h3>
          <c:set var="citizenBallotRequest" value="${fn:trim(election.citizenBallotRequest)}" />
          <c:set var="militaryBallotRequest" value="${fn:trim(election.militaryBallotRequest)}" />
          <c:choose>
            <c:when test="${citizenBallotRequest eq militaryBallotRequest}">
              <c:choose>
                <c:when test="${not empty citizenBallotRequest}">${citizenBallotRequest}</c:when>
                <c:otherwise>None on Record</c:otherwise>
              </c:choose>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${not empty citizenBallotRequest}"><strong>Overseas:</strong> ${citizenBallotRequest}</c:when>
                <c:otherwise><strong>Overseas:</strong> None on Record</c:otherwise>
              </c:choose>
              <c:choose>
                <c:when test="${not empty militaryBallotRequest}"><strong>Military:</strong> ${militaryBallotRequest}</c:when>
                <c:otherwise><strong>Military:</strong> None on Record</c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
        </div>
        <div class="col-xs-12 col-sm-4"><h3>Ballot Return Deadline</h3>
          <c:set var="citizenBallotReturn" value="${fn:trim(election.citizenBallotReturn)}" />
          <c:set var="militaryBallotReturn" value="${fn:trim(election.militaryBallotReturn)}" />
          <c:choose>
            <c:when test="${citizenBallotReturn eq militaryBallotReturn}">
              <c:choose>
                <c:when test="${not empty citizenBallotReturn}">${citizenBallotReturn}</c:when>
                <c:otherwise>None on Record</c:otherwise>
              </c:choose>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${not empty citizenBallotReturn}"><strong>Overseas:</strong> ${citizenBallotReturn}</c:when>
                <c:otherwise><strong>Overseas:</strong> None on Record</c:otherwise>
              </c:choose>
              <c:choose>
                <c:when test="${not empty militaryBallotReturn}"><strong>Military:</strong> ${militaryBallotReturn}</c:when>
                <c:otherwise><strong>Military:</strong> None on Record</c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
      <p>&nbsp;</p>
    </c:forEach>
  </c:if>

  <c:if test="${not empty param.stateName && empty localElections}">
    <div class="col-xs-12 col-sm-6 col-sm-offset-3 text-center alert alert-danger" style="margin-bottom:12px;">
      <strong>There are no elections on record for this state.</strong>
    </div>
    <p>&nbsp;</p>
  </c:if>
</c:if>

<div class="row">
  <div class="col-xs-12 col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3 text-center">
    <form action="<c:url value="/state-elections/state-election-dates-deadlines.htm"/>" name="eodForm" class="bc-form election-dates-form" method="get" id="eodForm">
    <div class="form-group">
      <h4>Select Your State to View Your Election Dates &amp; Deadlines</h4>
      <input type="hidden" name="uocava" value="true">
        <select class="form-control" id="stateSelection" name="stateName" required>
          <option value="">Select Your State to Continue</option>
          <c:forEach items="${states}" var="state">
            <c:choose>
              <c:when test="${param.stateName eq state.abbr}"><option value="${state.abbr}" selected="selected">${state.name}</option></c:when>
              <c:otherwise><option value="${state.abbr}">${state.name}</option></c:otherwise>
            </c:choose>
          </c:forEach>
        </select>

          <fieldset id="captcha">
            <div class="form-group text-center">
              <c:import url="/WEB-INF/faces/basic/pages/statics/EodCaptcha.jsp" />
            </div>
          </fieldset>
        
        <fieldset id="continue">
          <input class="eodSelect" value="Continue" type="submit"/>  <%-- onclick="return sendEodForm();" --%>
        </fieldset>
      </div>
    </form>
  </div>
</div>

<div class="alert alert-dismissible fade in social" role="alert">
  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
  <p><strong>Help Us Reach More Voters Like You!</strong>&nbsp;&nbsp; <iframe class="pull-right" src="https://www.facebook.com/plugins/like.php?href=https%3A%2F%2Fwww.overseasvotefoundation.org%2Fvote%2Fstate-elections%2Fstate-election-dates-deadlines.htm&width=112&layout=button_count&action=like&size=small&show_faces=false&share=true&height=46&appId" width="112" height="46" style="border:none;overflow:hidden" scrolling="no" frameborder="0" allowTransparency="true"></iframe></p>
</div>

<script type="text/javascript">
  $(window).scroll(function () {
    setTimeout(function(){
      $('.social').fadeIn();
    }, 2000);
  });

  $('#stateSelection').change(function() {
    hash =$(this).val();
    location.hash = "#" + hash;
  });
</script>
