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

<div id="remove-account" class="large-overlay page-form change-password">

    <c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
        <c:param name="title" value="Remove Voter Account" />
        <c:param name="icon" value="mva" />
        <c:param name="showCloser" value="true"/>
    </c:import>

	<div class="bd">
		<div class="bd-inner">

		<c:choose>
			<c:when test="${empty user or user eq 'anonymousUser'}">
				<p>Log in to <strong><a href="<c:url value="/Login.htm"/>">My Voter Account</a></strong> to remove your account.</p>
				<p>If you want to change your email, please, <strong><a href="<c:url value="/UpdateAccount.htm"/>">Update Your Account</a></strong>. </p>
			</c:when>
			<c:otherwise>
				<h4>Are You Sure You Want to Remove Your Account?</h4>
                <p>You will no longer have access to features such as registration and ballot request form reprinting and quick write-in ballot access.</p>
                <h4>To permanently remove the account for the user:</h4> <p class="accountName"><b><b>${user.username}</b></b></p>
                <p>Click the Delete My Account button at the bottom of this screen. <b>This action can not be undone.</b></p>
                <br/>
				<form action="<c:url value="/RemoveAccount.htm"/>" method="post">
					<input type="hidden" name="submission" value="true"/>
					<input type="image" src="<c:url value="/img/buttons/delete-account.gif"/>" name="delete" value="Continue" class="submit-button delete-account" />
					<a class="back-button" href="<c:url value="/Login.htm"/>"><img src="<c:url value="/img/buttons/back-button.gif"/>" alt="back"></a>
				</form>
			</c:otherwise>
		</c:choose>
	</div>
	</div>
	<div class="ft"><div class="ft-inner"></div></div>
</div>
