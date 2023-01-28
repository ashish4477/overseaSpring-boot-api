<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<div class="page-content">
	<div class="hd">
		<div class="hd-inner">
			<h2 class="title">Create your account today and save time each election cycle...</h2>
		</div>
	</div>
	<div class="bd">
		<div class="bd-inner">
		<ul>
			<li>
				Easy registration/ballot request form reprinting
			</li>
			<li>
				Quick write-in ballot access
			</li>
			<li>
				100% secure and private
			</li>
		</ul>
		<p>
			To create your account, you must begin or complete the <a href="<c:url value='/w/rava.htm'/>">Register to Vote</a> process or to request your <a href="<c:url value='/FwabStart.htm'/>">Federal Write-in Absentee Ballot</a>. You will find these options on our home page.
		</p>
		<p>
			At the end of data entry or at any time during the registration or ballot creation process, you can choose the &quot;<strong>Save or Stop</strong>&quot; button and you will be given an opportunity to save your information by creating a Voter Account.
		</p>
		<p>
			If you do choose to create a Voter Account, you will be prompted to create login credentials and your data will be saved for your future use.
		</p>
		<p>
			If you proceed through the registration/ballot request or write-in ballot creation process to the end, you can download your form and then choose to save your information in My Voter Account.
		</p>
		<p>
			Note: highly confidential data such as personal identification and complete birthdate will NOT be saved even in your very own Voter Account as an extra security measure. When reprinting a form through the My Voter Account service, you will be asked to re-enter this information.
		</p>
		<p>
			Click on an option to begin:
		</p>
		<ul>
			<li>
				<a href="<c:url value='/w/rava.htm?fields=3&3=${param.cStateId}&submission=true'/>">Register to Vote</a>
			</li>
			<li>
				<a href="<c:url value='/FwabStart.htm?fields=3&3=${param.cStateId}'/>">Print a Federal Write-in Absentee Ballot</a>
			</li>
		</ul>
	</div>
</div>
</div><!-- end bd -->
