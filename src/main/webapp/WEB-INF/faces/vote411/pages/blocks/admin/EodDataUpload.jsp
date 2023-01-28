<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 22, 2007
  Time: 9:07:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<div id="eod-admin-page" class="column-form">
    <div class="hd">
        <h2>Election Official Directory - Admin Area</h2>
    </div>
    <div class="bd">
        <c:if test="${success}" >
            <p>The OVF Election Official Directory has been updated for state <strong>${processedState.name}</strong>. 
            ${numberOfLeos} records have been created or updated. </p>
        </c:if>
        <c:if test="${wrongVersion}">
            <p class="error">The file is of old and incompatible version. Please, download file from the server again.</p>
        </c:if>
        <c:if test="${error}">
            <p class="error">No records have been created or updated. All errors have been written in log file.</p>
        </c:if>

        <form name="excel-file-upload" action="<c:url value="/admin/EodDataUpload.htm" />" method="post" enctype="multipart/form-data" >
            <fieldset class="state-upload">
                <label>
                    <span>Choose a state:</span>
                    <select class="field"  name="stateId">
                        <option>State...</option>
                        <c:forEach items="${states}" var="state">
                            <c:choose>
                                <c:when test="${state.id eq param.stateId}">
                                    <option value="${state.id}" selected="selected">${state.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${state.id}">${state.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span>Select a file:</span>
                    <input type="file" name="leosFile"  class="field"/>
                </label>
            </fieldset>
            <fieldset id="continue">
                <input type="image" src="<c:url value="/img/buttons/continue-button.gif"/>" />
            </fieldset>
        </form>
    </div>
    <div class="ft">
    </div>
</div>
