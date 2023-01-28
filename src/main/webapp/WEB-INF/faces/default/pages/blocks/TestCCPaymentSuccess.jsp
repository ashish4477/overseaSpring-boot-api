<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: May 20, 2008
  Time: 8:10:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div>
    <div class="hd">
        <h2>Test &quot;Authorize.net&quot;</h2>
    </div>
    <div class="bd">
        <c:forEach items="${authResponse}" var="authItem" varStatus="indx">
            ${indx.index}. ${authItem} <br/>
        </c:forEach>
    </div>
    <div class="bd">
        <a href="<c:url value="/test/TestPayment.htm"/>">Test it again.</a>
    </div>
</div>