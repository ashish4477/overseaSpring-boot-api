<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jul 3, 2007
  Time: 7:05:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<div id="login-form" class="row">
  <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-10 col-md-offset-1 col-lg-10 col-lg-offset-1">
    <c:if test="${not empty param.login_error}">
      <div class="text-center alert alert-danger">
          <p class="error mb-0">Sorry, you entered incorrect login or password.</p>
      </div>
    </c:if>
      <div class="row">
          <div class="new-login-notification">The Overseas Vote - Voter Account login and functions for overseas and military voters
              are now available on our new U.S. Vote Foundation website.</div>
      </div>
      <div class="row usvote-login-block">
          <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
              <div class="voter-account-title">
                <h4>Please login on the new site.</h4>
              </div>
          </div>
          <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 voter-box">
              <div class="voter-account-branding">
                  <span class="img pull-left"><a href="https://www.usvotefoundation.org/vote/Login.htm" data-uw-rm-brl="false"><img src="https://www.usvotefoundation.org/themes/custom/usvf/images/ovf_logo.png" alt="Check with U.S. Flag overlay" data-uw-rm-ima-original="check with u.s. flag overlay"></a></span>
                  <span class="text pull-left"><a href="https://www.usvotefoundation.org/vote/Login.htm" data-uw-rm-brl="false">Overseas<br role="presentation" data-uw-rm-sr="">Voter Account<br role="presentation" data-uw-rm-sr="">Login</a></span>
                  <div style="clear: both;"></div>
              </div>
              <div class="voter-account-links">
                  <a href="https://www.usvotefoundation.org/vote/CreateAccount.htm" class="register-link" data-uw-rm-brl="false">Create Account</a>
              </div>
          </div>
      </div>
      <div class="row">
        <div class="election-information col-xs-12">
            <div class="election-information-header">
                <h3>Get Your Voting & Election Information All in One Place</h3>
                <p>Your Voter Account can help you stay informed and cultivate your 'personal democracy'</p>
            </div>
            <div class="center-block election-information-features">
                <ul id="features">
                  <li><span class="glyphicon glyphicon-ok"></span> Your account moves with you and informs you as a voter</li>
                  <li><span class="glyphicon glyphicon-ok"></span> Get the latest fact-checked news and information</li>
                  <li><span class="glyphicon glyphicon-ok"></span> See your elected officials and check their legislative history</li>
                  <li><span class="glyphicon glyphicon-ok"></span> Quick access to local election office contact information</li>
                </ul>
            </div>
        </div>
      </div>
	</div>
  </div>
</div>
