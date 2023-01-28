<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 10, 2007
	Time: 4:09:39 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="voter-account-page" class="voter-account-page page-form body-content content column wide-content">
<c:if test="${updated}">
  <div id="update-message" class="col-xs-12 alert alert-success">Your account information has been updated</div>
  </c:if>

  <div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1">
      <h3>Generate Voter Registration &amp; Ballot Requests</h3>
      <br/>
      <a class="button" href="<c:url value="/w/rava.htm"/>">Voter Registration / Absentee Ballot Request Form</a><br/>
      <c:if test="${not empty user.voterType}"><a href="<c:url value="/FwabStart.htm"/>">Federal Write-In Absentee Ballot</a> <span class="hint">(Use only if your ballot is late - <a href="https://vhd.overseasvotefoundation.org/unified/index.php?group=ovf&_m=knowledgebase&_a=view&parentcategoryid=46&pcid=&nav=" class="more" target="_top"><cite> more info..</cite></a>)</span></c:if>
    </div>
  </div>
  <div class="row">
      <div class="col-xs-12 col-sm-4 col-sm-offset-1 voterAccount">
        <h3 class="mva">Voter Account</h3>
        <c:if test="${not empty user.name.firstName}">${user.name.firstName}</c:if> <c:if test="${not empty user.name.middleName}">${user.name.middleName}</c:if> <c:if test="${not empty user.name.lastName}">${user.name.lastName}</c:if> <c:if test="${not empty user.name.suffix}">${user.name.suffix}</c:if>
        <p>${user.username}</p>
        <br/>
      </div>
      <div class="col-xs-12 col-sm-6">
        <h3>Contact Information</h3>
          <c:if test="${not empty user.phone}"><strong>Phone:</strong> ${user.phone}<br /></c:if>
          <c:if test="${not empty user.alternatePhone}"><strong>Alt. Phone:</strong> ${user.alternatePhone}<br /></c:if>
          <c:if test="${not empty user.alternateEmail}"><strong>Alt. Email:</strong> ${user.alternateEmail}<br /></c:if>
      </div>
  </div>

  <div class="row">
     <div class="col-xs-12 col-sm-4 col-sm-offset-1">
        <h3>Voting Address</h3>
        <div class="voting address content">${user.votingAddress.formattedAddress}</div>
        <c:if test="${not empty user.previousAddress and not user.previousAddress.emptySpace }">
          <h3>Previous Address</h3>
          ${user.previousAddress.formattedAddress}
        </c:if>
      </div>
      <div class="col-xs-12 col-sm-6">
        <c:if test="${not empty user.currentAddress and not user.currentAddress.emptySpace }">
          <h3>Current  Address</h3>
          ${user.currentAddress.formattedAddress}
        </c:if>
        <c:if test="${not empty user.forwardingAddress and not user.forwardingAddress.emptySpace }">
          <h3>Ballot Forwarding Address</h3>
          ${user.forwardingAddress.formattedAddress}
       </c:if>
      </div>
  </div>


  <div class="row extended-profile">
    <div class="title col-xs-12">
      <h3 style="color:#fff;">More About Me <span><a style="font-weight: normal; color:#fff; font-size:60%;" href="<c:url value="/MyProfile.htm"/>">(edit)</a></span></h3>
    </div>
  </div>

  <c:if test="${empty extendedProfile.user}">
      <c:import url="/WEB-INF/${relativePath}/pages/blocks/ExtendedProfile.jsp"/>
