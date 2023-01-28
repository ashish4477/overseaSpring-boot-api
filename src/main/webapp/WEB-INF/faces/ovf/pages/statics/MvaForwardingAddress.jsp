<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


    <fieldset class="forwardingAddressInfo overseas" style="min-height: 90px;">
      <div class="checkbox">
        <label>
        <input type="checkbox" id="use-forwarding-address"/> I use a different address to receive mail
            <%-- Set var fwdAddressExists here before obj created (JS for display below) --%>
        <c:if test="${user.forwardingAddress!= null && !user.forwardingAddress['emptySpace']}"><c:set var="fwdAddressExists" value="true" scope="page" /></c:if>
        </label>
        </div>
        <div id="forwardingAddress">
            <spring:bind path="forwardingAddress.typeName">
                <input type="hidden" name="${status.expression}" value="OVERSEAS"/>
            </spring:bind>
            <div class="street1 form-group">
                <form:label	for="forwardingAddress.street1" path="forwardingAddress.street1" cssErrorClass="error" class="label-control">Address 1</form:label>
                <form:input path="forwardingAddress.street1" cssErrorClass="error" class="form-control"/>
                <form:errors path="forwardingAddress.street1" class="errorMsg" />
            </div>
            <div class="street2 form-group">
                <form:label	for="forwardingAddress.street2" path="forwardingAddress.street2" cssErrorClass="error" class="label-control">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
                <form:input path="forwardingAddress.street2" cssErrorClass="error" class="form-control"/>
                <form:errors path="forwardingAddress.street2" class="errorMsg" />
            </div>
            <div class="city form-group">
                <form:label	for="forwardingAddress.city" path="forwardingAddress.city" cssErrorClass="error" class="label-control">City/Town</form:label>
                <form:input path="forwardingAddress.city" cssErrorClass="error" class="form-control"/>
                <form:errors path="forwardingAddress.city" class="errorMsg" />
            </div>
            <div class="state form-group">
                <form:label	for="forwardingAddress.state" path="forwardingAddress.state" cssErrorClass="error" class="label-control">State/Province</form:label>
                <form:input path="forwardingAddress.state" cssErrorClass="error" class="form-control"/>
                <form:errors path="forwardingAddress.state" class="errorMsg" />
            </div>
            <div class="zip form-group">
                <form:label	for="forwardingAddress.zip" path="forwardingAddress.zip" cssErrorClass="error" class="label-control">Postal Code</form:label>
                <form:input path="forwardingAddress.zip" cssErrorClass="error" class="form-control"/>
                <form:errors path="forwardingAddress.zip" class="errorMsg" />
            </div>
            <div class="country form-group">
                <form:label	for="forwardingAddress.country" path="forwardingAddress.country" cssErrorClass="error" class="label-control">Country</form:label>
                <form:errors path="forwardingAddress.country" class="errorMsg" />
                <form:select path="forwardingAddress.country" cssErrorClass="error" class="form-control">
                    <form:option value="" label="- Select -"/>
                    <form:options items="${countries}" itemValue="name" itemLabel="name"/>
                </form:select>
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