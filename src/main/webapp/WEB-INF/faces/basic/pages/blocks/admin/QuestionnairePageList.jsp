<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column">
	<div class="hd">
		<h2>Questionnaire Pages</h2>
	</div>
	<div class="bd">
		<table class="questionary-pages-list">
			<c:forEach items="${pages}" var="page">
				<tr>
                    <td>${page.number}</td>
					<td><a href="<c:url value="/admin/EditQuestionnairePage.htm"><c:param name="id" value="${page.id}"/></c:url>">
						${page.title}
					</a></td>
                    <td>${page.type}</td>
				</tr>
			</c:forEach>	
		</table>
        <form action="<c:url value="/admin/EditQuestionnairePage.htm"/>" method="get">
            <input type="submit" value="Create"/>
            page for
            <select name="type">
                <c:forEach items="${formTypes}" var="typeName">
                    <option value="${typeName}">${typeName}</option>
                </c:forEach>
            </select>
        </form>
	</div>
	<div class="ft"></div>
</div>