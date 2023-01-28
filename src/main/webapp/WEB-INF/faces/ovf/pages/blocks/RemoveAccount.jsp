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

<div id="remove-account" class="row">
	<div class="col-xs-12 col-sm-8 col-sm-offset-2">

		<c:choose>
			<c:when test="${empty user or user eq 'anonymousUser'}">
				<p>Log in to <strong><a href="<c:url value="/Login.htm"/>">My Voter Account</a></strong> to remove your account.</p>
				<p>If you want to change your email, please, <strong><a href="<c:url value="/UpdateAccount.htm"/>">Update Your Account</a></strong>. </p>
			</c:when>
			<c:otherwise>
				<div class="block-page-title-block">
					<h1 class="title">Are You Sure You Want to Remove Your Account?</h1>
				</div>
				<div class="px-4">
	        <p>You will no longer have access to features such as registration and ballot request form reprinting and quick write-in ballot access and key votes from your representatives.</p>
	         <h3 style=" margin:0 auto; color:#98001B">${user.username}</h3> <br/>
	          <p><b>Clicking the Remove Account button will permanently remove the account for this user.
	            <br/><span style="color:#98001B">This action can not be undone.</span></b></p>
	         <br/>
					<form action="<c:url value="/RemoveAccount.htm"/>" method="post">
						<input type="hidden" name="submission" value="true"/>
						<input type="submit"  name="delete" value="Remove Account" />
						<a class="button back" href="<c:url value="/Login.htm"/>" alt="back"> Back</a>
					</form>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>
