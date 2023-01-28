<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jul 3, 2008
  Time: 3:04:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="content-pad">
    <h2>Transaction Summary</h2>

    <p class="waybill">Your FedEx&reg; Air Waybill (AWB) number is</p>

    <div class="waybill">${expressForm.fedexLabel.trackingNumber}</div>

    <p class="waybill">Do not forget to <strong>staple a <u>copy</u> of your FedEx Air Waybill</strong> to the ballot return envelope!</p>
    <p class="waybill"><a href="<c:url value="ExpressGetLabel.htm" ><c:param name="tracking" value="${expressForm.fedexLabel.trackingNumber}"/></c:url>" target="_blank"><img src="img/buttons/print-waybill-button.gif" alt="" /></a></p>

    <h2>Estimated transit time to the US for your ballot: ${expressForm.country.deliveryTime}*</h2>
    
    <c:set var="country" value="${expressForm.country}"/>

        <p>
        <strong>
        <c:if test="${country.pickup}">        	
            <c:choose>
                <c:when test="${country.group eq 'LAC' or country.group eq 'EMEA'}">
                    <a href="${country.serviceUrl}" target="_blank">Call to schedule a pick up</a><c:if test="!${country.dropoff}">.</c:if>
                </c:when>
                <c:otherwise>
                    <a href="${country.serviceUrl}" target="_blank">Call to book a pick up</a><c:if test="!${country.dropoff}">.</c:if>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test="${country.dropoff}">
            <c:if test="${country.pickup}"> or <a href="${country.dropoffUrl}" target="_blank"> find your nearest drop off location</a>.</c:if>
            <c:if test="!${country.pickup}"> <a href="${country.dropoffUrl}" target="_blank"> Find your nearest drop off location</a>.</c:if>             
         </c:if>
		</strong>
        Please use the following reference when scheduling your pick up or dropping your shipment:<br />
        <strong>VOTE EXPRESS 918273</strong></p>
        
        <p>Drop off or pick-up services may not be available in some countries. Please refer to your country page at Express Your Vote or contact FedEx for the available options in your country of residence.</p>
        
        <ol id="download-instructions">
        	<li>Please print three (3) copies of the FedEx International Air Waybill:
				<ol class="lettered">
					<li>Two (2) copies must be placed in the transparent pouch which is attached to the standard FedEx envelope</li>
					<li>One (1) copy must be stapled to your ballot return envelope. The Air Waybill will act as the international postmark, required for your ballot.</li>
					<li>The ballot return envelope must then be placed in the FedEx envelope.</li>
				</ol>
			</li>

        	<li>The FedEx envelope is the only packaging accepted for an Express Your Vote ballot shipment. A FedEx envelope can be obtained from your FedEx contact person.
			</li>
        	<li>Before sealing your FedEx envelope:
				<ol class="lettered">
					<li>If you received your ballot from your election office: Check that you have completed, signed and dated the outside of the ballot return envelope (if required) and any ballot affidavits;</li>
					<li>If you have used the Federal Write-in Absentee Ballot confirm that you have followed the envelope and other instructions outlined in the letter accompanying your ballot.</li>
				</ol>
				   <p><strong>IMPORTANT:</strong>  Your FedEx Air Waybill serves as your ballot’s postmark. Without it your ballot will be invalid. Only one (1) Ballot per FedEx Envelope.</p>
			</li>  
			<li>My Voter Account:  through use of the Express Your Vote service you have now established a new Voter Account or updated your existing Voter Account on the OVF system.
				<ol class="lettered">
					<li>You can login to this account to update your voter registration profile through the main OVF or partner site.</li>
					<li>Never share your Voter Account. </li>
                    <li>Please logout of your Voter Account before leaving the Express Your Vote system or allowing any other user to use your system.</li>
				</ol>
			</li>  
			   
        </ol>
        <p><b>* NOTE:</b> The FedEx Money-Back Guarantee does NOT apply to Express Your Vote shipments. To ensure that your ballot arrives on time, please ship on or before <span class="date">${country.lastDateText}, <fmt:formatDate value="${country.lastDateLocal}" pattern="MMMM dd, yyyy"/></span>.</p>
        <p><a href="#" onclick="window.print(); return false;">Print this page</a></p>
       <p><a href="<c:url value="/logout"/>" target="_top">Close session and log out now</a></p>
    <p>You can now track your shipment on <a href="http://www.fedex.com/Tracking?tracknumbers=${expressForm.fedexLabel.trackingNumber}">fedex.com</a> or from the <a href="<c:url value="/ExpressYourVote.htm?trackingNumber=${expressForm.fedexLabel.trackingNumber}"/>">Express Your Vote home page</a></p>

    <p><strong>FedEx&reg; Customer Service Contact:</strong> <a href="${country.serviceUrl}">Online Customer Service</a> or Phone [${country.servicePhone}]</p>


</div>