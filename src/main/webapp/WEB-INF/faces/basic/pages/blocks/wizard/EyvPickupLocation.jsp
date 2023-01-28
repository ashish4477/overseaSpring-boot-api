<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 25, 2008
  Time: 4:46:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/ovf-fedex.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/load-regions.js"/>" type="text/javascript"></script>
<script type="text/javascript" language="JavaScript">
    //<!--
		//var params = { 'elementId':'ajax_region_select', 'stateInputId':'select_state', 'regionInputId':'region-select' };
		//YAHOO.util.Event.addListener(window, "load", loadRegions, params);
		var params = { 'stateInputId':'select_state', 'regionInputId':'ajax_region_select', 'url':'<c:url value="/ajax/getRegionsHTMLSelect.htm"/>', 'selectNameId':'region-select' };
		YAHOO.util.Event.addListener("select_state", "change", loadRegions, params);
    //-->
</script>

<div class="content-pad">
    <h2>Shipper's Physical Address</h2> 
	(No P.O. Box or APO/FPO/DPO accepted – please use physical address only)
    <spring:bind path="expressForm.*">
        <c:if test="${status.error}">
        	<div class="error-indicator">
            <c:forEach items="${status.errorMessages}" var="errMsg" >
                <p>${errMsg}</p>
            </c:forEach>
            </div>
        </c:if>
    </spring:bind>

    <%--<authz:authorize ifNotGranted="ROLE_VOTER,ROLE_ADMIN,ROLE_FACE_ADMIN">--%>
        <form action="<c:url value="ExpressYourVote.htm"/>" method="post">
            <div class="retreive"><div class="retreive-text">Retrieve data from My Voter Account:</div>

                <div class="retreive-fields">
                    <%--<spring:bind path="expressForm.login">--%>
                    <label for="email">email </label><input name="j_username" id="email" type="email" value="" />
                    <%--</spring:bind>--%>
                    <%--<spring:bind path="expressForm.passw">--%>
                    <label for="password">password</label> <input name="j_password" id="password" type="password" value="" /></div>
                    <%--</spring:bind>--%>
                <div class="retreive-submit"><input type="image" src="img/buttons/go-button.gif" class="bt-go"  name="_target${currentStep}"/></div>
                <spring:bind path="expressForm.doLogin">
                    <input type="hidden" name="${status.expression}" value="true"/>
                </spring:bind>
                <div class="forgot-password"><a href="<c:url value='/RemindPassword.htm'/>">Forgot your password?</a></div>
            </div>
        </form>
    <%--</authz:authorize>--%>

    <form action="<c:url value='ExpressYourVote.htm'/>" name="expressYourVote" method="post" id="expressYourVote" >
    <spring:bind path="expressForm.doLogin">
        <input type="hidden" name="${status.expression}" value="false"/>
    </spring:bind>
    <!--<input type="hidden" name="submission" value="true"/>-->
        <div class="form-left">
           <%--  <authz:authorize ifNotGranted="ROLE_VOTER,ROLE_ADMIN,ROLE_FACE_ADMIN"> --%>
                <p>or enter voter's data manually:</p>
           <%--   </authz:authorize>
            <authz:authorize ifAnyGranted="ROLE_VOTER,ROLE_ADMIN,ROLE_FACE_ADMIN">
                <p>Enter voter's data:</p>
            </authz:authorize --%>
            <dl>
                <spring:bind path="expressForm.firstName">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label class="${error}" for="${status.expression}">First Name*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.middleName">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">Middle Name</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.lastName">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">Last Name*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.pickUp.street1">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">Address 1*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.pickUp.street2">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">Address 2</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.pickUp.city">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">City/Town*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <%-- TODO: Special case for Canada and Mexico --%>
                <c:if test="${expressForm.country.name eq 'Canada' or expressForm.country.name eq 'Mexico'}">
                    <dt>&nbsp;</dt><dd>
                       <c:if test="${expressForm.country.name eq 'Canada'}"><span class="note">You must use the 2-letter Province code, for example AB for Alberta</span></c:if>
                       <c:if test="${expressForm.country.name eq 'Mexico'}"><span class="note">You must use the 2-letter Province code, for example CS for Chiapas</span></c:if>
                    </dd>
                </c:if>
                <spring:bind path="expressForm.pickUp.state">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">State/Province</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.pickUp.zip">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">Postal Code<c:if test="${not empty expressForm.country.zipPattern}">*</c:if></label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <%--<spring:bind path="expressForm.countryId">--%>
                    <dt><label for="countryName">Country*</label></dt><dd><input  id="countryName" type="text" class="input-text" value="${expressForm.country.name}" readonly="readonly"/></dd>
                <%--</spring:bind>--%>
                <dt>&nbsp;</dt><dd class="last-row">*required field</dd>
            </dl>
        </div>

        <div class="form-right">
            <p>Your automatic delivery notification will be sent
                to the following email address:</p>

            <dl>
                <spring:bind path="expressForm.notificationEmail">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">Email*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="email" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.notificationEmailConfirm">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">Confirm Email*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="email" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <authz:authorize ifNotGranted="ROLE_VOTER,ROLE_ADMIN,ROLE_FACE_ADMIN">
                    <spring:bind path="expressForm.notificationPass">
                        <c:set var="error" value="" />
                        <c:if test="${status.error}">
                            <%--<p class="error">${status.errorMessage}</p>--%>
                            <c:set var="error" value="error-indicator" />
                        </c:if>
                        <dt><label  class="${error}" for="${status.expression}">Choose a Password</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="password" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                    </spring:bind>
                    <spring:bind path="expressForm.notificationPassConfirm">
                        <c:set var="error" value="" />
                        <c:if test="${status.error}">
                            <%--<p class="error">${status.errorMessage}</p>--%>
                            <c:set var="error" value="error-indicator" />
                        </c:if>
                        <dt><label  class="${error}" for="${status.expression}">Confirm Password</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="password" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                    </spring:bind>
                </authz:authorize>
                <spring:bind path="expressForm.notificationPhone">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                    <dt><label  class="${error}" for="${status.expression}">Telephone*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.destinationLeo">
                    <c:set var="error" value="" />
                    <c:if test="${status.error}">
                        <%--<p class="error">${status.errorMessage}</p>--%>
                        <c:set var="error" value="error-indicator" />
                    </c:if>
                <dt><label  class="${error}" for="select_state">Ballot destination*</label></dt>
                <dd>
                    <select name="stateId" id="select_state" class="input-select">
                        <option value="0">Choose state or territory</option>
                        <c:forEach items="${states}" var="state">
                            <c:choose>
                                <c:when test="${not empty expressForm.destinationLeo and expressForm.destinationLeo.region.state.id eq state.id}">
                                    <option value="${state.id}" selected="selected">${state.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${state.id}">${state.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </dd>
               <!--<dt>&nbsp;</dt><dd><p>Your state not here?
		<span class="rava-bubble">Unfortunately, some U.S. states specify that ballots arrive only by certain methods, not including FedEx. If your state is not on the list of participating state, please check the <a href="/state-vote-information-directory">State Voter Information Directory</a> for other allowable ballot return delivery options.</span></p></dd>
		-->
                <dt>&nbsp;</dt>
                <dd id="ajax_region_select">
                    <select name="regionId" id="region-select" class="input-select">
                        <option value="0">Choose your voting region</option>
                        <c:forEach items="${regions}" var="region">
                            <c:choose>
                                <c:when test="${not empty expressForm.destinationLeo and expressForm.destinationLeo.region.id eq region.id}">
                                    <option value="${region.id}" selected="selected">${region.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${region.id}">${region.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </dd>
            </spring:bind>
                <%--<c:if test="${expressForm.country.rate > 0}">
                    <dt><label for="payment">Payment Option*</label></dt>
                    <dd>
                        <select name="payment" id="payment" class="input-select">
                            <option value="0" selected="selected">Credit card</option>
                        </select>
                    </dd>
                </c:if>--%>
                <dt>&nbsp;</dt><dd class="last-row">
            <div style="font-weight:bold; margin-bottom:12px;">Please Note: You MUST schedule your pickup or drop off your shipment by the November 1st cutoff date!</div>
              <input type="image" src="img/buttons/continue-button.png" name="_target${currentStep+1}" value=""/>
            </dd>
            </dl>
        </div>
    </form>

    <div class="clear"></div>

</div>
