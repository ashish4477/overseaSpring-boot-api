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
<%@ page import="com.bearcode.ovf.actions.mva.FacebookIntegration" %>

<div id="facebook-login-error" class="body-content page-form content column wide-content">

	<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
		<c:param name="title" value="Facebook Authentication Error" />
	</c:import>

	<div class="bd">
		<div class="bd-inner">
			<p>There was a problem with the Facebook authetication process</p>
			<c:if test="${errorCode == FacebookIntegration.AUTH_ERROR_NEED_FB_INTEGRATION}">
				<p>
					There is an account with username <c:out value="${email}"/> that is not set to allow Facebook login.
					Please <a href="<c:url value="/Login.htm?username=${email}"/>">login</a> and update the account to allow Facebook login.  
				</p>

			</c:if>
			<c:if test="${not empty errorReason}">
				<p>Reason: <c:out value="${errorReason}" /></p>
			</c:if>
			<c:if test="${not empty errorDesc}">
				<p>Description: <c:out value="${errorDesc}" /></p>
			</c:if>

		</div>
	</div>
	<div class="ft"><div class="ft-inner"></div></div>
</div>
