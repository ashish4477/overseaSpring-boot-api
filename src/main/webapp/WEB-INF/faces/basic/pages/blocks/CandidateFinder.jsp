<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="body-content wide-content column" id="candidateFinder">
	<div class="page-form">
		<div class="hd"> 
			<div class="hd-inner"> 
				<h1 class="title">${title}</h1> 
			</div> 
		</div>
		 	
		<div class="bd">
			<div class="bd-inner">
				<div class="column-right"></div>
		
				<div class="column-left">
                    <p>The OVF Candidate Finder tool is the newest of our suite of nonpartisan voter services. It's easy to use and will help you to:</p>
                    <ul>
                        <li>Identify your Congressional District</li>
                        <li>Identify Congressional candidates in your district and their party affiliations</li>
                        <li>See details for individual Congressional candidates</li>
                    </ul>
					<p>
                        <strong>Get started</strong> by entering your last U.S. residence/voting residence address
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
				
				    <form action="<c:url value='CandidateFinder.htm'/>" name="candidateFinder" method="post" id="candidateFinder" class="who-can-use-fwab">
						<dl>
						    <spring:bind path="candidateFinder.address.street1">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            <%--<p class="error">${status.errorMessage}</p>--%>
						            <c:set var="error" value="error-indicator" />
						        </c:if>
						        <dt><label class="${error}" for="${status.expression}">Address 1*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
						    </spring:bind>
						    
						    <spring:bind path="candidateFinder.address.street2">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            <%--<p class="error">${status.errorMessage}</p>--%>
						            <c:set var="error" value="error-indicator" />
						        </c:if>
						        <dt><label class="${error}" for="${status.expression}">Address 2</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
						    </spring:bind>
						    
						    <spring:bind path="candidateFinder.address.city">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            <%--<p class="error">${status.errorMessage}</p>--%>
						            <c:set var="error" value="error-indicator" />
						        </c:if>
						        <dt><label class="${error}" for="${status.expression}">City/Town*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
						    </spring:bind>
						    
						    <spring:bind path="candidateFinder.address.state">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            <%--<p class="error">${status.errorMessage}</p>--%>
						            <c:set var="error" value="error-indicator" />
						        </c:if>
						        
						     <dt><label class="${error}" for="select_state">State*</label></dt>
						     <dd>
						         <select name="${status.expression}" id="select_state" class="input-select">
						             <option value="">Choose state or territory</option>
						             <c:forEach items="${states}" var="state">
					                     <c:choose>
					                         <c:when test="${not empty candidateFinder.address.state and candidateFinder.address.state eq state.abbr}">
					                             <option value="${state.abbr}" selected="selected">${state.name}</option>
					                         </c:when>
					                         <c:otherwise>
					                             <option value="${state.abbr}">${state.name}</option>
					                         </c:otherwise>
					                     </c:choose>
						             </c:forEach>
						         </select>
						     </dd>
						        
						    </spring:bind>
						    
						    <spring:bind path="candidateFinder.address.zip">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            <%--<p class="error">${status.errorMessage}</p>--%>
						            <c:set var="error" value="error-indicator" />
						        </c:if>
						        <dt><label class="${error}" for="${status.expression}">ZIP Code</label></dt>
						        <dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
					    	</spring:bind>

						    <spring:bind path="candidateFinder.email">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            <%--<p class="error">${status.errorMessage}</p>--%>
						            <c:set var="error" value="error-indicator" />
						        </c:if>
						        <dt><label class="${error}" for="${status.expression}">Email*</label></dt>
						        <dd><input name="${status.expression}" id="${status.expression}" type="email" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
					    	</spring:bind>

						    <spring:bind path="candidateFinder.confirmEmail">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            <%--<p class="error">${status.errorMessage}</p>--%>
						            <c:set var="error" value="error-indicator" />
						        </c:if>
						        <dt><label class="${error}" for="${status.expression}">Confirm Email*</label></dt>
						        <dd><input name="${status.expression}" id="${status.expression}" type="email" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
					    	</spring:bind>


						    <%--<spring:bind path="candidateFinder.country">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            &lt;%&ndash;<p class="error">${status.errorMessage}</p>&ndash;%&gt;
						            <c:set var="error" value="error-indicator" />
						        </c:if>

                                 <dt><label class="${error}" for="select_country">Country of Residence*</label></dt>
                                 <dd>
                                     <select name="${status.expression}" id="select_country" class="input-select">
                                         <option value="0">Choose country</option>
                                         <c:forEach items="${countries}" var="country">
                                             <c:choose>
                                                 <c:when test="${not empty candidateFinder.country and candidateFinder.country eq country.id}">
                                                     <option value="${country.id}" selected="selected">${country.viewValue}</option>
                                                 </c:when>
                                                 <c:otherwise>
                                                     <option value="${country.id}">${country.viewValue}</option>
                                                 </c:otherwise>
                                             </c:choose>
                                         </c:forEach>
                                     </select>
                                 </dd>
                            </spring:bind>
--%>

						    <spring:bind path="candidateFinder.addToList">
						        <c:set var="error" value="" />
						        <c:if test="${status.error}">
						            <%--<p class="error">${status.errorMessage}</p>--%>
						            <c:set var="error" value="error-indicator" />
						        </c:if>
						        <dt></dt>
						        <dd><input name="${status.expression}" id="${status.expression}" type="checkbox" checked="<c:if test='${status.value}'>checked</c:if>" /> <label class="${error}" for="${status.expression}">Yes, I would like to receive OVF Voter Alerts</label></dd>
					    	</spring:bind>

						    <dt>&nbsp;</dt><dd class="last-row">*required field <input type="image" src="img/buttons/continue-button.png" name="_target${currentStep+1}" value=""/></dd>

				        </dl>
					</form>
				</div>
				<div class="break"></div>
			</div>			
		</div>
		<div class="ft"><div class="ft-inner"></div></div>
	</div>

</div>