<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 19, 2007
	Time: 9:44:16 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="login-form" class="login-form large-overlay">
	<div class="row">
		<div class="col-xs-12 col-sm-6 col-sm-offset-3">
      <h2>Password Reminder</h2>
			<p id="note">Please enter the email account you signed-up with. Instructions will be sent on how to reset your password.</p>
			<form action="<c:url value="/RemindPassword.htm" />" method="post">
				<input type="hidden" name="submission" value="true"/>
				<fieldset id="credentials">
					<spring:bind path="userForm">
						<c:if test="${status.error}">
							<p class="error">${status.errorMessage}</p>
						</c:if>
					</spring:bind>
					<spring:bind path="userForm.username">
						<c:set var="error" value="" />
						<c:if test="${status.error}">
							<p class="error">${status.errorMessage}</p>
							<c:set var="error" value="error-indicator" />
						</c:if> <div class="form-group">
						<label for="${status.expression}">Email:</label>
            <input type="email" class="field form-control" name="${status.expression}" value="" /></div>
					</spring:bind>
				</fieldset>
				<input type="submit" name="remind" value="Continue" id="login-button" />
			</form>
			&nbsp;&nbsp;<a href="Login.htm"><p>Cancel and return to My Voter Account main page</p></a>
		</div>
	</div>
</div>