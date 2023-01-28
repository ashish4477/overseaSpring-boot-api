<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column-form faces list">
	<div class="hd">
		<h2>Questionnaire Field Types</h2>
	</div>
	<div class="bd">
		<table>
		    <c:forEach items="${fieldTypes}" var="fieldType">
		        <tr>
		            <td>
		                <a href="<c:url value="/admin/EditFieldType.htm"><c:param name="id" value="${fieldType.id}"/></c:url>">
		                    ${fieldType.name}
		                </a>
		            </td>
		        </tr>
		    </c:forEach>
		</table>
        <a class="btn" href="<c:url value="/admin/EditFieldType.htm?id=0&type=mail-in"/>">Create a new Field Type (mail-in checkbox)</a>
	</div>
	<div class="ft"></div>
</div>