<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 28, 2007
  Time: 6:19:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<div id="sidebar">
    <ul>
        <li class="rava-link item"><a href="<c:url value='/w/rava.htm'/>">Voter Registration /<br/>Absentee Ballot Request</a></li>
        <li class="vhd-link item"><a class="vhd ceebox2" rel="iframe width:800 height:450" href="https://vhd.overseasvotefoundation.org/index.php?/exxonmobilcat/Knowledgebase/List">Voter Help Desk</a></li>
        <li class="eod-link item"><a href="<c:url value='/eod.htm'/>">Election Official Directory</a></li>
        <li class="account-link item"><a href="<c:url value='/Login.htm'/>">My Voter Account</a></li>
        <li class="svid-link item"><a href="<c:url value='/svid.htm'/>">State Voter <br /><span>Information Directory</span></a></li>
 		<li class="fwab-link item"><a href="<c:url value='/FwabStart.htm'/>">Federal Write-in<br /><span>Absentee Ballot</span></a></li>
 		<li id="exxon-site-link"><a href="http://www.exxonmobilcat.com/">Return to the<br />ExxonMobil Citizen Action<br />Team Website</a></li>
    </ul>
</div>
