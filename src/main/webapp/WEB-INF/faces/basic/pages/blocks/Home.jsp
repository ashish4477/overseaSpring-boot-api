<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<div id="main-columns">
    <div id="rava" class="main-column home-block">
    	<c:if test="${param.msg eq 'ravaQuit'}">
		<div class="quitMsg"><h4>You have chosen to quit the application process. Please choose an option from the menu to restart the process.</h4></div>
		</c:if>
        <h2 class="title">Register to Vote</h2>
        <form action="<c:url value='/w/rava.htm'/>" method="get">
        	<input type="hidden" name="fields" value="3" />
             <fieldset>
             	<label>State-by-State<br />
					Automated Application<br />
					Assistance
				</label>
        		<input type="hidden" name="3" value="${param.cStateId}" />
				<input type="image" src="<c:url value="/img/buttons/continue-button.png"/>" alt="Continue..." />
            </fieldset>
        </form>
    </div>


    <div id="eod" class="main-column home-block">
        <h2>Election Official Directory</h2>
        <form action="<c:url value="/eod.htm"/>" method="get" name="eodForm">
            <fieldset>
                <label>Find your ${param.cStateName}<br /> local election official</label>
        		<input type="hidden" name="stateId" value="${param.cStateId}" />
        		<input type="image" src="<c:url value="/img/buttons/continue-button.png"/>" alt="Continue..." />                
            </fieldset>
        </form>
    </div>
    <div id="vhd" class="main-column home-block">
        <h2>Voter Help Desk</h2>
		<form action="https://vhd.overseasvotefoundation.org/unified/index.php" method="get">
            <input type="hidden" name="group" id="vhd-group-name" value="ovf">
			<input type="hidden" name="_m" value="knowledgebase" />
			<input type="hidden" name="_a" value="view" />
			<input type="hidden" name="pcid" value="" />
			<input type="hidden" name="nav" value="" />

			<fieldset>
				<label for="question-select">Choose your <br /> question category:</label>
				<select name="parentcategoryid" id="question-select" class="field">
					<c:import url="/WEB-INF/faces/basic/pages/statics/HelpDeskSelectOptions.jsp" />
				</select>
				<input class="button-link" type="image" id="vhd-continue"
					src="<c:url value="/img/buttons/continue-button.png"/>" alt="Continue..." />
            </fieldset>
        </form>    </div>
</div>
<div id="bottom-items">
	
	<div class="home-block" id="fwab">
		<h3><a href="<c:url value='/FwabStart.htm'/>">Federal Write-in<br/>Absentee Ballot</a></h3>
		<p><a href="<c:url value='/FwabStart.htm'/>">
				New, upgraded online ballot<br/>
		    	Candidate lists by district <br/>
				Vote on screen, print and mail<br/>
		</a></p>
	</div>
    <div class="home-block" id="svid">
        <h3><a href="<c:url value='/svid.htm'/>">State Voter<br/>Information Directory</a></h3>
		<p><a href="<c:url value='/svid.htm'/>">
			Registration Deadlines<br/>
			Delivery Options<br/>
			State Contact Information<br/>
		</a></p>
    </div>

    <div id="home-login" class="home-block">
    	<h3>My Voter Account</h3>
    	<form action="<c:url value='/Login.htm' />" method="post">
    		<div><input type="email" name="j_username" id="user-name" value="&nbsp;Enter your email address..." class="field sweep" /></div>
    		<div id="learn-more"><a href="<c:url value='/AboutMVA.htm'/>">Learn more</a> or </div>
    		<input class="button-link" type="image" id="home-login-button" src="<c:url value='/img/home/login-button.png'/>" alt="Log In" />
    	</form>
    </div>
</div>

