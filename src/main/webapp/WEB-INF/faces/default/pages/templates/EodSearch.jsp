<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Oct 29, 2009
  Time: 9:16:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
  <body>
    <c:forEach items="${found}" var="leo">
        <a href="<c:url value="/eod.htm"><c:param name="regionId" value="${leo.region.id}"/><c:param name="stateId" value="${leo.region.state.id}"/><c:param name="submission" value="true"/></c:url>">${leo.region.name}, ${leo.region.state.abbr}</a><br/>
    </c:forEach>
  </body>
</html>