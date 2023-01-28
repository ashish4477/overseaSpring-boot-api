<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script src="<c:url value="/js/load-regions.js"/>" type="text/javascript"></script>
<script type="text/javascript" language="JavaScript">
	//<!--
	var params = {'regionInputId':'votingRegion', 'stateInputId':'votingAddress.state' };
	YAHOO.util.Event.addListener(window, "load", loadRegions, params);
	YAHOO.util.Event.addListener("votingAddress.state", "change", loadRegions, params);
	//-->
</script>

<c:set var="faction" value=""/>
<c:if test="${not empty param.action}">
	<c:set var="faction" value="${param.action}"/>
</c:if>
<c:set var="fname" value="userForm"/>
<c:if test="${not empty param.name}">
	<c:set var="fname" value="${param.name}"/>
</c:if>
<c:set var="fid" value="userForm"/>
<c:if test="${not empty param.id}">
	<c:set var="fid" value="${param.id}"/>
</c:if>
<c:set var="modelAttr" value="user"/>
<c:if test="${not empty param.modelAttribute}">
	<c:set var="modelAttr" value="${param.modelAttribute}"/>
</c:if>
<c:url value="/Login.htm" var="fbackUrl"/>
<c:if test="${not empty param.backUrl}">
	<c:set var="fbackUrl" value="${param.backUrl}"/>
</c:if>

<form:form modelAttribute="${modelAttr}" action="${faction}" method="post" name="${fname}" id="${fid}" role="form" autocomplete="off">
<div class="row">
  <div class="col-xs-12">
    <%-- Display non-password related errors --%>
    <c:if test="${showErrorMsg}">
        <div id="update-message" class="warning">
            <p>The Voter Account application may have changed since your account was created.</p>
            <p>Please, update your Voter Account just after setting new password.</p>
            <%--<p><a href="<c:url value="/UpdateAccount.htm"/>">Click here to update your account information</a></p>--%>
        </div>
    </c:if>

	<div class="form-group">
    <form:label for="password" path="password" class="label" cssErrorClass="error">Password <small>(At least 6 characters long)</small></form:label>
    <form:password path="password" class="form-control"/>
    <form:errors path="password" class="errorMsg"/>
	</div>
	<div class="form-group">
			<form:label	for="confirmPassword" path="confirmPassword" class="label" cssErrorClass="error">Confirm Password</form:label>
			<form:password path="confirmPassword" class="form-control" />
			<form:errors path="confirmPassword" class="errorMsg"/>
	</div>
	<input type="submit" name="create" value="Continue" class="button"/>
</form:form>
</div>
</div>