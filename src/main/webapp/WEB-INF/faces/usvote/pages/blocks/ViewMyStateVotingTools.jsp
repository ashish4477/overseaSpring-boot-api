<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column">
    <div class="col-xs-12 col-sm-10 right-side sharing-voting">
        <h2>My State's Voting Tools</h2>
        <br>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <c:forEach items="${svid.validLookupTools}" var="lookupTool" varStatus="indx">
                <div class="panel panel-default">
                    <div class="panel-heading svid collapsed"role="tab">
                        <h4 class="panel-title">
                            <a role="button" target="_blank" href="<c:out value='${lookupTool.url}' />">${lookupTool.name}</a>
                        </h4>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>