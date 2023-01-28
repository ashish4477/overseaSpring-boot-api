<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Sep 4, 2008
  Time: 9:13:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<style type="text/css">
	.after-fwab .bd { padding-right: 87px !important; }
	.after-fwab .bd p { padding: 10px 0; }
	.after-fwab .bd p em { color: #c00; }
	.after-fwab .bd p.choices {
		line-height: 45px; padding: 0;
	}
	.after-fwab .bd a { display: block; height: 45px; }
	.after-fwab .bd a span { position: absolute; top: -9999px; }
	.after-fwab .bd a.yes { 
		width: 392px; 
		background: transparent url(./img/buttons/eyv-button.gif) no-repeat;
	}
	.after-fwab .bd a.no {
		width: 104px; margin-top: 69px;
		background: transparent url(./img/buttons/no-thanks-button.gif) no-repeat;
	}
</style>
<div class="wide-content after-fwab">
    <div class="hd">
		<h2 class="title">Express Your Vote</h2>
    </div>
    <div class="bd">
		<p>Express Your Vote is an innovative initiative supported by FedEx&copy; Express &mdash; providing you access to special OVF rates for FedEx&copy; service, speed, and reliability when shipping your ballot back to your election office for the 2010 General (Presidential) Election. Express Your Vote rates are highly-discounted to absolutely free, depending on the country of operation.</p>
		<p>Be confident it will be there in time to be counted and <em>Express Your Vote</em>!</p>
        <p class="choices">
			<a class="yes" href="<c:url value="/ExpressYourVote"/>"><span>Tell me more about Express Your Vote!</span></a>
			<a class="no" href="<c:url value="/home.htm"/>"><span>No thanks</span></a>
		</p>
    </div>
</div>