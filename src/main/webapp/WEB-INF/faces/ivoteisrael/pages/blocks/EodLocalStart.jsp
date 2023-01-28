<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<div id="eod-form" class="wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form ">

	<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1">
      <h1>
        <c:if test="${isEod}">Election Official</c:if>
        <c:if test="${isEod and isSvid}"> &amp; </c:if>
        <c:if test="${isSvid}">State Voter Information</c:if>
        Directory
      </h1>
   </div>
  </div>
  <div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2">
    <h3>Find useful voter information including:</h3>
    <ul>
    <c:choose>
      <c:when test="${isEod}">
        <li>Local Election Office and Contact Information</li>
        <li>Address, Telephone and Email for Local Election Offices</li>
        <li>Upcoming Federal Election Dates and Deadlines</li>
        <li>Upcoming State Election Dates and Deadlines</li>
        <li>Eligibility Requirements</li>
        <li>Identification Requirements</li>
        <li>Voter Materials Transmission Options</li>
        <li>State Lookup Tools – Am I Registered? Where’s my Ballot?</li>
      </c:when>
      <c:otherwise>
        <li>Upcoming Election Dates and Deadlines</li>
        <li>Voter Materials Transmission Options</li>
        <li>Am I Registered? - and other Lookup Tools</li>
      </c:otherwise>
    </c:choose>
    </ul>
      <c:import url="/WEB-INF/${relativePath}/pages/statics/EodSelectForm.jsp">
        <c:param name="showForm" value="true" />
      </c:import>
    </div>
  </div>
	<div class="ft"></div>
</div>