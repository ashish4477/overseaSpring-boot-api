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
    <h2>List of Mail Templates</h2>
  </div>
  <div class="bd">
    <a class="btn" href='<c:url value="/admin/CreateMailingTask.htm"/>'>Create New Mail Task</a>
    <%--<authz:authorize ifAllGranted="ROLE_ADMIN">--%>
    <table>
      <tr class="region-name">
        <th>Task date</th>
        <th>Mailing list</th>
        <th>Template</th>
        <th>Subject</th>
        <th>Status</th>
      </tr>
      <c:forEach items="${mailingTasks}" var="task">
        <tr class="region-name">
          <td>
            <a href="<c:url value="/admin/CreateMailingTask.htm"><c:param name="taskId" value="${task.id}"/></c:url> "><fmt:formatDate value="${task.startOn}" pattern="MM/dd/yyyy"/></a>
          </td>
          <td>${task.mailingList.name}</td>
          <td>${task.template.name}</td>
          <td>${task.subject}</td>
          <td>${task.statusName}</td>
        </tr>
      </c:forEach>
    </table>
    <a class="btn" href='<c:url value="/admin/CreateMailingTask.htm"/>'>Create New Mail Task</a>

    <%--</authz:authorize>--%>
  </div>
</div>