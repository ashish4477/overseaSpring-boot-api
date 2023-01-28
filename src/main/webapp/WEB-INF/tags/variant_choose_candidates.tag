<%@ tag body-content="scriptless"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="variant"
	type="com.bearcode.ovf.model.questionnaire.QuestionVariant"
	required="true"%>

<c:set var="single" value="${fn:length(variant.fields) == 1}" />
<c:set var="partyColumn" value="false" />
<c:forEach items="${variant.fields}" var="field">
	<c:forEach items="${field.options}" var="option" varStatus="varStat">
		<c:set var="openParens" value="${fn:indexOf(option.viewValue, '{')}" />
		<c:if test="${openParens != -1}">
			<c:set var="closeParens" value="${fn:indexOf(option.viewValue, '}')}" />
			<c:set var="infoColumnsString"
				value="${fn:substring(option.viewValue, openParens + 1, closeParens)}" />
			<c:set var="infoColumns" value="${fn:split(infoColumnsString, ',')}" />
			<c:if test="${fn:length(infoColumns) > 0 and infoColumns[0] != 'N/A'}">
				<c:set var="partyColumn" value="true" />
			</c:if>
		</c:if>
	</c:forEach>
</c:forEach>

<c:forEach items="${variant.fields}" var="field">
	<spring:bind path="wizardContext.answersAsMap[${field.id}]">
		<c:set var="optionCheked" value="false" />
		<input type="hidden" name="fields" value="${field.id}" />

		<c:choose>
			<c:when test="${field.type.templateName eq 'multiple_checkboxes'}">
				<table class="representatives-group table">
					<thead>
						<tr>
							<th class="candidate-name-col" scope="col">${field.title}</th>
							<c:if test="${partyColumn}">
								<th width="15%" class="candidate-party-col" scope="col"><fmt:message
										key="tag.candidates.party_affiliation" /></th>
							</c:if>
							<th class="candidate-select-col" scope="col"><fmt:message
									key="tag.candidates.choose_candidate" /></th>
						</tr>
					</thead>
					<c:forEach items="${field.options}" var="option"
						varStatus="varStat">
						<c:set var="openParens"
							value="${fn:indexOf(option.viewValue, '{')}" />
						<c:choose>
							<c:when test="${openParens != -1}">
								<c:set var="closeParens"
									value="${fn:indexOf(option.viewValue, '}')}" />
								<c:set var="infoColumnsString"
									value="${fn:substring(option.viewValue, openParens + 1, closeParens)}" />
								<c:set var="infoColumns"
									value="${fn:split(infoColumnsString, ',')}" />
								<c:if test="${fn:length(infoColumns) > 0 and infoColumns[0] != 'N/A'}">
										<c:set var="party" value="${infoColumns[0]}"/>
								</c:if>
								<c:choose>
									<c:when
										test="${fn:length(infoColumns) == 2 && fn:length(infoColumns[1]) > 0}">
										<c:set var="incumbent" value=" (incumbent)" />
									</c:when>
									<c:otherwise>
										<c:set var="incumbent" value="" />
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:set var="party" value="" />
								<c:set var="incumbent" value="" />
							</c:otherwise>
						</c:choose>
						<c:set var="candidate"
							value="${fn:split(option.viewValue,'{')[0]}" />

						<tr class="${(varStat.index % 2 == 0) ? 'odd' : 'even' }">
							<td class="candidate-name-col">${candidate}${incumbent}</td>
							<c:if test="${partyColumn}">
								<td class="candidate-party-col">${party}</td>
							</c:if>
							<td class="candidate-select-col"><c:set var="equalCoded" value="\\u003d" /> <c:set
									var="equalStr" value="=" /> <c:set var="fieldStrValue"
									value="${fn:replace(status.value.value,equalCoded,equalStr)}" />
								<c:choose>
									<c:when test="${fn:contains(fieldStrValue,option.optionValue)}">
										<input name="${field.id}" type="checkbox"
											value='<c:out value="${option.optionValue}" />'
											id="${variant.id}_${field.id}_${option.id}" checked="checked" />
										<c:set var="optionCheked" value="true" />
									</c:when>
									<c:otherwise>
										<input name="${field.id}" type="checkbox"
											value='<c:out value="${option.optionValue}" />'
											id="${variant.id}_${field.id}_${option.id}" />
									</c:otherwise>
								</c:choose> <fmt:message key="tag.candidates.choose" /></td>
						</tr>
					</c:forEach>
					<c:if test="${status.error}">
						<p class="error">
							<c:out value="${status.errorMessage}" />
						</p>
						<c:set var="error" value="error-indicator" />
					</c:if>
				</table>
			</c:when>
			<c:otherwise>
				<table class="representatives-group">
					<thead>
						<tr>
							<th class="candidate-name-col" scope="col">${field.title}</th>
							<c:if test="${partyColumn}">
								<th class="candidate-party-col" scope="col"><fmt:message
										key="tag.candidates.party_affiliation" /></th>
							</c:if>
							<th class="candidate-select-col" scope="col"><fmt:message
									key="tag.candidates.choose_candidate" /></th>
						</tr>
					</thead>
					<c:forEach items="${field.options}" var="option"
						varStatus="varStat">
						<c:set var="openParens"
							value="${fn:indexOf(option.viewValue, '{')}" />
						<c:choose>
							<c:when test="${openParens != -1}">
								<c:set var="closeParens"
									value="${fn:indexOf(option.viewValue, '}')}" />
								<c:set var="infoColumnsString"
									value="${fn:substring(option.viewValue, openParens + 1, closeParens)}" />
								<c:set var="infoColumns"
									value="${fn:split(infoColumnsString, ',')}" />
								<c:set var="incumbentIndex" value="1" />
								<c:if test="${fn:length(infoColumns) > 0 and infoColumns[0] != 'N/A'}">
										<c:set var="party" value="${infoColumns[0]}"/>
								</c:if>
								<c:choose>
									<c:when
										test="${fn:length(infoColumns) == 2 && fn:length(infoColumns[1]) > 0}">
										<c:set var="incumbent" value=" (incumbent)" />
									</c:when>
									<c:otherwise>
										<c:set var="incumbent" value="" />
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:set var="party" value="" />
								<c:set var="incumbent" value="" />
							</c:otherwise>
						</c:choose>
						<c:set var="candidate"
							value="${fn:split(option.viewValue,'{')[0]}" />

						<tr class="${(varStat.index % 2 == 0) ? 'odd' : 'even' }">
							<td class="candidate-name-col">${candidate}${incumbent}</td>
							<c:if test="${partyColumn}">
								<td class="candidate-party-col">${party}</td>
							</c:if>
							<td class="candidate-select-col"><c:choose>
									<c:when test="${status.value.value eq option.value}">
										<input name="${field.id}" type="radio"
											value='<c:out value="${option.optionValue}" />'
											id="${variant.id}_${field.id}_${option.id}" checked="checked" />
										<c:set var="optionCheked" value="true" />
									</c:when>
									<c:otherwise>
										<input name="${field.id}" type="radio"
											value='<c:out value="${option.optionValue}" />'
											id="${variant.id}_${field.id}_${option.id}" />
									</c:otherwise>
								</c:choose> <fmt:message key="tag.candidates.choose" /></td>
						</tr>
					</c:forEach>
					<c:if test="${fn:length(field.options) > 0}">
						<tr
							class="${(fn:length(field.options) % 2 == 0) ? 'odd' : 'even' }">
							<td><fmt:message key="tag.candidates.leave_blank" /></td>
							<c:if test="${partyColumn}">
								<td></td>
							</c:if>
							<td><c:choose>
									<c:when test="${not optionCheked}">
										<input name="${field.id}" type="radio" value=""
											id="${variant.id}_${field.id}_${option.id}" checked="checked" />
									</c:when>
									<c:otherwise>
										<input name="${field.id}" type="radio" value=""
											id="${variant.id}_${field.id}_${option.id}" />
									</c:otherwise>
								</c:choose> <fmt:message key="tag.candidates.choose_none" /></td>
						</tr>
					</c:if>
				</table>
			</c:otherwise>
		</c:choose>
	</spring:bind>
</c:forEach>
