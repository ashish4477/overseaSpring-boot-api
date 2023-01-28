<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<c:set var="LF" value="\n"/>

<div class="candidate-details">
	<h1 class="title">${title}</h1>


	<div class="column-right">
		<c:if test="${not empty cb.photo}">
			<div class='candidate-photo'>
				<img src="${cb.photo}" />
			</div>
		</c:if>
	</div>

	<div class="column-left">
		<h3>
			<c:out value="${cb.office.title}" />
			<c:out value="${cb.firstName}" />
			<c:out value="${cb.middleName}" />
			<c:if test="${not empty cb.nickName}">(<c:out
					value="${cb.nickName}" />)</c:if>
			<c:out value="${cb.lastName}" />
			<c:out value="${cb.suffix}" />
			<c:if test="${not empty cb.homeState}">(<c:out
					value="${cb.homeState}" />)</c:if>
		</h3>
		<p>
			<c:forEach items="${cb.office.elections}" var="election"
				varStatus="inx">
				<b>Office Seeking:</b> ${election.office}<br />
				<b>Party:</b> ${election.parties}<br />
			</c:forEach>
			<c:if test="${not empty cb.office.name}">
				<b>Current Office:</b>
				<c:out value="${cb.office.name}" />
				<br />
				<b>Current District:</b>
				<c:out value="${cb.office.district}" />
				<br />
				<b>First Elected:</b>
				<c:out value="${cb.office.firstElect}" />
				<br />
				<b>Last Elected:</b>
				<c:out value="${cb.office.lastElect}" />
				<br />
				<b>Next Election:</b>
				<c:out value="${cb.office.nextElect}" />
				<br />
			</c:if>
		</p>

		<h2>Background Information</h2>
		<p>
			<b>Gender:</b>
			<c:out value="${cb.gender}" />
			<br /> <b>Family:</b> ${fn:replace(fn:escapeXml(cb.family), ';',
			'<br/>')}<br /> <b>Birth Date:</b>
			<c:out value="${cb.birthDate}" />
			<br /> <b>Birthplace:</b>
			<c:out value="${cb.birthPlace}" />
			<br /> <b>Home City:</b>
			<c:out value="${cb.homeCity}" />
			,
			<c:out value="${cb.homeState}" />
			<br /> <b>Religion:</b>
			<c:out value="${cb.religion}" />
			<br />
		</p>

		<h2>Education</h2>
		<p>${fn:replace(fn:escapeXml(cb.education),newLineChar,"<br />")}</p>

		<h2>Professional Experience</h2>
		<p>${fn:replace(fn:escapeXml(cb.profession),newLineChar,"<br />")}</p>

		<h2>Political Experience</h2>
		<p>${fn:replace(fn:escapeXml(cb.political),newLineChar,"<br />")}</p>

		<h2>Organizations</h2>
		<p>${fn:replace(fn:escapeXml(cb.orgMembership),newLineChar,"<br
			/>")}</p>

		<h2>Caucuses/Non-Legislative Committees</h2>
		<p>${fn:replace(fn:escapeXml(cb.congMembership),newLineChar,"<br
			/>")}</p>

		<h2>Committees</h2>
		<p>
			<c:forEach items="${cb.office.committees}" var="cmt" varStatus="inx">
${cmt.committeeName}<br />
			</c:forEach>
		</p>

		<h2>Additional information</h2>
		<c:forEach items="${cab.additionalItems}" var="item" varStatus="inx">
			<p>
				<strong>${item.name}:</strong><br /> ${item.data}
			</p>
		</c:forEach>
	</div>
  <br/><br/>
    <a href="<c:url value="/CandidateFinder.htm" />" class="wizard-button back">Back</a>

	<div class="break"></div>
	<p style="font-size: 80%; font-style: italic; text-align: center; margin-top: 20px;">
		Data Source: Project Vote Smart
	</p>
</div>

