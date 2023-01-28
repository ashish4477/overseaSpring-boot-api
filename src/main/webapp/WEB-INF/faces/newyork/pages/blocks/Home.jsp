<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>

<script src="<c:url value="/js/load-regions.js"/>" type="text/javascript"></script>
<c:url value="/ajax/getRegionsHTMLSelect.htm" var="getRegionsURL"/>
<script type="text/javascript" language="JavaScript">
    //<!--
    var params = { 'stateInputId':'stateId', 'regionInputId':'ajax_region_select', 'url':'${getRegionsURL}' };
    YAHOO.util.Event.addListener(window, "load", loadRegions, params);
    //-->
</script>

<div class="well home featured voterType">
	<div class="container">
	        <h1>Register to Vote &amp; Request Your Absentee Ballot</h1>
	        <form method="get" action="<c:url value='/w/rava.htm'><c:param name='vrState' value='NY'/></c:url>">
	             <fieldset>
	             	<label>Get Automated Overseas Voter Registration Assistance</label>
	             	<label>Prepare Your Annual Absentee Ballot Request</label>
						<select name="voterType">
								<option value="">Please choose your voter type...</option>
								<c:forEach items="${voterTypesList}" var="voterType">
									<option value="${voterType.name}">${voterType.value}</option>
								</c:forEach>
								<optgroup label=""></optgroup>
							</select>
					<button type="submit" class="btn btn-info">NEXT</button>
	            </fieldset>
	        </form>
	    </div>
		</div>
		<div class="clearfix"></div>
		<div class="span6 home first">
		<div class="well featured ballot access">
			<div class="inner">
			<h3>New York Ballot Access System</h3>
			 <p>
			Registered New York voters can get and print your ballot and track
			the status of your ballot using the New York Ballot Access System
			</p>
			<a href="https://www.secureballotusa.com/NY" target="_blank" class="btn btn-info pull-right">GO</a>
		  </div>
    </div><!--/span-->
    </div><!--/span-->
		<div class="span3 home">
		<div class="well featured county selection">
			<div class="inner">
       <h3>County Information</h3>
        <p>View Contacts and Information by County</p>
        <form action="<c:url value="/countyCorner.htm"/>" method="post">
        <input type="hidden" name="stateId" id="stateId" value="35"/>
        <span id="ajax_region_select">
        <select required  id="votingRegion" class="field" name="regionId">
            <option value="0">Loading Please Wait..</option>
        </select>
        </span>
        <button class="btn btn-info" type="submit" value="View">GO</button>
        </form>
			</div>
		</div>
        </div><!--/span-->
        
