<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 14, 2007
  Time: 7:25:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  	<meta http-equiv="Content-Style-Type" content="text/css" />
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>" />
	<link rel="shortcut icon" href="http://gab.wi.gov/sites/default/files/pixture_reloaded_favicon.gif" type="image/x-icon" />
  	<title>Directory of Wisconsin Clerks | Government Accountability Board</title>
  	<link type="text/css" rel="stylesheet" media="all" href="http://gab.wi.gov/sites/default/files/css/css_e567f7ac81e8f83e2c9960ad8132645b.css" />
	<link type="text/css" rel="stylesheet" media="all" href="http://gab.wi.gov/sites/all/themes/pixture_reloaded/sf/css/superfish.css?7" />
  	<!--[if IE]>
    	<link type="text/css" rel="stylesheet" media="all" href="http://gab.wi.gov/sites/all/themes/pixture_reloaded/ie.css" >
  	<![endif]-->

  	<script type="text/javascript" src="http://gab.wi.gov/sites/default/files/js/js_e8d555cc451c1070331fc6b2028c98c1.js"></script>
	<script type="text/javascript" src="http://gab.wi.gov/sites/default/files/jstimer/timer.js?7"></script>
	<script type="text/javascript">
	<!--//--><![CDATA[//><!--
	jQuery.extend(Drupal.settings, { "basePath": "/", "googleanalytics": { "trackOutgoing": 1, "trackMailto": 1, "trackDownload": 1, "trackDownloadExtensions": "7z|aac|arc|arj|asf|asx|avi|bin|csv|doc|exe|flv|gif|gz|gzip|hqx|jar|jpe?g|js|mp(2|3|4|e?g)|mov(ie)?|msi|msp|pdf|phps|png|ppt|qtm?|ra(m|r)?|sea|sit|tar|tgz|torrent|txt|wav|wma|wmv|wpd|xls|xml|z|zip" } });
	//--><!]]>
	</script>

	<style type="">
		fieldset { border: 0; }
		#eodForm .oneline, #eodForm .one-line, #eodForm #continue {
			display: block;
			padding: 0.25em;
			margin: 10px 0 0 160px;
			width: 400px;
		}
		#eodForm #stateName { display: none; }
		.one-line .label {
		 	display: inline-block;
    		padding-right: 1%;
    		text-align: right;
   			vertical-align: middle;
    		width: 32%;
		}
		.accordian-sect, #eod-display .select-form-bd {
			border: 0px none;
		}
		.body-content .bd h5 {
			color: #600;
			font-size: 1.2em;
			margin-top: .5em;
		}
	</style>
	<script src="<c:url value="/js/yahoo-dom-event.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/animation.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/container_core.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/connection.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/element-min.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>

</head>
<body id="pixture-reloaded" class="not-front not-logged-in page-node node-type-page one-sidebar sidebar-left page-clerks-directory section-clerks node-full-view with-logo">

  <div id="skip-to-content">
    <a href="#main-content">Skip to main content</a>
  </div>

    <div id="page" style="width: 1050px;">

      <div id="header">

                  <div id="logo"><a href="http://gab.wi.gov/" title="Home page" rel="home"><img src="http://gab.wi.gov/sites/default/files/pixture_reloaded_logo.gif" alt="Government Accountability Board logo" /></a></div>

        <div id="head-elements">


          <div id="branding">
                                          <div id="site-name"><strong><a href="http://gab.wi.gov/" title="Home page" rel="home">Government Accountability Board</a></strong></div>


                          <div id="site-slogan"><em>STATE OF WISCONSIN</em></div>
                      </div> <!-- /#branding -->

        </div> <!-- /#head-elements -->

                  <!-- Primary || Superfish -->
          <div id="superfish">
            <div id="superfish-inner">


                <ul class="menu"><li class="expanded first"><a href="http://gab.wi.gov/about" title="About the Board">About the Board</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/about/introduction" title="Introduction to the G.A.B.">Introduction to the G.A.B.</a></li>
<li class="leaf"><a href="http://gab.wi.gov/about/members" title="">Board Members</a></li>
<li class="expanded"><a href="http://gab.wi.gov/about/staff" title="">Board Staff</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/about/staff/administration" title="">Administration</a></li>
<li class="leaf"><a href="http://gab.wi.gov/about/staff/elections" title="">Elections Division</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/about/staff/ethics" title="">Ethics Division</a></li>
</ul></li>
<li class="expanded"><a href="http://gab.wi.gov/about/board-actions" title="Board Actions">Board Actions</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/publications/rules">Administrative Rules</a></li>

