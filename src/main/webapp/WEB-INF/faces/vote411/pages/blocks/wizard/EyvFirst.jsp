<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 19, 2008
  Time: 10:11:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<fmt:setLocale value="en" scope="page"/>

<div class="content-pad">
    <div id="steps-diagram">
        <ul>
            <li class="first">
                <img src="<c:url value="/img/icons/evy-diagram-step-1.jpg" />" alt="1. Choose your country" />          
                <div class="text"><div class="num">1.</div>Choose Your Country</div>
            </li>
            <li>
                <img src="<c:url value="/img/icons/evy-diagram-step-2.jpg" />" alt="2. Create &amp; Print your FedEx&reg; Air Waybill" />
                <div class="text"><div class="num">2.</div>Create &amp; Print your FedEx&reg; Air Waybill</div>
            </li>
            <li>
                <img src="<c:url value="/img/icons/evy-diagram-step-3.jpg" />" alt="3. Call for Pickup or go to Drop-off" />
                <div class="text"><div class="num">3.</div>Call for Pickup or go to Drop-off</div>
            </li>
            <li>
                <img src="<c:url value="/img/icons/evy-diagram-step-4.jpg" />" alt="4. Receive delivery confirmation" />
                <div class="text"><div class="num">4.</div>Receive delivery confirmation</div>
            </li>
        </ul>
    </div>

	<div class="left-pane">
        <authz:authorize ifAnyGranted="ROLE_VOTER,ROLE_ADMIN,ROLE_FACE_ADMIN">
            <c:if test="${not empty labels}">
                <p style="clear: both;">
                    <c:forEach items="${labels}" var="label">
                        <c:if test="${not label.expired}">
                            <a href="<c:url value='/ExpressGetLabel.htm?tracking=${label.trackingNumber}'/>" target="_blank">Re-print Air Waybill ${label.trackingNumber}</a><br />
                        </c:if>
                    </c:forEach>
                </p>
            </c:if>
        </authz:authorize>
		 <div class="bl-country">

            <form action="<c:url value='ExpressYourVote.htm'/>" name="expressYourVote" method="post">
                <input type="hidden" name="submission" value="true"/>
               <!-- Program Ended <h2><strong>Yes!</strong> I want to Express my Vote:</h2> -->
                <p>
                    <spring:bind path="expressForm.countryId">
                        <select name="${status.expression}" id="select" class="input-select">
                            <option value="0">Choose country</option>
                            <c:forEach items="${fcountries}" var="country">
                            	<c:choose>
                            		<c:when test="${not empty country.rate && not empty country.exchangeRate && country.exchangeRate gt 0 && not empty country.localCurrencyName}">
                            			<c:set var="price"><fmt:formatNumber  value="${country.exchangeRate * country.rate}" minFractionDigits="2" /> ${country.localCurrencyName}</c:set>
                            		</c:when>
                            		<c:when test="${not empty country.rate && country.rate gt 0}">
                            			<c:set var="price">US <fmt:formatNumber  value="${country.rate}" type="currency" currencySymbol="$" /></c:set>
                            		</c:when>
                            		<c:otherwise>
                            			<c:set var="price">Free</c:set>
                            		</c:otherwise>                       	
                                </c:choose>
                               	<c:choose>
	                                <c:when test="${country.id eq status.value}">
	                                    <option value="${country.id}" selected="selected"><c:out value="${country.name} - ${price}" /></option>
	                                </c:when>
	                                <c:otherwise>
	                                    <option value="${country.id}" ><c:out value="${country.name} - ${price}" /></option>
	                                </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </p>
                <p><input type="image" src="img/buttons/continue-button.gif" alt="Continue..." name="_target${currentStep+1}" value=""/></p>
                <p>Your country is not on this list?
                <span class="rava-bubble">We apologize if your country is not on the list of participating countries &mdash; we have done all we can to make Express Your Vote as broad-reaching as possible, but there are some exceptions.  Please check the <a href="<c:url value="/svid.htm"/>">State Voter Information Directory</a> for your state to confirm your ballot return delivery options. Regular postal mail is acceptable in all situations, from any country to any state.</span></p>
            </form>

        </div>
        <div class="bl-track">
            <form action="http://www.fedex.com/Tracking" method="get" target="_blank">
                <h2>Track your ballot here</h2>
                <authz:authorize ifNotGranted="ROLE_VOTER,ROLE_ADMIN,ROLE_FACE_ADMIN">
                    <p>
                    	<c:choose>
                            <c:when test="${not empty trackingNumber}">
                                <input type="text" name="tracknumbers" id="track" value="<c:out value="${trackingNumber}"/>" class="input-text" />
                            </c:when>
                            <c:when test="${serviceStarted}">
                                <input type="text" name="tracknumbers" id="track" value="Enter your tracking number " class="input-text" />
                            </c:when>                                
                            <c:otherwise>
                                <input type="text" name="tracknumbers" id="track" value="" class="input-text" />
                            </c:otherwise>
                        </c:choose>
					</p>
                </authz:authorize>
                <authz:authorize ifAnyGranted="ROLE_VOTER,ROLE_ADMIN,ROLE_FACE_ADMIN">
                    <p>
                        <c:choose>
                            <c:when test="${not empty labels}">
                                <select name="tracknumbers"   class="input-text">
                                    <c:forEach items="${labels}" var="label">
                                        <option value="${label.trackingNumber}"<c:if test="${label.trackingNumber} eq ${trackingNumber}"> selected="selected"</c:if>>${label.trackingNumber}</option>
                                    </c:forEach>
                                </select>
                            </c:when>
                            <c:when test="${not empty trackingNumber}">
                                <input type="text" name="tracknumbers" id="track" value="<c:out value="${trackingNumber}"/>" class="input-text" />
                            </c:when>
                            <c:when test="${serviceStarted}">
                                <input type="text" name="tracknumbers" id="track" value="Enter your tracking number " class="input-text" />
                            </c:when>                                
                            <c:otherwise>
                                <input type="text" name="tracknumbers" id="track" value="" class="input-text" />
                            </c:otherwise>
                        </c:choose>
                    </p>
                </authz:authorize>
                <p><input type="image" src="img/buttons/track-button.gif" alt="Track" /></p>
            </form>
        </div>
	</div>

   	<%-- if Program Running
    <p>Overseas Vote is proud to once again offer Express Your Vote – an innovative initiative supported by FedEx&reg; Express – providing you access to special OVF rates for FedEx&reg; service, speed, and reliability when shipping your ballot back to your election office for the U.S. General Election on November 6, 2012.
    </p>

	<p>Currently running in 94 countries, Express Your Vote brings the latest in shipment tracking technology and automatic delivery notification to you.
	</p>
	
	<p>Express Your Vote rates are highly-discounted, depending on the country of operation. Please check the lists of participating states and countries. Express Your Vote — and be confident it will be there in time to be counted!
	</p>

  <p>To ensure that your vote arrives on time please ship your ballots before the last ship date specified for your country.
    </p>

	<p class="note"><img src="<c:url value="/img/icons/notice.gif"/>" alt="Exclamation Point">Please Note: Express Your Vote is for <strong>BALLOTS ONLY.</strong><br /> Registration/Ballot Request Forms are NOT included in this offer. This offer is available through November 1, 2012,
  </p>

  <p style="font-weight:bold; position:relative; left:25px;">Please Note: Closing dates and times for this offer vary by country.</p>

    --%>
   <%-- if Program Ended --%>
	<h2>Thank you for your interest in Express Your Vote</h2>
	
	<p style="color:#B90000; font-weight:bold;">The Express Your Vote program expired November 1, 2012 <br/> and is no longer available.</p>
	<p>The program provided voters from 94 countries with fast and reliable FedEx&reg; Express services to ship ballots back to U.S. election offices for the 2012 General Election.</p>
	<h2>Quick Links:</h2>
	<ul>
		<li><a href="https://www.overseasvotefoundation.org/vote/home.htm">Overseas Vote Home Page</a></li>
		<li><a href="http://www.fedex.com">FedEx Home Page</a></li>
	</ul>
	
	<p>Please send your questions to: <a href="mailto:voterhelpdesk@overseasvotefoundation.org">voterhelpdesk@overseasvotefoundation.org</a></p>

