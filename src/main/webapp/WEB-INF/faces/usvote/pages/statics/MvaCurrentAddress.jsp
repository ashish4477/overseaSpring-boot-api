<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

        <fieldset class="currentAddress">
            <div id="currentAddress">
                <form:errors path="currentAddress" class="errorMsg" />
                <!-- <input type="checkbox" id="current-same-as-voting" /> Same as Voting Address -->
                <div class="addressOptions military checkbox form-group">
                <label>
                  <spring:bind path="currentAddress.typeName">
                        <input type="checkbox" id="militaryAddress" <c:if test="${status.value eq 'MILITARY'}">checked="checked"</c:if> /> <span>Use Military Address</span>
                        <input type="hidden" name="${status.expression}" value="OVERSEAS" id="currentAddressType" >
                    </spring:bind>
                </label>
                </div>
                <div id="stdAddress">
                    <div class="street1 form-group">
                        <form:label	id="currentAddress1" for="currentAddress.street1" path="currentAddress.street1" cssErrorClass="error"  class="label-control">Address 1 *</form:label>
                        <form:input id="currentAddress.street1" path="currentAddress.street1" cssErrorClass="error form-control" class="form-control"/>
                        <form:errors path="currentAddress.street1" class="errorMsg" />
                    </div>
                    <div class="street2 form-group">
                        <form:label	id="currentAddress2" for="currentAddress.street2" path="currentAddress.street2" cssErrorClass="error"  class="label-control">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
                        <form:input path="currentAddress.street2" cssErrorClass="error form-control" class="form-control"/>
                        <form:errors path="currentAddress.street2" class="errorMsg" />
                    </div>
                    <div class="city form-group">
                        <form:label	id="currentCity" for="currentAddress.city" path="currentAddress.city" cssErrorClass="error"  class="label-control">City/Town *</form:label>
                        <form:input path="currentAddress.city" cssErrorClass="error form-control" class="form-control"/>
                        <form:errors path="currentAddress.city" class="errorMsg" />
                        <form:select id="militaryCurrentCity" path="currentAddress.city" cssErrorClass="error form-control" class="form-control" style="display: none;" disabled="">
                        	<form:option value="" label="- Select -"/>
                        	<form:option value="APO" label="APO"/>
                        	<form:option value="FPO" label="FPO"/>
                            <form:option value="DPO" label="DPO"/>
                        </form:select>
                    </div>
                   <div class="county form-group">
                    <form:label	for="currentAddress.county" path="currentAddress.county" cssErrorClass="error"  class="label-control">County</form:label>
                    <form:input path="currentAddress.county" cssErrorClass="error form-control" class="form-control"/>
                    <form:errors path="currentAddress.county" class="errorMsg" />
                	</div>
                    <div class="state form-group">
                        <form:label	id="currentState" for="currentAddress.state" path="currentAddress.state" cssErrorClass="error"  class="label-control">State/Province/Region</form:label>
                        <form:input path="currentAddress.state" cssErrorClass="error" class="form-control"/>
                        <form:errors path="currentAddress.state" class="errorMsg" />
                        <form:select id="militaryCurrentState" path="currentAddress.state" cssErrorClass="error form-control" class="form-control" style="display: none;" disabled="">
                        	<form:option value="" label="- Select -"/>
                        	<form:option value="AE" label="AE"/>
                        	<form:option value="AA" label="AA"/>
                            <form:option value="AP" label="AP"/>
                        </form:select>
                       </div>

                    <div class="zip form-group">
                        <form:label	for="currentAddress.zip" path="currentAddress.zip" cssErrorClass="error"  class="label-control">Zip/Postal Code</form:label>
                        <form:input path="currentAddress.zip" cssErrorClass="error form-control" class="form-control"/>
                        <form:errors path="currentAddress.zip" class="errorMsg" />
                    </div>
                    <div class="country form-group">
                        <form:label	for="currentAddress.country" path="currentAddress.country" cssErrorClass="error" class="label-control">Country *</form:label>
                        <form:select path="currentAddress.country" cssErrorClass="error form-control" class="form-control">
                            <form:option value="" label="- Select -"/>
                            <form:options items="${countries}" itemValue="name" itemLabel="name"/>
                        </form:select>
                        <form:errors path="currentAddress.country" class="errorMsg" />
                    </div>
                </div>
            </div>
        </fieldset>