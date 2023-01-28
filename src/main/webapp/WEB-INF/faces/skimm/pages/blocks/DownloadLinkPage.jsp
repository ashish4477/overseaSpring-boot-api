<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<link rel="stylesheet" href="<c:url value="/css/jquery-ui.css"/>">

<div class="body-content row column wide-content">
<div class="page-form col-xs-12 col-sm-10 col-sm-offset-1">
    <h1 class="title">Download your absentee ballot request form</h1>
    <h4>Remember to download it in the next 7 days before it expires. All you have to do is print, sign, and mail it to your election office. Happy voting. </h4>
    <div class="bd" id="overseas-vote-foundation-short">
    <div class="bd-inner">
        <div style="padding-left:50px; padding-bottom:15px; padding-top: 15px;">             
            <span id="pdf-download-link"><a href="<c:url value="/downloadPdf.htm?generationUUID=${uuid}"/>" id="download-link" class="button">Download</a></span>
        </div>
        <div class="security-recommendation">
            <h2>* PS: Downloading your form may take up to 30 seconds.</h2>
        </div>
    </div>
  </div>
</div>
</div>