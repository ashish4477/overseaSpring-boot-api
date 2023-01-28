<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>

<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="<c:url value="/js/bootstrap/plugins/date/ui.daterangepicker.css"/>" type="text/css" />
<script src="<c:url value="/js/bootstrap/plugins/date/date.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/bootstrap/plugins/date/daterangepicker.jQuery.js"/>" type="text/javascript"></script>
<script type="text/javascript">
    var initialDateRange;
    <c:choose>
    <c:when test="${startDate == null}">
    <c:choose>
    <c:when test="${endDate == null}">
    initialDateRange = "Today";
    </c:when>
    <c:otherwise>
    initialDateRange = "1/1/2000 - ${endDate}";
    </c:otherwise>
    </c:choose>
    </c:when>
    <c:otherwise>
    <c:choose>
    <c:when test="${endDate == null}">
    initialDateRange = "${startDate} - 12/31/3000";
    </c:when>
    <c:otherwise>
    initialDateRange = "${startDate} - ${endDate}";
    </c:otherwise>
    </c:choose>
    </c:otherwise>
    </c:choose>
    $(function() {
        $('#rangeC').daterangepicker({
            dateFormat : "mm/dd/yy",
            currentText : initialDateRange,
            arrows : false,
            onClose : function(dateText, inst) {
                setTimeout(function() {
                    parseDates();
                }, 1000);
            }
        });
    });

    function parseDates() {
        var dateInputValue = $('#rangeC').val().split(" ").join("");
        var range = dateInputValue.split('-');
        if ( typeof range[0] != 'undefined' ) {
            var prefix = '<c:url value="/onlinedatatransfer/OnlineDataTransfer.htm"><c:param name='vrState' value='${vrState}'/><c:param name='vrName' value='${vrName}'/></c:url>';
            if (typeof range[1] != "undefined") {
                window.location=prefix+'&startDate='+encodeURI(range[0])+'&endDate='+encodeURI(range[1]);
            }
            else {
                window.location=prefix+'&startDate='+encodeURI(range[0]);
            }
        }
    }
</script>


