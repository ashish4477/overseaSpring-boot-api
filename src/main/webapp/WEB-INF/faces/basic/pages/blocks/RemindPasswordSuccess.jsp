<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Oct 19, 2007
  Time: 10:24:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="login-form" class="login-form large-overlay">
    <c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
        <c:param name="title" value="Password Reset" />
        <c:param name="icon" value="mva" />
        <c:param name="showCloser" value="true"/>
    </c:import>
	<div class="bd">
		<p>Your request has been successfully processed.</p>
      <p>Instructions on how to reset your password have been sent to your e-mail address.</p>
	</div>
	<div class="ft">
	</div>
</div>