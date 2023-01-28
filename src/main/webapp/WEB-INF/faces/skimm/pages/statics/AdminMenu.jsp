<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Aug 21, 2007
  Time: 7:50:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<authz:authorize ifAllGranted="ROLE_ADMIN">
	<div id="admin-menu">
		<div class="page-content">
			<div class="hd">
				<div class="hd-inner">
					<h2>Admin Menu:</h2>
				</div>
			</div>
			<div class="bd">
				<div class="bd-inner">
					<div class="col">
						<h3>EOD:</h3>
						<ul>
                                    <li><a href="<c:url value="/admin/EodStates.htm"/>">EOD States List</a></li>
                                    <li><a href="<c:url value="/admin/EodCorrectionsList.htm?status=1"/>">EOD Corrections Sent</a></li>
                                    <li><a href="<c:url value="/admin/EodCorrectionsList.htm?status=2"/>">EOD Corrections Approved</a></li>
                                    <li><a href="<c:url value="/admin/EodCorrectionsList.htm?status=3"/>">EOD Corrections Rejected</a></li>

							<li><a href="<c:url value="/admin/InvalidateCaches.htm"/>"/>Cache</li>
							<li><a href="<c:url value="/admin/ExportConfigurationsList.htm"/>"/>SFTP Transfer Configurations</li>
							<li><a href="<c:url value="/admin/PropertiesList.htm"/>">OVF System Properties List</a></li>
						</ul>
					</div>
					<div class="col">
						<h3>Questionnaire:</h3>
						<ul>
							<li><a href="<c:url value="/admin/QuestionnaireView.htm"/>">Questionnaire Overview</a></li>
							<li><a href="<c:url value="/admin/QuestionnairePages.htm"/>">Pages List</a></li>
							<li><a href="<c:url value="/admin/FieldTypes.htm"/>">Field Types List</a></li>
							<li><a href="<c:url value="/admin/InstructionsList.htm"/>">Voter Instructions List</a></li>
							<li><a href="<c:url value="/admin/QuestionnaireMigration.htm"/>">Questionnaire Migration</a></li>
							<li><a href="<c:url value="/reportingdashboard/ReportingDashboard.htm"/>">Reporting Dashboard</a></li>
						</ul>
					</div>
					<div class="col">
						<h3>Other:</h3>
						<ul>
							<li><a href="<c:url value="/admin/AccountsList.htm"/>">Voter Accounts List</a></li>
							<li><a href="<c:url value="/admin/FacesConfigsList.htm"/>">Faces Configs List</a></li>
							<li><a href="<c:url value="/admin/FacesMigration.htm"/>">Faces Migration</a></li>
							<li><a href="<c:url value="/admin/Statistics.htm"/>">Statistics</a></li>
							<%--<li><a href="<c:url value="/admin/EyvCountryList.htm"/>">FedEx Countries List</a></li>--%>
							<li><a href="<c:url value="/admin/MailingLists.htm"/>">Mailing Lists</a></li>
							<li><a href="<c:url value="/logout"/>">Logout</a></li>
						</ul>
					</div>
					<div class="break"></div>
				</div>
			</div>
			<div class="ft"><div class="ft-inner"></div></div>
		</div>
	</div>
</authz:authorize>
<authz:authorize ifNotGranted="ROLE_ADMIN">
	<authz:authorize ifAllGranted="ROLE_FACE_ADMIN" >
		<div id="admin-menu">
			<h2>Admin Menu:</h2>
			<div class="bd">
				<div class="col">
					&nbsp;
				</div>
				<div class="col">
					<h3>Reports:</h3>
					<ul>
						<li><a href="<c:url value="/report/ReportQueryList.htm"/>">Reporting Dashboard</a></li>
					</ul>
				</div>
				<div class="col">
					<h3>Other:</h3>
					<ul>
							<%--<li><a href="<c:url value="/mail/MailTemplates.htm"/>">Edit mail templates</a></li>--%>
						<li><a href="<c:url value="/logout"/>">Logout</a></li>
					</ul>
				</div>
				<div class="break"></div>
			</div>
		</div>
	</authz:authorize>
</authz:authorize>