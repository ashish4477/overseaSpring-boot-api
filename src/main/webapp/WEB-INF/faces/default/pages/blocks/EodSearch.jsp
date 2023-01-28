<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="body-content">
	<div class="page-form">
		<div class="hd">
			<div class="hd-inner">
				<h1 class="title">Search Election Official Directory</h1>
			</div>
		</div>
		<div class="bd">
			<div class="bd-inner">
				<%--<div>
					<c:import url="/WEB-INF/faces/default/pages/statics/SearchForm.jsp" />
				</div>
				 --%>
				<c:choose>
					<c:when test="${not empty found}">
						<h2>Search Results</h2>
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
					</c:when>
					<c:otherwise>
						<p>No results found for "<c:out value="${searchTerms}"/>".</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="ft">
			<div class="ft-inner">
			</div>
		</div>
	</div>
 
</div>