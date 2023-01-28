<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <h2>Edit Question Variant</h2>
    </div>
    <div class="bd">
        <form:form commandName="variant" action="EditQuestionVariant.htm" method="post">
            <form:hidden path="id"/>
            <c:if test="${variant.id eq 0}">
                <input type="hidden" value="${variant.question.id}" name="questionId"/>
            </c:if>

            <table>
                <spring:hasBindErrors name="variant">
                    <tr>
                        <td colspan="2"><span style="color:red">${errors.globalError.defaultMessage}</span> </td>
                    </tr>
                </spring:hasBindErrors>
                <tr class="region-name">
                    <th>
                        Title<br/>
                        <span style="font-size:smaller;">(This is for internal use only.)</span>
                    </th>
                    <td>
                        <form:input path="title"/>
                        <form:errors path="title" cssStyle="color:red;" element="p"/>
                    </td>
                </tr>
                <tr>
                    <th>&nbsp;</th>
                    <td>
                        <form:select path="active">
                            <option value="true" <c:out value="${variant.active ? 'selected' : ''}" />>Active</option>
                            <option value="false" <c:out value="${not variant.active ? 'selected' : ''}" />>Hidden</option>
                        </form:select>
                    </td>
                </tr>

                <!-- Edit Fields -->
                <c:if test="${variant.id ne 0}">
                    <tr class="region-name">
                        <th rowspan="${fn:length(variant.fields) + 1}">
                            Questions
                        </th>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(variant.fields) == 0}">
                                    <a href="<c:url value="/admin/EditQuestionField.htm"><c:param name="variantId" value="${variant.id}"/></c:url>">Add Question</a>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${variant.fields}" var="field" begin="0" end="0">
                                        <a href="<c:url value="/admin/EditQuestionField.htm"><c:param name="fieldId" value="${field.id}"/></c:url>">
                                                ${field.order}. ${field.title}
                                        </a>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <c:if test="${fn:length(variant.fields) != 0}">
                        <c:forEach items="${variant.fields}" var="field" begin="1">
                            <tr>
                                <td>
                                    <a href="<c:url value="/admin/EditQuestionField.htm"><c:param name="fieldId" value="${field.id}"/></c:url>">
                                            ${field.order}. ${field.title}
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${keyQuestion}">
                                        The question goup is used in dependencies. This variant should contain only one question.
                                    </c:when>
                                    <c:otherwise>
                                        <a href="<c:url value="/admin/EditQuestionField.htm"><c:param name="variantId" value="${variant.id}"/></c:url>">Add Question</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                </c:if>

                <c:if test="${variant.id ne 0}">
                    <tr class="region-name">
                        <th>
                            Dependencies
                        </th>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(variant.keys) == 0}">
                                    <a href="<c:url value="/admin/EditDependencyGroup.htm"><c:param name="dependentId" value="${variant.id}"/></c:url>">Add Dependencies</a>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="dependencyGroup" value=""/>
                                    <c:set var="ulBody" value=""/>
                                    <ul type="disc">
                                        <c:forEach items="${variant.keys}" var="key" varStatus="keyIndx">
                                            <c:if test="${dependencyGroup ne key.dependsOnName}"> <%-- group starts --%>
                                                <c:choose>
                                                    <c:when test="${key.dependsOnName eq 'Face' }"> <%-- It's kind of trick: for FaceDependency 'dependsOnName' is always 'Face' --%>
                                                        <c:url var="editUrl" value="/admin/EditDependencyGroup.htm"><c:param name="type" value="FACE"/><c:param name="dependentId" value="${variant.id}"/></c:url>
                                                    </c:when>
                                                    <c:when test="${key.dependsOnName eq 'Flow' }"> <%-- It's kind of trick: for FlowDependency 'dependsOnName' is always 'Flow' --%>
                                                        <c:url var="editUrl" value="/admin/EditDependencyGroup.htm"><c:param name="type" value="FLOW"/><c:param name="dependentId" value="${variant.id}"/></c:url>
                                                    </c:when>
                                                    <c:when test="${fn:startsWith(key.dependsOnName, 'User field ')}"> <%-- It's kind of trick: for UserFieldDependency 'dependsOnName' always starts from 'User Field' --%>
                                                        <c:url var="editUrl" value="/admin/EditDependencyGroup.htm"><c:param name="type" value="USER"/><c:param name="dependentId" value="${variant.id}"/><c:param name="fieldName" value="${key.dependsOn.name}"/></c:url>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:url var="editUrl" value="/admin/EditDependencyGroup.htm"><c:param name="type" value="QUESTION"/><c:param name="dependentId" value="${variant.id}"/><c:param name="dependsOn" value="${key.dependsOn.id}"/></c:url>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:if test="${not keyIndx.first}"><c:set var="ulBody" value="${ulBody}</a></li>"/></c:if>
                                                <c:set var="ulBody" value="${ulBody}<li><a href='${editUrl}'>${key.dependsOnName} : ${key.conditionName}"/>
                                            </c:if>
                                            <c:if test="${dependencyGroup eq key.dependsOnName}"><c:set var="ulBody" value="${ulBody}, ${key.conditionName}"/></c:if> <%-- group continues--%>
                                            <c:if test="${dependencyGroup ne key.dependsOnName}"><c:set var="dependencyGroup" value="${key.dependsOnName}"/></c:if>
                                            <c:if test="${keyIndx.last}"><c:set var="ulBody" value="${ulBody}</a></li>"/></c:if><%-- all ends --%>
                                        </c:forEach>
                                            ${ulBody}
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <c:if test="${fn:length(variant.keys) != 0}">
                        <tr>
                            <th></th>
                            <td>
                                <a href="<c:url value="/admin/EditDependencyGroup.htm"><c:param name="dependentId" value="${variant.id}"/></c:url>">Add Dependencies</a>
                            </td>
                        </tr>
                    </c:if>
                </c:if>

                <c:if test="${not empty dependentVariants}">
                    <tr>
                        <th>Dependents Variants</th>
                        <td>
                            <ol>
                                <c:forEach items="${dependentVariants}" var="dependentVariant">
                                    <li><a href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${dependentVariant.id}"/></c:url>">${dependentVariant.title}</a></li>
                                </c:forEach>
                            </ol>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${not empty dependentInstructions}">
                    <tr>
                        <th>Dependents Instructions</th>
                        <td>
                            <ol>
                                <c:forEach items="${dependentInstructions}" var="instruction">
                                    <li><a href="<c:url value="/admin/EditInstruction.htm"><c:param name="id" value="${instruction.id}"/></c:url>">${instruction.name}</a></li>
                                </c:forEach>
                            </ol>
                        </td>
                    </tr>
                </c:if>

                <td colspan="3">
                    <a href="<c:url value="/admin/EditQuestionGroup.htm"><c:param name="id" value="${variant.question.id}"/></c:url>">Go back to Question Group</a>&nbsp;
                    <input type="submit" name="save" value="Save"/>&nbsp;
                    <c:if test="${variant.id ne 0}">
                        <input type="submit" name="delete" value="Delete" />
                        <c:if test="${!keyQuestion}">
                            <input type="submit" name="deleteHierarchy" value="Delete Hierarchy" />&nbsp;

                            <input type="button" value="Transform Checkboxes" onclick="window.location='<c:url value="/admin/CheckboxesToRadio.htm"><c:param name="variantId" value="${variant.id}"/></c:url>'; return false;">
                            <%--<a href="<c:url value="/admin/CheckboxesToRadio.htm"><c:param name="variantId" value="${variant.id}"/></c:url>">Transform Checkboxes Into Radio Buttons</a>--%>
                        </c:if>
                        <br/><br/>
                        <input type="submit" name="move" value="Move"> to
                        <select name="newQuestionId">
                            <c:forEach items="${questionsToMove}" var="question">
                                <c:if test="${question.id eq variant.question.id}">
                                    <c:set var="optSelect" value="selected='selected'"/>
                                </c:if>
                                <option value="${question.id}" ${optSelect}>${question.name}</option>
                                <c:set var="optSelect" value=""/>
                            </c:forEach>
                        </select>
                    </c:if>
                </td>
            </table>
        </form:form>
    </div>
    <div class="ft"></div>
</div>