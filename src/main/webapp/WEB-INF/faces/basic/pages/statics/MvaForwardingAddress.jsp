<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="legend">
    	<span>Ballot Forwarding Address</span>
        <div class="rava-bubble">If you need your ballots sent to an address different from your "Current Address," click the box that says, "Mail my ballot to a forwarding address," and insert the forwarding address information.</div>
    </div>
        <fieldset class="forwardingAddressInfo overseas">
           	<div class="container">
            <input type="checkbox" id="use-forwarding-address"  class="checkbox"/> Mail my ballot to a forwarding address
                <%-- Set var fwdAddressExists here before obj created (JS for display below) --%>
            <c:if test="${user.forwardingAddress!= null && !user.forwardingAddress['emptySpace']}"><c:set var="fwdAddressExists" value="true" scope="page" /></c:if>
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
                    <form:label	for="forwardingAddress.state" path="forwardingAddress.state" cssErrorClass="error">State/Province</form:label>
                    <form:input path="forwardingAddress.state" cssErrorClass="error" />
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