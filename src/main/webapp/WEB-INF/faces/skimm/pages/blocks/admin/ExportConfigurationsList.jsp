<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <h2>Registration Export Configurations</h2>
    </div>
    <div class="bd">
        <table>
            <c:forEach items="${exportConfigs}" var="config">
                <tr>
                    <th>${config.name}</th>
                    <td>
                        <a href="<c:url value="/admin/EditExportConfiguration.htm"><c:param name="id" value="${config.id}"/></c:url>">Edit Config</a>
                    </td>
                    <td>
                        <a href="<c:url value="/admin/ManageExportHistory.htm"><c:param name="id" value="${config.id}"/></c:url>">Manage Schedule</a>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="3">
                    <br/>
                    <a href="<c:url value="/admin/EditExportConfiguration.htm"/>">Create new Export Configuration</a>
                </td>
            </tr>
        </table>
    </div>
    <div class="ft"></div>
</div>