<li class="leaf"><a href="http://gab.wi.gov/about/enforcement" title="Enforcement">Enforcement</a></li>
<li class="leaf"><a href="http://gab.wi.gov/guidelines" title="Guidelines adopted by the G.A.B.">Guidelines</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/about/opinions" title="Board Opinions">Opinions</a></li>
</ul></li>
<li class="expanded last"><a href="http://gab.wi.gov/about/meetings" title="">Board Meetings</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/about/meetings" title="">Upcoming Board Meetings</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/about/meetings/archive" title="">Past Board Meetings</a></li>
</ul></li>
</ul></li>
<li class="expanded"><a href="http://gab.wi.gov/campaign-finance" title="Campaign Finance">Campaign Finance</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/campaign-finance/cfis" title="Campaign Finance Information System">Campaign Finance Information System</a></li>

<li class="leaf"><a href="http://gab.wi.gov/campaign-finance/registration-state" title="Registration-State">State Candidates &amp; Committees</a></li>
<li class="leaf"><a href="http://gab.wi.gov/campaign-finance/registration-local" title="Registration-Local">Local Candidates &amp; Committees</a></li>
<li class="leaf"><a href="http://gab.wi.gov/campaign-finance/limits-deadlines" title="Campaign Finance Limits and Deadlines">Limits &amp; Deadlines</a></li>
<li class="leaf"><a href="http://gab.wi.gov/campaign-finance/laws-guidance" title="Campaign Finance Legal Resources">Legal Resources</a></li>
<li class="expanded last"><a href="http://gab.wi.gov/campaign-finance/public-funding" title="Public Funding">Public Funding</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/campaign-finance/public-funding/democracytrustfund" title="Democracy Trust Fund Information">Supreme Court</a></li>

<li class="leaf last"><a href="http://gab.wi.gov/campaign-finance/public-funding/wecf" title="">Wis. Election Campaign Fund</a></li>
</ul></li>
</ul></li>
<li class="expanded"><a href="http://gab.wi.gov/elections-voting" title="Elections &amp; Voting">Elections &amp; Voting</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/elections-voting/recount" title="Information about the Supreme Court Recount">Recount</a></li>
<li class="expanded"><a href="http://gab.wi.gov/elections-voting/voters" title="Voters">Voters</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/elections-voting/voters/registration-voting" title="Registration and Voting">Registration and Voting</a></li>
<li class="leaf"><a href="http://gab.wi.gov/elections-voting/voters/absentee" title="Absentee Voting">Absentee Voting</a></li>
<li class="leaf"><a href="http://gab.wi.gov/elections-voting/voters/accessibility" title="Accessibility">Accessibility</a></li>
<li class="leaf"><a href="http://gab.wi.gov/elections-voting/voters/military-overseas" title="Military and Overseas Voters">Military &amp; Overseas Voters</a></li>

<li class="leaf last"><a href="http://gab.wi.gov/elections-voting/voters/become-a-poll-worker" title="Become a Poll Worker">Become a Poll Worker</a></li>
</ul></li>
<li class="expanded"><a href="http://gab.wi.gov/elections-voting/candidates" title="Candidates &amp; Committees">Candidates - Getting on the Ballot</a><ul class="menu"><li class="expanded first"><a href="http://gab.wi.gov/elections-voting/candidates/local" title="Local Candidates">Local Candidates</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/elections-voting/candidates/local/partisan" title="Local Candidates - Partisan">Partisan</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/elections-voting/candidates/local/non-partisan" title="Local Candidates - Non-Partisan">Non-Partisan</a></li>
</ul></li>
<li class="expanded"><a href="http://gab.wi.gov/elections-voting/candidates/state" title="State Candidates">State Candidates</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/elections-voting/candidates/state/partisan" title="State Candidates - Partisan">Partisan</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/elections-voting/candidates/state/non-partisan" title="State Candidates - Non-Partisan">Non-Partisan</a></li>
</ul></li>
<li class="leaf last"><a href="http://gab.wi.gov/elections-voting/candidates/federal" title="Federal Candidates">Federal Candidates</a></li>

</ul></li>
<li class="leaf"><a href="http://gab.wi.gov/elections-voting/results" title="Elections &amp;  Election Results">Elections &amp; Election Results</a></li>
<li class="leaf"><a href="http://gab.wi.gov/elections-voting/recall" title="Information about Recall Elections">Recall</a></li>
<li class="leaf"><a href="http://gab.wi.gov/elections-voting/voting-equipment" title="Voting Equipment">Voting Equipment</a></li>
<li class="leaf"><a href="http://gab.wi.gov/elections-voting/integrity" title="Election Integrity">Election Integrity</a></li>
<li class="leaf"><a href="http://gab.wi.gov/elections-voting/legal-resources" title="Election Administration Legal Resources">Legal Resources</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/elections-voting/statistics" title="Elections &amp; Voting Statistics">Statistics</a></li>
</ul></li>
<li class="expanded"><a href="http://gab.wi.gov/ethics" title="Ethics">Ethics</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/ethics/economic-interests" title="Candidates and Officials Economic Interests">Statements of Economic Interests</a></li>

