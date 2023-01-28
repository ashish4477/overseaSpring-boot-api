<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript">
	function setParameter(parameterField, parameterValue) {
		parameterField.value = parameterValue;
	}
</script>

<div class="page-header">
	<h2>
		Report Settings <small>Modify and Create a Report</small>
	</h2>
</div>

<form id="ReportSettings " class="form-horizontal span8" action='<c:url value="/reportingdashboard/ReportSettings.htm"/>'
	method="post" id="Report"
>
	<fieldset>
		<legend name="reportTitle">${status.value} Settings</legend>
		<spring:bind path="report.title">
			<div class="control-group">
				<label class="control-label">Report Title</label>

				<div class="controls">
					<input id="ReportTitle" name="reportTitle" value="${status.value}" type="text" id="input01" class="span4 input-xlarge" />
				</div>
			</div>
		</spring:bind>
		<div class="control-group">
			<spring:bind path="report.description">
				<label class="control-label">Report Description</label>

				<div class="controls">
					<textarea class="input-xlarge span4" name="reportDescription">${status.value}</textarea>
				</div>
			</spring:bind>
		</div>
		<c:choose>
			<c:when test="${flows != null}">
				<div class="control-group">
					<label class="control-label">Request Type</label>
					<spring:bind path="report.flowType">
						<div class="controls">
							<select class="span4" name="flowType">
								<option value="">--All Flows --</option>
								<c:forEach items="${flows}" var="flow">
									<c:choose>
										<c:when test="${flow == 'RAVA'}">
											<c:set var="flowName" value="FPCA" />
										</c:when>
										<c:otherwise>
											<c:set var="flowName" value="${flow}" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${report.flowType == flow}">
											<option value="${flow}" selected>${flowName}</option>
										</c:when>
										<c:otherwise>
											<option value="${flow}">${flowName}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</spring:bind>
				</div>
			</c:when>
			<c:otherwise>
				<div class="control-group">
					<label class="control-label">Flow Type</label>
					<div class="controls" style="margin-top: 5px;">
						<c:choose>
							<c:when test="${report.flowType == null}">All Flowss</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${report.flowType == 'RAVA'}">
										<c:set var="flowName" value="FPCA" />
									</c:when>
									<c:otherwise>
										<c:set var="flowName" value="${flow}" />
									</c:otherwise>
								</c:choose>
									${flowName}
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		<authz:authorize ifAllGranted="ROLE_ADMIN">
			<div class="control-group">
				<label class="control-label multiSelect">Filter By Site</label>
				<div class="controls">
					<select name="face" multiple="multiple" class="span4">
						<c:forEach items="${faces}" var="face">
							<c:set var="selectedFace" value="" />
							<c:forEach items="${report.faces}" var="reportFace">
								<c:if test="${reportFace == face}">
									<c:set var="selectedFace" value="selected" />
								</c:if>
							</c:forEach>
							<c:choose>
								<c:when test="${selectedFace == 'selected'}">
									<option value="${face}" selected>${face}</option>
								</c:when>
								<c:otherwise>
									<option value="${face}">${face}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</div>
			</div>

			<c:choose>
				<c:when test="possibleOwners == null">
					No possible owners
				</c:when>
				<c:otherwise>
					<div class="control-group">
						<label class="control-label singleSelect">Report Owner</label>
						<div class="controls">
							<select name="ownerId" class="span4">
								<c:set var="selectedOwner" value="" />
								<c:forEach items="${possibleOwners}" var="possibleOwner">
									<c:choose>
										<c:when test="${possibleOwner.id == report.owner.id}">
											<c:set var="selectedOwner" value="selected" />
										</c:when>
										<c:otherwise>
											<c:set var="selectedOwner" value="" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${selectedOwner == 'selected'}">
											<option value="${possibleOwner.id}" selected>${possibleOwner.username}</option>
										</c:when>
										<c:otherwise>
											<option value="${possibleOwner.id}">${possibleOwner.username}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
            <c:if test="${report.id != null}">
                <spring:bind path="report.standard">
                    <div class="control-group">
                        <label class="control-label">Standard Report</label>

                        <div class="controls">
                            <input id="ReportStandard" name="standard" value="TRUE" type="checkbox" class="span4 input-xlarge"
                                    <c:if test="${status.value}">checked="checked"</c:if> />
                        </div>
                    </div>
                </spring:bind>
            </c:if>
		</authz:authorize>

		<div class="innerRight">
			<hr>
			<input type="hidden" name="reportId" value="${report.id}" />

			<div class="btn-toolbar pull-right">
				<c:if test="${report.id != null}">
					<a class="btn"
						href="<c:url value="/reportingdashboard/DeleteReport.htm"><c:param name="reportId" value="${report.id}"/></c:url>"
					>Delete</a>
				</c:if>
				<button class="btn btn-primary" type="submit" value="Save" name="saveReportSettings">Save Settings</button>
			</div>
		</div>
	</fieldset>
</form>