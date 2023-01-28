<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<input type="submit" src="<c:url value="/img/buttons/continue-button.gif"/>" name="create" value="Continue" class="submit-button pull-right" />
<c:if test="${param.showBackButton}">
    <a class="back-button" href="<c:url value="/Login.htm"/>"><img src="<c:url value="/img/buttons/back-button.gif"/>" alt="back"></a>
</c:if>
<div class="break"></div>

<script type="text/javascript" language="JavaScript">
    //<!--
    
 if (document.cookie.length > 0){
    	function getCookie(c_name)
    	{
    	var i,x,y,ARRcookies=document.cookie.split(";");
    	for (i=0;i<ARRcookies.length;i++)
    	{
    	  x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
    	  y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
    	  x=x.replace(/^\s+|\s+$/g,"");
    	  if (x==c_name)
    	    {
    	    return unescape(y);
    	    }
    	  }
    	}
    	var voterType = getCookie('voterType');
    	var voterHistory = getCookie('voterHistory');
    	if (voterType){
        	document.getElementById('voterType').selectedIndex=voterType;
    	}
    	if (voterHistory){
    	document.getElementById('voterHistory').selectedIndex=voterHistory;
    	}
    	}
 //-->
 </script>