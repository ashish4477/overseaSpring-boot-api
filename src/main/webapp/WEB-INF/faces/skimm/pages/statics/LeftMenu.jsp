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
<ul>
    <li id="left-menu-rava"><a href="<c:url value='/w/rava.htm'><c:param name='fields' value='3'/><c:param name='3' value='${param.cStateId}'/><c:param name='vrState' value='${param.cStateAbbr}'/></c:url>">Register to Vote</a></li>
    <li id="left-menu-fwab"><a href="<c:url value='/FwabStart.htm'><c:param name='fields' value='3'/><c:param name='3' value='${param.cStateId}'/><c:param name='vrState' value='${param.cStateAbbr}'/></c:url>">Federal Write-in <br />Absentee Ballot</a></li>
    <li id="left-menu-vhd"><a class="vhd ceebox2" rel="iframe width:800 height:450" href="https://vhd.overseasvotefoundation.org/index.php?/ovf/Knowledgebase/List">Voter Help Desk</a></li>
    <li id="left-menu-eod"><a href="<c:url value='/election-official-directory/${param.cStateAbbr}'/>">Election Official Directory</a></li>
    <li id="left-menu-svid"><a href="<c:url value='/state-voting-information/${param.cStateAbbr}'/>">State Voter<br />Information Directory</a></li>
    <li id="left-menu-account"><a href="<c:url value='/Login.htm'/>">My Voter Account</a></li>
    <li id="left-menu-back"><a href="">Go back to the<br />STATE_NAME Secretary<br />of State's Website</a></li>
    <li id="provided-by-ovf">Provided by<br /><a href="http://www.overseasvotefoundation.org">Overseas Vote</a></li>    
</ul>
<div id="left-menu-footer"></div>
