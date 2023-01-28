<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>

			<div class="grid_14 prefix_1 suffix_1 message">
				<h1 id="page-heading">Register to vote and request your absentee ballot using the form <span>your</span> state prefers.</h1>
        <ul class="voter-type">
			    	<li>U.S. Domestic Voters</li>
			    	<li>Overseas Citizen Voters</li>
			    	<li>Military Voters</li>
            <li><div class="submit registration"><a href="<c:url value="/voter-registration-absentee-voting.htm"/>">Get Started</a></div></li>
			  </ul>
			</div><!-- closed in mainTemplate -->
      <div class="grid_16 features outer">
        <div class="grid_14 inner">
            <div class="grid_4 eod">
              <h2>Election Official Directory</h2>
              <p>Find your Local Election Official and get local contact and <br/> address information</p>
              <a class="button" href="<c:url value='/eoddomestic.htm'/>">Find Your Official</a>
            </div>
            <div class="grid_4 svid">
              <h2>State Voting Information</h2>
               <p>View election dates &amp; deadlines and voting <br/> transmission details</p>
              <a class="button" href="<c:url value='/sviddomestic.htm'/>">View Now</a>

            </div>
            <div class="grid_4 mva"">
               <h2>My Voter <br/>Account</h2>
               <p>Get one click access to your  <br/>voting information and recent votes from your representatives
</p>              <a class="button" href="<c:url value='/CreateAccount.htm'/>">Create Account</a>

            </div>
        </div>
    </div>

			<div class="grid_16 content-bottom">
				<div class="video grid_5">
				</div>
				<div class="tools grid_11">
					<h2>Voter Tools &amp; Information</h2>
					<p>We facilitate and increase the participation of all U.S. voters in the electoral process, regardless of their geographic location, by providing public access to innovative voter registration and absentee ballot request tools and information services.</p>
					<div class="grid_5">
						<div class="eod">
						<h3><a href="<c:url value='/eoddomestic.htm'/>">Election Official Directory</a></h3>
              <ul>
                <li>Find and contact your local election official</li>
                <li>Look-up election office addresses, telephone, fax, email and websites</li>
              </ul>
						</div>
					</div>
					<div class="grid_5">
						<div class="svid">
						<h3><a href="<c:url value='/sviddomestic.htm'/>">State Voting Information</a></h3>
            <ul>
              <li>State-by-state filing deadlines</li>
              <li>Options for how to send and receive voting materials</li>

            </ul>
						</div>
					</div>
					<div class="clear"></div>
					<div class="grid_5">
						<div class="cf">
						<h3><a href="<c:url value='/CandidateFinder.htm'/>">Candidate Finder</a></h3>
						<p>Identify congressional candidates in your district and their party affiliations</p>
						</div>
					</div>
					<div class="grid_5">
						<div class="vhd">
						<h3><a href="https://vhd.usvotefoundation.org/index.php?/Default/Knowledgebase/List/" rel="iframe width:800 height:450" class="ceebox2">Voter Help Desk</a></h3>
						<p>Get immediate answers to your questions about registration and voting</p>
						</div>
					</div>
			</div>
    </div>

<div class="grid_16 content-bottom">
				<div class="video grid_6">
          <h3>How to Request Your Absentee Ballot</h3>
					<a href="https://www.youtube.com/watch?v=ZGy3yxAm-Es" class="ceebox video-link"><img src="<c:url value='/img/uocava-absentee-voting-keyframe.jpg'/>"/></a>
          <h3>The A-Z of Overseas Voting</h3>
				</div>
				<div class="grid_10">
					<h2>Overseas &amp; Military Voters</h2>
					<p>Access automated, state-specific voter registration, absentee ballot request, voter account services and data directory applications in a single online location!</p>
          <p>Save time year after year by creating a voter account and gain access to one click access to important voting information and faster form generation.</p>
          </p>
          <form method='post' class="statesList">
            <strong>Select Your State to Get Started:</strong>
            <ul id="links">
              <li><a href="#">-- Please Select --</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=AL">Alabama Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=AK">Alaska Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=AZ">Arizona Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=AR">Arkansas Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=CA">California Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=CO">Colorado Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=CT">Connecticut Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=DE">Delaware Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=FL">Florida Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=GA">Georgia Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=HI">Hawaii Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=ID ">Idaho Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=IL">Illinois Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=IN ">Indiana Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=IA">Iowa Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=KS">Kansas Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=KY">Kentucky Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=LA">Louisiana Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=ME">Maine Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=MD">Maryland Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=MA">Massachusetts Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=MI">Michigan Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=MN">Minnesota Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=MS">Mississippi Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=MO">Missouri Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=MT">Montana Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=NE">Nebraska Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=NV">Nevada Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=NH">New Hampshire Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=NJ">New Jersey Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=NM">New Mexico Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=NY">New York Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=NC">North Carolina Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=ND">North Dakota Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=OH">Ohio Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=OK">Oklahoma Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=OR">Oregon Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=PA">Pennsylvania Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=RI">Rhode Island Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=SC">South Carolina Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=SD">South Dakota Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=TN">Tennessee Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=TX">Texas Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=UT">Utah Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=VT">Vermont Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=VA">Virginia Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=WA">Washington Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=WV">West Virginia Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=WI">Wisconsin Absentee Ballot Request</a></li>
                <li><a href="/vote/voter-registration-absentee-voting.htm?vrState=WY">Wyoming Absentee Ballot Request</a></li>
            </ul>
                 &nbsp;<input type="submit" name="button" id="button" value="Go!" onclick="showURL();" />
            </form>
			</div>
</div>
  <div class="grid_16 content-bottom">
				<div class="grid_10">
					<h2>Eighth Annual Voting and Elections Summit 2014</h2>
          <p>U.S. Vote Foundation and Overseas Vote are committed to developing a network of organizations, agencies, and concerned citizens who care and attend to the issues involved with the advancement of overseas, military and domestic voting processes.</p>

          <p><strong>Save the date!</strong> The Eighth Annual Voting and Elections Summit 2014 will be held on <strong>Thursday, January 30, 2014 at the Jack Morton Auditorium</strong> in the Media and Public Relations Building at George Washington University, Washington, D.C. Stay tuned for program details. <a href='/voting-and-elections-summit'>Read More...</a></p>
          </div>
  <div class="video grid_6">
  </div>

<script>
$(function() {
  $('#links').each(function(){
    var list=$(this),
    select=$(document.createElement('select')).insertBefore($(this).hide()).change(function(){
  window.location.href=$(this).val();
  });

  $('>li a', this).each(function(){
    var option=$(document.createElement('option'))
     .appendTo(select)
     .val(this.href)
     .html($(this).html());
      if($(this).attr('class') === 'selected'){
        option.attr('selected','selected');
      }
    });
    list.remove();
  });
});
</script>