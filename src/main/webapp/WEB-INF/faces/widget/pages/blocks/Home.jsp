<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>

    <div class="jumbotron">
      <div class="container">
        <div class="col-xs-10 col-sm-4 col-sm-offset-1 col-lg-3 hidden-xs">
        <h3>My voter account keeps me engaged in civic life back home</h3>
        <p><a class="btn btn-primary btn-lg" href="<c:url value='/Login.htm'/>" role="button">Learn more »</a></p>
        </div>
      </div>
    </div>
	<div class="container-fluid registration">
		<div class="container" >
			<div class="row">
           <form action="<c:url value='/w/rava.htm'/>" method="get">
				  <div class="col-xs-12 col-sm-6 col-md-5 col-md-offset-1 col-lg-4 col-lg-offset-2">
					<label class="form-control-static pull-right text-center"><h1>Register to Vote &amp; Request<br/> Your Absentee Ballot</h1></label>
				  </div>
				  <div class="col-xs-10 col-sm-4 col-md-4 col-lg-3">
  	          <select class="form-control" name="voterType">
                <option value="">CHOOSE YOUR VOTER TYPE</option>
                <c:forEach items="${voterTypesList}" var="voterType">
                  <option value="${voterType.name}">${voterType.value}</option>
                </c:forEach>
                <optgroup label=""></optgroup>
              </select>
				  </div>
				  <div class="col-xs-2 col-sm-1">
					<button type="submit" class="btn btn-primary">Next</button>
				  </div>
				</form>
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
          <p><a class="btn btn-primary" href="<c:url value='/state-voting-information'/>" role="button">View details »</a></p>
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