<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 19, 2008
  Time: 10:09:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="content-pad">
     <c:if test="${serviceActive == 'false'}">
      <h2 style="color:#B90000"> This program has expired. The FedEx&reg; Air Waybill cannot be created at this time.<br>The following information is for reference only:</h2><br/>
  </c:if>

    <h2>Service information for ${country.name}</h2>

    <p>In order to access the Express Your Vote services, you must create a FedEx&reg; Air Waybill (AWB) through the Express Your Vote system.   The FedEx&reg; Air Waybill can be created after you complete payment on this site. A manual AWB is not accepted.</p>

    <h3>Availability</h3>
    <p>The Express Your Vote shipping services for ballot return will be available from ${country.name}<br /><span class="date">October 13 through ${country.lastDateText}, <fmt:formatDate value="${country.lastDateLocal}" pattern="MMMM dd, yyyy"/></span>. To ensure that your vote arrives on time, please ship your ballots before <span class="date">${country.lastDateText}, <fmt:formatDate value="${country.lastDateLocal}" pattern="MMMM dd, yyyy"/></span>.</p>

    <h3>Transit Time</h3>
    <p>Estimated transit time to the US for your Express Your Vote shipment: <span class="date">${country.deliveryTime}</span></p>
   	<p><strong>IMPORTANT:</strong>  Your FedEx&reg; Air Waybill serves as your ballot’s postmark. Your ballot is only valid with this postmark. Only one (1) ballot is allowed per FedEx&reg; Envelope. Please complete one order per ballot.</p>

    <h3>Express Your Vote Shipping Rate:
        <c:choose>
            <c:when test="${country.rate eq 0}">
                <span class="date">Free</span>
            </c:when>
            <c:when test="${country.exchangeRate eq 0.}">
                <span class="date">USD <fmt:formatNumber value="${country.rate}" type="currency" currencySymbol="$" /> (payment accepted by credit card only)</span>
            </c:when>
            <c:otherwise>
                <span class="date"><fmt:formatNumber value="${country.exchangeRate}" maxFractionDigits="2" minFractionDigits="2"/> ${country.localCurrencyName} = ( US <fmt:formatNumber value="${country.rate}" type="currency" /> )</span>
            </c:otherwise>
        </c:choose>

    </h3>

     <c:if test="${serviceActive}">
        <form action="<c:url value='ExpressYourVote.htm'/>" name="expressYourVote" method="post">
            <input type="hidden" name="submission" value="true"/>
            <p class="final-button"><input type="image" src="img/buttons/create-express-button.gif" alt="Create my Express Your Vote Air Waybill" id="create-awb-button" name="_target${currentStep+1}" value=""/></p>
        </form>
    </c:if>

    <p id="gaurantee">The FedEx&reg; Express Money-Back Guarantee does not apply to shipments sent using the Overseas Vote discount for the <b>Express Your Vote</b> program.<br />Please see <a href="${country.tcUrl}" target="_blank">the FedEx&reg; Service Guide</a> for terms and conditions of carriage.</p>
    <c:if test="${serviceActive}">
    <h3>FedEx&reg; Service Access</h3>
    <ul>
        <c:if test="${country.pickup}">
            <c:choose>
                <c:when test="${country.group eq 'LAC' or country.group eq 'EMEA'}">
                    <li><a href="${country.serviceUrl}" target="_blank">Call FedEx&reg; to schedule a pick up</a> </li>
                </c:when>
                <c:otherwise>
                    <li><a href="${country.serviceUrl}" target="_blank">Call FedEx&reg; to book a pick up</a> </li>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test="${country.dropoff}">
            <c:choose>
                <c:when test="${fn:toUpperCase(country.group) eq 'CANADA'}">
                    <li>
                        <c:if test="${country.pickup}">(Or)</c:if> Drop off your shipment at any <a href="${country.dropoffUrl}" target="_blank">FedEx&reg; World Service Centre</a>, FedEx&reg; Office™ <a href="${country.dropoffUrl}" target="_blank">Print and Ship Centre</a> or self-service FedEx&reg; Express Drop Box location.
                    </li>
                </c:when>
                <c:when test="${fn:toUpperCase(country.group) eq 'APAC'}">
                    <li>
                        <c:if test="${country.pickup}">(Or)</c:if> Drop off at <a href="${country.dropoffUrl}" target="_blank">FedEx&reg; Station or World Service Centre</a> only.
                    </li>
                </c:when>
                <c:when test="${country.rate eq 0}">
                    <li><c:if test="${country.pickup}">(Or)</c:if> Drop off your shipment at a <a href="${country.dropoffUrl}" target="_blank">FedEx&reg; World Service Center</a> or <a href="${country.dropoffUrl}" target="_blank">FedEx&reg; Stations</a></li>
                </c:when>
                <c:otherwise>
                    <li><c:if test="${country.pickup}">(Or)</c:if> Drop off your shipment at a <a href="${country.dropoffUrl}" target="_blank">FedEx&reg; World Service Center</a></li>
                </c:otherwise>
            </c:choose>
        </c:if>

         <c:if test="${country.countryCode eq 'AU' or country.countryCode eq 'CN' or country.countryCode eq 'ID' or country.countryCode eq 'JP' or country.countryCode eq 'NZ' or country.countryCode eq 'TW' or country.countryCode eq 'KR'}">
        	<li>Absentee ballots shipped via the FedEx&reg; <b>Express Your Vote</b> program will NOT be accepted at FedEx&reg; Office (formerly FedEx&reg; Kinko's) drop off locations and FedEx&reg; Authorized Ship Centers.  Please go to the FedEx&reg; World Service Center or FedEx&reg; Station locations.</li>
 		</c:if>
         
    </ul>
    </c:if>

    <p class="final-button"><a href="<c:url value="/ExpressYourVote.htm"><c:param name="countryId" value="${country.id}" /></c:url>"><img src="img/buttons/back-button.gif" alt="Back" /></a></p>
</div>