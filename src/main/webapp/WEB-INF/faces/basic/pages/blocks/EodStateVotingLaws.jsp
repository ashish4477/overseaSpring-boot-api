<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style type="text/css">
    .tableFloatingHeaderOriginal th {
        background-color: #fff;
        border-bottom: 1px solid #DDD;
    }
</style>

<%--<div id="eod-form" class="wide-content column-form ">
    <div class="hd">

    </div>--%>
<div class="col-xs-12">
    <h1 class="title">Voting Methods and Options</h1>
    <div class="row">
        <table class="table table-bordered table-condensed table-hover election-dates voting-laws">
            <thead>
            <tr class="table-type-headings">
                <th width="20%">State</th>
                <c:if test="${stateVoterInformationList.size() > 0}">
                    <c:forEach items="${stateVoterInformationList.get(0).votingMethods}" var="stateVotingMethod" varStatus="indx2">
                        <th align="center" class="text-center"><c:out value="${stateVotingMethod.votingMethodType.name}"/></th>
                    </c:forEach>
                </c:if>
            </tr>
            </thead>
            <tbody>
                <c:forEach items="${stateVoterInformationList}" var="stateVoterInformation" varStatus="indx">
                    <tr>
                        <td><h3>${stateVoterInformation.state.name}</h3></td>
                        <c:forEach items="${stateVoterInformation.votingMethods}" var="stateVotingMethod" varStatus="indx2">
                            <td align="center">
                              <c:if test="${stateVotingMethod.allowed}"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span></c:if>
                              <c:if test="${not empty stateVotingMethod.additionalInfo}">
                                <span class="glyphicon glyphicon-info-sign" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="${stateVotingMethod.additionalInfo}"></span>
                              </c:if>
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
             </tbody>
        </table>
       <p>&nbsp;</p>
       <!--
       <h3>Term Definitions</h3>
       <table  class="chart table table-striped table-hover">
            <tbody>
            <tr>
                <td><strong>Early In-Person Voting:</strong></td>
                <td>allows voters to visit an election official's office or other satellite voting location an cast a vote in person if the voter is unable to vote on Election Day</td>
            </tr>
            <tr>
                <td><strong>No Excuse Absentee Voting:</strong></td>
                <td>allows any registered voter to request an absentee ballot with requiring that that voter state a reason for voting absentee</td>
            </tr>
            <tr>
                <td><strong>Mail Voting:</strong></td>
                <td>when an election is conducted by mail, a ballot is automatically mailed in advance of Election Day; only 2 sates have all elections by mail</td>
            </tr>
            <tr>
                <td><strong>Strict Photo ID:</strong></td>
                <td>A voter cannot cast a valid ballot without first presenting a photo ID (9 states)</td>
            </tr>
            <tr>
                <td><strong>Non-Strict Photo ID:</strong></td>
                <td>A voter must first present a photo ID, but if the voter does not have one, they can vote a provisional ballot but must later present ID (8 states)</td>
            </tr>
            <tr>
                <td><strong>Strict Non-Photo ID:</strong></td>
                <td>A voter must first present some form of ID (it must not have a photo) or vote a provisional ballot that will only be counted if the voter presents ID later</td>
            </tr>
            <tr>
                <td><strong>Non-Strict, Non-Photo ID:</strong></td>
                <td>A voter must present some form of ID (it must not have a photo)</td>
            </tr>
            <tr>
                <td><strong>Same Day Voter Registration:</strong></td>
                <td>A qualified resident of the state can go to the polls on Election Day and register and vote at the same time (8 states)</td>
            </tr>
            </tbody>
        </table>
        -->
    </div>
</div>
<script src="https://unpkg.com/sticky-table-headers"></script>
<script>
$('table').stickyTableHeaders();
  $( function() {
    $('[data-toggle="tooltip"]').tooltip()
  })
</script>





