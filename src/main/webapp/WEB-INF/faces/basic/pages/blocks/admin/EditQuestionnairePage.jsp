<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="column-form" id="rava-edit-question">
    <div class="hd">
        <h2>Edit Questionnaire Page</h2>
    </div>
    <div class="bd">
        <c:url var="actionUrl" value="/admin/EditQuestionnairePage.htm"/>
        <form:form action="${actionUrl}" commandName="questionaryPage">
            <form:hidden path="id" />
            <form:hidden path="type"/>

            <spring:bind path="questionaryPage.oldNumber">
                <input type="hidden" value="${questionaryPage.number}" name="oldNumber"/>
            </spring:bind>

            <spring:hasBindErrors name="questionaryPage">
                <p class="error">${errors.globalError.defaultMessage}</p>
            </spring:hasBindErrors>

            <table>
                <c:if test="${questionaryPage.id eq 0}"> <%-- new page. allowed to change class type--%>
                    <tr>
                        <th><label for="classType">Select Page Class</label></th>
                        <td>
                            <select name="classType" id="classType">
                                <c:forEach items="${pageClasses}" var="pC">
                                    <c:choose>
                                        <c:when test="${pageClass eq pC}">
                                            <option value="${pC}" selected="selected">${pC.description}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${pC}" >${pC.description}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <th><label for="title">Name</label></th>
                    <td>
                        <form:input path="title" cssClass="textfield" id="title"/>
                        <form:errors path="title" cssClass="error"/>
                    </td>
                </tr>
                <tr>
                    <th><label for="stepNumber">Step Number</label></th>
                    <td>
                        <form:input path="stepNumber" cssClass="textfield" id="stepNumber"/>
                        <form:errors path="stepNumber" cssClass="error"/>
                    </td>
                </tr>
                <tr>
                    <c:choose>
                        <c:when test="${pageClass eq 'GENERAL'}">
                            <th><form:label path="popupBubble">Popup Bubble</form:label></th>
                            <td>
                                <form:textarea path="popupBubble" rows="10" cols="50"/>
                                <form:errors path="popupBubble" cssClass="error"/>
                            </td>
                        </c:when>
                        <c:when test="${pageClass eq 'ADD_ON'}">
                            <th><form:label path="beanName">Select add-on</form:label></th>
                            <td>
                                <form:select path="beanName" >
                                    <form:options items="${additionalBehavior}"/>
                                </form:select>
                                <%--<form:errors path="popupBubble" cssClass="error"/>--%>
                            </td>
                        </c:when>
                        <c:when test="${pageClass eq 'EXTERNAL'}">
                            <th><form:label path="externalLink">Select external url</form:label></th>
                            <td>
                                <form:select path="externalLink">
                                    <form:options items="${additionalBehavior}"/>
                                </form:select>
                                <%--<form:errors path="popupBubble" cssClass="error"/>--%>
                            </td>
                        </c:when>
                    </c:choose>
                </tr>
                <c:if test="${(questionaryPage.id eq 0 and (fn:length(pages) gt 0)) or (questionaryPage.id gt 0 and (fn:length(pages) gt 1))}">
                    <tr>
                        <th><label for="number">Insert :</label></th>
                        <td>
                            <form:select path="number" id="number">
                                <c:if test="${minNumber le 0}">
                                    <option value="1">at first place</option>
                                </c:if>
                                <c:forEach items="${pages}" var="page" varStatus="vs">
                                    <c:set var="optSelect" value=""/>
                                    <c:if test="${page.id ne questionaryPage.id}">
                                        <c:if test="${(page.number eq (questionaryPage.number - 1)) or (questionaryPage.id eq 0 and vs.last)}">
                                            <c:set var="optSelect" value="selected='selected'"/>
                                        </c:if>
                                        <option value="${page.number + 1}" ${optSelect}>after ${page.title}</option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${questionaryPage.id ne 0 and pageClass ne 'EXTERNAL'}">
                    <tr>
                        <th>Question Groups</th>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(questionaryPage.questions) == 0}">
                                    <div>None in the list.</div>
                                </c:when>
                                <c:otherwise>
                                    <ol>
                                        <c:forEach items="${questionaryPage.questions}" var="question">
                                            <li><a href="<c:url value="/admin/EditQuestionGroup.htm"><c:param name="id" value="${question.id}"/></c:url>">
                                                    ${question.name}
                                            </a></li>
                                        </c:forEach>
                                    </ol>
                                </c:otherwise>
                            </c:choose>
                            <div><a href="<c:url value="/admin/EditQuestionGroup.htm"><c:param name="pageId" value="${questionaryPage.id}"/></c:url>">
                                Add Question Group
                            </a></div>
                        </td>
                    </tr>
                </c:if>
                
                <!--
                <c:if test="${not empty dependentVariants}">
                	<tr>
                		<th>Dependents</th>
    	            	<td>
                            <ol>
 	                			<c:forEach items="${dependentVariants}" var="dependentVariant">
     	            				<li><a href="<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${dependentVariant.id}"/></c:url>">${dependentVariant.title}</a></li>
		            	    	</c:forEach>
		            	    </ol>
    	            	</td>
        	        </tr>
            	 </c:if>
            	 -->
            </table>


            <p class="back">
                <a href="<c:url value="/admin/QuestionnairePages.htm"/>">&larr; Page List</a>&nbsp;
            </p>
            <input type="submit" name="save" class="button" value="Save"/>
            <c:if test="${questionaryPage.id ne 0}">
                <input type="submit" name="delete" class="button" value="Delete"/>
                <c:if test="${empty dependentVariants}">
	                <input type="submit" name="deleteHierarchy" class="button" value="Delete Hierarchy" />
	            </c:if>
            </c:if>
        </form:form>
    </div>
    <div class="ft"></div>

</div>
