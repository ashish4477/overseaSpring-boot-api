<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 9, 2007
	Time: 6:29:31 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="change-password" class="row">
  <div class="col-xs-12 col-sm-6">

		<h2>Reset Password</h2>
		<h4>Enter a new password</h4>
		<c:import url="/WEB-INF/${relativePath}/pages/statics/UserPasswordForm.jsp" />
  </div>
</div>