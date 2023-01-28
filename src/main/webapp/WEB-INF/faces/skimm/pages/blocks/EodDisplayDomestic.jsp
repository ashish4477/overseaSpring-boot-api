<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<div class="body-content row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
    <div id="eod-form" class="<c:if test="${not (isSvid and isEod)}">svid </c:if>page-form">
      <c:if test="${isEod}">
        <h1 class="title"><c:out value='${selectedState.name} - ${selectedRegion.name}'/> Election Official Directory</h1>
      </c:if>

      <div class="panel-group" id="accordion3" role="tablist" aria-multiselectable="true">
        <c:if test="${not empty leo}">
          <div class="row">
            <div class="col-xs-12 col-sm-9">
              <h3>Local Election Office Information</h3>
            </div>
<%--
            <div class="col-xs-12 col-sm-3"><br/>
              <div class="last-updated pull-right">Updated&nbsp;<b><fmt:formatDate value="${leo.updated}" pattern="MM/dd/yyyy" /></b></div>
            </div>
--%>
          </div>

          <div class="panel panel-default">
            <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseLeoEodAddress" role="tab">
              <h4 class="panel-title">
                <a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseLeoEodAddress" aria-expanded="true" aria-controls="collapseLeoEodAddress">Address Information</a>
              </h4>
            </div>
            <div id="collapseLeoEodAddress" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
              <div class="panel-body no-table">
              <c:forEach items="${leo.additionalAddresses}" var="additionalAddress" varStatus="indx">
                <div class="accordian-sect additional-address <c:if test="${indx.last}">last</c:if> ">
                    <dl class="row">
                      <dt class="col-xs-12 col-sm-6">
                        <h4 class="address-type">${additionalAddress.type.name} <c:if test="${additionalAddress.combinedLabel}">for:</c:if></h4>
                        <c:if test="${additionalAddress.combinedLabel}">
                        <%--<c:forEach items="${additionalAddress.functions}" var="function">
                        <h4>${function.description}</h4>
                        </c:forEach>--%>
                          <ul>
                            <li><h4>${additionalAddress.shortLabel}</h4></li>
                          </ul>
                      </c:if>
                     
                     
                      </dt>
                      <dd class="col-xs-12 col-sm-6">
                            <c:out value='${additionalAddress.address.addressTo}' />
                                <br />
                            <c:out value='${additionalAddress.address.street1}' />
                                <br />
                            <c:if test="${not empty additionalAddress.address.street2}">
                                <c:out value='${additionalAddress.address.street2}' />
                                <br />
                            </c:if>
                            <c:out value='${additionalAddress.address.city}, ${additionalAddress.address.state}' />
                            <c:if test="${not empty additionalAddress.address.zip}">
                                <c:out value='${additionalAddress.address.zip}' />
                                <c:if test="${not empty additionalAddress.address.zip4}">-<c:out value='${additionalAddress.address.zip4}' />
                                </c:if>
                            </c:if>
                            <br/><br/>
                        <c:if test="${not empty additionalAddress.website}"><a href="${additionalAddress.website}" target="_blank">${additionalAddress.website}</a><br/></c:if>
                        <c:if test="${not empty additionalAddress.mainOfficer}">
                          phone: ${additionalAddress.mainPhoneNumber}<br/>
                        </c:if>
                        <c:choose>
                          <c:when test="${not empty additionalAddress.email}"><a href="mailto:${additionalAddress.email}">${additionalAddress.email}</a><br/></c:when>
                          <c:when test="${not empty additionalAddress.mainOfficer and not empty additionalAddress.mainOfficer.email}"><a href="mailto:${additionalAddress.mainOfficer.email}">${additionalAddress.mainOfficer.email}</a><br/></c:when>
                        </c:choose>
                        </dd>
                    </dl>
                </div>
            </c:forEach>
              </div>
            </div>
          </div>

          <div class="panel panel-default">
            <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseEodContact" role="tab">
              <h4 class="panel-title">
                <a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseEodContact" aria-expanded="true" aria-controls="collapseEodContact">Election Official Contact Details</a>
              </h4>
            </div>
            <div id="collapseEodContact" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
              <div class="panel-body eod svid">
                <div class="table-responsive">
                  <table class="table eod">
                    <c:if test="${empty leo.officers && empty leo.generalEmail}">
                      <tr>
                        <td><p>none on record</p></td>
                      </tr>
                    </c:if>
                    <c:forEach items="${leo.officers}" var="officer">
                      <thead>
                      <tr>
                        <th><h4 class="header">${officer.officeName}</h4></th>
                        <th>
                          <c:choose>
                            <c:when test="${not empty officer.firstName or not empty officer.lastName }">
                              <c:out value='${officer.firstName} ${officer.lastName}'/>
                            </c:when>
                            <c:otherwise>none on record</c:otherwise>
                          </c:choose>
                        </th>
                      </tr>
                      </thead>
                      <tr>
                        <td><h4>Phone</h4></td>
                        <td>
                          <c:choose><c:when test="${not empty officer.phone}"><c:out value='${officer.phone}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                        </td>
                      </tr>
                      <tr>
                        <td><h4>Fax</h4></td>
                        <td>
                          <c:choose><c:when test="${not empty officer.fax}"><c:out value='${officer.fax}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                        </td>
                      </tr>
                      <tr>
                        <td><h4>Email Address</h4></td>
                        <td><c:choose><c:when test="${not empty officer.email}"><a href="mailto:<c:out value='${officer.email}'/>"><c:out value='${officer.email}'/></a></c:when><c:otherwise>none on record</c:otherwise></c:choose></td>
                      </tr>
                    </c:forEach>
                    <c:if test="${not empty leo.generalEmail}">
                      <tr>
                        <td><h4>Election Office Email Address</h4></td>
                        <td><a href="mailto:${leo.generalEmail}">${leo.generalEmail}</a></td>
                      </tr>
                    </c:if>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <div class="panel panel-default">
            <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseEodLeoAdditional" role="tab">
              <h4 class="panel-title">
                <a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseEodLeoAdditional" aria-expanded="true" aria-controls="collapseEodLeoAdditional">Additional Information</a>
              </h4>
            </div>
            <div id="collapseEodLeoAdditional" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
              <div class="panel-body no-table">
                <div class="accordian-sect office-hours">
                  <dl class="row"><dt class="col-xs-12 col-sm-6"><h4>Office Hours</h4></dt>
                    <dd class="col-xs-12 col-sm-6">
                      <c:choose>
                        <c:when test="${not empty leo.hours}">
                          <c:out value='${leo.hours}' />
                        </c:when>
                        <c:otherwise>none on record</c:otherwise>
                      </c:choose>
                    </dd>
                  </dl>
                </div>
                <div class="accordian-sect further-instructions">
                  <dl class="row"><dt class="col-xs-12 col-sm-6"><h4>Further instructions</h4></dt>
                    <dd class="col-xs-12 col-sm-6">
                      <c:choose>
                        <c:when test="${not empty leo.furtherInstruction}">${fn:replace(fn:escapeXml(leo.furtherInstruction),newLineChar,"<br />")}</c:when>
                        <c:otherwise>none on record</c:otherwise>
                      </c:choose>
                    </dd></dl>
                </div>
                <div class="accordian-sect voter-registration-or-status-inquiry last">
                  <dl class="row"><dt class="col-xs-12 col-sm-6"><h4>Am I Registered?</h4></dt>
                    <dd class="col-xs-12 col-sm-6">${amIRegistered}</dd>
                  </dl>
                </div>
              </div>
            </div>

          </div><!-- end Panel -->

          <c:choose>
            <c:when test="${not empty leo.website}">
              <p>&nbsp;</p>
              <a class="button small pull-right" href="<c:out value='${leo.website}'/>" target="_blank">Visit ${selectedRegion.name} Local Elections Website</a>
            </c:when>
          </c:choose>
          <div class="clearfix"></div>
          <div class="corrections hidden-xs">
            <a class="pull-right" href="${regionCorrectionUri}">Update <b>${selectedRegion.name}, ${selectedState.name}</b> Information</a>
            <br />
            <span class="hint">(For Election Official Use Only)</span>
          </div>
          <br/><br/>
        </c:if>

        <c:if test="${isSvid}">
        <h1 class="title" align="center">State of ${selectedState.name} Voting Requirements</h1>
        <p>&nbsp;</p>
        <!-- SVID Tab panes -->
        <div class="tab-content">
          <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#domestic" aria-controls="domestic" role="tab" data-toggle="tab">U.S. Domestic Voters</a></li>
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
                        <c:set var="stateInfoId" value="0"/>
                        <c:forEach items="${localElections}" var="election" varStatus="inx">
                        <c:if test="${election.stateId ne stateInfoId}">
                          <c:set var="stateInfoId" value="${election.stateId}"/>
                        </c:if>
                        <c:set var="domesticEarlyVoting" value="${election.domesticEarlyVoting}"/>
                        <c:set var="domesticAbsenteeVoting" value="${election.absenteeVoting}"/>
                          <table class="table" cellspacing="0">
                            <thead>
                            <c:if test="${empty level or election.sortOrder ne level}">
                              <c:set var="level" value="${election.sortOrder}"/>
                              <tr>
                                <th colspan="2" class="election-level" id="${level.name}"><h4>${level.name} Elections</h4></th>
                              </tr>
                            </c:if>
                            <tr>
                              <th colspan="2" class="header election-title"><h4>${election.title} held on ${election.heldOn}</h4></th>
                            </tr>
                             <tr class="labels">
                              <th><h4>Type</h4></th>
                              <th><h4>Deadlines</h4></th>
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
                            <c:if test="${not empty domesticAbsenteeVoting}">
                              <tr>
                                <td>
                                  <h4>In-Person Absentee Voting</h4>
                                </td>
                                <td>
                                  <c:choose>
                                    <c:when test="${fn:contains(domesticAbsenteeVoting, 'not allowed')}">
                                      In-Person Absentee Voting is not allowed in the state of ${election.stateName}
                                    </c:when>
                                    <c:otherwise>
                                      ${domesticAbsenteeVoting}
                                    </c:otherwise>
                                  </c:choose>
                                </td>
                              </tr>
                            </c:if>
                            <c:if test="${not empty election.domesticEarlyVoting}">
                              <tr>
                                <td>
                                  <h4>Early Voting</h4>
                                </td>
                                <td>
                                  ${election.domesticEarlyVoting}
                                </td>
                              </tr>
                            </c:if>
                            <c:if test="${not empty election.domesticNotes}">
                              <tr>
                                <td colspan="2" class="note">${election.domesticNotes}</td>
                              </tr>
                            </c:if>
                          </table>
                        </c:forEach>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="panel panel-default">
                  <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#GeneralInfo" role="tab">
                    <h4 class="panel-title">
                      <a role="button" class="accordion-toggle" data-toggle="collapse" href="#GeneralInfo" aria-expanded="true" aria-controls="collapseOne">General Information</a>
                    </h4>
                  </div>
                  <div id="GeneralInfo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body svid markdown no-table">
                      <overseas:markdownToHtml markdown="${stateVoterInformation.votingGeneralInfo}"/>
                    </div>
                  </div>
                </div>

                <div class="panel panel-default">
                  <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseOne" role="tab">
                    <h4 class="panel-title"><a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Eligibility Requirements</a></h4>
                  </div>
                  <div id="collapseOne" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body svid no-table">
                      <c:forEach items="${eligibilityRequirements.get(\"Domestic Voter\")}" var="requirementList">
                        <h4 class="header">${requirementList.header}</h4>
                        <ul>
                          <c:forEach items="${requirementList.items}" var="requirementItem">
                            <li> <c:out value='${requirementItem.name}'/></li>
                          </c:forEach>
                        </ul>
                        <c:if test="${not empty requirementList.footer}">
                          ${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
                        </c:if>
                      </c:forEach>
                      <c:if test="${not empty eligibilityRequirements.get(\"Student Eligibility\")}">
                        <h4 class="header">Student Eligibility</h4>  
                      </c:if>
                      <c:forEach items="${eligibilityRequirements.get(\"Student Eligibility\")}" var="requirementList">
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
                    </div>
                  </div>
                </div>

                <div class="panel panel-default">
                  <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseTwo" role="tab">
                    <h4 class="panel-title"><a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseTwo" aria-expanded="true" aria-controls="collapseOne">Identification Requirements</a></h4>
                  </div>
                  <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body svid no-table">
                      <c:forEach items="${identificationRequirements}" var="requirementLists">
                        <%-- Domestic Only --%>
                        <c:if test="${!(fn:contains(requirementLists.key, 'Military') || fn:contains(requirementLists.key, 'Overseas'))}">
                          <c:if test="${(fn:length(requirementLists.value) > 1 || (fn:length(requirementLists.value) == 1 && (not empty requirementLists.value[0].header || fn:length(requirementLists.value[0].items) > 0)))}">
                            <h4 class="header">${requirementLists.key}</h4>
                            <p>
                            <c:forEach items="${requirementLists.value}" var="requirementList">
                              <h4>${requirementList.header}</h4>
                              <ul>
                                <c:forEach items="${requirementList.items}" var="requirementItem">
                                  <li>${requirementItem.name}</li>
                                </c:forEach>
                              </ul>
                              <c:if test="${not empty requirementList.footer}"> <p>
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
                          <c:forEach items="${voterTypeTransmissionMethods.groupedItems}" var="items" varStatus="loop">
                            <c:if test="${ loop.index != 2 }">
                              <tr class="r1">
                                <td class="first-col"><h4>${items.key}</h4></td>
                                <td><c:choose>
                                  <c:when test="${items.value.get(\"In-Person\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td class="odd"><c:choose>
                                  <c:when test="${items.value.get(\"Mail\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td><c:choose>
                                  <c:when test="${items.value.get(\"Fax\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td class="odd"><c:choose>
                                  <c:when test="${items.value.get(\"Email\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td><c:choose>
                                  <c:when test="${items.value.get(\"Online\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                              </tr>
                            </c:if>
                          </c:forEach>
                          <c:forEach items="${voterTypeTransmissionMethods.groupedItems}" var="items" varStatus="loop">
                            <c:if test="${ loop.index == 2 }">
                              <tr class="r1">
                                <td class="first-col"><h4>${items.key}</h4></td>
                                <td><c:choose>
                                  <c:when test="${items.value.get(\"In-Person\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td class="odd"><c:choose>
                                  <c:when test="${items.value.get(\"Mail\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td><c:choose>
                                  <c:when test="${items.value.get(\"Fax\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td class="odd"><c:choose>
                                  <c:when test="${items.value.get(\"Email\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td><c:choose>
                                  <c:when test="${items.value.get(\"Online\")}">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    <span class="glyphicon-ie" aria-hidden="true"></span>
                                  </c:when>
                                  <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                              </tr>
                            </c:if>
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
                  <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseFour" role="tab">
                    <h4 class="panel-title">
                      <a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseFour" aria-expanded="true" aria-controls="collapseOne">State Lookup Tools - Am I Registered? Where's my Ballot?</a>
                    </h4>
                  </div>
                  <div id="collapseFour" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body svid">
                      <div class="table-responsive">
                        <table class="table eod" cellspacing="0">
                          <c:forEach items="${stateVoterInformation.validLookupTools}" var="lookupTool">
                            <tr>
                              <td><h4><c:out value='${lookupTool.name}' /></h4></td>
                              <td>
                                <a target="_blank" href="<c:out value='${lookupTool.url}' />"><c:out value='${lookupTool.url}' /></a>
                              </td>
                            </tr>
                          </c:forEach>
                        </table>
                      </div>
                    </div>
                  </div>
                </div>
              </div><!-- end Domestic Panel -->
            </div><!-- End Domestic tab -->

          </div>
          </c:if>
          <p>&nbsp;</p>
          <div class="row">
            <div class="col-xs-12">
              <div class="pull-right">
                <!-- <a href="<c:url value="/voter-registration-absentee-voting.htm"/>" class="button small">Register to Vote</a> &nbsp; -->
                <a href="<c:url value="/voter-registration-absentee-voting.htm"/>" class="button small">Request Absentee Ballot</a></div>
            </div>
          </div>


        </div>
      </div>
    </div>
    <div class="bd select-form-bd row">
      <div class="col-xs-12 col-sm-6 col-sm-offset-3">
        <c:import url="/WEB-INF/${relativePath}/pages/statics/EodSelectForm.jsp" />
      </div>
    </div>
    <script type="text/javascript">
      $(document).ready(function(){
        if (location.hash) {
          $('a[href=' + location.hash + ']').tab('show');
        }
      });
    </script>