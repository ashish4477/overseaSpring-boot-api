<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value="/js/load-regions.js"/>" type="text/javascript"></script>
<c:url value="/ajax/getRegionsHTMLSelect.htm" var="getRegionsURL"/>
<script type="text/javascript" language="JavaScript">
    //<!--
    var params = { 'selectNameId': 'eodRegionId', 'regionInputId':'eodRegionId', 'stateInputId':'votingAddress.state', 'url':'${getRegionsURL}' };
    YAHOO.util.Event.addListener(window, "load", loadRegions, params);
    YAHOO.util.Event.addListener("votingAddress.state", "change", loadRegions, params);
    //-->
</script>

<c:if test="${not empty param.modelAttribute}">
    <c:set var="modelAttr" value="${param.modelAttribute}"/>
</c:if>

<c:choose>
    <c:when test="${not empty param.modelAttribute}">
        <c:set var="modelAttr" value="${param.modelAttribute}"/>
    </c:when>
    <c:otherwise>
        <c:set var="modelAttr" value="user"/>
    </c:otherwise>
</c:choose>


<div id="create-voter-alert" class="body-content column wide-content">
    <div class="page-form">
        <c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
            <c:param name="title" value="Sign me up for Voter Alerts"/>
        </c:import>
        <form:form id="voter-alert-form" modelAttribute="user" action="CreateVoterAlert.htm" method="post">
        <div class="row voter-alert">
            <div class="col-xs-12 col-sm-6 mx-auto">
                <fieldset class="nameInfo">
                    <div class="row">
                        <div class="radio col">
                            <label>
                                <form:radiobutton path="voterHistory" value="DOMESTIC_VOTER"/>
                                <span>Domestic voter</span>
                            </label>
                        </div>
                        <div class="radio col">
                            <label>
                                <form:radiobutton path="voterHistory" value="OVERSEAS_VOTER"/>
                                <span>Overseas voter</span>
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="firstName col">
                            <form:label for="name.firstName" path="name.firstName" cssErrorClass="error"
                                        class="required">First Name</form:label>
                            <form:input id="firstName" path="name.firstName" class="medium form-control"/>
                            <form:errors path="name.firstName" class="errorMsg"/>
                        </div>
                        <div class="firstName col">
                            <form:label for="name.lastName" path="name.lastName" cssErrorClass="error"
                                        class="required">Last Name</form:label>
                            <form:input id="lastName" path="name.lastName" class="medium form-control"/>
                            <form:errors path="name.lastName" class="errorMsg"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="firstName col">
                            <form:label for="name.initial" path="name.initial" cssErrorClass="error">Middle Name</form:label>
                            <form:input id="initial" path="name.initial" class="medium form-control"/>
                            <form:errors path="name.initial" class="errorMsg"/>
                        </div>
                        <div class="firstName col">
                            <form:label for="name.suffix" path="name.suffix" cssErrorClass="error">Suffix</form:label>
                            <form:input id="suffix" path="name.suffix" class="medium form-control"/>
                            <form:errors path="name.suffix" class="errorMsg"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="firstName col">
                            <form:label id="votingCity" for="votingAddress.city" path="votingAddress.city"
                                        cssErrorClass="error">U.S. City</form:label>
                            <form:input path="votingAddress.city" class="medium form-control"/>
                            <form:errors path="votingAddress.city" class="errorMsg"/>
                        </div>
                        <div class="firstName col">
                            <form:label for="votingAddress.county" path="votingAddress.county"
                                        cssErrorClass="error">U.S. County</form:label>
                            <form:input path="votingAddress.county" class="medium form-control"/>
                            <form:errors path="votingAddress.county" class="errorMsg"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="firstName col">
                            <form:label id="votingState" for="votingAddress.state" path="votingAddress.state"
                                        cssErrorClass="error">State/Province/Region</form:label>
                            <form:select path="votingAddress.state" multiple="false" class="form-control">
                                <form:option value="" label="- Select -"/>
                                <form:options items="${states}" itemValue="abbr" itemLabel="name"/>
                            </form:select>
                            <form:errors path="votingAddress.state" class="errorMsg"/>
                        </div>

                        <div class="firstName col">
                            <form:label for="votingAddress.zip" path="votingAddress.zip"
                                        cssErrorClass="error">Zip/Postal Code</form:label>
                            <form:input path="votingAddress.zip" class="medium form-control"/>
                            <form:errors path="votingAddress.zip" class="errorMsg"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="firstName col">
                            <label class="oneline">Voting Region *<span class="hint"> (Select state to see options)</span></label>
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
                                    <input type="hidden" id="votingRegionId" value="${selectedRegion.id}">
                                </c:if>
                            </div>
                            <form:errors path="eodRegionId" class="errorMsg" />
                        </div>
                        <div class="firstName col">
                            <form:label for="username" path="username"
                                        cssErrorClass="error">Email</form:label>
                            <form:input path="username" class="medium form-control"/>
                            <form:errors path="username" class="errorMsg"/>
                            <spring:bind path="username">
                                <c:if test="${fn:contains(status.errorCode, 'username.exists')}">
                                </c:if>
                            </spring:bind>
                        </div>
                    </div>
                    <input type="submit" value="Sign me up" class="submit-button"/>
                    </form:form>
                </fieldset>
            </div>
            <div class="ft">
                <div class="ft-inner"></div>
            </div>
        </div>

        <!-- <div class="row voter-alert">
            <div class="col-xs-12 col-sm-6 mx-auto voter-alert-signup-content">
                <h3>Sign-up for Voter Alerts</h3>
                <p>Stay up-to-date on elections in your state and on the national level. Get election reminders through the Voter Alert program from US Vote and Overseas Vote. You can be sure to never miss an election!</p>
                <p><strong>Please send me Voter Alerts for the following topics: </strong></p>

                <ul>
                    <li>State-specific Voter Alerts: includes election reminders and announcements for your state <br>(To change the state for which you receive alerts, please go to your Voter Account and update your “voting address” )</li>
                    <li>Nationwide Voter Alerts: relevant information and reminders that pertain to all US voters</li>
                    <li>US Vote News and Voter Services Information</li>
                    <li>Special Event Invitations</li>
                    <li>Survey Invitations</li>
                    <li>All of the above</li>
                </ul>
                
            </div>
        </div> -->

    </div>
</div>
