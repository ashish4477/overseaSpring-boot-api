<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Nov 23, 2007
  Time: 6:08:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column-form">
    <div class="hd">
        <h2>Overall Statistics</h2>
    </div>
    <div class="bd">
        <h3>Users totals</h3>
        <table>
            <tr>
                <td>Count of all users</td>
                <td>${usersCount}</td>
            </tr>
            <tr>
                <td>Count of real users <br/>
                    <span style="font-size:smaller;">users who intentionally registered</span>
                </td>
                <td>${realUsers}</td>
            </tr>
        </table>

        <form action="#" method="get">
            <table>
                <tr>
                    <th colspan="4">Select date range ( format: mm/dd/yyyy )  </th>
                </tr>
                <tr>
                    <th>Start date: </th>
                    <td>
                        <spring:bind path="statisticsForm.startRange">
                            <input type="text" name="${status.expression}" value="${status.value}"/>
                        </spring:bind>
                    </td>
                    <th>End date: </th>
                    <td>
                        <spring:bind path="statisticsForm.endRange">
                            <input type="text" name="${status.expression}" value="${status.value}"/>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td colspan="4"><input type="submit" value="Submit"/> </td>
                </tr>
            </table>
        </form>


        <h3>Form downloads</h3>
        <table>
            <tr>
                <th>Form type</th>
                <th>Number of downloads</th>
                <th>Number of users</th>
            </tr>
            <c:forEach items="${pdfStats}" var="pdfEntry">
                <tr>
                    <td>${fn:toUpperCase(pdfEntry[2])}</td>
                    <td>${pdfEntry[0]}</td>
                    <td>${pdfEntry[1]}</td>
                </tr>
            </c:forEach>
        </table>

        <h3>By State statistics</h3>
        <table>
            <tr>
                <th>State</th>
                <th>Count of users</th>
            </tr>
            <c:forEach items="${usersByState}" var="stateEntry">
                <tr>
                    <c:set var="state" value="${stateEntry.key}"/>
                    <c:set var="count" value="${stateEntry.value}"/>
                    <td>${state.name}</td>
                    <td>${count}</td>
                </tr>
            </c:forEach>
        </table>

    </div>
</div>