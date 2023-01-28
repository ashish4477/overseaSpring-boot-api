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
    <h2>Statistics of Mailing Links</h2>
  </div>
  <div class="bd">
    <%--<a class="btn" href='<c:url value="/admin/CreateMailingTask.htm"/>'>Create New Mail Task</a>--%>
    <%--<authz:authorize ifAllGranted="ROLE_ADMIN">--%>
      <form action="<c:url value="/admin/MailingLinkStatistics.htm"/>" method="post">
        <table width="100%" cellpadding="2" cellspacing="4">
          <tr class="region-name">
            <th>
              From date
            </th>
            <td>
              <input name="fromDate" type="text" id="datepicker" value="<fmt:formatDate value="${fromTheDate}" pattern="MM/dd/yyyy"/>"/>
            </td>
            <td>
              <input type="submit" value="Show">
            </td>
          </tr>
        </table>
      </form>
    <table>
      <tr class="region-name">
        <th>Status</th>
        <th># of erros</th>
        <th>Message</th>
        <th>Count</th>
      </tr>
      <c:forEach items="${stats}" var="stat">
        <tr class="region-name">
          <td>${stat.status}</td>
          <td>${stat.errorCount}</td>
          <td>${stat.errorMessage}</td>
          <td>${stat.rowCount}</td>
        </tr>
      </c:forEach>
    </table>

    <%--</authz:authorize>--%>
      <form action="<c:url value="/admin/MailingLinkStatistics.htm"/>" method="post">
        <table width="100%" cellpadding="2" cellspacing="4">
          <tr class="region-name">
            <th>
              From date
            </th>
            <th>Old status</th>
            <th>New status</th>
            <th># of records</th>
            <th>&nbsp;</th>
          </tr>
          <tr class="region-name">
            <td>
              <fmt:formatDate value="${fromTheDate}" pattern="MM/dd/yyyy"/>
              <input name="fromDate" type="hidden" value="<fmt:formatDate value="${fromTheDate}" pattern="MM/dd/yyyy"/>"/>
            </td>
            <td>
              <select name="fromStatus">
                <c:forEach items="${linkStatuses}" var="status">
                  <option value="${status}">${status}</option>
                </c:forEach>
              </select>
            </td>
            <td>
              <select name="toStatus">
                <c:forEach items="${linkStatuses}" var="status">
                  <option value="${status}">${status}</option>
                </c:forEach>
              </select>
            </td>
            <td>
              <input type="text" name="count" value="1">
            </td>
            <td>
              <input type="submit" value="Change link status" name="changeStatus">
            </td>
          </tr>
        </table>
      </form>
  </div>
</div>

<script type="text/javascript" language="JavaScript">
  $( function() {
    $( "#datepicker" ).datepicker();
  } );
</script>