<li class="leaf"><a href="http://gab.wi.gov/ethics/standards" title="Standards of Conduct">Standards of Conduct</a></li>
<li class="leaf"><a href="http://gab.wi.gov/ethics/advice" title="Seek Ethics Advice">Seek Advice</a></li>
<li class="leaf"><a href="http://gab.wi.gov/ethics/enforcement" title="Enforcement History">Enforcement History</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/about/opinions/ethics" title="">Ethics Opinions</a></li>
</ul></li>
<li class="expanded"><a href="http://gab.wi.gov/lobbying" title="Lobbying">Lobbying</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/lobbying/view-database" title="Eye on Lobbying">Eye on Lobbying</a></li>
<li class="expanded"><a href="http://gab.wi.gov/lobbying/register-report" title="Register &amp; Report Lobbying Activities">Register &amp; Report</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/lobbying/register-report/registration" title="Lobbying Registration">Registration</a></li>

<li class="leaf last"><a href="http://gab.wi.gov/lobbying/register-report/report" title="Lobbying Reporting">Reporting</a></li>
</ul></li>
<li class="leaf"><a href="http://gab.wi.gov/lobbying/law-guidance" title="Lobbying Laws &amp; Guidance">Legal Resources</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/lobbying/training" title="Lobbying Training">Training</a></li>
</ul></li>
<li class="expanded last active-trail"><a href="http://gab.wi.gov/clerks" title="Clerks">Clerks</a><ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/clerks/recent-communications" title="Recent Clerk Communications">Recent Clerk Communications</a></li>
<li class="leaf"><a href="http://gab.wi.gov/clerks/guidance" title="Guidance">Guidance on Election Processes</a></li>
<li class="leaf"><a href="http://gab.wi.gov/clerks/notices" title="Election Notices">Election Notices</a></li>
<li class="leaf"><a href="http://gab.wi.gov/clerks/education-training" title="Training">Education/Training</a></li>

<li class="leaf"><a href="http://gab.wi.gov/clerks/svrs" title="Statewide Voter Registration System">SVRS - Statewide Voter Registration System</a></li>
<li class="leaf"><a href="http://gab.wi.gov/clerks/laws-guidance" title="Election Laws &amp; Guidance">Legal Resources</a></li>
<li class="leaf last active-trail"><a href="http://gab.wi.gov/clerks/directory" title="Directory of Wisconsin Clerks" class="active">Directory of Wisconsin Clerks</a></li>
</ul></li>
</ul>                          </div> <!-- /inner -->
          </div> <!-- /primary || superfish -->

    </div> <!--/#header -->


    <div id="main" class="clear-block no-header-blocks">

      <div id="content"><div id="content-inner">

                        <c:import url="${content}">
                            <c:param name="cStateId" value="55" />
                            <c:param name="cStateAbbr" value="WI" />
                            <c:param name="cStateName" value="Wisconsin" />
                        </c:import>


      </div></div> <!-- /#content-inner, /#content -->

              <div id="sidebar-left" class="region region-left">
          <div id="block-search-0" class="block block-search region-odd even region-count-1 count-2">
  <div class="block-inner">

          <h2 class="block-title">Search</h2>

    <div class="block-content">

      <div class="block-content-inner">
        <form action="http://gab.wi.gov/clerks/directory"  accept-charset="UTF-8" method="post" id="search-block-form">
<div><div class="container-inline">
  <div class="form-item" id="edit-search-block-form-1-wrapper">
 <label for="edit-search-block-form-1">Search this site: </label>
 <input type="text" maxlength="128" name="search_block_form" id="edit-search-block-form-1" size="15" value="" title="Enter the terms you wish to search for." class="form-text" />
</div>
<input type="submit" name="op" id="edit-submit" value="Search"  class="form-submit" />
<input type="hidden" name="form_build_id" id="form-18beea8addcdb63f02a494edd36882aa" value="form-18beea8addcdb63f02a494edd36882aa"  />
<input type="hidden" name="form_id" id="edit-search-block-form" value="search_block_form"  />
</div>

</div></form>
      </div>
    </div>

  </div>
