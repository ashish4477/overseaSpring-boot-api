<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Nov 12, 2007
  Time: 9:29:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column-form faces list">
    <div class="hd">
        <h2>List of Faces Configurations</h2>
    </div>
    <div class="bd">
        <%--<p class="pagination">
            Pages:
            <c:forEach begin="0" end="${userFilter.pagesTotal-1}" var="pageNum">
                <c:choose>
                    <c:when test="${userFilter.page eq pageNum}">
                        &nbsp;${pageNum +1}&nbsp;
                    </c:when>
                    <c:otherwise>
                        &nbsp;<a href="<c:url value="/admin/AccountsList.htm">
                        <c:param name="page" value="${pageNum}"/>
                        <c:forEach items="${userFilter.pagingParams}" var="paramEntry">
                            <c:param name="${paramEntry.key}" value="${paramEntry.value}"/>
                        </c:forEach>
                    </c:url>">${pageNum +1}</a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </p>--%>
        <%--<form action="<c:url value="/admin/FacesConfigsList.htm"/>" method="get" >--%>
<a class="btn" href="<c:url value='/admin/EditFaceConfig.htm'/>">Create New Configuration</a>
            <table>
                <tr class="region-name">
                    <th>URL and Context Path</th>
                    <th>Internal Path</th>
                    <th>URL and Context Path</th>
                    <th>Instructions</th>
                </tr>
                
                <c:forEach items="${faceConfigs}" var="faceConfig">
                    <tr class="region-name">
                        <td><a title="Edit This Face" href="<c:url value='/admin/EditFaceConfig.htm'><c:param name="configId" value="${faceConfig.id}"/></c:url>">${faceConfig.name} <c:if test="${faceConfig.name == 'Default'}">(Main OVF Site)</c:if><c:if test="${faceConfig.defaultPath}">(Default Template)</c:if></a></td>
                        <td>${faceConfig.relativePrefix} </td>
                        <td><a href="http://${faceConfig.urlPath}" target="_blank">${faceConfig.urlPath}</a></td>
                        <td>
                            <c:if test="${not empty faceInstructions[faceConfig.id]}">Yes</c:if>
                            <%--<c:forEach items="${faceInstructions[faceConfig.id]}" var="instruction">
                                <a href="<c:url value='/admin/EditFaceFlowInstruction.htm'><c:param name="id" value="${instruction.id}"/></c:url>">${instruction.flowTypeName}</a>
                            </c:forEach>--%>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        <!--</form>-->

        <%--<p class="pagination">
            Pages:
            <c:forEach begin="0" end="${userFilter.pagesTotal-1}" var="pageNum">
                <c:choose>
                    <c:when test="${userFilter.page eq pageNum}">
                        &nbsp;${pageNum +1}&nbsp;
                    </c:when>
                    <c:otherwise>
                        &nbsp;<a href="<c:url value="/admin/AccountsList.htm">
                        <c:param name="page" value="${pageNum}"/>
                        <c:forEach items="${userFilter.pagingParams}" var="paramEntry">
                            <c:param name="${paramEntry.key}" value="${paramEntry.value}"/>
                        </c:forEach>
                    </c:url>">${pageNum +1}</a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </p>--%>
    </div>
</div>