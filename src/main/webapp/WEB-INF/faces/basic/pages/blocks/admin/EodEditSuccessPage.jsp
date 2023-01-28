<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Jul 20, 2007
	Time: 3:44:50 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="eod-corrections" class="column-form">
	<div class="hd">
		<div class="hd-inner">
			<h2>Election Official Directory - Success!</h2>
		</div>
	</div>
	<div class="bd">
	<div class="bd-inner">
		<spring:message code="${messageCode}"/><br/><br/>
		<c:if test="${not empty leo}"><%-- leo is defined on EditLocalOfficial page --%>
			<p>
				<a href="<c:url value="/admin/EodVotingRegions.htm"><c:param name="stateId" value="${leo.region.state.id}"/><c:param name="lookFor" value="${leo.region.id}"/></c:url>">
					&larr; Regions in ${leo.region.state.name}
				</a>
			</p>
			<p>
				<a href="<c:url value="/admin/EodStates.htm"/>">&larr; States List</a>
			</p>
		</c:if>
		<c:if test="${not empty correction}">
			<a href="<c:url value="/admin/EodCorrectionsList.htm"/>">&larr; Go back to Corrections List</a>
		</c:if>
        <c:if test="${not empty svid}">
            <p>
                <a href="<c:url value="/admin/EodVotingRegions.htm"><c:param name="stateId" value="${svid.state.id}"/></c:url>">
                    &larr; Back to ${svid.state.name}
                </a>
            </p>
            <p>
                <a href="<c:url value="/admin/EodStates.htm"/>">&larr; States List</a>
            </p>
        </c:if>
        <c:if test="${not empty election}">
            <p>
                <a href="<c:url value="/admin/EodVotingRegions.htm"><c:param name="stateId" value="${election.stateInfo.state.id}"/></c:url>">
                    &larr; Back to ${election.stateInfo.state.name}
                </a>
            </p>
            <p>
                <a href="<c:url value="/admin/SvidEdit.htm"><c:param name="stateId" value="${election.stateInfo.state.id}"/></c:url>">
                    &larr; Back to ${election.stateInfo.state.name} SVID
                </a>
            </p>
            <p>
                <a href="<c:url value="/admin/EodStates.htm"/>">&larr; States List</a>
            </p>
        </c:if>
</div>
</div>
<div class="ft"></div>
</div>