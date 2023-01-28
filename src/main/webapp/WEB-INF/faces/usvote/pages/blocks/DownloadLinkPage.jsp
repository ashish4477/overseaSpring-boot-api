<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<link rel="stylesheet" href="<c:url value="/css/jquery-ui.css"/>">


<div class="row">
    <div class="col-xs-12">
        <div class="block-page-title-block">
            <h1 class="title">Download Your Absentee Ballot Request Form</h1>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-sm-10 offset-sm-1">
        <div class="form-group">
            <h4>Remember to download it in the next 7 days before it expires. All you have to do is print, sign, and mail it to your election office. Happy voting. </h4>
            <div class="bd" id="overseas-vote-foundation-short">
                <div class="bd-inner">
                    <div style="padding:35px; text-align: center;">
                        <span id="pdf-download-link"><a href="<c:url value="/downloadPdf.htm?generationUUID=${uuid}"/>" id="download-link" class="button">Download</a></span>
                    </div>
                    <div class="security-recommendation">
                        <h2>* PS: Downloading your form may take up to 30 seconds.</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>