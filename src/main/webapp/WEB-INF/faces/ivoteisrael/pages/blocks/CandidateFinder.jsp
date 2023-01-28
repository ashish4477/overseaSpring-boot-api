<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="candidateFinder" class="row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
	<h1 class="title">${title}</h1> 
	<ul>
	    <li>Identify your Congressional District</li>
	    <li>Identify Congressional candidates in your district and their party affiliations</li>
	    <li>See details for individual Congressional candidates</li>
	</ul>
	<p>
		<strong>Enter Your U.S. Voting Address to Get Started:</strong>
		<span class="rava-bubble">
			Your U.S. voting residence address is the last place that you last established a residence (domicile).
	    	<br /><br/>
	    	As an overseas absentee or military voter, you do not need to own or be living at that address, or sending mail there for it to be your voting residence address. It does not matter where your parents and relatives live or once lived, your voting residence address is about where you lived only.
		</span>
	</p>
			
    <spring:bind path="candidateFinder.*">
        <c:if test="${status.error}">
        	<div class="error-indicator">
            <c:forEach items="${status.errorMessages}" var="errMsg" >
                <p>${errMsg}</p>
            </c:forEach>
            </div>
        </c:if>
    </spring:bind>

		<form role="form" action="<c:url value='/CandidateFinder.htm'/>"
			name="candidateFinder" method="post" id="candidateFinder"
			class="who-can-use-fwab">

      <div class="row">
        <div class="col-xs-12">
				<spring:bind path="candidateFinder.address.street1">
					<c:set var="error" value="" />
					<c:if test="${status.error}">
						<%--<p class="error">${status.errorMessage}</p>--%>
						<c:set var="error" value="error-indicator" />
					</c:if>
          <div class="form-group">

						<label class="${error}" for="${status.expression}">Address
							1*</label>
						<input  id="street" name="${status.expression}" id="${status.expression}"
							type="text" class="form-control"
							value="<c:out value='${status.value}'/>" />
				</spring:bind>
             </div>
        <div class="form-group">
				<spring:bind path="candidateFinder.address.street2">
					<c:set var="error" value="" />
					<c:if test="${status.error}">
						<%--<p class="error">${status.errorMessage}</p>--%>
						<c:set var="error" value="error-indicator" />
					</c:if>
						<label class="${error}" for="${status.expression}">Address
							2</label>
						<input name="${status.expression}" id="${status.expression}"
							type="text" class="form-control"
							value="<c:out value='${status.value}'/>" />
				</spring:bind>
        </div>
        </div>
      </div>
         <div class="row">
         <div class="col-xs-12 col-sm-6">
         <div class="form-group">
				<spring:bind path="candidateFinder.address.city">
					<c:set var="error" value="" />
					<c:if test="${status.error}">
						<%--<p class="error">${status.errorMessage}</p>--%>
						<c:set var="error" value="error-indicator" />
					</c:if>

          <label class="${error}" for="${status.expression}">City/Town*</label>
          <input id="city" name="${status.expression}" id="${status.expression}"
							type="text" class="form-control"
							value="<c:out value='${status.value}'/>" />

				</spring:bind>
              </div></div>
        <div class="col-xs-4 col-sm-2">
          <div class="form-group">

				<spring:bind path="candidateFinder.address.state">
					<c:set var="error" value="" />
					<c:if test="${status.error}">
						<%--<p class="error">${status.errorMessage}</p>--%>
						<c:set var="error" value="error-indicator" />
					</c:if>
          <label class="${error}" for="state"> State* </label>
          <input class="form-control" type="text" id="state" value="${state.abbr}" name="${status.expression}"/>

				</spring:bind>
        </div>
        </div>

        <div class="col-xs-6 col-sm-3">
          <div class="form-group">

				<spring:bind path="candidateFinder.address.zip">
					<c:set var="error" value="" />
					<c:if test="${status.error}">
						<%--<p class="error">${status.errorMessage}</p>--%>
						<c:set var="error" value="error-indicator" />
					</c:if>

						<label class="${error}" for="${status.expression}">Zip
							Code</label>


						<input id="zipcode" name="${status.expression}" id="${status.expression}"
							type="text" class="form-control"
							value="<c:out value='${status.value}'/>" />

				</spring:bind>
         </div>
         </div>
           </div>
      <div class="form-group">
				<spring:bind path="candidateFinder.email">
					<c:set var="error" value="" />
					<c:if test="${status.error}">
						<%--<p class="error">${status.errorMessage}</p>--%>
						<c:set var="error" value="error-indicator" />
					</c:if>
						<label style="margin-right:8px;" class="${error}" for="${status.expression}">Email*</label>
						<input name="${status.expression}" id="${status.expression}" type="email" class="form-control" value="<c:out value='${status.value}'/>" />
				</spring:bind>
      </div>
      <div class="form-group">
      <div class="form-group">
				<spring:bind path="candidateFinder.confirmEmail">
					<c:set var="error" value="" />
					<c:if test="${status.error}">
						<%--<p class="error">${status.errorMessage}</p>--%>
						<c:set var="error" value="error-indicator" />
					</c:if>

						<label class="${error}" for="${status.expression}">Confirm
							Email*</label>

						<input name="${status.expression}" id="${status.expression}" type="email" class="form-control" value="<c:out value='${status.value}'/>" />
				</spring:bind>
      </div>
      <div class="form-group">
				<spring:bind path="candidateFinder.addToList">
					<c:set var="error" value="" />
					<c:if test="${status.error}">
						<%--<p class="error">${status.errorMessage}</p>--%>
						<c:set var="error" value="error-indicator" />
					</c:if>
						<label class="${error}" for="${status.expression}">
              <input name="${status.expression}" id="${status.expression}" type="checkbox" /> Yes, I would like to receive Voter Alerts</label>
				</spring:bind>
        </div>
		<input class="pull-right submit-button wizard-button" type="submit" name="Continue" value="Continue" />
          </div>
		</form>

  <script src="https://d79i1fxsrar4t.cloudfront.net/jquery.liveaddress/2.4/jquery.liveaddress.min.js"></script>
  <script>
    var liveaddress = $.LiveAddress({ key: "3222262652846318224", debug: false, autoVerify: true, invalidMessage: "That address is not valid" });
  </script>
   </div>
</div>


