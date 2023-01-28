<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Jul 12, 2007
	Time: 5:32:53 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="eod-admin-page" class="column-form">
	<div class="hd">
		<h2>Election Official Directory - Admin Area</h2>
	</div>
	<div class="bd">
		<form action="<c:url value="/admin/EodCorrectionsList.htm"/>" method="get" name="EodListForm">
		<input type="hidden" name="page" value="${paging.page}" />
		<fieldset class="status-select">
			<label>
				<span>Status</span>
				<spring:bind path="paging.status">
					<select name="${status.expression}" onchange="document.EodListForm.submit();">
						<option value="1" <c:if test="${status.value eq 1}">selected="selected"</c:if> >sent</option>
						<option value="2" <c:if test="${status.value eq 2}">selected="selected"</c:if> >approved</option>
						<option value="3" <c:if test="${status.value eq 3}">selected="selected"</c:if> >rejected</option>
					</select>
				</spring:bind>
			</fieldset>
		</form>
		<table cellpadding="4" cellspacing="2" >
			<thead>
				<tr>
					<th>State</th>
					<th>Region</th>
					<th>Date</th>
					<th>Changes</th>
					<c:if test="${paging.status gt 1}">
						<th>Edited by</th>
						<th>Updated</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${correctionsList}" var="correction" varStatus="inx">
					<c:choose>
						<c:when test="${inx.index % 2 == 0}">
							<tr class="even">
						</c:when>
						<c:otherwise>
							<tr class="odd">
						</c:otherwise>
					</c:choose>
						<td>${correction.correctionFor.region.state.name}</td>
						<td><a href="<c:url value="/admin/EodCorrectionsEdit.htm"><c:param name="correctionsId">${correction.id}</c:param></c:url>">${correction.correctionFor.region.name}</a></td>
						<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${correction.created}"/></td>
						<td>
							<c:choose>
							<c:when test="${paging.status gt 1}">
								<c:choose>
									<c:when test="${correction.allCorrect eq 0}"><span class="text-danger">Changes Submitted</span></c:when>
								<c:otherwise><span class="text-success">No Changes Submitted</span></c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${correction.allCorrect eq 0}"><span class="text-danger">Changes Pending Approval</span></c:when>
								<c:otherwise><span class="text-success">Record Confirmed, No Changes</span></c:otherwise>
								</c:choose>
							</c:otherwise>
							</c:choose>
						</td>
						<c:if test="${paging.status gt 1}">
							<td>${correction.editor.username}</td>
							<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${correction.updated}"/></td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<p class="pagination">
			Pages:
			<c:forEach begin="0" end="${paging.pagesTotal-1}" var="pageNum" varStatus="status">
				<c:if test="${paging.page < 10 && status.index == 10}"><span class="more-page-links" id="more-page-links"></c:if>
				<c:choose>
					<c:when test="${paging.page eq pageNum}">
						&nbsp;${pageNum +1}&nbsp;
					</c:when>
					<c:otherwise>
						&nbsp;<a href="<c:url value="/admin/EodCorrectionsList.htm">
						    <c:param name="page" value="${pageNum}"/>
						    <c:param name="status" value="${paging.status}"/>
                        </c:url>">${pageNum +1}</a>&nbsp;
					</c:otherwise>
				</c:choose>
				<c:if test="${paging.page < 10 && status.index >= 10 && status.last}"></span class="more-page-links"><a href="javascript: void();" id="more-page-links-opener">more...</a></c:if>
			</c:forEach>
		</p>
	</div>
	<div class="ft">
	</div>
</div>

