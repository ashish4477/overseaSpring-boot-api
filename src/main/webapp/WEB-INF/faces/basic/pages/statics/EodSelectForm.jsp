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

	<form action="<c:url value="${pageUrl}"/>" name="eodForm" class="bc-form" method="get" id="eodForm">
		<input type="hidden" name="submission" value="true"/>
		<fieldset class="state-select">
			<c:choose>
				<c:when test="${not empty param.cStateId}">
					<input type="hidden" name="stateId" id="stateId" value="${param.cStateId}"/>
					<label id="stateName" class="oneline"><span>Your state:</span> ${param.cStateName}</label>
				</c:when>
				<c:otherwise>
       <label class="oneline one-line select select-state-field">
            <span>Choose a state:</span>
        <c:choose>
          <c:when test="${useCleanUrl}">
            <select required class="field" name="stateName" id="select_state">
          </c:when>
          <c:otherwise>
              <select required class="field" name="stateId" id="select_state">
          </c:otherwise>
        </c:choose>
                <option value="0">State...</option>
		                    <c:forEach items="${states}" var="state">
                                <c:choose>
                                    <c:when test="${useCleanUrl}">
                                        <option value="${state.abbr}" <c:if test="${not empty selectedState and selectedState.id eq state.id}">selected="selected"</c:if>>${state.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${state.id}" <c:if test="${not empty selectedState and selectedState.id eq state.id}">selected="selected"</c:if>>${state.name}</option>
                                    </c:otherwise>
                                </c:choose>
		                    </c:forEach>
		                    <optgroup label=""></optgroup>
		                </select>
		            </label>
				</c:otherwise>
			</c:choose>
			<c:if test="${isEod}">
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
   					<span id="ajax_region_select">
					<select required  id="votingRegion" class="field" name="regionId">
						<option value="0">Select...</option>
						<c:forEach items="${regions}" var="region">
							<option value="${region.id}" <c:if test="${not empty selectedRegion and selectedRegion.id eq region.id}">selected="selected"</c:if>>${region.name}</option>
						</c:forEach>
						<optgroup label=""></optgroup>
					</select>
					</span>		
					</label>

			</c:if>
			<c:import url="/WEB-INF/faces/basic/pages/statics/EodCaptcha.jsp" />
		</fieldset>
			    
	         <%--<div class="automated">
	         <label>This field is used to check for automated submissions, please leave blank to view results</label>
             <input type="text" name="automated"/>
             </div>--%>

	    <fieldset id="continue">
	        <input class="eodSelect" type="image" src="<c:url value="/img/buttons/continue-button.gif"/>" onclick="return sendEodForm();"/>
	    </fieldset>
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
