<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="legend">
    <span>Current Address</span>
</div>
<fieldset class="currentAddress">
    <div class="container" id="currentAddress">
        <form:errors path="currentAddress" class="errorMsg" />
        <!-- <input type="checkbox" id="current-same-as-voting" /> Same as Voting Address -->
        <div class="addressOptions military">
            <spring:bind path="currentAddress.typeName">
                <input type="checkbox" id="militaryAddress" class="checkbox" <c:if test="${status.value eq 'MILITARY'}">checked="checked"</c:if> /> Use Military Address
                <input type="hidden" name="${status.expression}" value="OVERSEAS" id="currentAddressType" >
            </spring:bind>
        </div>
        <div id="stdAddress">
            <div class="street1">
                <form:label	id="currentAddress1" for="currentAddress.street1" path="currentAddress.street1" cssErrorClass="error">Address 1 *</form:label>
                <form:input id="currentAddress.street1" path="currentAddress.street1" cssErrorClass="error" />
                <form:errors path="currentAddress.street1" class="errorMsg" />
            </div>
            <div class="street2">
                <form:label	id="currentAddress2" for="currentAddress.street2" path="currentAddress.street2" cssErrorClass="error">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
                <form:input path="currentAddress.street2" cssErrorClass="error" />
                <form:errors path="currentAddress.street2" class="errorMsg" />
            </div>
            <div class="city">
                <form:label	id="currentCity" for="currentAddress.city" path="currentAddress.city" cssErrorClass="error">City/Town *</form:label>
                <form:input path="currentAddress.city" cssErrorClass="error" />
                <form:errors path="currentAddress.city" class="errorMsg" />
                <form:select id="militaryCurrentCity" path="currentAddress.city" cssErrorClass="error" style="display: none;" disabled="">
                	<form:option value="" label="- Select -"/>
                	<form:option value="APO" label="APO"/>
                	<form:option value="FPO" label="FPO"/>
                    <form:option value="DPO" label="DPO"/>
                </form:select>


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
                <form:select id="militaryCurrentState" path="currentAddress.state" cssErrorClass="error" style="display: none;" disabled="">
                	<form:option value="" label="- Select -"/>
                	<form:option value="AE" label="AE"/>
                	<form:option value="AA" label="AA"/>
                    <form:option value="AP" label="AP"/>
                </form:select>
               </div>

            <div class="zip">
                <form:label	for="currentAddress.zip" path="currentAddress.zip" cssErrorClass="error">Zip/Postal Code</form:label>
                <form:input path="currentAddress.zip" cssErrorClass="error" />
                <form:errors path="currentAddress.zip" class="errorMsg" />
            </div>
            <div class="country">
                <form:label	for="currentAddress.country" path="currentAddress.country" cssErrorClass="error">Country *</form:label>
                <form:select path="currentAddress.country" cssErrorClass="error">
                    <form:option value="" label="- Select -"/>
                    <form:options items="${countries}" itemValue="name" itemLabel="name"/>
                </form:select>
                <form:errors path="currentAddress.country" class="errorMsg" />
            </div>
        </div>
    </div>
</fieldset>