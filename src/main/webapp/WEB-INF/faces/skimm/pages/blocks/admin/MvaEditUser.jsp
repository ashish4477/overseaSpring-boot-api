<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Oct 24, 2007
  Time: 5:01:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <c:choose>
            <c:when test="${ovfUser.id ne 0}">
                <h2>Edit Voter Account</h2>
            </c:when>
            <c:otherwise>
                <h2>Create new Voter Account</h2>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="bd">
        <c:url var="formAction" value="/admin/EditAccount.htm"/>
        <form:form commandName="ovfUser" action="${formAction}" name="QuestionForm">
            <input type="hidden" value="${ovfUser.id}" name="userId"/>

            <table>
                <spring:hasBindErrors name="question">
                    <tr>
                        <td colspan="2"><span style="color:red">${errors.globalError.defaultMessage}</span> </td>
                    </tr>
                </spring:hasBindErrors>
                <tr class="region-name">
                    <th>
                        Login / Email
                    </th>
                    <td>
                        <form:errors path="username" style="color:red"/>
                        <form:input path="username" type="email"/>
                    </td>
                </tr>
                <tr class="region-name">
                    <th>
                        Change password
                    </th>
                    <td>
                        <form:errors path="password" style="color:red"/>
                        <input type="text" name="newPasswd" value=""/>
                    </td>
                </tr>
                <tr class="region-name">
                    <th>
                        First Name
                    </th>
                    <td>
                        <c:choose>
                            <c:when test="${ovfUser.id ne 0}">
                                <c:out value='${ovfUser.name.firstName}'/>
                            </c:when>
                            <c:otherwise>
                                <form:errors path="name.firstName" style="color:red"/>
                                <form:input path="name.firstName"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr class="region-name">
                    <th>
                        Last Name
                    </th>
                    <td>
                        <c:choose>
                            <c:when test="${ovfUser.id ne 0}">
                                <c:out value='${ovfUser.name.lastName}'/>
                            </c:when>
                            <c:otherwise>
                                <form:errors path="name.lastName" style="color:red"/>
                                <form:input path="name.lastName"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>

                    <tr class="region-name">
                        <th>
                            User's Role(s)
                        </th>
                        <td>
                            <c:forEach items="${roles}" var="role">
                                <c:set var="checked" value=""/>
                                <c:forEach items="${ovfUser.roles}" var="uRole" >
                                    <c:if test="${role.id eq uRole.id}"><c:set var="checked" value="checked"/></c:if>
                                </c:forEach>
                                <div>
                                	<input type="checkbox" name="selectedRoles" value="${role.id}" ${checked} id="role_${role.id}"/>
                                	<label for="role_${role.id}">${role.roleName}</label>
                                </div>
                            </c:forEach>
                        </td>
                    </tr>

                <tr >
                    <th>
                        Assigned Face
                    </th>
                    <td>
                        <select name="selectedFace">
                            <c:forEach items="${faceConfigs}" var="fConfig">
                                <option value="${fConfig.id}" <c:if test="${fConfig.id eq ovfUser.assignedFace.id}">selected="selected"</c:if> >${fConfig.urlPath}<%--${fn:substringAfter(fConfig.relativePrefix,"faces/")}--%></option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
               <tr>
                   <td colspan="3">
                       <input type="submit" name="save" value="save"/>&nbsp;
                   </td>
               </tr>
            </table>
        </form:form>

        <c:if test="${not empty pdfLog}">
            <h3>RAVA/FWAB form download history</h3>
            <table cellpadding="2">
                <c:forEach items="${pdfLog}" var="download">
                    <tr>
                        <td>${fn:toUpperCase(download.flowType)}</td>
                        <td><fmt:formatDate value="${download.creationDate}" pattern="hh:mm MM-dd-yyyy"/> </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </div>
    <div class="ft"></div>
</div>

