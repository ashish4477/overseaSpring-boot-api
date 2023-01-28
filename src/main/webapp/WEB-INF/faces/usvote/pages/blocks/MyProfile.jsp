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

<%--<div class="row">--%>
<%--  <div class="col-xs-12 col-sm-10">--%>
<%--  <h3>Generate Voter Registration &amp; Ballot Requests</h3>--%>
<%--     <div class="content">--%>
<%--      <a class="button small" href="<c:url value="/voter-registration-absentee-voting.htm"/>">Voter Registration/Absentee Ballot Request Form</a><br/>--%>
<%--      <a class="mva-fwab" href="<c:url value="/FwabStart.htm"/>">Federal Write-In Absentee Ballot</a> <span class="hint"><strong>(Use only if your ballot is late - </strong><a href="https://vhd.overseasvotefoundation.org/unified/index.php?group=ovf&_m=knowledgebase&_a=view&parentcategoryid=46&pcid=&nav=" class="more" target="_top"><cite><strong>more info..</strong></cite></a>)</span>--%>
<%--     </div>--%>
<%--  </div>--%>
<%--</div>--%>
<%--<div class="row">--%>
<%--	<div class="col-xs-6 col-sm-4 voterAccount">--%>
<%--    <h3 class="mva">Voter Account <small><a href="<c:url value="/Login.htm"/>" tabindex="-1" role="menuitem">(Edit Account)</a></small></h3>--%>
<%--    <div class="content">--%>
<%--    <c:if test="${not empty user.name.firstName}">${user.name.firstName}</c:if> <c:if test="${not empty user.name.middleName}">${user.name.middleName}</c:if> <c:if test="${not empty user.name.lastName}">${user.name.lastName}</c:if> <c:if test="${not empty user.name.suffix}">${user.name.suffix}</c:if>--%>
<%--    <p>${user.username}</p>--%>
<%--    <br/>--%>
<%--    </div>--%>
<%--  </div>--%>
<%--	<div class="col-xs-6">--%>
<%--    <h3>Contact Information</h3>--%>
<%--    <div class="content">--%>
<%--      <c:if test="${not empty user.phone}"><strong>Phone:</strong> ${user.phone}<br /></c:if>--%>
<%--      <c:if test="${not empty user.alternatePhone}"><strong>Alt. Phone:</strong> ${user.alternatePhone}<br /></c:if>--%>
<%--      <c:if test="${not empty user.alternateEmail}"><strong>Alt. Email:</strong> ${user.alternateEmail}<br /></c:if>--%>
<%--    </div>--%>
<%--	</div>--%>
<%--</div>--%>


<%--<div class="row">--%>
<%--   <div class="col-xs-12 col-sm-4">--%>
<%--      <h3>Voting Address</h3>--%>
<%--      <div class="voting address content">${user.votingAddress.formattedAddress}</div>--%>
<%--      <c:if test="${not empty user.previousAddress and not user.previousAddress.emptySpace }">--%>
<%--        <h3>Previous Address</h3>--%>
<%--        <div class="content">${user.previousAddress.formattedAddress}</div>--%>
<%--      </c:if>--%>
<%--    </div>--%>
<%--    <div class="col-xs-12 col-sm-6">--%>
<%--      <c:if test="${not empty user.currentAddress and not user.currentAddress.emptySpace }">--%>
<%--        <h3>Current  Address</h3>--%>
<%--        <div class="current address content">${user.currentAddress.formattedAddress}</div>--%>
<%--      </c:if>--%>
<%--      <c:if test="${not empty user.forwardingAddress and not user.forwardingAddress.emptySpace }">--%>
<%--        <h3>Ballot Forwarding Address</h3>--%>
<%--        <div class="content">${user.forwardingAddress.formattedAddress}</div>--%>
<%--     </c:if>--%>
<%--    </div>--%>
<%--</div>--%>
<section class="extended-profile">
  <div class="section title">
     <h3>More About Me <span><a style="font-weight: normal; color:#fff; font-size:11px;" href="#">(edit)</a></span></h3>
  </div>

<div class="clearfix"></div>
    <c:import url="/WEB-INF/${relativePath}/pages/blocks/ExtendedProfile.jsp"/>
</section>
<div class="row">
  <div class="col-xs-12 col-sm-10">
  <small><strong>Note to all "My Voter Account" holders:</strong><br/>
    <p>We <cite>never</cite> send email to account holders requesting Voter Account registration details, account updates, or suggesting that your account will expire.  Any such emails can be considered spam and do not originate from us.</p></small>
  </div>
</div>

