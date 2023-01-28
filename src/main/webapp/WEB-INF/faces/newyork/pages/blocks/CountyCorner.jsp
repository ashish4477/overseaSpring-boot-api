


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div id="countyInformation" class="wide-content county column-form">
    <div class="hd">
        <h1 class="title">County Information</h1>
        <%--<div class="last-updated pull-right">Updated&nbsp;<b><fmt:formatDate value="${leo.updated}" pattern="MM/dd/yyyy" /></b></div>--%>
    </div>
    <div class="bd">
        <c:if test="${not empty leo}">
            <h3>${leo.region.name}, ${leo.region.state.name}</h3>
            <c:choose>
                <c:when test="${not empty leo.mailing}">
                    <address>
                        <strong><c:out value='${leo.mailing.addressTo}'/></strong><br>
                        <c:out value='${leo.mailing.street1}'/><br/>
                        <c:if test="${not empty leo.mailing.street2}"><c:out value='${leo.mailing.street2}'/><br/></c:if>
                        <c:out value='${leo.mailing.city}, ${leo.mailing.state}'/>
                        <c:if test="${not empty leo.mailing.zip}">
                            <c:out value='${leo.mailing.zip}'/><c:if test="${not empty leo.mailing.zip4}">-<c:out value='${leo.mailing.zip4}'/></c:if></c:if>
                      </address>
                      <address>


                        <strong>Phone:</strong> <c:choose><c:when test="${not empty leo.leoPhone}"><c:out value='${leo.leoPhone}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                        <br/>
                        <strong>Fax:</strong> <c:choose><c:when test="${not empty leo.leoFax}"><c:out value='${leo.leoFax}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                        <br/>
                        <strong>Website:</strong> <c:choose><c:when test="${not empty leo.website}"><a href="<c:out value='${leo.website}'/>" target="_blank"><c:out value='${leo.website}'/></a></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                        <br/>
                        <strong>Email Address:</strong> <c:choose><c:when test="${not empty leo.generalEmail}"><a href="mailto:<c:out value='${leo.generalEmail}'/>"><c:out value='${leo.generalEmail}'/></a></c:when><c:otherwise>none on record</c:otherwise></c:choose>
                        <br/>
                    </address>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose></c:if>
        <c:if test="${not empty leo.officers}">
            <div class="row officers">
                <h4>OFFICERS</h4>
                <div class="well span7">
                    <div class="row" >
                        <c:forEach items="${leo.officers}" var="officer">
                            <div class="span3">
                                <p><strong>${officer.firstName} ${officer.lastName}</strong><br/>
                                    <em>${officer.officeName}</em><br/>
                                    <c:if test="${not empty officer.email}"><a href="mailto:${officer.email}">${officer.email}</a></c:if> </p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>
 <h4>Additional Information</h4>
      <div style="padding:8px 0"><b>Office Hours:</b> <c:choose><c:when test="${not empty leo.hours}"><c:out value='${leo.hours}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
      </div>
      <h5>Further instructions</h5>
			<c:choose><c:when test="${not empty leo.furtherInstruction}">${fn:replace(fn:escapeXml(leo.furtherInstruction),newLineChar,"<br />")}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
      <br/><br/>
       <h5>Am I Registered?</h5>
			 <c:choose><c:when test="${not empty svid.confirmationOrStatus}"><a target="_blank" href="<c:out value='${svid.confirmationOrStatus}'/>"><c:out value='${svid.confirmationOrStatus}'/></a></c:when><c:otherwise>none on record</c:otherwise></c:choose>

    <div class="bd select-form-bd">
			<c:import url="/WEB-INF/faces/basic/pages/statics/EodSelectForm.jsp" />
		</div>
        <div class="ft"></div>
    </div>
</div>
