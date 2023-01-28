<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Nov 12, 2007
  Time: 9:40:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form faces config">
  <div class="hd">
    <h2>Edit Faces Configuration</h2>
    <a class="btn" href="<c:url value='/admin/FacesConfigsList.htm'/>">Go Back to the List</a>
  </div>
  <div class="bd">
    <table>
      <form:form action="EditFaceConfig.htm" commandName="faceConfig" method="post" name="QuestionForm">
        <input type="hidden" value="${faceConfig.id}" name="configId"/>
        <tr class="region-name">
          <th>
            URL and Context Path:
          </th>
          <td>
            <form:input path="urlPath" cssClass="text"/>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Internal Path:
          </th>
          <td>
            <form:input path="relativePrefix" cssClass="text"/>
          </td>
        </tr>
        <tr class="region-name">
          <th>

          </th>
          <td>
            <spring:bind path="faceConfig.defaultPath">
              <c:if test="${not status.value}" >
                <input class="checkbox" type="checkbox" name="${status.expression}" value="true"/> Set This Config as Default
                <p>&nbsp;</p>
              </c:if>
              <c:if test="${status.value}">
                This is Default Config.
                <p>&nbsp;</p>
              </c:if>
            </spring:bind>
          </td>
        </tr>
        <tr class="region-name">
          <th></th>
          <td>
            <form:checkbox path="useEnvelope" cssClass="checkbox"/>
            <form:label path="useEnvelope">Generate Security and Mail Envelopes</form:label>
          </td>
        </tr>

        <tr class="region-name">
          <th></th>
          <td>
            <form:checkbox path="useFaxPage" cssClass="checkbox"/>
            <form:label path="useFaxPage">Generate Fax Page</form:label>
          </td>
        </tr>
        <tr class="region-name">
          <th></th>
          <td>
            <form:checkbox path="useNotarizationPage" cssClass="checkbox"/>
            <form:label path="useNotarizationPage">Generate Notarization Page</form:label>
          </td>
        </tr>
        <tr class="region-name">
          <th></th>
          <td>
            <form:checkbox path="useBlankAddendumPage" cssClass="checkbox"/>
            <form:label path="useBlankAddendumPage">Generate Blank Addendum Page</form:label>
          </td>
        </tr>
        <tr class="region-name">
          <th></th>
          <td>
            <form:checkbox path="useCaptcha" cssClass="checkbox"/>
            <form:label path="useCaptcha">Require CAPTCHA on EOD/SSVID for anonymous users</form:label>
          </td>
        </tr>
        <tr class="region-name">
          <th></th>
          <td>
            <form:checkbox path="loginAllowed" cssClass="checkbox"/>
            <form:label path="loginAllowed">Anonymous users will be redirected to Login page within the Flow Wizard</form:label>
          </td>
        </tr>
        <tr class="region-name">
          <th></th>
          <td>
            <form:checkbox path="autoCreateAccount" cssClass="checkbox"/>
            <form:label path="autoCreateAccount">Auto Create Account during the Flow Wizard</form:label>
          </td>
        </tr>


        <tr class="region-name">
          <th>
            Path to external content:
          </th>
          <td>
            <form:input path="externalContentUrl" cssClass="text"/>
            <div class="hint"> (Example: /yourPageName.htm)</div>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            URL to Drupal site:
          </th>
          <td>
            <form:input path="drupalUrl" cssClass="text"/>
            <div class="hint"> (Example: https://siteName.overseasvotefoundation.org)</div>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Fields to skip on create/edit user validation:
          </th>
          <td>
            <form:input path="userValidationSkipFields" cssClass="text"/>
            <div class="hint"> (if the face does not contain "Voter History" field add "voterHistory" here )</div>
          </td>
        </tr>
        <c:if test="${faceConfig.id gt 0}">
          <tr class="region-name">
            <th>Flow Instructions:</th>
            <td>
              <c:forEach items="${faceInstructions}" var="instruction">
                <a href="<c:url value='/admin/EditFaceFlowInstruction.htm'><c:param name="id" value="${instruction.id}"/></c:url>">${instruction.flowTypeName}</a>
              </c:forEach>
              <c:if test="${addingAvailable}">
                <a href="<c:url value="/admin/EditFaceFlowInstruction.htm"><c:param value="${faceConfig.id}" name="configId"/></c:url>">Create a New Instruction</a>
              </c:if>
              <p>&nbsp;</p>
            </td>
          </tr>
          <tr>
            <th>Attached to mailing List</th>
            <td>
              <select name="mailingListId">
                <option value="0">Select Mailing List...</option>
                <c:forEach items="${mailingLists}" var="mailingList">
                  <option value="${mailingList.id}" <c:if test="${not empty faceMailingList and faceMailingList.mailingList.id eq mailingList.id}">selected="selected"</c:if> >${mailingList.name}</option>
                </c:forEach>
              </select>
            </td>
          </tr>
        </c:if>
        <tr>
          <td>&nbsp;</td>
          <td>
            <input class="submit" type="submit" name="save" value="Save Configuration"/>
            <br/>
          </td>
        </tr>
      </form:form>

      <c:if test="${faceConfig.id gt 0}">
        <tr class="region-name">
          <th>Face Logo:</th>
          <td>
            <c:choose>
              <c:when test="${not empty logo}">
                <img src="<c:url value="/faceLogo/getLogo.do"><c:param value="${faceConfig.id}" name="configId"/></c:url>" alt="face logo"><br/><br/>
                <form action="<c:url value="/admin/FaceLogoUpload.htm"/>" method="post" enctype="multipart/form-data">
                  <input type="hidden" value="${faceConfig.id}" name="configId"/>
                  <input type="file" name="faceLogo"><br />
                  <input class="upload" type="submit" name="Upload" value="Replace Face Logo"><br/><br/>
                </form>

              </c:when>
              <c:otherwise>
                <form action="<c:url value="/admin/FaceLogoUpload.htm"/>" method="post" enctype="multipart/form-data">
                  <input type="hidden" value="${faceConfig.id}" name="configId"/>
                  <input type="file" name="faceLogo"> <br/>
                  <input class="upload" type="submit" name="Upload" value="Upload Face Logo"><br/><br/>
                </form>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:if>
    </table>
  </div>
  <div class="ft"></div>
</div>

