<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jan 11, 2008
  Time: 5:57:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<div id="eod-admin-page" class="column-form">
	<div class="hd">
		<h2>Reporting Dashboard</h2>
	</div>
	<div class="bd">
		<table class="result">
			<tr>
				<th>&nbsp;</th>
				<th>Name</th>
				<%--<th>Description</th>--%>
				<th>&nbsp;</th>
                <th>&nbsp;</th>
                <th>&nbsp;</th>
			</tr>
			<c:forEach items="${queries}" var="theQuery" varStatus="inx">
                <c:choose>
                    <c:when test="${inx.index % 2 == 0}">
                        <c:set var="trClass" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="trClass" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <tr class="${trClass}">
					<td>${1 + inx.index }</td>
                    <td><a href="<c:url value="/report/Results.htm"><c:param name="queryId" value="${theQuery.id}"/></c:url>">${theQuery.name}</a></td>
                    <%--<td>${theQuery.description}</td>--%>
                    <td>
                        <a href="<c:url value="/report/ResultsExport.htm"><c:param name="queryId" value="${theQuery.id}"/></c:url> " target="_blank">Export</a>
                    </td>
                    <td>
                        <authz:authorize ifAllGranted="ROLE_ADMIN">
                            <a href="<c:url value="/report/EditQuery.htm"><c:param name="queryId" value="${theQuery.id}"/></c:url>">Edit</a>
                        </authz:authorize>
                        <authz:authorize ifNotGranted="ROLE_ADMIN">
                            <c:if test="${theQuery.owner ne null and theQuery.owner.id eq currentUser.id}">
                                <a href="<c:url value="/report/EditQuery.htm"><c:param name="queryId" value="${theQuery.id}"/></c:url>">Edit</a>
                            </c:if>
                        </authz:authorize> &nbsp;
                    </td>
                    <td>
					    <a href="<c:url value="/report/EditQuery.htm"><c:param name="copyId" value="${theQuery.id}"/></c:url>">Copy</a>
                    </td>
				</tr>
			</c:forEach>
            <tr>
                <td colspan="5">
                    <a href="<c:url value="/report/EditQuery.htm"></c:url>">Create new report</a>
                </td>
            </tr>
        </table>
	</div>
	<div class="ft">
	</div>
</div>

