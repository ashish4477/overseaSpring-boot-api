<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div id="eod-form" class="wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form">
  <div class="hd">
    <h1 class="title">
      <c:if test="${isEod}">Election Official</c:if>
      <c:if test="${isEod and isSvid}"> &amp; </c:if>
      <c:if test="${isSvid}">State Voter Information</c:if>
      Directory</h1>
  </div>
  <div class="bd">
    <c:if test="${not empty leo}">
      <h3><c:out value='${selectedState.name} - ${selectedRegion.name}'/></h3>
      <c:choose>
        <c:when test="${not empty leo.website}">
          <h4>Local Elections Website: <a href="<c:out value='${leo.website}'/>" target="_blank">Visit Official Website</a></h4>
        </c:when>
        <c:otherwise>
        </c:otherwise>
      </c:choose>
      <div id="addressinfo" class="accordian rounded-5">
        <h5 class="ctrl">Address Information</h5>
        <div class="pnl" style="display:none">
          <div class="accordian-content rounded-5">
            <c:forEach items="${leo.additionalAddresses}" var="additionalAddress" varStatus="indx">
                <div class="accordian-sect additional-address <c:if test="${indx.last}">last</c:if> ">
                    <dl class="row">
                      <dt class="col-12 col-sm-6">
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
                      <dd class="col-12 col-sm-6">
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
      <div id="contactdetails" class="accordian rounded-5">
        <h5 class="ctrl">Election Official Contact Details</h5>
        <div class="pnl" style="display:none">
          <div class="accordian-content rounded-5">
            <c:if test="${empty leo.officers && empty leo.generalEmail}">
              <div class="accordian-sect empty">
                <dl>
                  <dt>none on record</dt>
                </dl>
              </div>
            </c:if>
            <c:if test="${not empty leo.generalEmail}">
              <div class="accordian-sect general-email">
                <dl>
                  <dt>Election Office Email Address</dt>
                  <dd>${leo.generalEmail}</dd>
                </dl>
              </div>
            </c:if>
            <c:forEach items="${leo.officers}" var="officer">
              <div class="accordian-sect officer">
                <dl>
                  <dt class="officeName">${officer.officeName}</dt>
                  <dd>
                    <c:choose>
                      <c:when test="${not empty officer.firstName or not empty officer.lastName }">
                        <c:out value='${officer.firstName} ${officer.lastName}'/>
                      </c:when>
                      <c:otherwise>none on record</c:otherwise>
                    </c:choose>
                  </dd>
                </dl>
                <dl>
                  <dt>Phone</dt>
                  <dd>
                    <c:choose><c:when test="${not empty officer.phone}"><c:out value='${officer.phone}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                  </dd>
                </dl>
                <dl>
                  <dt>Fax</dt>
                  <dd>
                    <c:choose><c:when test="${not empty officer.fax}"><c:out value='${officer.fax}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                  </dd>
                </dl>
                <dl>
                  <dt>Email Address</dt>
                  <dd>
                    <c:choose><c:when test="${not empty officer.email}"><c:out value='${officer.email}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                  </dd>
                </dl>
              </div>
            </c:forEach>
          </div>
        </div>
      </div>
      <div id="additionalinfo" class="accordian rounded-5">
        <h5 class="ctrl">Additional Information</h5>
        <div class="pnl">
          <div class="accordian-content rounded-5">
            <div class="accordian-sect office-hours">
              <dl>
                <dt>Office Hours</dt>
                <dd>
                  <c:choose><c:when test="${not empty leo.hours}"><c:out value='${leo.hours}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                </dd>
              </dl>
            </div>
            <div class="accordian-sect further-instructions">
              <dl>
                <dt>Further instructions</dt>
                <dd>
                  <c:choose><c:when test="${not empty leo.furtherInstruction}">${fn:replace(fn:escapeXml(leo.furtherInstruction),newLineChar,"<br />")}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
                </dd>
              </dl>
            </div>
              <%--<div class="accordian-sect voter-registration-or-status-inquiry last">
                <dl>
                  <dt>Am I Registered?</dt>
                  <dd>${amIRegistered}</dd>
                </dl>
              </div>--%>
          </div>
        </div>
      </div>
      <div class="corrections d-xs-none">
        <a href="${regionCorrectionUri}">Update <b>${selectedRegion.name} ${selectedState.name}</b></a>
        <br /><span class="hint">(For Election Official Use Only)</span>
      </div>
    </c:if>

    <c:if test="${not empty uocavaOffice}">
      <h3><c:out value='${selectedState.name} - ${uocavaOffice.region.name}'/></h3>
              <h3>Overseas &amp; Military Voter Election Office Information</h3>
        <h4>The state of ${selectedState.name} has a universal office for overseas and military voters.<br/>
        If you are an overseas or military voter, please use the following contact information:</h4>
      <c:choose>
        <c:when test="${not empty uocavaOffice.website}">
          <h4>Local Elections Website: <a href="<c:out value='${uocavaOffice.website}'/>" target="_blank">Visit Official Website</a></h4>
        </c:when>
        <c:otherwise>
        </c:otherwise>
      </c:choose>
      <div id="addressinfo_uocava" class="accordian rounded-5">
        <h5 class="ctrl">Address Information</h5>
        <div class="pnl" style="display:none">
          <div class="accordian-content rounded-5">

            <c:forEach items="${uocavaOffice.additionalAddresses}" var="additionalAddress" varStatus="indx">
              <div class="accordian-sect additional-address <c:if test="${indx.last}">last</c:if> ">
                <dl class="row">
                  <dt class="col-12 col-sm-6">
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
                  <dd class="col-12 col-sm-6">
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
      <div id="contactdetails_uocava" class="accordian rounded-5">
        <h5 class="ctrl">Election Official Contact Details</h5>
        <div class="pnl" style="display:none">
          <div class="accordian-content rounded-5">
            <c:if test="${empty uocavaOffice.officers && empty uocavaOffice.generalEmail}">
              <div class="accordian-sect empty">
                <dl>
                  <dt>none on record</dt>
                </dl>
              </div>
            </c:if>
            <c:if test="${not empty uocavaOffice.generalEmail}">
              <div class="accordian-sect general-email">
                <dl>
                  <dt>Election Office Email Address</dt>
                  <dd>${uocavaOffice.generalEmail}</dd>
                </dl>
              </div>
            </c:if>
            <c:forEach items="${uocavaOffice.officers}" var="officer">
              <div class="accordian-sect officer">
                <dl>
                  <dt class="officeName">${officer.officeName}</dt>
                  <dd>
                    <c:choose>
                      <c:when test="${not empty officer.firstName or not empty officer.lastName }">
                        <c:out value='${officer.firstName} ${officer.lastName}'/>
                      </c:when>
                      <c:otherwise>none on record</c:otherwise>
                    </c:choose>
                  </dd>
                </dl>
                <dl>
                  <dt>Phone</dt>
                  <dd>
                    <c:choose><c:when test="${not empty officer.phone}"><c:out value='${officer.phone}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                  </dd>
                </dl>
                <dl>
                  <dt>Fax</dt>
                  <dd>
                    <c:choose><c:when test="${not empty officer.fax}"><c:out value='${officer.fax}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                  </dd>
                </dl>
                <dl>
                  <dt>Email Address</dt>
                  <dd>
                    <c:choose><c:when test="${not empty officer.email}"><c:out value='${officer.email}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                  </dd>
                </dl>
              </div>
            </c:forEach>
          </div>
        </div>
      </div>
      <div id="additionalinfo_uocava" class="accordian rounded-5">
        <h5 class="ctrl">Additional Information</h5>
        <div class="pnl">
          <div class="accordian-content rounded-5">
            <div class="accordian-sect office-hours">
              <dl>
                <dt>Office Hours</dt>
                <dd>
                  <c:choose><c:when test="${not empty uocavaOffice.hours}"><c:out value='${uocavaOffice.hours}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                </dd>
              </dl>
            </div>
            <div class="accordian-sect further-instructions">
              <dl>
                <dt>Further instructions</dt>
                <dd>
                  <c:choose><c:when test="${not empty uocavaOffice.furtherInstruction}">${fn:replace(fn:escapeXml(uocavaOffice.furtherInstruction),newLineChar,"<br />")}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
                </dd>
              </dl>
            </div>
              <%--<div class="accordian-sect voter-registration-or-status-inquiry last">
                <dl>
                  <dt>Am I Registered?</dt>
                  <dd>${amIRegistered}</dd>
                </dl>
              </div>--%>
          </div>
        </div>
      </div>
      <div class="corrections d-xs-none">
        <a href="${uocavaCorrectionUri}">Update <b>${uocavaOffice.region.name} ${selectedState.name}</b></a>
        <br /><span class="hint">(For Election Official Use Only)</span>
      </div>
    </c:if>

    <c:if test="${not empty stateVoterInformation}">
      <h3><c:out value='${selectedState.name}'/> Statewide Voter Information</h3>
      <h4>For Overseas and Military Voters Only</h4>
      <div  id="electiondeadlines" class="accordian rounded-5">
        <h5 class="ctrl">Upcoming Election Dates and Deadlines</h5>
        <div class="pnl" style="display:none">
          <c:forEach items="${localElections}" var="election" varStatus="inx">
            <table class="accordian-content rounded-5 election-deadlines" cellspacing="0">
              <thead>
              <c:if test="${empty level or election.sortOrder ne level}">
                <c:set var="level" value="${election.sortOrder}"/>
                <tr class="r0">
                  <th colspan="3" class="election-level" id="${level.name}"><h4>${level.name} Elections</h4></th>
                </tr>
              </c:if>
              <tr class="r0">
                <td class="first-col" colspan="3"><h4>${election.title} held on ${election.heldOn}</h4></td>
              </tr>
              <tr class="labels">
                <th><h4>Type</h4></th>
                <th colspan="2"><h4>Deadlines</h4></th>
              </tr>
            </thead>
              <tr class="r0">
                <td class="first-col">&nbsp;</td>
                <td class="b-left">Overseas Citizens</td>
                <td class="b-left">Uniformed Services</td>
              </tr>
              <tr class="r1">
                <td class="first-col">Absentee Voter Registration</td>
                <td  class="b-left">${election.citizenBallotReturn}</td>
                <td class="b-left">${election.citizenBallotReturn}</td>
              </tr>
              <tr class="r2">
                <td class="first-col">Ballot Request for Registered Voter</td>
                <td class="b-left">${election.citizenBallotReturn}</td>
                <td class="b-left">${election.citizenBallotReturn}</td>
              </tr>
              <tr class="r3">
                <td class="first-col">Ballot Return</td>
                <td class="b-left">${election.citizenBallotReturn}</td>
                <td class="b-left">${election.militaryBallotReturn}</td>
              </tr>
              <tr class="r4">
                <td colspan="3" class="note">${fn:replace(fn:escapeXml(election.notes),newLineChar,"<br />")}</td>
              </tr>
            </table>
          </c:forEach>
          <c:if test="${empty localElections}">
            <p class="accordian-content rounded-5 election-deadlines">There are no elections on record for this location. Election dates and
              deadlines will be posted when available. Questions or comments can be
              sent to <a href="mailto:info@overseasvotefoundation.org">info@overseasvotefoundation.org</a></p>
          </c:if>
        </div>
      </div>
      <div  id="transoptions" class="accordian rounded-5">
        <h5 class="ctrl">Voter Materials Transmission Options</h5>
        <div class="pnl" style="display:none">

          <c:forEach items="${stateVoterInformation.transmissionMethods}" var="voterTypeTransmissionMethods">
            <c:if test="${(fn:contains(voterTypeTransmissionMethods.voterType.name, 'Military') || fn:contains(voterTypeTransmissionMethods.voterType.name, 'Overseas'))}">
              <div class="table-responsive">
                <table class="accordian-content rounded-5 trans-options" cellspacing="0">
                  <tr class="r0">
                    <td colspan="6" class="first-col"><h4>${voterTypeTransmissionMethods.voterType.name}</h4></td>
                  </tr>
                  <tr class="r0">
                    <td class="first-col">&nbsp;</td>
                    <td><strong>In-Person</strong></td>
                    <td><strong>Mail</strong></td>
                    <td><strong>Fax</strong></td>
                    <td><strong>Email</strong></td>
                    <td><strong>Online</strong></td>
                  </tr>
                  <c:forEach items="${voterTypeTransmissionMethods.groupedItems}" var="items" varStatus="indx">
                    <c:choose>
                      <c:when test="${indx.first}"><c:set value="r1" var="style"/></c:when>
                      <c:when test="${indx.last}"><c:set value="r3" var="style"/></c:when>
                      <c:otherwise><c:set var="style" value="r2"/> </c:otherwise>
                    </c:choose>
                    <tr class="${style}">
                      <td class="first-col">${items.key}</td>
                      <td><c:choose>
                        <c:when test="${items.value.get(\"In-Person\")}">
                          <span class="icon-ok" aria-hidden="true"></span>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                      </c:choose></td>
                      <td class="odd"><c:choose>
                        <c:when test="${items.value.get(\"Mail\")}">
                          <span class="icon-ok" aria-hidden="true"></span>
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
                          <span class="icon-ok" aria-hidden="true"></span>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                      </c:choose></td>
                      <td><c:choose>
                        <c:when test="${items.value.get(\"Online\")}">
                          <span class="icon-ok" aria-hidden="true"></span>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                      </c:choose></td>
                    </tr>
                  </c:forEach>
                  <c:if test="${not empty voterTypeTransmissionMethods.additionalInfo}">
                    <tr class="r4">
                      <td colspan="6" class="note">
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
      <div id="statelookuptools" class="accordian rounded-5">
        <h5 class="ctrl">Lookup Tools</h5>
        <div class="pnl" style="display:none">
          <table class="accordian-content rounded-5 election-deadlines" cellspacing="0">

            <c:forEach items="${stateVoterInformation.validLookupTools}" var="lookupTool" varStatus="indx">
              <c:choose>
                <c:when test="${indx.first}"><c:set value="r1" var="style"/></c:when>
                <c:when test="${indx.last}"><c:set value="r3" var="style"/></c:when>
                <c:otherwise><c:set var="style" value="r2"/> </c:otherwise>
              </c:choose>
              <tr class="${style}">
                <td class="first-col"><strong><c:out value='${lookupTool.name}' /></strong></td>
                <td class="b-left">
                  <a target="_blank" href="<c:out value='${lookupTool.url}' />"><c:out value='${lookupTool.url}' /></a>
                </td>
              </tr>
            </c:forEach>

          </table>
        </div>
      </div>
    </c:if>

    <div class="bd select-form-bd row">
      <div class="col-12 col-sm-6 offset-sm-3">
        <c:import url="/WEB-INF/faces/basic/pages/statics/EodSelectForm.jsp" />
      </div>
    </div>
  </div>
</div>
<div class="ft"></div>
