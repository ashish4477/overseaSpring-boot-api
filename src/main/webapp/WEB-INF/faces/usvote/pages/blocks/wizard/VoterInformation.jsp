<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%-- This has to be done this way in order to access the errors --%>

<div class="body-content page-form ${fn:toLowerCase(wizardContext.flowType)} content column wide-content">
	<div class="page-form">
	<c:choose>
		<c:when test="${empty userDetails and userDetails.id ne 0 }">
		  <div class="login-option pt-3">
		    Already have an account? <a href="<c:url value='/Login.htm'/>" title="View My Voter Account">Login Now</a>
		  </div>
		</c:when>
	</c:choose>

	<c:choose>
		<c:when test="${wizardContext.flowType == 'FWAB'}">
			<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
				<c:param name="title" value="Federal Write-in Absentee Ballot" />
				<c:param name="progressLabel" value="write-in ballot progress" />
			</c:import>
		</c:when>
		<c:when test="${wizardContext.flowType == 'RAVA'}">
			<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
				<c:param name="title" value="Register to Vote / Absentee Ballot Request" />
				<c:param name="label" value="<ul><li>test 123</li></ul>" />
				<c:param name="progressLabel" value="registration progress" />
			</c:import>
		</c:when>
		<c:when test="${wizardContext.flowType == 'DOMESTIC_REGISTRATION'}">
			<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
				<c:param name="title" value="Register to Vote" />
				<c:param name="progressLabel" value="registration progress" />
			</c:import>
		</c:when>
		<c:when test="${wizardContext.flowType == 'DOMESTIC_ABSENTEE'}">
			<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
				<c:param name="title" value="Absentee Ballot Request" />
				<c:param name="progressLabel" value="registration progress" />
			</c:import>
		</c:when>
		<c:otherwise>
			<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
				<c:param name="title" value=" " />
				<c:param name="progressLabel" value="registration progress" />
			</c:import>
		</c:otherwise>
	</c:choose>
		
	<div class="px-4">
	<br>
	<c:choose>
		<c:when test="${wizardContext.flowType == 'DOMESTIC_ABSENTEE'}">
			<ul>
			    <li><strong>You must be registered to vote before you submit your Absentee Ballot Request.</strong></li>
			    <li><strong>To check if you are a registered voter: </strong>go to <a href='https://www.usvotefoundation.org/vote/sviddomestic.htm'>State Voting Requirements</a> / "State Lookup Tools" for a link to your state's voter registration lookup tool.</li>
			</ul>
		</c:when>
		<c:when test="${wizardContext.flowType == 'DOMESTIC_REGISTRATION' || wizardContext.flowType == 'FWAB'}">
			<ul>
				<li>Enter your information using English-language / US-keyboard characters only. Non-English characters may be dropped.</li>
			</ul>
		</c:when>
		<c:otherwise>
			<ul>
			    <li>If you have already requested an absentee ballot but have not yet received it, you can still vote by using the <a href='/vote/FwabStart.htm'>Federal Write-In Absentee Ballot (FWAB).</a></li>
				<li>Enter your information using English-language / US-keyboard characters only. Non-English characters may be dropped.</li>
			</ul>
		</c:otherwise>
	</c:choose>
	<br>
	<br>
	</div>
		
	<div class="bd px-4" id="overseas-vote-foundation-short">
        <div class="bd-inner">
            <c:choose>
                <c:when test="${wizardContext.flowType == 'DOMESTIC_REGISTRATION'}">
                    <c:import url="/WEB-INF/${relativePath}/pages/statics/DomesticRegistrationMvaForm.jsp">
                        <c:param name="action"><c:url value="${formUrl}"/></c:param>
                        <c:param name="showPasswordFields" value="false"/>
                    </c:import>
                </c:when>
                <c:when test="${wizardContext.flowType == 'DOMESTIC_ABSENTEE'}">
                    <c:import url="/WEB-INF/${relativePath}/pages/statics/DomesticAbsenteeMvaForm.jsp">
                        <c:param name="action"><c:url value="${formUrl}"/></c:param>
                        <c:param name="showPasswordFields" value="false"/>
                    </c:import>
                </c:when>
                <c:otherwise>
                    <c:import url="/WEB-INF/${relativePath}/pages/statics/OverseasMvaForm.jsp">
                        <c:param name="action"><c:url value="${formUrl}"/></c:param>
                        <c:param name="showPasswordFields" value="false"/>
                    </c:import>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
	<div class="ft"><div class="ft-inner"></div></div>
	</div>
</div>