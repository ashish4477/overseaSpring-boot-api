<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="column-form">
  <div class="hd">
    <h2>Edit Field Type</h2>
  </div>
  <div class="bd">
    <form:form commandName="ovfProperty" action="EditProperty.htm" method="post" name="ovfProperty">
      <form:hidden path="propertyName"/>
      <table>
        <tr class="region-name">
          <th>
            ${ovfProperty.propertyName.groupName}
          </th>
          <th>
              ${ovfProperty.propertyName.propertyName}
          </th>
          </tr>
        <tr>
          <td colspan="2">
            <form:input path="propertyValue" size="100%"/>
            <form:errors path="propertyValue" cssStyle="color:red;"/>
          </td>
        </tr>
        <tr>
        <td colspan="2">
          <input type="submit" value="save"/>
          &nbsp;
          <a href="<c:url value="/admin/PropertiesList.htm"/>"><button  value="cancel">cancel</button></a>
        </td>
        </tr>
      </table>
    </form:form>
  </div>
  <div class="ft"></div>
</div>

