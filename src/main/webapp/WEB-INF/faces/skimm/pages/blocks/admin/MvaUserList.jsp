<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Oct 23, 2007
  Time: 6:48:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <h2>List of Voter Accounts</h2>
    </div>
    <div class="bd">
        <p>
            <a class="btn" href="<c:url value="/admin/EditAccount.htm"/>">Create a new Account</a>
        </p>
 		<authz:authorize ifAllGranted="ROLE_USER_EXPORT">
			Export all users:
			 <c:set var="exportPages" value="${(exportCount/exportPageSize)}"></c:set>
			 <c:forEach var="i" begin="0" end="${exportPages}" step="1" varStatus ="status">
           		<a href="<c:url value="/admin/exportUsers.htm?format=csv&page=${i}"/>">CSV${i}</a>
			 </c:forEach>
 		</authz:authorize>
        <p class="pagination">
            <c:set var="beginPage" value="${userFilter.page - 5}"/>
            <c:if test="${beginPage lt 0}"><c:set var="beginPage" value="0"/> </c:if>
            <c:set var="endPage" value="${userFilter.page + 5}"/>
            <c:if test="${endPage gt (userFilter.pagesTotal-1)}"><c:set var="endPage" value="${userFilter.pagesTotal-1}"/> </c:if>

            Pages:
            <c:if test="${beginPage gt 0}">
                &nbsp;<a href="<c:url value="/admin/AccountsList.htm">
                        <c:param name="page" value="0"/>
                        <c:forEach items="${userFilter.pagingParams}" var="paramEntry">
                            <c:param name="${paramEntry.key}" value="${paramEntry.value}"/>
                        </c:forEach>
                    </c:url>">First</a>&nbsp;
            </c:if>

            <c:forEach begin="${beginPage}" end="${endPage}" var="pageNum">  <%--userFilter.pagesTotal-1--%>
                <c:choose>
                    <c:when test="${userFilter.page eq pageNum}">
                        &nbsp;<strong>${pageNum +1}</strong>&nbsp;
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

            <c:if test="${endPage lt (userFilter.pagesTotal-1)}">
                &nbsp;<a href="<c:url value="/admin/AccountsList.htm">
                        <c:param name="page" value="${userFilter.pagesTotal-1}"/>
                        <c:forEach items="${userFilter.pagingParams}" var="paramEntry">
                            <c:param name="${paramEntry.key}" value="${paramEntry.value}"/>
                        </c:forEach>
                    </c:url>">Last</a>&nbsp;
            </c:if>
        </p>
        <form:form commandName="userFilter" method="get" id="userFilterForm" >
            <table>
                <tr class="region-name">
                    <th align="center">Login / Email</th>
                    <th colspan="2"  align="center">Name</th>
                    <th  align="center">Role</th>
                </tr>
                <tr class="region-name">
                    <td>
                        <form:input path="login"/>
                    </td>
                    <td>
                        <form:input path="firstName" />
                    </td>
                    <td>
                        <form:input path="lastName"/>
                    </td>
                    <td>
                        <form:select path="roleId">
                            <form:option value="0" label="All roles"/>
                            <form:options items="${roles}" itemValue="id" itemLabel="roleName"/>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="4"><input type="submit" value="search"/></td>
                </tr>

                <c:forEach items="${userList}" var="ovfUser">
                    <tr class="region-name">
                        <td><a href="<c:url value="/admin/EditAccount.htm"><c:param name="userId" value="${ovfUser.id}"/></c:url>"><c:out value='${ovfUser.username}'/></a> </td>
                        <td colspan="2"><c:out value='${ovfUser.name.firstName}'/> <c:out value='${ovfUser.name.lastName}'/></td>
                        <td>
                            <c:forEach items="${ovfUser.roles}" var="role" varStatus="ind">
                                ${role.roleName}<c:if test="${not ind.last}">,</c:if>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </form:form>

        <p class="pagination">
            Pages:
            <c:if test="${beginPage gt 0}">
                &nbsp;<a href="<c:url value="/admin/AccountsList.htm">
                        <c:param name="page" value="0"/>
                        <c:forEach items="${userFilter.pagingParams}" var="paramEntry">
                            <c:param name="${paramEntry.key}" value="${paramEntry.value}"/>
                        </c:forEach>
                    </c:url>">First</a>&nbsp;
            </c:if>

            <c:forEach begin="${beginPage}" end="${endPage}" var="pageNum">  <%--userFilter.pagesTotal-1--%>
                <c:choose>
                    <c:when test="${userFilter.page eq pageNum}">
                        &nbsp;<strong>${pageNum +1}</strong>&nbsp;
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

            <c:if test="${endPage lt (userFilter.pagesTotal-1)}">
                &nbsp;<a href="<c:url value="/admin/AccountsList.htm">
                        <c:param name="page" value="${userFilter.pagesTotal-1}"/>
                        <c:forEach items="${userFilter.pagingParams}" var="paramEntry">
                            <c:param name="${paramEntry.key}" value="${paramEntry.value}"/>
                        </c:forEach>
                    </c:url>">Last</a>&nbsp;
            </c:if>
        </p>
        <p>
            <a class="btn" href="<c:url value="/admin/EditAccount.htm"/>">Create a new Account</a>
        </p>
    </div>
</div>