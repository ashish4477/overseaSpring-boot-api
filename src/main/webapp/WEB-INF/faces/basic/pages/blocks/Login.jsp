<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jul 3, 2007
  Time: 7:05:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<div id="login-form" class="login-form large-overlay">
    <c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
        <c:param name="title" value="My Voter Account" />
        <c:param name="icon" value="mva" />
        <c:param name="showCloser" value="true" />
    </c:import>
    <div class="bd">
        <c:if test="${not empty param.login_error}">
            <div class="text-center alert alert-danger">
                <p class="error mb-0">Sorry, you entered incorrect login or password.</p>
            </div>
        </c:if>
        <h2 class="title">Sign in to My Voter Account:</h2>
        <c:import url="/WEB-INF/faces/basic/pages/statics/LoginForm.jsp" />
		<div id="create-account">
		<h3>Don't have a My Voter Account yet?</h3>
		<a href="<c:url value='/CreateAccount.htm'/>"> > Create a new My Voter Account Now</a>
		<ul id="features">
			<li>Quick write-in ballot access</li>
			<li>Easy form reprinting</li>
		</ul>
		<a href="<c:url value="/cms.htm?uri=/about-my-voter-account"/>" class="details">> More Details..</a>
		</div>
	</div>
    <div class="ft">
    </div>
</div>
