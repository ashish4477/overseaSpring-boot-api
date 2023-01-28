<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<fieldset class="votingAddress">
        <spring:bind path="votingAddress.typeName">
            <input type="hidden" id="regularAddress" name="${status.expression}" value="STREET" <c:if test="${status.value ne 'STREET'}">disabled="disabled"</c:if> >
        </spring:bind>
  <div class="row form-group">
      <div class="addressOptions ruralRoute col-xs-12 col-sm-5">
         <div class="checkbox">
           <label><form:checkbox path="votingAddress.typeName" id="ruralRoute" value="RURAL_ROUTE"/> Use Rural Route</label>
        </div>
       </div>
      <div class="addressOptions addressDescribed col-xs-12 col-sm-7">
        <div class="checkbox">
          <label><form:checkbox path="votingAddress.typeName" id="addressDescribed" value="DESCRIBED"/> Use Address Description</label>
        </div>
        </div>
  </div>
  <form:errors path="votingAddress" class="errorMsg" />
        <div class="street1 form-group" id="votingAddressStreet1">
            <form:label id="votingAddress1"	for="votingAddress.street1" path="votingAddress.street1" cssErrorClass="error" class="label-control">Address 1 * <span class="hint">(Cannot be a P.O. Box)</span></form:label>
            <form:input path="votingAddress.street1" cssErrorClass="error" class="form-control" />
            <form:errors path="votingAddress.street1" class="errorMsg" />
        </div>
        <div id="votingAddressDescription" class="addressDescription form-group">
          <label>Address Description *</label>
          <form:textarea class="form-control" path="votingAddress.description" id="votingAddressDescriptionArea" rows="5" name="6"/>
          <form:errors path="votingAddress.description" class="errorMsg" />
        </div>
        <div id="votingAddress2" class="street2 form-group">
            <form:label	for="votingAddress.street2" path="votingAddress.street2" cssErrorClass="error" class="label-control">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
            <form:input path="votingAddress.street2" cssErrorClass="error" class="form-control" />
            <form:errors path="votingAddress.street2" class="errorMsg" />
        </div>
        <div class="city form-group">
            <form:label	for="votingAddress.city" path="votingAddress.city" cssErrorClass="error" class="label-control">City/Town *</form:label>
            <form:input path="votingAddress.city" cssErrorClass="error" class="form-control"/>
            <form:errors path="votingAddress.city" class="errorMsg" />
        </div>
       <div class="county form-group">
            <form:label	for="votingAddress.county" path="votingAddress.county" cssErrorClass="error" class="label-control">County</form:label>
            <form:input path="votingAddress.county" cssErrorClass="error" class="form-control"/>
            <form:errors path="votingAddress.county" class="errorMsg" />
        </div>
        <div class="state form-group">
            <form:label	for="votingAddress.state" path="votingAddress.state" cssErrorClass="error" class="label-control">State *</form:label>
            <form:select path="votingAddress.state" cssErrorClass="error" multiple="false" class="form-control">
                <form:option value="" label="- Select -"/>
                <form:options items="${states}" itemValue="abbr" itemLabel="name"/>
            </form:select>
            <form:errors path="votingAddress.state" class="errorMsg" />
        </div>
  <div class="row">
    <div class="col-xs-12 col-sm-8">
        <div class="zip form-group">
            <form:label	for="votingAddress.zip" path="votingAddress.zip" cssErrorClass="error" class="label-control">ZIP Code *</form:label>
            <form:input path="votingAddress.zip" maxlength="5" cssErrorClass="error medium" class="form-control" />
            <form:errors path="votingAddress.zip" class="errorMsg" />
        </div>
      </div>
      <div class="col-xs-12 col-sm-4">
        <div class="zip4 form-group">
            <form:label	for="votingAddress.zip4" path="votingAddress.zip4" cssErrorClass="error" class="label-control">ZIP+4</form:label>
            <form:input path="votingAddress.zip4" maxlength="4" cssErrorClass="error" class="form-control"/>
            <form:errors path="votingAddress.zip4" class="errorMsg" />
        </div>
      </div>
    </div>
        <div class="voting region form-group">
            <label class="oneline">Voting Region *<small> (Select state to see options)</small></label>
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