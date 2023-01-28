<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="create-account" class="body-content column wide-content">
<div class="page-form">
	<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
		<c:param name="title" value="Create Your Voter Account" />
	</c:import>

	<div class="bd">
			<div class="bd-inner">
			<p>Please enter your information to create an account</p>
			<c:import url="/WEB-INF/faces/basic/pages/statics/OverseasMvaForm.jsp" />
			<p>&nbsp;</p>
			<p>Note: The ExxonMobil Mobil Citizen Action Team does not store any of the voter identification information, such as your social security number or birth date, collected during this registration process.</p>
		</div>
	</div>
	<div class="ft"><div class="ft-inner"></div></div>
</div>
</div>
