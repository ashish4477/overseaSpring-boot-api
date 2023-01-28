<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<div id="eod-form" class="wide-content <c:if test="${not (isSvid and isEod)}">svid </c:if>column-form ">
	<div class="hd">
		<h1 class="title">
		<c:if test="${isEod}">Election Official</c:if>
		<c:if test="${isEod and isSvid}"> &amp; </c:if>
		<c:if test="${isSvid}">State Voter Information</c:if>
		 Directory</h1>
	</div>
	<div class="bd">
        <p>The OVF Election Official Directory is the most comprehensive and up-to-date resource for U.S. election office contact information available today.</p>
		<c:if test="${isEod}">
			<c:if test="${isEod and isSvid}"><p class="list-head">The Election Official Directory:</p></c:if>
			<ul>
				<li>Find and contact your local election official</li>
				<li>Look-up election office addresses, telephone, fax, email and websites</li>
			</ul>
		</c:if>
		<c:if test="${isSvid}">
			<c:if test="${isEod and isSvid}"><p class="list-head">The State Voter Information Directory:</p></c:if>
			<ul>
				<li>State-by-state filing deadlines</li>
				<li>Options for how to send and receive voting materials</li>

			</ul>
		</c:if>
		<c:import url="/WEB-INF/faces/basic/pages/statics/EodLocalSelectForm.jsp">
			<c:param name="showForm" value="true" />
		</c:import>
	</div>

	<div class="ft"></div>
	</div>
	
	