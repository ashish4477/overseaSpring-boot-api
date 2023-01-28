<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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

<c:import url="/WEB-INF/faces/basic/pages/statics/MvaFormHeader.jsp" />

<form:form modelAttribute="${modelAttr}" action="${faction}" method="post" name="${fname}" id="${fid}" class="userFields">
<input type="hidden" name="voterType" value="UNSPECIFIED" />
<input type="hidden" name="phoneType" value="Other" />
<div class="legend"><span>Voter Information</span></div>
<fieldset class="voterInfo">
    <div class="voterType">
        <form:label	for="voterClassificationType" path="voterClassificationType" cssErrorClass="error" class="required">Voter Type *</form:label>
        <form:select id="voterClassificationType" path="voterClassificationType" cssErrorClass="error">
            <form:option value="" label="- Select -"/>
            <form:options itemValue="name" itemLabel="value"/>
        </form:select>
        <form:errors path="voterClassificationType" class="errorMsg" />
    </div>
    <div class="voterHistory">
        <form:label for="voterHistory" path="voterHistory" cssErrorClass="error" class="required">Voting History <c:if test="${empty userValidationFieldsToSkip['voterHistory']}">*</c:if></form:label>
        <form:select path="voterHistory" cssErrorClass="error">
            <form:option value="" label="- Select -"/>
            <form:options itemValue="name" itemLabel="value"/>
        </form:select>
        <form:errors path="voterHistory" class="errorMsg" />
    </div>
</fieldset>
<c:import url="/WEB-INF/faces/basic/pages/statics/MvaPersonalDetails.jsp" />
<div class="section two">
    <div class="column left">
    	<c:import url="/WEB-INF/faces/basic/pages/statics/MvaVotingAddress.jsp" />
    </div>
    <div class="column right">
    	<c:import url="/WEB-INF/faces/basic/pages/statics/MvaCurrentAddress.jsp" />
    </div>
</div>

   <c:if test="${wizardContext.flowType ne 'FWAB'}">
      <div class="section three">
          <c:if test="${not empty faceMailingList}">
            <div class="column left">
              <div class="legend"><span>Voters Alert / Mailing List Opt-In</span></div>
              <c:import url="/WEB-INF/faces/basic/pages/statics/MailOptInForm.jsp"/>
            </div>
          </c:if>
          <div class="column right">
              <c:import url="/WEB-INF/faces/basic/pages/statics/MvaForwardingAddress.jsp" />
          </div>
      </div>
  </c:if>

<c:import url="/WEB-INF/faces/basic/pages/statics/MvaFormFooter.jsp" />
</form:form>

  <div class="modal fade error-message">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">x</span></button>
                    <h4 class="modal-title text-center">Some of the information you entered is incomplete or inaccurate. <br/>Please review the error message(s) below.</h4>
                </div>
                <div class="modal-body text-center">
                    <a class="button" data-dismiss="modal">Continue</a>
                </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>
  $(function() {
  <spring:bind path="${modelAttr}">
    <c:if test="${status.errors.errorCount > 0}">

    $('.error-message').modal('show');
    </c:if>
  </spring:bind>
  });
</script>
