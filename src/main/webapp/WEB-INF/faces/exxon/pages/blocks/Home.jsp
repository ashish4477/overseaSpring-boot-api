<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 28, 2007
  Time: 6:41:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>

<div id="main-columns">
    <div id="rava" class="main-column">
        <h2>Register to Vote Request your Ballot</h2>
        <form action="<c:url value='/w/rava.htm'/>" method="get">
        	<%--<input type="hidden" name="submission" value="true" />--%>
        	<input type="hidden" name="fields" value="3" />
             <fieldset>
             	<label>State-by-State<br />
					Automated Application<br />
					Assistance
				</label>
				<input class="button-link" type="image" src="<c:url value="/img/buttons/continue-button.gif"/>" alt="Continue..." />
            </fieldset>
        </form>
    </div>

    <!-- The following block is hidden onload and is brought into view by Javascript -->
     <%--<div id="overlay-rava" class="large-overlay" style="display: none">
        <div class="hd">
            <h2>Register to Vote</h2>
			<div id="progress-bar"></div>
            <div id="rava-close" class="close"><img src="<c:url value="/img/buttons/close.gif"/>" /></div>
        </div>
        <div class="bd">
            <iframe longdesc="A form in which there are no swallows, but you might see a starling."
                frameborder="0" scrolling="no" id="overlay-rava-iframe" src="<c:url value="/w/rava.htm"/>">
                <h2>How inconvenient!</h2>
                <p>The browser you're using doesn't seem to support frames, but you can forge ahead with your voter registration by visiting the <a href="./Rava.htm">Register to Vote Form</a>.</p>
            </iframe>
        </div>
        <div class="ft"></div>
    </div>--%>

    <div id="eod" class="main-column">
        <h2>Election Official Directory</h2>
        <form action="<c:url value="/eod.htm"/>" method="get" name="eodForm">
            <fieldset>
                <label for="eod-select">Choose the state <br />you wish to look up:</label>
                <select name="stateId" id="eod-select" class="field">
                    <option value="" selected="selected">State to look up</option>
                    <c:forEach items="${states}" var="state">
                        <option value="${state.id}">${state.name}</option>
                    </c:forEach>
                    <optgroup label=""></optgroup>
                </select>
                <a href="<c:url value="/eod.htm"/>" id="eod-continue" class="button-link" onclick="document.eodForm.submit();return false;"><img src="<c:url value="/img/buttons/continue-button.gif"/>" alt="Continue..." /></a>
            </fieldset>
        </form>
    </div>
    <div id="vhd" class="main-column">
      <h2>Voter Help Desk</h2>
      <fieldset>
      <label for="question-select">Answers to Your <br/>Frequently Asked<br/>Questions </label>
      <p><a href="https://vhd.overseasvotefoundation.org/index.php?/exxonmobilcat/Knowledgebase/List" rel="iframe width:800 height:450" class="vhd ceebox2"><img style=" margin-left: 56px; margin-top: 38px;" type="image" src="<c:url value="/img/buttons/continue-button.gif"/>" alt="Continue..." /></a></p>
      </fieldset>
        </form>
    </div>
</div>
<div id="bottom-items">
	<%--div class="home-block" id="fwab">
		<h3><a href="<c:url value="/FwabStart.htm"/>">Federal Write-in<br/>Absentee Ballot</a></h3>
		<p><a href="<c:url value="/FwabStart.htm"/>">
				New, upgraded online ballot<br/>
		    	Candidate lists by district <br/>
				Vote onscreen, print and mail<br/>
		</a></p>
	</div>
    <div class="home-block" id="svid">
        <h3><a href="<c:url value="svid.htm"/>">State Voter<br/>Information Directory</a></h3>
		<p><a href="<c:url value="svid.htm"/>">
			Registration Deadlines<br/>
			Delivery Options<br/>
			State Contact Information<br/>
		</a></p>
    </div--%>
    
    <div class="home-block" id="svid-full">
        <h3><a href="<c:url value="svid.htm"/>">State Voter Information Directory:</a></h3>
		<p><a href="<c:url value="svid.htm"/>">
			Registration Deadlines<br/>
			Delivery Options for Registrations and Ballouts<br/>
			State Contact Information<br/>
		</a></p>
    </div>
    
    <div id="home-login">
    	<h3>My Voter Account</h3>
    	<form action="<c:url value='/Login.htm' />" method="post">
    		<div><input type="email" name="j_username" id="user-name" value="&nbsp;Enter your email address" class="field sweep" /></div>
    		<input class="button-link" type="image" id="home-login-button" src="<c:url value='/img/home/login-button.png'/>" alt="Log In" />
    	</form>
    </div>
</div>
<div id="overlay-shader" style="display: none"></div>
