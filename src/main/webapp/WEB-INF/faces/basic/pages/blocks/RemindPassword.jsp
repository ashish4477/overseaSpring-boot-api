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
    <c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
        <c:param name="title" value="Password Reminder" />
        <c:param name="icon" value="mva" />
        <c:param name="showCloser" value="true"/>
    </c:import>
	<div class="bd">
		<div class="bd-inner">
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
						</c:if>
						<label for="${status.expression}">Email: <input type="email" class="field" name="${status.expression}" value="" /></label>
					</spring:bind>
				</fieldset>
				<input type="image" src="<c:url value="/img/buttons/continue-button.gif"/>" name="remind" value="Continue" id="login-button" />
			</form>
			&nbsp;&nbsp;<a href="Login.htm"><p>Cancel and return to My Voter Account main page</p></a>
		</div>
	</div>
	<div class="ft"></div>
</div>