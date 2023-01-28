<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<fieldset class="currentAddress" style="min-height: 603px">
  <form:errors path="currentAddress" class="errorMsg" />
  <!-- <input type="checkbox" id="current-same-as-voting" /> Same as Voting Address -->
 Enter address information ONLY if it is different from your Home Voting Address<br/><br/>
      <div class="street1 form-group">
          <form:label	id="currentAddress1" for="currentAddress.street1" path="currentAddress.street1" cssErrorClass="error" class="label-control">Address 1</form:label>
          <form:input id="currentAddress.street1" path="currentAddress.street1" cssErrorClass="error" class="form-control"/>
          <form:errors path="currentAddress.street1" class="errorMsg" />
      </div>
      <div class="street2 form-group">
          <form:label	id="currentAddress2" for="currentAddress.street2" path="currentAddress.street2" cssErrorClass="error" class="label-control">Address 2 <span class="hint">(Apt, Suite, Building, Floor, etc.)</span></form:label>
          <form:input path="currentAddress.street2" cssErrorClass="error" class="form-control"/>
          <form:errors path="currentAddress.street2" class="errorMsg" />
      </div>
      <div class="city form-group">
          <form:label	id="currentCity" for="currentAddress.city" path="currentAddress.city" cssErrorClass="error" class="label-control">City/Town</form:label>
          <form:input path="currentAddress.city" cssErrorClass="error" class="form-control"/>
          <form:errors path="currentAddress.city" class="errorMsg" />
      </div>
     <div class="county form-group">
      <form:label	for="currentAddress.county" path="currentAddress.county" cssErrorClass="error" class="label-control">County</form:label>
      <form:input path="currentAddress.county" cssErrorClass="error" class="form-control"/>
      <form:errors path="currentAddress.county" class="errorMsg" />
    </div>
      <div class="state form-group">
          <form:label	id="currentState" for="currentAddress.state" path="currentAddress.state" cssErrorClass="error" class="label-control">State/Province/Region</form:label>
          <form:input path="currentAddress.state" cssErrorClass="error" class="form-control"/>
          <form:errors path="currentAddress.state" class="errorMsg" />
         </div>
      <div class="zip form-group">
          <form:label	for="currentAddress.zip" path="currentAddress.zip" cssErrorClass="error" class="label-control">Zip/Postal Code</form:label>
          <form:input path="currentAddress.zip" cssErrorClass="error"  class="form-control"/>
          <form:errors path="currentAddress.zip" class="errorMsg" />
      </div>
</fieldset>