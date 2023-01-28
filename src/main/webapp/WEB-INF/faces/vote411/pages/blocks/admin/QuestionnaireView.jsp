<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="overview" style="background-color:#eee; padding:12px;">
    <div class="hd">
        <h2>${title}</h2>
    </div>
    <div class="bd">
    <div class="filters">
    <h4>Choose options to filter by:</h4>
        <form:form commandName="viewForm" action="QuestionnaireView.htm" method="get">
		<div class="all">
            <form:label path="flowType">Flow:</form:label>
            <form:select path="flowType">
                <form:options />
            </form:select>

            <form:label path="state">Voting State:</form:label>
            <form:select path="state">
                <form:options items="${states}" itemLabel="name" itemValue="abbr"/>
            </form:select>

            <form:label path="faceConfigId">Face:</form:label>
            <form:select path="faceConfigId">
                <form:options itemValue="id" itemLabel="name" items="${faceConfigs}"/>
            </form:select>
         </div>
         <div>
            <form:label path="voterType">Voter Type (absentee only):</form:label>
            <form:select path="voterType">
                <form:options itemValue="name" itemLabel="value"/>
            </form:select>
		</div>
		<div>
            <form:label path="voterHistory">Voter History (absentee only):</form:label>
            <form:select path="voterHistory">
                <form:options itemValue="name" itemLabel="value"/>
            </form:select>	
		</div>
            <input class="submit" type="submit" name="view" value="View">
        </form:form>
        </div>
        <c:forEach items="${pages}" var="page">
            <div class="view-page">
                <h4>Page ${page.number}. Title -
                    <a href="<c:url value="/admin/EditQuestionnairePage.htm"><c:param name="id" value="${page.id}"/></c:url>">
                            ${page.title}
                    </a></h4>
                <c:forEach items="${page.questions}" var="questionGroup">
                    <%-- group should contain only one variant --%>
                    <c:set var="variant" value="${questionGroup.variants[0]}"/>
                    <div class="view-question">
                        <h5>Question Group ${questionGroup.order}. Title -
                            <a href="<c:url value="/admin/EditQuestionGroup.htm"><c:param name="id" value="${questionGroup.id}"/></c:url> ">${questionGroup.name}</a>
                        </h5>
                        <c:forEach items="${questionGroup.variants}" var="variant" varStatus="ind">
                            <%--${ind.index +1}.--%>
                            <dl>
                                <dt>Variant:</dt>
                                <dd><a href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${variant.id}"/></c:url>"><strong>${variant.dependencyDescription}</strong></a></dd>
                            </dl>
                            <table>
                                <tr>
                                    <th>#</th>
                                    <th>Question name</th>
                                    <th>Required</th>
                                    <th>PDF variable</th>
                                        <%--<th>Include states</th>--%>
                                    <th>Has dependent</th>
                                    <th>Type</th>
                                </tr>
                                <c:forEach items="${variant.fields}" var="field">
                                    <tr>
                                        <td>${field.order}</td>
                                        <td><a href="<c:url value="/admin/EditQuestionField.htm"><c:param name="fieldId" value="${field.id}"/></c:url>">${field.title}</a></td>
                                        <td>${field.required?'Yes':'No'}</td>
                                        <td>${field.inPdfName}</td>
                                            <%--<td>..</td>--%>
                                        <td>
                                            <c:set var="dependents" value="${dependencies[field.id]}"/>
                                            <c:if test="${not empty dependents}">
                                                <c:forEach items="${dependents}" var="dependent">
                                                    Page ${dependent.question.page.number}.  &quot;${dependent.question.page.title}&quot;</br>
                                                    Question &quot;${dependent.question.name}&quot;<br>
                                                </c:forEach>
                                            </c:if>
                                        </td>
                                        <td>${field.type.templateName}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:forEach>
                    </div>

                </c:forEach>

            </div>
        </c:forEach>
    </div>
    <div class="ft"></div>
</div>