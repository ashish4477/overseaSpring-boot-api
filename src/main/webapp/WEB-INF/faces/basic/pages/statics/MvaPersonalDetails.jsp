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

<div class="section one">
    <div class="column left">
     <div class="legend"><span>Name</span></div>
        <fieldset class="nameInfo">
            <div class="container">
                 <div class="Title split left">
                    <form:label for="name.title" path="name.title" cssErrorClass="error">Title</form:label>
                       <form:select path="name.title" class="tiny">
                        <form:option value="" label=""/>
                        <form:option value="Mr." label="Mr."/>
                        <form:option value="Mrs." label="Mrs."/>
                        <form:option value="Miss" label="Miss"/>
                        <form:option value="Ms." label="Ms."/>
                    </form:select>
                    <form:errors path="name.title" class="errorMsg" />
                </div>
                <div class="firstName split right">
                    <form:label for="name.firstName" path="name.firstName" cssErrorClass="error" class="required">First *</form:label>
                    <form:input id="firstName" path="name.firstName" class="medium"/>
                    <form:errors path="name.firstName" class="errorMsg" />
                </div>
                <div class="middleName split right">
                    <form:label for="name.middleName" path="name.middleName" cssErrorClass="error">Middle</form:label>
                    <form:input id="middleName" path="name.middleName" class="small"/>
                    <form:errors path="name.middleName" class="errorMsg" />
                </div>
                <div class="clear-fix break">&nbsp;</div>
                <div class="lastName split left">
                    <form:label for="name.lastName" path="name.lastName" cssErrorClass="error" class="required">Last *</form:label>
                    <form:input id="lastName" path="name.lastName" class="medium" />
                    <form:errors path="name.lastName" class="errorMsg" />
                </div>
                <div class="suffix split right">
                    <form:label for="name.suffix" path="name.suffix" cssErrorClass="error">Suffix</form:label>
                       <form:select path="name.suffix" class="small">
                        <form:option value="" label="Select"/>
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
        <div class="accordion prev">
        <div class="legend">
       <span>Previous Name </span>
       <div class="hint"><small style="font-size:12px;">(Use this option if you want to update your name)</small></div>
    </div>
        <fieldset class="previousName nameInfo">
            <div class="container">
                 <div class="Title split left">
                    <form:label for="previousName.title" path="previousName.title" cssErrorClass="error">Title</form:label>
                       <form:select path="previousName.title" class="tiny">
                        <form:option value="" label=""/>
                        <form:option value="Mr." label="Mr."/>
                        <form:option value="Mrs." label="Mrs."/>
                        <form:option value="Miss" label="Miss"/>
                        <form:option value="Ms." label="Ms."/>
                    </form:select>
                    <form:errors path="previousName.title" class="errorMsg" />
                </div>
                <div class="firstName split right">
                    <form:label for="previousName.firstName" path="previousName.firstName" cssErrorClass="error" class="required">First</form:label>
                    <form:input path="previousName.firstName" class="medium"/>
                    <form:errors path="previousName.firstName" class="errorMsg" />
                </div>
                <div class="middleName split right">
                    <form:label for="previousName.middleName" path="previousName.middleName" cssErrorClass="error">Middle</form:label>
                    <form:input path="previousName.middleName" class="small"/>
                    <form:errors path="previousName.middleName" class="errorMsg" />
                </div>
                <div class="clear-fix break">&nbsp;</div>
                <div class="lastName split left">
                    <form:label for="previousName.lastName" path="previousName.lastName" cssErrorClass="error" class="required">Last</form:label>
                    <form:input path="previousName.lastName" class="medium" />
                    <form:errors path="previousName.lastName" class="errorMsg" />
                </div>
                <div class="suffix split right">
                    <form:label for="previousName.suffix" path="previousName.suffix" cssErrorClass="error">Suffix</form:label>
                       <form:select path="previousName.suffix" class="small">
                        <form:option value="" label=" Select"/>
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
        </div>
         <div class="legend"><span class="required">Birth Date</span></div>
        <fieldset class="birthInfo">
            <div class="container">
                <div class="birthMonth">
                    <form:label for="birthMonth" path="birthMonth" cssErrorClass="error" class="required">Month *</form:label>
                    <form:select  id="birthMonth" path="birthMonth" cssErrorClass="error">
                        <form:option value="0" label="Select"/>
                        <c:forEach var="i" begin="1" end="12" step="1" varStatus ="status">
                            <form:option value="${i}" label="${i}"/>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="birthDate">
                    <form:label for="birthDate" path="birthDate" cssErrorClass="error" class="required">Day *</form:label>
                    <form:select id="birthDate" path="birthDate" cssErrorClass="error">
                        <form:option value="0" label="Select"/>
                        <c:forEach var="i" begin="1" end="31" step="1" varStatus ="status">
                            <form:option value="${i}" label="${i}"/>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="birthYear">
                    <form:label for="birthYear" path="birthYear" cssErrorClass="error" class="required">Year *</form:label>
                    <form:select id="birthYear" path="birthYear" cssErrorClass="error">
                    <c:set var="yearNumb" value="${yearNumber-16}" scope="page"></c:set>
                    <form:option value="0" label="Select"/>
                    <c:forEach begin="1900" end="${yearNumb}" var="i" step="1">
                      <c:set var="j" value="${1900+yearNumb-i}"></c:set>
                        <form:option value="${j}" label="${j}"/>
                    </c:forEach>
                    </form:select>
                </div>
                <form:errors path="birthMonth" class="errorMsg" />
                <form:errors path="birthDate" class="errorMsg" />
                <form:errors path="birthYear" class="errorMsg" />
            </div>
        </fieldset>
    </div><!-- end column left -->
    <div class="column right">
        <div class="legend"><span>Contact Information</span></div>
        <fieldset class="loginInfo">
            <div class="container">
                <div class="username">
                    <form:label for="username" path="username" cssErrorClass="error" class="required">Primary Email * <span class="hint">(used for login if applicable)</span>  </form:label>
                    <form:input path="username"/>
                    <form:errors path="username" class="errorMsg" />
                    <spring:bind path="username">
                        <c:if test="${fn:contains(status.errorCode, 'username.exists')}">
                            <div id="forgot-password"><a href="<c:url value="/RemindPassword.htm"/>">Forgot Password?</a></div>
                        </c:if>
                    </spring:bind>
                </div>
                <div class="password">
                    <c:if test="${showPasswordFields eq 'true'}">
                        <form:label for="password" path="password" cssErrorClass="error">Password * <span class="hint">(At least six characters long)</span></form:label>
                        <form:input type="password" path="password" />
                        <form:errors path="password" class="errorMsg" />
                        <form:label for="confirmPassword" path="confirmPassword" class="confirmPassword">Confirm Password *</form:label>
                        <form:input type="password" path="confirmPassword" />
                        <form:errors path="confirmPassword" class="errorMsg" />
                    </c:if>
                </div>
                <div class="phone">
                    <form:label for="phone" path="phone" cssErrorClass="error">Phone * (select country flag first)</form:label>
                    <form:input path="phone" type="tel"/>
                    <form:errors path="phone" class="errorMsg" />
                </div>
            </div>
        </fieldset>
        <div class="accordion alt">
        <div class="legend">
          <span>Alternate Contact Information</span>
          <div class="hint">&nbsp;</div>
        </div>
        <fieldset class="alternateContact">
            <div class="container">
                <div class="alternateEmail">
                    <form:label for="alternateEmail" path="alternateEmail" cssErrorClass="error">Alternate Email</form:label>
                    <form:input path="alternateEmail" type="email"/>
                    <form:errors path="alternateEmail" class="errorMsg" />
                </div>
                <div class="alternatePhone">
                    <form:label for="alternatePhone" path="alternatePhone" cssErrorClass="error">Alternate Phone<span class="hint"> (work phone, cell phone)</span></form:label>
                    <form:input path="alternatePhone" type="tel"/>
                    <form:errors path="alternatePhone" class="errorMsg" />
                </div>
            </div>
        </fieldset>
        </div>
        <div class="legend"><!-- span>Personal Details</span --></div>

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