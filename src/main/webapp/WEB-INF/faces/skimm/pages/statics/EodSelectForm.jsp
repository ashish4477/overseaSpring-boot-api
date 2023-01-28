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

		<form action="<c:url value="${pageUrl}"/>" role="form" name="eodForm" class="bc-form" method="get" id="eodForm">
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
				<div class="form-group text-center">
					<c:import url="/WEB-INF/faces/skimm/pages/statics/EodCaptcha.jsp" />
				</div>
			</fieldset>

			<div class="text-center">
				<input type="submit" value="Submit" class="" onclick="return sendEodForm();"/>
			</div>
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
	</script>

</c:if>
