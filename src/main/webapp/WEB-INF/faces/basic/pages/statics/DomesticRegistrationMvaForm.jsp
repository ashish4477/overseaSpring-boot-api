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

<c:import url="/WEB-INF/faces/basic/pages/statics/MvaPersonalDetails.jsp" />
<div class="section two"><a name="votingAddress"></a>
    <div class="column left">
    	<div class="legend"><span>Home Address</span><div class="rava-bubble">Where you are registering to vote<br/> Your legal address used for voting purposes</div></div>
      <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaVotingAddress.jsp" />
    </div>
    <div class="column right"><a name="previousAddress"></a>
        <div class="legend"><span>Previous Address</span>  <div class="rava-bubble">If you were registered to vote in a different location</div></div>
        <fieldset class="previousAddress">
            <div class="container">
                <form:errors path="previousAddress" class="errorMsg" />
                <!-- <input type="checkbox" id="current-same-as-voting" /> Same as Voting Address -->
                <div class="hint">
                   <p>(Use only if you were registered to vote before and wish to change or cancel your prior registration)</p>
                </div>
                <div id="stdAddress">
                    <div class="previousAddress1">
                        <form:label	for="previousAddress.street1" path="previousAddress.street1" cssErrorClass="error">Address 1</form:label>
                        <form:input path="previousAddress.street1" cssErrorClass="error" />
                        <form:errors path="previousAddress.street1" class="errorMsg" />
                    </div>
                    <div class="previousAddress2">
                        <form:label	for="previousAddress.street2" path="previousAddress.street2" cssErrorClass="error">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
                        <form:input path="previousAddress.street2" cssErrorClass="error" />
                        <form:errors path="previousAddress.street2" class="errorMsg" />
                    </div>
                    <div class="city">
                        <form:label	id="currentCity" for="previousAddress.city" path="previousAddress.city" cssErrorClass="error">City/Town</form:label>
                        <form:input path="previousAddress.city" cssErrorClass="error" />
                        <form:errors path="previousAddress.city" class="errorMsg" />
                    </div>
                    <div class="county">
                    <form:label	for="previousAddress.county" path="previousAddress.county" cssErrorClass="error">County</form:label>
                    <form:input path="previousAddress.county" cssErrorClass="error" class="large"/>
                    <form:errors path="previousAddress.county" class="errorMsg" />
                	</div>
                    <div class="state">
                    <form:label	id="previousState" for="previousAddress.state" path="previousAddress.state" cssErrorClass="error">State</form:label>
                     <form:select path="previousAddress.state" cssErrorClass="error" multiple="false">
                        <form:option value="" label="- Select -"/>
                        <form:options items="${states}" itemValue="abbr" itemLabel="name"/>
                    </form:select>
                    <form:errors path="previousAddress.state" class="errorMsg" />
                    </div>
                    <div class="zip">
                        <form:label	for="previousAddress.zip" path="previousAddress.zip" cssErrorClass="error">Zip/Postal Code</form:label>
                        <form:input path="previousAddress.zip" cssErrorClass="error" />
                        <form:errors path="previousAddress.zip" class="errorMsg" />
                    </div>
                </div>
            </div>
        </fieldset>
         <div class="legend"><span>Forwarding Address</span><div class="rava-bubble">Check the following box only if you do not want your ballot mailed to your current address.</div></div>
         <fieldset class="forwardingAddressInfo domestic">
        
            <input type="checkbox" id="use-forwarding-address"  class="checkbox"/> I use a different address to receive mail
                <%-- Set var fwdAddressExists here before obj created (JS for display below) --%>
            <c:if test="${user.forwardingAddress!= null && !user.forwardingAddress['emptySpace']}"><c:set var="fwdAddressExists" value="true" scope="page" /></c:if>
            <div class="container">
            <div id="forwardingAddress">
                <spring:bind path="forwardingAddress.typeName">
                    <input type="hidden" name="${status.expression}" value="OVERSEAS"/>
                </spring:bind>
                <div class="street1">
                    <form:label	for="forwardingAddress.street1" path="forwardingAddress.street1" cssErrorClass="error">Address 1</form:label>
                    <form:input path="forwardingAddress.street1" cssErrorClass="error" />
                    <form:errors path="forwardingAddress.street1" class="errorMsg" />
                </div>
                <div class="street2">
                    <form:label	for="forwardingAddress.street2" path="forwardingAddress.street2" cssErrorClass="error">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
                    <form:input path="forwardingAddress.street2" cssErrorClass="error" />
                    <form:errors path="forwardingAddress.street2" class="errorMsg" />
                </div>
                <div class="city">
                    <form:label	for="forwardingAddress.city" path="forwardingAddress.city" cssErrorClass="error">City/Town</form:label>
                    <form:input path="forwardingAddress.city" cssErrorClass="error" />
                    <form:errors path="forwardingAddress.city" class="errorMsg" />
                </div>
                <div class="state">
                    <form:label	for="forwardingAddress.state" path="forwardingAddress.state" cssErrorClass="error">State</form:label>
                    <form:select path="forwardingAddress.state" cssErrorClass="error" multiple="false">
                        <form:option value="" label="- Select -"/>
                        <form:options items="${states}" itemValue="abbr" itemLabel="name"/>
                    </form:select>
                    <form:errors path="forwardingAddress.state" class="errorMsg" />
                </div>
                <div class="zip">
                    <form:label	for="forwardingAddress.zip" path="forwardingAddress.zip" cssErrorClass="error">Postal Code</form:label>
                    <form:input path="forwardingAddress.zip" cssErrorClass="error" />
                    <form:errors path="forwardingAddress.zip" class="errorMsg" />
                </div>
                <div class="country">
                    <form:label	for="forwardingAddress.country" path="forwardingAddress.country" cssErrorClass="error">Country</form:label>
                    <form:errors path="forwardingAddress.country" class="errorMsg" />
                    <form:select path="forwardingAddress.country" cssErrorClass="error">
                        <form:option value="" label="- Select -"/>
                        <form:options items="${countries}" itemValue="name" itemLabel="name"/>
                    </form:select>
                </div>
            </div>
            </div>
            <c:if test="${fwdAddressExists == 'true'}">
                <script type="text/javascript">
                    /* check box & display field if fwding address exists */
                    document.getElementById('use-forwarding-address').checked = true;
                    document.getElementById('forwardingAddress').style.display = "block";                    
                </script>
            </c:if>
        </fieldset>
    </div>
</div>
<input type="image" src="<c:url value="/img/buttons/continue-button.gif"/>" name="create" value="Continue" class="submit-button" />
<c:if test="${param.showBackButton}">
    <a class="back-button" href="<c:url value="/Login.htm"/>"><img src="<c:url value="/img/buttons/back-button.gif"/>" alt="back"></a>
</c:if>
<div class="break"></div>
</form:form>
<script type="text/javascript" language="JavaScript">
    //<!--
    
 if (document.cookie.length > 0){
    	function getCookie(c_name)
    	{
    	var i,x,y,ARRcookies=document.cookie.split(";");
    	for (i=0;i<ARRcookies.length;i++)
    	{
    	  x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
    	  y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
    	  x=x.replace(/^\s+|\s+$/g,"");
    	  if (x==c_name)
    	    {
    	    return unescape(y);
    	    }
    	  }
    	}
    	var voterType = getCookie('voterType');
    	var voterHistory = getCookie('voterHistory');
    	if (voterType){
        	document.getElementById('voterType').selectedIndex=voterType;
    	}
    	if (voterHistory){
    	document.getElementById('voterHistory').selectedIndex=voterHistory;
    	}
    	}
 //-->
 </script>
