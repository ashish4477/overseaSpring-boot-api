<%--
  Created by IntelliJ IDEA.
  User: Brett
  Date: 2/29/12
  Time: 11:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(document).ready(function(){
    $('li.collapseTitle1').click(function() {
      $(this).toggleClass('expanded');
        $('li.collapseTitle1 a i').toggleClass('icon-plus icon-minus');
 });

   $('li.collapseTitle2').click(function() {
        $(this).toggleClass('expanded');
        $('li.collapseTitle2 a i').toggleClass('icon-plus icon-minus');
   });

    $("a").each(function(){
               if ($(this).attr("href") == window.location.pathname){
                       $(this).parent().addClass("active");
               }
       });

});
</script>
<c:set var="super_standards" value="${standardReports.Usage_by_Request_Type.id},${standardReports.Completed_by_Request_Type.id},${standardReports.By_Age_Group.id},${standardReports.By_Voting_Region___Voter_Type_Military.id},${standardReports.By_Voting_Region___Voter_Type_Temporary_Overseas.id},${standardReports.By_Voting_Region___Voter_Type_Permanent_Overseas.id},${standardReports.By_Voting_Region___Voter_Type_US_Registration.id},${standardReports.By_Voting_Region___Voter_Type_US_Absentee.id}"/>

<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">Panel title</h3>
  </div>
  <div class="panel-body">
    <ul class="nav nav-pills nav-stacked">
      <li><a class="nav-header" href="ReportingDashboard.htm"><i class="icon icon-home"></i> Dashboard Home</a></li>
      <li><a class="nav-header" href="ScheduledReports.htm"><i class="icon-calendar"></i> Scheduled Reports</a></li>
      <li><a class="nav-header" href='<c:url value="/reportingdashboard/MailingList.csv" />'><i class="icon-envelope"></i> Export Mailing List</a>
      <li class="nav-header"><i class="icon-signal"></i> Standard Reports</li>
    </ul>
  </div>
</div>



<div id="sidebar" class="span3">
	<div class="wellSquare sidebar-nav">
  <div class="well">
		<ul class="nav nav-list" id="StandardReportsList">
			<li><a class="nav-header" href="ReportingDashboard.htm"><i class="icon icon-home"></i> Dashboard Home</a></li>
			<li><a class="nav-header" href="ScheduledReports.htm"><i class="icon-calendar"></i> Scheduled Reports</a></li>
			<li><a class="nav-header" href='<c:url value="/reportingdashboard/MailingList.csv" />'><i class="icon-envelope"></i> Export Mailing List</a>
			<li class="nav-header"><i class="icon-signal"></i> Standard Reports</li>

            <ul class="nav nav-list">
			<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.Usage_by_Request_Type.id}"/></c:url>'>Usage by Request Type</a></li>
			<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.Completed_by_Request_Type.id}"/></c:url>'>Completed by Request Type</a></li>
			<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.By_Age_Group.id}"/></c:url>'>By Age Group</a></li>
		    <%--<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.By_Voter_County.id}"/></c:url>'>By Voter County</a></li>--%>
                <c:forEach items="${standardReports}" var="mapEntry">
                    <c:set var="report" value="${mapEntry.value}"/>
                    <c:if test="${not fn:contains(super_standards, report.id)}">
                        <li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${report.id}"/></c:url>'>${report.title}</a></li>
                    </c:if>
                </c:forEach>
<hr/>
        <li class="collapseTitle1">Voting Region &amp; Voter Type</li>
	     <ul id="voterRegionList" class="nav nav-list in collapse">
	     		<c:if test="${relativePath != 'faces/usvote'}">
					<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.By_Voting_Region___Voter_Type_Military.id}"/></c:url>'>- Military</a></li>
					<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.By_Voting_Region___Voter_Type_Temporary_Overseas.id}"/></c:url>'>- Temporary Overseas</a></li>
					<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.By_Voting_Region___Voter_Type_Permanent_Overseas.id}"/></c:url>'>- Permanent Overseas</a></li>
				</c:if>
				<c:if test="${relativePath == 'faces/default' || relativePath == 'faces/usvote'}">
				<%-- Display only for main ovf site --%>
					<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.By_Voting_Region___Voter_Type_US_Registration.id}"/></c:url>'>- US Registration</a></li>
					<li><a href='<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${standardReports.By_Voting_Region___Voter_Type_US_Absentee.id}"/></c:url>'>- US Absentees</a></li>
				</c:if>
			</ul>
          <hr />
      </ul>
    <%--end StandardReport block well--%>
    </div>
     <%--begin Custom sidebar block well--%>
        <div class="well">
        <ul class="topNav nav nav-list">
        <ul id="customReportsMenu" class="nav nav-list">
        <li class="collapseTitle2"><a class="nav-header" href='#' data-toggle="collapse" data-target="#customReportList"><i class="icon-edit"></i> Custom Reports</a> </li>
        <ul id="customReportList" class="nav nav-list in collapse">
        <li><a class="" href="CreateReport.htm">> Create Custom Report</a></li>
         <hr />
        <c:forEach items="${customReports}" var="custom">
        <li><a href="<c:url value="/reportingdashboard/ViewReport.htm"><c:param name="reportId" value="${custom.id}"/></c:url>">- ${custom.title}</a></li>
		</c:forEach>
		 </ul>
        
        </ul>
        </ul>
      <%--end Custom sidebar block well--%>
		</div>
    <%--end wellsquare grey backdrop--%>
    </div>
  <%--end sidebar--%>
 </div>