</div>

<%--
<div class="content-pad states">

	<h3>Participating US States and territories</h3>
	
	<c:set var="colCurrent" value="1"/>
	<c:set var="colItems" value="0"/>
	<c:set var="dumpHeader" value="true"/>	
	<c:set var="dumpFooter" value="false"/>	
	
	<c:set var="statesPerCol" value="10"/> 
	<c:forEach items="${states}" var="state" varStatus="status">
		<c:if test="${state.name ne 'Alabama'}">
			<c:if test="${dumpHeader}">
				<ul id="states0${colCurrent}0">
				<c:set var="dumpHeader" value="false"/>
				<c:set var="colCurrent" value="${colCurrent + 1}"/>
			</c:if>
			<li>${state.name}</li>
			<c:set var="colItems" value="${colItems+1}"/>
			<c:if test="${colItems > statesPerCol}">
				<c:set var="colItems" value="0"/>
				<c:set var="dumpHeader" value="true"/>
				<c:set var="dumpFooter" value="true"/>
			</c:if>
			<c:if test="${dumpFooter || status.last}">
				<c:set var="dumpFooter" value="false"/>
				</ul>
			</c:if>
		</c:if>
	</c:forEach>

	
	<div class="clear"></div>
	<p>Your state is not on this list? <span class="rava-bubble">Unfortunately, some U.S. states specify that ballots arrive only by certain methods, not including FedEx. If your state is not on the list of participating state, please check the <a href="/state-vote-information-directory">State Voter Information Directory</a> for other allowable ballot return delivery options.</span></p>

</div>
--%>
