<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<div class="well home featured voterType">
	<div class="container">
	        <h1>Register to Vote &amp; Request Your Absentee Ballot</h1>
	        <form method="get" action="<c:url value='/w/rava.htm'><c:param name='vrState' value='OH'/></c:url>">
	             <fieldset>
	             	<label>Get Automated Overseas Voter Registration Assistance</label>
	             	<label>Prepare Your Annual Absentee Ballot Request</label>
						<select name="voterClassificationType">
								<option value="">Please choose your voter type...</option>
								<c:forEach items="${voterClassificationTypesList}" var="voterClassificationType">
									<option value="${voterClassificationType.name}">${voterClassificationType.value}</option>
								</c:forEach>
							</select>
					<button type="submit" class="btn btn-info">NEXT</button>
	            </fieldset>
	        </form>
	    </div>
		</div>
		<div class="clearfix"></div>
		<div class="span3 home first">
		<div class="well featured fwab">
			<div class="inner">
			<h3><a href="<c:url value='/FwabStart.htm'><c:param name='vrState' value='OH'/></c:url>">Federal Write-in<br/>Absentee Ballot</a></h3>
			<ul>
				<li><a href="<c:url value='/FwabStart.htm'><c:param name='vrState' value='OH'/></c:url>">Use This Ballot if Your<br/>Requested Ballot is Late</a></li>
			</ul>
			</div>
		</div>
        </div><!--/span-->
		<div class="span3 home second">
		<div class="well featured svid">
			<div class="inner">
			<h2><a href="<c:url value="/svid.htm"><c:param name="submission" value="true"/><c:param name="stateId" value="39"/></c:url>">State Voter <span style="white-space:nowrap;">Information Directory</span></a></h2>
			<ul>
				<li><a href="<c:url value="/svid.htm"><c:param name="submission" value="true"/><c:param name="stateId" value="39"/></c:url>">Check Your Election Dates and Deadlines</a></li>
			</ul>
			</div>
		</div>
        </div><!--/span-->
		<div class="span3 home third">
		<div class="well featured eod">
			<div class="inner">
			<h2><a href="<c:url value="/eod.htm"><c:param name="submission" value="true"/><c:param name="stateId" value="39"/></c:url>">Election Official Directory</a></h2>
			<ul>
				<li><a href="<c:url value="/eod.htm"><c:param name="submission" value="true"/><c:param name="stateId" value="39"/></c:url>">Contact Your Absentee Voting Clerk</a></li>
			</ul>
			</div>
		</div>
        </div><!--/span-->
    	<div class="span8">
			<div class="well featured last">
				<p><a href="#" target="_blank"><a href="https://usoav.ohiosos.gov/UOCAVA_vr__Login.aspx" target="_blank">Check your ballot status through the Ohio Centralized Ballot Tracking System</a></p>
			</div>
	    </div>