<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="voter-account-page" class="voter-account-page page-form body-content content column wide-content extended-profile">
  <div class="row">
    <div class="col-xs-12">
      <h2>Election Officials Serving ${leo.eodRegion.name}, ${selectedState.name}</h1>
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
</div>
