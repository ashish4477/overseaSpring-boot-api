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

        <p>&nbsp;</p>
        <div class="row">
          <div class="col-xs-12">
            <div class="pull-right"><a href="<c:url value="/voter-registration-absentee-voting.htm"/>" class="button small">Register to Vote</a> &nbsp;
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