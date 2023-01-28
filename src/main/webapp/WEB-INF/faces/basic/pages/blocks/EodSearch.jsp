<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div id="eod-search-down" class="wide-content column-form">
	<div class="hd">
		<h1>Election Official Directory - Search Results</h1>
	</div>
	<div class="bd">
		<c:choose>
			<c:when test="${not empty found}">
				<h3>Search Results</h3>
				<dl class="search-results">
                    <c:forEach items="${found}" var="leo">
						<dt class="title">
						  <a href="<c:url value="/eod.htm"><c:param name="regionId" value="${leo.region.id}"/><c:param name="stateId" value="${leo.region.state.id}"/><c:param name="submission" value="true"/></c:url>">${leo.region.name}, ${leo.region.state.abbr}</a>
						</dt>
						<dd>
						    <p class="search-snippet">
							    <c:if test="${not empty leo.physical.addressTo}">${leo.physical.addressTo}, </c:if>
							    <c:if test="${not empty leo.physical.street1}">${leo.physical.street1}, </c:if>
							    <c:if test="${not empty leo.physical.street1}">${leo.physical.street2}, </c:if>
						    	${leo.physical.city}, ${leo.physical.state} ${leo.physical.zip}
							</p>
						</dd>
				    </c:forEach>
			    </dl>
                <c:if test="${paging.pagingInfo.actualRows gt paging.pagingInfo.maxResults}">
                    <p>Not all results were returned (too many results were found).</p>
                    <p>Suggestions:</p>
                    <ul><li>Try adding words or more specific terms</li></ul>
                </c:if>
			</c:when>
			<c:otherwise>
				<p>No results found for "<c:out value="${searchTerms}"/>".</p>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="ft" />
 
</div>