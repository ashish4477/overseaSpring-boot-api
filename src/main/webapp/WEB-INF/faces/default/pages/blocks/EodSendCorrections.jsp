<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Jul 10, 2007
	Time: 2:40:33 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<script>
    $(function() {
        /*$( document ).tooltip({
            position: {
                my: "center bottom-20", // the "anchor point" in the tooltip element
                at: "center top", // the position of that anchor point relative to selected element
            }
        });*/
        var availableTags = [
            <c:forEach items="${additionalAddressTypes}" var="addrType" varStatus="indx">
            "${addrType.name}"<c:if test="${not indx.last}">, </c:if>
            </c:forEach>
        ];
        $('input[id^="correctionAddressesTypeName"]').autocomplete({
            source: availableTags
        });

        $('input[id^="correctionAddressesTypeName"]').attr('title', "Standard Options: "+availableTags);
    });
</script>

<c:set var="leo" value="${correction.correctionFor}" />
<div id="eod-corrections" class="page-form body-content column-form">
<div class="hd">
    <div class="hd-inner">
        <h1 class="title">Election Official Directory - Send Corrections</h1>
    </div>
</div>
<div class="bd">
<div class="bd-inner">
<div class="last-updated">Updated&nbsp;<b><fmt:formatDate value="${leo.updated}" pattern="MM/dd/yyyy" /></b></div>
<h3><c:out value='${leo.region.state.name} - ${leo.region.name}'/></h3>
<p class="instr">This system allows you to confirm the current data on record or to submit edits for review and
    approval by OVF Election Services'staff. We estimate 24 – 48 hours for submitted changes to take effect.
    <em>Note: your name, telephone number and email are required with any data confirmation or submitted change.</em>
</p>
<p><b>This data entry screen is ONLY for use by State Election Officials.</b></p>
<spring:bind path="correction.*">
    <c:if test="${status.error}">
      <div id="update-message" class="alert alert-danger">
          <c:forEach items="${status.errorMessages}" var="errMessage">
              <h3 class="error">${errMessage}</h3>
          </c:forEach>
        <h4>To make changes or add data:</h4>
        <ol>
        <li>Click "Edit" next to the information you would like to change, enter your changes</li>
        <li>Select the option: "I have made changes, please update this information" at the bottom of the page</li>
        <li>Click Submit</li>
        </ol>

        <h4>If the information below is correct select the option:"The information is correct, no updates are necessary"</h4>
      </div>
    </c:if>
</spring:bind>

<form action="<c:url value="eodCorrections.htm"/>" name="eodForm" method="post" id="eodForm">
<%--<input type="hidden" name="leoId" value="${leo.id}"/>--%>
<h3>Address Information</h3>
<div class="sect leo-website first">
    <div class="corrected">
        <h4>Local Elections Website</h4>
        <p class="ctrl">
            <c:choose><c:when test="${not empty leo.website}"><span><c:out value='${leo.website}'/></span></c:when>
                <c:otherwise>none on record</c:otherwise></c:choose>
        </p>
        <fieldset class="pnl">
            <label>
                <span class="field-name">URL</span>
                <spring:bind path="correction.website">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.website}'/>" />
                </spring:bind>
            </label>
        </fieldset>
    </div>
    <div class="break"></div>
