<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%--
This is used in multiple places
--%>

<form role="form" action="<c:url value="/user_login"/>" method="post">
  <fieldset id="credentials">
    <div class="form-group">
    <label for="j_username" class="label-control">Email:</label>
    <input class="form-control" type="email" id="j_username" name="j_username" value="<c:out value="${param.j_username}" />"/>
    </div>
    <div class="form-group">
    <label for="j_password" class="label-control">Password:</label>
    <input class="form-control" type="password" id="j_password" name="j_password" />
    </div>
    <div class="pull-right">
    <label for="_spring_security_remember_me" class="checkbox-inline"><input type="checkbox" id="_spring_security_remember_me" name="_spring_security_remember_me" class="field" /> Remember Me</label>
    &nbsp; <a style="vertical-align:middle;" href="<c:url value="/RemindPassword.htm"/>">Forgot Password?</a>
    </div>
  </fieldset>

  <input type="hidden" name="spring-security-redirect" value="<c:out value="${param.redirect}" />" />
  <input type="submit" value="Login" id="login-button" />
</form>
