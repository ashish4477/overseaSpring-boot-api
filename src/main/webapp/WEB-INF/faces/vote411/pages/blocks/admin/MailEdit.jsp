<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 30, 2008
  Time: 9:19:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
  <div class="hd">
    <h2>Edit Mail Template</h2>
  </div>
  <div class="bd">
    <c:url value="/admin/EditMailTemplate.htm" var="formAction"/>
    <form:form commandName="mailTemplate" action="${formAction}" method="post" name="QuestionForm" id="mainForm">
      <input type="hidden" name="submission" value="true"/>
      <form:hidden path="id"/>
      <table cellspacing="4" cellpadding="2" width="100%">
        <tr class="region-name">
          <th width="20%">
            Template Name<sup>*</sup>
          </th>
          <td class="region-name">
            <form:errors path="name" style="color:red"/>
            <form:input path="name" />
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Email Subject<sup>*</sup>
          </th>
          <td>
            <form:errors path="subject" style="color:red"/>
            <form:input path="subject" />
          </td>
        </tr>
        <tr class="region-name">
          <th>
            From address
          </th>
          <td>
            <form:errors path="from" style="color:red"/>
            <form:input path="from" type="text"/>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Reply To Address
          </th>
          <td>
            <form:errors path="replyTo" style="color:red"/>
            <form:input path="replyTo" type="email"/>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Email Body
          </th>
          <td>&nbsp;</td>
        </tr>
        <tr class="region-name">
          <td colspan="2">
            <form:errors path="bodyTemplate" style="color:red"/>
            <form:hidden path="bodyTemplate"/>
            <div id="editor" style="width: 100%;"></div>
          </td>
        </tr>
        <tr class="region-name">
        <td colspan="3">
          <input type="submit" name="save" value="Save"/>&nbsp;&nbsp;
        </td>
        </tr>
        <tr class="region-name" style="border: dashed gray 1px;">
          <th>Send to email</th>
          <td><input type="email" name="sendToEmail" value="${user.username}"> <br/>
            <input type="submit" name="test" value="Test this template">&nbsp;&nbsp;
          </td>
        </tr>
      </table>
    </form:form>
  </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.2.8/ace.js" type="text/javascript" charset="utf-8"></script>
<script  type="text/javascript" language="JavaScript">
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
    editor.setValue( $("#bodyTemplate").val() );

    $("#mainForm").submit( function() {
      $("#bodyTemplate").val( editor.getValue() );
    });
  });

</script>
