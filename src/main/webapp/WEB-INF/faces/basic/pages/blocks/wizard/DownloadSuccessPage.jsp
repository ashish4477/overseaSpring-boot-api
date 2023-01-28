<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<link rel="stylesheet" href="<c:url value="/css/jquery-ui.css"/>">
<div class="success-page">
<div class="row">
    <div class="col-xs-12 text-center">
        <h1 class="title">Congratulations!</h1>
        <h2>Now, help your friends to get ready for election day!</h2>

       <a href="https://www.facebook.com/sharer/sharer.php?u=https://secure.avaaz.org/en/globalvote_partners/" target="_blank">
            <div id="shareBtn" class="btn btn-lg btn-facebook clearfix"><i class="fa fa-facebook"></i> Share on Facebook</div>
        </a>
    </div>
</div>
<p>&nbsp;</p>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1">
    <h3>IMPORTANT! - You MUST complete the following to finish your registration.</h3>
    <ol>
        <li>Check your downloads folder for your registration form. If you need to make any changes, click the Back button and update your details</li>
        <li>Print, sign, and submit your registration to the election official listed below</li>
        <li>Most states require that you also mail your completed registration even if you have faxed or emailed it. Avaaz has partnered with international shipping specialist DHL to provide a fast, easy-to-use solution for returning your completed form quickly. (Note that each state has <a href="/vote/svid.htm">different requirements</a> and we recommend that you ensure the sending method you choose is acceptable.)</li>
    </ol>
    <p class="text-center">
    <c:set var="dhlUser" value="${wizardContext.wizardResults.user}"/>
    <c:set var="registrar" value="${leo.officers[0]}"/>
    <c:url var="dhlIntegrationUrl" value="https://www.avaaz.org/static/dhl_shipping">
        <c:param name="userFirstName" value="${dhlUser.name.firstName}"/>
        <c:param name="userLastName" value="${dhlUser.name.lastName}"/>
        <c:param name="userPhone" value="${dhlUser.phone}"/>
        <c:param name="userEmail" value="${dhlUser.username}"/>
        <c:param name="userCurrentAddressStreet1" value="${dhlUser.currentAddress.street1}"/>
        <c:param name="userCurrentAddressStreet2" value="${dhlUser.currentAddress.street2}"/>
        <c:param name="userCurrentAddressCity" value="${dhlUser.currentAddress.city}"/>
        <c:param name="userCurrentAddressState" value="${dhlUser.currentAddress.state}"/>
        <c:param name="userCurrentAddressCounty" value="${dhlUser.currentAddress.county}"/>
        <c:param name="userCurrentAddressZip" value="${dhlUser.currentAddress.zip}"/>
        <c:param name="userCurrentAddressCountry" value="${dhlUser.currentAddress.country}"/>
        <c:param name="registrarOfficeName" value="${registrar.officeName}"/>
        <%--<c:param name="registrarDepartment" value=""/>--%>
        <c:param name="registrarAddressee" value="${leo.physical.addressTo}"/>
        <c:param name="registrarAddress" value="${leo.physical.fullStreet}"/>
        <c:param name="registrarCity" value="${leo.physical.city}"/>
        <c:param name="registrarZip" value="${leo.physical.zip}"/>
        <c:param name="registrarState" value="${leo.physical.state}"/>
        <c:param name="registrarName" value="${registrar.firstName} ${registrar.lastName}"/>
        <c:param name="registrarPhone" value="${registrar.phone}"/>
    </c:url>
    <a href="#" onclick="goDHL()" class="btn btn-default dhl">Schedule a pickup with DHL</a></p>
    <p>&nbsp;</p>

<script>
    function goDHL() {
        var dhlWindow = window.open("${dhlIntegrationUrl}", "DHL Shipping", "width=500,height=500");
    }
</script>

    <c:if test="${not empty leo}">
    <fieldset>
        <div>
            <h3>How to return your registration form:</h3>
            <c:if test="${not empty leo.generalEmail}">
                <p><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> <strong>By Email: email your registration form to</strong>
                <blockquote>${leo.generalEmail}</blockquote>
                </p>
            </c:if>
            <c:if test="${not empty leo.leoFax}">
                <p><span class="glyphicon glyphicon-earphone" aria-hidden="true"></span>
                    <strong>By Fax - you can use a popular online fax service like <a href="https://www.myfax.com/free/" target="_blank">https://www.myfax.com/free/</a> : </strong> <br/>
                      <blockquote>
                      ${leo.leo.fullName}<br/>
                        ${leo.leoFax}
                    </blockquote>
                </p>
            </c:if>
            <p><span class="glyphicon glyphicon-plane" aria-hidden="true"></span>
                <strong>By Mail:</strong><br>
                <address>
                <blockquote>
                    ${leo.mailing.addressTo}<br/>
                    ${leo.mailing.street1}<br/>
                <c:if test="${not empty leo.mailing.street2}">${leo.mailing.street2}<br/></c:if>
                    ${leo.mailing.city},
                    ${leo.mailing.state}
                    ${leo.mailing.zip}<c:if test="${not empty leo.mailing.zip4}">-${leo.mailing.zip4}</c:if>
                    </blockquote>
                </address>
            </p>
        </div>
        </fieldset>
    </c:if>

        <div style="padding-left:50px; padding-bottom:15px; margin-top:25px;">
        <p>If necessary, use the Back button to make changes to your Form and then repeat the download.</p>
            <a href="<c:url value="${wizardUrl}"><c:param name="back" value="true"/></c:url>" id="back-button" class="back button">Back</a>
        </div>
    </div>
</div>
</div>


