<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@page import= "com.bearcode.ovf.model.reportingdashboard.ScheduledReport"%>
<%--<title>Reporting Dashboard - Scheduled Reports</title>--%>

        <div class="page-header">
		<h2>Currently Scheduled Reports</h2>
        </div>
        
        <div class="innerRight">
         <c:choose>
			<c:when test="${empty scheduledReports}">
		        <div class="alert alert-info"><h4>You currently have no reports scheduled.<br/>
		        To schedule a report view the report and choose the "schedule" button</h4>
		        </div>
			</c:when>
			<c:otherwise>
			<table class="table table-striped table-bordered custom">
				<thead>
					<tr>
						<th>Report Name</th>
						<th>Include data for this time period</th>
						<th>Send email notification</th>
					</tr>
				</thead>
                <tbody>
				<c:forEach items="${scheduledReports}" var="scheduledReport">
					<tr>
						<td><a title="Remove" href="<c:url value='DeleteScheduledReport.htm'><c:param name='scheduledReportId' value='${scheduledReport.id}'/></c:url>"
						><i class="icon-remove"></i></a>
						<a title="Edit" href="<c:url value='EditScheduledReport.htm'><c:param name='scheduledReportId' value='${scheduledReport.id}'/></c:url>"
						><i class="icon-pencil"></i></a>&nbsp; ${scheduledReport.report.title}</td>
						<td>${scheduledReport.duration}</td>
						<td>${scheduledReport.interval}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			</c:otherwise>
			</c:choose>
            </div>
            


