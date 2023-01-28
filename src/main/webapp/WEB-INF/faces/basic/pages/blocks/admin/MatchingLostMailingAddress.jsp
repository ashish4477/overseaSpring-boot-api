<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column-form faces list">
    <div class="hd">
        <h2>Matching Lost Mailing Address</h2>
    </div>
    <div class="bd">
        <p>Found lost addresses: ${addressCount}</p>
        <c:if test="${not empty facesOfLostAddress}">
            <table>
                <tr>
                    <th> Face </th>
                    <th> States </th>
                    <th> # </th>
                    <th> Link to </th>
                    <th>  </th>
                </tr>
                <c:choose>
                    <c:when test="${stateByState}">
                        <c:forEach items="${facesOfLostAddress}" var="faceEntry">
                            <c:forEach items="${faceEntry.value}" var="stateEntry">
                            <form action="<c:url value="/admin/MatchLostMailingAddress.htm"/>" method="post">
                                <tr>
                                    <td>${faceEntry.key} <input type="hidden" name="faceName" value="${faceEntry.key}"></td>
                                    <td>${stateEntry.key} <input type="hidden" name="stateName" value="${stateEntry.key}"></td>
                                    <td>${stateEntry.value}</td>
                                    <td>
                                        <select name="mailingListId">
                                            <option value="0">Select mailing list</option>
                                            <c:forEach items="${mailingLists}" var="mailingList">
                                                <option value="${mailingList.id}">${mailingList.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="runMatching" value="Run matching">
                                        <input type="hidden" name="byState" value="true">
                                    </td>
                                </tr>
                            </form>
                            </c:forEach>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${facesOfLostAddress}" var="faceEntry">
                            <form action="<c:url value="/admin/MatchLostMailingAddress.htm"/>" method="post">
                                <tr>
                                    <td>${faceEntry.key} <input type="hidden" name="faceName" value="${faceEntry.key}"></td>
                                    <c:set var="addressNumber" value="0"/>
                                    <td>
                                        <c:forEach items="${faceEntry.value}" var="stateEntry">
                                            ${stateEntry.key} <c:set var="addressNumber" value="${addressNumber + stateEntry.value}"/>
                                        </c:forEach>
                                    </td>
                                    <td>${addressNumber}</td>
                                    <td>

                                        <select name="mailingListId">
                                            <option value="0">Select mailing list</option>
                                            <c:forEach items="${mailingLists}" var="mailingList">
                                                <option value="${mailingList.id}">${mailingList.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="runMatching" value="Run matching">
                                        <input type="submit" name="byState" value="Go to by state matching">
                                    </td>
                                </tr>
                            </form>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
             </table>
        </c:if>
        <c:if test="${stateByState}">
            <a class="btn" href='<c:url value="/admin/MatchLostMailingAddress.htm"/>'>Back to Matching by Faces</a><br/>
        </c:if>
        <a class="btn" href='<c:url value="/admin/MailingLists.htm"/>'>Back to Mailing Lists</a>
    </div>
</div>