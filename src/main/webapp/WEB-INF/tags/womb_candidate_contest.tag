<%@ tag body-content="scriptless"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>

<%@ attribute name="candidateBios" required="true" type="java.util.Map"%>
<%@ attribute name="inlineBios" required="true"%>
<%@ attribute name="contest" required="true"
              type="com.bearcode.ovf.model.vip.VipContest"%>
<%@ attribute name="contestOffice" required="false"%>
<%@ attribute name="partisanParty" required="true" %>

<c:set var="partyField" value="false" />
<c:forEach items="${contest.ballot.candidates}" var="ballotCandidate"
           varStatus="contestStatus">
    <c:if test="${not empty ballotCandidate.candidate.party}">
        <c:set var="partyField" value="true" />
    </c:if>
</c:forEach>

<fieldset class="question-group">
    <h4>
        <c:set var="specialType" value="" />
        <c:set var="party" value="" />
        <c:set var="primaryType" value="" />
        <c:if test="${contest.special}">
            <c:set var="specialType" value="SPECIAL " />
        </c:if>
        <c:if test="${contest.partisan}}">
            <c:set var="party" value="${fn:toUpperCase(contest.partisanParty)} " />
        </c:if>
        <c:if test="${fn:toUpperCase(contest.type) ne 'GENERAL'}">
            <c:set var="contestType" value="${fn:toUpperCase(contest.type)}" />
        </c:if>
        <c:set var="electionType" value="${specialType}${party}${primaryType}" />

        <c:set var="office" value="${fn:toUpperCase(contestOffice)}" />
        <c:if test="${empty office}">
            <c:set var="office" value="${fn:toUpperCase(contest.office)}" />
        </c:if>
        <c:set var="district" value="" />
        <!--
		<c:if test="${fn:toUpperCase(contest.electoralDistrict.type) ne 'STATEWIDE'}">
			<c:set var="district" value="${fn:toUpperCase(contest.electoralDistrict.name)}" />
		</c:if>
		-->
        <c:choose>
            <c:when
                    test="${not empty contest.numberVotingFor and contest.numberVotingFor gt 1}">
                <c:set var="numberVoting"
                       value="( Vote for up to ${contest.numberVotingFor} )" />
            </c:when>
            <c:when test="${fn:contains(office, 'PRESIDENT')}">
                <c:set var="numberVoting" value="( Vote for one team )" />
            </c:when>
            <c:otherwise>
                <c:set var="numberVoting" value="( Vote for One )" />
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty electionType}">
            <c:out value="${electionType} ELECTION" />
        </c:if>
        <c:out value="${office}" />
        <br /> <span><c:out value="${numberVoting}" /></span>
    </h4>
    <table class="table">
        <thead>
        <tr>
            <th>Candidate Name</th>
            <c:if test="${partyField}">
                <th>Political Party</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${contest.ballot.candidates}" var="ballotCandidate"
                   varStatus="contestStatus">
            <c:set var="candidate" value="${ballotCandidate.candidate}" />
            <c:set var="candidateBio" value="${candidateBios[candidate.vipId]}" />
            <c:set var="keepCandidate" value="true" />
            <c:set var="candidateParty" value="${candidate.party}" />
            <c:if test="${'PRIMARY' == fn:toUpperCase(contest.type)}">
                <c:choose>
                    <c:when test="${'NONPARTISAN' == fn:toUpperCase(candidateParty)}">
                        <c:set var="keepCandidate" value="true" />
                    </c:when>
                    <c:when test="${empty partisanParty}">
                        <c:choose>
                            <c:when test="${empty candidateParty}">
                                <c:set var="keepCandidate" value="true" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="keepCandidate" value="false" />
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <c:set var="keepCandidate" value="${fn:toUpperCase(partisanParty) == fn:toUpperCase(candidateParty)}" />
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${keepCandidate}">
                <tr>
                    <td class="candidate-list"><c:choose>
                        <c:when test="${empty candidateBio or (candidateBio.contactInformationEmpty and empty candidateBio.biography) }">
                            <c:out value="${candidate.name}" />
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${empty candidateBio.candidateUrl or candidateBio.candidateUrlEmpty}">
                                    <a
                                            href='<c:url value="/WomBCandidateBio.htm"><c:param name="candidateVipId" value="${candidate.vipId}"/></c:url>'><c:out
                                            value="${candidate.name}" /></a>
                                </c:when>
                                <c:otherwise>
                                    <a target="_blank"
                                       href='<c:url value="${candidateBio.candidateUrl}"/>'><c:out
                                            value="${candidate.name}" /></a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                        <c:if test="${(fn:contains(office, 'JUDGE') || fn:contains(office,'JUSTICE') || fn:contains(office,'COURT')) && candidate.incumbent}"> (incumbent)</c:if></td>
                    <c:if test="${partyField}">
                        <td><c:if test="${not empty candidateParty}">(<c:out
                                value="${candidateParty}" />)</c:if></td>
                    </c:if>
                </tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
</fieldset>