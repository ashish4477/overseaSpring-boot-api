<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="fwab fwab-start column wide-content" id="fwab-start-box">
	<div class="hd">
		<h1 class="title">Federal Write-in Absentee Ballot</h1>
	</div>
	<div class="bd">
		<div class="column-right">
			<h2>Watch the video</h2>
			<br />
			<a href="https://www.youtube.com/watch?v=2KPGyUZk8Mw" target="_blank">
				<img src="<c:url value='/img/video-screenshot.gif' />" alt="FWAB Video Screenshot" />
			</a>
			<h2>Find out what to do<br />if your ballot is late.</h2>
			<p class="caption">(Video opens in new window)</p>
			
		</div>
		<div class="column-left">
			<div class="what-is-fwab">
				<h2>What is the Federal Write-in Absentee Ballot (FWAB)?</h2>
				<ul>
				<li>The FWAB is an alternative ballot for overseas and uniformed services voters whose ballots are late.</li>
				</ul>
			</div>
			<form class="who-can-use-fwab" id="who-can-use-fwab" action="" method="post">
				<h2>Let's Get Started<br /></h2>
                <br/>
                <div class="go-button-container" style="margin-top: 20px;">
                    <a class="go-button" id="go-button" href="<c:url value='/w/fwab.htm?useVoter=3&amp;fwab=true&amp;${queryString }'/>"><img src="<c:url value='/img/buttons/start-button.png'/>" alt="Start" /></a>
                </div>
			</form>
		</div>
		<div class="break"></div>
	</div>
	<div class="ft"></div>
</div>
