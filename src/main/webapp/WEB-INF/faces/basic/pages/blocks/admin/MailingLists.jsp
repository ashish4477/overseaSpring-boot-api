<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column-form faces list">
  <div class="hd">
    <h2>List of Mailing Lists</h2>
  </div>
  <div class="bd">
    <a class="btn" href='<c:url value="/admin/EditMailingList.htm"/>'>Create New <strong>Mailing List</strong></a>
    <table>
      <tr class="region-name">
        <th></th>
        <th>Name</th>
        <th>Subscribers</th>
        <th>Unsubscribers</th>
        <th>Bounces</th>
      </tr>

      <c:forEach items="${mailingLists}" var="mailingList" varStatus="indx">
        <tr class="region-name">
          <td>${indx.index}</td>
          <td><a title="Edit This Mailing List" href="<c:url value='/admin/EditMailingList.htm'><c:param name='id' value='${mailingList.id}'/></c:url>">${mailingList.name}</a></td>
          <td>${subscribersCount[mailingList.id]}</td>
          <td>${unsubscribersCount[mailingList.id]}</td>
          <td>${errorsCount[mailingList.id]}</td>
        </tr>
      </c:forEach>
    </table>
    <a class="btn" href='<c:url value="/admin/EditMailingList.htm"/>'>Create New <strong>Mailing List</strong></a><br/>
    <a class="btn" href="<c:url value="/admin/EditFieldType.htm?id=0&type=mail-in"/>">Create a new <strong>Field Type</strong></a><br/><br/>
    <a class="btn" href="<c:url value="/admin/MatchLostMailingAddress.htm"/>">Go to matching lost mailing address with mailing lists</a>
    <a class="btn" href="<c:url value="/admin/MailTemplatesList.htm"/>">Go to <strong>Mail Templates</strong> lists</a>
    <a class="btn" href="<c:url value="/admin/MailingTasks.htm"/>">Go to <strong>Mailing Tasks</strong> list</a>
  </div>
</div>