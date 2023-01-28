<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="column-form">
    <div class="hd">
        <h2>Edit Field Type</h2>
    </div>
    <div class="bd">
        <form:form commandName="fieldType" action="EditFieldType.htm" method="post" name="FieldTypeForm">
            <form:hidden path="id"/>
            <c:if test="${fieldType.id eq 0}"><input type="hidden" name="type" value="${param.type}"></c:if>
            <table>
                <tr class="region-name">
                    <th>
                        Name
                    </th>
                    <td>
                        <form:input path="name"/>
                        <form:errors path="name" cssStyle="color:red;"/>
                    </td>
                </tr>
                <tr class="region-name">
                    <th>
                        View template name
                    </th>
                    <td>
                        <form:select path="templateName">
                            <form:options items="${templateNames}"/>
                        </form:select>
                        <form:errors path="templateName" cssStyle="color:red;" />
                    </td>
                </tr>
                <tr class="region-name">
                    <th>
                        Use generic options
                    </th>
                    <td>
                        <form:checkbox path="genericOptionsAllowed"/>
                        <form:errors path="genericOptionsAllowed" cssStyle="color:red;" />
                    </td>
                </tr>
                <tr class="region-name">
                    <th>
                        Use verification pattern
                    </th>
                    <td>
                        <form:checkbox path="verificationPatternApplicable"/>
                        <form:errors path="verificationPatternApplicable" cssStyle="color:red;" />
                    </td>
                </tr>

                <%--<tr class="region-name">
                    <th>
                        Admin template
                    </th>
                    <td>
                        <spring:bind path="fieldType.adminTemplate">
                            <textarea rows="5" cols="15" name="adminTemplate">${fieldType.adminTemplate}</textarea>
                        </spring:bind>
                    </td>
                </tr>--%>
                <tr class="region-name">
                    <th>
                        Information
                    </th>
                    <td>
                        To create "Mailing Sign-Up" Field Type add key word "Mail-in"
                        somewhere in the name. Also use "Checkbox" or "Checkbox Filled"
                        template for that Field Type.
                    </td>
                </tr>
                <td colspan="3">
                    <input type="submit" value="save"/>
                </td>
            </table>
        </form:form>
    </div>       
    <div class="ft"></div>
</div>

