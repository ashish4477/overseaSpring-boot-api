<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>

<div class="body-content row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
    <div id="eod-form" class="<c:if test="${not (isSvid and isEod)}">svid </c:if>page-form">
      <c:if test="${isEod}">
        <h1 class="title"><c:out value='${selectedState.name} - ${selectedRegion.name}'/> Election Official Directory</h1>
      </c:if>
      <c:if test="${empty showCaptcha}">
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
                            phone: ${additionalAddress.mainOfficer.phone}<br/>
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
            <br/>
            <div class="corrections hidden-xs">
              <a href="${regionCorrectionUri}">Update <b>${selectedRegion.name} ${selectedState.name}</b></a>
              <br /><span class="hint">(For Election Official Use Only)</span>
            </div>
          </c:if>

          <c:if test="${isSvid}"><h2 class="title">State Voter Information Directory</h2></c:if>
          <c:if test="${not empty stateVoterInformation}">
            <h3><c:out value='${selectedState.name}' /> Statewide Voter Information</h3>

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
                            <th class="header election-title" colspan="3"><h4>${election.title} held on ${election.heldOn}</h4></th>
                          </tr>
                          <tr class="labels">
                            <th><h4>Type</h4></th>
                            <th colspan="2"><h4>Deadlines</h4></th>
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
                          <c:if test="${not empty election.notes}">
                            <tr>
                              <td colspan="3" class="note">${election.notes}</td>
                            </tr>
                          </c:if>
                        </table>
                      </div>
                    </c:forEach>
                  </div>
                </div>
              </div>

              <div class="panel panel-default">
                <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseSix" role="tab">
                  <h4 class="panel-title"><a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseSix" aria-expanded="true" aria-controls="collapseSix">Eligibility Requirements</a></h4>
                </div>
                <div id="collapseSix" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                  <div class="panel-body svid no-table">
                    <h4 class="header">Overseas Voters</h4>
                    <c:forEach items="${eligibilityRequirements.get(\"Overseas Voter\")}" var="requirementList">
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
                    <c:forEach items="${eligibilityRequirements.get(\"Military Voter\")}" var="requirementList">
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
                <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseSeven" role="tab">
                  <h4 class="panel-title"><a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseSeven" aria-expanded="true" aria-controls="collapseOne">Identification Requirements</a></h4>
                </div>
                <div id="collapseSeven" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                  <div class="panel-body svid no-table">
                    <c:forEach items="${identificationRequirements}" var="requirementLists">
                      <%-- Uocava Only --%>
                      <c:if test="${(fn:contains(requirementLists.key, 'Military') || fn:contains(requirementLists.key, 'Overseas'))}">
                        <h4 class="header">${requirementLists.key}</h4>
                        <c:forEach items="${requirementLists.value}" var="requirementList">
                          <p>${requirementList.header}</p>
                          <ul>
                            <c:forEach items="${requirementList.items}" var="requirementItem">
                              <li>${requirementItem.name}</li>
                            </c:forEach>
                          </ul>
                          <c:if test="${not empty requirementList.footer}">
                            ${fn:replace(fn:escapeXml(requirementList.footer),newLineChar,"<br/>")}
                            <br /><br />
                          </c:if>
                        </c:forEach>

                      </c:if>
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
              </div></div>

            <div class="panel panel-default">
              <div class="panel-heading svid collapsed" data-toggle="collapse" data-target="#collapseNine" role="tab">
                <h4 class="panel-title">
                  <a role="button" class="accordion-toggle" data-toggle="collapse" href="#collapseNine" aria-expanded="true" aria-controls="collapseOne">State Lookup Tools - Am I Registered? Where's my Ballot?</a>
                </h4>
              </div>
              <div id="collapseNine" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
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

            </div><!-- end Domestic Panel -->

          </c:if>

        </div>
      </c:if>

      <div class="bd select-form-bd row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3">
          <c:import url="/WEB-INF/${relativePath}/pages/statics/EodSelectForm.jsp" />
        </div>
      </div>
      <div class="ft">
        <div class="ft-inner"></div>
      </div>
    </div>
  </div>
</div>
