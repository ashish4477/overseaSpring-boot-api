<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:useBean id="date" class="java.util.Date" />

<div class="row">
  <div class="col-12 col-sm-10 offset-sm-1 <c:if test="${empty primaries}">text-center</c:if>">
    <h1 class="title">Primary Election Dates <fmt:formatDate value="${date}" pattern="yyyy" /></h1>

      <table class="table table-responsive election-dates">
        <thead>
          <th><h3>State</h3></th>
          <th><h3>Date of Primary</h3></th>
          <th><h3>Deadlines</h3></th>
          <th><h3>Voting Requirements</h3></th>
        </thead>
        <tbody>
        <c:forEach items="${states}" var="state" >
          <tr>
            <td><strong>${state.name}</strong></td>
            <td>
              <c:set var="election" value="${primaries[state.abbr]}"/>
                <c:choose>
                  <c:when test="${not empty election}">${election.heldOn} - ${election.title}</c:when>
                  <c:otherwise>No Primary Information Available</c:otherwise>
                </c:choose>
            </td>
            <td style="white-space: nowrap">
              <a href="<c:url value="/state-elections/state-election-dates-deadlines.htm"><c:param name="stateName">${state.abbr}</c:param></c:url>">Check Deadlines</a>
            </td>
            <td style="white-space: nowrap">
              <a href="<c:url value="/sviddomestic.htm"><c:param name="stateId">${state.id}</c:param><c:param name="submission" value="1"/></c:url>">View State Requirements</a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
      </table>
    
  </div>
</div>