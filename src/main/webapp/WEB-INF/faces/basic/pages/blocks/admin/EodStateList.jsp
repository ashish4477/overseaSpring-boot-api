<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Jul 5, 2007
	Time: 6:00:28 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<div id="eod-admin-page" class="column-form">
	<div class="hd">
		<h2>Election Official Directory</h2>
	</div>
	<div class="bd">
		<!--
		<form action="<c:url value="/admin/EodStates.htm"/>" method="get" name="EodListForm">
			<input type="hidden" name="page" value="${paging.page}" />
		</form>
	-->
		<p class="pagination">
			Pages:
			<c:forEach begin="0" end="${paging.pagesTotal-1}" var="pageNum">
				<c:choose>
					<c:when test="${paging.page eq pageNum}">
						&nbsp;${pageNum +1}&nbsp;
					</c:when>
					<c:otherwise>
						&nbsp;<a href="<c:url value="/admin/EodStates.htm"/>?page=${pageNum}">${pageNum +1}</a>&nbsp;
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</p>
		<table>
			<tr>
				<th></th>
				<th>State</th>
				<th># of records</th>
				<th># of approved</th>
				<th></th>
                <th><a href="<c:url value="/admin/EodDataDownload.htm"><c:param name="all" value="true"/></c:url>" target="_blank">download all</a></th>
			</tr>
			<c:forEach items="${statistics}" var="stats" varStatus="inx">
				<c:choose>
					<c:when test="${inx.index % 2 == 0}">
						<tr class="even">
					</c:when>
					<c:otherwise>
						<tr class="odd">
					</c:otherwise>
				</c:choose>
					<td>${1 + inx.index + paging.page * paging.pageSize}</td>
					<td><a href="<c:url value="/admin/EodVotingRegions.htm"><c:param name="stateId" value="${stats.state.id}"/></c:url>">${stats.state.name}</a> </td>
					<td>${stats.total}</td>
					<td>${stats.approved}</td>
					<td> <a href="<c:url value="/admin/EodDataUpload.htm"><c:param name="stateId" value="${stats.state.id}"/></c:url>">go to upload</a> </td>
                    <td>&nbsp;<a href="<c:url value="/admin/EodDataDownload.htm"><c:param name="stateId" value="${stats.state.id}"/></c:url>" target="_blank">download</a> </td>
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
						&nbsp;<a href="<c:url value="/admin/EodStates.htm"/>?page=${pageNum}">${pageNum +1}</a>&nbsp;
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</p>
	</div>
	<div class="ft">
	</div>
</div>

