<html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reporting Dashboard - Edit Report</title>

<script type="text/javascript">
	$(document).ready(function() {
		var pageSelect = document.getElementById("pageSelect");
		pageSelect.options.length = 0;
		for (pageKey in pages) {
			pageOption = new Option();
			pageOption.text = pageKey;
			pageOption.value = pageKey;
			pageSelect.add(pageOption);
		}
		pageSelect.selectedIndex = 0;
		selectPage();
		
		var userDetailSelect = document.getElementById("userDetailSelect");
		userDetailSelect.options.length = 0;
		for (userDetailKey in userDetails) {
			userDetailOption = new Option();
			userDetailOption.text = userDetailKey;
			userDetailOption.value = userDetailKey;
			userDetailSelect.add(userDetailOption);
		}
		userDetailSelect.selectedIndex = 0;
		selectUserDetail();

		if ($("p").hasClass("Number")) {
			$('ul#metricMenu li #num').addClass("disabled");
		}
		if ($("p").hasClass("Percentage")) {
			$('ul#metricMenu li #percentage').addClass("disabled");
		}
		if ($("p").hasClass("age group")) {
			$('ul#metricMenu li #age').addClass("disabled");
		}
	});

	var pages = new Object();
	var page = createDummyPage();
	pages[page["key"]] = page;
	<c:forEach items="${pages}" var="pageItem">
		page = new Object();
		page["key"] = "${fn:escapeXml(pageItem.key)}";
		page["value"] = new Object();
		var group = createDummyGroup();
		page["value"][group["key"]] = group;
		var groupCount = 1;
		<c:forEach items="${pageItem.value}" var="groupItem">
			group = new Object();
			group["key"] = "${fn:escapeXml(groupItem.key)}";
			group["value"] = new Object();
			var question = createDummyQuestion();
			group["value"][question["key"]] = question;
			var questionCount = 1;
			<c:forEach items="${groupItem.value}" var="questionItem">
				question = new Object();
				question["key"] = "${fn:escapeXml(questionItem.key)}";
				question["value"] = new Array();
				<c:forEach items="${questionItem.value}" var="answerItem">
					question["value"].push(unescape("${answerItem}"));
				</c:forEach>
				group["value"][question["key"]] = question;
				++questionCount;
			</c:forEach>
			if (questionCount > 1) {
				page["value"][group["key"]] = group;
				++groupCount;
			}
		</c:forEach>
		if (groupCount > 1) {
			pages[page["key"]] = page;
		}
	</c:forEach>
	
	var userDetails = new Object();
	var userDetail = createDummyUserDetail();
	var answers;
	userDetails[userDetail["key"]] = userDetail;
	<c:forEach items="${userFieldNames}" var="userFieldName">
		userDetail = new Object();
		userDetail["key"] = "${userFieldName.uiName}";
		userDetail["value"] = "${userFieldName.uiName}";
		answers = new Array();
		<c:forEach items="${userFieldName.answers}" var="answer">
			answers.push("${answer}");
		</c:forEach>
		userDetail["answers"] = answers;
		userDetails[userDetail["key"]] = userDetail;
	</c:forEach>
	
	function createDummyUserDetail() {
		var userDetail = new Object();
		userDetail["key"] = "-- Select a Voter Detail --";
		userDetail["value"] = "-- Select a Voter Detail --";
		return userDetail;
	}

	function createDummyPage() {
		var page = new Object();
		page["key"] = "-- Select a Page --";
		page["value"] = new Object();
		var group = createDummyGroup();
		page["value"][group["key"]] = group;
		return page;
	}

	function createDummyGroup() {
		var group = new Object();
		group["key"] = "-- Select a Group --";
		group["value"] = new Object();
		var question = createDummyQuestion();
		group["value"][question["key"]] = question;
		return group;
	}

	function createDummyQuestion() {
		var question = new Object();
		question["key"] = "-- Select a Question --";
		question["value"] = new Array();
		return question;
	}

	function findPage() {
		var pageSelect = document.getElementById("pageSelect");
		var pageKey = pageSelect.options[pageSelect.selectedIndex].value;
		var page = pages[pageKey];
		return page;
	}

	function findGroup(page) {
		var groupSelect = document.getElementById("groupSelect");
		var groupKey = groupSelect.options[groupSelect.selectedIndex].value;
		var group = page["value"][groupKey];
		return group;
	}

	function findQuestion(group) {
		var questionSelect = document.getElementById("questionSelect");
		var questionKey = questionSelect.options[questionSelect.selectedIndex].value;
		var question = group["value"][questionKey];
		return question;
	}

	function selectPage() {
		var page = findPage();
		var groupSelect = document.getElementById('groupSelect');
		groupSelect.options.length = 0;

		for (groupKey in page["value"]) {
			var groupOption = new Option();
			groupOption.text = groupKey;
			groupOption.value = groupKey;
			groupSelect.add(groupOption);
		}
		if (groupSelect.options.length == 2) {
			groupSelect.selectedIndex = 1;
		} else {
			groupSelect.selectedIndex = 0;
		}
		groupSelect.disabled = false;
		selectGroup();
	}

	function selectGroup() {
		var page = findPage();
		var group = findGroup(page);
		var questionSelect = document.getElementById('questionSelect');
		questionSelect.options.length = 0;
		for (questionKey in group["value"]) {
			var questionOption = new Option();
			questionOption.text = questionKey;
			questionOption.value = questionKey;
			questionSelect.add(questionOption);
		}
		if (questionSelect.options.length == 2) {
			questionSelect.selectedIndex = 1;

		} else {
			questionSelect.selectedIndex = 0;
			questionSelect.disabled = false;
		}
		
		questionSelect.disabled = false;
		selectQuestion();
	}

	function selectQuestion() {
		var page = findPage();
		var group = findGroup(page);
		var question = findQuestion(group);
		var answers = question["value"];
		var questionAnswersSelect = document.getElementById("questionAnswersSelect");
		questionAnswersSelect.options.length = 0;
		if ( (answers.length == undefined) || (answers.length == 0)) {
			questionAnswersSelect.disabled = true;
			$('.showAllValues').hide();
			
		} else {
			for (var i = 0; i < answers.length; ++i) {
				var answer = answers[i];
				questionAnswerOption = new Option();
				questionAnswerOption.text = answer;
				questionAnswerOption.value = answer;
				questionAnswersSelect.add(questionAnswerOption);
			}
			questionAnswersSelect.disabled = false;
			$('.showAllValues').show();
			$('.reset').prop('checked', true);
		}
	}
	
	function selectUserDetail() {
		var userDetailSelect = document.getElementById("userDetailSelect");
		var userDetailKey = userDetailSelect.options[userDetailSelect.selectedIndex].value;
		var userDetail = userDetails[userDetailKey];
		var answers = userDetail["answers"];
		var userDetailAnswersSelect = document.getElementById("userDetailAnswersSelect");
		userDetailAnswersSelect.options.length = 0;
		if ((answers == undefined) || (answers.length == 0)) {
			userDetailAnswersSelect.disabled = true;
			$('.showAllValues').hide();
		} else {
			for (var i = 0; i < answers.length; ++i) {
				var answer = answers[i];
				userDetailAnswerOption = new Option();
				userDetailAnswerOption.text = answer;
				userDetailAnswerOption.value = answer;
				userDetailAnswersSelect.add(userDetailAnswerOption);
			}
			userDetailAnswersSelect.disabled = false;
			$('.showAllValues').show();
			$('.reset').prop('checked', true);
		}
	}

	function moveRow(elem, direction) {
		parentTable = elem.parentNode.parentNode.parentNode;
		clickedRowIndex = elem.parentNode.parentNode.rowIndex - 1;
		adjacentRowIndex = clickedRowIndex + direction;
		if (adjacentRowIndex < 0) {
			alert("This is the uppermost row and cannot be moved upwards");
			return false;
		}

		maxRowIndex = parentTable.getElementsByTagName("tr").length - 1;
		if (adjacentRowIndex > maxRowIndex) {
			alert("This is the bottommost row and cannot be moved downwards.");
			return false;
		}

		clickedRow = parentTable.getElementsByTagName("tr")[clickedRowIndex];
		adjacentRow = parentTable.getElementsByTagName("tr")[adjacentRowIndex];

		clickedRow_Clone = clickedRow.cloneNode(true);
		adjacentRow_Clone = adjacentRow.cloneNode(true);

		adjacentRow = parentTable.replaceChild(clickedRow_Clone, adjacentRow);
		clickedRow = parentTable.replaceChild(adjacentRow_Clone, clickedRow);
	};

	function deleteRow(elem) {
		parentTable = elem.parentNode.parentNode.parentNode;
		clickedRowIndex = elem.parentNode.parentNode.rowIndex - 1;
		clickedRow = parentTable.getElementsByTagName("tr")[clickedRowIndex];
		parentTable.removeChild(clickedRow);
	};

	function setMetric(metricName) {
		if (!$("p").hasClass(metricName)) {
			editReport = $('#editReport');			
			var metric = $('<input/>', { type:'hidden', id: 'metric', name:'metric', value:metricName});
			metric.appendTo(editReport);
			var addMetric = $('<input/>', { type:'hidden', id: 'addMetric', name:'addMetric', value:metricName});
			addMetric.appendTo(editReport);
			editReport.submit();
			return true;
		} else {
			alert(metricName + ' already Exists');
		}
	};

	function checkQuestion() {
		var page = findPage();
		if (page["key"] == "-- Select a Page --") {
			alert("You must select a page");
			return false;
		}
		var group = findGroup(page);
		if (group["key"] == "-- Select a Group --") {
			alert("You must select a group");
			return false;
		}
		var question = findQuestion(group);
		if (question["key"] == "-- Select a Question --") {
			alert("You must select a question");
			return false;
		}

		editReport = $('#editReport');
		var hiddenInput = $('<input/>', { type:'hidden', id: 'saveReport', name:'saveReport', value:'saveReport'});
		hiddenInput.appendTo(editReport);
		$.ajax({
			url: editReport.attr('action'),
			type: 'POST',
			data: editReport.serialize(),
			async: false
		});
		
		return true;
	};
	
	function checkProfile() {
		var userDetailSelect = document.getElementById("userDetailSelect");
		var userDetailKey = userDetailSelect.options[userDetailSelect.selectedIndex].value;
		if (userDetailKey == "-- Select a Voter Detail --") {
			alert("You must select a voter detail");
			return false;
		}

		editReport = $('#editReport');
		var hiddenInput = $('<input/>', { type:'hidden', id: 'saveReport', name:'saveReport', value:'saveReport'});
		hiddenInput.appendTo(editReport);
		$.ajax({
			url: editReport.attr('action'),
			type: 'POST',
			data: editReport.serialize(),
			async: false
		});
		
		return true;		
	};

