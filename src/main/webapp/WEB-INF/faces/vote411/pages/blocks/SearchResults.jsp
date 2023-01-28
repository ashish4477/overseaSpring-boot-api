<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div class="body-content">
    <div class="page-content">
        <div class="hd">
            <div class="hd-inner">
                <h2 class="title">Search Results</h2>
            </div>
        </div>

        <div class="bd">
            <div class="bd-inner">

                <div><div id="search-form-container">
                    <c:if test="${not empty eodResults && fn:length(eodResults) gt 0}">
                        <h2>Results from Election Official Directory:</h2>
                        <dl class="search-results">
                            <c:forEach items="${eodResults}" var="leo">
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
                    </c:if>
                    <c:if test="${not empty siteResults}">
                        <h2>Results from OVF Site Content:</h2>
                        <dl class="search-results">
                            <c:forEach items="${siteResults}" var="item">
                                <dt class="title">
                                    <a href="${item.link}">${item.title}</a>
                                </dt>
                                <dd>
                                    <p class="search-snippet">
                                            <c:if test="${not empty item.description.value and item.description.value ne 'NULL'}">${item.description.value}</c:if>
                                    </p>
                                </dd>
                            </c:forEach>
                        </dl>
                    </c:if>
                    <c:if test="${not empty vhdResults}">
                        <h2>Results from Voter Help Desk:</h2>
                        <dl class="search-results">
                            <c:forEach items="${vhdResults}" var="item">
                                <dt class="title">
                                    <a href="${item.link}">${item.title}</a>
                                </dt>
                                <dd>
                                    <p class="search-snippet">
                                            <c:if test="${not empty item.description.value and item.description.value ne 'NULL'}">${item.description.value}</c:if>
                                    </p>
                                </dd>
                            </c:forEach>
                        </dl>
                    </c:if>
                    <c:if test="${(empty eodResults or fn:length(eodResults) eq 0 ) && empty siteResults && empty vhdResults }">
                        <h2>There were no matches for your search, please try again.</h2>
                        <h4>Suggestions:</h4>
                        <ul>
                            <li>Check the spelling of the words</li>
                            <li>Try different words or more general terms</li>
                        </ul>
                    </c:if>
                </div>
                </div>
            </div>
        </div>

        <div class="ft"><div class="ft-inner"></div></div>
    </div>
</div>