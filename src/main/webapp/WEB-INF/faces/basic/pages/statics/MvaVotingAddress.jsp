<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--
<div class="legend">
    	<span>Last U.S. Residence Address</span>
    	<div class="rava-bubble"><strong>Uniformed Services:</strong> Use legal residence address.<br/> <br/>
        <strong>Overseas Citizens:</strong> Use the address of the last place you lived resided before departing moving outside of the U.S.
        <br/> <br/>
        This must be a physical street address. A Post Office (P.O.) Box cannot be used as a voting residence address.<br/> <br/>
        If you lived in an area with no street names, click the box that says, "Use Rural Route," include the route name and number and box number, and describe the residence location.<br/> <br/>
        If you don't recall the exact street address, click the box that says "Use Address Description," and describe the residence location.

		</div>
    	</div>
--%> 
<div class="legend"><span>Last U.S. Residence Address</span></div>
        <fieldset class="votingAddress">
            <div class="container">
                <spring:bind path="votingAddress.typeName">
                    <input type="hidden" id="regularAddress" name="${status.expression}" value="STREET" <c:if test="${status.value ne 'STREET'}">disabled="disabled"</c:if> >
                </spring:bind>
           		<div class="addressOptions ruralRoute"><form:checkbox path="votingAddress.typeName" class="checkbox" id="ruralRoute" value="RURAL_ROUTE"/> Use Rural Route</div>
            	<div class="addressOptions addressDescribed"><form:checkbox path="votingAddress.typeName"  class="checkbox" id="addressDescribed" value="DESCRIBED"/> Use Address Description</div>
            	<form:errors path="votingAddress" class="errorMsg" />
            	<div class="clear-fix break">&nbsp;</div>
                <div class="street1" id="votingAddressStreet1">
                    <form:label id="votingAddress1"	for="votingAddress.street1" path="votingAddress.street1" cssErrorClass="error">Address 1 * <span class="hint">(Cannot be a P.O. Box)</span></form:label>
                    <form:input path="votingAddress.street1" cssErrorClass="error" />
                    <form:errors path="votingAddress.street1" class="errorMsg" />
                </div>
                <div id="votingAddressDescription" class="addressDescription">
                	<label>Address Description *</label>
                	<form:textarea class="large" path="votingAddress.description" id="votingAddressDescriptionArea" rows="5" name="6"/>
                	<form:errors path="votingAddress.description" class="errorMsg" />
                </div>
                <div id="votingAddress2" class="street2">
                    <form:label	for="votingAddress.street2" path="votingAddress.street2" cssErrorClass="error">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
                    <form:input path="votingAddress.street2" cssErrorClass="error" />
                    <form:errors path="votingAddress.street2" class="errorMsg" />
                </div>
                <div class="city">
                    <form:label	for="votingAddress.city" path="votingAddress.city" cssErrorClass="error">City/Town *</form:label>
                    <form:input path="votingAddress.city" cssErrorClass="error" />
                    <form:errors path="votingAddress.city" class="errorMsg" />
                </div>
               <div class="county">
                    <form:label	for="votingAddress.county" path="votingAddress.county" cssErrorClass="error">County</form:label>
                    <form:input path="votingAddress.county" cssErrorClass="error" class="large"/>
                    <form:errors path="votingAddress.county" class="errorMsg" />
                </div>
                <div class="state">
                    <form:label	for="votingAddress.state" path="votingAddress.state" cssErrorClass="error">State *</form:label>
                    <form:select path="votingAddress.state" cssErrorClass="error" multiple="false">
                        <form:option value="" label="- Select -"/>
                        <form:options items="${states}" itemValue="abbr" itemLabel="name"/>
                    </form:select>
                    <form:errors path="votingAddress.state" class="errorMsg" />
                </div>
                <div class="zip split left">
                    <form:label	for="votingAddress.zip" path="votingAddress.zip" cssErrorClass="error">ZIP Code *</form:label>
                    <form:input path="votingAddress.zip" maxlength="5" cssErrorClass="error medium" class="medium" />
                    <form:errors path="votingAddress.zip" class="errorMsg" />
                </div>
                <div class="zip4 split right">
                    <form:label	for="votingAddress.zip4" path="votingAddress.zip4" cssErrorClass="error">ZIP+4</form:label>
                    <form:input path="votingAddress.zip4" maxlength="4" cssErrorClass="error" class="small"/>
                    <form:errors path="votingAddress.zip4" class="errorMsg" />
                </div>
                <div class="clear-fix break">&nbsp;</div>
                <div class="voting region">
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
            </div>
        </fieldset>
        <script type="text/javascript">
    $(document).ready(function() {
        var selectobject = document.getElementById("votingAddress.state");
for (var i=0; i<selectobject.length; i++) {
    if (selectobject.options[i].value == 'AS')
        selectobject.remove(i);
}
        });

</script>