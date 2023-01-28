<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="column-form">
    <div class="hd">
        <h2>Edit Question Group</h2>
    </div>
    <div class="bd">
        <form:form commandName="question" action="EditQuestionGroup.htm" method="post" name="QuestionForm">
            <form:hidden path="id"/>

            <c:if test="${question.id eq 0}">
                <input type="hidden" value="${question.page.id}" name="pageId"/>
            </c:if>

            <spring:bind path="question.oldOrder">
                <input type="hidden" value="${question.order}" name="${status.expression}"/>
            </spring:bind>

            <table>
                <spring:hasBindErrors name="question">
                    <tr>
                        <td colspan="2"><span style="color:red">${errors.globalError.defaultMessage}</span> </td>
                    </tr>
                </spring:hasBindErrors>
                <tr class="region-name">
                    <th>
                        Name
                    </th>
                    <td>
                        <form:input path="name"/>
                        <form:errors path="name" cssStyle="color:red;" element="p"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span style="font-size:smaller;">This is a short name, it's used for identifying the question group in administration interface.</span>
                    </td>
                </tr>
                <tr class="region-name">
                    <th>Title</th>
                    <td>
                        <form:input path="title"/>
                        <form:errors path="title" cssStyle="color:red;" element="p"/>
                    </td>
                </tr>
                <tr><td colspan="2"><span style="font-size:smaller;">This is a full name, it's used in user interface.</span></td></tr>
                <tr><td colspan="2">Decorations:</td></tr>
                <tr class="region-name">
                    <th>HTML class for field set</th>
                    <td>
                        <form:input path="htmlClassFieldset"/>
                        <form:errors path="htmlClassFieldset" cssStyle="color:red;" element="p"/>
                    </td>
                </tr>
                <tr class="region-name">
                    <th>HTML class for options</th>
                    <td>
                        <form:input path="htmlClassOption"/>
                        <form:errors path="htmlClassOption" cssStyle="color:red;" element="p"/>
                    </td>
                </tr>
                <tr><td colspan="2">
                    <span style="font-size:smaller;">
                        Each option of radio button question field in this group will be marked with two styles:
                        <em>&lt;this_class&gt;</em> and <em>&lt;this_class&gt;_&lt;option_number&gt;</em>.
                        First one is used to customize all options, second one - to customize each option individually.<br/>
                        Options are alphabetically ordered and <em>&lt;option_number&gt;</em> starts from 1.
                    </span>
                </td></tr>

                <c:if test="${(question.id eq 0 and (fn:length(question.page.questions) gt 0)) or (question.id gt 0 and (fn:length(question.page.questions) gt 1))}">
                    <tr class="region-name">
                        <th>
                            Insert After
                        </th>
                        <td>
                            <spring:bind path="question.order">
                                <select name="${status.expression}">
                                    <option value="1">First question</option>
                                    <c:forEach items="${question.page.questions}" var="q" varStatus="vs">
                                        <c:if test="${q.id ne question.id}">
                                            <option value="${q.order + 1}"
                                                <c:if test="${(q.order eq question.order - 1) or (question.id == 0 and vs.last)}">
                                                    selected="true"
                                                </c:if>
                                                >${q.name}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </spring:bind>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${question.id ne 0}">
                    <tr class="region-name">
                        <th rowspan="${fn:length(question.variants) + 1}">
                            Question Group Variants
                        </th>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(question.variants) == 0}">
                                    <a href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="questionId" value="${question.id}"/></c:url>">Add Question Variant</a>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${question.variants}" var="variant" begin="0" end="0">
                                        <dl>
                                            <dt>
                                                <a href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${variant.id}"/></c:url>">${variant.title}</a> <span class="text-danger"> <c:out value="${variant.active ? '' : '(hidden)'}"/></span>
                                            </dt>
                                            <dd>
                                                <c:choose>
                                                    <c:when test="${keyQuestion}">(${variant.dependencyDescription})</c:when>
                                                    <c:otherwise>
                                                        <dl>
                                                            <dt>(${variant.dependencyDescription})</dt>
                                                            <dd>
                                                                <a
                                                                    href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="cloneVariant" value="cloneVariant"/><c:param name="id" value="${variant.id}"/></c:url>"
                                                                    >Clone</a>
                                                            </dd>
                                                        </dl>
                                                    </c:otherwise>
                                                </c:choose>
                                            </dd>
                                        </dl>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <c:if test="${fn:length(question.variants) != 0}">
                        <c:forEach items="${question.variants}" var="variant" begin="1">
                            <tr>
                                <td>
                                    <dl>
                                        <dt>
                                            <a href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${variant.id}"/></c:url>">${variant.title}</a> <span class="text-danger"> <c:out value="${variant.active ? '' : '(hidden)'}"/></span>
                                        </dt>
                                        <dd>
                                            <c:choose>
                                                <c:when test="${keyQuestion}">(${variant.dependencyDescription})</c:when>
                                                <c:otherwise>
                                                    <dl>
                                                        <dt>(${variant.dependencyDescription})</dt>
                                                        <dd>
                                                            <a
                                                                href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="cloneVariant" value="cloneVariant"/><c:param name="id" value="${variant.id}"/></c:url>"
                                                                >Clone</a>
                                                        </dd>
                                                    </dl>
                                                </c:otherwise>
                                            </c:choose>
                                        </dd>
                                    </dl>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${keyQuestion}">
                                        This question group is used in dependencies. It should contain only one variant.
                                    </c:when>
                                    <c:otherwise>
                                        <a href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="questionId" value="${question.id}"/></c:url>">Add Question Variant</a>
                                    </c:otherwise>
                                </c:choose>
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
                <tr>
                    <td colspan="3">
                        <a class="back btn" href="<c:url value="/admin/EditQuestionnairePage.htm"><c:param name="id" value="${question.page.id}"/></c:url>">Go back to Page</a>&nbsp;
                        <input type="submit" name="save" value="Save"/>&nbsp;
                        <c:if test="${question.id ne 0}">
                            <input type="submit" name="delete" value="Delete"/>
                            <c:if test="${!keyQuestion}">
                                <input type="submit" name="deleteHierarchy" value="Delete Hierarchy"/>
                            </c:if>
                            <br/><br/>
                            <input type="submit" name="move" value="Move"> to
                            <select name="newPageId">
                                <c:forEach items="${pages}" var="page">
                                    <c:if test="${page.id eq question.page.id}">
                                        <c:set var="optSelect" value="selected='selected'"/>
                                    </c:if>
                                    <option value="${page.id}" ${optSelect}>${page.title}</option>
                                    <c:set var="optSelect" value=""/>
                                </c:forEach>
                            </select>
                        </c:if>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
    <div class="ft"></div>
</div>

