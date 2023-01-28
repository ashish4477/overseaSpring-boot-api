<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
<script src="<c:url value="/js/bootstrap/bootstrap.min.js"/>" type="text/javascript"></script>

<%-- This has to be done this way in order to access the errors --%>
<c:set var="faction" value="" />
<c:if test="${not empty param.action}">
	<c:set var="faction" value="${param.action}" />
</c:if>
<c:set var="fname" value="userForm" />
<c:if test="${not empty param.name}">
	<c:set var="fname" value="${param.name}" />
</c:if>
<c:set var="fid" value="userForm" />
<c:if test="${not empty param.id}">
	<c:set var="fid" value="${param.id}" />
</c:if>
<c:set var="modelAttr" value="user" />
<c:if test="${not empty param.modelAttribute}">
	<c:set var="modelAttr" value="${param.modelAttribute}" />
</c:if>

<script type="text/javascript">	
function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function addOption(field, value, name) {
	var fieldOption = new Option();
	fieldOption.value = value;
	fieldOption.text = name;
	field.add(fieldOption);	
}

function chooseOption(field, value) {
	if (!isBlank(value)) {
		for (idx = 0; idx < field.options.length; ++idx) {
			if (value.toUpperCase() == field.options[idx].text.toUpperCase()) {
				field.selectedIndex = idx;
				break;
			}
		}
	}
}

$(document).ready(function() {
	var state = document.getElementById("state");
	if (!isBlank(state.value)) {
		var county = document.getElementById("county");
		var zip = document.getElementById("zip");
		var city = document.getElementById("city");
		if (!isBlank(county.value)) {
			changeCounty();
		} else if (!isBlank(zip.value)) {
			changeZip();
		}
		if (!isBlank(city.value)) {
			changeCity();
		}
	}
});

function changeCounty() {
	var state = document.getElementById("state");
	var county = document.getElementById("county");
	if (isBlank(state.value) || isBlank(county.value)) {
		return;
	}

	var submitButton = document.getElementById("submitButton")
	submitButton.disabled = true;

	var zip = document.getElementById("zip");
	zip.value = "";

	var queryStr = '/vote/votingPrecinct/cities/state/' + state.value + "/region/" + county.value;
	updateCities(queryStr);
}

function changeZip() {
	var state = document.getElementById("state");
	var zip = document.getElementById("zip");
	if (isBlank(state.value) || isBlank(zip.value)) {
		return;
	}

	var submitButton = document.getElementById("submitButton")
	submitButton.disabled = true;

	var county = document.getElementById("county");
	county.value = "";
	
	var queryStr = '/vote/votingPrecinct/cities/state/' + state.value + "/zip/" + zip.value;
	updateCities(queryStr);
}

function updateCities(queryStr) {
	var city = document.getElementById("city");
	var cityValue = city.value;
	$.getJSON(queryStr, '', function(cities, textstatus, jqXHR) {
		city.options.length = 0;
		if (cities.length > 1) {
			addOption(city, "", "- Select -");
		}
		$.each(cities, function(id, name) {
			addOption(city, name, name);
		});
		if (cities.length > 1) {
			chooseOption(city, cityValue);
		} else {
			$.each(cities, function(id, name) {
				chooseOption(city, name);
			});
		}
		city.disabled = false;
		changeCity();
	});		
}

function changeCity() {
	var state = document.getElementById("state");
	var city = document.getElementById("city");
	if (isBlank(state.value) || isBlank(city.value)) {
		return;
	}

	var submitButton = document.getElementById("submitButton")
	submitButton.disabled = true;
	
	var queryStr = '/vote/votingPrecinct/streets/state/' + state.value + '/city/' + city.value;
	var streetName = document.getElementById("streetName");
	var streetValue = streetName.value;
	$.getJSON(queryStr, '', function(streets, textstatus, jqXHR) {
		streetName.options.length = 0;
		if (streets.length > 1) {
			addOption(streetName, "", "- Select -");
		}
		$.each(streets, function(id, name) {
			addOption(streetName, name, name);
		});
		if (streets.length > 1) {
			chooseOption(streetName, streetValue);
		} else {
			$.each(streets, function(id, name) {
				chooseOption(streetName, name);
			});
		}
		streetName.disabled = false;
		submitButton.disabled = false
	});
}

function buildStreet1() {
	var houseNumber = document.getElementById("houseNumber");
	var streetName = document.getElementById("streetName");
	var street1 = document.getElementById("street1");
	street1.value = houseNumber.value + " " + streetName.value;
}
</script>

