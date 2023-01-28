<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 9, 2007
	Time: 6:29:31 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="body-content page-form content column wide-content rava">

	<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
		<c:param name="title" value="Update Your Voter Account" />
	</c:import>

	<div class="bd px-4" id="overseas-vote-foundation-short">
		<div class="bd-inner">
		<p>Update your account information below</p>
		<c:import url="/WEB-INF/faces/basic/pages/statics/OverseasMvaForm.jsp">
			<c:param name="showPasswordFields" value="false"/>
            <c:param name="showBackButton" value="true"/>
		</c:import>
		</div>
	</div>
	<div class="ft"><div class="ft-inner"></div></div>
</div>