</div> <!-- /block --><div id="block-menu-secondary-links" class="block block-menu region-even odd region-count-2 count-3">
  <div class="block-inner">

          <h2 class="block-title">Resources</h2>


    <div class="block-content">
      <div class="block-content-inner">
        <ul class="menu"><li class="leaf first"><a href="http://gab.wi.gov/complaints" title="Complaints">Complaints</a></li>
<li class="leaf"><a href="http://gab.wi.gov/contract-sunshine" title="Contract Sunshine">Contract Sunshine</a></li>
<li class="leaf"><a href="http://gab.wi.gov/calendar" title="">Calendar</a></li>
<li class="leaf"><a href="http://gab.wi.gov/contact" title="Contact Us">Contact Us</a></li>
<li class="leaf"><a href="http://gab.wi.gov/faq" title="">FAQs</a></li>
<li class="leaf"><a href="http://gab.wi.gov/forms" title="Forms">Forms</a></li>

<li class="leaf"><a href="http://gab.wi.gov/guidelines" title="">Guidelines</a></li>
<li class="leaf"><a href="http://gab.wi.gov/legal-resources" title="Legal Resources">Legal Resources</a></li>
<li class="leaf"><a href="http://gab.wi.gov/links" title="Links">Links</a></li>
<li class="leaf"><a href="http://gab.wi.gov/news">News &amp; Notices</a></li>
<li class="leaf"><a href="http://gab.wi.gov/publications" title="Publications">Publications</a></li>
<li class="leaf last"><a href="http://gab.wi.gov/training" title="Training">Training</a></li>
</ul>      </div>
    </div>

  </div>
</div> <!-- /block --><div id="block-views-upcoming_events-block_1" class="block block-views region-odd even region-count-3 count-4">
  <div class="block-inner">

          <h2 class="block-title">Upcoming Events</h2>

    <div class="block-content">
      <div class="block-content-inner">
        <div class="view view-upcoming-events view-id-upcoming_events view-display-id-block_1 view-dom-id-1">




      <div class="view-content">
      <div class="item-list">
    <ul>
          <li class="views-row views-row-1 views-row-odd views-row-first">
  <div class="views-field-title">
                <span class="field-content"><a href="http://gab.wi.gov/about/meetings/2011/may">GAB May 2011 Meeting</a></span>
  </div>

  <div class="views-field-field-time-value">
                <span class="field-content"><span class="date-display-single">May 17, 2011 - 9:30am</span></span>

  </div>
</li>
          <li class="views-row views-row-2 views-row-even">
  <div class="views-field-title">
                <span class="field-content"><a href="http://gab.wi.gov/node/1841">Special Board Meeting</a></span>
  </div>

  <div class="views-field-field-time-value">
                <span class="field-content"><span class="date-display-single">May 23, 2011 - 9:30am</span></span>
  </div>

</li>
          <li class="views-row views-row-3 views-row-odd views-row-last">
  <div class="views-field-title">
                <span class="field-content"><a href="http://gab.wi.gov/node/1458">G.A.B. Closed</a></span>
  </div>

  <div class="views-field-field-time-value">
                <span class="field-content"><span class="date-display-single">May 30, 2011 - 7:30am</span></span>
  </div>
</li>

      </ul>
</div>    </div>




      <div class="view-footer">
      <p><a href="http://gab.wi.gov/events">More ...</a></p>
    </div>


</div>       </div>
    </div>

  </div>
</div> <!-- /block --><div id="block-block-1" class="block block-block region-even odd region-count-4 count-5">
  <div class="block-inner">

          <h2 class="block-title">Welcome to our new website</h2>

    <div class="block-content">
      <div class="block-content-inner">
        <p>We hope you find our new site easy to use and navigate.</p>

<p><a href="../../../../../../node/1029">Click here</a> for more information about the new site.&nbsp;</p>
<p><a href="mailto:gab@wi.gov?subject=Question%20from%20G.A.B.%20web%20site">E-mail us </a>with your questions or comments.</p>
      </div>
    </div>

  </div>
</div> <!-- /block -->        </div> <!-- /#sidebar-left -->



    </div> <!-- #main -->

    <div id="footer" class="region region-footer">
              <div id="footer-message">
          <p>&nbsp;</p>
<p>Wisconsin Government Accountability Board | 212 East Washington Avenue, Third Floor P.O. Box 7984 | Madison, Wisconsin 53707-7984</p>
<p><em>tele</em> (608) 266-8005 | <em>fax</em> (608) 267-0500 | <em>tty </em>1-800-947-3529 | <em>e-mail</em> gab@wi.gov</p>

<p>Toll-Free Voter Help Line: 1-866-VOTE-WIS</p>        </div> <!-- /#footer-message -->
    </div> <!-- /#footer -->

  </div> <!--/#page -->


  

</body>

</html>