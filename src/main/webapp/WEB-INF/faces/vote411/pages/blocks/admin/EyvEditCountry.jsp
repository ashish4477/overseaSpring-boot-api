<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jun 20, 2008
  Time: 6:15:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div id="eod-corrections" class="column-form">
<div class="hd">
    <h2>Express Your Vote : Edit Country Description</h2>
</div>

<div class="bd">
<form action="<c:url value="/admin/EyvEditCountry.htm"/>" name="eodForm" method="post" id="eodForm">
<input type="hidden" name="countryId" value="${fedexCountry.id}"/>

<table>
<tr class="region-name">
    <th>
        Country Name
    </th>
    <td>
        <spring:bind path="fedexCountry.name">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>
<tr class="region-name">
    <th>
        Pick Up
    </th>
    <td>
        <spring:bind path="fedexCountry.pickup">
            <script type="text/javascript">
                // god, this is such a hack. stupid spring. - nick
                YAHOO.util.Event.addListener("YAHOO-bc-${status.expression}",'change', function() {
                    document.getElementById("${status.expression}").value = (this.checked) ? 'true' : 'false';
                });
            </script>
            <input type="hidden" id="${status.expression}" name="${status.expression}" value="${status.value}" />

            <c:choose>
                <c:when test="${status.value}">
                    <input type="checkbox" id="YAHOO-bc-${status.expression}" checked="checked" />
                </c:when>
                <c:otherwise>
                    <input type="checkbox" id="YAHOO-bc-${status.expression}" />
                </c:otherwise>
            </c:choose>
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>
<tr class="region-name">
    <th>
        Drop Off
    </th>
    <td>
        <spring:bind path="fedexCountry.dropoff">
            <script type="text/javascript">
                // god, this is such a hack. stupid spring. - nick
                YAHOO.util.Event.addListener("YAHOO-bc-${status.expression}",'change', function() {
                    document.getElementById("${status.expression}").value = (this.checked) ? 'true' : 'false';
                });
            </script>
            <input type="hidden" id="${status.expression}" name="${status.expression}" value="${status.value}" />

            <c:choose>
                <c:when test="${status.value}">
                    <input type="checkbox" id="YAHOO-bc-${status.expression}" checked="checked" />
                </c:when>
                <c:otherwise>
                    <input type="checkbox" id="YAHOO-bc-${status.expression}" />
                </c:otherwise>
            </c:choose>
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        Customer Service phone
    </th>
    <td>
        <spring:bind path="fedexCountry.servicePhone">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        Country FedEx site
    </th>
    <td>
        <spring:bind path="fedexCountry.fedexUrl">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>


<tr class="region-name">
    <th>
        Customer Service URL
    </th>
    <td>
        <spring:bind path="fedexCountry.serviceUrl">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>


<tr class="region-name">
    <th>
        Drop off Locator URL
    </th>
    <td>
        <spring:bind path="fedexCountry.dropoffUrl">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        T&amp;C URL
    </th>
    <td>
        <spring:bind path="fedexCountry.tcUrl">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        Rate (USD)
    </th>
    <td>
        <spring:bind path="fedexCountry.rate">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        Local Currency name
    </th>
    <td>
        <spring:bind path="fedexCountry.localCurrencyName">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>


<tr class="region-name">
    <th>
        Rate in local currency
    </th>
    <td>
        <spring:bind path="fedexCountry.exchangeRate">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        Transit Time to the USA
    </th>
    <td>
        <spring:bind path="fedexCountry.deliveryTime">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        Billing account #
    </th>
    <td>
        <spring:bind path="fedexCountry.accountNumber">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        Last Date (format: &quot;10-27-2008 10:30 pm&quot;)
    </th>
    <td>
        <spring:bind path="fedexCountry.lastDate">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        ZIP code pattern
    </th>
    <td>
        <spring:bind path="fedexCountry.zipPattern">
            <input type="text" name="${status.expression}" value="${status.value}" />
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr class="region-name">
    <th>
        Country Group
    </th>
    <td>
        <spring:bind path="fedexCountry.group">
            <select name="${status.expression}">
                <option value="LAC" <c:if test="${status.value eq 'LAC'}">selected="selected"</c:if> >LAC</option>
                <option value="CANADA" <c:if test="${status.value eq 'CANADA'}">selected="selected"</c:if> >CANADA</option>
                <option value="APAC" <c:if test="${status.value eq 'APAC'}">selected="selected"</c:if> >APAC</option>
                <option value="EMEA" <c:if test="${status.value eq 'EMEA'}">selected="selected"</c:if> >EMEA</option>
            </select>
            <c:if test="${status.error}">
                <span>${status.errorMessage}</span>
            </c:if>
        </spring:bind>
    </td>
</tr>

<tr>
    <td>
        <input type="hidden" name="submission" value="true"/>
        <input type="hidden" name="save" value="Save"/>
        <a href="<c:url value="/admin/EyvCountryList.htm"/>">
            &larr; Return to Country List
        </a>
    </td>
    <td>
        <input type="image" src="<c:url value="/img/buttons/continue-button.gif"/>"/>
    </td>
</tr>

</table>

</form>
</div>
<div class="ft"></div>
</div>