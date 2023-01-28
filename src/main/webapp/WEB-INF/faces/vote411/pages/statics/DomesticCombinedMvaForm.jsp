<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script src="<c:url value="/js/load-regions.js"/>" type="text/javascript"></script>
<c:url value="/ajax/getRegionsHTMLSelect.htm" var="getRegionsURL"/>
<script type="text/javascript" language="JavaScript">
    //<!--
    var params = { 'selectNameId': 'eodRegionId', 'regionInputId':'eodRegionId', 'stateInputId':'votingAddress.state', 'url':'${getRegionsURL}' };
    YAHOO.util.Event.addListener(window, "load", loadRegions, params);
    YAHOO.util.Event.addListener("votingAddress.state", "change", loadRegions, params);
    //-->
</script>

<c:set var="faction" value=""/>
<c:if test="${not empty param.action}">
    <c:set var="faction" value="${param.action}"/>
</c:if>
<c:set var="fname" value="userForm"/>
<c:if test="${not empty param.name}">
    <c:set var="fname" value="${param.name}"/>
</c:if>
<c:set var="fid" value="userForm"/>
<c:if test="${not empty param.id}">
    <c:set var="fid" value="${param.id}"/>
</c:if>
<c:set var="modelAttr" value="user"/>
<c:if test="${not empty param.modelAttribute}">
    <c:set var="modelAttr" value="${param.modelAttribute}"/>
</c:if>
<c:set var="showPasswordFields" value="true"/>
<c:if test="${not empty param.showPasswordFields}">
    <c:set var="showPasswordFields" value="${param.showPasswordFields}"/>
</c:if>

<form:form modelAttribute="${modelAttr}" action="${faction}" method="post" name="${fname}" id="${fid}" class="domestic userFields">

<c:import url="/WEB-INF/${relativePath}/pages/statics/MvaPersonalDetails.jsp" />
  <div class="row">
    <div class="col-12 col-sm-6 ">
      <h3>Home Address</h3><small>The address you use for voting purposes</small>
      <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaVotingAddress.jsp" />
  </div>
  <div class="col-12 col-sm-6">
  <c:if test="${not empty user.voterType}">
        <h3>Current Address</h3><br/>
      <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaCurrentAddress.jsp" /> 
  </c:if>
      <h3> Forwarding Address</h3>
      <c:import url="/WEB-INF/${relativePath}/pages/statics/MvaForwardingAddress.jsp" />
 </div>
 </div>
  <div class="row">
  <div class="col-12">
    <input type="submit" name="create" value="Save" class="submit-button pull-right" />
    <c:if test="${param.showBackButton}">
        <a class="button back" href="<c:url value="/Login.htm"/>">Back</a>
    </c:if>
  </div>
</div>
</form:form>

<div class="modal fade error-message">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">x</span></button>
                    <h4 class="modal-title text-center">Some of the information you entered is incomplete or inaccurate. <br/>Please review the error message(s) below.</h4>
                </div>
                <div class="modal-body text-center">
                    <a class="button" data-dismiss="modal">Continue</a>
                </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>
    $(function() {
    <spring:bind path="${modelAttr}">
      <c:if test="${status.errors.errorCount > 0}">

        $('.error-message').modal('show');
      </c:if>
    </spring:bind>
    });
</script>
