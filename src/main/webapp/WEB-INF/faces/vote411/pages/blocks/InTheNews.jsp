<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div id="eod-form" class="voter-account-page wide-content column-form">
<div class="row">
  <div class="col-12 col-sm-9">
    <h1 class="title">In The News <small>Featured Articles</small></h1>
  </div>
  <div class="col-12 col-sm-3" style="text-align:center;">
    <p>Provided by FactCheck.org</p>
    <img src="<c:url value="/img/factcheck-logo.jpg"/>"/>
  </div>
</div>
    <div class="col-12">
        <form action="<c:url value="InTheNews.htm"/>" method="post" class="form-inline">
        <div class="form-group">
          <label class="sr-only">Filtered By:</label>
        </div>
        <div class="row well">
          <div class="col-sm-5">
            <select name="issue" id="issues" class="form-control">
                <option value="">Issue Type</option>
                <c:forEach items="${issues}" var="issue">
                    <c:set var="selected" value=""/>
                      <c:choose>
                        <c:when test="${issue.label ne ''}">
                          <c:if test="${issue.label eq param.issue}"><c:set var="selected" value="selected='selected'"/></c:if>
                          <option value="${fn:escapeXml(issue.label)}" ${selected}>${issue.label}</option>
                        </c:when>
                        <c:otherwise>
                          <c:if test="${issue.name eq param.issue}"><c:set var="selected" value="selected='selected'"/></c:if>
      -                    <option value="${fn:escapeXml(issue.name)}" ${selected}>${issue.name}</option>
                          </c:otherwise>
                      </c:choose>
                </c:forEach>
              </select>
            </div>
            <div class="col-sm-3">
              <select name="location" id="locations" class="form-control">
                  <option value="">Location</option>
                  <c:forEach items="${locations}" var="location">
                      <c:set var="selected" value=""/>
                      <c:if test="${location.name eq param.location}"><c:set var="selected" value="selected='selected'"/></c:if>
                      <option value="${fn:escapeXml(location.name)}" ${selected}>${location.name}</option>
                  </c:forEach>
                  <optgroup label=""></optgroup>
              </select>
             </div>
             <div class="col-sm-2">
              <select name="year" id="years" class="form-control">
                  <option value="">All Years</option>
                  <c:forEach begin="0" end="${currentYear-2011}" var="yearN">
                      <c:set var="selected" value=""/> <c:set var="year" value="${currentYear-yearN}"/>
                      <c:if test="${year eq param.year}"><c:set var="selected" value="selected='selected'"/></c:if>
                      <option value="${year}" ${selected}>${year}</option>
                  </c:forEach>
                  <optgroup label=""></optgroup>
              </select>
             </div>
             <input type="submit" value="Go" class="pull-right" style="margin:0;">
          </div>
        </form>
    </div>
<div class="row">
  <div class="col-12">
    <c:if test="${not empty stateLocation and not empty stateArticles}">
      <div class="section title">
        <h3>${stateLocation.name}</h3>
        </div>
        <c:forEach items="${stateArticles}" var="article">
            <div>
                <h3>${article.title}</h3>
                <p class="byline"><fmt:formatDate value="${article.publishedAt}" pattern="MMMM dd, yyyy hh:mm a" /></p>
                <p>${article.description}</p>
                <p><a href="<c:url value="/Article.htm"><c:param name="id" value="${article.id}"/></c:url>"><button class="open">Read more...</button></a> </p>
            </div>
            <div class="clearfix"></div>
        </c:forEach>
    </c:if>
    <c:if test="${not empty typeLocation and not empty typeArticles}">
        <div class="section title">
          <h3>${typeLocation.name}</h2>
        </div>
        <c:forEach items="${typeArticles}" var="article">
            <div>
                <h3>${article.title}</h3>
                <p class="byline"><fmt:formatDate value="${article.publishedAt}" pattern="MMMM dd, yyyy hh:mm a" /></p>
                <p>${article.description}</p>
                <p><a href="<c:url value="/Article.htm"><c:param name="id" value="${article.id}"/></c:url>"><button class="open">Read more...</button></a> </p>
            </div>
            <div class="clearfix"></div>
        </c:forEach>
    </c:if>
    <c:if test="${not empty searchArticles}">
        <h2>Search result</h2>
        <c:forEach items="${searchArticles}" var="article">
            <div>
                <h3>${article.title}</h3>
                <p class="byline"><fmt:formatDate value="${article.publishedAt}" pattern="MMMM dd, yyyy hh:mm a" /></p>
                <p>${article.description}</p>
                <p><a href="<c:url value="/Article.htm"><c:param name="id" value="${article.id}"/></c:url>"><button class="open">Read more...</button></a> </p>
                <div class="clearfix"></div>
            </div>
        </c:forEach>
        <div>
            <c:if test="${not empty meta.previous}">
                <form action="<c:url value="/InTheNews.htm"/>" method="post" name="prevGo">
                    <input type="hidden" name="uri" value="${meta.previous}">
                    <a href="#" onclick="document.prevGo.submit(); return false;">&lt;&lt; Previous</a>
                </form>
            </c:if>
            &nbsp;
            <c:if test="${not empty meta.next}">
                <form action="<c:url value="/InTheNews.htm"/>" method="post" name="nextGo">
                    <input type="hidden" name="uri" value="${meta.next}">
                    <a href="#" onclick="document.nextGo.submit(); return false;">Next &gt;&gt;</a>
                </form>
            </c:if>
        </div>
    </c:if>
    <c:if test="${empty stateArticles and empty typeArticles and empty searchArticles}">
        <h2>Nothing found</h2>
    </c:if>

   <p>&nbsp;</p>
</div>

</div>
  </div>