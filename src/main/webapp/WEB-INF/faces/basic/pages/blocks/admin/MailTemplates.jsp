<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 30, 2008
  Time: 8:27:15 PM
  To change this template use File | Settings | File Templates.
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
    <a class="btn" href='<c:url value="/admin/EditMailTemplate.htm"/>'>Create New Mail Template</a>
    <%--<authz:authorize ifAllGranted="ROLE_ADMIN">--%>
    <table>
      <tr class="region-name">
        <th>Template name</th>
        <th>Subject</th>
      </tr>
      <c:forEach items="${allMails}" var="mail">
        <tr class="region-name">
          <td>
            <a href="<c:url value="/admin/EditMailTemplate.htm"><c:param name="templateId" value="${mail.id}"/></c:url> ">${mail.name}</a>
          </td>
          <td>${mail.subject}</td>
        </tr>
      </c:forEach>
    </table>
    <a class="btn" href='<c:url value="/admin/EditMailTemplate.htm"/>'>Create New Mail Template</a>

    <%--</authz:authorize>--%>
  </div>
</div>