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


<script> top.location.href='${facebookOAuthUrl}';</script>


<div id="change-password" class="body-content page-form content column wide-content">

	<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
		<c:param name="title" value="Facebook Authentication Problem" />
	</c:import>

	<div class="bd">
		<div class="bd-inner">
		<p>There was a problem with the Facebook authentication process</p>
		<p>It appears that either you have Javascript disabled or the Facebook authorization server is not responding.</p>
		</div>
	</div>
	<div class="ft"><div class="ft-inner"></div></div>
</div>
