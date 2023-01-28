<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 9, 2008
  Time: 4:29:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Overseas Vote | ${title}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" media="all" href="css/reset-fonts.css" />
    <link rel="stylesheet" type="text/css" media="all" href="css/eyv-main.css" />

    
</head>
<body id="fedex-label-page"<c:if test="${imageLabel}"> onload="window.print();"</c:if>>
    <c:import url="${content}"/>
</body>
</html>