<div class="body-content page-form ${fn:toLowerCase(wizardContext.flowType)} content column wide-content">
	<div class="page-form">
		<c:choose>
			<c:when test="${wizardContext.flowType == 'FWAB'}">
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value="Federal Write-in Absentee Ballot" />
					<c:param name="progressLabel" value="write-in ballot progress" />
				</c:import>
			</c:when>
			<c:when test="${wizardContext.flowType == 'RAVA'}">
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value="Register to Vote / Absentee Ballot Request" />
					<c:param name="progressLabel" value="registration progress" />
				</c:import>
			</c:when>
			<c:when test="${wizardContext.flowType == 'DOMESTIC_REGISTRATION'}">
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value="Register to Vote" />
					<c:param name="progressLabel" value="registration progress" />
				</c:import>
			</c:when>
			<c:when test="${wizardContext.flowType == 'DOMESTIC_ABSENTEE'}">
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value="Absentee Ballot Request" />
					<c:param name="progressLabel" value="registration progress" />
				</c:import>
			</c:when>
			<c:otherwise>
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value=" " />
					<c:param name="progressLabel" value="registration progress" />
				</c:import>
			</c:otherwise>
		</c:choose>
		
		<div class="bd" id="overseas-vote-foundation-short">
			<div class="bd-inner">
				<form:form modelAttribute="${modelAttr}" action="${faction}" method="post" name="${fname}"
					onsubmit="return buildStreet1()" class="userFields" id="whatsOnMyBallot">
					<div class="legend">
						<span>Last U.S. Residence Address</span>
						<div class="rava-bubble">
							<b>Uniformed Services:</b> Use legal residence.<br /> <br /> <b>Overseas Citizens:</b> Use the address of the last place
							you lived before departing the U.S.<br /> <br /> This must be a physical street address. A Post Office Box cannot be used
							as a voting address.
						</div>
					</div>
					<div>
					<div>
						<fieldset class="votingAddress">
							<div class="content">
								<p class="description offset1">The county and ZIP fields are used to determine the list of cities available. Only one of them should be selected - the other will be cleared. Both will be filled in on the form based on the address chosen.</span></p>
								<form:hidden id="state" for="votingAddress.state" path="votingAddress.state" />
								<div class="county">
									<form:label for="votingAddress.county" path="votingAddress.county" cssErrorClass="error">County</form:label>
									<form:select id="county" path="votingAddress.county" cssErrorClass="error" multiple="false" onchange="changeCounty()">
										<form:option value="" label="- Select -" />
										<form:options items="${regions}" itemValue="name" itemLabel="name" />
										<form:errors path="votingAddress.county" class="errorMsg" />
									</form:select>
								</div>
								<div class="zip">
									<form:label for="votingAddress.zip" path="votingAddress.zip" cssErrorClass="error">ZIP Code</form:label>
									<form:select id="zip" path="votingAddress.zip" cssErrorClass="error" multiple="false" onchange="changeZip()">
										<form:option value="" label="- Select -" />
										<form:options items="${zipCodes}" />
										<form:errors path="votingAddress.zip" class="errorMsg" />
									</form:select>
								</div>
								<div class="city">
									<form:label for="votingAddress.city" path="votingAddress.city" cssErrorClass="error">City</form:label>
									<form:select id="city" path="votingAddress.city" cssErrorClass="error" multiple="false" disabled="true" onchange="changeCity()">
										<c:if test="${not empty user.votingAddress.city}">
											<form:option value="${user.votingAddress.city}" name="${user.votingAddress.city}" />
										</c:if>
										<form:errors path="votingAddress.city" class="errorMsg" />
									</form:select>
								</div>
								<div class="street1">
									<form:hidden id="street1" path="votingAddress.street1" />
									<form:errors path="votingAddress" class="errorMsg" />
									<div class="houseNumber">
										<label for="houseNumber">House #</label>
										<input id="houseNumber" name="houseNumber" value="${houseNumber}" />
									</div>
									<div class="streetName">
										<label for="streetName">Street Name</label> <select id="streetName" name="streetName" disabled="true">
											<c:if test="${not empty streetName}">
												<option>${streetName}</option>
											</c:if>
											<optgroup label=""></optgroup>
										</select>
									</div>
								</div>
							</div>
						</fieldset>
						</div>
					</div>
					<input type="image" src='<c:url value="/img/buttons/continue-button.gif"/>' name="create" value="Continue"
						class="submit-button" id="submitButton" disabled="disabled"
					/>
					<c:if test="${param.showBackButton}">
						<a class="back-button" href='<c:url value="/Login.htm"/>'><img
							src='<c:url value="/img/buttons/back-button.gif"/>' alt="back"
						></a>
					</c:if>
					<div class="break"></div>
				</form:form>
			</div>
		</div>
		<div class="ft">
			<div class="ft-inner"></div>
		</div>
	</div>
</div>