</c:if>
  <c:if test="${not empty extendedProfile.user}">
  <div class="row answers">
        <h2>Voter Information</h2>
        <c:if test="${not empty extendedProfile.voterType}">
        <div class="col-xs-12 col-sm-4">
        <h4>Voter Type</h4>
        ${extendedProfile.voterType}
        </div>
        </c:if>
        <c:if test="${not empty extendedProfile.votingMethod}">
        <div class="col-xs-12 col-sm-4">
        <h4>Primary Voting Method</h4>
        ${extendedProfile.votingMethod}
        </div>
        </c:if>
        <c:if test="${not empty extendedProfile.politicalParty}">
        <div class="col-xs-12 col-sm-4">
        <h4>Political Party</h4>
        ${extendedProfile.politicalParty}
        </div>
        </c:if>
    </div>
    <div class="row answers">
      <c:if test="${not empty extendedProfile.voterParticipationArr}">
      <div class="col-xs-12 col-sm-4">
      <h4>I Vote in:</h4>
      <ul>
       <c:set var="electionTypes">
       <c:out value="${extendedProfile.voterParticipation}" />
       </c:set>
       <c:set var="elections" value="${fn:split(electionTypes,';')}" />
       <c:forEach items="${elections}" var="myElections">
          <li><c:out value="${myElections}" /></li>
       </c:forEach>
       <c:if test="${not empty extendedProfile.voterParticipationOther}">
         <li>${extendedProfile.voterParticipationOther}</li>
      </c:if>
      </ul>

      </div>
      </c:if>
      <c:if test="${not empty extendedProfile.outreachParticipationArr}">
      <div class="col-xs-12 col-sm-4">
      <h4>Voter Outreach:</h4>
      <ul>
       <c:set var="outreachTypes">
       <c:out value="${extendedProfile.outreachParticipation}" />
       </c:set>
       <c:set var="outreach" value="${fn:split(outreachTypes,';')}" />
       <c:forEach items="${outreach}" var="myOutreach">
         <li><c:out value="${myOutreach}" /></li>
       </c:forEach>
        <c:if test="${not empty extendedProfile.outreachParticipationOther}">
          <li>${extendedProfile.outreachParticipationOther}</li>
        </c:if>
      </ul>
      </div>
    </c:if>
      <c:if test="${not empty extendedProfile.volunteeringArr}">
            <div class="col-xs-12 col-sm-4">
            <h4>Volunteering:</h4>
            <ul>
             <c:set var="volunteeringTypes">
             <c:out value="${extendedProfile.volunteering}" />
             </c:set>
             <c:set var="volunteering" value="${fn:split(volunteeringTypes,';')}" />
             <c:forEach items="${volunteering}" var="myVolunteering">
                <li><c:out value="${myVolunteering}" /></li>
             </c:forEach>
             <c:if test="${not empty extendedProfile.volunteeringOther}">
               <li>${extendedProfile.volunteeringOther}</li>
            </c:if>
            </ul>
            </div>
            </c:if>
  </div>
  </div>
   <div class="row answers">
     <c:if test="${not empty extendedProfile.socialMediaArr}">
      <div class="col-xs-12 col-sm-4">
      <h4>Social Media:</h4>
      <ul>
       <c:set var="socialMediaTypes">
       <c:out value="${extendedProfile.socialMedia}" />
       </c:set>
       <c:set var="socialMedia" value="${fn:split(socialMediaTypes,';')}" />
       <c:forEach items="${socialMedia}" var="myMediaTypes">
         <li><c:out value="${myMediaTypes}" /></li>
       </c:forEach>
        <c:if test="${not empty extendedProfile.socialMediaOther}">
          <li>${extendedProfile.socialMediaOther}</li>
        </c:if>
      </ul>
      </div>
    </c:if>
    <c:if test="${not empty extendedProfile.satisfaction}">
      <div class="col-xs-12 col-sm-8">
      <h4>Satisfaction with the voter registration/ballot request process for the last election:</h4>
        <c:if test="${not empty extendedProfile.satisfaction}">
          ${extendedProfile.satisfaction}
        </c:if>
      </div>
    </c:if>
  </div>
  </c:if>
<div class="row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
  <small><strong>Note to all "My Voter Account" holders:</strong><br/>
    <p>We <cite>never</cite> send email to account holders requesting Voter Account registration details, account updates, or suggesting that your account will expire.  Any such emails can be considered spam and do not originate from us.</p></small>
  </div>
</div>


