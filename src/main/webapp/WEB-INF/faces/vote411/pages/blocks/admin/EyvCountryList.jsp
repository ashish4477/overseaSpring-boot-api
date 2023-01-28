<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 20, 2008
  Time: 6:12:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<div id="eod-admin-page" class="column-form">
	<div class="hd">
		<h2>Express Your Vote : List of countries</h2>
	</div>
	<div class="bd">
		<!--
		<form action="<c:url value="/admin/EodStates.htm"/>" method="get" name="EodListForm">
			<input type="hidden" name="page" value="${paging.page}" />
		</form>
	-->
		<ul id="countries010" style="width: 30%; float: left; margin: 0; padding: 0;">
			<c:forEach items="${fcountries}" var="country" varStatus="inx">
				<c:choose>
					<c:when test="${inx.index % 2 == 0}">
						<li class="odd" style="list-style: none; margin: 0; padding: 5px; margin-right: 10px; background-color: white;">
					</c:when>
					<c:otherwise>
						<li class="even" style="list-style: none; margin: 0; padding: 5px; margin-right: 10px; background-color: white;">
					</c:otherwise>
				</c:choose>
					<a href="<c:url value="/admin/EyvEditCountry.htm"><c:param name="countryId" value="${country.id}"/></c:url>">${country.name}</a>
					</li>
			</c:forEach>
		</ul>
		<ul id="countries020" style="width: 30%; float: left; margin: 0; padding: 0;"></ul>
		<ul id="countries030" style="width: 30%; float: left; margin: 0; padding: 0;"></ul>
		
		<script language="javascript" src="<c:url value="/scripts/jquery.js"/>"></script>
		
		<script language="javascript">

function breakColumns (what, where, columnsNumber) {
//	var columnsNumber = 4;
	var listSize = $(what).size();
	var columnSize = Math.ceil(listSize / columnsNumber);
	var fillCounter = 0;
	var columnCounter = 0;
	
	var columns = new Array();
	columns[0] = new Array();

	$(what).each(function (i) {
		if (fillCounter >= columnSize) {  // go to the next column
			columnCounter++;
			columns[columnCounter] = new Array();
			fillCounter = 0;
		}

		var li = this;
		columns[columnCounter][fillCounter] = li;
		fillCounter++;
	});


	for (var k = 1; k < columns.length; k++) {
		var cn = k + 1;
		for (var i = 0; i < columns[k].length; i++) {
			$(columns[k][i]).appendTo(where.replace('%', cn));
		}
	}
}

breakColumns("#countries010 > li", "#countries0%0", 3);

		</script>
		
		<%--<p class="pagination">
			Pages:
			<c:forEach begin="0" end="${paging.pagesTotal-1}" var="pageNum">
				<c:choose>
					<c:when test="${paging.page eq pageNum}">
						&nbsp;${pageNum +1}&nbsp;
					</c:when>
					<c:otherwise>
						&nbsp;<a href="<c:url value="/admin/EodStates.htm"/>?page=${pageNum}">${pageNum +1}</a>&nbsp;
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</p>--%>
		<div style="clear: both"></div>
	</div>
	<div class="ft">
	</div>
</div>

