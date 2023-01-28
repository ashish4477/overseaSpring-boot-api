<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<div id="footer-menu" class="inner">
	&copy; <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%> Overseas Vote. All rights reserved. U.S. VOTE FOUNDATION is a trade name and trademark of <a href="https://www.overseasvotefoundation.org" target="_blank">Overseas Vote</a>.
</div>
<div class="footer-links">
	<a href="<c:url value='/home.htm' />">Home</a> |
    <!--<a href="<c:url value='TermsOfUse.htm'/>/">Terms of Use</a> |-->
    <a href="<c:url value='TermsOfUse.htm'/>">Terms of Use</a> |
    <!--<a href="<c:url value='PrivacyPolicy.htm'/>">Privacy &amp; Security Policy</a> |-->
	<a href="/privacy-policy">Privacy &amp; Security Policy</a>
</div>
