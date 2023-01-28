<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 18, 2007
  Time: 7:36:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
  <div class="hd">
    <h2>Edit Voter Instruction</h2>
  </div>
  <div class="bd">
    <form:form action="EditInstruction.htm" commandName="filling" method="post" id="mainForm">
      <form:hidden path="id"/>
      <input type="hidden" name="submission" value="true"/>

      <table>
        <colgroup><col width="40%"><col width="60%"></colgroup>
        <spring:hasBindErrors name="filling">
          <tr>
            <td colspan="2"><span style="color:red">${errors.globalError.defaultMessage}</span> </td>
          </tr>
        </spring:hasBindErrors>
        <tr class="region-name">
          <th>
            Name
            <p style="font-size:smaller;">This is a name that used only for administration.</p>
          </th>
          <td>
            <form:errors path="name" cssStyle="color: :red"/>
            <form:input path="name"/>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Position in PDF
            <p style="font-size:smaller;">
              Position should be defined in form &lt;page&gt;_&lt;question group&gt;_&lt;question&gt;_&lt;position&gt;.
              The position should be defined in terms of resulting PDF form.
              For example, 1_2_a_2 means page 1, group &quot;MY INFORMATION&quot;,
              question &quot;TYPED OR PRINTED NAME&quot;, position 2 &quot;first name&quot;.
            </p>
          </th>
          <td>
            <form:input path="inPdfName"/>
          </td>
        </tr>

        <tr class="region-name">
          <th colspan="2">
            Instruction
          </th>
        </tr>

        <tr class="region-name">
          <td colspan="2">
            <form:errors path="text" cssStyle="color: :red"/>
            <form:hidden path="text" id="fillingText"/>
            <div id="editor" style="width: 100%;"></div>
          </td>
        </tr>

        <c:if test="${filling.id ne 0}">
          <tr class="region-name">
            <th>
              Dependencies
            </th>
            <td>
              <c:choose>
                <c:when test="${fn:length(filling.keys) == 0}">
                  <a href="<c:url value="/admin/EditDependencyGroup.htm"><c:param name="dependentId" value="${filling.id}"/></c:url>">Add Dependencies</a>
                </c:when>
                <c:otherwise>
                  <c:set var="dependencyGroup" value=""/>
                  <c:set var="ulBody" value=""/>
                  <ul type="disc">
                    <c:forEach items="${filling.keys}" var="key" varStatus="keyIndx">
                      <c:if test="${dependencyGroup ne key.dependsOnName}"> <%-- group starts --%>
                        <c:choose>
                          <c:when test="${key.dependsOnName eq 'Face' }"> <%-- It's kind of trick: for FaceDependency 'dependsOnName' is always 'Face' --%>
                            <c:url var="editUrl" value="/admin/EditDependencyGroup.htm"><c:param name="type" value="FACE"/><c:param name="dependentId" value="${filling.id}"/></c:url>
                          </c:when>
                          <c:when test="${key.dependsOnName eq 'Flow' }"> <%-- It's kind of trick: for FlowDependency 'dependsOnName' is always 'Flow' --%>
                            <c:url var="editUrl" value="/admin/EditDependencyGroup.htm"><c:param name="type" value="FLOW"/><c:param name="dependentId" value="${filling.id}"/></c:url>
                          </c:when>
                          <c:when test="${fn:startsWith(key.dependsOnName, 'User field ')}"> <%-- It's kind of trick: for UserFieldDependency 'dependsOnName' always starts from 'User Field' --%>
                            <c:url var="editUrl" value="/admin/EditDependencyGroup.htm"><c:param name="type" value="USER"/><c:param name="dependentId" value="${filling.id}"/><c:param name="fieldName" value="${key.dependsOn.name}"/></c:url>
                          </c:when>
                          <c:otherwise>
                            <c:url var="editUrl" value="/admin/EditDependencyGroup.htm"><c:param name="type" value="QUESTION"/><c:param name="dependentId" value="${filling.id}"/><c:param name="dependsOn" value="${key.dependsOn.id}"/></c:url>
                          </c:otherwise>
                        </c:choose>
                        <c:if test="${not keyIndx.first}"><c:set var="ulBody" value="${ulBody}</a></li>"/></c:if>
                        <c:set var="ulBody" value="${ulBody}<li><a href=&quot;${editUrl}&quot;>${key.dependsOnName} : ${key.conditionName}"/>
                      </c:if>
                      <c:if test="${dependencyGroup eq key.dependsOnName}"><c:set var="ulBody" value="${ulBody}, ${key.conditionName}"/></c:if> <%-- group continues--%>
                      <c:if test="${dependencyGroup ne key.dependsOnName}"><c:set var="dependencyGroup" value="${key.dependsOnName}"/></c:if>
                      <c:if test="${keyIndx.last}"><c:set var="ulBody" value="${ulBody}</a></li>"/></c:if><%-- all ends --%>
                    </c:forEach>
                      ${ulBody}
                  </ul>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <c:if test="${fn:length(filling.keys) != 0}">
            <tr>
              <th></th>
              <td>
                <a href="<c:url value="/admin/EditDependencyGroup.htm"><c:param name="dependentId" value="${filling.id}"/></c:url>">Add Dependencies</a>
              </td>
            </tr>
          </c:if>
        </c:if>
        <td colspan="2">
          <a href="<c:url value="/admin/InstructionsList.htm"/>">Go back to Voter Instruction List</a>&nbsp;
          <input type="submit" name="save" value="save"/>&nbsp;
          <c:if test="${filling.id ne 0}">
            <input type="submit" name="delete" value="delete"/>
          </c:if>
        </td>
      </table>
    </form:form>
  </div>
  <div class="ft"></div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.2.8/ace.js" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript" language="JavaScript">
  var editor = ace.edit("editor");
  //editor.setTheme("ace/theme/monokai");
  editor.getSession().setMode("ace/mode/html");
  editor.getSession().setMode("ace/mode/velocity");
  editor.setAutoScrollEditorIntoView(true);
  editor.setOption("maxLines", 20);
  editor.setOption("minLines", 5);
  editor.getSession().setUseWrapMode(true);
  editor.setTheme("ace/theme/eclipse");
  editor.clearSelection(1);

  $(document).ready( function() {
    editor.setValue( $("#fillingText").val() );

    $("#mainForm").submit( function() {
      $("#fillingText").val( editor.getValue() );
    });
  });

</script>
