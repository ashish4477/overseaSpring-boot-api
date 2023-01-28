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
        <p class="error">Sorry, you entered incorrect login or password.</p>
    </c:if>
    <h2 class="title">My Voter Account Sign In</h2><br/>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/LoginForm.jsp" />
		<div id="create-account">
      <br/><br/>
      <h3>Get Your Voting & Election Information All in One Place</h3>
       <p>&nbsp; Your Voter Account can help you stay informed and cultivate your 'personal democracy'</p>
            <ul id="features">
              <li><span class="glyphicon glyphicon-ok"></span> Your account moves with you and informs you as a voter</li>
              <li><span class="glyphicon glyphicon-ok"></span> Get the latest fact-checked news and information</li>
              <li><span class="glyphicon glyphicon-ok"></span> See your elected officials and check their legislative history</li>
              <li><span class="glyphicon glyphicon-ok"></span> Find out about upcoming election dates and deadlines</li>
              <li><span class="glyphicon glyphicon-ok"></span> Quick access to local election office contact information</li>
            </ul>
      <!--<a href="<c:url value="/about-voter-account"/>" class="details">Learn More</a> -->
       <a class="button pull-right" href="<c:url value='/CreateAccount.htm'/>"> Create Voter Account</a>
		</div>
	</div>
</div>
