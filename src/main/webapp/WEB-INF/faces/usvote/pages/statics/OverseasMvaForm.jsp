<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="action" value=""/>
<c:if test="${not empty param.action}">
    <c:set var="action" value="${param.action}"/>
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

<form:form modelAttribute="${modelAttr}" action="${action}" method="post" name="${fname}" id="${fid}" class="userFields">
<div class="row">
<div class="col-xs-12">
  
<h3>Voter Information</h3>
<fieldset class="voterInfo">
  <div class="row form-group">
    <div class="voterType">
       <div class="col-xs-4"> <form:label	for="voterType" path="voterType" cssErrorClass="error" class="required label-control">Voter Type *</form:label>
       </div>
       <div class="col-xs-8">
           <form:select path="voterType" multiple="false" class="form-control">
               <form:option value="" label="- Select -"/>
               <form:option value="OVERSEAS_VOTER" label="Overseas Voter"/>
               <form:option value="MILITARY_VOTER" label="Military Voter"/>
           </form:select>
           <form:errors path="voterType" class="errorMsg"/>
        </div>
    </div>
  </div>
    <div class="row form-group">
        <div class="voterClassificationType">
            <div class="col-xs-4"> <form:label	for="voterClassificationType" path="voterClassificationType" cssErrorClass="error" class="required label-control">Voter Classification *</form:label>
            </div>
            <div class="col-xs-8">
                <form:select id="voterClassificationType" path="voterClassificationType" cssErrorClass="error form-control" class="form-control">
                    <form:option value="" label="- Select -"/>
                    <form:options itemValue="name" itemLabel="value"/>
                </form:select>
                <form:errors path="voterClassificationType" class="errorMsg" />
            </div>
        </div>
    </div>
  <div class="row form-group">
    <div class="voterHistory">
    <div class="col-xs-4"><form:label for="voterHistory" path="voterHistory" cssErrorClass="error" class="required label-control">Voting History <c:if test="${empty userValidationFieldsToSkip['voterHistory']}">*</c:if></form:label>
    </div>
    <div class="col-xs-8">
        <form:select path="voterHistory" cssErrorClass="error" class="form-control">
            <form:option value="" label="- Select -"/>
            <form:options itemValue="name" itemLabel="value"/>
        </form:select>
        <form:errors path="voterHistory" class="errorMsg" />
    </div>
  </div>
  </div>
  <div class="row form-group">
        <c:if test="${wizardContext.flowType == 'FWAB'}">
            <c:forEach items="${wizardContext.currentPage.questions}" var="question" varStatus="vs">
              <div class="fwab_yes_no_section">
                <c:if test="${not empty question.title}">
                    <h2>
                        ${question.title}
                        <c:if test="${fn:length(question.variants[0].fields) == 1 && question.variants[0].fields[0].required}">
                            <b>*</b>
                        </c:if>
                    </h2>
                </c:if>
          
              
                <c:set var="variant" value="${question.variants[0]}" />
                <div class="col-xs-4">
                    <c:forEach items="${variant.fields}" var="field">
                        <c:choose>
                            <c:when test="${field.type.templateName == 'no_input'}">
                                <label class="required label-control">${field.helpText}</label>
                            </c:when>
    
                            <c:when test="${field.type.templateName == 'checkbox'}">
                                <input type="hidden" name="fields" value="${field.id}" />
                                <input type="hidden" id="${field.id}" class="ucfwabyesno_hidden" name="${field.id}" value="${status.value.value}" />
                            </c:when>
                        </c:choose>
                    </c:forEach>
                </div>
                <div class="col-xs-8">
                    <select id="fwabyesno_select" name="fwabyesno_select" class="form-control">
                        <option value="">- Select -</option>
                        <c:forEach items="${variant.fields}" var="field">
                            <c:if test="${field.type.templateName == 'checkbox'}">
                                <c:choose>
                                    <c:when test="${field.id} == ${status.value.value}">
                                        <option value="${field.id}" selected>${field.title}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${field.id}">${field.title}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                    
                <script type="text/javascript">
                    YAHOO.util.Event.addListener("fwabyesno_select",'change', function() {
                        var id = this.value;
                        if (id != ""){
                            document.getElementById(id).value = 'true';
                            var hiddens = document.getElementsByClassName('ucfwabyesno_hidden');
                            Array.prototype.forEach.call(hiddens, function(el) {
                                // Do stuff here
                                if (el.getAttribute("id") != id){
                                    el.value = 'false';
                                }
                            });  
                        }
                    });
                </script>
                </div>
            </c:forEach>
        </c:if>
    </div>
</fieldset>
</div>
</div>

<c:import url="/WEB-INF/${relativePath}/pages/statics/MvaPersonalDetails.jsp" />

<div class="row">
  <div class="col-xs-12 col-sm-6">
      <hr class="header-above-border">
    <h3>Last U.S. Residence Address
    	<div class="rava-bubble"><strong>Uniformed Services:</strong> Use legal residence address.<br/> <br/>
        <strong>Overseas Citizens:</strong> Use the address of the last place you lived resided before departing moving outside of the U.S.
        <br/> <br/>
        This must be a physical street address. A Post Office (P.O.) Box cannot be used as a voting residence address.<br/> <br/>
        If you lived in an area with no street names, click the box that says, "Use Rural Route," include the route name and number and box number, and describe the residence location.<br/> <br/>
        If you don't recall the exact street address, click the box that says "Use Address Description," and describe the residence location.
		</div>
    	</h3>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaVotingAddress.jsp" />
  </div>
  <div class="col-xs-12 col-sm-6">
      <hr class="header-above-border">
    <h3>Current Address</h3>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaCurrentAddress.jsp" />
  </div>
</div>

<div class="row form-group">
  <c:if test="${not empty faceMailingList}">
  <div class="col-xs-12 col-md-6">
    <hr class="header-above-border">
    <h3>Voters Alert / Mailing List Opt-In</h3>
    <c:import url="/WEB-INF/faces/basic/pages/statics/MailOptInForm.jsp"/>
  </div>
  </c:if>
  
  <div class="col-xs-12 col-md-6">
    <hr class="header-above-border">
    <h3>Ballot Forwarding Address
    <div class="rava-bubble">If you need your ballots sent to an address different from your "Current Address," click the box that says, "Mail my ballot to a forwarding address," and insert the forwarding address information.</div>
    </h3>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaForwardingAddress.jsp" />
  </div>
</div>

  <div class="modal fade error-message">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">x</span></button>
                    <h4 class="modal-title text-center">Some of the information you entered is incomplete or inaccurate. <br/>Please review the error message(s) below.</h4>
                </div>
                <div class="modal-body text-center">
                    <a class="button close" data-dismiss="modal">Continue</a>
                </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<c:import url="/WEB-INF/${relativePath}/pages/statics/MvaFormFooter.jsp" />
</form:form>

<script>
  $(function() {
  <spring:bind path="${modelAttr}">
    <c:if test="${status.errors.errorCount > 0}">

    $('.error-message').modal('show');
    </c:if>
  </spring:bind>
  });
</script>