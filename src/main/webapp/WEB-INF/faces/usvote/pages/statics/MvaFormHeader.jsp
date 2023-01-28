<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="<c:url value="/js/select2.full.min.js"/>"></script>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/select2.css"/>">
<script src="<c:url value="/js/load-regions.js"/>" type="text/javascript"></script>
<c:url value="/ajax/getRegionsHTMLSelect.htm" var="getRegionsURL"/>
<c:url value="/ajax/getJsonRegions2.htm" var="getJsonRegionUrl"/>
<script type="text/javascript" language="JavaScript">
  //<!--
  var params = { 'selectNameId': 'eodRegionId', 'regionInputId':'eodRegionId', 'stateInputId':'votingAddress\\.state', 'url':'${getJsonRegionUrl}' };
  $(document).ready( function() {
    var element = getJsonRegions2( params );
    $("#votingAddress\\.state").change(function() {
      element.val(null).trigger("change");
    });
  });

  //-->
</script>
