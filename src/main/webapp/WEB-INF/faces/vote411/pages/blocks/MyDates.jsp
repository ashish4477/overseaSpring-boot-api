<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div id="eod-form" class="row voter-account-page wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form">
	<div class="col-12 col-sm-10 offset-sm-1">
		<h2 class="title">My Dates and Deadlines</h2>


		<c:if test="${not empty stateVoterInformation}">

			   <!-- SVID Tab panes -->
    <div class="tab-content">
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#domestic" aria-controls="domestic" role="tab" data-toggle="tab">U.S. Domestic Voters</a></li>
        <li role="presentation"><a href="#uocava" aria-controls="uocava" role="tab" data-toggle="tab">Overseas &amp; Military Voters</a></li>
    </ul>

<!-- Tab panes -->
<div class="tab-content svid">
  <div role="tabpanel" class="tab-pane fade in active" id="domestic">

    <h4>U.S. Domestic Voters</h4>

    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

      <div class="panel panel-default">
        <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#electionDates" role="tab">
          <h4 class="panel-title">
            <a role="button" class="accordion-toggle" data-toggle="collapse" href="#electionDates" aria-expanded="true" aria-controls="collapseOne">Upcoming Election Dates &amp; Deadlines</a>
          </h4>
        </div>
        <div id="electionDates" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
          <div class="panel-body svid">
                <c:if test="${empty localElections}">
                     <div class="panel-body svid markdown no-table">
                        <h4>None on Record</h4>
                    </div>
                </c:if>
              <div class="table-responsive">
                <c:forEach items="${localElections}" var="election" varStatus="inx">
                <table class="table" cellspacing="0">
                    <thead>
                    <c:if test="${empty level or election.sortOrder ne level}">
                      <c:set var="level" value="${election.sortOrder}"/>
                      <tr>
                        <th colspan="2" class="election-level" id="${level.name}"><h4>${level.name} Elections</h4></th>
                      </tr>
                    </c:if>
                    <tr>
                            <th colspan="2" class="header"><h4>${election.title} held on ${election.heldOn}</h4></th>
                        </tr>
                    </thead>
                    <tr>
                        <td><h4>New Voter Registration</h4></td>
                        <td><div>${election.domesticRegistration}</div></td>
                    </tr>
                    <tr>
                        <td><h4>Absentee Ballot Request</h4></td>
                        <td>${election.domesticBallotRequest}</td>
                    </tr>
                    <tr>
                        <td><h4>Absentee Ballot Return</h4></td>
                        <td><div>${election.domesticBallotReturn}</div></td>
                    </tr>
                    <tr>
                        <td><h4>Early Voting</h4></td>
                        <td><c:choose>
                            <c:when test="${not empty election.domesticEarlyVoting}">
                                ${election.domesticEarlyVoting}
                            </c:when>
                            <c:otherwise>none on record</c:otherwise>
                        </c:choose></td>
                    </tr>
                    <tr>
                        <td colspan="2" class="note">${election.domesticNotes}</td>
                    </tr>
                </table>
                </c:forEach>
                </div>
          </div>
        </div>
      </div>

      <div class="panel panel-default">
        <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseThree" role="tab">
          <h4 class="panel-title"><a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseThree" aria-expanded="true" aria-controls="collapseOne">Voter Materials Transmission Options</a></h4>
        </div>
        <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
          <div class="panel-body svid">
            <div class="table-responsive">
              <c:forEach items="${stateVoterInformation.transmissionMethods}" var="voterTypeTransmissionMethods">
                <%-- Domestic Only --%>
                <c:if test="${!(fn:contains(voterTypeTransmissionMethods.voterType.name, 'Military') || fn:contains(voterTypeTransmissionMethods.voterType.name, 'Overseas'))}">
                <table class="accordian-content rounded-5 trans-options" cellspacing="0">
                  <tr class="r0">
                    <td class="first-col"><h4 class="header">${voterTypeTransmissionMethods.voterType.name}</h4></td>
                    <td><strong>In-Person</strong></td>
                    <td><strong>Mail</strong></td>
                    <td><strong>Fax</strong></td>
                    <td><strong>Email</strong></td>
                    <td><strong>Online</strong></td>
                  </tr>
                  <!--${voterTypeTransmissionMethods.groupedItems}-->
                  <c:forEach items="${voterTypeTransmissionMethods.groupedItems}" var="items">
                    <tr class="r1">
                      <td class="first-col"><h4>${items.key}</h4></td>
                      <td><c:choose>
                        <c:when test="${items.value.get(\"In-Person\")}">
                          <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                      </c:choose></td>
                      <td class="odd"><c:choose>
                        <c:when test="${items.value.get(\"Mail\")}">
                        <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                      </c:choose></td>
                      <td><c:choose>
                        <c:when test="${items.value.get(\"Fax\")}">
                        <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                      </c:choose></td>
                      <td class="odd"><c:choose>
                        <c:when test="${items.value.get(\"Email\")}">
                        <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                      </c:choose></td>
                      <td><c:choose>
                        <c:when test="${items.value.get(\"Online\")}">
                        <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                      </c:choose></td>
                    </tr>
                  </c:forEach>
                  <c:if test="${not empty voterTypeTransmissionMethods.additionalInfo}">
                    <tr>
                      <td colspan="5" class="note">
                          ${voterTypeTransmissionMethods.additionalInfo}
                      </td>
                    </tr>
                  </c:if>
                </table>
            </div>
            </c:if>
            </c:forEach>
          </div>
        </div>
      </div>
    </div><!-- end Domestic Panel -->
  </div><!-- End Domestic tab -->

  <div role="tabpanel" class="tab-pane fade" id="uocava">
    <h4>Overseas &amp; Military Voters</h4>
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

      <div class="panel panel-default">
        <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseFive" role="tab">
          <h4 class="panel-title">
            <a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseFive" aria-expanded="true" aria-controls="collapseFive">Upcoming Election Dates &amp; Deadlines</a>
          </h4>
        </div>
        <div id="collapseFive" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
          <div class="panel-body svid">
            <c:if test="${empty localElections}">
                <div class="panel-body svid markdown no-table">
                    <h4>None on Record</h4>
                </div>
            </c:if>
            <c:forEach items="${localElections}" var="election" varStatus="inx">
              <div class="table-responsive">
                <table class="table" cellspacing="0">
                  <thead>
                  <c:if test="${empty level or election.sortOrder ne level}">
                    <c:set var="level" value="${election.sortOrder}"/>
                    <tr>
                      <th colspan="3" class="election-level" id="${level.name}"><h4>${level.name} Elections</h4></th>
                    </tr>
                  </c:if>
                  <tr>
                    <th class="header" colspan="3"><h4>${election.title} held on ${election.heldOn}</h4></th>
                  </tr>
                  </thead>
                  <tr>
                    <th></th>
                    <th>Overseas</th>
                    <th>Military</th>
                  </tr>
                  <tr>
                    <td><h4>Absentee Voter Registration</h4></td>
                    <td>${election.citizenRegistration}</td>
                    <td>${election.militaryRegistration}</td>
                  </tr>
                  <tr>
                    <td><h4>Ballot Request for Registered Voter</h4></td>
                    <td>${election.citizenBallotRequest}</td>
                    <td>${election.militaryBallotRequest}</td>
                  </tr>
                  <tr>
                    <td><h4>Ballot Return</h4></td>
                    <td>${election.citizenBallotReturn}</td>
                    <td>${election.militaryBallotReturn}</td>
                  </tr>
                  <tr>
                    <td colspan="3" class="note">${election.notes}</td>
                  </tr>
                </table>
              </div>
            </c:forEach>
          </div>
        </div>
      </div>

      <div class="panel panel-default">
        <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseEight" role="tab">
          <h4 class="panel-title"><a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseEight" aria-expanded="true" aria-controls="collapseOne">Voter Materials Transmission Options</a></h4>
        </div>
        <div id="collapseEight" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
          <div class="panel-body svid">
          
            <c:forEach items="${stateVoterInformation.transmissionMethods}" var="voterTypeTransmissionMethods">
              <%-- Uocava Only --%>
              <c:if test="${(fn:contains(voterTypeTransmissionMethods.voterType.name, 'Military') || fn:contains(voterTypeTransmissionMethods.voterType.name, 'Overseas'))}">
                <div class="table-responsive">
                  <table class="accordian-content rounded-5 trans-options" cellspacing="0">
                    <tr class="r0">
                      <td class="first-col"><h4 class="header">${voterTypeTransmissionMethods.voterType.name}</h4></td>
                      <td><strong>In-Person</strong></td>
                      <td><strong>Mail</strong></td>
                      <td><strong>Fax</strong></td>
                      <td><strong>Email</strong></td>
                      <td><strong>Online</strong></td>
                    </tr>
                    <c:forEach items="${voterTypeTransmissionMethods.groupedItems}" var="items">
                      <tr class="r1">
                        <td class="first-col"><h4>${items.key}</h4></td>
                        <td><c:choose>
                          <c:when test="${items.value.get(\"In-Person\")}">
                          <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                          </c:when>
                          <c:otherwise>-</c:otherwise>
                        </c:choose></td>
                        <td class="odd"><c:choose>
                          <c:when test="${items.value.get(\"Mail\")}">
                          <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                          </c:when>
                          <c:otherwise>-</c:otherwise>
                        </c:choose></td>
                        <td><c:choose>
                          <c:when test="${items.value.get(\"Fax\")}">
                          <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                          </c:when>
                          <c:otherwise>-</c:otherwise>
                        </c:choose></td>
                        <td class="odd"><c:choose>
                          <c:when test="${items.value.get(\"Email\")}">
                          <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                          </c:when>
                          <c:otherwise>-</c:otherwise>
                        </c:choose></td>
                        <td><c:choose>
                          <c:when test="${items.value.get(\"Online\")}">
                          <img class="icon-check" src="<c:url value="/img/icons/check.svg"/>"/>
                          </c:when>
                          <c:otherwise>-</c:otherwise>
                        </c:choose></td>
                      </tr>
                    </c:forEach>
                    <c:if test="${not empty voterTypeTransmissionMethods.additionalInfo}">
                      <tr>
                        <td colspan="5" class="note">
                            ${voterTypeTransmissionMethods.additionalInfo}
                        </td>
                      </tr>
                    </c:if>
                  </table>
              </c:if>
            </c:forEach>
            </div>
          </div>
        </div>
        </div>
            </div>
          </div>
  
        </div><!-- end Domestic Panel -->

      </div><!--End Uocava tab-->
    </div><!-- End all tabs -->
		</c:if>
		<p>&nbsp;</p>
	</div>

</div>