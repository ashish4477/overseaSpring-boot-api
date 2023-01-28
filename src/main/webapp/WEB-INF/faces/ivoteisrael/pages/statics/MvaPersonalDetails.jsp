<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<link rel="stylesheet" href="<c:url value="/css/intltelinput/intlTelInput.css"/>">
<script src="<c:url value="/js/intltelinput/intlTelInput.min.js"/>"></script>
<c:set var="showPasswordFields" value="true"/>
<c:if test="${not empty param.showPasswordFields}">
    <c:set var="showPasswordFields" value="${param.showPasswordFields}"/>
</c:if>

    <div class="row">
      <div class="col-xs-12 col-sm-6">
        <h3>Name</h3>
        <fieldset class="nameInfo">
         <div class="row form-group">
         <div class="Title col-xs-2 col-sm-3">
          <form:label	for="name.title" path="name.title" cssErrorClass="error">Title</form:label>
               <form:select path="name.title" class="tiny form-control">
                <form:option value="" label=""/>
                <form:option value="Mr." label="Mr."/>
                <form:option value="Mrs." label="Mrs."/>
                <form:option value="Miss" label="Miss"/>
                <form:option value="Ms." label="Ms."/>
            </form:select>
            <form:errors path="name.title" class="errorMsg" />
        </div>
        <div class="firstName col-xs-6">
            <form:label	for="name.firstName" path="name.firstName" cssErrorClass="error" class="required">First *</form:label>
            <form:input id="firstName" path="name.firstName" class="medium form-control"/>
            <form:errors path="name.firstName" class="errorMsg" />
        </div>
        <div class="middleName col-xs-4 col-sm-3">
            <form:label	for="name.middleName" path="name.middleName" cssErrorClass="error">Middle</form:label>
            <form:input id="middleName" path="name.middleName" class="small form-control"/>
            <form:errors path="name.middleName" class="errorMsg" />
        </div>
        </div>
        <div class="row form-group">
        <div class="lastName col-xs-10 col-sm-9">
                    <form:label	for="name.lastName" path="name.lastName" cssErrorClass="error" class="required">Last *</form:label>
                    <form:input id="lastName" path="name.lastName" class="medium form-control" />
                    <form:errors path="name.lastName" class="errorMsg" />
                </div>
                <div class="suffix col-xs-2 col-sm-3">
                    <form:label	for="name.suffix" path="name.suffix" cssErrorClass="error">Suffix</form:label>
                       <form:select path="name.suffix" class="small form-control">
                        <form:option value="" label=""/>
                        <form:option value="Jr" label="Jr"/>
                        <form:option value="Sr" label="Sr"/>
                        <form:option value="II" label="II"/>
                        <form:option value="III" label="III"/>
                        <form:option value="IV" label="IV"/>
                    </form:select>
                    <form:errors path="name.suffix" class="errorMsg" />
                </div>
          </div>
		</fieldset>
      </div>
      <div class="col-xs-12 col-sm-6">
        <h3>Contact Information</h3>
        <fieldset class="loginInfo">
            <div class="username form-group">
                <form:label	for="username" path="username" cssErrorClass="error" class="required control-label">Primary Email * <small>(used for login if applicable)</small>  </form:label>
                <form:input path="username" class="form-control" />
                <form:errors path="username" class="errorMsg" />
                <spring:bind path="username">
                    <c:if test="${fn:contains(status.errorCode, 'username.exists')}">
                        <div id="forgot-password"><a href="<c:url value="/RemindPassword.htm"/>">Forgot Password?</a></div>
                    </c:if>
                </spring:bind>
            </div>
             <c:if test="${empty userDetails and userDetails.id ne 0}">
        <div class="usernameConfirmation form-group">
                <label for="usernameConfirmation" path="usernameConfirmation" cssErrorClass="error" class="usernameConfirmation required control-label"> Please Confirm Your Email *  <span id="confirmMessage"></span></label>
                <input path="usernameConfirmation" name="usernameConfirmation" id="usernameConfirmation" class="form-control usernameConfirmation" required/>
            </div>
    </c:if>

           

            
            <div class="password form-group">
                <c:if test="${showPasswordFields eq 'true'}">
                    <form:label	for="password" path="password" cssErrorClass="error" class="control-label">Password * <small>(At least six characters long)</small></form:label>
                    <form:input type="password" path="password" class="form-control" />
                    <form:errors path="password" class="errorMsg" />
                    <form:label	for="confirmPassword" path="confirmPassword" class="confirmPassword">Confirm Password *</form:label>
                    <form:input type="password" path="confirmPassword" class="form-control" />
                    <form:errors path="confirmPassword" class="errorMsg" />
                </c:if>
            </div>
            <div class="phone form-group">
              <div class="col-xs-12 col-sm-8">
                <form:label	for="phone" path="phone" cssErrorClass="error" class="control-label">Phone (select country flag first)</form:label>
                <form:input path="phone" type="tel" class="form-control" />
                <form:errors path="phone" class="errorMsg" />
              </div>
              <div class="col-xs-12 col-sm-4">
                <form:label for="phoneType" path="phoneType" cssErrorClass="error" class="control-label">Phone Type *</form:label>
                <form:select path="phoneType" class="form-control" >
                  <form:option value="" label=""/>
                  <form:option value="Home" label="Home"/>
                  <form:option value="Work" label="Work"/>
                  <form:option value="Mobile" label="Mobile"/>
                  <form:option value="Other" label="Other"/>
                </form:select>
                <form:errors path="phoneType" class="errorMsg" />
              </div>
            </div>
        </fieldset>
      </div>
    </div>
    <div class="row">
      <div class="col-xs-12 col-sm-6">
      <div class="accordion prev">
		  <div class="legend"> <!-- needed for accordion -->
       <h3><span>Previous Name</span></h3>
       <small>(Use this option if you want to update your name)</small>
    </div>
		<fieldset class="previousName nameInfo">
      <div class="row form-group">
         <div class="Title Title col-xs-2 col-sm-3">
          <form:label	for="previousName.title" path="previousName.title" cssErrorClass="error" class="control-label">Title</form:label>
               <form:select path="previousName.title" class="tiny form-control">
                <form:option value="" label=""/>
                <form:option value="Mr." label="Mr."/>
                <form:option value="Mrs." label="Mrs."/>
                <form:option value="Miss" label="Miss"/>
                <form:option value="Ms." label="Ms."/>
            </form:select>
            <form:errors path="previousName.title" class="errorMsg" />
        </div>
        <div class="firstName col-xs-6">
            <form:label	for="previousName.firstName" path="previousName.firstName" cssErrorClass="error" class="required control-label">First</form:label>
            <form:input path="previousName.firstName" class="medium form-control"/>
            <form:errors path="previousName.firstName" class="errorMsg" />
        </div>
        <div class="middleName col-xs-4 col-sm-3">
            <form:label	for="previousName.middleName" path="previousName.middleName" cssErrorClass="error" class="control-label">Middle</form:label>
            <form:input path="previousName.middleName" class="small form-control"/>
            <form:errors path="previousName.middleName" class="errorMsg" />
        </div>
      </div>
      <div class="row form-group">
        <div class="lastName col-xs-10 col-sm-9">
            <form:label	for="previousName.lastName" path="previousName.lastName" cssErrorClass="error" class="required control-label">Last</form:label>
            <form:input path="previousName.lastName" class="medium form-control" />
            <form:errors path="previousName.lastName" class="errorMsg" />
        </div>
        <div class="suffix col-xs-2 col-sm-3">
            <form:label	for="previousName.suffix" path="previousName.suffix" cssErrorClass="error" class="control-label">Suffix</form:label>
               <form:select path="previousName.suffix" class="small form-control">
                <form:option value="" label=""/>
                <form:option value="Jr" label="Jr"/>
                <form:option value="Sr" label="Sr"/>
                <form:option value="II" label="II"/>
                <form:option value="III" label="III"/>
                <form:option value="IV" label="IV"/>
            </form:select>
            <form:errors path="previousName.suffix" class="errorMsg" />
        </div>
      </div>
		</fieldset>
      </div> </div>
      <div class="col-xs-12 col-sm-6">
        <div class="accordion alt">
        <div class="legend"> <!-- needed for accordion -->
          <h3><span>Alternate Contact Information</span></h3>
          <small>&nbsp;</small>
        </div>
        <fieldset class="alternateContact">
        <div class="alternateEmail form-group">
                <form:label	for="alternateEmail" path="alternateEmail" cssErrorClass="error" class="control-label">Alternate Email</form:label>
                <form:input path="alternateEmail" type="email" class="form-control"/>
                <form:errors path="alternateEmail" class="errorMsg" />
            </div>
            
            
            <div class="alternatePhone form-group">
              <div class="col-xs-12 col-sm-8">
                <form:label	for="alternatePhone" path="alternatePhone" cssErrorClass="error" class="control-label">Alternate Phone<small> (work phone, cell phone)</small></form:label>
                <form:input path="alternatePhone" type="tel" class="form-control"/>
                <form:errors path="alternatePhone" class="errorMsg" />
              </div>
              <div class="col-xs-12 col-sm-4">
                <form:label	for="alternatePhoneType" path="alternatePhoneType" cssErrorClass="error" class="control-label">Phone Type</form:label>
                <form:select path="alternatePhoneType" class="form-control">
                  <form:option value="" label=""/>
                  <form:option value="Home" label="Home"/>
                  <form:option value="Work" label="Work"/>
                  <form:option value="Mobile" label="Mobile"/>
                  <form:option value="Other" label="Other"/>
                </form:select>
                <form:errors path="alternatePhoneType" class="errorMsg" />
              </div>
            </div>
		</fieldset>
		</div>
      </div>
   </div>
     <div class="row form-group">
      <div class="col-xs-12 col-sm-6">
        <h3>Birth Date</h3>
        <fieldset class="birthInfo">
          <div class="row">
            <div class="birthMonth form-group col-xs-4">
                <form:label	for="birthMonth" path="birthMonth" cssErrorClass="error" class="required control-label">Month *</form:label>
                <form:select  id="birthMonth" path="birthMonth" cssErrorClass="error" class="form-control">
                    <form:option value="0" label="Select"/>
                    <c:forEach var="i" begin="1" end="12" step="1" varStatus ="status">
                        <form:option value="${i}" label="${i}"/>
                    </c:forEach>
                </form:select>
                <form:errors path="birthMonth" class="errorMsg" />
            </div>
            <div class="birthDate form-group col-xs-4">
                <form:label	for="birthDate" path="birthDate" cssErrorClass="error" class="required control-label">Day *</form:label>
                <form:select id="birthDate" path="birthDate" cssErrorClass="error" class="form-control">
                    <form:option value="0" label="Select"/>
                    <c:forEach var="i" begin="1" end="31" step="1" varStatus ="status">
                        <form:option value="${i}" label="${i}"/>
                    </c:forEach>
                </form:select>
                <form:errors path="birthDate" class="errorMsg" />
            </div>
            <div class="birthYear form-group col-xs-4">
                <form:label	for="birthYear" path="birthYear" cssErrorClass="error" class="required control-label">Year *</form:label>
                <form:select id="birthYear" path="birthYear" cssErrorClass="error" class="form-control">
                <c:set var="yearNumb" value="${yearNumber-16}" scope="page"></c:set>
                    <form:option value="0" label="Select"/>
                    <c:forEach begin="1900" end="${yearNumb}" var="i" step="1">
                      <c:set var="j" value="${1900+yearNumb-i}"></c:set>
                        <form:option value="${j}" label="${j}"/>
                    </c:forEach>
                </form:select>
                <form:errors path="birthYear" class="errorMsg" />
            </div>
          </div>
        </fieldset>
      </div>
    </div>

<script>
    var phoneInput = $("#phone"), altPhoneInput = $("#alternatePhone");

    phoneInput.attr('name', 'phoneNational');
    altPhoneInput.attr('name', 'alternatePhoneNational');

    phoneInput.intlTelInput( {
        nationalMode: true,
        hiddenInput: "phone",
        utilsScript: "<c:url value="/js/intltelinput/utils.js"/>"
    });
    altPhoneInput.intlTelInput( {
        nationalMode: true,
        hiddenInput: "alternatePhone",
        utilsScript: "<c:url value="/js/intltelinput/utils.js"/>"
    });

</script>
<script>
    $(function() {




    if (localStorage["usernameConfirmation"]) {
        $('#usernameConfirmation').val(localStorage["usernameConfirmation"]);
    }

  $('#usernameConfirmation').blur(function(){

      localStorage[$(this).attr('name')] = $(this).val();

      if($('#username').val() != $('#usernameConfirmation').val()) {
        $('.usernameConfirmation').addClass( "has-error" );
        $('#confirmMessage').html("<br/>Your Emails do not match, please correct your entry");
      }
      else{
        $('.usernameConfirmation').removeClass( "has-error" );
        $('#confirmMessage').empty();
      }
    });

    });
</script>
