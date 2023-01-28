<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: May 20, 2008
  Time: 7:53:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>



<div>
    <div class="hd">
        <h2>Test &quot;Authorize.net&quot;</h2>
    </div>
    <div class="bd">
        <form action="<c:url value="/test/TestPayment.htm"/>" method="post">
            <fieldset>
                <label class="oneline">
                    <span>Enter amount:</span>
                    <spring:bind path="cardForm.amount">
                        <input name="${status.expression}" value="${status.value}"/>
                    </spring:bind>
                </label>
                <label class="oneline">
                    <span>Enter your Credit Card number:</span>
                    <spring:bind path="cardForm.cardNum">
                        <input name="${status.expression}" value="${status.value}"/>
                    </spring:bind>
                </label>
                <label class="oneline">
                    <span>Enter Credit Card expired date:</span>
                    <spring:bind path="cardForm.cardExpiredMonth">
                        <select name="${status.expression}">
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
                    <spring:bind path="cardForm.cardExpiredYear">
                        <select name="${status.expression}">
                            <option value="10">2010</option>
                            <option value="11">2011</option>
                            <option value="12">2012</option>
                            <option value="13">2013</option>
                            <option value="14">2014</option>
                            <option value="15">2015</option>
                            <option value="16">2016</option>
                            <option value="17">2017</option>
                        </select>
                    </spring:bind>
                </label>
            </fieldset>
            <fieldset id="continue">
                <input type="image" src="<c:url value="/img/buttons/continue-button.gif"/>" />
            </fieldset>
        </form>
    </div>
</div>