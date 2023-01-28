<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="fwab fwab-start row" id="fwab-start-box">
	<div class="col-xs-12 col-sm-10 col-sm-offset-1">
		<h1 class="title">Federal Write-in Absentee Ballot</h1>
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
  <div class="col-xs-12 col-sm-3">
			<h3>Watch the video</h3>
			<p><a href="https://www.youtube.com/watch?v=2KPGyUZk8Mw" target="_blank" class="fancybox-media">
				<img src="<c:url value='/img/video-screenshot.gif' />" alt="FWAB Video Screenshot" />
			</a></p>
			<strong>Find out what to do if your ballot is late.</strong>
	</div>
</div>
</div>
</div>