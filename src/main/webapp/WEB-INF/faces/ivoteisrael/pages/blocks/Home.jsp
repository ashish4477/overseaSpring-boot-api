<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<div class="column ${styleClass} wide-content page-form rava-intro">

<h1>Register to Vote &amp; Request Your Absentee Ballot</h1>

   <div class="row">
     <div class="col-xs-12 form-type-selection">
  <form id="voterRegistrationAbsenteeBallotForm" method="get" class="form-group" action="<c:url value='/w/rava.htm'></c:url>">
    <div class="form-group">
      <div class="col-xs-10 center-block">
    <fieldset>
         <ul class="list-unstyled">
           <li><span class="glyphicon glyphicon-ok"></span> Get Automated Overseas Voter Registration Assistance</li>
           <li><span class="glyphicon glyphicon-ok"></span> Prepare Your Annual Absentee Ballot Request</li>
         </ul>

          <select name="voterClassificationType" class="input-lg form-control">
			<option value="">Please choose your voter type...</option>
			<c:forEach items="${voterClassificationTypesList}" var="voterClassificationType">
				<option value="${voterClassificationType.name}">${voterClassificationType.value}</option>
			</c:forEach>
			<optgroup label=""></optgroup>
		</select>

    <div class="row">
      <div class="col-sm-12">
        <input type="submit" name="Continue" value="Get Started" class="submit-button pull-right" />
      </div>
    </div>

      <div class="row fwab">
      <section class="col-xs-12">
         <footer>
          <strong>Missing Your Absentee Ballot?</strong><br />
          <p>
          <small>If you have already requested an absentee ballot but have not
          yet received it, you can still vote by using the back-up
          <a href="/vote/FwabStart.htm">Federal Write-In Absentee Ballot (FWAB).</a> </small>
          </p>
         </footer>
      </section>
    </div>
  </fieldset>
       </div> </div>
  </form>
</div>
</div>
</div>
<br/><br/>
<div class="row">
  <section class="content-bottom col-xs-12">
    <div class="region region-content-bottom1">
      <section class="block block-block featured-voting-tools row clearfix" id="block-block-8">

      <div class="col-sm-4 eod"><img width="62" height="62" src="/vote/img/icons/eod.png"><h2>Election Official<br>Directory</h2>
      <p class="featured-tools-descript center-block">Find your Local Election Official and get local contact information</p>
      <p><a class="button" href="/vote/eod.htm">Find Your Official</a></p>
      </div>
      <div class="col-sm-4 svid"><img width="51" height="62" src="/vote/img/icons/svid.png"><h2>State Voting<br>Information</h2>
      <p class="featured-tools-descript center-block">View election dates &amp; deadlines and voting requirements</p>
      <p><a class="button" href="/vote/svid.htm">View Now</a></p>
      </div>
      <div class="col-sm-4 mva"><img width="64" height="62" src="/vote/img/icons/voter-account.png"><h2>My Voter<br>Account</h2>
      <p class="featured-tools-descript center-block">Get one click access to state information and recent votes from your representatives</p>
      <p><a class="button" href="/vote/CreateAccount.htm">Create Account</a></p>
      </div>

      </section> <!-- /.block -->
    </div>
  </section>
  <p>&nbsp;</p>
</div>