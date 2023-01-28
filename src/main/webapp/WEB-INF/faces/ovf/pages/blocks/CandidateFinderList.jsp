<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="candidate-finder-list">
	<h1 class="title">${title}</h1>
	<div class="column-right">
		<div class="your-address">
			<h2>Your Voting Address</h2>
			<c:out value="${address.address.street1}" />
			<br />
			<c:out
				value="${address.address.city}, ${address.address.state} ${address.address.zip}" /><c:if test="${not empty address.address.zip4}">-<c:out
					value="${address.address.zip4}" />
			</c:if>
			<br />
			<h2>Your Congressional District</h2>
			${address.address.state} District
			<c:out value="${address.district}" />
		</div>
		<a class="change-address"
			href="<c:url value="CandidateFinder.htm">
		<c:param name="address.street1"><c:out value="${address.address.street1}" /></c:param>
		<c:param name="address.city"><c:out value="${address.address.city}" /></c:param>
		<c:param name="address.state"><c:out value="${address.address.state}" /></c:param>
		<c:param name="address.zip"><c:out value="${address.address.zip}" /><c:if test="${not empty address.address.zip4}">-<c:out value="${address.address.zip4}"/></c:if></c:param>	
		</c:url>">Change
			Address</a>
	</div>

	<div class="column-left">
		<p>Listed below are the U.S. Congressional candidates for your
			voting address. Click on a candidate's name to see more information.
		</p>

		<h2>Candidates for U.S. Senate</h2>
		<c:choose>
			<c:when test="${not empty senateList}">
				<ul class="candidate-list">
					<c:forEach items="${senateList}" var="senate" varStatus="inx">
						<li><a
							href="<c:url value='/CandidateFinderDetail.htm'><c:param name='cid' value='${senate.candidateId}'/></c:url>"><c:out
									value="${senate.firstName}" /> <c:out
									value="${senate.lastName}" />
						</a> (<c:out value="${senate.electionParties}" />)</li>
					</c:forEach>
				</ul>
			</c:when>
			<c:when test="${not empty senateIncumbentList}">
					<h3>Incumbents</h3>
					<ul class="candidate-list">
						<c:forEach items="${senateIncumbentList}" var="senate" varStatus="inx">
							<li>
								<a href="<c:url value='/CandidateFinderDetail.htm'><c:param name='cid' value='${senate.candidateId}'/></c:url>"><c:out value="${senate.firstName}"/> <c:out value="${senate.lastName}"/></a> (<c:out value="${senate.officeParties}"/>)
							</li>
						</c:forEach>
					</ul>
			</c:when>
			<c:otherwise>
				<p>Due to either a late primary race or delayed certification of
					candidate lists, the candidate information for this state is not
					yet updated. Please check back with us again. We apologize for the
					inconvenience.</p>
				<p>
					In the meantime, you may consult the <a href="<c:url value='/sviddomestic.htm'/>">State Voter
					Information Directory</a> to identify the state website which may have
					further information.
				</p>
			</c:otherwise>
		</c:choose>

		<h2>Candidates for U.S. House</h2>
		<c:choose>
			<c:when test="${not empty representativeList}">
				<ul class="candidate-list">
					<c:forEach items="${representativeList}" var="repr" varStatus="inx">
						<li><a
							href="<c:url value='/CandidateFinderDetail.htm'><c:param name='cid' value='${repr.candidateId}'/></c:url>"><c:out
									value="${repr.firstName}" /> <c:out value="${repr.lastName}" />
						</a> (<c:out value="${repr.electionParties}" />)</li>
					</c:forEach>
				</ul>
			</c:when>
			<c:otherwise>
				<p>Due to either a late primary race or delayed certification of
					candidate lists, the candidate information for this state is not
					yet updated. Please check back with us again. We apologize for the
					inconvenience.</p>
				<p>
					In the meantime, you may consult the <a
						href="<c:url value='/sviddomestic.htm'/>">State Voter
						Information Directory</a> to identify the state website which may have
					further information.
				</p>
				<c:if test="${not empty representativeIncumbentList}">
					<h3>Incumbents</h3>
					<ul class="candidate-list">
						<c:forEach items="${representativeIncumbentList}" var="repr" varStatus="inx">
							<li>
								<a href="<c:url value='/CandidateFinderDetail.htm'><c:param name='cid' value='${repr.candidateId}'/></c:url>"><c:out value="${repr.firstName}"/> <c:out value="${repr.lastName}"/></a> (<c:out value="${repr.officeParties}"/>)
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</c:otherwise>
		</c:choose>

		<h2>Candidates for U.S. President and Vice President</h2>
	    <c:if test="${not empty presidentVicePresident}">
			<ul class="candidate-list">
				<c:forEach items="${presidentVicePresident}" var="repr" varStatus="inx">
					<li>
						<a href="<c:url value='/CandidateFinderDetail.htm'><c:param name='cid' value='${repr.candidateId}'/></c:url>"><c:out value="${repr.firstName}"/> <c:out value="${repr.lastName}"/></a>, <a href="<c:url value='/CandidateFinderDetail.htm'><c:param name='cid' value='${repr.runningMateId}'/></c:url>"><c:out value="${repr.runningMateName}"/></a>  (<c:out value="${repr.officeParties}"/>)
					</li>
				</c:forEach>
			</ul>
		</c:if>
	</div>
	<div class="back-button">
		<br/><br/>
		<a href="<c:url value="CandidateFinder.htm">
		<c:param name="address.street1"><c:out value="${address.address.street1}" /></c:param>
		<c:param name="address.city"><c:out value="${address.address.city}" /></c:param>
		<c:param name="address.state"><c:out value="${address.address.state}" /></c:param>
		<c:param name="address.zip"><c:out value="${address.address.zip}" /><c:if test="${not empty address.address.zip4}">-<c:out value="${address.address.zip4}"/></c:if></c:param>		
		</c:url>" class="wizard-button back">Back</a>
    <%--<a href="<c:url value="/CandidateFinder.htm" />" class="wizard-button back">Back</a>--%>
	</div>
</div>
