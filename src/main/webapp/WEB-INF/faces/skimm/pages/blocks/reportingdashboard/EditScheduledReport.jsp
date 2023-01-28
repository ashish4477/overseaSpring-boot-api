<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<div class="page-header">
		<h2>Edit Scheduled Report <small>user defined notification details </small></h2>
</div>
	<div class="innerRight">
	<form id="editScheduledReport" class="form-horizontal" action='<c:url value="/reportingdashboard/EditScheduledReport.htm"/>'
		method="post">
		<table class="table table-bordered Zebra-stripe">
			<tr>
				<th>Report Name</th>
				<td>${scheduledReport.report.title}</td>
			</tr>

			<tr>
				<th>Include data for this time period</th>
				<td><select name="duration">
						<c:forEach items="${timeSpans}" var="timeSpan">
							<c:choose>
								<c:when test="${scheduledReport.duration == timeSpan}">
									<option value="${timeSpan}" selected>${timeSpan}</option>
								</c:when>
								<c:otherwise>
									<option value="${timeSpan}"}">${timeSpan}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<th>Send email notification</th>
				<td><select name="interval">
						<c:forEach items="${timeSpans}" var="timeSpan">
							<c:choose>
								<c:when test="${scheduledReport.interval == timeSpan}">
									<option value="${timeSpan}" selected>${timeSpan}</option>
								</c:when>
								<c:otherwise>
									<option value="${timeSpan}">${timeSpan}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
				</select></td>
			</tr>
		</table>
		
		<div class="scheduledReportControl well clearfix">
			<input type="hidden" id="scheduledReportId" name="scheduledReportId" value="${scheduledReport.id}"/>
			<div class="pull-right">
				<input class="btn btn-primary" type="submit" value="Save" />
			</div>
		</div>
	</form>
 </div>