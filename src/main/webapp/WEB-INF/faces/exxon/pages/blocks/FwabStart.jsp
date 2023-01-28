<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="body-content">
	
	<div class="page-form fwab-start" id="fwab-start-box">
		<div class="hd">
			<div class="hd-inner">
				<h1 class="title">Federal Write-in Absentee Ballot</h1>
			</div>
		</div>
		<div class="bd">
			<div class="bd-inner">
				<div class="column-right">
					<h2>Watch the video</h2>
					<br />
					<a href="https://www.youtube.com/watch?v=2KPGyUZk8Mw" target="_blank">
						<img src="<c:url value='/img/video-screenshot.gif' />" alt="FWAB Video Screenshot" />
					</a>
					<h2>Find out what to do<br />if your ballot is late.</h2>
					<p class='caption'>(Video opens in new window)</p>
				</div>
				<div class="column-left">
			<div class="what-is-fwab">
				<h2>What is the Federal Write-in Absentee Ballot (FWAB)?</h2>
				<ul>
				<li>The FWAB is an alternative ballot for overseas and uniformed services voters whose ballots are late.</li>
				<li>In some states, the FWAB can also be used as a voter registration form &amp; ballot.</li>
				</ul>
			</div>
			<form class="who-can-use-fwab" id="who-can-use-fwab" action="" method="post">
				<h2>Quick-check for your FWAB eligibility<br /><em>(check all that apply)</em></h2>
				<label>
					<span class="field"><input type="checkbox" class="step step-two" value="step-two" /></span>
					<span class="label">I sent in the registration/ballot request form to receive an absentee ballot by my state deadline, but my absentee ballot has not yet arrived.</span>
				</label>
				<p>- OR -</p>
				<label>
					<span class="field"><input type="checkbox" class='step step-three' value="step-three" /></span>
					<span class="label">I am from AK, AZ, AR, CO, DE, DC, GA, IA, MA, ME, MD, MN, MS, MT, NE, NC, NV, OK, OR, SC, SD, UT, VA, or WA <span class="hint">(These states allow the FWAB to be used as a simultaneous registration + ballot)</span></span>
				</label>
				<p>
					<strong>Please check the appropriate boxes above, and click start.</strong><br />
				</p>
				<div class="go-button-container">
					<a class="go-button" id="go-button" style="visibility:hidden" href="<c:url value='/w/fwab.htm?useVoter=3&amp;fwab=true&amp;${queryString }'/>"><img src="<c:url value='/img/buttons/start-button.png'/>" alt="Start" /></a>
				</div>
			</form>
			</div>
			<div class="break"></div>
		</div>
		</div>
		<div class="ft"><div class="ft-inner"></div></div>
	</div>
</div>
