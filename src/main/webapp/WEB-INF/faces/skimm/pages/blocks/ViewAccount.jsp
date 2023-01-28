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

  <c:if test="${empty selectedRegion}">
    <div class="modal fade error-message" id="update-account">
      <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">x</span></button>
                <h4 class="modal-title text-center">Account Update Required</h4>
                <br/>
                <p class="text-center">Our system has changed, please update your account and select your voting region to continue.</p>
            </div>
            <div class="modal-body text-center">
                <a class="button" href="<c:url value="/UpdateAccount.htm#updateRequired"/>">Update Your Account</a>
            </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <script type="text/javascript">
      $(window).load(function(){
          $('#update-account').modal('show');
      });
    </script>
  </c:if>

  <div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1">
      <h3>Generate Voter Registration &amp; Ballot Requests</h3>
        <a class="button small" href="<c:url value="/voter-registration-absentee-voting.htm"/>">Voter Registration / Absentee Ballot Request Form</a><br/>
        <c:if test="${not empty user.voterType}"><a class="mva-fwab" href="<c:url value="/FwabStart.htm"/>">Federal Write-In Absentee Ballot</a> <span class="hint"><strong>(Use only if your ballot is late - </strong><a href="https://vhd.overseasvotefoundation.org/unified/index.php?group=ovf&_m=knowledgebase&_a=view&parentcategoryid=46&pcid=&nav=" class="more" target="_top"><cite><strong>more info..</strong></cite></a>)</span></c:if>
    </div>
  </div>
  <div class="row">
      <div class="col-xs-12 col-sm-4 col-sm-offset-1 voterAccount">
            <h3 class="mva">Voter Account <small><a href="<c:url value="/UpdateAccount.htm"/>" tabindex="-1" role="menuitem">(Edit Account)</a></small></h3>
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
    <div class="section title col-xs-12">
      <h3 style="color:#fff;">More About Me <span><a style="font-weight: normal; color:#fff; font-size:60%;" href="<c:url value="/MyProfile.htm"/>">(edit)</a></span></h3>
    </div>
  </div>

  <c:if test="${empty extendedProfile.user}">
      <c:import url="/WEB-INF/${relativePath}/pages/blocks/ExtendedProfile.jsp"/>
</c:if>
  <c:if test="${not empty extendedProfile.user}">
  <div class="row answers">
    <div class="col-xs-12">
        <h3>Voter Information</h3>
        <fieldset>
        <c:if test="${not empty extendedProfile.voterType}">
        <div class="col-xs-12 col-sm-4">
        <h3>Voter Type:</h3>
        <p>${extendedProfile.voterType}</p>
        </div>
        </c:if>
        <c:if test="${not empty extendedProfile.votingMethod}">
        <div class="col-xs-12 col-sm-4">
        <h3>Primary Voting Method:</h3>
        <p>${extendedProfile.votingMethod}</p>
        </div>
        </c:if>
        <c:if test="${not empty extendedProfile.politicalParty}">
        <div class="col-xs-12 col-sm-4">
        <h3>Political Party:</h3>
        <p>${extendedProfile.politicalParty}</p>
        </div>
        </c:if>
        </fieldset>
    </div>
  </div>
    <div class="row answers">
      <div class="col-xs-12">
      <h3>Election Participation</h3>
      <fieldset>
      <c:if test="${not empty extendedProfile.voterParticipationArr}">
      <div class="col-xs-12 col-sm-4">
      <h3>I Vote in:</h3>
      <ul class="list-unstyled">
       <c:set var="electionTypes">
       <c:out value="${extendedProfile.voterParticipation}" />
       </c:set>
       <c:set var="elections" value="${fn:split(electionTypes,';')}" />
       <c:forEach items="${elections}" var="myElections">
          <li><span class="glyphicon glyphicon-ok"></span> <c:out value="${myElections}" /></li>
       </c:forEach>
       <c:if test="${not empty extendedProfile.voterParticipationOther}">
         <li><span class="glyphicon glyphicon-ok"></span> ${extendedProfile.voterParticipationOther}</li>
      </c:if>
      </ul>

      </div>
      </c:if>
      <c:if test="${not empty extendedProfile.outreachParticipationArr}">
      <div class="col-xs-12 col-sm-4">
      <h3>Voter Outreach:</h3>
      <ul class="list-unstyled">
       <c:set var="outreachTypes">
       <c:out value="${extendedProfile.outreachParticipation}" />
       </c:set>
       <c:set var="outreach" value="${fn:split(outreachTypes,';')}" />
       <c:forEach items="${outreach}" var="myOutreach">
         <li><span class="glyphicon glyphicon-ok"></span> <c:out value="${myOutreach}" /></li>
       </c:forEach>
        <c:if test="${not empty extendedProfile.outreachParticipationOther}">
          <li><span class="glyphicon glyphicon-ok"></span> ${extendedProfile.outreachParticipationOther}</li>
        </c:if>
      </ul>
      </div>
    </c:if>
      <c:if test="${not empty extendedProfile.volunteeringArr}">
            <div class="col-xs-12 col-sm-4">
            <h3>Volunteering:</h3>
            <ul class="list-unstyled">
             <c:set var="volunteeringTypes">
             <c:out value="${extendedProfile.volunteering}" />
             </c:set>
             <c:set var="volunteering" value="${fn:split(volunteeringTypes,';')}" />
             <c:forEach items="${volunteering}" var="myVolunteering">
                <li><span class="glyphicon glyphicon-ok"></span> <c:out value="${myVolunteering}" /></li>
             </c:forEach>
             <c:if test="${not empty extendedProfile.volunteeringOther}">
               <li><span class="glyphicon glyphicon-ok"></span> ${extendedProfile.volunteeringOther}</li>
            </c:if>
            </ul>
            </div>
            </c:if>
      </fieldset>
  </div>
  </div>
   <div class="row answers">
     <div class="col-xs-12">
     <h3>Voting Participation</h3>
     <fieldset>
     <c:if test="${not empty extendedProfile.socialMediaArr}">
      <div class="col-xs-12 col-sm-4">
      <h3>Social Media:</h3>
      <ul class="list-unstyled">
       <c:set var="socialMediaTypes">
       <c:out value="${extendedProfile.socialMedia}" />
       </c:set>
       <c:set var="socialMedia" value="${fn:split(socialMediaTypes,';')}" />
       <c:forEach items="${socialMedia}" var="myMediaTypes">
         <li><span class="glyphicon glyphicon-ok"></span> <c:out value="${myMediaTypes}" /></li>
       </c:forEach>
        <c:if test="${not empty extendedProfile.socialMediaOther}">
          <li><span class="glyphicon glyphicon-ok"></span> ${extendedProfile.socialMediaOther}</li>
        </c:if>
      </ul>
      </div>
    </c:if>
    <c:if test="${not empty extendedProfile.satisfaction}">
      <div class="col-xs-12 col-sm-6">
      <h3>Satisfaction with the voter registration/ballot request process for the last election:</h3>
        <c:if test="${not empty extendedProfile.satisfaction}">
          ${extendedProfile.satisfaction}
        </c:if>
      </div>
    </c:if>
  </div>
  </c:if>
</fieldset>
</div>
</div>
<div class="row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
  <small><strong>Note to all "My Voter Account" holders:</strong><br/>
    <p>We <cite>never</cite> send email to account holders requesting Voter Account registration details, account updates, or suggesting that your account will expire.  Any such emails can be considered spam and do not originate from us.</p></small>
  </div>
</div>