</div>
<div class="sect mailing-address">
    <div class="corrected">
        <h4>Mailing Address</h4>
        <div class="ctrl">
            <c:out value='${leo.mailing.addressTo}'/><br />
            <c:out value='${leo.mailing.street1}'/><br />
            <c:out value='${leo.mailing.street2}'/><br />
            <c:out value='${leo.mailing.city}'/>, <c:out value='${leo.mailing.state}'/><c:if test="${not empty leo.mailing.zip}"> <c:out value='${leo.mailing.zip}'/><c:if test="${not empty leo.mailing.zip4}">-<c:out value='${leo.mailing.zip4}'/></c:if></c:if>&nbsp;
            <form:errors path="correction.mailing.*" cssClass="errorMsg"/>
        </div>
        <fieldset class="pnl">
            <label>
                <span class="field-name">Addressee</span>
                <spring:bind path="correction.mailing.addressTo">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.mailing.addressTo}'/>" />
                </spring:bind>
            </label>
            <label>
                <span class="field-name">Street Line 1</span>
                <spring:bind path="correction.mailing.street1">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.mailing.street1}'/>" />
                </spring:bind>
            </label>
            <label>
                <span class="field-name">Street Line 2</span>
                <spring:bind path="correction.mailing.street2">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.mailing.street2}'/>" />
                </spring:bind>
            </label>
            <label>
                <span class="field-name">City, State, Zip</span>
                <spring:bind path="correction.mailing.city">
                    <input type="text" name="${status.expression}"
                           value="<c:out value='${leo.mailing.city}'/>" class="city"/>
                </spring:bind>
                <spring:bind path="correction.mailing.state">
                    <input type="text" name="${status.expression}"
                           value="<c:out value='${leo.mailing.state}'/>" class="state" maxlength="2"/>
                </spring:bind>,
                <spring:bind path="correction.mailing.zip">
                    <input type="text" name="${status.expression}"
                           value="<c:out value='${leo.mailing.zip}'/>" class="zip" maxlength="5"/>
                </spring:bind> -
                <spring:bind path="correction.mailing.zip4">
                    <input type="text" name="${status.expression}"
                           value="<c:out value='${leo.mailing.zip4}'/>" class="zip4" maxlength="4"/>
                </spring:bind>
            </label>
        </fieldset>
    </div>
    <div class="break"></div>
</div>
<div class="sect physical-address">
    <div class="corrected">
        <h4>Office Address / Express Mail Delivery</h4>
        <div class="ctrl">
            <c:out value='${leo.physical.addressTo}'/><br />
            <c:out value='${leo.physical.street1}'/><br />
            <c:out value='${leo.physical.street2}'/><br />
            <c:out value='${leo.physical.city}'/>, <c:out value='${leo.physical.state}'/><c:if test="${not empty leo.physical.zip}"> <c:out value='${leo.physical.zip}'/><c:if test="${not empty leo.physical.zip4}">-<c:out value='${leo.physical.zip4}'/></c:if></c:if>&nbsp;
            <form:errors path="correction.physical.*" cssClass="errorMsg"/>
        </div>
        <fieldset class="pnl">
            <label>
                <span class="field-name">Addressee</span>
                <spring:bind path="correction.physical.addressTo">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.physical.addressTo}'/>" />
                </spring:bind>
            </label>
            <label>
                <span class="field-name">Street Line 1</span>
                <spring:bind path="correction.physical.street1">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.physical.street1}'/>" />
                </spring:bind>
            </label>
            <label>
                <span class="field-name">Street Line 2</span>
                <spring:bind path="correction.physical.street2">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.physical.street2}'/>" />
                </spring:bind>
            </label>
            <label>
                <span class="field-name">City, State, Zip</span>
                <spring:bind path="correction.physical.city">
                    <input type="text" name="${status.expression}"
                           class="city" value="<c:out value='${leo.physical.city}'/>" />
                </spring:bind>
                <spring:bind path="correction.physical.state">
                    <input type="text" name="${status.expression}"
                           class="state" value="<c:out value='${leo.physical.state}'/>" maxlength="2"/>
                </spring:bind>,
                <spring:bind path="correction.physical.zip">
                    <input type="text" name="${status.expression}"
                           class="zip" value="<c:out value='${leo.physical.zip}'/>" maxlength="5"/>
                </spring:bind> -
                <spring:bind path="correction.physical.zip4">
                    <input type="text" name="${status.expression}"
                           class="zip4" value="<c:out value='${leo.physical.zip4}'/>" maxlength="4"/>
                </spring:bind>
            </label>
        </fieldset>
    </div>
    <div class="break"></div>
