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
<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<script type="text/javascript">
	function setRemoveAddress( addressNum, addressId ) {
		$("#deleteAddress".concat(addressNum) ).val(addressId);
	}
</script>

<c:set var="leo" value="${correction.correctionFor}" />
<div id="eod-form" class="wide-content column-form">
	<div class="hd">
		<h1 class="title">Election Official Directory - Send Corrections</h1>
	</div>
	<div class="bd" id="eod-corrections">
		<div class="last-updated">Updated&nbsp;<b><fmt:formatDate value="${leo.updated}" pattern="MM/dd/yyyy" /></b></div>
		<h3><c:out value='${leo.region.state.name} - ${leo.region.name}'/></h3>
		<p class="instr">This system allows you to confirm the current data on record or to submit edits for review and 
			approval by OVF Election Services’ staff. We estimate 24 – 48 hours for submitted changes to take effect. 
			<em>Note: your name, telephone number and email are required with any data confirmation or submitted change.</em>
		</p>
		<p><b>This data entry screen is for Election Officials ONLY. Voters please go to Register to Vote or to the Voter Help Desk.</b></p>
<spring:bind path="correction.*">
    <c:if test="${status.error}">
      <div id="update-message" class="alert alert-danger">
        <c:forEach items="${status.errorMessages}" var="errMessage">
			<h3 class="error">${errMessage}</h3>
		</c:forEach>
        <h4>To make changes or add data:</h4>
        <ol>
        <li>Click "Edit" next to the information you would like to change, enter your changes</li>
        <li>Select the option: "My record needs to be changed; please update this contact record" at the bottom of the page</li>
        <li>Click Submit</li>
        </ol>

        <h4>If the information below is correct select the option:"I've reviewed this data record and it is correct; no updates are needed"</h4>
      </div>
    </c:if>
