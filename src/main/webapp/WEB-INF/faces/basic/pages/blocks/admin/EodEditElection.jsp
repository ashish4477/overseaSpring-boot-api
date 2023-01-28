<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Oct 26, 2009
  Time: 3:09:39 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	pageContext.setAttribute("newLineChar", "\n");
%>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/admin.css"/>" />


<div id="eod-corrections" class="column-form EodEditElection">
	<div class="hd">
    <h2>${election.stateInfo.state.name} Election Deadlines </h2>
	</div>

	<div class="bd">
		<form action="<c:url value='/admin/ElectionEdit.htm'/>" name="eodForm" method="post" id="eodForm">
			<div class="sect title first">
          <fieldset>
					<label>
              <h3>Title</h3>
                <spring:bind path="election.title">
								<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
			</div>
			<div class="sect type">
					<fieldset>
						<label> <h3>Type of election</h3>
						</label>
						<spring:bind path="election.typeOfElection">
							<select name="${status.expression}">
                <c:choose>
                  <c:when test="${empty election.typeOfElection}">
										<option selected="selected">Please Select an Election Type</option>
									</c:when>
                </c:choose>
								<c:choose>
									<c:when test="${election.typeOfElection eq 'FEDERAL'}">
										<option value="FEDERAL" selected="selected">Federal</option>
									</c:when>
									<c:otherwise>
										<option value="FEDERAL">Federal</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${election.typeOfElection eq 'STATE'}">
										<option value="STATE" selected="selected">State</option>
									</c:when>
									<c:otherwise>
										<option value="STATE">State</option>
									</c:otherwise>
								</c:choose>
                <c:choose>
                    <c:when test="${election.typeOfElection eq 'LOCAL_MUNICIPAL'}">
                        <option value="LOCAL_MUNICIPAL" selected="selected">Local/Municipal</option>
                    </c:when>
                    <c:otherwise>
                        <option value="LOCAL_MUNICIPAL">Local/Municipal</option>
                    </c:otherwise>
                </c:choose>
								<c:choose>
									<c:when test="${election.typeOfElection eq 'CAUCUS'}">
										<option value="CAUCUS" selected="selected">Caucus</option>
									</c:when>
									<c:otherwise>
										<option value="CAUCUS">Caucus</option>
									</c:otherwise>
								</c:choose>
								<optgroup label=""></optgroup>
							</select>
						</spring:bind>
					</fieldset>
				</div>
			<div class="sect held-on">
					<fieldset>
						<label> <h3>Date Held</h3> <spring:bind path="election.heldOn">
								<input type="text" class='datepicker' name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
			</div>
			<div class="sect deadlines">
					<fieldset>
						<label>
              <h3>US New Voter Registration Deadline</h3>
              <spring:bind path="election.domesticRegistration">
								<input class="datepicker" type="text" name="${status.expression}" value="${election.domesticRegistration}" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
            </label>
					</fieldset>
					<fieldset>
						<label>
              <h3>U.S  Domestic Voter Ballot Request Deadline</h3>
              <spring:bind path="election.domesticBallotRequest">
								<input class="datepicker" type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
               <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
	        <fieldset>
						<label>
              <h3>U.S  Domestic Voter Ballot Return deadline</h3>
              <spring:bind path="election.domesticBallotReturn">
								<input type="text" class="datepicker" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
					<fieldset>
						<label>
              <h3>U.S  Domestic Voter Early Voting Date Range</h3>
              <spring:bind path="election.domesticEarlyVoting">
								<input type="text" class="datepicker" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
					<fieldset>
						<label>
              <h3>Overseas Voter Registration Deadline</h3>
              <spring:bind path="election.citizenRegistration">
								<input type="text" class="datepicker" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
					<fieldset>
						<label>
              <h3>Overseas Voter Ballot Request Deadline</h3>
              <spring:bind path="election.citizenBallotRequest">
								<input type="text" class="datepicker" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
					<fieldset>
						<label>
              <h3>Overseas Voter Ballot Return deadline</h3>
              <spring:bind path="election.citizenBallotReturn">
								<input type="text" class="datepicker" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
					<fieldset>
						<label>
              <h3>Military Voter Registration deadline</h3>
              <spring:bind path="election.militaryRegistration">
								<input type="text"  class="datepicker" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
					<fieldset>
						<label>
              <h3>Military Voter Ballot Request deadline</h3>
              <spring:bind path="election.militaryBallotRequest">
								<input type="text" class="datepicker" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
					<fieldset>
						<label>
              <h3>Military Voter Ballot Return deadline</h3>
              <spring:bind path="election.militaryBallotReturn">
								<input type="text" class="datepicker" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
              <button class="reset" type="button">Cancel</button>
						</label>
					</fieldset>
			</div>
			<div class="sect usnotes">
					<fieldset>
						<label>
              <h3>US Notes</h3>
              <spring:bind path="election.domesticNotes">
						<textarea rows="12" cols="35" name="${status.expression}"><c:out value='${status.value}' /></textarea>
							</spring:bind>
						</label>
					</fieldset>
			</div>
			<div class="sect notes">
      <fieldset>
        <label>
        <h3>Overseas Notes</h3>
        <spring:bind path="election.notes">
        <textarea rows="12" cols="35" name="${status.expression}"><c:out value='${status.value}' /></textarea>
          </spring:bind>
        </label>
      </fieldset>
			</div>
			<div class="sect order-ticket">
					<fieldset>
						<label>
              <h3>Order</h3>
              <spring:bind path="election.order">
							<input type="text" name="${status.expression}" value="<c:out value='${status.value}'/>" />
							</spring:bind>
						</label>
					</fieldset>
				</div>

      <nav role="navigation">
        <ul class="nav navbar-nav">
          <input type="hidden" name="stateId" value="${election.stateInfo.state.id}" />
          <input type="hidden" name="electionId" value="${election.id}"/>
          <li class="first leaf"><button type="button"><a href="<c:url value="/admin/SvidEdit.htm"><c:param name="stateId" value="${election.stateInfo.state.id}"/></c:url>">
					&larr; Return to ${election.stateInfo.state.name} </a></button></li>
          <c:if test="${election.id > 0}"><li class="pull-left">
          <button name="delete" value="true">Delete Election</button>
          </li>
				</c:if>
        <li><button name="save" value="true">Save Changes</button></li>
        </ul>
      </nav>

		</form>
	</div>
	<div class="ft"></div>
</div>


