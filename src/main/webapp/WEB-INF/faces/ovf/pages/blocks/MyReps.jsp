<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 18, 2013
	Time: 4:09:39 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="voter-account-page" class="voter-account-page page-form body-content content column wide-content">
  <div class="row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
      <h2>Congressional Officials of ${votingState.name}</h2>
    <br/><br/>
      <c:if test="${empty officials}">
    <p>There are currently no officials that match your voting region. Please check your address.</p>
  </c:if>
    </div>
  </div>
  <div class="row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
  <c:if test="${not empty officials}">
          <c:forEach var="candidate" items="${officials}">
            <div class="row representatives">
            <div class="col-xs-12 col-sm-3">
              <img src="${candidate.photo}" class="pull-right">
            </div>
                <div class="col-xs-12 col-sm-8">
                  <h3> ${candidate.firstName} ${candidate.middleName} ${candidate.lastName}</h3>
                  <h4> ${candidate.office.name} - ${candidate.office.district}, ${candidate.office.parties}</h4>
                  <p><b>Next Election:</b> ${candidate.office.nextElect} </p>
                  <p><a class="button" href="<c:url value="/CandidateVoting.htm"><c:param name="candidateId">${candidate.id}</c:param></c:url>">View ${candidate.firstName} ${candidate.lastName}'s Voting History</a></p>
                 </div>
            </div>
          </c:forEach>
  </c:if>
  </div>
  </div>
</div>

