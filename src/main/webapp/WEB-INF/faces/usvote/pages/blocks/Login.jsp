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
  <div class="col-xs-12 col-sm-8">
    <div class="block-page-title-block">
      <h1 class="title">Voter Account Login</h1>
    </div>

    <c:if test="${not empty param.login_error}">
      <div class="text-center alert alert-danger">
        <p class="error mb-0">Sorry, you entered incorrect login or password.</p>
      </div>
    </c:if>

    <c:import url="/WEB-INF/${relativePath}/pages/statics/LoginForm.jsp" />

    <div class="already-login-section">
      <h2>Already Logged In</h2>
      <a href="/vote/MyVotingInformation.htm" class="return-btn-dashboard">Return To My Dashboard</a>
    </div>

  </div>
</div>
