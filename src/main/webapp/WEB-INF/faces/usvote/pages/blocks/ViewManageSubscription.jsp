<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="modelAttr" value="user"/>
<c:if test="${not empty param.modelAttribute}">
    <c:set var="modelAttr" value="${param.modelAttribute}"/>
</c:if>
<c:set var="action" value=""/>
<c:if test="${not empty param.action}">
    <c:set var="action" value="${param.action}"/>
</c:if>
<c:set var="fid" value="userForm"/>
<c:if test="${not empty param.id}">
    <c:set var="fid" value="${param.id}"/>
</c:if>

<div class="column">
    <div class="col-xs-12 col-sm-10 right-side vote">
        <h2>Voter Alerts Sign-up</h2>
        <div class="voter-alert manage-subsription">
            <div class="voter-alert-signup-content">
                <p>Stay up-to-date on elections in your state and on the national level. Get election reminders through the Voter Alert program from US Vote and Overseas Vote. You can be sure to never miss an election!</p>
            </div>
        </div>
        <fieldset>
            <form:form id="${fid}" modelAttribute="${modelAttr}" action="${action}" method="post">
                <div class="email-opt-cbx">
                    <div class="check"><form:radiobutton path="emailOptOut" value="false" label="Yes! Please send me Voter Alerts!"/></div>
                    <div class="check"><form:radiobutton path="emailOptOut" value="true" label="Unsubscribe me from Voter Alerts."/></div>
                </div>
                <div class="subscription-wrap">
                    <div class="subscribe-left-input">
                        <input type="submit" value="Save"/>
                    </div>
                    <!-- <div class="management-sec">
                    <small>(This will take you to our subscription management system)</small><br>
                    <small>or an interface for it?</small>
                    </div> -->
                </div>
            </form:form>
        </fieldset>
    </div>
</div>
<script>
    $(function () {
        <spring:bind path="${modelAttr}">
        <c:if test="${status.errors.errorCount > 0}">

        $('.error-message').modal('show');
        </c:if>
        </spring:bind>
    });
</script>

