<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div class="body-content wide-content column" id="onlineDataTransfer">
    <div class="page-form rava-start" id="rava-start-box">
        <div class="hd">
            <div class="hd-inner">
                <h2 class="title">${title}</h2>
            </div>
        </div>

        <div class="bd">
            <c:choose>
                <c:when test="${not empty run}">
                    <div class="alert alert-block" style="color: #b46b00; font-weight: bold;">
                        Delivery has been run.
                    </div>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${resultSize eq 0}">
                            <div class="alert alert-block" style="color: #b46b00; font-weight: bold;">
                                <c:out value="There are currently no pending voter registrations records to be downloaded." />
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="table shadow rnd4 table-striped">
                                <tr>
                                    <th>Configuration</th>
                                    <th>Number of records</th>
                                </tr>

                                <c:forEach var="entry" items="${resultsMap}">
                                    <tr>
                                        <td>${entry.key.facePrefix}</td>
                                        <td>${entry.value}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <br />

                            <div class="nav nav-pills">
                                <form action="<c:url value="/onlinedatatransfer/RunDelivery.htm"/>" method="post" >
                                    <input type="submit" value="Run Deivery">
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
</div>

