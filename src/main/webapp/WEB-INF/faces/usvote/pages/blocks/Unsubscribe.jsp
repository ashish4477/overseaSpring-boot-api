<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 10, 2007
	Time: 4:09:39 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<div id="voter-account-page" class="voter-account-page page-form body-content content column wide-content">
  <div class="hd">
    <div class="hd-inner">
      <h3 class="title">Unsubscribe from Mailing List</h3>
    </div>
  </div>
  <div class="bd">
    <div class="bd-inner">
      <p>You have been ${success} unsubscribed from our mail list.</p>

      <p>We enjoyed every minute of having you with us.  If you ever want to get back onto our
        mailing list, we will welcome you back with open arms.  Good luck with whatever your
        future holds for you.</p>

      <p>Best Regards,</p>

      <p>US Vote</p>
    </div>
  </div>

  <div class="ft"><div class="ft-inner"></div></div>

</div>
