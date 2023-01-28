<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<div class="jumbotron">
      <div class="container">
        <a href="/vote/state-elections/state-election-dates-deadlines.htm">&nbsp;</a>
      </div>
    </div>
	<div class="container-fluid registration">
		<div class="container" >
			<div class="row">
				  <div class="col-xs-12 registration-content">
                      <div class="registration-title"><label class="form-control-static text-center registration-title">Register to Vote &amp; Request<br/> Your Absentee Ballot</label></div>
                      <div class="registration-button"><button type="submit" class="btn btn-primary" data-toggle="modal" data-target="#registration-redirect-popup">GO</button></div>
				  </div>
			</div>
		</div>
	</div>
	<div class="container services">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-xs-12 col-sm-4">
          <h3>State Voter<br/>Information Directory</h3>
          <p>Check Your Election
Dates &amp; Deadlines </p>
          <p><a class="btn btn-primary" href="<c:url value='/svid.htm'/>" role="button">View details »</a></p>
       </div>
        <div class="col-xs-12 col-sm-4">
          <h3>Election Official<br/>Directory</h3>
          <p>Contact Your Absentee Voting Clerk</p>
          <p><a class="btn btn-primary" href="<c:url value='/election-official-directory'/>" role="button">View details »</a></p>
        </div>
        <div class="col-xs-12 col-sm-4">
          <h3>Federal Write-in<br/>Absentee Ballot</h3>
          <p>Use This Method if Your Requested Ballot is Late</p>
          <p><a class="btn btn-primary" href="<c:url value='/FwabStart.htm'/>" role="button">View details »</a></p>
        </div>
      </div>
