<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Jul 17, 2007
	Time: 9:53:04 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<script type="text/javascript" language="JavaScript">
	//<!--
	YAHOO.ovf.sendSnapshotUrl = "<c:url value="/ajax/EmailToOfficer.htm"/>";
	//-->
</script>

<script>
  $(function() {
  	$( document ).tooltip({
	    position: {
	        my: "center bottom-20", // the "anchor point" in the tooltip element
	        at: "center top", // the position of that anchor point relative to selected element
	    }
	});

    var availableTags = [
			<c:forEach items="${additionalAddressTypes}" var="addrType" varStatus="indx">
			"${addrType.name}"<c:if test="${not indx.last}">, </c:if>
		</c:forEach>
    ];

    $('input[id^="additionalAddressTypeName"]').autocomplete({
      source: availableTags
    });

    $('.additionalAddressTip').attr('title', "Standard Options: "+availableTags);});

	function setRemoveAddress( addressNum, addressId ) {
		$("#deleteAddress".concat(addressNum) ).val(addressId);
	}

  </script>

<div id="eod-corrections" class="column-form admin">
<div class="navigation" style="text-align:right;">
  				<a class="admin-links" href="<c:url value="/admin/EodVotingRegions.htm"><c:param name="stateId" value="${leo.region.state.id}"/><c:param name="lookFor" value="${leo.region.id}"/></c:url>">
					&lt; Back to ${leo.region.state.name}
				</a> | <a class="admin-links" href="<c:url value="/admin/EodStates.htm"/>"> &lt; States List</a> | &nbsp;
    <form action="<c:url value="/admin/EodEdit.htm"/>" method="get" style="float:right;">
          <select style="width:125px;" name="regionId" id="regionSelect" onchange="this.form.submit()">
            <option value="0">Change County</option>
            <c:forEach items="${votingRegions}" var="vLeo">
            <option value="${vLeo.region.id}">${vLeo.region.name}</option>
          </c:forEach>
          <optgroup label=""></optgroup>
          </select>
        </form>
  </div>
	<div class="hd">
		<h2>Election Official Directory</h2>
	</div>

	<div class="bd">
        <h3 style="display:inline-block;">${leo.region.state.name} - ${leo.region.name}</h3>
        <c:url value="/admin/EodEdit.htm" var="leoAction"/>
        <form:form commandName="leo" action="${leoAction}" name="eodForm" method="post" id="eodForm">
					<c:if test="${leo.id eq 0}" ><input type="hidden" name="stateId" value="${leo.region.state.id}"></c:if>

        	<div class="last-updated">Updated&nbsp;<b><fmt:formatDate value="${leo.updated}" pattern="MM/dd/yyyy" /></b></div>
        	<c:if test="${not empty messageCode}"><div id="update-message" class="success"><spring:message code="${messageCode}"/></div></c:if>

			<spring:bind path="leo.*">
				<c:if test="${status.error}">
					<div id="update-message" class="warning">There was an error in your submission, please check the fields below.</div>
				</c:if>
			</spring:bind>

        <h3>Address Information</h3>

        <div class="sect region-name first">
				<div class="corrected">
					<h4>Voting Region Name</h4>
                    <form:errors path="region.name"  class="errorMsg" />
					<p class="ctrl">${leo.region.name}</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Region Name</span>
                                <form:input path="region.name"/>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>County Type</h4>
					<form:errors path="region.county.type" class="errorMsg"/>
					<p class="ctrl">${leo.region.county.type}</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">County Type</span>
							<form:input path="region.county.type"/>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>County Name</h4>
					<form:errors path="region.county.name" class="errorMsg"/>
					<p class="ctrl">${leo.region.county.name}</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">County Name</span>
							<form:input path="region.county.name"/>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Municipality Type</h4>
					<form:errors path="region.municipality.type" class="errorMsg"/>
					<p class="ctrl">${leo.region.municipality.type}</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Municipality Type</span>
							<form:input path="region.municipality.type"/>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Municipality Name</h4>
					<form:errors path="region.municipality.name"/>
					<p class="ctrl">${leo.region.municipality.name}</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Municipality Name</span>
							<form:input path="region.municipality.name"/>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
        <div class="sect leo-addres-type">
            <div class="corrected">
                <h4>Local Office Address Type</h4>
                <form:errors path="localOfficeType"  class="errorMsg" />
                <p class="ctrl">${leo.localOfficeType}</p>
                <fieldset class="pnl">
                    <label>
                        <span class="field-name">Local Office Address Type</span>
                        <form:select path="localOfficeType" items="${localOfficeTypes}"/>
                    </label>
                </fieldset>
            </div>
            <div class="break"></div>
        </div>
			<div class="sect leo-website">
				<div class="corrected">
					<h4>Local Elections Website</h4>
                    <form:errors path="website"  class="errorMsg" />
					<p class="ctrl">
						<c:choose><c:when test="${not empty leo.website}">${leo.website}</c:when>
						<c:otherwise>none on record</c:otherwise></c:choose>
					</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">URL</span>
                                <form:input path="website"/>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
			<div class="sect mailing-address">
				<div class="corrected">
					<h4>Mailing Address</h4>
                    <form:errors path="mailing.*"  class="errorMsg" />
					<div class="ctrl">
						<p><c:out value='${leo.mailing.addressTo}'/><br />
						<c:out value='${leo.mailing.street1}'/><br />
						<c:out value='${leo.mailing.street2}'/><br />
						<c:out value='${leo.mailing.city}, ${leo.mailing.state} ${leo.mailing.zip}'/><c:if test="${not empty leo.mailing.zip4}">-<c:out value='${leo.mailing.zip4}'/></c:if></p>
					</div>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Addressee</span>
                            <form:input path="mailing.addressTo"/>
						</label>
						<label>
							<span class="field-name">Street Line 1</span>
                            <form:input path="mailing.street1"/>
						</label>
						<label>
							<span class="field-name">Street Line 2</span>							
                            <form:input path="mailing.street2"/>
						</label>
						<label>
							<span class="field-name">City, State, Zip</span>
                            <form:input path="mailing.city" cssClass="city"/>
                            <form:input path="mailing.state" cssClass="state" maxlength="2"/>,
                            <form:input path="mailing.zip" cssClass="zip" maxlength="5"/> - <form:input path="mailing.zip4" cssClass="zip4" maxlength="4"/>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
			<div class="sect physical-address">
				<div class="corrected">
					<h4>Office Address / Express Mail Delivery</h4>
                    <form:errors path="physical.*"  class="errorMsg" />
					<div class="ctrl">
						<c:out value='${leo.physical.addressTo}'/><br />
						<c:out value='${leo.physical.street1}'/><br />
						<c:out value='${leo.physical.street2}'/><br />
						<c:out value='${leo.physical.city}, ${leo.physical.state} ${leo.physical.zip}'/><c:if test="${not empty leo.mailing.zip4}">-<c:out value='${leo.physical.zip4}'/></c:if>
					</div>
					<fieldset class="pnl">
                        <label>
                            <span class="field-name">Addressee</span>
                            <form:input path="physical.addressTo"/>
                        </label>
                        <label>
                            <span class="field-name">Street Line 1</span>
                            <form:input path="physical.street1"/>
                        </label>
                        <label>
                            <span class="field-name">Street Line 2</span>
                            <form:input path="physical.street2"/>
                        </label>
                        <label>
                            <span class="field-name">City, State, Zip</span>
                            <form:input path="physical.city" cssClass="city"/>
                            <form:input path="physical.state" cssClass="state" maxlength="2"/>,
                            <form:input path="physical.zip" cssClass="zip" maxlength="5"/> - <form:input path="physical.zip4" cssClass="zip4" maxlength="4"/>
                        </label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
			<c:forEach items="${leo.additionalAddresses}" var="additionalAddress" varStatus="indx">
				<div class="sect mailing-address">
					<div class="corrected">
						<h4>${additionalAddress.type.name} <c:if test="${empty additionalAddress.type.name}"><span style="white-space:nowrap">Create a new Additional Address</span></c:if>
						<form:errors path="additionalAddresses[${indx.index}].type.name"  class="errorMsg" />
						</h4>
						<div class="ctrl">
							<c:if test="${!empty additionalAddress.address.addressTo}">
								<c:out value='${additionalAddress.address.addressTo}'/><br/>
							</c:if>	
							<c:if test="${!empty additionalAddress.address.street1}">
								<c:out value='${additionalAddress.address.street1}'/><br/>
							</c:if>
							<form:errors path="additionalAddresses[${indx.index}].address.street1"  class="errorMsg" />
							<c:if test="${!empty additionalAddress.address.street2}">
								<c:out value='${additionalAddress.address.street2}'/><br/>
							</c:if>	
							<c:if test="${!empty additionalAddress.address.city}"><c:out value='${additionalAddress.address.city}'/>,</c:if>
							<form:errors path="additionalAddresses[${indx.index}].address.city"  class="errorMsg" />
							<c:if test="${!empty additionalAddress.address.state}"><c:out value='${additionalAddress.address.state}'/></c:if>
							<form:errors path="additionalAddresses[${indx.index}].address.state"  class="errorMsg" />
							<c:if test="${!empty additionalAddress.address.zip}"><c:out value='${additionalAddress.address.zip}'/></c:if>
							<form:errors path="additionalAddresses[${indx.index}].address.zip"  class="errorMsg" />
							<c:if test="${not empty additionalAddress.address.zip4}">-<c:out value='${additionalAddress.address.zip4}'/></c:if><br/>
							<c:if test="${not empty additionalAddress.email}">Email: ${additionalAddress.email}</c:if>
							<form:errors path="additionalAddresses[${indx.index}].email"  class="errorMsg" /><br/>
							<c:if test="${not empty additionalAddress.website}">Website: ${additionalAddress.website}</c:if>
						</div>
						<fieldset class="pnl">
							<label>
								<span class="field-name">Address Type</span>
								<form:input path="additionalAddresses[${indx.index}].type.name" cssClass="addy" placeholder="Type to See Standard Options" value="${additionalAddress.type.name}" id="additionalAddressTypeName${indx.index}"/>
								<span class="additionalAddressTip" style="font-weight:bold; padding: 2px 5px; background-color:#226091; color:#fff; margin-left:5px;">?</span>
							</label>
							<label>
								<span class="field-name">Addressee</span>
								<form:input path="additionalAddresses[${indx.index}].address.addressTo"/>
							</label>
							<label>
								<span class="field-name">Street Line 1</span>
								<form:input path="additionalAddresses[${indx.index}].address.street1"/>
							</label>
							<label>
								<span class="field-name">Street Line 2</span>
								<form:input path="additionalAddresses[${indx.index}].address.street2"/>
							</label>
							<label>
								<span class="field-name">City, State, Zip</span>
								<form:input path="additionalAddresses[${indx.index}].address.city" cssClass="city"/>
								<form:input path="additionalAddresses[${indx.index}].address.state" cssClass="state" maxlength="2"/>,
								<form:input path="additionalAddresses[${indx.index}].address.zip" cssClass="zip" maxlength="5"/> - <form:input path="additionalAddresses[${indx.index}].address.zip4" cssClass="zip4" maxlength="4"/>
							</label>
							<label>
								<span class="field-name">Email</span>
								<form:input path="additionalAddresses[${indx.index}].email" cssClass="email" value="${additionalAddress.email}"/>
							</label>
							<label>
								<span class="field-name">Website</span>
								<form:input path="additionalAddresses[${indx.index}].website" cssClass="website" value="${additionalAddress.website}"/>
							</label>
							<div style="margin: 6px 0 6px 215px; width:250px;">
								<input type="checkbox" name="Delete" class="inline delete" value="Delete address" onclick="setRemoveAddress(${indx.index},${additionalAddress.id});"> <%-- $('#deleteAddress${indx.index}').value = ${additionalAddress.id};">--%>
								<label>Remove This Address</label>
							</div>
						</fieldset>
					</div>
					<div class="break"></div>
				</div>
				<input type="hidden" name="deleteAddress" id="deleteAddress${indx.index}" value="0">
			</c:forEach>

            <div class="sect general-email">
                <div class="corrected">
                    <h4>Election Office Email Address</h4>
                    <form:errors path="generalEmail"  class="errorMsg" />
                    <p class="ctrl">
                        <c:choose><c:when test="${not empty leo.generalEmail}">${leo.generalEmail}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
                    </p>
                    <fieldset class="pnl">
                        <label>
                            <span class="field-name">Election Office Email Address</span>
                            <form:input path="generalEmail"/>
                        </label>
                    </fieldset>
                </div>
                <div class="break"></div>
            </div>
      <h3>Election Official Contact Details</h3>
        <span class="success" id="leoEmailFeedback"></span>

            <c:forEach items="${leo.officers}" var="officer" varStatus="indx">
			<div class="sect local-election-official">
        <h3>${officer.officeName}
              <c:if test="${indx.index eq 0}">
                  (The first contact serves as Local Election Official.)
              </c:if>
              <c:if test="${indx.index eq 1}">
                  (The second contact serves as Absentee Voter Clerk.)
              </c:if>
              <c:if test="${indx.index ge 2}">
                  (This contact serves as Additional Contact.)
              </c:if>
          </h3>
                <spring:bind path="officers[${indx.index}]" >
                    <c:if test="${officer.id eq 0 and status.errors.errorCount gt 0}">
                        <%-- TODO: a new contact has errors. all input fields should be enabled and displayed.
                        Otherwise previously entered correct values could be lost. See Redmine #4420
                        --%>
                    </c:if>
                </spring:bind>

                <div class="corrected">
                    <h4>Office Name</h4>
                    <form:errors path="officers[${indx.index}].officeName"  class="errorMsg" />
                    <p class="ctrl">
                      <c:choose>
							        <c:when test="${not empty officer.officeName}">
								      ${officer.officeName}
							       </c:when>
							        <c:otherwise>none on record</c:otherwise>
						         </c:choose>
                    </p>
                    <fieldset class="pnl">
                        <label>
                            <span class="field-name">Office Name</span>
                            <form:input placeholder="Office Name" path="officers[${indx.index}].officeName" cssClass="firstName"/>
                        </label>
                    </fieldset>
                </div>
				<div class="corrected">
					<h4>Person Name</h4>
                    <form:errors path="leo"  class="errorMsg" />
					<p class="ctrl">
						<c:choose>
							<c:when test="${not empty officer.firstName or not empty officer.lastName}">
								${officer.firstName} ${officer.lastName}
							</c:when>
							<c:otherwise>none on record</c:otherwise>
						</c:choose>
					</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Official Name</span>
               <form:input placeholder="First Name" path="officers[${indx.index}].firstName" cssClass="firstName"/>
               <form:input placeholder="Last Name" path="officers[${indx.index}].lastName" cssClass="lastName"/>
						</label>
					</fieldset>
				</div>

				<div class="corrected">
					<h4>Phone</h4>
                    <form:errors path="officers[${indx.index}].phone" class="errorMsg" />
					<p class="ctrl">
						<c:choose><c:when test="${not empty officer.phone}"><c:out value='${officer.phone}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
					</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Phone Number</span>
               <form:input placeholder="(xxx) xxx-xxxx" path="officers[${indx.index}].phone"/>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Fax</h4>
                    <form:errors path="officers[${indx.index}].fax"  class="errorMsg" />
					<p class="ctrl">
						<c:choose><c:when test="${not empty officer.fax}">${officer.fax}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
					</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Fax Number</span>
              <form:input placeholder="(xxx) xxx-xxxx" path="officers[${indx.index}].fax"/>
						</label>
					</fieldset>
				</div>
				<div class="corrected">
					<h4>Email Address</h4>
                    <form:errors path="officers[${indx.index}].email"  class="errorMsg" />
					<p class="ctrl">
						<c:choose><c:when test="${not empty officer.email}">${officer.email}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
					</p>
					<fieldset class="pnl">
						<label>
							<span class="field-name">Email Address</span>
              <form:input path="officers[${indx.index}].email"/>
						</label>
					</fieldset>
          <c:if test="${not empty officer.email}"><br/><button class="snapshot" onclick="YAHOO.ovf.sendSnapshot('leoId=${leo.id}&recipient=${indx.index}','leoEmailFeedback'); return false;">Send Snapshot to Official</button>
          </c:if>
				</div>
				<div class="break"></div>
			</div>
      </c:forEach>
        <div>
            <button onclick="YAHOO.ovf.sendSnapshot('leoId=${leo.id}','allEmailFeedback'); return false;">Send Snapshot to ALL Officials</button>
       <span id="allEmailFeedback"></span>
        </div>
      <h3>Additional Information</h3>
			<div class="sect further-instructions">
				<div class="corrected">
					<h4>Further instructions</h4>
                    <form:errors path="furtherInstruction"  class="errorMsg" />
					<p class="ctrl">
						<c:choose><c:when test="${not empty leo.furtherInstruction}">${fn:replace(fn:escapeXml(leo.furtherInstruction),newLineChar,"<br />")}</c:when>
						<c:otherwise>none on record</c:otherwise></c:choose>
					</p>
					<fieldset class="pnl">
						<label>
                            <form:textarea path="furtherInstruction"  rows="12" cols="35"/>
						</label>
					</fieldset>
				</div>
				<div class="break"></div>
			</div>
            <div class="sect office-hours">
                <div class="corrected">
                    <h4>Hours</h4>
                    <form:errors path="hours"  class="errorMsg" />
                    <p class="ctrl">
                        <c:choose><c:when test="${not empty leo.hours}"><c:out value='${leo.hours}'/></c:when>
                        <c:otherwise>none on record</c:otherwise></c:choose>
                    </p>
                    <fieldset class="pnl">
                        <label>
                           	<span class="field-name">Hours</span>
                            <form:input path="hours"/>
                        </label>
                    </fieldset>
                </div>
                <div class="break"></div>
            </div>
			<div style="margin-bottom:12px; text-align:right;">
                <c:choose>
                    <c:when test="${leo.region.id ne 0}">
                        <input type="hidden" name="regionId" value="${leo.region.id}"/>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="stateId" value="${leo.region.state.id}"/>
                    </c:otherwise>
                </c:choose>

				<input type="hidden" name="save" value="Save"/>
        <button name="save" value="save">Save</button>
        <p>&nbsp;</p>

			</div>
        </form:form>
		</div>
		<div class="ft"></div>
	</div>
<form action="<c:url value="/ajax/EmailToOfficer.htm"/>" method="get" id="emptyForm" ></form> 
