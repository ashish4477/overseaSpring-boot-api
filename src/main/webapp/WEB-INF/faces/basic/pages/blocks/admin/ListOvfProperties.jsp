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
    <h2>List of System Properties</h2>
  </div>
  <div class="bd">

    <table>
      <tr class="region-name">
        <th>Property Group</th>
        <th>Property Name</th>
        <th>Property Value</th>
      </tr>

      <c:set value="" var="group"/>
      <c:forEach items="${ovfProperties}" var="ovfProperty">
        <tr class="region-name" <c:if test="${group ne ovfProperty.propertyName.groupName}">style="border-top: solid black 1px"</c:if> >
          <td>
            <c:if test="${group ne ovfProperty.propertyName.groupName}">
              ${ovfProperty.propertyName.groupName}
              <c:set var="group" value="${ovfProperty.propertyName.groupName}"/>
            </c:if>&nbsp;
          </td>
          <td>
            <c:url value="/admin/EditProperty.htm" var="editUrl"><c:param name="propertyName" value="${ovfProperty.propertyName}"/></c:url>
            <a href="${editUrl}">${ovfProperty.propertyName.propertyName}</a>
          </td>
          <td><a href="${editUrl}">${ovfProperty.propertyValue}</a></td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>