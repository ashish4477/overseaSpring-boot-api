<%@ tag body-content="scriptless"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="variant" type="com.bearcode.ovf.model.questionnaire.QuestionVariant" required="true"%>

<c:set var="single" value="${fn:length(variant.fields) == 1}" />
<c:forEach items="${variant.fields}" var="field">
	<spring:bind path="wizardContext.answersAsMap[${field.id}]">
		<c:set var="optionCheked" value="false" />
		<input type="hidden" name="fields" value="${field.id}" />
		<c:choose>
			<c:when test="${not empty field.additionalHelp}">
				<c:set var="additionalHelp" value="has-rava-bubble" />
			</c:when>
			<c:otherwise>
				<c:set var="additionalHelp" value="no-rava-bubble" />
			</c:otherwise>
		</c:choose>

		<div class="representatives-group">
			<span class="select referendum">
				<p>${field.title}</p>

				<c:forEach items="${field.options}" var="option"
					varStatus="varStat"
				>
					<div class="${(varStat.index % 2 == 0) ? 'odd' : 'even' }">
						<c:choose>
							<c:when test="${status.value.value eq option.value}">
								<input name="${field.id}" type="radio" value='<c:out value="${option.optionValue}" />' id="${variant.id}_${field.id}_${option.id}"
									checked="checked"
								> ${option.viewValue}</input>
								<c:set var="optionCheked" value="true" />
							</c:when>
							<c:otherwise>
								<input name="${field.id}" type="radio" value='<c:out value="${option.optionValue}" />' id="${variant.id}_${field.id}_${option.id}"> ${option.viewValue}</input>
							</c:otherwise>
						</c:choose>
						<fmt:message key="tag.referendum.choose" />
						</td>
					</div>
				</c:forEach>
				<c:if test="${not empty field.additionalHelp}">
					<div class="rava-bubble">${field.additionalHelp}</div>
				</c:if>
			</span>
			</table>
	</spring:bind>
</c:forEach>
