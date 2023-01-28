<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form class="output-type register-vote-request-ballot" role="form" action="<c:url value="/voter-registration-absentee-voting.htm"/>" method="post"
      id="voterRegistrationBallotRequestForm">
  <div class="column">
    <div class="col-xs-12 col-sm-10 right-side">
      <!-- <h2>Register to Vote / Request a Ballot</h2> -->
      <!-- <div id="voter-register-description">
        <p>US Vote's system will help you to generate the correct state-specific ballot request form.</p>
        <p>If your state offers an online direct option for registration or ballot request, you will be provided
          with that option and a direct link to the service.</p>
        <p>Remember if you generate a form, you will need to download and print the form, then sign and post the
          original to your election office.</p>
        <p>This process will utilize the information in your Voting Address and Information.</p>
        <p>Completed forms generated with the US Vote system will come with a customized instruction letter and
          the address of your election office. Print, sign and send the form to your election office.</p>
      </div> -->
      <br>
      <div id="voter-register-form">
        <!-- <h4>What Type of Voting Form Would You Like to Prepare?</h4> -->
        <div id="us-based-voters" class="imageleft-checkbox">
          <div class="image-left">
            <img src="/vote/img/icons/us-map.png">
          </div>
          <h5>U.S. Based Voters</h5>
          <div class="radio-container">
            <div class="radio registration">
              <label>
                <input type="radio" name="formType" value="domestic_registration" required="required"/>
                <span>Voter Registration</span>
              </label>
            </div>
          </div>
          <div class="radio-container domestic-absentee">
            <div class="radio">
              <label>
                <input type="radio" name="formType" value="domestic_absentee" required="required"/> 
                <span>Absentee Ballot Request</span>
              </label>
            </div>
          </div>
        </div>

        <div id="overseas-citizen-officers" class="imageleft-checkbox">
          <div class="image-left">
            <img src="/vote/img/icons/world-map.png">
          </div>
          <h5>Overseas Citizen Voters</h5>
          <div class="radio-container">
            <div class="radio">
              <label>
                <input type="radio" name="formType" value="rava" required="required"/>
                <span>Voter Registration & Absentee Ballot Request</span>
              </label>
            </div>
          </div>
        </div>

        <div id="military-voter" class="imageleft-checkbox">
          <div class="image-left">
            <img src="/vote/img/icons/us-flag.png">
          </div>
          <h5>Military Voters</h5>
          <div class="radio-container">
            <div class="radio">
              <label>
                <input type="radio" name="formType" value="rava" required="required"/> 
                <span>Voter Registration & Absentee Ballot Request</span>
              </label>
            </div>
          </div>
        </div>
      </div>



      <div class="fwab">
        <section class="col-xs-10">
          <footer>
            <strong>Overseas Citizens & Military Voters:</strong><br/>
            <p>
              <small>If you have already requested an absentee ballot but have not
                yet received it, <br>you can still vote by using the back-up
                <a class="link" href="/vote/FwabStart.htm">Federal Write-In Absentee Ballot (FWAB).</a> </small>
            </p>
          </footer>
        </section>
      </div>
      
      <input type="submit" name="Continue" value="Continue" class="submit-button pull-right continue-btn"/>
    </div>
  </div>
</form>


<div class="note-text">
  <section class="col-xs-10">
    <p><strong>Please Note:</strong> This website will not store your social number, birth date or driver's license number.</p>
  </section>
</div>
