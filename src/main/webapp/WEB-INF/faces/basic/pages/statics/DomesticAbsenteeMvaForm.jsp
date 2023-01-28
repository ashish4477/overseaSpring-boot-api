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
    	<div class="legend"><span>Home Voting Address</span>
    	<div class="rava-bubble">Your legal address used for voting purposes</div>
    	<div class="hint">(Address where you are currently registered to vote)<br/><br/></div>
    	</div>
      <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaVotingAddress.jsp" />
    </div>
    <div class="column right">
    <div class="accordion">
    <div class="legend">
        	<span>Ballot Mail-to Address</span>
        	<div class="rava-bubble">Address of where you want your ballot sent. This is usually where you are currently living.</div>
            	<div class="hint">(<b>Enter address information <u>only</u> if it is different from your Home Voting Address</b>)</div>
        </div>
        <fieldset class="currentAddress">
            <div class="container" id="currentAddress">
                <form:errors path="currentAddress" class="errorMsg" />
                <!-- <input type="checkbox" id="current-same-as-voting" /> Same as Voting Address -->

                <div id="stdAddress">
                    <div class="street1">
                        <form:label	id="currentAddress1" for="currentAddress.street1" path="currentAddress.street1" cssErrorClass="error">Address 1</form:label>
                        <form:input id="currentAddress.street1" path="currentAddress.street1" cssErrorClass="error" />
                        <form:errors path="currentAddress.street1" class="errorMsg" />
                    </div>
                    <div class="street2">
                        <form:label	id="currentAddress2" for="currentAddress.street2" path="currentAddress.street2" cssErrorClass="error">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
                        <form:input path="currentAddress.street2" cssErrorClass="error" />
                        <form:errors path="currentAddress.street2" class="errorMsg" />
                    </div>
                    <div class="city">
                        <form:label	id="currentCity" for="currentAddress.city" path="currentAddress.city" cssErrorClass="error">City/Town</form:label>
                        <form:input path="currentAddress.city" cssErrorClass="error" />
                        <form:errors path="currentAddress.city" class="errorMsg" />
                    </div>
                   <div class="county">
                    <form:label	for="currentAddress.county" path="currentAddress.county" cssErrorClass="error">County</form:label>
                    <form:input path="currentAddress.county" cssErrorClass="error" class="large"/>
                    <form:errors path="currentAddress.county" class="errorMsg" />
                	</div>
                    <div class="state">
                        <form:label	id="currentState" for="currentAddress.state" path="currentAddress.state" cssErrorClass="error">State/Province/Region</form:label>
                        <form:input path="currentAddress.state" cssErrorClass="error" />
                        <form:errors path="currentAddress.state" class="errorMsg" />
                       </div>

                    <div class="zip">
                        <form:label	for="currentAddress.zip" path="currentAddress.zip" cssErrorClass="error">Zip/Postal Code</form:label>
                        <form:input path="currentAddress.zip" cssErrorClass="error" />
                        <form:errors path="currentAddress.zip" class="errorMsg" />
                    </div>
                </div>
            </div>
        </fieldset>
    </div>
  </div>
</div>
<input type="submit" name="create" value="Continue" class="submit-button" />
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
