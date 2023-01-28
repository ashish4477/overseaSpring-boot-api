<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
  <div class="hd">
    <h2>Edit Mailing List</h2>
  </div>
  <div class="bd">
    <form:form action="EditMailingList.htm" method="post" commandName="mailingList" id="mainForm">
      <c:if test="${mailingList.id ne 0}">
        <form:hidden path="id"/>
      </c:if>
      <table width="100%" cellpadding="2" cellspacing="4">
        <tr class="region-name">
          <th width="20%">
            Name<sup>*</sup>
          </th>
          <td>
            <form:input path="name"/>
            <form:errors path="name"/>
          </td>
        </tr>
          <%--<tr class="region-name">
              <th>
                  GetResponse API Key<sup>*</sup>
              </th>
              <td>
                  <form:input path="apiKey"/>
                  <form:errors path="apiKey"/>
              </td>
          </tr>
          <tr class="region-name">
              <th>
                  GetResponse Campaign ID<sup>*</sup>
              </th>
              <td>
                  <c:choose>
                      <c:when test="${not empty campaignNames}">
                          <form:select path="campaignId">
                              <form:options items="${campaignNames}" />
                          </form:select>
                          &lt;%&ndash;<form:errors path="campaignId"/>&ndash;%&gt;
                      </c:when>
                      <c:otherwise>
                          Campaigns not found. Enter API Key first.
                      </c:otherwise>
                  </c:choose>
              </td>
          </tr>--%>
        <tr class="region-name">
          <th>
            Questionnaire Field Type
          </th>
          <td>
            <form:errors path="fieldType"/>
            <form:select path="fieldType">
              <option value="0">Select field type...</option>
              <form:options items="${fieldTypes}" itemLabel="name" itemValue="id"/>
            </form:select>
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
            Footnote
          </th>
          <td>&nbsp;</td>
        </tr>
        <tr class="region-name">
          <td colspan="2">
            <form:errors path="signature" style="color:red"/>
            <form:hidden path="signature"/>
            <div id="editor" style="width: 100%;"></div>
          </td>
        </tr>
        <tr>
          <th>Faces</th>
          <td>
            <c:forEach items="${faces}" var="face">
              <input type="checkbox" name="faceId" value="${face.id}" id="faceId_${face.id}" <c:if test="${fn:contains(selectedFaces, face)}">checked="checked" </c:if> >
              <label for="faceId_${face.id}">${face.name}</label><br/>
            </c:forEach>
          </td>
        </tr>
        <tr>
          <th>Instruction</th>
          <td>
            User's email will be added to the Mailing List when user checks field with
            selected Field Type. List above contains only so-called "Mailing Signup" types.
            Such type has key word "Mail-in" in its name.<br/>
            It's recommended to <a class="btn" href="<c:url value="/admin/EditFieldType.htm?id=0&type=mail-in"/>">create a new Field Type</a>
            for each Mailing List and then add question field
            of that Field Type in a flow.<br/>
            <em>From address</em> and <em>Repy To address</em> will be used in emails to subscribers.
            These addresses could be overriden if corresponding fields are set in Mail Template.<br/>
            <em>Signature</em> will be added to Mail template body. HTML and Velocity script are allowed here.

          </td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" name="save" value="save"/>&nbsp;
          </td>
        </tr>
      </table>
    </form:form>
    <a class="btn" href="<c:url value="/admin/CreateMailingTask.htm"><c:param name="listId" value="${mailingList.id}"/></c:url>">Create new <strong>Mailing Task</strong></a>

  </div>
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
    editor.setValue( $("#signature").val() );

    $("#mainForm").submit( function() {
      $("#signature").val( editor.getValue() );
    });
  });

</script>
