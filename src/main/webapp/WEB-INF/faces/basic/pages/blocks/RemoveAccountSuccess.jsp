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

<div id="removed" class="page-form large-overlay">

    <c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
        <c:param name="title" value="Voter Account Removed" />
        <c:param name="showCloser" value="true"/>
    </c:import>

    <div class="bd">
		<div class="bd-inner">
        <h2>Your voter account has been deleted.</h2>
        <p>All personal information has been removed from the system.</p>
		</div>
    </div>
	<div class="ft"><div class="ft-inner"></div></div>
</div>
