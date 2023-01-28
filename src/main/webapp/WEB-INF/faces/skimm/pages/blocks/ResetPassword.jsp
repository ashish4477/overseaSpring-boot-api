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
<div class="row">
	<div class="col-xs-12 col-sm-6 col-sm-offset-4">
		<div id="change-password" class="body-content page-form content column wide-content">
			<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
				<c:param name="title" value="Change Password" />
			</c:import>
	        <c:choose>
	            <c:when test="${not empty user}">
	                <h4>Enter a new password</h4>
	                 <c:import url="/WEB-INF/${relativePath}/pages/statics/UserPasswordForm.jsp" />
	            </c:when>
	            <c:otherwise>
	                <h4 class="alert alert-error">Your request cannot be completed at this time.</h4>
	                <p>The link for your request appears to have expired. Please <a href="<c:url value="/RemindPassword.htm"/>">try again.</a></p>
	            </c:otherwise>
	        </c:choose>
		</div>
	</div>
</div>
