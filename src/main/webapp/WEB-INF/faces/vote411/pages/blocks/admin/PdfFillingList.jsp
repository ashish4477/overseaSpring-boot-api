<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 18, 2007
  Time: 6:51:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column">
	<div class="hd">
		<h2>Voter Instructions</h2>
	</div>
	<div class="bd">
		<table class="questionary-pages-list" cellpadding="2" cellspacing="2">
            <tr>
                <th width="25%">Name</th>
                <th width="10%">Variable</th>
                <th width="65%">Dependencies</th>
            </tr>
            <c:forEach items="${fillings}" var="filling">
                <tr>
                    <td>
                        <a href="<c:url value="/admin/EditInstruction.htm"><c:param name="id" value="${filling.id}"/></c:url>">
                                ${filling.name}
                        </a>
                    </td>
                    <td>${filling.inPdfName} <c:if test="${fn:contains(filling.text, '#if')}">*</c:if> </td>
                    <td>
                            ${filling.dependencyDescription}
                    </td>
                </tr>
            </c:forEach>
        </table>
		<p><a href="<c:url value="/admin/EditInstruction.htm"/>">
			Create new Instruction
		</a></p>
        <p class="pagination">
            Pages:
            <c:forEach begin="0" end="${paging.pagesTotal-1}" var="pageNum">
                <c:choose>
                    <c:when test="${paging.page eq pageNum}">
                        &nbsp;${pageNum +1}&nbsp;
                    </c:when>
                    <c:otherwise>
                        &nbsp;<a href="<c:url value="/admin/InstructionsList.htm"><c:param name="page" value="${pageNum}"/></c:url>">${pageNum +1}</a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </p>
	</div>
	<div class="ft"></div>
</div>