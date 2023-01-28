<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js" type="text/javascript"></script>
<script src='<c:url value="/js/bootstrap/bootstrap.min.js"/>' type="text/javascript"></script>

<%-- This has to be done this way in order to access the errors --%>
<c:set var="modelAttr" value="whatsOnMyBallot" />
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
		
		var queryStr = '/vote/mobilevote/votingPrecinct/cities/state/' + state.value + "/region/" + county.value;
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

		var queryStr = '/vote/mobilevote/votingPrecinct/cities/state/' + state.value + "/zip/" + zip.value;
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
			changeCity();
			city.disabled = false;
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

		var queryStr = '/vote/mobilevote/votingPrecinct/streets/state/' + state.value + '/city/' + city.value;
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
			submitButton.disabled = false;
		});
	}

	function buildStreet1() {
		var houseNumber = document.getElementById("houseNumber");
		var streetName = document.getElementById("streetName");
		var street1 = document.getElementById("street1");
		street1.value = houseNumber.value + " " + streetName.value;
	}
</script>

<div class="body-content page-form rava content column wide-content">
	<div class="page-form">
		<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
			<c:param name="title" value="What's on My Ballot?" />
			<c:param name="progressLabel" value="What's on My Ballot? progress" />
		</c:import>

		<div class="bd" id="overseas-vote-foundation-short">
			<div class="bd-inner">
				<form:form class="userFields" modelAttribute="${modelAttr}" method="post" name="whatsOnMyBallot" id="whatsOnMyBallot"
					onsubmit="return buildStreet1()"
				>
					<div class="legend">
						<span>Voter Information</span>
					</div>

					<fieldset class="voterInfo">
						<div class="voterType">
							<form:label for="voterType" path="voterType" cssErrorClass="error" class="required">Voter Type</form:label>
                <select id="voterType" name="voterType">
                  <option value="">- Select -</option>
                  <option value="CITIZEN_OVERSEAS_INDEFINITELY">U.S. Citizen Residing Outside The U.S. Indefinitely and My Return is Not Certain</option>
                  <option value="CITIZEN_OVERSEAS_TEMPORARILY">U.S. Citizen Residing Outside The U.S. Temporarily and I Intend to Return</option>
                  <option value="UNIFORMED_SERVICE_MEMBER">Uniformed Services or Merchant Marine on Active Duty </option><option value="SPOUSE_OR_DEPENDENT">Eligible Military Spouse or Dependent</option>
                  <optgroup label=""></optgroup>
                </select>
							<form:errors path="voterType" class="errorMsg" />
						</div>
					</fieldset>

					<div class="break"></div>

					<div class="section one">
						<div class="residence">
							<div class="legend"><span>Last U.S. Residence Address</span>
								<div class="rava-bubble">
									<b>Uniformed Services:</b> Use legal residence.<br /> <br /> <b>Overseas Citizens:</b> Use the address of the last place you lived before departing the U.S.<br /> <br /> This must be a physical street address. A Post Office Box cannot be used as a voting address.
								</div>
							</div>

							<fieldset class="votingAddress">
								<div class="content">
									<a name="address"></a>
									<p class="description offset1">The county and ZIP fields are used to determine the list of cities available.
									<br/>Only one of them should be selected - the other will be cleared.</p>
									<form:hidden id="state" for="address.state" path="address.state" />
									<div class="county">
										<form:label for="address.county" path="address.county" cssErrorClass="error">County</form:label>
										<form:select id="county" path="address.county" cssErrorClass="error" multiple="false" onchange="changeCounty()">
											<form:option value="" label="- Select -" />
											<form:options items="${regions}" itemValue="name" itemLabel="name" />
											<form:errors path="address.county" class="errorMsg" />
										</form:select>
									</div>
									<div class="zip">
										<form:label for="address.zip" path="address.zip" cssErrorClass="error">ZIP Code</form:label>
										<form:select id="zip" path="address.zip" cssErrorClass="error" multiple="false" onchange="changeZip()">
											<form:option value="" label="- Select -" />
											<form:options items="${zipCodes}" />
											<form:errors path="address.zip" class="errorMsg" />
										</form:select>
									</div>
									<div class="city">
										<form:label for="address.city" path="address.city" cssErrorClass="error">City</form:label>
										<form:select id="city" path="address.city" cssErrorClass="error" multiple="false" disabled="true" onchange="changeCity()">
											<form:option value="${whatsOnMyBallot.address.city}" name="${whatsOnMyBallot.address.city}" selected="true" />
											<form:errors path="address.city" class="errorMsg" />
										</form:select>
									</div>
									<div class="street1">
										<form:hidden id="street1" path="address.street1" />
										<form:errors path="address" class="errorMsg" />
										<div class="houseNumber">
											<label for="houseNumber" class="required">House #</label>
											<input id="houseNumber" name="houseNumber" value="${houseNumber}" />
										</div>
										<div class="streetName">
											<label for="streetName" class="required">Street Name</label> <select id="streetName" name="streetName" disabled="true">
												<c:if test="${not empty streetName}">
													<option>${streetName}</option>
												</c:if>
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
					<div class="break"></div>

					<form:hidden path="votingState.id" />
					<form:hidden path="votingState.name" />
					<form:hidden path="votingState.abbr" />
					<form:hidden path="votingState.fipsCode" />
				</form:form>
			</div>
		</div>
		<div class="ft">
			<div class="ft-inner"></div>
		</div>
	</div>
</div>