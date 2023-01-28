<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <h2>Manage Export Schedule</h2>
    </div>
    <div class="bd">
        <table>
            <tr>
                <th>Export Configuration</th>
                <td>${exportCofig.name}</td>
            </tr>
            <tr>
                <th>Total records found:</th>
                <td>${historyCount}</td>
            </tr>
            <c:if test="${not empty firstHistory}">
                <tr>
                    <th>First record created on:</th>
                    <td><fmt:formatDate value="${firstHistory.creationDate}" pattern="yyyy-MM-dd hh:mm:ss"/> </td>
                </tr>
            </c:if>
            <c:if test="${not empty lastHistory}">
                <tr>
                    <th>Last record created on:</th>
                    <td><fmt:formatDate value="${lastHistory.creationDate}" pattern="yyyy-MM-dd hh:mm:ss"/> </td>
                </tr>
            </c:if>
            <c:if test="${not empty lastExported}">
                <tr>
                    <th>File last exported on:</th>
                    <td><fmt:formatDate value="${lastExported.exportDate}" pattern="yyyy-MM-dd hh:mm:ss"/> </td>
                </tr>
            </c:if>
            <tr>
                <th>Records scheduled to export:</th>
                <td>${preparedCount}</td>
            </tr>
            <c:if test="${not empty firstPrepared}">
                <tr>
                    <th>Export records starting from:</th>
                    <td><fmt:formatDate value="${firstPrepared.creationDate}" pattern="yyyy-MM-dd hh:mm:ss"/> </td>
                </tr>
            </c:if>

            <form action="<c:url value="/admin/ManageExportHistory.htm"><c:param name="id" value="${exportCofig.id}"/></c:url>" method="post">
                <tr>
                    <th>Enter a date to start export from <br/> format 'year-month-day'</th>
                    <td><input name="startExportDate" type="date"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" name="save" value="Set new date for Export">
                    </td>
                </tr>
            </form>

            <tr>
                <td colspan="2">
                    <a href="<c:url value="/admin/ExportConfigurationsList.htm"/>">Go back to Configuration list</a>&nbsp;
                </td>
            </tr>
        </table>
    </div>
    <div class="ft"></div>
</div>
