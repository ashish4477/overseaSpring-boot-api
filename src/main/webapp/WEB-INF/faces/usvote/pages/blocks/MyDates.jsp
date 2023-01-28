<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%--<% pageContext.setAttribute("newLineChar", "\n"); %>--%>

<div class="column">
    <div id="eod-form" class="row voter-account-page wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form">
        <div class="col-xs-12 right-side">
            <h2 class="title">Quick Take on Voting in my State</h2>


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
                    <div role="tabpanel" class="tab-pane fade show in active" id="domestic-<c:out value='${param.contentType}' />">

                        <h4>U.S. Domestic Voters</h4>

                        <div class="panel-group" id="accordion1" role="tablist" aria-multiselectable="true">
                            <div class="panel panel-default">
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#electionDates" role="tab">
                                    <h4 class="panel-title">
                                        <a role="button" class="accordion-toggle" data-bs-toggle="collapse"
                                           href="#electionDates" aria-expanded="true" aria-controls="collapseOne">Upcoming
                                            Election Dates &amp; Deadlines</a>
                                    </h4>
                                </div>
                                <div id="electionDates" class="panel-collapse collapse" role="tabpanel"
                                     aria-labelledby="headingOne">
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
                                                            <th colspan="2" class="election-level" id="${level.name}">
                                                                <h4>${level.name} Elections</h4></th>
                                                        </tr>
                                                    </c:if>
                                                    <tr>
                                                        <th colspan="2" class="header"><h4>${election.title} held
                                                            on ${election.heldOn}</h4></th>
                                                    </tr>
                                                    </thead>
                                                    <tr>
                                                        <td><h4>New Voter Registration</h4></td>
                                                        <td>
                                                            <div>${election.domesticRegistration}</div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><h4>Absentee Ballot Request</h4></td>
                                                        <td>${election.domesticBallotRequest}</td>
                                                    </tr>
                                                    <tr>
                                                        <td><h4>Absentee Ballot Return</h4></td>
                                                        <td>
                                                            <div>${election.domesticBallotReturn}</div>
                                                        </td>
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
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#generalInformation" role="tab">
                                    <h4 class="panel-title">
                                        <a role="button" class="accordion-toggle" data-bs-toggle="collapse"
                                           href="#generalInformation" aria-expanded="true" aria-controls="collapseOne">General Information</a>
                                    </h4>
                                </div>
                                <div id="generalInformation" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                                    <div class="panel-body svid markdown no-table">
                                        <overseas:markdownToHtml markdown="${stateVoterInformation.votingGeneralInfo}"/>
                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#eligibilityRequirements" role="tab">
                                    <h4 class="panel-title">
                                        <a role="button" class="accordion-toggle" data-bs-toggle="collapse"
                                           href="#eligibilityRequirements" aria-expanded="true"
                                           aria-controls="collapseOne">Eligibility Requirements</a>
                                    </h4>
                                </div>
                                <!-- Accordian place for Eligibility Requirements-->
                                <div id="eligibilityRequirements" class="panel-collapse collapse" role="tabpanel"
                                     aria-labelledby="headingOne">
                                    <div class="panel panel-default">
                                        <div id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                                            <div class="panel-body svid no-table">
                                                <c:forEach items="${eligibilityRequirements.get(\"Domestic Voter\")}"
                                                           var="requirementList">
                                                    <h4 class="header">${requirementList.header}</h4>
                                                    <ul>
                                                        <c:forEach items="${requirementList.items}"
                                                                   var="requirementItem">
                                                            <li><c:out value='${requirementItem.name}'/></li>
                                                        </c:forEach>
                                                    </ul>
                                                    <c:if test="${not empty requirementList.footer}">
                                                        ${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
                                                    </c:if>
                                                </c:forEach>
                                                <c:if test="${not empty eligibilityRequirements.get(\"Student Eligibility\")}">
                                                    <h4 class="header">Student Eligibility</h4>
                                                </c:if>
                                                <c:forEach
                                                        items="${eligibilityRequirements.get(\"Student Eligibility\")}"
                                                        var="requirementList">
                                                    <p>${requirementList.header}</p>
                                                    <ul>
                                                        <c:forEach items="${requirementList.items}"
                                                                   var="requirementItem">
                                                            <li><c:out value='${requirementItem.name}'/></li>
                                                        </c:forEach>
                                                    </ul>

                                                    <p>
                                                        <c:if test="${not empty requirementList.footer}">
                                                            ${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
                                                        </c:if>
                                                    </p>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#identificationRequirements" role="tab">
                                    <h4 class="panel-title">
                                        <a role="button" class="accordion-toggle" data-bs-toggle="collapse"
                                           href="#identificationRequirements" aria-expanded="true"
                                           aria-controls="collapseOne">Identification Requirements</a>
                                    </h4>
                                </div>
                                <!-- Accordian place for Identification Requirements-->
                                <div id="identificationRequirements" class="panel-collapse collapse" role="tabpanel"
                                     aria-labelledby="headingOne">
                                    <div id="collapseTwo" role="tabpanel" aria-labelledby="headingOne">
                                        <div class="panel-body svid no-table">
                                            <c:forEach items="${identificationRequirements}" var="requirementLists">
                                                <%-- Domestic Only --%>
                                                <c:if test="${!(fn:contains(requirementLists.key, 'Military') || fn:contains(requirementLists.key, 'Overseas'))}">
                                                    <c:if test="${(fn:length(requirementLists.value) > 1 || (fn:length(requirementLists.value) == 1 && (not empty requirementLists.value[0].header || fn:length(requirementLists.value[0].items) > 0)))}">
                                                        <h4 class="header">${requirementLists.key}</h4>
                                                        <p>
                                                        <c:forEach items="${requirementLists.value}"
                                                                   var="requirementList">
                                                            <h4>${requirementList.header}</h4>
                                                            <ul>
                                                                <c:forEach items="${requirementList.items}"
                                                                           var="requirementItem">
                                                                    <li>${requirementItem.name}</li>
                                                                </c:forEach>
                                                            </ul>
                                                            <c:if test="${not empty requirementList.footer}"><p>
                                                                    ${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
                                                            </p>
                                                            </c:if>
                                                        </c:forEach>
                                                        </p>
                                                    </c:if>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#collapseThree" role="tab">
                                    <h4 class="panel-title"><a role="button" class="accordion-toggle"
                                                               data-bs-toggle="collapse" href="#collapseThree"
                                                               aria-expanded="true" aria-controls="collapseOne">Voter
                                        Materials Transmission Options</a></h4>
                                </div>
                                <div id="collapseThree" class="panel-collapse collapse" role="tabpanel"
                                     aria-labelledby="headingOne">
                                    <div class="panel-body svid">
                                        <div class="table-responsive">
                                            <c:forEach items="${stateVoterInformation.transmissionMethods}"
                                                       var="voterTypeTransmissionMethods">
                                                <%-- Domestic Only --%>
                                            <c:if test="${!(fn:contains(voterTypeTransmissionMethods.voterType.name, 'Military') || fn:contains(voterTypeTransmissionMethods.voterType.name, 'Overseas'))}">
                                            <table class="accordian-content rounded-5 trans-options" cellspacing="0">
                                                <tr class="r0">
                                                    <td class="first-col"><h4
                                                            class="header">${voterTypeTransmissionMethods.voterType.name}</h4>
                                                    </td>
                                                    <td><strong>In-Person</strong></td>
                                                    <td><strong>Mail</strong></td>
                                                    <td><strong>Fax</strong></td>
                                                    <td><strong>Email</strong></td>
                                                    <td><strong>Online</strong></td>
                                                </tr>
                                                <!--${voterTypeTransmissionMethods.groupedItems}-->
                                                <c:forEach items="${voterTypeTransmissionMethods.groupedItems}"
                                                           var="items">
                                                    <tr class="r1">
                                                        <td class="first-col"><h4>${items.key}</h4></td>
                                                        <td><c:choose>
                                                            <c:when test="${items.value.get(\"In-Person\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose></td>
                                                        <td class="odd"><c:choose>
                                                            <c:when test="${items.value.get(\"Mail\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose></td>
                                                        <td><c:choose>
                                                            <c:when test="${items.value.get(\"Fax\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose></td>
                                                        <td class="odd"><c:choose>
                                                            <c:when test="${items.value.get(\"Email\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose></td>
                                                        <td><c:choose>
                                                            <c:when test="${items.value.get(\"Online\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
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
                            <div class="panel panel-default">
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#stateLoopup" role="tab">
                                    <h4 class="panel-title"><a role="button" class="accordion-toggle"
                                                               data-bs-toggle="collapse" href="#stateLoopup"
                                                               aria-expanded="true" aria-controls="collapseOne">State
                                        Lookup Tools - Am I Registered?</a></h4>
                                </div>
                                <div id="stateLoopup" class="panel-collapse collapse" role="tabpanel"
                                     aria-labelledby="headingOne">
                                    <div class="panel panel-default">
                                        <div id="collapseFour" role="tabpanel" aria-labelledby="headingOne">
                                            <div class="panel-body svid">
                                                <div class="table-responsive">
                                                    <table class="table eod" cellspacing="0">
                                                        <c:forEach items="${stateVoterInformation.validLookupTools}"
                                                                   var="lookupTool">
                                                            <tr>
                                                                <td><h4><c:out value='${lookupTool.name}'/></h4>
                                                                </td>
                                                                <td>
                                                                    <a target="_blank"
                                                                       href="<c:out value='${lookupTool.url}' />"><c:out
                                                                            value='${lookupTool.url}'/></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </div>
                                            </div>
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
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#collapseFive" role="tab">
                                    <h4 class="panel-title">
                                        <a role="button" class="accordion-toggle" data-bs-toggle="collapse"
                                           href="#collapseFive" aria-expanded="true" aria-controls="collapseFive">Upcoming
                                            Election Dates &amp; Deadlines</a>
                                    </h4>
                                </div>
                                <div id="collapseFive" class="panel-collapse collapse" role="tabpanel"
                                     aria-labelledby="headingOne">
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
                                                            <th colspan="3" class="election-level"
                                                                id="${level.name}"><h4>${level.name} Elections</h4>
                                                            </th>
                                                        </tr>
                                                    </c:if>
                                                    <tr>
                                                        <th class="header" colspan="3"><h4>${election.title} held
                                                            on ${election.heldOn}</h4></th>
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
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#eligibilityRequirementOverseas" role="tab">
                                    <h4 class="panel-title">
                                        <a role="button" class="accordion-toggle" data-bs-toggle="collapse"
                                           href="#eligibilityRequirementOverseas" aria-expanded="true"
                                           aria-controls="collapseFive">Eligibility Requirements</a>
                                    </h4>
                                </div>
                                <div id="eligibilityRequirementOverseas" class="panel-collapse collapse"
                                     role="tabpanel" aria-labelledby="headingOne">
                                    <div class="panel-body svid no-table">
                                        <h4 class="header">Overseas Voters</h4>
                                        <c:forEach items="${eligibilityRequirements.get(\"Overseas Voter\")}"
                                                   var="requirementList">
                                            <p>${requirementList.header}</p>
                                            <ul>
                                                <c:forEach items="${requirementList.items}" var="requirementItem">
                                                    <li><c:out value='${requirementItem.name}'/></li>
                                                </c:forEach>
                                            </ul>

                                            <p>
                                                <c:if test="${not empty requirementList.footer}">
                                                    ${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
                                                </c:if>
                                            </p>
                                        </c:forEach>
                                        <h4 class="header">Military Voters</h4>
                                        <c:forEach items="${eligibilityRequirements.get(\"Military Voter\")}"
                                                   var="requirementList">
                                        <p>${requirementList.header}</p>
                                        <ul>
                                            <c:forEach items="${requirementList.items}" var="requirementItem">
                                                <li> ${requirementItem.name}</li>
                                            </c:forEach>
                                        </ul>

                                        <p>
                                            <c:if test="${not empty requirementList.footer}">
                                                ${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
                                            </c:if>
                                            </c:forEach>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#identificationRequirementOverseas" role="tab">
                                    <h4 class="panel-title">
                                        <a role="button" class="accordion-toggle" data-bs-toggle="collapse"
                                           href="#identificationRequirementOverseas" aria-expanded="true"
                                           aria-controls="collapseFive">Identification Requirements</a>
                                    </h4>
                                </div>
                                <div id="identificationRequirementOverseas" class="panel-collapse collapse"
                                     role="tabpanel" aria-labelledby="headingOne">
                                    <div class="panel-body svid no-table">
                                        <c:forEach items="${identificationRequirements}" var="requirementLists">
                                            <%-- Uocava Only --%>
                                            <c:if test="${(fn:contains(requirementLists.key, 'Military') || fn:contains(requirementLists.key, 'Overseas'))}">
                                                <h4 class="header">${requirementLists.key}</h4>
                                                <c:forEach items="${requirementLists.value}" var="requirementList">
                                                    <p>${requirementList.header}</p>
                                                    <ul>
                                                        <c:forEach items="${requirementList.items}"
                                                                   var="requirementItem">
                                                            <li>${requirementItem.name}</li>
                                                        </c:forEach>
                                                    </ul>
                                                    <c:if test="${not empty requirementList.footer}">
                                                        ${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
                                                        <br/><br/>
                                                    </c:if>
                                                </c:forEach>

                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse" data-bs-target="#collapseEight" role="tab">
                                    <h4 class="panel-title"><a role="button" class="accordion-toggle" data-bs-toggle="collapse" href="#collapseEight" aria-expanded="true" aria-controls="collapseOne">Voter Materials Transmission Options</a></h4>
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
                                                                <span class="bi bi-check" aria-hidden="true"></span>
                                                            </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose></td>
                                                        <td class="odd"><c:choose>
                                                            <c:when test="${items.value.get(\"Mail\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose></td>
                                                        <td><c:choose>
                                                            <c:when test="${items.value.get(\"Fax\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose></td>
                                                        <td class="odd"><c:choose>
                                                            <c:when test="${items.value.get(\"Email\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose></td>
                                                        <td><c:choose>
                                                            <c:when test="${items.value.get(\"Online\")}">
                                                                <span class="bi bi-check" aria-hidden="true"></span>                                                        </c:when>
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
                        </div>
                            <div class="panel panel-default">
                                <div class="panel-heading svid collapsed" data-bs-toggle="collapse"
                                     data-bs-target="#stateLookupToolsOverseas" role="tab">
                                    <h4 class="panel-title">
                                        <a role="button" class="accordion-toggle" data-bs-toggle="collapse"
                                           href="#stateLookupToolsOverseas" aria-expanded="true"
                                           aria-controls="collapseFive">State Lookup Tools-Am I Registered</a>
                                    </h4>
                                </div>
                                <div id="stateLookupToolsOverseas" class="panel-collapse collapse" role="tabpanel"
                                     aria-labelledby="headingOne">
                                    <div class="panel-body svid">
                                        <div class="table-responsive">
                                            <table class="table eod" cellspacing="0">
                                                <c:forEach items="${stateVoterInformation.validLookupTools}"
                                                           var="lookupTool">
                                                    <tr>
                                                        <td><h4><c:out value='${lookupTool.name}'/></h4></td>
                                                        <td>
                                                            <a target="_blank"
                                                               href="<c:out value='${lookupTool.url}' />"><c:out
                                                                    value='${lookupTool.url}'/></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </div>

                </div><!-- End all tabs -->
                </c:if>
                <p>&nbsp;</p>
            </div>

        </div>
    </div>
</div>