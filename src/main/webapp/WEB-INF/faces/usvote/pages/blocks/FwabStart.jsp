<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="fwab fwab-start row" id="fwab-start-box">
	<div class="col-xs-12 col-sm-10">
		<div class="block-page-title-block">
	        <h1 class="title election-head1">Federal Write-in Absentee Ballot</h1>
	      </div>
	    <div class="content-inner-fwab">
			<div class="row">
				<div class="col-xs-12 col-sm-9">
					<h3>What is the Federal Write-in Absentee Ballot (FWAB)?</h3>
					<ul>
						<li>The FWAB is an alternative ballot for overseas and uniformed services voters whose ballots are late.</li>
						<li>In some states, the FWAB can also be used as a voter registration form &amp; ballot.</li>
					</ul>
					<form class="who-can-use-fwab" id="who-can-use-fwab" action="" method="post">
						<h3>Let's Get Started<br /></h3>
						<br/>
						<div class="go-button-container" style="margin-top: 20px;">
							<a class="go-button button" id="go-button" href="<c:url value='/w/fwab.htm?useVoter=3&amp;fwab=true&amp;${queryString }'/>">Start</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>