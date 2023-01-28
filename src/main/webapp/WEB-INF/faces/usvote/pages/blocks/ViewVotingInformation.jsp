<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="voter-account-page" class="voter-account-page page-form body-content content column wide-content">
    <div class="column">
        <div class="col-xs-12 col-sm-10 right-side">
            <h2>Voting Address and Information</h2>
            <span><small><a class="edit-btn" href="<c:url value="/UpdateAccount.htm"/>" tabindex="-1"
                            role="menuitem">Edit Account</a></small></span>
        </div>
    </div>
    <div class="column">
        <div class="col-xs-12 col-sm-4 voterAccount">
            <div class="voter-list">
                <h4 class="mva">Voter information</h4>
                <p><strong>Name:</strong>
                    <c:if test="${not empty user.name.firstName}">${user.name.firstName}</c:if> <c:if
                            test="${not empty user.name.middleName}">${user.name.middleName}</c:if> <c:if
                            test="${not empty user.name.lastName}">${user.name.lastName}</c:if> <c:if
                            test="${not empty user.name.suffix}">${user.name.suffix}</c:if></p>
                <p><strong>Email:</strong> ${user.username}</p>
                <%--        <br/>--%>
                <%--      </div>--%>
                <%--      <div class="col-xs-12 col-sm-6">--%>
                <c:if test="${not empty user.phone}">Phone: ${user.phone}<br/></c:if>
                <c:if test="${not empty user.alternatePhone}">Alt. Phone: ${user.alternatePhone}
                    <br/></c:if>
                <c:if test="${not empty user.alternateEmail}">Alt. Email: ${user.alternateEmail}
                    <br/></c:if>
                <%--      </div>--%>
                <%--  </div>--%>

                <%--  <div class="column">--%>
                <%--     <div class="col-xs-12 col-sm-4">--%>
            </div>
            <div class="voter-list">
            <h4 class="mva">Voter Address</h4>
            <div>${user.votingAddress.formattedAddress}</div>
            <c:if test="${not empty user.previousAddress and not user.previousAddress.emptySpace }">
                <h3>Previous Address</h3>
                ${user.previousAddress.formattedAddress}
            </c:if>
            <%--      </div>--%>
            <%--      <div class="col-xs-12 col-sm-6">--%>
            </div>
            <div class="voter-list">
                <c:if test="${not empty user.currentAddress and not user.currentAddress.emptySpace }">
                    <c:choose>
                    <c:when test="${not empty user.forwardingAddress and not user.forwardingAddress.emptySpace }">
                        <h4 class="mva">Current Address</h4>
                        ${user.forwardingAddress.formattedAddress}
                    </c:when>
                        <c:otherwise>
                            <h4 class="mva">Current Address</h4>
                            ${user.currentAddress.formattedAddress}
                        </c:otherwise>
                    </c:choose>
                </c:if>
<%--                <c:if test="${not empty user.forwardingAddress and not user.forwardingAddress.emptySpace }">--%>
<%--                    <h3>Ballot Forwarding Address</h3>--%>
<%--                    ${user.forwardingAddress.formattedAddress}--%>
<%--                </c:if>--%>
            </div>
        </div>
    </div>
</div>
