<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%-- Only display if this is an EOD page or not a state face template, or both (Don't display on a state-specific --%>
<c:if test="${empty param.cStateId or isEod or param.showForm}">
	<c:if test="${isEod}">
		<script type="text/javascript" src="<c:url value="/js/select2.full.min.js"/>"></script>
		<link type="text/css" rel="stylesheet" href="<c:url value="/css/select2.css"/>">
		<script src="<c:url value="/js/load-regions.js"/>" type="text/javascript"></script>
		<script type="text/javascript" language="JavaScript">
			//<!--
			<c:url value="/ajax/getRegionsHTMLSelect.htm" var="getRegionUrl"/>
			<c:url value="/ajax/getJsonRegions2.htm" var="getJsonRegionUrl"/>

			<c:choose>
			<c:when test="${not empty param.cStateId}">
			var params = { 'stateInputId':'stateId', 'regionInputId':'votingRegion', 'url':'${getJsonRegionUrl}' };
			$(document).ready( function() {
				var element = getJsonRegions2( params );
			});
			</c:when>
			<c:otherwise>
			var params = { 'stateInputId':'select_state', 'regionInputId':'votingRegion', 'url':'${getJsonRegionUrl}' };
			$(document).ready( function() {
				var element = getJsonRegions2( params );
				$("#select_state").change(function() {
					element.val(null).trigger("change");
				});
			});
			</c:otherwise>
			</c:choose>
			//-->
		</script>
	</c:if>

		<form action="<c:url value="${pageUrl}"/>" role="form" name="eodForm" class="bc-form election-dates-form" method="get" id="eodForm">
			<input type="hidden" name="submission" value="true"/>
			<fieldset class="state-select">
				<c:choose>
					<c:when test="${not empty param.cStateId}">
						<div class="form-group">
							<input type="hidden" name="stateId" id="stateId" class="form-control" value="${param.cStateId}"/>
							<label id="stateName" class="oneline"><span>Your state:</span> ${param.cStateName}</label>
						</div>
					</c:when>
					<c:otherwise>
						<div class="form-group">
							<label class="oneline one-line select select-state-field">
								<span>Choose a State:</span>
							</label>
							<c:choose>
								<c:when test="${useCleanUrl}">
									<select class="field form-control" name="stateName" id="select_state">
										<option value="0">- Select Your State -</option>
										<c:forEach items="${states}" var="state">
										<option value="${state.abbr}" <c:if test="${not empty selectedState and selectedState.id eq state.id}">selected="selected"</c:if>>${state.name}</option>
										</c:forEach>
										<optgroup label=""></optgroup>
									</select>
								</c:when>
								<c:otherwise>
									<select class="field form-control" name="stateId" id="select_state">
										<option value="0">- Select Your State -</option>
										<c:forEach items="${states}" var="state">
										<option value="${state.id}" <c:if test="${not empty selectedState and selectedState.id eq state.id}">selected="selected"</c:if>>${state.name}</option>
										</c:forEach>
										<optgroup label=""></optgroup>
									</select>
								</c:otherwise>
							</c:choose>

						</div>
					</c:otherwise>
				</c:choose>
				<c:if test="${isEod}">
					<div class="form-group">
						<label class="oneline one-line select select-region-field">
							<c:choose>
								<c:when test="${not empty param.regionLabel}">
									<c:set var="regionLabel" value="${param.regionLabel}"/>
								</c:when>
								<c:otherwise>
									<c:set var="regionLabel" value="region"/>
								</c:otherwise>
							</c:choose>
							<br />
							<span>Choose a ${regionLabel}:</span>
						</label>
   					<span id="ajax_region_select">
					<select id="votingRegion" class="field form-control" name="regionId">
						<option value="0">Select...</option>
						<c:forEach items="${regions}" var="region">
							<option value="${region.id}" <c:if test="${not empty selectedRegion and selectedRegion.id eq region.id}">selected="selected"</c:if>>${region.name}</option>
						</c:forEach>
						<optgroup label=""></optgroup>
					</select>
					</span>
					</div>
				</c:if>

				<c:if test="${isEod eq false}">
					<br/>
                <fieldset class="state-select">
					<label class="oneline one-line select select-state-field">
						<span>Choose Between</span>
					</label>
                  <select class="form-control" id="locationSelection" name="location" required="" onchange="myFunction()">
                    <option value="">- Select an Election Location -</option>
                    <c:choose>
                       	<c:when test="${location eq 'domestic'}">
                          	<option value="domestic" selected="selected">U.S. Domestic Voters</option>
						</c:when>
						<c:otherwise>
					   		<option value="domestic">U.S. Domestic Voters</option>
						</c:otherwise>
                    </c:choose>
                    <c:choose>
						<c:when test="${location eq 'overseas'}">
							<option value="overseas" selected="selected">Overseas & Military Voters</option>
						</c:when>
						<c:otherwise>
							<option value="overseas">Overseas & Military Voters</option>
						</c:otherwise>
                     </c:choose>
                  </select>
                </fieldset>
                <c:if test="${location eq 'domestic'}">
                <br/>
                </c:if>
				<fieldset class="state-select">
					<c:choose>
						<c:when test="${location eq 'overseas'}">
							<c:set var="generalInfo" value="none"/>
						</c:when>
						<c:otherwise>
							<c:set var="generalInfo" value="block"/>
						</c:otherwise>
					</c:choose>
					
					<label class="oneline one-line select select-state-field" id="votingSelectionLabel" style="display: ${selectElection}">
						<span>Choose a Section:</span>
					</label>
								
					<select class="field form-control" name="voting_information" id="votingSelection" style="display: ${selectElection}">
						<c:choose>
							<c:when test="${votingInformation eq 'Upcoming Election Dates and Deadlines'}">
								<option value="Upcoming Election Dates and Deadlines" selected="selected">Upcoming Election Dates and Deadlines</option>
							</c:when>
							<c:otherwise>
								<option value="Upcoming Election Dates and Deadlines">Upcoming Election Dates and Deadlines</option>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${votingInformation eq 'General Information'}">
								<option value="General Information" selected="selected" style="display: ${generalInfo}">General Information</option>
							</c:when>
							<c:otherwise>
								<option value="General Information" style="display: ${generalInfo}">General Information</option>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${votingInformation eq 'Eligibility Requirements'}">
								<option value="Eligibility Requirements" selected="selected">Eligibility Requirements</option>
							</c:when>
							<c:otherwise>
								<option value="Eligibility Requirements">Eligibility Requirements</option>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${votingInformation eq 'Identification Requirements'}">
								<option value="Identification Requirements" selected="selected">Identification Requirements</option>
							</c:when>
							<c:otherwise>
								<option value="Identification Requirements">Identification Requirements</option>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${votingInformation eq 'Voter Materials Transmission Options'}">
								<option value="Voter Materials Transmission Options" selected="selected">Voter Materials Transmission Options</option>
							</c:when>
							<c:otherwise>
								<option value="Voter Materials Transmission Options">Voter Materials Transmission Options</option>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${votingInformation eq 'State Lookup Tools – Am I Registered?'}">
								<option value="State Lookup Tools – Am I Registered?" selected="selected">State Lookup Tools – Am I Registered?</option>
							</c:when>
							<c:otherwise>
								<option value="State Lookup Tools – Am I Registered?">State Lookup Tools – Am I Registered?</option>
							</c:otherwise>
						</c:choose>

					<select class="field form-control" name="stateName" id="select_state">
                </fieldset>
                </c:if>
				<div class="form-group text-center">
					<c:import url="/WEB-INF/faces/basic/pages/statics/EodCaptcha.jsp" />
				</div>
			</fieldset>
			<input type="submit" value="submit" class="pull-right" onclick="return sendEodForm();"/>
		</form>

	<script type="text/javascript" language="JavaScript">
		function sendEodForm() {
			document.eodForm.submission.value = true;
			<c:if test="${useCleanUrl}">
			var stateSelect = document.getElementById('select_state');
			if ( stateSelect ) {
				if (stateSelect.selectedIndex <= 0) return false;
				var stateAbbr = stateSelect.options[stateSelect.selectedIndex].value;
				document.eodForm.action += '/' +  stateAbbr;

				var regionSelect = document.getElementById('votingRegion');
				if ( regionSelect && regionSelect.selectedIndex > 0 ) {
					var regionName = regionSelect.options[ regionSelect.selectedIndex ].text;
					document.eodForm.action += '/' + encodeURIComponent(regionName.replace(/\./g,'%2e'));
				}
			}
			</c:if>
			return true;
		}
		function myFunction() {
			    var location = document.getElementById("locationSelection");
		        var voting = document.getElementById("votingSelection");
				var votingLabel = document.getElementById("votingSelectionLabel");

				// If the Election location is overseas, "General Information" should be hide.
		        if (location.value == "overseas") {
		        	voting[1].style.display = "none"
		        	voting.value = "Upcoming Election Dates and Deadlines"
		        }
		        else {
		        	voting[1].style.display = "block"
		        }
		}
	</script>
</c:if>
