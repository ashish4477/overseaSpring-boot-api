<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<% pageContext.setAttribute("newLineChar", "\n"); %>--%>

<div id="eod-form" class="row voter-account-page wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form">
    <div class="col-xs-12 right-side">
        <h2 class="title">Upcoming Elections</h2>


        <c:if test="${not empty stateVoterInformation}">

        <!-- SVID Tab panes -->
        <div class="tab-content">
            <ul class="nav nav-pills nav-fill" role="tablist">
                <c:choose>
                    <c:when test="${param.contentType == 'mobile'}">
                        <li role="presentation" class="active nav-item"><a href="#domestic-mobile" aria-controls="domestic-mobile" role="tab" data-bs-toggle="tab">U.S. Domestic<br/> Voters</a></li>
                        <li role="presentation" class="nav-item"><a href="#uocava-mobile" aria-controls="uocava-mobile" role="tab" data-bs-toggle="tab">Overseas &amp; Military<br/> Voters</a></li>
                    </c:when>
                    <c:otherwise>
                        <li role="presentation" class="active nav-item"><a href="#domestic-desktop" aria-controls="domestic-desktop" role="tab" data-bs-toggle="tab">U.S. Domestic Voters</a></li>
                        <li role="presentation" class="nav-item"><a href="#uocava-desktop" aria-controls="uocava-desktop" role="tab" data-bs-toggle="tab">Overseas &amp; Military Voters</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content svid">
                <div role="tabpanel" class="tab-pane fade in show active" id="domestic-<c:out value='${param.contentType}' />">

                    <h4>U.S. Domestic Voters</h4>

                    <div class="panel-group" id="accordion1" role="tablist" aria-multiselectable="true">

                        <div class="panel panel-default">
                            <div class="panel-heading svid collapsed" data-bs-toggle="collapse" data-bs-target="#electionDates" role="tab">
                                <h4 class="panel-title">
                                    <a role="button" class="accordion-toggle" data-bs-toggle="collapse" href="#electionDates" aria-expanded="true" aria-controls="collapseOne">Upcoming Election Dates &amp; Deadlines</a>
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
                    </div><!-- end Domestic Panel -->
                </div><!-- End Domestic tab -->

                <div role="tabpanel" class="tab-pane fade" id="uocava-<c:out value='${param.contentType}' />">
                    <h4>Overseas &amp; Military Voters</h4>
                    <div class="panel-group" id="accordion2" role="tablist" aria-multiselectable="true">

                        <div class="panel panel-default">
                            <div class="panel-heading svid collapsed" data-bs-toggle="collapse" data-bs-target="#collapseFive" role="tab">
                                <h4 class="panel-title">
                                    <a role="button" class="accordion-toggle" data-bs-toggle="collapse" href="#collapseFive" aria-expanded="true" aria-controls="collapseFive">Upcoming Election Dates &amp; Deadlines</a>
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

                    </div>
                </div>

            </div><!-- end Domestic Panel -->

        </div><!--End Uocava tab-->
    </div><!-- End all tabs -->
    </c:if>
    <p>&nbsp;</p>
</div>

</div>