</div>
    <c:forEach items="${correction.additionalAddresses}" var="correctionAddress" varStatus="indx">
        <div class="sect additional-address">
            <div class="corrected">
                <h4>${correctionAddress.addressTypeName} <c:if test="${empty correctionAddress.addressTypeName}"><em>Create a new Additional Address<em></em></c:if>
                </h4>
                <div class="ctrl">
                    <c:out value='${correctionAddress.addressTo}'/><br />
                    <c:out value='${correctionAddress.street1}'/><br />
                    <c:out value='${correctionAddress.street2}'/><br />
                    <c:out value='${correctionAddress.city}'/>, <c:out value='${correctionAddress.state}'/><c:if test="${not empty correctionAddress.zip}"> <c:out value='${correctionAddress.zip}'/><c:if test="${not empty correctionAddress.zip4}">-<c:out value='${correctionAddress.zip4}'/></c:if></c:if>&nbsp;
                    <c:if test="${!empty correctionAddress.email}">
                        <c:out value='${correctionAddress.email}'/><br/>
                    </c:if>
                    <c:if test="${!empty correctionAddress.website}">
                        <c:out value='${correctionAddress.website}'/><br/>
                    </c:if>
                    <form:errors path="correction.additionalAddresses[${indx.index}].*" cssClass="errorMsg"/>
                </div>
                <fieldset class="pnl">
                    <label>
                        <span class="field-name">Address Type</span>
                        <spring:bind path="correction.additionalAddresses[${indx.index}].addressTypeName">
                            <input type="text" name="${status.expression}" value="<c:out value='${correctionAddress.addressTypeName}'/>"
                                    id="correctionAddressesTypeName${indx.index}" class="addy"/>
                        </spring:bind>
                    </label>
                    <label>
                        <span class="field-name">Addressee</span>
                        <spring:bind path="correction.additionalAddresses[${indx.index}].addressTo">
                            <input type="text" name="${status.expression}" value="<c:out value='${correctionAddress.addressTo}'/>" />
                        </spring:bind>
                    </label>
                    <label>
                        <span class="field-name">Street Line 1</span>
                        <spring:bind path="correction.additionalAddresses[${indx.index}].street1">
                            <input type="text" name="${status.expression}" value="<c:out value='${correctionAddress.street1}'/>" />
                        </spring:bind>
                    </label>
                    <label>
                        <span class="field-name">Street Line 2</span>
                        <spring:bind path="correction.additionalAddresses[${indx.index}].street2">
                            <input type="text" name="${status.expression}" value="<c:out value='${correctionAddress.street2}'/>" />
                        </spring:bind>
                    </label>
                    <label>
                        <span class="field-name">City, State, Zip</span>
                        <spring:bind path="correction.additionalAddresses[${indx.index}].city">
                            <input type="text" name="${status.expression}"
                                   class="city" value="<c:out value='${correctionAddress.city}'/>" />
                        </spring:bind>
                        <spring:bind path="correction.additionalAddresses[${indx.index}].state">
                            <input type="text" name="${status.expression}"
                                   class="state" value="<c:out value='${correctionAddress.state}'/>" maxlength="2"/>
                        </spring:bind>,
                        <spring:bind path="correction.additionalAddresses[${indx.index}].zip">
                            <input type="text" name="${status.expression}"
                                   class="zip" value="<c:out value='${correctionAddress.zip}'/>" maxlength="5"/>
                        </spring:bind> -
                        <spring:bind path="correction.additionalAddresses[${indx.index}].zip4">
                            <input type="text" name="${status.expression}"
                                   class="zip4" value="<c:out value='${correctionAddress.zip4}'/>" maxlength="4"/>
                        </spring:bind>
                    </label>
                    <label>
                        <span class="field-name">Email</span>
                        <spring:bind path="correction.additionalAddresses[${indx.index}].email">
                            <input type="text" name="${status.expression}" value="<c:out value='${correctionAddress.email}'/>" />
                        </spring:bind>
                    </label>
                    <label>
                        <span class="field-name">Website</span>
                        <spring:bind path="correction.additionalAddresses[${indx.index}].website">
                            <input type="text" name="${status.expression}" value="<c:out value='${correctionAddress.website}'/>" />
                        </spring:bind>
                    </label>
                </fieldset>
            </div>
            <div class="break"></div>
        </div>

    </c:forEach>
