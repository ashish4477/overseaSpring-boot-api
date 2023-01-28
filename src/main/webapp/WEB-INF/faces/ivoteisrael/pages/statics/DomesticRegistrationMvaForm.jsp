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
<c:set var="showPasswordFields" value="true"/>
<c:if test="${not empty param.showPasswordFields}">
    <c:set var="showPasswordFields" value="${param.showPasswordFields}"/>
</c:if>

  
<form:form modelAttribute="${modelAttr}" action="${faction}" method="post" name="${fname}" id="${fid}" class="domestic userFields">

<c:import url="/WEB-INF/${relativePath}/pages/statics/MvaPersonalDetails.jsp" />

<div class="row">
  <div class="col-xs-12 col-sm-6 ">
    <h3>Home Address</span><div class="rava-bubble">Where you are registering to vote<br/> Your legal address used for voting purposes</div></h3>
    <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaVotingAddress.jsp" />
  </div>
    <div class="col-xs-12 col-sm-6">
    <div class="legend">
    <h3><span>Previous Address</span>  <div class="rava-bubble">If you were registered to vote in a different location</div></h3>
      <small>(Use only if you were registered to vote before and wish to change or cancel your prior registration)</small>
    <br/><br/>
    </div>
        <fieldset class="previousAddress">
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

   <div class="legend">
    <h3><span>Forwarding Address</span></h3>
    <small>(Use if you use a different address to receive mail)</small><br/><br/>
    </div>
  <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaForwardingAddress.jsp" />
</div>
<input type="submit" src="<c:url value="/img/buttons/continue-button.gif"/>" name="create" value="Continue" class="submit-button pull-right" />
<c:if test="${param.showBackButton}">
    <a class="back-button pull-right" href="<c:url value="/Login.htm"/>"><img src="<c:url value="/img/buttons/back-button.gif"/>" alt="back"></a>
</c:if>
</form:form>

