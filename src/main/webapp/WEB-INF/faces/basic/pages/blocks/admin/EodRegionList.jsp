<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Jul 17, 2007
	Time: 8:50:55 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" language="JavaScript">
	//<!--
	YAHOO.ovf.sendSnapshotUrl = "<c:url value="/ajax/EmailToOfficer.htm"/>";
	//-->
</script>

<div id="eod-admin-page" class="column-form">
	<div class="hd">
		<h2>${selectedState.name} - Election Official Directory</h2>
	</div>
	<div class="bd">
		<p class="navigation">
                        <a style=" float: right;" href="<c:url value='/admin/SvidEdit.htm'><c:param name="stateId" value="${selectedState.id}"/></c:url> "><button>Edit SVID</button></a>
            <a style=" float: right;" href="<c:url value='/admin/EditStateVotingLaws.htm'><c:param name="stateId" value="${selectedState.id}"/></c:url> "><button>Edit Voting Laws Info</button></a>
            <a href="<c:url value="/admin/EodStates.htm" />">&larr; Back To States List</a></p>

        <br/><button onclick="YAHOO.ovf.sendSnapshot('stateId=${paging.stateId}','leoEmailFeedback'); return false;">Send Snapshot to ALL Election Officials in ${selectedState.name}</button>
            <span id="leoEmailFeedback"></span>

 		<p class="pagination">
			Pages:
			<c:forEach begin="0" end="${paging.pagesTotal-1}" var="pageNum">
				<c:choose>
					<c:when test="${paging.page eq pageNum}">
						&nbsp;${pageNum +1}&nbsp;
					</c:when>
					<c:otherwise>
                        &nbsp;<a href="<c:url value="/admin/EodVotingRegions.htm"><c:param name="stateId" value="${paging.stateId}"/><c:param name="page" value="${pageNum}"/></c:url>">${pageNum +1}</a>&nbsp;
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</p>
		<table>
			<tr>
				<th>#</th>
				<th>Region</th>
				<th>Website</th>
				<th>status</th>
				<th>last update</th>
				<th></th>
			</tr>
			<c:forEach items="${listLeo}" var="leo" varStatus="inx">
				<c:choose>
					<c:when test="${inx.index % 2 == 0}">
						<tr class="even">
					</c:when>
					<c:otherwise>
						<tr class="odd">
					</c:otherwise>
				</c:choose>
					<td>${1 + inx.index + paging.page * paging.pageSize}</td>
					<td><a href="<c:url value="/admin/EodEdit.htm"><c:param name="regionId" value="${leo.region.id}"/> </c:url>" >${leo.region.name}</a> </td>
					<td><a href="<c:url value="${leo.website}"/>" target="_blank">${fn:substring(leo.website,0,43)}</a> </td>
					<td><c:choose><c:when test="${leo.status eq 1}">completed</c:when><c:otherwise>not completed</c:otherwise></c:choose> </td>
					<td><fmt:formatDate value="${leo.updated}" pattern="MM/dd/yyyy" /></td>
					<td></td>
				</tr>
			</c:forEach>
		</table>
		<p class="pagination">
			Pages:
			<c:forEach begin="0" end="${paging.pagesTotal-1}" var="pageNum">
				<c:choose>
					<c:when test="${paging.page eq pageNum}">
						&nbsp;${pageNum +1}&nbsp;
					</c:when>
					<c:otherwise>
						&nbsp;<a href="<c:url value="/admin/EodVotingRegions.htm"><c:param name="stateId" value="${paging.stateId}"/><c:param name="page" value="${pageNum}"/></c:url>">${pageNum +1}</a>&nbsp;
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</p>
        <form action="<c:url value="/admin/RemoveRegions.htm"/>" method="post">
            <input type="hidden" name="stateId" value="${paging.stateId}"/>
            <input type="submit" value="Remove ALL regions" onclick="return confirm('Are you sure?')">
        </form>
		<a style=" float: right;" href="<c:url value='/admin/EodEdit.htm'><c:param name="stateId" value="${selectedState.id}"/></c:url> "><button>Add new region</button></a>
	</div>
	<div class="ft">
	</div>
</div>
<form action="<c:url value="/ajax/EmailToOfficer.htm"/>" method="get" id="emptyForm" ></form>

