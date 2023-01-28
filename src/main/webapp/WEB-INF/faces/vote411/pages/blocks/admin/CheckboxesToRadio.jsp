<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="column-form">
    <div class="hd">
        <h2>Edit Question Variant : Checkboxes to Radio Button Conversion</h2>
    </div>
    <div class="bd">
        <form:form commandName="selectedVariant" action="CheckboxesToRadio.htm" method="post">
            <input type="hidden" name="variantId" value="${selectedVariant.id}"/>

            <c:choose>
                <c:when test="${fn:length(fieldsToTransform) gt 1}">
                    <table>
                        <tr class="region-name">
                            <th>
                                Questions to transform
                            </th>
                            <th>
                                Variable Name
                            </th>
                        </tr>
                        <c:forEach items="${fieldsToTransform}" var="field">
                            <tr>
                                <td>${field.order}. ${field.title}</td>
                                <td>${field.inPdfName}</td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>Variable name:</td><td><strong>${variablePrefix}</strong></td>
                        </tr>
                    </table>
                    <input type="submit" value="Do transform" name="save">
                    <input type="button" value="Go back" onclick="window.location='<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${selectedVariant.id}"/></c:url>';return false;" >

                </c:when>
                <c:otherwise>
                    Questions to transform were not found.<br/>
                    <input type="button" value="Go back to Question Variant" onclick="window.location='<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${selectedVariant.id}"/></c:url>';return false;" >
                </c:otherwise>
            </c:choose>

        </form:form>
    </div>
    <div class="ft"></div>
</div>