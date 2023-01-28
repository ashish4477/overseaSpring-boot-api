<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%--
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.7/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="<c:url value="/js/bootstrap/plugins/date/ui.daterangepicker.css"/>" type="text/css" />
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.7/jquery-ui.min.js"></script>
<script src="<c:url value="/js/bootstrap/plugins/date/date.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/bootstrap/plugins/date/daterangepicker.jQuery.js"/>" type="text/javascript"></script>
--%>
<!-- Include Required Prerequisites -->
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/bootstrap-daterangepicker@2.1.25/moment.min.js"></script>

<!-- Include Date Range Picker -->
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/bootstrap-daterangepicker@2.1.25/daterangepicker.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/bootstrap-daterangepicker@2.1.25/daterangepicker.min.css" />

<script type="text/javascript">
	var startDate;
  var endDate;
	<c:choose>
	<c:when test="${startDate == null}">
	<c:choose>
	<c:when test="${endDate == null}">
//	initialDateRange = "Today";
  startDate = moment().format("MM/DD/YYYY")
  endDate = startDate;
	</c:when>
	<c:otherwise>
//	initialDateRange = "1/1/2000 - ${endDate}";
  startDate = "1/1/2000";
  endDate = "${endDate}";
	</c:otherwise>
	</c:choose>
	</c:when>
	<c:otherwise>
	<c:choose>
	<c:when test="${endDate == null}">
	//initialDateRange = "${startDate} - 12/31/3000";
  startDate = "${startDate}";
  endDate = "21/31/2100";
	</c:when>
	<c:otherwise>
	//initialDateRange = " - ";
  startDate = "${startDate}";
  endDate = "${endDate}";
	</c:otherwise>
	</c:choose>
	</c:otherwise>
	</c:choose>
	$(function() {
    $('#rangeC').daterangepicker({
          locale : { format : "MM/DD/YYYY" },
          startDate : startDate,
          endDate : endDate
        },
        function( start, end ) {
          setTimeout(function() {
            parseDates( start.format("MM/DD/YYYY") + "-" + end.format("MM/DD/YYYY") );
          }, 1000);
        });
	});

	function parseDates( dateInputValue ) {
		//var dateInputValue = $('#rangeC').val().split(" ").join("");
		var range = dateInputValue.split('-');
		if ( typeof range[0] != 'undefined' ) {
			var prefix = '<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${report.id}"/></c:url>';
			if (typeof range[1] != "undefined") {
				window.location=prefix+'&startDate='+encodeURI(range[0])+'&endDate='+encodeURI(range[1]);
			}
			else {
				window.location=prefix+'&startDate='+encodeURI(range[0]);
			}
		}
	}
</script>

<c:set var="super_standards" value="${standardReports.Usage_by_Request_Type.id},${standardReports.Completed_by_Request_Type.id},${standardReports.By_Age_Group.id},${standardReports.By_Voting_Region___Voter_Type_Military.id},${standardReports.By_Voting_Region___Voter_Type_Temporary_Overseas.id},${standardReports.By_Voting_Region___Voter_Type_Permanent_Overseas.id},${standardReports.By_Voting_Region___Voter_Type_US_Registration.id},${standardReports.By_Voting_Region___Voter_Type_US_Absentee.id}"/>

