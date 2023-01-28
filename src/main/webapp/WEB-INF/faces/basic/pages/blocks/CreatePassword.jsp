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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="create-password" class="body-content page-form content column wide-content">

	<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
		<c:param name="title" value="Register to Vote / Absentee Ballot Request" />
	</c:import>
	
	<c:set var="curPageNum" value="${wizardContext.currentPage.number}"/>
    <c:url var="skipUrl" value="/w/${fn:toLowerCase(wizardContext.flowType)}/${curPageNum + 1}.htm"/>
    <c:url var="backUrl" value="/w/${fn:toLowerCase(wizardContext.flowType)}/${curPageNum}.htm"/>

	<div class="bd">
		<div class="bd-inner">
     <c:choose>
        <c:when test="${not empty existed}">
            <h2>An account using this email already exists</h2>
            <p>Please sign in to your Voter Account or correct your entry.</p>
            <br/>
            <a href="<c:url value="/VoterInformation.htm"/>" class="wizard button back"><img alt="continue" src="<c:url value="/img/buttons/back-button.gif"/>"></a>
            <a class="button" href="<c:url value="/Login.htm"/>"><img alt="continue" src="<c:url value="/img/buttons/login-button.gif"/>"/></a>
          <p style="padding-top:22px; padding-bottom:41px; line-height:19px;">Creating a Voter Account is not mandatory, however it is recommended if you need to make changes or reprint your form. <a style="text-decoration: underline;" href="${skipUrl}">Continue Without Creating an Account</a></p>

        </c:when>
     <c:otherwise>
			<h2>Would you like to save your profile in a voter account?</h2>
			<p>Voter Accounts allow you to save, maintain and re-use profile information used to generate or re-print your ballot request or access a write-in ballot - whenever you need with just a few clicks, year after year.</p>
      <br/>
            <div class="column left">
                        <h4>Enter a Password to Create An Account</h4>
                        <c:import url="/WEB-INF/faces/basic/pages/statics/UserPasswordForm.jsp">
                            <c:param name="action"><c:url value="/CreateAccount.htm"/></c:param>
                            <c:param name="backUrl" value="${backUrl}"/>
                        </c:import>

			</div>
			<div class="column right">
				<h4>Continue Without An Account</h4>
				<p style="padding-top:22px; padding-bottom:41px; line-height:19px;">Creating a Voter Account is not mandatory, however it is recommended if you need to make changes or reprint your form. <br/><a href="/about-my-voter-account" style="text-decoration:underline;" class="hint">View Voter Account Benefits</a></p>
					<a href="${skipUrl}"><img style="margin-right:8px;" src="<c:url value="/img/buttons/continue-button-no-account.gif"/>" alt="continue without creating an account"></a>
			</div>
			</div>
           </c:otherwise>
                </c:choose>
		</div>
	<div class="break"></div>
	<div class="ft"><div class="ft-inner"></div></div>
	</div>
