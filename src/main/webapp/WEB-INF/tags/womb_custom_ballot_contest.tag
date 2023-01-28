<%@ tag body-content="scriptless"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>

<%@ attribute name="contest" required="true" type="com.bearcode.ovf.model.vip.VipContest"%>

<fieldset class="question-group">
	<h4>
		<c:out value="${fn:toUpperCase(contest.ballot.customBallot.heading)}" escapeXml="false"/>
	</h4>

	<table class="table">
		<th>Ballot Response</th>
		<c:forEach items="${contest.ballot.customBallot.ballotResponses}" var="ballotResponse" varStatus="customBallotStatus">
			<tr>
				<td class="candidate-list"><c:out value="${ballotResponse.ballotResponse.text}" escapeXml="false"/></td>
			</tr>
		</c:forEach>
	</table>
</fieldset>