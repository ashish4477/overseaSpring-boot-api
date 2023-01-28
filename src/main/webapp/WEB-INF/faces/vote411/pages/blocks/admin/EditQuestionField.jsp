<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Aug 16, 2007
  Time: 1:57:24 PM
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
    <h2>Edit Question</h2>
</div>
<div class="bd">
<form:form action="EditQuestionField.htm" commandName="field">
<input type="hidden" value="${field.id}" name="fieldId"/>

<c:if test="${field.id eq 0}">
    <input type="hidden" value="${field.question.id}" name="variantId"/>
</c:if>

<spring:bind path="field.oldOrder">
    <input type="hidden" value="${field.order}" name="${status.expression}"/>
</spring:bind>

<table>
    <spring:hasBindErrors name="field">
        <tr>
            <td colspan="2"><span style="color:red">${errors.globalError.defaultMessage}</span> </td>
        </tr>
    </spring:hasBindErrors>
    <tr class="region-name">
        <th>
            Title <br/>
        </th>
        <td>
            <form:input path="title" />
            <form:errors path="title" />
            <span style="font-size:smaller;">This title will be shown if question group contains more than 1 question.</span>
        </td>
    </tr>

    <c:if test="${(field.id eq 0 and (fn:length(field.question.fields) gt 0)) or (field.id gt 0 and (fn:length(field.question.fields) gt 1))}">
        <tr class="region-name">
            <th>
                Insert After
            </th>
            <td>
                <form:select path="order">
                    <c:set var="positioned" value="false"/>
                    <option value="1">First field</option>
                    <c:forEach items="${field.question.fields}" var="q" varStatus="ind">
                        <c:if test="${q.id ne field.id}">
                            <c:if test="${q.order eq field.order - 1}">
                                <c:set var="selectedFlag" value="selected=\"true\"" />
                            </c:if>
                            <c:if test="${not positioned and ind.last and field.id eq 0}">
                                <c:set var="selectedFlag" value="selected=\"true\"" />
                            </c:if>
                            <option value="${q.order + 1}" ${selectedFlag}>${q.title}</option>
                        </c:if>
                    </c:forEach>
                </form:select>
            </td>
        </tr>
    </c:if>

    <tr class="region-name">
        <th>
            Type
        </th>
        <td>
            <c:choose>
                <c:when test="${usedInDependecies and not field.type.genericOptionsAllowed }">
                    <span style="color:red;">WARNING: This question is used in dependecies. Type can't be changed.</span><br/>
                    ${field.type.name}
                </c:when>
                <c:otherwise>
                    <form:errors path="type"/>
                    <form:select path="type">
                        <option value="0">Select field type...</option>
                        <form:options items="${fieldTypes}" itemLabel="name" itemValue="id"/>
                    </form:select>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr class="region-name">
        <th>
            Security option
        </th>
        <td>
            <form:checkbox path="security"/>
            The field answer won't be stored in the DB if checked
        </td>
    </tr>
    <tr class="region-name">
        <th>
            Required option
        </th>
        <td>
            <form:checkbox path="required"/>
            The field answer should be entered if checked
        </td>
    </tr>
    <c:if test="${not empty field.type and field.type.verificationPatternApplicable}" >
        <tr class="region-name">
            <th>
                Verification pattern
            </th>
            <td>
                <form:input path="verificationPattern"/>
                <p>Actually works only with "String field" type.</p>
            </td>
        </tr>
    </c:if>
    <tr class="region-name">
        <th>
            Tooltip
        </th>
        <td>
            <form:textarea path="helpText" cols="40" rows="8" />
        </td>
    </tr>
    <tr class="region-name">
        <th>
            Popup Bubble
        </th>
        <td>
            <form:textarea path="additionalHelp" cols="40" rows="8" />
        </td>
    </tr>
    <tr class="region-name">
        <th>
            Position in PDF
        </th>
        <td>
            <form:input path="inPdfName"/>
            <p style="font-size:smaller;">
                Position should be defined in form &lt;page&gt;_&lt;question group&gt;_&lt;question&gt;_&lt;position&gt;.
                The position should be defined in terms of resulting PDF form.
                For example, 1_2_a_2 means page 1, group &quot;MY INFORMATION&quot;,
                question &quot;TYPED OR PRINTED NAME&quot;, position 2 &quot;first name&quot;.
            </p>
        </td>
    </tr>
    <c:if test="${not empty field.type and field.type.templateName eq 'text'}">
        <tr class="region-name">
            <th>
                Data Role
            </th>
            <td>
                <form:select path="dataRole">
                    <form:option value="text"/>
                    <form:option value="number"/>
                    <form:option value="tel"/>
                    <form:option value="date"/>
                    <form:option value="email"/>
                </form:select>
            </td>
        </tr>
    </c:if>
    <c:if test="${not empty field.type and field.type.dependenciesAllowed}">
        <tr class="region-name">
            <th>Dependency </th>
            <td>
                <c:choose>
                    <c:when test="${not empty dependency}">
                        <a href="<c:url value="/admin/EditFieldDependency.htm"><c:param name="dependencyId" value="${dependency.id}"/></c:url>">Depends on answer to
                                ${dependency.dependsOn.name} Question Group</a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/admin/EditFieldDependency.htm"><c:param name="fieldId" value="${field.id}"/></c:url>" >Add Dependency for the Question</a>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:if>
    <c:if test="${not empty field.type and field.type.genericOptionsAllowed}">
        <tr>
            <th>
                Options
            </th>
            <td>
                <c:if test="${usedInDependecies}">
                    <span style="color:red;">WARNING: This question is used in dependecies. Be careful with options.</span><br/>
                </c:if>
                <form:errors path="genericOptions" cssStyle="color:red;"/>
                            <span id="generic-options">
                                <c:forEach items="${field.genericOptions}" var="genericOption">
                                    <c:choose>
                                        <c:when test="${genericOption.id ne 0}">
                                            <input type="hidden" name="optionIds" value="${genericOption.id}">
                                            <input type="text" name="gen${genericOption.id}" value="${genericOption.value}"><br/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" name="genOptions" value="${genericOption.value}"><br/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </span>
                <input type="text" id="add-generic-option"><input type="button" value="Add an Option" onclick="addOption();"/>
                <script type="text/javascript" language="JavaScript">
                    //<!--
                    function addOption() {
                        var elemTo = document.getElementById("generic-options");
                        var elemFrom = document.getElementById("add-generic-option");
                        if ( elemFrom != null && elemTo != null
                                & elemFrom.value.length > 0 ) {
                            elemTo.innerHTML += "<input type=\"text\" name=\"genOptions\" value=\""+ elemFrom.value +"\"><br/>";
                            elemFrom.value = "";
                        }
                    }
                    //-->
                </script>
            </td>
        </tr>
    </c:if>


    <tr>
        <td colspan="2">
            <a href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${field.question.id}"/></c:url>">Go back to Question Variant</a>&nbsp;
            <input type="submit" name="save"  value="save"/>&nbsp;
            <c:if test="${field.id ne 0 and not usedInDependecies}">
                <input type="submit" name="delete" value="delete"/>
            </c:if>
        </td>
    </tr>
    <c:if test="${field.id ne 0}">
        <tr>
            <td colspan="2">
                <input type="button" value="Add Another Question" onclick="window.location='<c:url value="/admin/EditQuestionField.htm"><c:param name="variantId" value="${field.question.id}"/></c:url>';return false;">
                <c:if test="${not empty field.type and field.type.genericOptionsAllowed}">
                    <input type="button" value="Conversion to Checkboxes" onclick="window.location='<c:url value="/admin/RadioToCheckboxes.htm"><c:param name="fieldId" value="${field.id}"/></c:url>';return false;">
                </c:if>
            </td>
        </tr>
    </c:if>
</table>
</form:form>
</div>
<div class="ft"></div>
</div>

