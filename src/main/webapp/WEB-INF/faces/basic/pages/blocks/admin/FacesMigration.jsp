<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column">
	<div class="hd">
		<h2>${title}</h2>
	</div>
	<div class="bd">
        <div style="padding:10px;">
            <p>Download faces migration file and save it for transfer to another instance. </p>
	        <a href="<c:url value="/admin/GetFacesFile.do"/>" target="_new" id="download-link"><img src="<c:url value="/img/buttons/just-download-button.gif" />" alt=""/></a>
        </div>
        <div style="padding:10px;">
            <c:forEach items="${facesMigrationContext.messages}" var="msg">
                <p>${msg}</p>
            </c:forEach>
            <form action="<c:url value="/admin/FacesMigration.htm"/>" enctype="multipart/form-data" method="post">
                <p>Upload faces migration file</p>
                <input type="file" name="migrationZip">
                <input type="submit" name="save" value="Upload"/>
            </form>
        </div>
    </div>
	<div class="ft"></div>
</div>
