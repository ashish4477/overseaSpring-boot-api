<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--<c:if test="${not empty extendedProfile.user}">--%>
<div class="column">
<div class="col-xs-12 col-sm-10 right-side">
    <h2>My Democracy Profile</h2>
    <span><small><a href="<c:url value="/MyProfile.htm"/>" tabindex="-1"
                    role="menuitem">EDIT PROFILE</a></small></span>
</div>
</div>
<div class="column answers">
<div class="col-xs-12 mt-15">
    <div class="row">
        <div class="col-md-6">

            <div class="item-column voters">
                <h4 class="mva">Voter Information</h4>
                <fieldset>
                    <c:if test="${not empty extendedProfile.user.voterType}">
                        <p>Voter Type:<span> ${extendedProfile.user.voterType.title}</span> </p>
                    </c:if>
                    <c:if test="${not empty extendedProfile.votingMethod}">
                        <p>Primary Voting Method: <span>${extendedProfile.votingMethod} </span> </p>
                    </c:if>
                    <c:if test="${not empty extendedProfile.politicalParty}">
                        <p>Political Party: <span>${extendedProfile.politicalParty}</span></p>
                    </c:if>
                </fieldset>
            </div>  

            <div class="item-column voters">
                <h4 class="mva">Voting Participation</h4>
                <fieldset class="general-list-styling">
                    <c:if test="${not empty extendedProfile.socialMediaArr}">
                        <strong class="list-heading">Social Media:</strong>
                        <ul class="list-unstyled">
                            <c:set var="socialMediaTypes">
                                <c:out value="${extendedProfile.socialMedia}"/>
                            </c:set>
                            <c:set var="socialMedia" value="${fn:split(socialMediaTypes,';')}"/>
                            <c:forEach items="${socialMedia}" var="myMediaTypes">
                                <li><span class="glyphicon glyphicon-ok"></span> <c:out value="${myMediaTypes}"/></li>
                            </c:forEach>
                            <c:if test="${not empty extendedProfile.socialMediaOther}">
                                <li><span class="glyphicon glyphicon-ok"></span> ${extendedProfile.socialMediaOther}</li>
                            </c:if>
                        </ul>
                    </c:if>
                </fieldset>
            </div>

        </div>

        <div class="col-md-6">
            <div class="item-column voters">
                <h4 class="mva">Election Participation</h4>
                <fieldset class="general-list-styling">
                    <c:if test="${not empty extendedProfile.voterParticipationArr}">
                        <strong class="list-heading">I Vote in:</strong>
                        <ul class="list-unstyled">
                            <c:set var="electionTypes">
                                <c:out value="${extendedProfile.voterParticipation}"/>
                            </c:set>
                            <c:set var="elections" value="${fn:split(electionTypes,';')}"/>
                            <c:forEach items="${elections}" var="myElections">
                                <li><span class="glyphicon glyphicon-ok"></span> <c:out value="${myElections}"/></li>
                            </c:forEach>
                            <c:if test="${not empty extendedProfile.voterParticipationOther}">
                                <li><span
                                        class="glyphicon glyphicon-ok"></span> ${extendedProfile.voterParticipationOther}
                                </li>
                            </c:if>
                        </ul>
                    </c:if>
                    <c:if test="${not empty extendedProfile.outreachParticipationArr}">
                        <strong class="list-heading">Voter Outreach:</strong>
                        <ul class="list-unstyled">
                            <c:set var="outreachTypes">
                                <c:out value="${extendedProfile.outreachParticipation}"/>
                            </c:set>
                            <c:set var="outreach" value="${fn:split(outreachTypes,';')}"/>
                            <c:forEach items="${outreach}" var="myOutreach">
                                <li><span class="glyphicon glyphicon-ok"></span> <c:out value="${myOutreach}"/></li>
                            </c:forEach>
                            <c:if test="${not empty extendedProfile.outreachParticipationOther}">
                                <li><span
                                        class="glyphicon glyphicon-ok"></span> ${extendedProfile.outreachParticipationOther}
                                </li>
                            </c:if>
                        </ul>
                    </c:if>
                    <c:if test="${not empty extendedProfile.volunteeringArr}">
                        <strong class="list-heading">Volunteering:</strong>
                        <ul class="list-unstyled">
                            <c:set var="volunteeringTypes">
                                <c:out value="${extendedProfile.volunteering}"/>
                            </c:set>
                            <c:set var="volunteering" value="${fn:split(volunteeringTypes,';')}"/>
                            <c:forEach items="${volunteering}" var="myVolunteering">
                                <li><span class="glyphicon glyphicon-ok"></span> <c:out value="${myVolunteering}"/></li>
                            </c:forEach>
                            <c:if test="${not empty extendedProfile.volunteeringOther}">
                                <li><span class="glyphicon glyphicon-ok"></span> ${extendedProfile.volunteeringOther}
                                </li>
                            </c:if>
                        </ul>
                    </c:if>
                </fieldset>
            </div>  
        </div>

    </div>
</div>
</div>