<div class="sect general-email">
    <div class="corrected">
        <h4>Election Office Email Address</h4>
        <p class="ctrl">
            <c:choose><c:when test="${not empty leo.generalEmail}"><c:out value='${leo.generalEmail}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
        <fieldset class="pnl">
            <label>
                <span class="field-name">Election Office Email Address</span>
                <spring:bind path="correction.generalEmail">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.generalEmail}'/>" />
                </spring:bind>
            </label>
        </fieldset>
        </p>
    </div>
</div>
<h3>Election Official Contact Details</h3>
<c:forEach items="${correction.officers}" var="officer" varStatus="index">
    <c:set var="corOfficer" value="${officer}"/>
    <c:if test="${index.index lt fn:length(leo.officers)}">
        <c:set var="corOfficer" value="${leo.officers[index.index]}"/>
    </c:if>
    <spring:nestedPath path="correction.officers[${index.index}]">
        <div class="sect officer${index.index}">
            <div class="corrected">
                <h4>Office Name</h4>
                <p class="ctrl">
                    <c:choose><c:when test="${not empty corOfficer.officeName}">${corOfficer.officeName}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
                </p>
                <fieldset class="pnl">
                    <label>
                        <span class="field-name">Office Name</span>
                        <spring:bind path="officeName">
                            <input type="text" name="${status.expression}" value="${corOfficer.officeName}" />
                        </spring:bind>
                    </label>
                </fieldset>
            </div>
            <div class="corrected">
                <h4>Officials Name</h4>
                <p class="ctrl">
                    <c:choose>
                        <c:when test="${not empty corOfficer.firstName or not empty corOfficer.lastName}">
                            <c:out value='${corOfficer.firstName}'/> <c:out value='${corOfficer.lastName}'/>
                        </c:when>
                        <c:otherwise>none on record</c:otherwise>
                    </c:choose>
                </p>
                <fieldset class="pnl">
                    <label>
                        <div>
                            <span class="field-name">Officials Name</span>
                            <spring:bind path="firstName">
                                <input type="text" class="firstName" name="${status.expression}"
                                       value="<c:out value='${corOfficer.firstName}'/>" />
                            </spring:bind>
                            <spring:bind path="lastName">
                                <input type="text" class="lastName" name="${status.expression}"
                                       value="<c:out value='${corOfficer.lastName}'/>" />
                            </spring:bind>
                        </div>
                    </label>
                </fieldset>
            </div>

            <div class="corrected">
                <h4>Phone</h4>
                <p class="ctrl">
                    <c:choose><c:when test="${not empty corOfficer.phone}"><c:out value='${corOfficer.phone}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                </p>
                <fieldset class="pnl">
                    <label>
                        <span class="field-name">Phone Number</span>
                        <spring:bind path="phone">
                            <input type="phone" name="${status.expression}" value="<c:out value='${corOfficer.phone}'/>" />
                        </spring:bind>
                    </label>
                </fieldset>
            </div>
            <div class="corrected">
                <h4>Fax</h4>
                <p class="ctrl">
                    <c:choose><c:when test="${not empty corOfficer.fax}"><c:out value='${corOfficer.fax}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                </p>
                <fieldset class="pnl">
                    <label>
                        <span class="field-name">Fax Number</span>
                        <spring:bind path="fax">
                            <input type="text" name="${status.expression}" value="<c:out value='${corOfficer.fax}'/>" />
                        </spring:bind>
                    </label>
                </fieldset>
            </div>
            <div class="corrected">
                <h4>Email Address</h4>
                <p class="ctrl">
                    <c:choose><c:when test="${not empty corOfficer.email}"><c:out value='${corOfficer.email}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                </p>
                <fieldset class="pnl">
                    <label>
                        <span class="field-name">Email Address</span>
                        <spring:bind path="email">
                            <input type="text" name="${status.expression}" value="<c:out value='${corOfficer.email}'/>" />
                        </spring:bind>
                    </label>
                </fieldset>
            </div>
        </div>
