<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column">
	<div class="hd">
		<h2>${title}</h2>
	</div>
    <div class="bd">
         <c:forEach items="${migrationContext.messages}" var="msg">
             <p class="status-message">${msg}</p>
         </c:forEach>
        <div style="padding:10px;">
            <form:form commandName="migrationContext" action="GetMigrationFile.do" target="_new">
                <p><b>Download flow migration file and save it for transfer to another instance.</b></p>
                <%--<p>
                    <form:label path="pageType">Select page type</form:label>
                    <form:select path="pageType">
                        <form:option value="" label="Migrate All"/>
                        <form:options/>
                    </form:select>
                </p>--%>
                <p>
                    <label for="fileType">Select output file type:</label>
                    <select id="fileType" name="fileType">
                        <option value="zip">ZIP file</option>
                        <option value="json">Formatted JSON</option>
                    </select>
                </p>
                <%--<p>
                    <form:checkbox path="includeFillings"/>
                    <form:label path="includeFillings">Include PDF instructions</form:label>
                </p>--%>
                <input type="image" src="<c:url value="/img/buttons/just-download-button.gif" />" alt=""/>
            </form:form>
            <h4>Important Note:</h4>
            <p>Migration file for selected flow does not contain some information from questionnaire.
                It's not important in most cases. But it's recommended to use migration file for all flows
                when working on questionnaire gets completed.
            </p>

        </div>
        <div style="padding:10px;">
            <form action="<c:url value="/admin/QuestionnaireMigration.htm"/>" enctype="multipart/form-data" method="post">
                <p>
                    <b>Upload flow migration file</b><br/>
                    Your could upload either ZIP or JSON file.
                </p>
                <input type="file" name="migrationZip">
                <input type="submit" name="save" value="Upload"/>
            </form>
            <h4>Important Note:</h4>
            <p>Migration process would work incorrectly if there is any report in Reporting Dashboard.
                It's hardly recommended to use migration process before adding reports in Reporting Dashboard.</p>
        </div>
        <c:if test="${databaseConflict}">
            <div style="padding:10px;">
                <form action="<c:url value="/admin/QuestionnaireMigration.htm"/>" method="post">
                    <h3 style="color:red">There are conflicts in an auxiliary DB table.</h3>
                    <p>
                        Last migration upload produced some conflicts in the DB. It's not a problem
                        but it's recommended to fix it.
                    </p>
                    <input type="submit" name="clearConflicts" value="Clear Conflicts"/>
                </form>
            </div>
        </c:if>
    </div>
	<div class="ft"></div>
</div>
