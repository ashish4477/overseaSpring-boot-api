<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jan 14, 2008
  Time: 6:12:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<div id="eod-admin-page" class="column-form">
	<div class="hd">
		<h2>Results</h2>
	</div>
	<div class="bd">
        <a href="<c:url value="/report/ReportQueryList.htm"/>">Back to Report List</a> &nbsp; &nbsp;
        <a href="<c:url value="/report/ResultsExport.htm"><c:param name="queryId" value="${query.id}"/></c:url> " target="_blank">Export to Excel</a> &nbsp; &nbsp;
        <a href="<c:url value="/report/EditQuery.htm"><c:param name="queryId" value="${query.id}"/></c:url> ">Edit</a>
        <table class="result">
            <tr>
                <authz:authorize ifAllGranted="ROLE_ADMIN">
                    <c:if test="${query.applyFaces}">
                        <th class="leftAlign">Face name</th>
                    </c:if>
                </authz:authorize>
                <c:forEach items="${query.elements}" var="element">
                    <th class="leftAlign">${fn:join(element.titles, ",")}</th>
                </c:forEach>
                <th class="leftAlign"># of voters</th>
                <th class="leftAlign">percents</th>
            </tr>
            <c:forEach items="${results}" var="row" varStatus="inx" >
                <c:choose>
                    <c:when test="${inx.index % 2 == 0}">
                        <c:set var="trClass" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="trClass" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <tr class="${trClass}">
                    <c:forEach items="${row}" var="elem" varStatus="elemInx">
                        <authz:authorize ifAllGranted="ROLE_ADMIN">
                            <c:if test="${not elemInx.last }" >
                                <td>&nbsp;${elem}&nbsp;</td>
                            </c:if>
                        </authz:authorize>
                        <authz:authorize ifNotGranted="ROLE_ADMIN">
                            <c:if test="${not elemInx.last and not elemInx.first }" >
                                <td>&nbsp;${elem}&nbsp;</td>
                            </c:if>
                        </authz:authorize>
                        <c:if test="${elemInx.last}">
                            <td  class="rightAlign">
                                <fmt:formatNumber value="${elem}" pattern="0" />
                            </td>
                            <td class="rightAlign">
                                <fmt:formatNumber value="${100.0*elem/total}" pattern="0.000" />%
                            </td>
                        </c:if>
                    </c:forEach>
                </tr>
            </c:forEach>
            <c:choose>
                <c:when test="${trClass eq 'odd'}">
                    <c:set var="trClass" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="trClass" value="odd"/>
                </c:otherwise>
            </c:choose>
            <tr class="${trClass}">
                <c:if test="${query.applyFaces}"><c:set var="offset" value="1"/></c:if>
                <authz:authorize ifNotGranted="ROLE_ADMIN">
                    <c:set var="offset" value="0"/>
                </authz:authorize>
                <th colspan="${fn:length(query.elements)+offset}">Total:</th>
                <td class="rightAlign"><fmt:formatNumber value="${total}" pattern="0" /></td>
                <td>&nbsp;</td>
            </tr>
        </table>
        <a href="<c:url value="/report/ReportQueryList.htm"/>">Back to Report List</a> &nbsp; &nbsp;
        <a href="<c:url value="/report/ResultsExport.htm"><c:param name="queryId" value="${query.id}"/></c:url> " target="_blank">Export to Excel</a> &nbsp; &nbsp;
        <a href="<c:url value="/report/EditQuery.htm"><c:param name="queryId" value="${query.id}"/></c:url> ">Edit</a>

    </div>
    <div class="ft">
    </div>
</div>
