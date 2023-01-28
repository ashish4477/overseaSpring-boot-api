<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%--
This is used in multiple places
--%>

<form action="<c:url value="/user_login"/>" method="post">
	<fieldset id="credentials">
		<label for="j_username">Email: <input type="email" id="j_username" name="j_username" value="<c:out value="${param.j_username}" />"/></label>
		<label for="j_password">Password: <input type="password" id="j_password" name="j_password" /></label>
	</fieldset>
	<fieldset id="remember-me">
		<label for="_spring_security_remember_me"><input type="checkbox" id="_spring_security_remember_me" name="_spring_security_remember_me" class="field" /> Remember Me</label>
	</fieldset>
	<div id="forgot-password"><a href="<c:url value="/RemindPassword.htm"/>">Forgot Password?</a></div>
	<input type="hidden" name="spring-security-redirect" value="<c:out value="${param.redirect}" />" />
	<input type="image" src="<c:url value="/img/buttons/login-button.gif"/>" id="login-button" />
</form>
