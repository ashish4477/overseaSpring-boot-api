<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<c:set var="onlySvid" value="${isSvid and (not isEod)}"/>

<div id="eod-form" class="wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form ">
  <div class="row">
    <div class="col-xs-12 <c:if test="${not isSvid}">col-md-8</c:if> center-list">
       <div class="text-center">
         <h1 class="title">
           <c:if test="${isEod}">Election Official Directory</c:if>
           <c:if test="${isEod and isSvid}"> &amp; </c:if>
           <c:if test="${isSvid}">State Voting Requirements &amp; Information</c:if>
         </h1>
         <c:choose>
           <c:when test="${onlySvid}">
             <div class="row">
              <div class="col-sm-12 col-md-6"><h3>Find Useful Voting Information</h3></div>
              <div class="hidden-xs hidden-sm col-md-6"><h3>Additional Resources</h3></div>
             </div>
           </c:when>
           <c:otherwise>
            <h3>Find useful voter information including:</h3>
           </c:otherwise>
         </c:choose>
        </div>
     </div>
  </div>
  <div class="row">
    <div class="col-xs-12 <c:if test="${onlySvid}">col-md-6 </c:if> center-list">
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
            <li>Eligibility Requirements</li>
            <li>Identification Requirements</li>
            <li>Voter Materials Transmission Options</li>
            <li>State Lookup Tools – Am I Registered?</li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
    <c:if test="${onlySvid}">
      <div class="hidden-xs hidden-sm col-md-6 center-list">
        <ul>
          <li><a href="/wheres-my-ballot">Where’s My Ballot? &raquo;</a></li>
          <li><a href="/ballot-return-options">Ballot Return Options &raquo;</a></li>
          <li><a href="/early-voting-dates">Early Voting Dates &raquo;</a></li>
          <li><a href="/vote/state-elections/state-voting-laws-requirements.htm">Voting Methods &raquo;</a></li>
          <li><a href="/ex-off-voting-rights">Ex-Offender Voting Rights &raquo;</a></li>
        </ul>
      </div>
    </c:if>
  </div>
  <div class="row">
    <div class="col-xs-12 <c:if test="${not (onlySvid)}">col-sm-6 col-md-4</c:if><c:if test="${onlySvid}">col-md-4</c:if>">
      <c:import url="/WEB-INF/${relativePath}/pages/statics/EodSelectForm.jsp">
        <c:param name="showForm" value="true" />
      </c:import>
    </div>
    <c:if test="${onlySvid}">
      <div class="hidden-xs hidden-sm col-md-6">&nbsp;</div>
    </c:if>
  </div>
  <c:if test="${onlySvid}">
    <div class="row mobile-exoffender">
      <div class="col-xs-12 hidden-md hidden-lg">
        <div class="text-center">
          <h3>Additional Resources</h3>
        </div>
        <div class="center-list">
          <ul>
            <li><a href="/wheres-my-ballot">Where’s My Ballot? &raquo;</a></li>
            <li><a href="/ballot-return-options">Ballot Return Options &raquo;</a></li>
            <li><a href="/early-voting-dates">Early Voting Dates &raquo;</a></li>
            <li><a href="/vote/state-elections/state-voting-laws-requirements.htm">Voting Methods &raquo;</a></li>
            <li><a href="/ex-off-voting-rights">Ex-Offender Voting Rights &raquo;</a></li>
          </ul>
        </div>
      </div>
    </div>
  </c:if>
