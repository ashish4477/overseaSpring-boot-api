<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 18, 2013
	Time: 4:09:39 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="voter-account-page" class="voter-account-page page-form body-content content column wide-content extended-profile">
     <div class="row">
      <div class="col-xs-12">
        <h2>Election Officials Serving ${leo.eodRegion.name}, ${selectedState.name}</h2>
      </div>
 </div>
 <c:if test="${not empty leo.officers}">
            <div class="row officers">
                <div class="col-xs-12">
                    <div class="row" >
                        <c:forEach items="${leo.officers}" var="officer">
                            <div class="col-xs-12 col-sm-6">
                                <h3>${officer.firstName} ${officer.lastName}</h3>
                                    <em>${officer.officeName}</em><br/>
                                    <c:if test="${not empty officer.email}"><a href="mailto:${officer.email}">${officer.email}</a></c:if>
                                    <br/>
                                    <c:if test="${not empty leo.leoPhone}">Phone: ${leo.leoPhone}
                                </c:if>
                              <p>&nbsp;</p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${empty leo.officers}">
          <div class="row" >
            <div class="col-xs-12">
            <p>There are currently no election officials for your voting region, please check your address</p>
            </div>
          </div>
       </c:if>
  <section class="extended-profile">
  <div class="section title">
    <h3>More About This Location</h3>
  </div>

<div class="col-xs-12">
  <h3>${leo.eodRegion.name}, ${selectedState.name} <a style="font-size:12px; font-weight:normal;" href="<c:url value="/eod.htm"><c:param name="submission" value="true"/><c:param name="stateId">${selectedState.id}</c:param><c:param name="regionId">${user.eodRegionId}</c:param> </c:url> ">(View More Details)</a></h3>
  <br/>
	<div class="col-xs-6">
    <dl>
      <dt><h4>Mailing Address</h4></dt>
      <dd class="content">
        Local Election Offices may have multiple addresses.
        <br/>
         <a class="button small" href="<c:url value="/eod.htm"><c:param name="submission" value="true"/><c:param name="stateId">${selectedState.id}</c:param><c:param name="regionId">${user.eodRegionId}</c:param> </c:url> ">Check Yours Here</a>
      </dd>
   <dt>
 </dl>
	</div>
  	<div class="col-xs-6">
      <c:if test="${not empty leo.generalEmail}">
          <dl>
            <dt><h4>Election Office Email Address</h4></dt>
            <dd class="content">${leo.generalEmail}</dd>
          </dl>
      </c:if>
      <dl>
        <dt><h4>Office Hours</h4></dt>
        <dd class="content">
        <c:choose><c:when test="${not empty leo.hours}"><c:out value='${leo.hours}'/></c:when><c:otherwise>none on record</c:otherwise></c:choose>
        </dd>
      </dl>
	</div>
  <div class="col-xs-12">
    <dl>
      <dt><h4>Further Instructions</h4></dt>
        <dd class="content"><c:choose><c:when test="${not empty leo.furtherInstruction}">${fn:replace(fn:escapeXml(leo.furtherInstruction),newLineChar,"<br />")}</c:when><c:otherwise>none on record</c:otherwise></c:choose>
        </dd>
    </dl>
    <c:forEach items="${svid.validLookupTools}" var="lookupTool" varStatus="indx">
      <dl>
        <dt><h4>${lookupTool.name}</h4></dt>
        <dd class="content">
          <a target="_blank" href="<c:out value='${lookupTool.url}' />"><c:out value='${lookupTool.url}' /></a>
        </dd>
      </dl>
    </c:forEach>
  </div>
</div>
</section>

</div>