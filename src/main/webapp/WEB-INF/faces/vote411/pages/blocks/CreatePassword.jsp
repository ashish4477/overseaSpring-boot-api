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

<div id="create-password" class="row">
 <div class="col-12 col-sm-10 offset-sm-1">
	
	<c:set var="curPageNum" value="${wizardContext.currentPage.number}"/>
  <c:url var="skipUrl" value="/w/${fn:toLowerCase(wizardContext.flowType)}/${curPageNum + 1}.htm"/>
  <c:url var="backUrl" value="/w/${fn:toLowerCase(wizardContext.flowType)}/${curPageNum}.htm"/>
  <c:choose>
    <c:when test="${not empty existed}">
      <h2>An account using this email already exists</h2>
      <p>Please sign in to your Voter Account or correct your entry.</p>
      <br/>
      <a href="<c:url value="/VoterInformation.htm"/>" class="wizard button back">Back</a>
         <a class="button" href="<c:url value="/Login.htm"/>">Voter Account Sign-in</a>
         <div id="forgot-password" style="margin-left:125px; margin-top:-10px;"><a href="<c:url value="/RemindPassword.htm"/>">Forgot Password?</a></div>
         <br/>
    </c:when>
    <c:otherwise>
			<h2>Would you like to save your profile in a voter account?</h2>
			<p>Voter Accounts allow you to  maintain and re-use profile information used to generate or re-print your request or access a write-in ballot - whenever you need with just a few clicks, year after year.</p>

      <div class="row">
        <div class="col-12 col-sm-10 offset-sm-1">
          <h4>Enter a Password to Create An Account</h4>
          <c:import url="/WEB-INF/${relativePath}/pages/statics/UserPasswordForm.jsp">
              <c:param name="action"><c:url value="/CreateAccount.htm"/></c:param>
              <c:param name="backUrl" value="${backUrl}"/>
           </c:import>
        </div>
      </div>
         </c:otherwise>
   </c:choose>
       <div class="row">
          <div class="col-12">
            <em>Creating a Voter Account is not mandatory, however it is recommended if you need to make changes or reprint your form. <br/><a style="text-decoration: underline;" href="${skipUrl}">Continue Without Creating an Account</a></em>
          </div>
       </div>
  </div>
</div>
