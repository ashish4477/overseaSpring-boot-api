<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
  <div class="hd">
    <h2>Create Mailing Task</h2>
  </div>
  <div class="bd">
    <form:form action="CreateMailingTask.htm" method="post" commandName="task">
      <c:if test="${task.id ne 0}">
        <form:hidden path="id"/>
      </c:if>
      <table width="100%" cellpadding="2" cellspacing="4">
        <tr class="region-name">
          <th width="20%">
            Mailing List<sup>*</sup>
          </th>
          <td>
            <c:choose>
              <c:when test="${task.id ne 0}">
                ${task.mailingList.name}
                <form:hidden path="mailingList"/>
              </c:when>
              <c:otherwise>
                <form:select path="mailingList" items="${campaigns}" itemLabel="name" itemValue="id"/>
                <form:errors path="mailingList"/>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Template<sup>*</sup>
          </th>
          <td>
            <c:choose>
              <c:when test="${task.id ne 0}">${task.template.name}<form:hidden path="template"/></c:when>
              <c:otherwise>
                <form:select path="template" items="${templates}" itemLabel="name" itemValue="id"/>
                <form:errors path="template"/>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Start on date<sup>*</sup>
          </th>
          <td>
            <form:input path="startOn" id="datepicker"/>
            <form:errors path="startOn"/>
          </td>
        </tr>
        <tr class="region-name">
          <th>
            Subject
          </th>
          <td>
            <form:input path="subject"/>
            <form:errors path="subject"/>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <c:choose>
              <c:when test="${task.id ne 0}"><input type="submit" name="save" value="Restart the task"/></c:when>
              <c:otherwise><input type="submit" name="save" value="Create the task"/></c:otherwise>
            </c:choose>
            &nbsp;
          </td>
        </tr>
      </table>
    </form:form>

  </div>
</div>

<script type="text/javascript" language="JavaScript">
  $( function() {
    $( "#datepicker" ).datepicker();
  } );
</script>
