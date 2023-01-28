<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jul 3, 2007
  Time: 7:05:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<div id="login-form" class="row">
  <div class="col-xs-12 col-sm-8 col-sm-offset-2">
    <c:if test="${not empty param.login_error}">
      <div class="text-center alert alert-danger">
          <p class="error mb-0">Sorry, you entered incorrect login or password.</p>
      </div>
    </c:if>
    <h2 class="title">My Voter Account Sign In</h2><br/>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/LoginForm.jsp" />
		<div id="create-account">
      <br/>
      <h3>Don't Have a My Voter Account Yet?</h3>
      <ul id="features" class="list-unstyled">
        <li><span class="glyphicon glyphicon-ok"></span> Quick Write-in Ballot Access</li>
        <li><span class="glyphicon glyphicon-ok"></span> State Election Dates & Deadlines</li>
        <li><span class="glyphicon glyphicon-ok"></span> State Election Official Contact Information</li>
        <li><span class="glyphicon glyphicon-ok"></span> Recent Votes From Your Representatives</li>
      </ul>
      <!--<a href="<c:url value="/about-voter-account"/>" class="details">Learn More</a> -->
       <a class="button pull-right" href="<c:url value='/CreateAccount.htm'/>"> Create Voter Account</a>
		</div>
	</div>
</div>
