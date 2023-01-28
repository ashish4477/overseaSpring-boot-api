<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	pageContext.setAttribute("newLineChar", "\n");
%>
<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/admin.css"/>" />

<div id="eod-voting-laws" class="column-form">
    <div class="hd">
   		<h2>State Voting Laws and Regulations</h2>
      <div class="admin-page-nav">
      <form:form action="${actionUrl}" method="get">
             <select onchange="this.form.submit()" name="stateId" id="gotoState">
                <option>Change State</option>
            <c:forEach items="${states}" var="state">
                <option value="${state.id}">${state.name}</option>
            </c:forEach>
            </select>
        </form:form>
         <button type="button"><a href="<c:url value="/admin/EodStates.htm"/>">EOD States List</a></button>
      </div>
   	</div>
    <div class="bd">
        <h3>${votingLaws.state.name}</h3>
        <form:form commandName="votingLaws" method="post">
            <table>
                <tr>
                    <td>Early Voting:</td>
                    <td><form:checkbox path="earlyInPersonVoting" value="true"/></td>
                </tr>
                <tr>
                    <td>No Excuse Absentee Voting:</td>
                    <td><form:checkbox path="noExcuseAbsenteeVoting" value="true"/></td>
                </tr>
                <tr>
                    <td>Same Day Voter Registration:</td>
                    <td><form:select path="sameDayRegistration" items="${sameDayRegistrations}"/></td>
                </tr>
                <tr>
                    <td>Mail Voting:</td>
                    <td>
                        <form:select path="allMailVoting" items="${mailVotingTypes}">
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td>Voter ID Regulations:</td>
                    <td>
                        <form:select path="voterId" items="${voterIdTypes}">
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td>Additional text to Voter ID Regulations:</td>
                    <td><form:input path="voterIdAdditional"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input class="pull-right button" type="submit" value="Save">&nbsp;
                    </td>
                </tr>
            </table>
        </form:form>
    </div>

    <div class="ft"></div>
</div>