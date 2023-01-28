<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="row">
  <div class="col-xs-12">
    <form:form modelAttribute="extendedProfile" action="MyProfile.htm" commandName="extendedProfile" role="form">
    <div class="row">
      <div class="col-xs-12">
      <h3>Voter Information</h3>
      <fieldset class="row">
        <div class="col-xs-12 col-sm-4">
        <label>Voter Type</label>
            <form:select path="user.voterType" multiple="false" class="form-control">
                <form:option value="" label="- Select -"/>
                <form:option value="DOMESTIC_VOTER" label="US-based Domestic Voter"/>
                <form:option value="OVERSEAS_VOTER" label="Overseas Voter"/>
                <form:option value="MILITARY_VOTER" label="Military Voter"/>
            </form:select>
        </div>
        <div class="col-xs-12 col-sm-4">
        <label>Primary Voting Method</label>
        <form:select path="votingMethod" class="form-control">
            <form:option value="">-select-</form:option>
            <c:set var="options">Polling Place,Early In Person,Absentee Vote-by-Mail,Online Absentee Ballot</c:set>
            <form:options items="${fn:split(options,',')}"/>
        </form:select>
        </div>
        <div class="col-xs-12 col-sm-4">
        <label> Political Party</label>
        <form:select path="politicalParty" class="form-control">
            <form:option value="">-select-</form:option>
            <c:set var="options">Constitution,Democrat,Green,Independent,Libertarian,Republican,Other</c:set>
            <form:options items="${fn:split(options,',')}"/>
        </form:select>
         </div>
        </fieldset>
      </div>
      </div>
      <div class="row">
      <div class="col-xs-12 participation inline-checkboxes">
          <h3>Election Participation</h3><fieldset>
          <h4>I Vote in:</h4>
          <c:set var="options"> Presidential Elections, Primary Elections, Mid-Term Elections, Special Elections, State Elections, Local/Municipal Elections</c:set>
          <form:checkboxes path="voterParticipationArr" items="${fn:split(options,',')}"/>
          <br/>
          <label>Other (please specify):</label> <form:input class="form-control" path="voterParticipationOther"/>
        </fieldset>
        </div>
      </div>
        <div class="row">
          <div class="col-xs-12">
            <h3>Voting Participation</h3>
            <fieldset class="row">
              <div class="col-xs-12 col-sm-4 inline-checkboxes">
                <h4>Voting Promotion</h4>
                <c:set var="options">Organized Voter Event,Joined Voter Reg Drive,Helped Friends Register,Reminded Friends to Register</c:set>
                <form:checkboxes path="outreachParticipationArr" items="${fn:split(options,',')}"/>
                <br/>
                <label>Other (please specify):</label> <form:input class="form-control" path="outreachParticipationOther" />
                <p>&nbsp;</p>
              </div>
            
            </fieldset>
        </div>
       </div>

        <div class="row">
          <div class="col-xs-12">

            <fieldset class="row">
              <div class="col-xs-12 col-sm-4 inline-checkboxes wrap">
                <h4>Do You Use Social Media to Promote Voter Participation?</h4>
                <c:set var="options">Facebook,Twitter,Email</c:set>
                <form:checkboxes path="socialMediaArr" items="${fn:split(options,',')}"/>
                <label style="margin-top:6px;">Other (please specify):</label> <form:input class="form-control" path="socialMediaOther" />
                <p>&nbsp;</p>
              </div>
            </fieldset>

          </div>
        </div>


        <div class="row">
          <div class="col-xs-12">

            <fieldset class="row">
              <div class="col-xs-12 col-sm-4 inline-checkboxes">
                <h4>Volunteering</h4>
                <c:set var="options">Worked at the Polls,Joined a Campaign,Supported Chosen Candidates,Organized Events</c:set>
                <form:checkboxes path="volunteeringArr" items="${fn:split(options,',')}"/>
                <br/>
                <label>Other</label> <form:input class="form-control" path="volunteeringOther" />
              </div>
            </fieldset>

          </div>
        </div>

       <div class="row">
         <div class="col-xs-12">
            <h3>Satisfaction</h3>
            <fieldset class="col-xs-12 inline-checkboxes">
            <h4>How satisfied were you with the voter registration/ballot request process for the last election?</h4>
            <c:set var="options">Very Satisfied,Satisfied,Neutral,Dissatisfied,Very Dissatisfied,</c:set>
            <form:radiobuttons path="satisfaction" items="${fn:split(options,',')}"/>
            </fieldset>
          </div>
       </div>
       <div class="row">
          <div class="col-xs-12">
            <input type="submit" class="submit pull-right" value="Save Profile">
          </div>
       </div>
    </form:form>
  </div>
</div>
