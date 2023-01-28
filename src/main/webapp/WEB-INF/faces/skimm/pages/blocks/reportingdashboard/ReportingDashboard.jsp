<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<script language="javascript" src="<c:url value="/js/bootstrap/plugins/flot/jquery.flot.min.js"/>" type="text/javascript"></script>
<script language="javascript" src="<c:url value="/js/bootstrap/plugins/flot/jquery.flot.pie.min.js"/>" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
    var usageData = new Array();
    var completedData = new Array();
<c:forEach items="${reportRows.Usage_by_Request_Type}" var="usagePie" varStatus="i">
    var pieChartData${i.count} = {};
    pieChartData${i.count}.label = "${usagePie[0]}";
    pieChartData${i.count}.data = ${usagePie[2]};
    usageData.push(pieChartData${i.count});
</c:forEach>
 <c:forEach items="${reportRows.Completed_by_Request_Type}" var="completedPie" varStatus="i" >
    var pieChartData${i.count} = {};
    pieChartData${i.count}.label = "${completedPie[0]}";
    pieChartData${i.count}.data = ${completedPie[3]};
    completedData.push(pieChartData${i.count});
</c:forEach>

$.plot($("#usagePie"),usageData,
{
        series: {
            pie: {
                show: true,
                 radius: 1,
               label: {
                    show: true,
                    radius: 2/3,
                    formatter: function(label, series){
                        return '<div class="chartLabel">'+Math.round(series.percent)+'%</div>';
                    },
                    threshold: 0.1
                }
                }
        },
        grid: {
            hoverable: true,
            clickable: true
        }
});

   $.plot($("#completedPie"),completedData,
{
     series: {
            pie: {
                show: true,
                 radius: 1,
               label: {
                    show: true,
                    radius: 2/3,
                    formatter: function(label, series){
                        return '<div class="chartLabel">'+Math.round(series.percent)+'%</div>';
                    },
                    threshold: 0.1
                }
                }
        },
        grid: {
            hoverable: true,
            clickable: true
        }
});
$("#completedPie").bind("plothover", pieHover);
$("#completedPie").bind("plotclick", pieClick);
$("#usagePie").bind("plothover", pieHover);
$("#usagePie").bind("plotclick", pieClick);

function pieHover(event, pos, obj)
{
if (!obj)
return;
percent = parseFloat(obj.series.percent).toFixed(2);
$("#hover").html('<span style="font-weight: bold; color: '+obj.series.color+'">'+obj.series.label+' ('+percent+'%)</span>');
}
function pieClick(event, pos, obj)
{
if (!obj)
return;
percent = parseFloat(obj.series.percent).toFixed(2);
alert(''+obj.series.label+': '+percent+'%');
}

});

</script>
<div class="page-header">
	<h2>Reporting Dashboard</h2>
</div>
<!-- begin Graph -->
<fieldset>
	<div class="charts-row">
		<div class="row">
			<div class="span4">
				<div id="standardUsage" class="standardReports1Col clearfix">
					<legend>Usage by Request Type</legend>
					<div class="innerWell center" style="height: 180px; overflow: hidden;">
						<div id="usagePie" class="chart" style="width: 100%; height: 95%;">You need JavaScript for this to work</div>
					</div>

					<div class="report-content-box rnd4">
						<div class="report-content-head  shadow rnd4 clearfix">
							<h3>
								Chart View: <small>Usage by Request Type</small>
							</h3>
						</div>

						<table class="table  standardReportsBlock">
							<thead>
								<tr>
									<c:forEach items="${columnHeaders.Usage_by_Request_Type}" var="columnHeader" varStatus="columnHeaderStatus">
										<th class="standardReportHead">${columnHeader}</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${reportRows.Usage_by_Request_Type}" var="reportRow" varStatus="reportRowStatus">
									<tr>
										<c:forEach items="${reportRow}" var="reportColumn" varStatus="reportColumnStatus">
											<td>${reportColumn}</td>
										</c:forEach>
									</tr>
								</c:forEach>
								<c:if test="${totals.Usage_by_Request_Type != null}">
									<tr>
										<c:forEach items="${totals.Usage_by_Request_Type}" var="total" varStatus="totalStatus">
											<td>${total}</td>
										</c:forEach>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<%--end Usage by Request Type column--%>
			
			<div class="span5">
				<div class="innerRight">
					<div id="standardCompleted" class="standardReport1Col clearfix">
						<div class="standardReportTitle">
							<legend>Completed by Request Type</legend>
						</div>
						<div class="innerWell" style="height: 180px; overflow: hidden;">
							<div id="completedPie" class="chart" style="width: 100%; height: 95%;">You need JavaScript for this to work</div>
						</div>

						<div class="report-content-box rnd4">
							<div class="report-content-head  shadow rnd4 clearfix">
								<h3>
									Chart View: <small>Completed by Request Type</small>
								</h3>
							</div>
							<table class="table standardReportsBlock">
								<thead>
									<tr>
										<c:forEach items="${columnHeaders.Completed_by_Request_Type}" var="columnHeader" varStatus="columnHeaderStatus">
											<th class="standardReportHead">${columnHeader}</th>
										</c:forEach>
									</tr>
								</thead>

								<tbody>
									<c:forEach items="${reportRows.Completed_by_Request_Type}" var="reportRow" varStatus="reportRowStatus">
										<tr>
											<c:forEach items="${reportRow}" var="reportColumn" varStatus="reportColumnStatus">
												<td>${reportColumn}</td>
											</c:forEach>
										</tr>
									</c:forEach>
									<c:if test="${totals.Completed_by_Request_Type != null}">
										<tr>
											<c:forEach items="${totals.Completed_by_Request_Type}" var="total" varStatus="totalStatus">
												<td>${total}</td>
											</c:forEach>
										</tr>
									</c:if>
								</tbody>
							</table>

						</div>
					</div>
				</div>
				<!--end Completed by Request Type chart-row-->
			</div>
		</div>
	</div>
</fieldset>


