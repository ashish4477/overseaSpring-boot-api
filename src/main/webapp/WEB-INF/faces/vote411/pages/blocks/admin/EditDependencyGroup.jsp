<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <h2>${title}</h2>
        <h3>${subHeader}</h3>
    </div>
    <div class="bd">
        <form:form action="EditDependencyGroup.htm" commandName="dependencyGroup">
            <input type="hidden" name="dependentId" value="${dependencyGroup.dependent.id}">

            <table>
                <c:choose>
                    <c:when test="${empty dependencyGroup.type}">
                        <tr>
                            <th>Select dependency type</th>
                            <td>
                                <form:select path="type">
                                    <form:options />
                                </form:select>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise><form:hidden path="type"/></c:otherwise>
                </c:choose>
                <c:if test="${dependencyGroup.type eq 'USER'}">
                    <tr>
                        <c:choose>
                            <c:when test="${empty dependencyGroup.fieldName}">
                                <th>Select User Field</th>
                                <td>
                                    <form:select path="fieldName" items="${userFields}"/>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <form:hidden path="fieldName"/>
                                <th>Selected User Field</th>
                                <td>${dependencyGroup.fieldName}</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:if>
                <c:if test="${dependencyGroup.type eq 'QUESTION'}">
                    <tr>
                        <c:choose>
                            <c:when test="${empty dependencyGroup.dependsOn}">
                                <th>Select Question Group this object should depend on. </th>
                                <td>
                                    <form:select path="dependsOn">
                                        <option>Select question group.</option>
                                        <form:options items="${questions}" itemValue="id" itemLabel="name"/>
                                    </form:select>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <form:hidden path="dependsOn"/>
                                <th>Selected question</th>
                                <td>${dependencyGroup.dependsOn.name}</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:if>

                <c:if test="${not empty choices}">
                    <tr>
                        <th>Choose conditions</th>
                        <td>
                            <form:checkboxes path="selectedCondition" items="${choices}" />
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="2">
                        <a href="<c:url value="${goBackUrl}"><c:param name="id" value="${dependencyGroup.dependent.id}"/></c:url>">Go back to Dependent Object</a>&nbsp;
                        <c:choose>
                            <c:when test="${not empty choices}">
                                <input type="submit" name="save" value="Save"/>&nbsp;
                            </c:when>
                            <c:otherwise>
                                <input type="submit" name="continue" value="Continue">
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
    <div class="ft"></div>
</div>
