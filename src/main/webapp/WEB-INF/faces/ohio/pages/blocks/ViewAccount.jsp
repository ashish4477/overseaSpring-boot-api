<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 10, 2007
	Time: 4:09:39 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<div id="voter-account-page" class="voter-account-page page-form body-content content column wide-content">
    <div class="hd">
        <div class="hd-inner">
            <h2 class="title">My Voter Account</h2>
        </div>
    </div>

    <div class="bd">
        <div class="bd-inner">
            <c:if test="${updated}">
                <div id="update-message" class="success">Your account information has been updated</div>
            </c:if>
		<authz:authorize ifAllGranted="ROLE_REPORTING_DASHBOARD">
            <div class="rounded-5" style="padding:2px 22px; border:solid #ccc 1px; background-color:#fff;">
            <ul class="actions">
                <li><a href="<c:url value="/reportingdashboard/ReportingDashboard.htm"/>">View Reporting Dashboard</a></li>
            </ul>
            </div>
			<p>&nbsp;</p>
		</authz:authorize>
            
            <div style="padding:12px 22px; border:solid #ccc 1px; background-color:#fff;" class="rounded-5">
             <h2 class="mva">Voter Account Information</h2>
             <div class="content">
			<div style="padding-left:25px;">
				<div class="column left">
            	<h3>User Information</h3>
            	<div class="content">
            		<c:if test="${not empty user.name.firstName}">${user.name.firstName}</c:if> <c:if test="${not empty user.name.middleName}">${user.name.middleName}</c:if> <c:if test="${not empty user.name.lastName}">${user.name.lastName}</c:if> <c:if test="${not empty user.name.suffix}">${user.name.suffix}</c:if>
            		<p>${user.username}</p>
            		<c:if test="${not empty user.phone}"><p>Ph. ${user.phone}</p></c:if>
            		<c:if test="${not empty user.alternatePhone}"><p>Alt. Ph. ${user.alternatePhone}</p></c:if>
            		<c:if test="${not empty user.alternateEmail}"><p>Alt. Email: ${user.alternateEmail}</p></c:if>
            	</div>
				</div>
			<div class="column right">
           	<c:if test="${not empty user.forwardingAddress and not user.forwardingAddress.emptySpace }">
               <h3>Ballot Forwarding Address</h3>
               <div class="content">
               	<div>${user.forwardingAddress.formattedAddress}</div>
               </div>
           </c:if>
           </div>
			<div class="break">&nbsp;</div>
			<div>
				<div class="column left">
	            	<h3>Last U.S. Residence Address</h3>
	            	<div class="content">
	            		<div>${user.votingAddress.formattedAddress}</div>
	            	</div>
				</div>
				<div class="column right">
	            		<h3>Current Address</h3>
	            		<div class="content">
	            			<div>${user.currentAddress.formattedAddress}</div>
						</div>
				</div>
			</div>
			<div class="break"></div>
			</div>
			</div></div>
			<p>&nbsp;</p>
            <div style="padding:12px 22px; border:solid #ccc 1px; background-color:#fff;" class="rounded-5">
            <h2>Generate Voter Registration &amp; Ballot Requests</h2>
            <ul class="actions">
                <li><a href="<c:url value="/w/rava.htm"/>">Voter Registration/Absentee Ballot Request Form</a></li>
            </ul>
            </div>
			<p>&nbsp;</p>
<%--
            <c:if test="${not empty leo}">
            <div style="padding:12px 22px; border:solid #ccc 1px; background-color:#fff;" class="rounded-5">
                <h2 class="eod">Election Officials Serving ${user.votingRegion.name}, ${user.votingRegion.state.name}</h2>
	                <div class="content" style="padding-left:25px;">
		                <div class="column left">
		                    <c:if test="${not empty leo.leo.firstName or not empty leo.leo.lastName}"><h3>Local Election Official:</h3></c:if>
		                    <div class="content">
		                   		${leo.leo.title} ${leo.leo.firstName} ${leo.leo.lastName} ${leo.leo.suffix}
		                   		<c:if test="${not empty leo.leoEmail}">
		                   			<p><a href="mailto:${leo.leoEmail}">${leo.leoEmail}</a></p>
		                   		</c:if>
		                    	<c:if test="${not empty leo.leoPhone}">
		                        	<p>Phone: ${leo.leoPhone}</p>
		                    	</c:if>
		                    </div>
		                </div>
		                <div class="column right">
		                    <c:if test="${not empty leo.lovc.firstName or not empty leo.lovc.lastName}"><h3>Absentee Voter Clerk:</h3></c:if>
		                    <div class="content">
		                   		${leo.lovc.title} ${leo.lovc.firstName} ${leo.lovc.lastName} ${leo.lovc.suffix}
		                   		<c:if test="${not empty leo.leoEmail}">
		                   			<p><a href="mailto:${leo.lovcEmail}">${leo.lovcEmail}</a></p>
		                   		</c:if>
		                    	<c:if test="${not empty leo.lovcPhone}">
		                        	<p>Phone: ${leo.lovcPhone}</p>
		                    	</c:if>
		                    </div>
		                </div>
		                <div class="break"></div><br />
		                 <ul>
	                	<c:if test="${empty leo.website}"><li><a target="_blank" href="${leo.website}">Am I Registered?</a></li></c:if>
	              		<li><a href="<c:url value="/eod.htm"><c:param name="submission" value="true"/><c:param name="stateId">${user.votingRegion.state.id}</c:param><c:param name="regionId">${user.votingRegion.id}</c:param> </c:url> ">View More Details for This Location </a> </li>
					</ul>
	              	</div>
	              	</div>
	               </c:if>
--%>
			        <div style="padding:12px 22px;">
		            <h2 style="font-size:13px;">Note to all "My Voter Account" holders: </h2>
		            <p>OVF <cite>never</cite> sends email to account holders requesting Voter Account registration details, account updates, or suggesting that your account will expire.  Any such emails can be considered spam and do not originate from OVF.</p>
		            <p>&nbsp;</p>
		            <h2 style="font-size:13px;">Donation Information </h2>
		            <p>Help support our goal of providing online overseas voter registration services to U.S. citizens around the globe! Overseas Vote is a nonprofit, nonpartisan 501(c)(3) public charity and can use your financial support. <a href="/donate" target="_top">Make a donation to OVF </a></p>
		            </div>
	            </div>
            </div>
    <div class="ft"><div class="ft-inner"></div></div>
</div>

