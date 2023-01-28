<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<script src="<c:url value="/js/load-regions.js"/>" type="text/javascript"></script>
<c:url value="/ajax/getRegionsHTMLSelect.htm" var="getRegionsURL"/>
<script type="text/javascript" language="JavaScript">
    //<!--
    var params = { 'selectNameId': 'eodRegionId', 'regionInputId':'eodRegionId', 'stateInputId':'votingAddress.state', 'url':'${getRegionsURL}' };
    YAHOO.util.Event.addListener(window, "load", loadRegions, params);
    YAHOO.util.Event.addListener("votingAddress.state", "change", loadRegions, params);
    //-->
</script>

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
<c:set var="showPasswordFields" value="true"/>
<c:if test="${not empty param.showPasswordFields}">
    <c:set var="showPasswordFields" value="${param.showPasswordFields}"/>
</c:if>

  
<form:form modelAttribute="${modelAttr}" action="${action}" method="post" name="${fname}" id="${fid}" class="domestic userFields">

<c:import url="/WEB-INF/${relativePath}/pages/statics/MvaPersonalDetails.jsp" />

<div class="row form-group">
  <div class="col-xs-12 col-md-6 ">
    <hr class="header-above-border">
    <h3>Home Address</span><div class="rava-bubble">Where you are registering to vote<br/> Your legal address used for voting purposes</div></h3>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaVotingAddress.jsp" />
  </div>
    <div class="col-xs-12 col-md-6">
        <hr class="header-above-border">
    <div class="legend">
    <h3><span>Previous Address</span>  <div class="rava-bubble">If you were registered to vote in a different location</div></h3>
    </div>
        <fieldset class="previousAddress" style="min-height: 603px;">
                
                (Use only if you were registered to vote before and wish to change or cancel your prior registration)</small>
                <form:errors path="previousAddress" class="errorMsg" />
                <!-- <input type="checkbox" id="current-same-as-voting" /> Same as Voting Address -->

                <div id="stdAddress">
                    <div class="previousAddress1 form-group">
                        <form:label	for="previousAddress.street1" path="previousAddress.street1" cssErrorClass="error" class="label-control">Address 1</form:label>
                        <form:input path="previousAddress.street1" cssErrorClass="error" class="form-control"/>
                        <form:errors path="previousAddress.street1" class="errorMsg" />
                    </div>
                    <div class="previousAddress2 form-group">
                        <form:label	for="previousAddress.street2" path="previousAddress.street2" cssErrorClass="error" class="label-control">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
                        <form:input path="previousAddress.street2" cssErrorClass="error" class="form-control" />
                        <form:errors path="previousAddress.street2" class="errorMsg" />
                    </div>
                    <div class="city form-group">
                        <form:label	id="currentCity" for="previousAddress.city" path="previousAddress.city" cssErrorClass="error" class="label-control">City/Town</form:label>
                        <form:input path="previousAddress.city" cssErrorClass="error" class="form-control" />
                        <form:errors path="previousAddress.city" class="errorMsg" />
                    </div>
                    <div class="county form-group">
                    <form:label	for="previousAddress.county" path="previousAddress.county" cssErrorClass="error" class="label-control">County</form:label>
                    <form:input path="previousAddress.county" cssErrorClass="error" class="large form-control"/>
                    <form:errors path="previousAddress.county" class="errorMsg" />
                	</div>
                    <div class="state form-group">
                    <form:label	id="previousState" for="previousAddress.state" path="previousAddress.state" cssErrorClass="error" class="label-control">State</form:label>
                     <form:select path="previousAddress.state" cssErrorClass="error" multiple="false" class="form-control">
                        <form:option value="" label="- Select -"/>
                        <form:options items="${states}" itemValue="abbr" itemLabel="name"/>
                    </form:select>
                    <form:errors path="previousAddress.state" class="errorMsg" />
                    </div>
                    <div class="zip form-group">
                        <form:label	for="previousAddress.zip" path="previousAddress.zip" cssErrorClass="error" class="label-control">Zip/Postal Code</form:label>
                        <form:input path="previousAddress.zip" cssErrorClass="error" class="form-control"/>
                        <form:errors path="previousAddress.zip" class="errorMsg" />
                    </div>
                </div>
        </fieldset>
</div>
</div>
<div class="row">
  <c:if test="${not empty faceMailingList}">
  <div class="col-xs-12 col-md-6 <c:if test="${wizardContext.flowType eq 'FWAB'}">offset-sm-6</c:if>">
    <hr class="header-above-border">
    <h3>Voters Alert / Mailing List Opt-In</h3>
    <c:import url="/WEB-INF/faces/basic/pages/statics/MailOptInForm.jsp"/>
  </div>
  </c:if>
  <c:if test="${wizardContext.flowType ne 'FWAB'}">
  <div class="col-xs-12 col-md-6">
    <hr class="header-above-border">
    <h3>Ballot Forwarding Address
    <div class="rava-bubble">If you need your ballots sent to an address different from your "Current Address," click the box that says, "Mail my ballot to a forwarding address," and insert the forwarding address information.</div>
    </h3>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaForwardingAddress.jsp" />
  </div>
  </c:if>
</div>
<input type="submit" src="<c:url value="/img/buttons/continue-button.gif"/>" name="create" value="Continue" class="submit-button pull-right" />
<c:if test="${param.showBackButton}">
    <a class="back-button pull-right" href="<c:url value="/Login.htm"/>"><img src="<c:url value="/img/buttons/back-button.gif"/>" alt="back"></a>
</c:if>
</form:form>

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

<script>
    $(function() {
    <spring:bind path="${modelAttr}">
      <c:if test="${status.errors.errorCount > 0}">

        $('.error-message').modal('show');
      </c:if>
    </spring:bind>
    });
</script>
