<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
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

<form:form modelAttribute="${modelAttr}" action="${faction}" method="post" name="${fname}" id="${fid}"
           class="domestic userFields">
    <div>
        <div class="col-xs-12 col-sm-6 mx-auto">
            <fieldset class="nameInfo">
                <div class="row">
                    <div class="col">
                        <form:label for="voterType" path="voterType" cssErrorClass="error" class="required">Voter Type <small>(Required)</small></form:label>
                        <form:select path="voterType" multiple="false" class="form-control">
                            <form:option value="" label="- Select -"/>
                            <form:option value="DOMESTIC_VOTER" label="US-based Domestic Voter"/>
                            <form:option value="OVERSEAS_VOTER" label="Overseas Voter"/>
                            <form:option value="MILITARY_VOTER" label="Military Voter"/>
                        </form:select>
                        <form:errors path="voterType" class="errorMsg"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <form:label for="name.firstName" path="name.firstName" cssErrorClass="error" class="required">First Name <small>(Required)</small></form:label>
                        <form:input id="firstName" path="name.firstName" class="medium form-control"/>
                        <form:errors path="name.firstName" class="errorMsg"/>
                    </div>
                    <div class="col">
                        <form:label for="name.lastName" path="name.lastName" cssErrorClass="error" class="required">Last Name <small>(Required)</small></form:label>
                        <form:input id="lastName" path="name.lastName" class="medium form-control"/>
                        <form:errors path="name.lastName" class="errorMsg"/>
                    </div>
                </div>
                <div class="row fields-block">
                    <div class="col">
                        <form:label for="name.initial" path="name.initial" cssErrorClass="error">Middle Name</form:label>
                        <form:input id="initial" path="name.initial" class="medium form-control"/>
                        <form:errors path="name.initial" class="errorMsg"/>
                    </div>
                    <div class="col">
                        <form:label	for="name.suffix" path="name.suffix" cssErrorClass="error">Suffix</form:label>
                        <form:select path="name.suffix" class="medium form-control">
                            <form:option value="" label="- Select -"/>
                            <form:option value="Jr" label="Jr"/>
                            <form:option value="Sr" label="Sr"/>
                            <form:option value="II" label="II"/>
                            <form:option value="III" label="III"/>
                            <form:option value="IV" label="IV"/>
                        </form:select>
                        <form:errors path="name.suffix" class="errorMsg" />
                    </div>
                </div>
                <div class="row">
                    <div class="fields-block-label">Please identify where you vote:</div>
                    <div class="col">
                        <form:label id="votingCity" for="votingAddress.city" path="votingAddress.city" cssErrorClass="error">U.S. City <small>(Required)</small></form:label>
                        <form:input path="votingAddress.city" class="medium form-control"/>
                        <form:errors path="votingAddress.city" class="errorMsg"/>
                    </div>
                    <div class="col">
                        <form:label for="votingAddress.county" path="votingAddress.county" cssErrorClass="error">U.S. County</form:label>
                        <form:input path="votingAddress.county" class="medium form-control"/>
                        <form:errors path="votingAddress.county" class="errorMsg"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <form:label id="votingState" for="votingAddress.state" path="votingAddress.state" cssErrorClass="error">U.S. State <small>(Required)</small></form:label>
                        <form:select path="votingAddress.state" multiple="false" class="form-control">
                            <form:option value="" label="- Select -"/>
                            <form:options items="${states}" itemValue="abbr" itemLabel="name"/>
                        </form:select>
                        <form:errors path="votingAddress.state" class="errorMsg"/>
                    </div>

                    <div class="col">
                        <form:label for="votingAddress.zip" path="votingAddress.zip" cssErrorClass="error">Zip Code</form:label>
                        <form:input path="votingAddress.zip" class="medium form-control"/>
                        <form:errors path="votingAddress.zip" class="errorMsg"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col fields-block">
                        <label class="online">Voting Jurisdiction <small> (Required, Select state to see options)</small></label>
                        <div id="ajax_region_select">
                            <form:select cssErrorClass="error" class="form-control" path="eodRegionId">
                                <c:if test="${not empty selectedRegion}">
                                    <option value="${selectedRegion.id}" selected="selected">${selectedRegion.name}</option>
                                </c:if>
                                <c:if test="${empty selectedRegion}">
                                    <option value="0">- Select -</option>
                                </c:if>
                            </form:select>
                            <c:if test="${not empty selectedRegion}">
                                <input type="hidden" id="eodRegionId" value="${selectedRegion.id}">
                            </c:if>
                        </div>
                        <form:errors path="eodRegionId" class="errorMsg" />
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <form:label for="username" path="username" cssErrorClass="error">Email <small>(Required)</small></form:label>
                        <form:input path="username" class="medium form-control"/>
                        <form:errors path="username" class="errorMsg"/>
                        <spring:bind path="username">
                            <c:if test="${fn:contains(status.errorCode, 'username.exists')}">
                            </c:if>
                        </spring:bind>
                    </div>
                </div>
                <c:if test="${showPasswordFields eq 'true'}">
                <div class="row">
                    <div class="col">

                            <form:label for="password" path="password" cssErrorClass="error" class="control-label">Password <small>(Required, At least six characters long)</small></form:label>
                            <form:input type="password" path="password" class="form-control" />
                            <form:errors path="password" class="errorMsg" />
                    </div>
                </div>
                <div class="row fields-block">
                    <div class="col">
                            <form:label for="confirmPassword" path="confirmPassword" class="confirmPassword">Confirm Password <small>(Required)</small></form:label>
                            <form:input type="password" path="confirmPassword" class="form-control" />
                            <form:errors path="confirmPassword" class="errorMsg" />
                    </div>
                </div>
                </c:if>
                <div class="row">
                    <div class="voter-alert-checkbox">
                        <form:checkbox path="optIn" cssClass="checkbox" id="mailingListOptIn"/> <label for="mailingListOptIn">Sign Me Up for U.S. Vote Foundation Voter Alerts</label>
                    </div>
                </div>
                <input type="submit" value="Create my Voter Account" class="submit-button"/>
            </fieldset>
        </div>
    </div>
</form:form>