<br/>
    </spring:nestedPath>
</c:forEach>
<h3>Additional Information</h3>
<div class="sect further-instructions">
    <div class="corrected">
        <h4>Further Instructions</h4>
        <p class="ctrl">
            <c:choose><c:when test="${not empty leo.furtherInstruction}">${fn:replace(fn:escapeXml(leo.furtherInstruction),newLineChar,"<br />")}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
        </p>
        <fieldset class="pnl">
            <spring:bind path="correction.furtherInstruction">
                <textarea rows="12" cols="35" name="${status.expression}"><c:out value='${leo.furtherInstruction}'/></textarea>
            </spring:bind>
        </fieldset>
    </div>
    <div class="break"></div>
</div>
<div class="sect office-hours last">
    <div class="corrected">
        <h4>Office Hours</h4>
        <p class="ctrl">
            <c:choose><c:when test="${not empty leo.hours}"><c:out value='${leo.hours}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
        </p>
        <fieldset class="pnl">
            <label>
                <span class="field-name">Hours</span>
                <spring:bind path="correction.hours">
                    <input type="text" name="${status.expression}" value="<c:out value='${leo.hours}'/>" />
                </spring:bind>
            </label>
        </fieldset>
    </div>
    <div class="break"></div>
</div>
<br/>
<h3>Submitted By:</h3>
<div class="sect additional-message first">
    <h4>Update Log Notes</h4>
    <p class="note">(This Information is not displayed to the public)</p>
    <fieldset>
        <spring:bind path="correction.allCorrect">
            <label>
                <input type="radio" name="${status.expression}" value="0" required/>
                <span>My record needs to be changed; please update this contact record.</span>
            </label><br/>
            <label>
                <input type="radio" name="${status.expression}" value="1" required/>
                <span>I've reviewed this data record and it is correct; no updates are needed.</span>
            </label>
        </spring:bind>
    </fieldset>
    <spring:bind path="correction.message">
        <textarea rows="5" cols="40" name="${status.expression}"><c:out value='${status.value}'/></textarea>
    </spring:bind>
</div>
<div class="sect submitter-info last">
    <h4>Your Name<span class="required"> *</span></h4>
    <fieldset>
        <spring:bind path="correction.submitterName">
            <input type="text" required name="${status.expression}" value="<c:out value='${status.value}'/>" />
        </spring:bind>
    </fieldset>
    <h4>Your Email<span class="required"> *</span></h4>
    <fieldset>
        <spring:bind path="correction.submitterEmail">
            <input type="email" required name="${status.expression}" value="<c:out value='${status.value}'/>" />
        </spring:bind>
    </fieldset>
    <h4>Your Phone<span class="required"> *</span></h4>
    <fieldset>
        <spring:bind path="correction.submitterPhone">
            <input type="text" required name="${status.expression}" value="<c:out value='${status.value}'/>" />
        </spring:bind>
    </fieldset>
    <h4>&nbsp;</h4>
    <fieldset>
     <div class="message"><strong>PLEASE NOTE - Your changes will NOT show up on the site immediately. They will be reviewed within 48 hours of submission before being published to the site. </strong></div>
    </fieldset>
    <div class="break"></div>
</div>
<div id="continue">
    <input type="hidden" name="regionId" value="${leo.region.id}"/>
    <a href="<c:url value="/eod.htm" />?submission=true&amp;stateId=${leo.region.state.id}&amp;regionId=${leo.region.id}">
        &larr; Back to <c:out value='${leo.region.name}'/>
    </a>
    <input type="image" src="<c:url value="/img/buttons/submit-button.gif"/>" onclick="document.eodForm.submission.value = true;"/>
    <div class="break"></div>
</div>
</form>
</div>
</div>
<div class="ft">
    <div class="ft-inner"></div>
</div>
</div>