<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<c:set var="onlySvid" value="${isSvid and (not isEod)}"/>

<div id="eod-form" class="wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form ">
  <div class="row">
    <div class="col-xs-12 <c:if test="${not isSvid}">col-md-8</c:if> align-left">
      <c:choose>
        <c:when test="${onlySvid}">
          <div class="block-page-title-block">
            <h1 class="title">State Voting Requirements &amp; Information</h1>
          </div>
        </c:when>
        <c:otherwise>
          <div class="block-page-title-block">
            <h1 class="title">Local Election Office Contact Info</h1>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="col-xs-12 align-left">
      <ul class="svid-list-block">
        <c:choose>
          <c:when test="${isEod}">
          </c:when>
          <c:otherwise>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </div>
  <div class="row">
    <div class="col-xs-12 col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3">
      <c:import url="/WEB-INF/${relativePath}/pages/statics/EodLocalSelectForm.jsp">
        <c:param name="showForm" value="true" />
      </c:import>
    </div>
    <div class="hidden-sm hidden-md hidden-lg col-xs-12">&nbsp;</div>
  </div>
