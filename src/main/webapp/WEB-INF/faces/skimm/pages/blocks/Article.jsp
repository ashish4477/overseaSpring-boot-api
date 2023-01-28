<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div id="eod-form" class="voter-account-page wide-content column-form">
<div class="row">
  <div class="col-xs-12 col-sm-9">
    <h1 class="title">In The News</h1>
  </div>
  <div class="col-xs-12 col-sm-3" style="text-align:center;">
    <p>Provided by FactCheck.org</p>
    <img src="<c:url value="/img/factcheck-logo.jpg"/>"/>
  </div>
</div>
    <div class="col-xs-12">
        <h3>${article.title}</h3>
        <p>
            Published at <fmt:formatDate value="${article.publishedAt}" pattern="MMMM dd, yyyy hh:mm a" />
            by ${article.creator}
        </p>
        ${article.content}
        <p> &nbsp;</p>
        <p><a href="${article.link}" target="_blank"><button class="open">Read source</button></a></p>
    </div>
</div>
