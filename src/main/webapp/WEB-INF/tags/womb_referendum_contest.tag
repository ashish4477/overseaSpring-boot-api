<%@ tag body-content="scriptless"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>

<%@ attribute name="referendumDetails" required="true" type="java.util.Map"%>
<%@ attribute name="inlineDetails" required="true" %>
<%@ attribute name="contest" required="true" type="com.bearcode.ovf.model.vip.VipContest"%>

<fieldset class="question-group">
	<c:set var="referendum" value="${contest.ballot.referendum}" />
	<c:set var="referendumDetail" value="${referendumDetails[referendum.vipId]}" />
	<h4>
		<c:choose>
			<c:when test="${!inlineDetails && not empty referendumDetails
			&& not empty referendumDetails.conStatement && not empty referendumDetails.effectOfAbstain && not empty referendumDetails.passageThreshold && not empty referendumDetails.proStatement }">
				<a
					href='<c:url value="/WomBReferendumDetail.htm"><c:param name="referendumVipId" value="${referendum.vipId}"/></c:url>'
				><c:out value="${fn:toUpperCase(referendum.title)}" escapeXml="false" /></a>
			</c:when>
			<c:otherwise>
				<c:out value="${fn:toUpperCase(referendum.title)}" escapeXml="false" />
			</c:otherwise>
		</c:choose>
	</h4>
	<c:if test="${not empty referendum.subTitle}">
		<p>
			<i><c:out value="${referendum.subTitle}" escapeXml="false" /></i>
		</p>
	</c:if>
	<c:if test="${not empty referendum.brief}">
		<p>
			<i><c:out value="${referendum.brief}" escapeXml="false" /></i>
		</p>
	</c:if>
	<c:if test="${not empty referendum.text}">
		<p>
			<i><c:out value="${referendum.text}" escapeXml="false" /></i>
		</p>
	</c:if>

	<table  class="table">
		<th>Ballot Response</th>
		<c:forEach items="${referendum.ballotResponses}" var="ballotResponse" varStatus="referendumStatus">
			<tr>
				<td class="candidate-list"><c:out value="${ballotResponse.ballotResponse.text}" escapeXml="false" /></td>
			</tr>
		</c:forEach>
	</table>
</fieldset>