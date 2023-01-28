<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<div id="eod-form" class="wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form ">
  <div class="row">
    <div class="col-12 col-md-8 offset-md-2 center-list">
       <div class="text-center">
          <h1 class="title">
              <c:choose>
                <c:when test="${isEod}">Contact an Election Official</c:when>
                <c:when test="${isSvid}">State Voting Requirements</c:when>
                <c:otherwise></c:otherwise>
              </c:choose>
          </h1>
          <h3>Find useful voter information including:</h3>
        </div>
     </div>
  </div>   
  <div class="d-flex">
      <ul class="mx-auto justify-content-center">
        <c:choose>
          <c:when test="${isEod}">
            <li>Local Election Office and Contact Information</li>
            <li>Address, Telephone and Email for Local Election Offices</li>
          </c:when>
          <c:otherwise>
            <li>Upcoming Election Dates and Deadlines</li>
            <li>Eligibility Requirements</li>
            <li>Identification Requirements</li>
            <li>Voter Materials Transmission Options</li>
            <li>State Lookup Tools – Am I Registered? Where’s my Ballot?</li>
          </c:otherwise>
        </c:choose>
      </ul>
  </div>
  <div class="row">
    <div class="col-12 col-sm-6 offset-sm-3 col-md-4 offset-md-4">
      <c:import url="/WEB-INF/${relativePath}/pages/statics/EodSelectForm.jsp">
        <c:param name="showForm" value="true" />
      </c:import>
    </div>
  </div>
</div>