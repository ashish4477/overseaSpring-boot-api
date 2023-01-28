<%--
Created by IntelliJ IDEA.
User: Leo
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<div class="column-form">
  <div class="hd">
    <h2>Send Grid Log Messages</h2>
  </div>
  <div class="bd">
    <%--<a class="btn" href='<c:url value="/admin/CreateMailingTask.htm"/>'>Create New Mail Task</a>--%>
    <%--<authz:authorize ifAllGranted="ROLE_ADMIN">--%>
    <table>
      <tr class="region-name">
        <th>Date</th>
        <th>Level</th>
        <th>Message</th>
      </tr>
      <c:forEach items="${logMessages}" var="message">
        <tr class="region-name">
          <td><fmt:formatDate value="${message.logDate}" pattern="dd.MM.yyyy HH:mm:ss" /></td>
          <td>${message.logLevel}</td>
          <td>${message.message}</td>
        </tr>
      </c:forEach>
    </table>

    <%--</authz:authorize>--%>
  </div>
</div>