<div class="body-content wide-content column" id="onlineDataTransfer">
	<div class="page-form rava-start" id="rava-start-box">
		<div class="hd">
			<div class="hd-inner">
				<h2 class="title">${title}</h2>
			</div>
		</div>

		<div class="bd">
            <div id="dataPicker" class="well form-inline rnd4">
                <span class="help-inline"><b>Set Report Date Range:</b></span>&nbsp;&nbsp;
                <input class="form-control date-range" style="background-color:#fff;" type="text" value="" readonly='readonly' placeholder="click to change date range..." id="rangeC" />
            </div>
            <div class="alert alert-info clearfix">
                <a class="close" data-dismiss="alert">×</a>
                <c:choose>
                    <c:when test="${startDate != null && endDate != null}">
                        <h4>
                            <strong>Current date range: ${startDate} to ${endDate}</strong>
                        </h4>
                    </c:when>
                    <c:when test="${startDate != null}">
                        <h4>
                            <strong>for ${startDate}</strong>
                        </h4>
                    </c:when>
                    <c:when test="${endDate != null}">
                        <h4>
                            <strong>before ${endDate}</strong>
                        </h4>
                    </c:when>
                    <c:otherwise>
                        <h4>
                            Current Date Range: Year to Date (<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>)
                        </h4>
                    </c:otherwise>
                </c:choose>
            </div>

			<c:if test="${empty pending}">
				<div class="alert alert-block" style="color: #b46b00; font-weight: bold;">
					<c:out value="There are currently no pending voter registrations records to be downloaded." />
				</div>
			</c:if>
			<table class="table shadow rnd4 table-striped">
				<tr>
					<td>Voting State</td>
					<td><c:choose>
						<c:when test="${not empty vrState}">${vrState}</c:when>
						<c:otherwise>All states</c:otherwise>
					</c:choose> </td>
				</tr>

				<tr>
					<td>Voting Region</td>
					<td><c:choose>
						<c:when test="${not empty vrName}">${vrName}</c:when>
						<c:otherwise>All regions</c:otherwise>
					</c:choose></td>
				</tr>

				<tr>
					<td>There are ${resultsCount} records found.</td>
					<td>
                        <c:if test="${resultsCount gt fn:length(pending)}">Only first ${fn:length(pending)} records are shown</c:if>
                        <c:if test="${not downloadAllowed}"><span>You selected too big range, we can't create such huge file for download. Please select smaller date range. </span></c:if>
                    </td>
				</tr>

				<tr>
					<th>ID</th>
					<th>Created</th>
					<th>First Name</th>
					<th>Middle Name</th>
					<th>Last Name</th>
					<th>Suffix</th>
				</tr>

				<c:forEach var="entry" items="${pending}">
					<tr>
						<td>${entry.key}</td>
						<td>${created[entry.key]}</td>
						<td>${entry.value.firstName}</td>
						<td>${entry.value.middleName}</td>
						<td>${entry.value.lastName}</td>
						<td>${entry.value.suffix}</td>
					</tr>
				</c:forEach>
			</table>
			<br />

			<div class="nav nav-pills">
				<%--<a class="btn btn-info"
					href="<c:url value='OnlineDataTransfer.htm'><c:param name='vrState' value='${vrState}'/><c:param name='vrName' value='${vrName}'/></c:url>"
				><i class="icon-refresh icon-white"></i> <b><c:out value="Refresh Pending Voter Registrations" /></b></a>--%>
				<c:choose>
					<c:when test="${not empty pending and downloadAllowed }">
						<a class="btn btn-info" id="download"
							href="<c:url value='OnlineDataTransferZip.htm'><c:param name='vrState' value='${vrState}'/><c:param name='vrName' value='${vrName}'/><c:param name='startDate' value='${startDate}'/><c:param name='endDate' value='${endDate}'/></c:url>"
						><span class="glyphicon glyphicon-download-alt icon-download-alt icon-white"></span> <b><c:out value="Download Pending Voter Registrations" /></b></a>
					</c:when>
					<c:otherwise>
						<a href="#" id="download" class="btn disabled"><span class="glyphicon glyphicon-ban-circle icon-ban-circle"></span> <b>Download Pending Voter Registrations</b></a>
					</c:otherwise>
				</c:choose>
			</div>
            <div id="download_message" style="display: none; font-weight: bold; text-align: center;">Wait a moment please. Done <span id="percent">0</span>% </div>

		</div>
	</div>
</div>


<script type="text/javascript">

    function checkCsvStatus(id) {
        setTimeout(function() {
        var jqxhr = $.ajax( '<c:url value="/onlinedatatransfer/ajax/DoDataTransfer.htm"><c:param name='vrState' value='${vrState}'/><c:param name='vrName' value='${vrName}'/><c:param name='startDate' value='${startDate}'/><c:param name='endDate' value='${endDate}'/></c:url>' + '&id=' + id )
          .done(function(data) {
            //console.log(id, data);

            $('#percent').text('' + data.percent);
            if (data.status === 2 ) {
            // generation finished
                window.location.href = '<c:url value='OnlineDataTransferZip.htm'><c:param name='vrState' value='${vrState}'/><c:param name='vrName' value='${vrName}'/></c:url>' + '&id=' + data.id;

            } else if (data.status === 0 || data.status === 1) {
            // new and in_progress statuses
                checkCsvStatus(data.id);  // check every 2 seconds

            } else if (data.status === 3) {    // error status
                //alert(data.message);
                $('#download_message').text( data.message );
            }
          })
          .fail(function() {
          });
        }, 2000); // check every 2 seconds
    }

  $("#download").one("click", function() {
      $('#download_message').show();
      checkCsvStatus('');
      $(this).attr('class', 'btn disabled');
      $(this).children(":first").attr('class', 'icon-ban-circle');
      $(this).click(function () { return false; });
      return false;
  });

</script>