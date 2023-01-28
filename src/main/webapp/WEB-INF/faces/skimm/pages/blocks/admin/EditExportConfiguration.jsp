<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <h2>Edit Registration Export Configuration</h2>
    </div>
    <div class="bd">
        <form:form action="EditExportConfiguration.htm" commandName="exportCofiguration">
            <input type="hidden" value="${exportCofiguration.id}" name="id"/>

            <table>
                <tr>
                    <th>Select Faces to use with this Configuration. </th>
                    <td>
 <%--                       <form:select path="faceConfigs" multiple="true">
                            <form:options items="${faceConfigs}" itemValue="id" itemLabel="name"/>
                        </form:select>
 --%>
                        <c:choose>
                            <c:when test="${exportCofiguration.id eq 0}">
                                <form:checkboxes path="faceConfigs" items="${faceConfigs}"  itemValue="id" itemLabel="name" delimiter="<br/>"/>
                            </c:when>
                            <c:otherwise>
                                <form:checkboxes path="faceConfigs" items="${faceConfigs}"  itemValue="id" itemLabel="name" delimiter="<br/>" disabled="true"/>
                            </c:otherwise>
                        </c:choose>
                        <form:errors path="faceConfigs" cssStyle="color:red;"/>
                    </td>
                </tr>
                <tr>
                    <th>Server Address</th>
                    <td>
                        <form:input path="serverAddress"/>
                    </td>
                </tr>
                <tr>
                    <th>Server Port</th>
                    <td>
                        <form:input path="serverPort" placeholder="(Usually 22)"/>
                    </td>
                </tr>
                <tr>
                    <th>Directory on the Server</th>
                    <td>
                        <form:input path="sftpDir"/>
                    </td>
                </tr>
                <tr>
                    <th>SFTP User Name</th>
                    <td>
                        <form:input path="sftpUserName"/>
                    </td>
                </tr>
                <tr>
                    <th>SFTP User Password</th>
                    <td>
                        <form:input path="sftpPassword" placeholder="(not used with Private Key)"/>
                    </td>
                </tr>
                <tr>
                    <th>Path to Private Key File</th>
                    <td>
                        <form:input path="sftpPrivateKey" placeholder="file:/home/vfaftp/.ssh/vfa_rsa"/>
                    </td>
                </tr>
                <tr>
                    <th>ZIP file encryption password</th>
                    <td>
                        <form:input path="zipPassword"/>
                    </td>
                </tr>
               <tr>
                    <th>Delivery Schedule</th>
                    <td>
                        <form:radiobuttons items="${deliverySchedules}" path="deliverySchedule" delimiter=" "/>
                    </td>
                </tr>
                <%--<tr>
                    <th>Export Answer Level</th>
                    <td>
                        <form:radiobuttons items="${exportLevels}" path="exportAnswersLevel" delimiter=" "/>
                    </td>
                </tr>--%>
                <tr>
                    <td colspan="2" align="right">
                        <form:checkbox path="enabled"/> Configuration Enabled
                    </td>
                </tr>


                <tr>
                    <td colspan="2">
                        <a href="<c:url value="/admin/ExportConfigurationsList.htm"/>">Go back to Configuration list</a>&nbsp;
                        <input type="submit" name="save" value="save" style="float:right;"/>&nbsp;
                    </td>
                </tr>
            </table>
        </form:form>
        <p>&nbsp;</p>
    </div>
    <div class="ft"></div>
</div>
