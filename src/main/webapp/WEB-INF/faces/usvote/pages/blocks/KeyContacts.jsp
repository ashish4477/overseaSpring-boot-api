<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 18, 2013
	Time: 4:09:39 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="voter-account-page"
     class="voter-account-page page-form body-content content column wide-content extended-profile">
    <section class="extended-profile">
        <div class="col-xs-12">
            <c:forEach items="${svid.validLookupTools}" var="lookupTool" varStatus="indx">
                <dl>
                    <dt><h4>${lookupTool.name}</h4></dt>
                    <dd class="content">
                        <a target="_blank" href="<c:out value='${lookupTool.url}' />"><c:out
                                value='${lookupTool.url}'/></a>
                    </dd>
                </dl>
            </c:forEach>
        </div>
    </section>
</div>