</spring:bind>

		<form action="<c:url value="eodCorrections.htm"/>" name="eodForm" method="post" id="eodForm">
            <%--<input type="hidden" name="leoId" value="${leo.id}"/>--%>

			<table class="section leo-website" id="eod-corrections">
        <tr>
          <td colspan="2"><h3>Address Information</h3></td>
        </tr>
				<tr>
					<th>Local Elections Website</th>
					<td class="corrected">
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
					</td>
				</tr>
				
			</table>
			<table class="section address">
				<tr>
					<td colspan="2"><hr/></td>
				</tr>   
				<tr>
					<th>Mailing Address</th>
					<td class="corrected">
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
					</td>
				</tr>
			</table>
			<table class="section address">
				<tr>
					<td colspan="2"><hr/></td>
				</tr>  
				<tr>
					<th>Office Address / Express Mail Delivery</th>
					<td class="corrected">
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
					</td>
				</tr>
			</table>
				<c:forEach items="${correction.additionalAddresses}" var="correctionAddress" varStatus="indx">
					<table class="section additional-address">
						<tr>
							<td colspan="2"><hr/></td>
						</tr>  
						<tr>
							<th>${correctionAddress.addressTypeName} <c:if test="${empty correctionAddress.addressTypeName}"><em>Create a new Additional Address<em></em></c:if> </th>

							<td class="corrected">
							<div class="ctrl">
								<form:errors path="correction.additionalAddresses[${indx.index}].addressTypeName"  class="errorMsg" />
								<c:if test="${!empty correctionAddress.addressTo}">
									<c:out value='${correctionAddress.addressTo}'/><br/>
								</c:if>	
								<c:if test="${!empty correctionAddress.street1}">
									<c:out value='${correctionAddress.street1}'/><br/>
								</c:if>
									<form:errors path="correction.additionalAddresses[${indx.index}].street1"  class="errorMsg" />
								<c:if test="${!empty correctionAddress.street2}">
									<c:out value='${correctionAddress.street2}'/><br/>
								</c:if>	
								<c:if test="${!empty correctionAddress.city}"><c:out value='${correctionAddress.city}'/>,</c:if>
									<form:errors path="correction.additionalAddresses[${indx.index}].city"  class="errorMsg" />
								<c:if test="${!empty correctionAddress.state}"><c:out value='${correctionAddress.state}'/></c:if>
									<form:errors path="correction.additionalAddresses[${indx.index}].state"  class="errorMsg" />
								<c:if test="${!empty correctionAddress.zip}"><c:out value='${correctionAddress.zip}'/></c:if>
									<form:errors path="correction.additionalAddresses[${indx.index}].zip"  class="errorMsg" /><br/>
								<c:if test="${not empty correctionAddress.zip4}">-<c:out value='${correctionAddress.zip4}'/><br/></c:if>
								<form:errors path="correction.additionalAddresses[${indx.index}].email"  class="errorMsg" />
								<c:if test="${!empty correctionAddress.email}">
									<c:out value='${correctionAddress.email}'/><br/>
								</c:if>
								<c:if test="${!empty correctionAddress.website}">
									<c:out value='${correctionAddress.website}'/><br/>
								</c:if>
							</div>
							<fieldset class="pnl">
								<label>
									<span class="field-name">Address Type</span>
									<spring:bind path="correction.additionalAddresses[${indx.index}].addressTypeName">
										<select name="${status.expression}">
											<option value="">Select an Address Type</option>
											<c:forEach items="${additionalAddressTypes}" var="addrType">
												<option value="${addrType.name}" <c:if test="${addrType.name eq status.value}">selected="selected"</c:if>>${addrType.name}</option>
											</c:forEach>
										</select>
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
						</td>
						</tr>
					</table>

				</c:forEach>
            <table class="section general-email">
            	<tr>
					<td colspan="2"><hr/></td>
				</tr>  
                <tr>
                    <th>Election Office Email Address Address</th>
                    <td class="corrected">
                    <p class="ctrl">
                        <c:choose><c:when test="${not empty leo.generalEmail}"><c:out value='${leo.generalEmail}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                    </p>
                    <fieldset class="pnl">
                        <label>
                            <span class="field-name">Election Office Email Address Address</span>
                            <spring:bind path="correction.generalEmail">
                                <input type="text" name="${status.expression}" value="<c:out value='${leo.generalEmail}'/>" />
                            </spring:bind>
                        </label>
                    </fieldset>
                    </td>
                </tr>

            </table>
            <br/>
            <table class="section">
            	<tr>
            		<td><h3>Election Official Contact Details</h3></td>
            	</tr>
            </table>
            <c:forEach items="${correction.officers}" var="officer" varStatus="index">
                <c:set var="corOfficer" value="${officer}"/>
                <c:if test="${index.index lt fn:length(leo.officers)}">
                    <c:set var="corOfficer" value="${leo.officers[index.index]}"/>
                </c:if>
			<spring:nestedPath path="correction.officers[${index.index}]">
                <table class="section local-election-official">
	                <c:if test="${not index.first}">
		                <tr>
		                	<td colspan="2"><hr/></td>
		                </tr>
	                </c:if>
	                <tr>
                    <th>Office Name</th>
                    <td class="corrected">
                    <p class="ctrl">
                        <c:choose><c:when test="${not empty corOfficer.officeName}">${corOfficer.officeName}</c:when><c:otherwise>Add an Additional Contact</c:otherwise></c:choose>
                    </p>
                    <fieldset class="pnl">
                        <label>
                            <span class="field-name">Office Name</span>
                            <spring:bind path="officeName">
                                <input type="text" name="${status.expression}" value="${corOfficer.officeName}" />
                            </spring:bind>
                        </label>
                    </fieldset>
                    </td>
                </tr>
				<tr>
					<th><c:choose><c:when test="${not empty corOfficer.officeName}">${corOfficer.officeName}</c:when><c:otherwise>Name of Official</c:otherwise></c:choose></th>
					<td class="corrected">
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
							<span class="field-name">Official Name</span>
							<spring:bind path="firstName">
								<input type="text" placeholder="First Name" class="firstName" name="${status.expression}"
									value="<c:out value='${corOfficer.firstName}'/>" />
							</spring:bind>
							<spring:bind path="lastName">
								<input type="text" placeholder="Last Name" class="lastName" name="${status.expression}"
									value="<c:out value='${corOfficer.lastName}'/>" />
							</spring:bind>
						</label>
					</fieldset>
					</td>
				</tr>

				<tr>
					<th>Phone</th>
					<td class="corrected">
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
					</td>
				</tr>
				<tr>
					<th>Fax</th>
					<td class="corrected">
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
					</td>
				</tr>
				<tr>
					<th>Email Address</th>
					<td class="corrected">
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
					</td>
				</tr>
			</table>
      <br/>
      </spring:nestedPath>
      </c:forEach>

			<table class="section further-instructions">
        <tr>
          <td colspan="2"><h3>Additional Information</h3></td>
        </tr>
				<tr>
					<th>Further Instructions</th>
					<td class="corrected">
					<p class="ctrl">
						<c:choose><c:when test="${not empty leo.furtherInstruction}">${fn:replace(fn:escapeXml(leo.furtherInstruction),newLineChar,"<br />")}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
					</p>
					<fieldset class="pnl">
						<spring:bind path="correction.furtherInstruction">
							<textarea rows="12" cols="70" name="${status.expression}"><c:out value='${leo.furtherInstruction}'/></textarea>
						</spring:bind>
					</fieldset>
					</td>
				</tr>
				<tr>
					<th>Office Hours</th>
					<td class="corrected">
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
					</td>
				</tr>
			</table>


			<table class="section additional-message">
				<tr>
					<td>
						<h3>Submitted By:</h3>
						<div class="form-horizontal">
						<div class="form-group">
					    <label class="col-sm-3 control-label">Your Name *</label>
					    <div class="col-sm-9">
					      <spring:bind path="correction.submitterName">
							 <input type="text" class="form-control" required name="${status.expression}" value="<c:out value='${status.value}'/>" />
						 </spring:bind>
					    </div>
					  </div>
					  <div class="form-group">
					    <label class="col-sm-3 control-label">Your Email *</label>
					    <div class="col-sm-9">
					      <spring:bind path="correction.submitterEmail">
							 <input type="email" class="form-control" required name="${status.expression}" value="<c:out value='${status.value}'/>" />
						 </spring:bind>
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="inputEmail3" class="col-sm-3 control-label">Your Phone *</label>
					    <div class="col-sm-9">
					      <spring:bind path="correction.submitterPhone">
							 <input type="tel" class="form-control" placeholder="xxx-xxx-xxxx" required name="${status.expression}" value="<c:out value='${status.value}'/>" />
						 </spring:bind>
					    </div>
					  </div>
					  </div>
					  <spring:bind path="correction.allCorrect">
						<div class="form-group">
						<label class="col-sm-3" class="control-label">Submission Type *</label>
						    <div class="col-sm-9">
						      <div class="radio">
						        <label> 
						          <input type="radio" name="${status.expression}" value="0" required/>
								The record for <c:out value='${leo.region.name}'/> needs to be changed; please update this information.
						        </label>
						      </div>
						      <div class="radio">
						        <label>
						          <input type="radio" name="${status.expression}" value="1" required/>
								I've reviewed the information for <c:out value='${leo.region.name}'/> and it is correct; no updates are needed.
						        </label>
						      </div>
						    </div>
						  </div>
						</spring:bind>
						<div class="form-group">
					    <label class="col-sm-3 control-label">Update Log Notes</label>
					    <div class="col-sm-9">
					      <spring:bind path="correction.message">
							<textarea rows="5" class="form-control" placeholder="(This Information is not displayed to the public)" cols="40" name="${status.expression}"><c:out value='${status.value}'/></textarea>
						</spring:bind>
					    </div>
					  </div>
					  <div class="row">
					  <div class="col-sm-12" style="margin-top:12px;">
						<strong>PLEASE NOTE - Your changes will NOT show up on the site immediately. They will be reviewed within 48 hours of submission before being published to the site.</strong>
						</div>
					  </div>
						<div id="continue">
				<input type="hidden" name="regionId" value="${leo.region.id}"/>
				<a style="margin: 18px;" class="pull-right" href="<c:url value="/eod.htm" />?submission=true&amp;stateId=${leo.region.state.id}&amp;regionId=${leo.region.id}"> View <c:out value='${leo.region.name}'/>
				</a>
        <input type="submit" value="Submit" class="pull-right"/>
			</div>
					</td>
				</tr>

			</table>

		</form>
	</div>
</div>