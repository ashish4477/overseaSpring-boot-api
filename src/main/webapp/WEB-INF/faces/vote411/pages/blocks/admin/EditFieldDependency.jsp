<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 24, 2007
  Time: 8:55:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <h2>Edit Dependency</h2>
    </div>
    <div class="bd">
        <form:form action="EditFieldDependency.htm" commandName="dependency">
            <input type="hidden" value="${dependency.id}" name="dependencyId"/>

            <c:if test="${dependency.id eq 0}">
                <input type="hidden" value="${dependency.dependent.id}" name="fieldId"/>
            </c:if>

            <table>
                <tr>
                    <th colspan="2">Dependency for Question ${dependency.dependent.title} of Question Group ${dependency.dependent.question.question.name}</th>
                </tr>
                <tr>
                    <th>Select Question Group this question depends on. </th>
                    <td>
                        <form:select path="dependsOn">
                            <option>Select question group...</option>
                            <form:options items="${questions}" itemValue="id" itemLabel="name"/>
                        </form:select>

                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <a href="<c:url value="/admin/EditQuestionField.htm"><c:param name="fieldId" value="${dependency.dependent.id}"/></c:url>">Go back to Question</a>&nbsp;
                        <input type="submit" name="save" value="save"/>&nbsp;
                        <c:if test="${dependency.id ne 0}">
                            <input type="submit" name="delete" value="delete"/>
                        </c:if>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
    <div class="ft"></div>
</div>
