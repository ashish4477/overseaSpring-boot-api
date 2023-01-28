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

<div id="change-password" class="body-content page-form content column wide-content">

	<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
		<c:param name="title" value="Change Password" />
	</c:import>

	<div class="bd">
		<div class="bd-inner">
		<div style="margin:0 auto;width:300px;">
		<h4>Enter a new password</h4>
		<c:import url="/WEB-INF/faces/basic/pages/statics/UserPasswordForm.jsp" />
		</div>
		</div>
	</div>
	<div class="ft"><div class="ft-inner"></div></div>
</div>
