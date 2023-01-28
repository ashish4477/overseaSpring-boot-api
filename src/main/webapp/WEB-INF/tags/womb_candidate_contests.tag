<%@ tag body-content="scriptless"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>

<%@ attribute name="candidateBios" required="true" type="java.util.Map" %>
<%@ attribute name="inlineBios" required="true" %>
<%@ attribute name="contestOffice" required="true" %>
<%@ attribute name="wombContests" required="true" type="java.util.List" %>
<%@ attribute name="reportNoContests" required="false" %>
<%@ attribute name="partisanParty" required="true" %>

<div>
	<c:choose>
		<c:when test="${not empty wombContests}">
			<c:forEach items="${wombContests}" var="contest" varStatus="contestStatus">
				<overseas:womb_candidate_contest candidateBios="${candidateBios}" inlineBios="${inlineBios}" contest="${contest}" contestOffice="${contestOffice}" partisanParty="${partisanParty}" />
			</c:forEach>
		</c:when>

		<c:when test="${empty wombContests and not empty reportNoContests}">
			<h2>Candidates for ${contestOffice}</h2>
			<p>Due to either a late primary race or delayed certification of candidate lists, the candidate information for this state is
				not yet updated. Please check back with us again. We apologize for the inconvenience.</p>
			<p>
				In the meantime, you may consult the <a href="<c:url value='/svid.htm'/>">OVF State Voter Information Directory</a> to identify
				the state website which may have further information.
			</p>
		</c:when>
	</c:choose>
</div>