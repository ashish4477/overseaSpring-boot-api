<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<div class="container footer">  <%-- Express Your Vote style="width:auto;" --%>
	<div class="nav-collapse">
		<ul class="nav"><%-- Express Your Vote style="padding-top:19px;" --%>
      <li><a href="http://www.elections.ny.gov" target="_blank">New York State Board of Elections</a></li>
			<li class="divider-vertical"></li>
			<li><a href="<c:url value='TermsOfUse.htm'/>">Terms of Use</a></li>
			<li class="divider-vertical"></li>
			<li><a href="<c:url value='PrivacyPolicy.htm'/>">Privacy
					&amp; Security Policy</a></li>
			<li class="divider-vertical"></li>
			<li><a href="https://www.overseasvotefoundation.org"
				target="_blank">Site Provided By Overseas Vote</a></li>   <%-- Express Your Vote --%>
		</ul>
    <%-- Express Your Vote
    <div class="express-your-vote-banner" style="float:right; margin-top:-6px">
          <a href="<c:url value="/ExpressYourVote.htm"/>">
              <img src="<c:url value="/img/buttons/express-your-vote-button.png"/>" alt="Express Your Vote">
          </a>
        </div>
     --%>
	</div>
</div>