</script>

</head>
<body>
	<div class="page-header">
		<h2>
			Edit Report<small>&nbsp; add and remove columns to custom report</small>
		</h2>
	</div>

	<form id="editReport" name="editReport" class="form-horizontal" action='<c:url value="/reportingdashboard/EditReport.htm"/>'
		method="post">
		<div class="innerRight">
			<div class="report-content-box rnd4">
				<div class="report-content-head clearfix">
					<h3>${report.title}</h3>

					<ul id="reportEditNav" class="nav nav-pills pull-right">
						<li id="voterProfileSettings" class="dropdown">
							<div class="btn-group" title="Add to Report">
								<button class="btn btn-small" data-toggle="dropdown">
									<span id="add-metric-column" class="addMetric">Add to Report</span>
								</button>
								<button class="btn dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul id="addToReport" class="dropdown-menu" id="menu1">
									<li><a href="#voterProfile" data-toggle="modal">Answers from Voter Profile</a></li>
									<c:if test="${pages != null}">
										<li class="divider"></li>
										<li><a href="#voterQuestions" data-toggle="modal">Answers from Registration/Request Process</a></li>
									</c:if>
								</ul>
							</div>
						</li>
						<li id="addMetric" class="dropdown">
							<div class="btn-group" title="Add to Report">
								<button class="btn btn-small" id="Add-Metric" data-toggle="dropdown">
									<span id="add-metric-column" class="addMetric">Add Metric</span>
								</button>
								<button class="btn dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>

								<ul class="dropdown-menu" id="metricMenu">
									<li id="num"><a onclick='setMetric("Number");' href="#">Number</a>
									<li class="divider"></li>
									<li id="percentage" class=""><a onclick='setMetric("Percentage");' href="#">Percentage</a>
									<li class="divider"></li>
									<li id="age"><a onclick='setMetric("Age Group");' href="#">Age Group</a>
								</ul>
							</div>
						</li>
					</ul>
				</div>

				<div class="report-content-wrap">
					<table id="reportTable" width="100%" class="table shadow table-bordered Zebra-stripe">
						<thead>
							<tr>
								<th>Column Title</th>
								<th align="center" style="white-space: nowrap;">Move Up</th>
								<th align="center" style="white-space: nowrap;">Move Down</th>
								<th align="center">Remove</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${report.columns}" var="column" varStatus="columnStatus">
								<tr>
									<td>
										<p class="${column.name}">${column.name}</p> <input type="hidden" name="columnId" value="${column.id}" />
									</td>
									<td class="span1"><a onclick="javascript:moveRow(this, -1)" href="#"><i class="icon-arrow-up"></i></a></td>
									<td class="span1"><a onclick="javascript:moveRow(this, 1)" href="#"><i class="icon-arrow-down"></i></a></td>
									<td class="span1"><a class="span1" onClick="javascript:deleteRow(this)" title="Remove" href="#"><i
											class="icon-trash icon"
										></i></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="reportControl well clearfix">
						<input type="hidden" name="columnId" value="-1" />
						<input class="btn" type="hidden" name="reportId" value="${report.id}" />
						<div class="pull-right">
							<c:if test="${report.id != null}">
								<a class="btn confirm" href="<c:url value="/reportingdashboard/DeleteReport.htm"><c:param name="reportId" value="${report.id}"/></c:url>"> Delete Report</a>
							</c:if>
							<input class="btn btn-primary" type="submit" value="Save &amp; View" name="saveReport" id="saveReport" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<%--end report editor table--%>

	<div id="voterProfile" class="modal fade hide" style="display: none;">
		<form id="editReportVoterProfile" class="form-horizontal" action="<c:url value="/reportingdashboard/EditReport.htm"/>"
			method="post">
			<fieldset>
				<div class="modal-header">
					<a class="close" data-dismiss="modal">X</a>
					<h3>Report Editor for: ${report.title}</h3>
				</div>

				<div class="modal-body">
					<legend>Add Column Based on Voter Profile </legend>
					<div id="Details">
						<div class="control-group">
							<label class="control-label">Select Voter Detail</label><br />
							<div class="controls">
								<select id="userDetailSelect" class="span4" name="userDetail" onChange="selectUserDetail();">
								</select>

								<p class="help-block">Select to get voter detail data results</p>
							</div>
						</div>
						
						<hr>
						<div class="control-group">
							<label class="control-label">Enter Custom Title For Voter Profile Data</label>
							<div class="controls">
								<input name="columnName" value="Enter Column Title" type="text" class="span4 input-xlarge userColumnName" />
								<p class="help-block">Custom name to identify voter profile column title</p>
							</div>
						</div>

						<hr>
						<div class="control-group">
							<label class="control-label">Show These Values in Report</label>
							<div class="controls">
								<select id="userDetailAnswersSelect" class="span4" name="answer" disabled="disabled" multiple="multiple"></select>
								<p class="showAllValues"><input type="checkbox" class="reset" name="reset" checked="checked" /> Show All Values</p>
							</div>
						</div>
					</div>
					<%--end modal body--%>
				</div>

				<div class="modal-footer">
					<input class="btn" type="hidden" name="reportId" value="${report.id}" />
					<input class="btn btn-primary" type="submit" value="Add Column To Report" name="addUserDetail" onclick="return checkProfile();"/>
					<input class="btn btn-cancel" value="Cancel" data-dismiss="modal" name="Cancel" />
				</div>
			</fieldset>
		</form>
	</div>

	<div id="voterQuestions" class="modal fade hide" style="display: none;">
		<form id="editReportVoterQuestions" class="form-horizontal" action='<c:url value="/reportingdashboard/EditReport.htm"></c:url>'
			method="post">
			<fieldset>
				<div class="modal-header">
					<a class="close" data-dismiss="modal">X</a>
					<h3>Report Editor for: ${report.title}</h3>
				</div>

				<div class="modal-body">
					<legend>Data Analysis From Questions</legend>
					<label>Question Variations From Page Flow</label>
					<div id="Pages">
						<div class="controlgroup">
							<label class="control-label">Page</label>
							<div class="controls">
								<select id="pageSelect" name="wizardPage" class="span4 questionSelect" onChange="selectPage()">
								</select>
							</div>
						</div>
						<div class="controlgroup">
							<label class="control-label">Group</label>
							<div class="controls">
								<select id="groupSelect" name="wizardGroup" class="span4 questionSelect" onChange="selectGroup()">
								</select>
							</div>
						</div>
						
						<div class="controlgroup">
							<label class="control-label">Question</label>
							<div class="controls">
								<select id="questionSelect" name="wizardQuestion" class="span4 questionSelect" onChange="selectQuestion()">
								</select>
							</div>
						</div>
						
						<hr>
						<div class="controlgroup">
							<label>Enter Custom Title For Voter Question Data</label>
							<div class="controls">
								<input name="columnName" value="Enter Column Title" type="text" class="span4 input-xlarge questionColumnName" />
								<p class="help-block">Custom name to identify voter question column title</p>
							</div>
						</div>

						<hr>
						<div class="control-group">
							<label class="control-label">Show These Values in Report</label>
							<div class="controls">
								<select id="questionAnswersSelect" class="span4" name="answer" disabled="disabled" multiple="multiple"></select>
								<p class="showAllValues"><input type="checkbox" class="reset" name="reset" checked="checked" /> Show All Values</p>
							</div>
						</div>
					</div>
				</div>

				<div class="modal-footer">
					<input type="hidden" name="reportId" value="${report.id}" />
					<input class="btn btn-primary" type="submit" value="Add Column To Report" name="addQuestion" onclick="return checkQuestion();" />
					<input class="btn btn-cancel" type="text" value="Cancel" data-dismiss="modal" name="Cancel" />
				</div>
			</fieldset>
		</form>
	</div>
	
	<script>
	//sets column name to match selection
	$('#userDetailSelect').change(function() {
		userColumnName = $(this).val();
		$('.userColumnName').val(userColumnName);
		});	
	
	$('.questionSelect').change(function() {
		questionColumnName = $('#questionSelect').val();
		$('.questionColumnName').val(questionColumnName);
		});	
	
	$('.confirm').click(function(){
		  var answer = confirm('Are You Sure You Want to Remove This Report?');
		  return answer;
		});
	
	$('select[multiple]').click(function(){
		$('.reset').prop('checked', false);
		$('select[multiple]').removeAttr('disabled');
		});

	$('.reset').change(function(){
	 if ($(this).prop('checked'))
	     $('select[multiple] option').removeAttr('checked'),
	     $('select[multiple] option').removeAttr('selected'),
	     //clears the selection visually
	     $('select[multiple]').attr("disabled","disabled"),
	     $('select[multiple]').removeAttr('disabled');
	 else
		 $('select[multiple]').removeAttr('disabled');
	});	
	
	</script>
</body>
</html>
