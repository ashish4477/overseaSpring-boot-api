<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Jul 12, 2007
	Time: 8:03:06 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<c:set var="leo" value="${correction.correctionFor}" />
<div id="eod-correction-approve" class="column-form">
	<div class="hd">
		<h2>Election Official Directory - Approve/Deny Corrections</h2>
	</div>
	<div class="bd">
		<div class="last-updated">Updated&nbsp;<b><fmt:formatDate value="${correction.correctionFor.updated}" pattern="MM/dd/yyyy" /></b></div>
		<h3>${correction.correctionFor.region.state.name} - ${correction.correctionFor.region.name}</h3>
		<c:choose>
			<c:when test="${correction.status eq 1}"><h3 class="text-success">Corrections are ready for approval</h3></c:when>
			<c:when test="${correction.status eq 2}"><h3 class="text-success">Corrections have beean already approved</h3></c:when>
			<c:when test="${correction.status eq 3}"><h3 class="text-danger">Corrections have been denied</h3></c:when>
		</c:choose>
		<form action="<c:url value="/admin/EodCorrectionsEdit.htm"/>" name="eodForm" method="post" id="eodForm">
		<spring:nestedPath path="correction.correctionFor"><table>
			<thead>
				<tr>
					<th>Field</th><th>Current Value</th><th>Suggested Correction</th>
					<c:if test="${correction.status eq 1}"><th><input class="select-all" type="checkbox" /> All</th></c:if>
				</tr>
			</thead>
			<tbody>
				<c:if test="${correction.website ne correction.correctionFor.website }">
					<tr class="approve-deny">
						<th>Local Elections Website</th>
						<td>
							<c:choose>
								<c:when test="${not empty correction.correctionFor.website}">
									<a href="<c:out value='${correction.correctionFor.website}'/>" target="_blank">
										<c:out value='${correction.correctionFor.website}'/>
									</a>
								</c:when>
								<c:otherwise>
									none on record
								</c:otherwise>
							</c:choose>
						</td>
						<td class="correction">
							<em><a href="<c:out value='${correction.website}'/>" target="_blank"><c:out value='${correction.website}'/></a></em>

							<spring:bind path="website">
								<input type="hidden" class="corrected" name="${status.expression}"
									value="<c:out value='${correction.website}'/>" disabled="disabled"/>
							</spring:bind>

						</td>
						<c:if test="${correction.status eq 1}">
						<td class="confirm-deny-checkbox">
							<label>
								<input type="checkbox" class="ctrl" />
								<span class="status">Denied</span>
							</label>
						</td>
						</c:if>
					</tr>
				</c:if>

				<c:if test="${correction.mailing ne correction.correctionFor.mailing }">
					<tr class="approve-deny">
						<th>Mailing Address</th>
						<td>
							<c:out value='${correction.correctionFor.mailing.addressTo}'/> <br />
							<c:out value='${correction.correctionFor.mailing.street1}'/> <br />
							<c:out value='${correction.correctionFor.mailing.street2}'/> <br />
							<c:out value='${correction.correctionFor.mailing.city} ${correction.correctionFor.mailing.state}'/><c:if test="${not empty correction.correctionFor.mailing.state and not empty correction.correctionFor.mailing.zip}">, </c:if>
							<c:out value='${correction.correctionFor.mailing.zip}'/>
							<c:if test="${not empty correction.correctionFor.mailing.zip4}">
								- <c:out value='${correction.correctionFor.mailing.zip4}'/>
							</c:if>
						</td>
                        <td class="correction">
                            <em><c:out value='${correction.mailing.addressTo}'/></em>

                            <spring:bind path="mailing.addressTo">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.mailing.addressTo}'/>" disabled="disabled"/>
                            </spring:bind>

                            <br />
                            <em><c:out value='${correction.mailing.street1}'/></em>

                            <spring:bind path="mailing.street1">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.mailing.street1}'/>" disabled="disabled"/>
                            </spring:bind>

                            <br />
                            <em><c:out value='${correction.mailing.street2}'/></em>

                            <spring:bind path="mailing.street2">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.mailing.street2}'/>" disabled="disabled"/>
                            </spring:bind>

                            <br />
                            <em><c:out value='${correction.mailing.city}'/></em>

                            <spring:bind path="mailing.city">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.mailing.city}'/>" disabled="disabled"/>
                            </spring:bind>

                            <em><c:out value='${correction.mailing.state}'/></em><c:if test="${not empty correction.mailing.zip or not empty correction.correctionFor.mailing.zip}">, </c:if>

                            <spring:bind path="mailing.state">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.mailing.state}'/>" disabled="disabled"/>
                            </spring:bind>

                            <em><c:out value='${correction.mailing.zip}'/></em>

                            <spring:bind path="mailing.zip">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.mailing.zip}'/>" disabled="disabled"/>
                            </spring:bind>

                            - <em><c:out value='${correction.mailing.zip4}'/></em>

                            <spring:bind path="mailing.zip4">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.mailing.zip4}'/>" disabled="disabled"/>
                            </spring:bind>

                        </td>
						<c:if test="${correction.status eq 1}">
						<td class="confirm-deny-checkbox">
							<label>
								<input type="checkbox" class="ctrl"/>
								<span class="status">Denied</span>
							</label>
						</td>
						</c:if>
					</tr>
				</c:if>

				<c:if test="${correction.physical ne correction.correctionFor.physical}">
					<tr class="approve-deny">
						<th>Office Address / Express Mail Delivery</th>
						<td>
							<c:out value='${correction.correctionFor.physical.addressTo}'/> <br />
							<c:out value='${correction.correctionFor.physical.street1}'/> <br />
							<c:out value='${correction.correctionFor.physical.street2}'/> <br />
							<c:out value='${correction.correctionFor.physical.city} ${correction.correctionFor.physical.state}'/><c:if test="${not empty correction.correctionFor.physical.state and not empty correction.correctionFor.physical.zip}">, </c:if>
							<c:out value='${correction.correctionFor.physical.zip}'/>
							<c:if test="${not empty correction.correctionFor.physical.zip4}">
								- <c:out value='${correction.correctionFor.physical.zip4}'/>
							</c:if>
						</td>
						<td class="correction">
                            <em><c:out value='${correction.physical.addressTo}'/></em>

                            <spring:bind path="physical.addressTo">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.physical.addressTo}'/>" disabled="disabled"/>
                            </spring:bind>

                            <br />
                            <em><c:out value='${correction.physical.street1}'/></em>

                            <spring:bind path="physical.street1">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.physical.street1}'/>" disabled="disabled"/>
                            </spring:bind>

                            <br />
                            <em><c:out value='${correction.physical.street2}'/></em>

                            <spring:bind path="physical.street2">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.physical.street2}'/>" disabled="disabled"/>
                            </spring:bind>

                            <br />
                            <em><c:out value='${correction.physical.city}'/></em>

                            <spring:bind path="physical.city">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.physical.city}'/>" disabled="disabled"/>
                            </spring:bind>

                            <em><c:out value='${correction.physical.state}'/></em><c:if test="${not empty correction.physical.zip or not empty correction.correctionFor.physical.zip}">, </c:if>

                            <spring:bind path="physical.state">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.physical.state}'/>" disabled="disabled"/>
                            </spring:bind>

                            <em><c:out value='${correction.physical.zip}'/></em>

                            <spring:bind path="physical.zip">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.physical.zip}'/>" disabled="disabled"/>
                            </spring:bind>

                            - <em><c:out value='${correction.physical.zip4}'/></em>

                            <spring:bind path="physical.zip4">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${correction.physical.zip4}'/>" disabled="disabled"/>
                            </spring:bind>

                        </td>
						<c:if test="${correction.status eq 1}">
							<td class="confirm-deny-checkbox">
								<label>
									<input type="checkbox" class="ctrl"/>
									<span class="status">Denied</span>
								</label>
							</td>
						</c:if>
					</tr>
				</c:if>
				<c:forEach items="${correction.additionalAddresses}" var="correctionAddress" varStatus="indx">
					<c:if test="${correctionAddress ne correction.correctionFor.additionalAddresses[indx.index] }">
						<c:set var="sourceAddress" value="${correction.correctionFor.additionalAddresses[indx.index]}"/>
						<tr class="approve-deny">
							<th>${sourceAddress.type.name}</th>
							<td>
								${sourceAddress.type.name} <br />
								<c:out value='${sourceAddress.address.addressTo}'/> <br />
								<c:out value='${sourceAddress.address.street1}'/> <br />
								<c:out value='${sourceAddress.address.street2}'/> <br />
								<c:out value='${sourceAddress.address.city} ${sourceAddress.address.state}'/><c:if test="${not empty sourceAddress.address.state and not empty sourceAddress.address.zip}">, </c:if>
								<c:out value='${sourceAddress.address.zip}'/>
								<c:if test="${not empty sourceAddress.address.zip4}">
									- <c:out value='${sourceAddress.address.zip4}'/>
								</c:if> <br/>
									<c:out value='${sourceAddress.email}'/><br/>
									<c:out value='${sourceAddress.website}'/>
							</td>
							<td class="correction">
								<em>${correctionAddress.addressTypeName}</em>

								<spring:bind path="additionalAddresses[${indx.index}].type.name">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.addressTypeName}'/>" disabled="disabled"/>
								</spring:bind>
								<br />

								<em><c:out value='${correctionAddress.addressTo}'/></em>

								<spring:bind path="additionalAddresses[${indx.index}].address.addressTo">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.addressTo}'/>" disabled="disabled"/>
								</spring:bind>

								<br />
								<em><c:out value='${correctionAddress.street1}'/></em>

								<spring:bind path="additionalAddresses[${indx.index}].address.street1">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.street1}'/>" disabled="disabled"/>
								</spring:bind>

								<br />
								<em><c:out value='${correctionAddress.street2}'/></em>

								<spring:bind path="additionalAddresses[${indx.index}].address.street2">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.street2}'/>" disabled="disabled"/>
								</spring:bind>

								<br />
								<em><c:out value='${correctionAddress.city}'/></em>

								<spring:bind path="additionalAddresses[${indx.index}].address.city">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.city}'/>" disabled="disabled"/>
								</spring:bind>

								<em><c:out value='${correctionAddress.state}'/></em><c:if test="${not empty correctionAddress.zip or not empty sourceAddress.address.zip}">, </c:if>

								<spring:bind path="additionalAddresses[${indx.index}].address.state">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.state}'/>" disabled="disabled"/>
								</spring:bind>

								<em><c:out value='${correctionAddress.zip}'/></em>

								<spring:bind path="additionalAddresses[${indx.index}].address.zip">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.zip}'/>" disabled="disabled"/>
								</spring:bind>

								- <em><c:out value='${correctionAddress.zip4}'/></em>

								<spring:bind path="additionalAddresses[${indx.index}].address.zip4">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.zip4}'/>" disabled="disabled"/>
								</spring:bind>

								<br />
								<em><c:out value='${correctionAddress.email}'/></em>

								<spring:bind path="additionalAddresses[${indx.index}].email">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.email}'/>" disabled="disabled"/>
								</spring:bind>

								<br />
								<em><c:out value='${correctionAddress.website}'/></em>

								<spring:bind path="additionalAddresses[${indx.index}].website">
									<input type="hidden" class="corrected" name="${status.expression}"
										   value="<c:out value='${correctionAddress.website}'/>" disabled="disabled"/>
								</spring:bind>
							</td>
							<c:if test="${correction.status eq 1}">
								<td class="confirm-deny-checkbox">
									<label>
										<input type="checkbox" class="ctrl"/>
										<span class="status">Denied</span>
									</label>
								</td>
							</c:if>
						</tr>
					</c:if>

				</c:forEach>

                <c:if test="${correction.generalEmail ne correction.correctionFor.generalEmail }">
                    <tr class="approve-deny">
                        <th>Election Office Email Address</th>
                        <td>
                            <c:choose>
                                <c:when test="${not empty correction.correctionFor.generalEmail}">
                                        <c:out value='${correction.correctionFor.generalEmail}'/>
                                </c:when>
                                <c:otherwise>
                                    none on record
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="correction">
                            <em><c:out value='${correction.generalEmail}'/></em>

                            <spring:bind path="generalEmail">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                    value="<c:out value='${correction.generalEmail}'/>" disabled="disabled"/>
                            </spring:bind>

                        </td>
						<c:if test="${correction.status eq 1}">
							<td class="confirm-deny-checkbox">
								<label>
									<input type="checkbox" class="ctrl"/>
									<span class="status">Denied</span>
								</label>
							</td>
						</c:if>
                    </tr>
                </c:if>

                <c:forEach items="${correction.officers}" var="corOfficer" varStatus="index">
                    <c:set var="officer" value="${correction.correctionFor.officers[index.index]}"/>
                    <c:if test="${corOfficer.officeName ne officer.officeName}">
                        <tr class="approve-deny">
                            <th>Office Name</th>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty officer.officeName}">
                                        <c:out value='${officer.officeName}'/>
                                    </c:when>
                                    <c:otherwise>
                                        none on record
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="correction">
                                <em><c:out value='${corOfficer.officeName}'/></em>

                                <spring:bind path="officeName">
                                    <input type="hidden" class="corrected" name="${status.expression}"
                                        value="<c:out value='${corOfficer.officeName}'/>" disabled="disabled"/>
                                </spring:bind>

                            </td>
							<c:if test="${correction.status eq 1}">
								<td class="confirm-deny-checkbox">
									<label>
										<input type="checkbox" class="ctrl"/>
										<span class="status">Denied</span>
									</label>
								</td>
							</c:if>
                        </tr>
                    </c:if>
                    <c:if test="${corOfficer.firstName ne officer.firstName or corOfficer.lastName ne officer.lastName}">
					<tr class="approve-deny">
						<th>Local Election Official</th>
						<td>
							<c:out value='${officer.firstName} ${officer.lastName}'/>
						</td>
						<td class="correction">

                            <spring:bind path="firstName">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${corOfficer.firstName}'/>" disabled="disabled"/>
                            </spring:bind>

                            <em><c:out value='${corOfficer.firstName}'/></em>

                            <spring:bind path="lastName">
                                <input type="hidden" class="corrected" name="${status.expression}"
                                       value="<c:out value='${corOfficer.lastName}'/>" disabled="disabled"/>
                            </spring:bind>

                            <em><c:out value='${corOfficer.lastName}'/></em>
                        </td>
						<c:if test="${correction.status eq 1}">
							<td class="confirm-deny-checkbox">
								<label>
									<input type="checkbox" class="ctrl"/>
									<span class="status">Denied</span>
								</label>
							</td>
						</c:if>
					</tr>
				</c:if>

				<c:if test="${fn:trim(corOfficer.phone) ne fn:trim(officer.phone) }">
					<tr class="approve-deny">
						<th>Phone</th>
						<td>
							<c:choose>
								<c:when test="${not empty officer.phone}">
									<c:out value='${officer.phone}'/>
								</c:when>
								<c:otherwise>
									none on record
								</c:otherwise>
							</c:choose>
						</td>
						<td class="correction">
							<em><c:out value='${corOfficer.phone}'/></em>

							<spring:bind path="phone">
								<input type="hidden" class="corrected" name="${status.expression}"
									value="<c:out value='${corOfficer.phone}'/>" disabled="disabled"/>
							</spring:bind>
						</td>
						<c:if test="${correction.status eq 1}">
							<td class="confirm-deny-checkbox">
								<label>
									<input type="checkbox" class="ctrl"/>
									<span class="status">Denied</span>
								</label>
							</td>
						</c:if>
					</tr>
				</c:if>

				<c:if test="${fn:trim(corOfficer.fax) ne fn:trim(officer.fax)}">
					<tr class="approve-deny">
						<th>Fax</th>
						<td>
							<c:choose>
								<c:when test="${not empty officer.fax}">
									<c:out value='${officer.fax}'/>
								</c:when>
								<c:otherwise>
									none on record
								</c:otherwise>
							</c:choose>
						</td>
						<td class="correction">
							<em><c:out value='${corOfficer.fax}'/></em>

							<spring:bind path="fax">
								<input type="hidden" class="corrected" name="${status.expression}"
									value="<c:out value='${corOfficer.fax}'/>" disabled="disabled"/>
							</spring:bind>

						</td>
						<c:if test="${correction.status eq 1}">
							<td class="confirm-deny-checkbox">
								<label>
									<input type="checkbox" class="ctrl"/>
									<span class="status">Denied</span>
								</label>
							</td>
						</c:if>
					</tr>
				</c:if>
				<c:if test="${corOfficer.email ne officer.email}">
					<tr class="approve-deny">
						<th>Email</th>
						<td>
							<c:choose>
								<c:when test="${not empty officer.email}">
									<c:out value='${officer.email}'/>
								</c:when>
								<c:otherwise>
									none on record
								</c:otherwise>
							</c:choose>
						</td>
						<td class="correction">
							<em><c:out value='${corOfficer.email}'/></em>

							<spring:bind path="email">
								<input type="hidden" class="corrected" name="${status.expression}"
									value="<c:out value='${corOfficer.email}'/>" disabled="disabled"/>
							</spring:bind>

						</td>
						<c:if test="${correction.status eq 1}">
							<td class="confirm-deny-checkbox">
								<label>
									<input type="checkbox" class="ctrl"/>
									<span class="status">Denied</span>
								</label>
							</td>
						</c:if>
					</tr>
				</c:if></spring:nestedPath>
                </c:forEach>

				<c:if test="${correction.furtherInstruction ne correction.correctionFor.furtherInstruction}">
					<tr class="approve-deny">
						<th>Further Instructions</th>
						<td>
							<c:choose>
								<c:when test="${not empty correction.correctionFor.furtherInstruction}">
									${fn:replace(fn:escapeXml(correction.correctionFor.furtherInstruction),newLineChar,"<br />")}
								</c:when>
								<c:otherwise>
									none on record
								</c:otherwise>
							</c:choose>
						</td>
						<td class="correction">
							${fn:replace(fn:escapeXml(correction.furtherInstruction),newLineChar,"<br />")}

							<spring:bind path="furtherInstruction">
								<input type="hidden" class="corrected" name="${status.expression}"
									value="<c:out value='${correction.furtherInstruction}'/>" disabled="disabled"/>
							</spring:bind>

						</td>
						<c:if test="${correction.status eq 1}">
							<td class="confirm-deny-checkbox">
								<label>
									<input type="checkbox" class="ctrl"/>
									<span class="status">Denied</span>
								</label>
							</td>
						</c:if>
					</tr>
				</c:if>
		        <c:if test="${correction.hours ne correction.correctionFor.hours}">
		            <tr class="approve-deny">
		                <th>Office Hours</th>
		                <td>
		                    <c:choose>
		                        <c:when test="${not empty correction.correctionFor.hours}">
		                            <c:out value='${correction.correctionFor.hours}'/>
		                        </c:when>
		                        <c:otherwise>
		                            none on record
		                        </c:otherwise>
		                    </c:choose>
		                </td>
		                <td class="correction">
		                    <c:out value='${correction.hours}'/>

		                    <spring:bind path="hours">
		                        <input type="hidden" class="corrected" name="${status.expression}"
		                            value="<c:out value='${correction.hours}'/>" disabled="disabled"/>
		                    </spring:bind>

		                </td>
						<c:if test="${correction.status eq 1}">
							<td class="confirm-deny-checkbox">
								<label>
									<input type="checkbox" class="ctrl"/>
									<span class="status">Denied</span>
								</label>
							</td>
						</c:if>
		            </tr>
		        </c:if>
            </tbody>
		    </table>
    <table class="status">
      <tr>
        <td>
			    <div>
					<c:if test="${correction.status eq 1}">
              <h4>Status</h4>
                <c:choose>
                  <c:when test="${correction.allCorrect eq 0}"><h3 class="text-danger">Changes have been made to this record. The information needs to be reviewed and approved/updated.</h3></c:when>
                  <c:otherwise><h3 class="text-success">Confirmed as correct, The information is ready for approval.</h3></c:otherwise>
                </c:choose>
					</c:if>
              <h4>Update Log Notes</h4>
              <p><c:out value='${correction.message}'/></p>
              <h4>Submitted by</h4>
              <p>
                <c:out value='${correction.submitterName}'/><br />
                Email: <a href="mailto:<c:out value='${correction.submitterEmail}'/>"><c:out value='${correction.submitterEmail}'/></a><br />
                Phone: <c:out value='${correction.submitterPhone}'/>
              </p>
           </div>
          </td>
        </tr>
      </table>
			<c:if test="${correction.status eq 1}">
			<fieldset>
				<input type="hidden" name="correctionsId" value="${correction.id}"/>
        <input type="submit" class="button" name="decline" value="Reject all corrections" <%--onclick="document.eodForm.submission.value = true;"--%>>
				<input type="submit" class="button" name="accept"  value="Accept changes" <%--onclick="document.eodForm.submission.value = true;"--%>>
			</fieldset>
			</c:if>

			<h3>Confirmation Contact Information</h3>
			<table>
				<tr class="confirmation-header">
					<th>Field</th>
					<th>Confirmed Data</th>
				</tr>
				<tr class="confirmation">
					<th>Local Elections Website</th>
					<td>
						<c:choose>
							<c:when test="${not empty correction.correctionFor.website}">
								<a href="${correction.correctionFor.website}">
									<c:out value='${correction.correctionFor.website}'/>
								</a>
							</c:when>
							<c:otherwise>
								none on record
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr class="confirmation">
					<th>Mailing Address</th>
					<td>
						<c:out value='${correction.correctionFor.mailing.addressTo}'/> <br />
						<c:out value='${correction.correctionFor.mailing.street1}'/> <br />
						<c:out value='${correction.correctionFor.mailing.street2}'/> <br />
						<c:out value='${correction.correctionFor.mailing.city} ${correction.correctionFor.mailing.state}'/><c:if test="${not empty correction.correctionFor.mailing.state and not empty correction.correctionFor.mailing.zip}">, </c:if>
						<c:out value='${correction.correctionFor.mailing.zip}'/>
						<c:if test="${not empty correction.correctionFor.mailing.zip4}">
							- <c:out value='${correction.correctionFor.mailing.zip4}'/>
						</c:if>
					</td>
				</tr>
				<tr class="confirmation">
					<tr class='confirmation'>
						<th>Local Election Official</th>
						<td colspan="3">
							<c:out value="${correction.correctionFor.leo.firstName} ${correction.correctionFor.leo.lastName}"/>
						</td>
					</tr>
				</tr>
				<tr class="confirmation">
					<th><acronym title="Local Election Official">LEO</acronym> Phone</th>
					<td>
						<c:choose>
							<c:when test="${not empty correction.correctionFor.leoPhone}">
								<c:out value='${correction.correctionFor.leoPhone}'/>
							</c:when>
							<c:otherwise>
								none on record
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr class="confirmation">
					<th><acronym title="Local Election Official">LEO</acronym> Email</th>
					<td>
						<c:choose>
							<c:when test="${not empty correction.correctionFor.leoEmail}">
								<a href="mailto:${correction.correctionFor.leoEmail}">
									<c:out value='${correction.correctionFor.leoEmail}'/>
								</a>
							</c:when>
							<c:otherwise>
								none on record
							</c:otherwise>
						</c:choose>
					</td>
				</tr>

			</table>
        </spring:nestedPath>
		</form>
	</div>
	<div class="ft"></div>
</div>