<form action="<c:url value="/reportingdashboard/ViewReport.htm" />" method="post" id="viewReportForm">
	<div class="column-form">
		<div class="page-header">
			<h2>
				<c:choose>
					<c:when test="${report.standard}">Standard</c:when>
					<c:otherwise>Custom</c:otherwise>
				</c:choose>
				Report View <small> statistics &amp; data</small>
			</h2>
		</div>
		<div class="innerRight">
			<div id="dataPicker" class="well form-inline rnd4">
				<span class="help-inline"><b>Set Report Date Range:</b></span>&nbsp;&nbsp;
				<input type="text" value="" readonly='readonly' placeholder="click to change date range..." id="rangeC" />
			</div>

			<div class="alert alert-info clearfix">
				<a class="close" data-dismiss="alert">×</a>
				<c:choose>
					<c:when test="${startDate != null && endDate != null}">
						<h4>
							<strong>Current date range: ${startDate} to ${endDate}</strong>
						</h4>
					</c:when>
					<c:when test="${startDate != null}">
						<h4>
							<strong>for ${startDate}</strong>
						</h4>
					</c:when>
					<c:when test="${endDate != null}">
						<h4>
							<strong>before ${endDate}</strong>
						</h4>
					</c:when>
					<c:otherwise>
						<h4>
							Current Date Range: Year to Date (<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>)
						</h4>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="innerRight">
			<div class="report-content-head">
				<h3>${report.title}</h3>

				<div id="reportEditNav" class="nav nav-pills pull-right">
					<c:if test="${!report.standard or !fn:contains(super_standards, report.id)}">
						<a class="btn btn-small btn-primary"
							href="<c:url value="/reportingdashboard/ReportSettings.htm"><c:param name="reportId" value="${report.id}"/></c:url>"
						> <i class="icon-white icon-cog"></i>Settings
						</a>
					</c:if>
					<a class="btn btn-small btn-primary"
						href="<c:url value="/reportingdashboard/CloneReport.htm"><c:param name="reportId" value="${report.id}"/></c:url>"
					><i class="icon-white icon-retweet"></i> Clone</a> <a class="btn btn-small btn-primary"
						href="<c:url value="/reportingdashboard/ExportExcelReport.htm"><c:param name="reportId" value="${report.id}"/><c:if test="${startDate != null}"><c:param name="startDate" value="${startDate}"/></c:if><c:if test="${endDate != null}"><c:param name="endDate" value="${endDate}"/></c:if></c:url>"
					> <i class="icon-white icon-file"></i> Export
					</a> <a class="btn btn-small btn-primary"
						href="<c:url value="/reportingdashboard/CreateScheduledReport.htm"><c:param name="reportId" value="${report.id}"/></c:url>"
					> <i class="icon-white icon-calendar"></i> Schedule
					</a>
				</div>
			</div>

			<c:choose>
				<c:when test="${empty columnHeaders}">
					<div class="alert alert-error">
						<h4>
							<c:choose>
								<c:when test="${report.standard}">
									<strong>This standard report has no columns defined!</strong>
									Please report this to the site administrator!
								</c:when>
								<c:otherwise>
									<strong>This report has no columns defined!</strong>
									You can view report data by adding columns within Edit Report.
								</c:otherwise>
							</c:choose>
						</h4>
					</div>
				</c:when>
				<c:otherwise>
					<div class="report-content-box">
						<div class="report-content-wrap">
							<div class="container">
								<c:forEach items="${headerRows}" var="headerRow" varStatus="headerRowStatus">
									<h4>${headerRow}</h4>
								</c:forEach>

								<table class="table shadow rnd4 table-striped">
									<thead>
										<tr>
											<c:forEach items="${columnHeaders}" var="columnHeader" varStatus="columnHeaderStatus">
												<th>${columnHeader}</th>
											</c:forEach>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty reportRows}">
												<tr class="alert alert-error">
													<th colspan="${fn:length(columnHeaders)}"><h4>
															<c:choose>
																<c:when test="${report.standard}">
																	There is no data for this report.
																</c:when>
																<c:otherwise>
																	There is no data for this report. You may want to change the date range or edit the columns in the report.
																</c:otherwise>
															</c:choose>
														</h4></th>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach items="${reportRows}" var="reportRow" varStatus="reportRowStatus">
													<tr>
														<c:forEach items="${reportRow}" var="reportColumn" varStatus="reportColumnStatus">

															<td>${reportColumn}</td>
														</c:forEach>
													</tr>
												</c:forEach>

												<c:if test="${totals != null}">
													<tr>
														<c:forEach items="${totals}" var="total" varStatus="totalStatus">
															<td>${total}</td>
														</c:forEach>
													</tr>
												</c:if>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>

			<c:if test="${!report.standard}">
				<div class="form-actions clearfix">
					<div class="nav nav-pills pull-right">
						<a class="btn btn btn-primary"
							href="<c:url value="/reportingdashboard/EditReport.htm"><c:param name="reportId" value="${report.id}"/></c:url>"
						><i class="icon-white icon-pencil"></i> Edit Report</a>
					</div>
				</div>
			</c:if>
		</div>
	</div>

	<div class="ft"></div>
</form>

