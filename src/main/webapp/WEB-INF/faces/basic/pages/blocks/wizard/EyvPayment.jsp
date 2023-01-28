<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 25, 2008
  Time: 4:55:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="content-pad">
    <h2>Payment Information</h2>

     <spring:bind path="expressForm.*">
        <c:if test="${status.error}">
        	<div class="error-indicator">
            <c:forEach items="${status.errorMessages}" var="errMsg" >
                <p>${errMsg}</p>
            </c:forEach>
            </div>
        </c:if>
    </spring:bind>
 
    <form action="<c:url value='ExpressYourVote.htm'/>" name="expressYourVote" method="post">
        <input type="hidden" name="submission" value="true"/>
        <div class="col-left">
            <h4>Shipper's address:</h4>
            <div class="address">
                <p>
                    <c:out value='${expressForm.firstName}'/> <c:out value='${expressForm.lastName}'/><br />
                    <c:out value='${expressForm.pickUp.street1}'/> <br />
                    <c:if test="${not empty expressForm.pickUp.street2}"><c:out value='${expressForm.pickUp.street2}'/><br/></c:if>
                    <c:out value='${expressForm.pickUp.zip}'/> <c:out value='${expressForm.pickUp.city}'/><br />
                    ${expressForm.country.name}<br />
                    <c:out value='${expressForm.notificationEmail}'/>
                </p>
            </div>

            <p style="text-align: right;"><a href="<c:url value='ExpressYourVote.htm'><c:param name="submission" value="true"/><c:param name="_target${currentStep-1}"/></c:url>">change...</a></p>

            <h2 style="float:left; margin-bottom: 8px">Your Express Your Vote<br />
                Shipping Rate is</h2><h2 style="float: right; margin-bottom: 8px"><br /><span class="date">US $<fmt:formatNumber value="${expressForm.country.rate}" maxFractionDigits="2" minFractionDigits="2" /></span></h2>

            <dl>
                <%--<dt><label for="">Card type</label></dt><dd><select name="card" id="card" class="input-select">
                  <option value="0" selected="selected">Please choose</option>
                </select></dd>--%>
                    <spring:bind path="expressForm.nameOnCard">
                        <c:set var="error" value="" />
                        <c:if test="${status.error}">
                            <%--<p class="error">${status.errorMessage}</p>--%>
                            <c:set var="error" value="error-indicator" />
                        </c:if>
                        <dt><label  class="${error}" for="${status.expression}">Name on Card*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="<c:out value='${status.value}'/>" /></dd>
                    </spring:bind>
                    <spring:bind path="expressForm.ccNumber">
                        <c:set var="error" value="" />
                        <c:if test="${status.error}">
                            <%--<p class="error">${status.errorMessage}</p>--%>
                            <c:set var="error" value="error-indicator" />
                        </c:if>
                        <dt><label  class="${error}" for="${status.expression}">Card Number*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" class="input-text" value="" maxlength="16" /></dd>
                    </spring:bind>
                    <spring:bind path="expressForm.cvv">
                        <c:set var="error" value="" />
                        <c:if test="${status.error}">
                            <%--<p class="error">${status.errorMessage}</p>--%>
                            <c:set var="error" value="error-indicator" />
                        </c:if>
                        <dt><label  class="${error}" for="${status.expression}">Security Code*</label></dt><dd><input name="${status.expression}" id="${status.expression}" type="text" maxlength="5" class="input-text" value=""  /></dd>
                    </spring:bind>
                <dt><label for="exp_month">Expiration Date</label></dt>
                <dd>
                    <spring:bind path="expressForm.ccExpiredMonth">
                        <select name="${status.expression}" id="exp_month" class="input-text" >
                            <option value="01">01</option>
                            <option value="02">02</option>
                            <option value="03">03</option>
                            <option value="04">04</option>
                            <option value="05">05</option>
                            <option value="06">06</option>
                            <option value="07">07</option>
                            <option value="08">08</option>
                            <option value="09">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                        </select>
                    </spring:bind>
                    &nbsp;/&nbsp;
                    <spring:bind path="expressForm.ccExpiredYear">
                        <select name="${status.expression}" id="exp_year" class="input-text" >
                            <option value="12">2012</option>
                            <option value="13">2013</option>
                           	<option value="14">2014</option>
                            <option value="15">2015</option>
                            <option value="16">2016</option>
                            <option value="17">2017</option>
                            <option value="18">2018</option>
                            <option value="19">2019</option>
                            <option value="20">2020</option>
                        </select>
                    </spring:bind>
                </dd>
            </dl>
        </div>

        <div class="col-right">
            <h4>Destination address:</h4>

            <div class="address">
                <p>
                    <c:out value='${expressForm.destinationLeo.physical.addressTo}'/><br />
                    <c:out value='${expressForm.destinationLeo.physical.street1}'/> <br />
                    <c:if test="${not empty expressForm.destinationLeo.physical.street2}"><c:out value='${expressForm.destinationLeo.physical.street2}'/><br/></c:if>
                    <c:out value='${expressForm.destinationLeo.physical.zip} ${expressForm.destinationLeo.physical.city}, ${expressForm.destinationLeo.physical.state}'/><br />
                    USA<br />
                    <%--<c:out value='${expressForm.destinationLeo.leoEmail}'/>--%>
                </p>
            </div>

            <h4>Billing Address (if different from above)</h4>

            <dl>
                <spring:bind path="expressForm.billing.street1">
                    <dt><label for="${status.expression}">Address 1</label></dt><dd><input id="${status.expression}" name="${status.expression}" type="text" class="input-text" value="${status.value}" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.billing.street2">
                    <dt><label for="${status.expression}">Address 2</label></dt><dd><input id="${status.expression}" name="${status.expression}" type="text" class="input-text" value="${status.value}" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.billing.city">
                    <dt><label for="${status.expression}">City/Town</label></dt><dd><input id="${status.expression}" name="${status.expression}" type="text" class="input-text" value="${status.value}" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.billing.state">
                    <dt><label for="${status.expression}">State/Province</label></dt><dd><input id="${status.expression}" name="${status.expression}" type="text" class="input-text" value="${status.value}" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.billing.zip">
                    <dt><label for="${status.expression}">Postal Code</label></dt><dd><input id="${status.expression}" name="${status.expression}" type="text" class="input-text" value="${status.value}" /></dd>
                </spring:bind>
                <spring:bind path="expressForm.billingCountry">
                <dt><label for="${status.expression}">Country</label></dt><dd><select id="${status.expression}" name="${status.expression}" class="input-select">
                <option value="">Please choose</option>
                <c:forEach items="${countriesList}" var="countryItem">
                    <c:choose>
                        <c:when test="${status.value eq countryItem.value}">
                            <option value="${countryItem.value}" selected="selected">${countryItem.value}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${countryItem.value}">${countryItem.value}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                </spring:bind>
            </select></dd>

                <dt></dt><dd class="last-row"><input type="image" src="img/buttons/continue-button.png" name="_target${currentStep+1}" /></dd>
            </dl>
        </div>
    </form>
    <div class="clear"></div>
</div>
