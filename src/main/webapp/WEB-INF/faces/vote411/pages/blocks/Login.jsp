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
  <div class="col-12 col-sm-8 offset-sm-2">
    <c:if test="${not empty param.login_error}">
      <div class="text-center alert alert-danger">
          <p class="error mb-0">Sorry, you entered incorrect login or password.</p>
      </div>
    </c:if>
    <h2 class="title">My Voter Account Sign In</h2><br/>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/LoginForm.jsp" />
		<div id="create-account">
      <br/><br/>
      <h3>Get Your Voting & Election Information All in One Place</h3>
       <p>&nbsp; Your Voter Account can help you stay informed and cultivate your 'personal democracy'</p>
            <ul id="features">
              <li><img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/></span> Your account moves with you and informs you as a voter</li>
              <li><img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/> Get the latest fact-checked news and information</li>
              <li><img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/> See your elected officials and check their legislative history</li>
              <li><img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/> Find out about upcoming election dates and deadlines</li>
              <li><img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/> Quick access to local election office contact information</li>
            </ul>
      <!--<a href="<c:url value="/about-voter-account"/>" class="details">Learn More</a> -->
       <a class="button pull-right" href="<c:url value='/CreateAccount.htm'/>"> Create Voter Account</a>
		</div>
	</div>
</div>
