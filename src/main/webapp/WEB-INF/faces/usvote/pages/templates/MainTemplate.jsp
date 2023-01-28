<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<link rel="shortcut icon" href="<c:url value="/img/favicon.ico"/>"/>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta name="google-site-verification" content="OvhbVlwiAZVLygiKV71u6weoELJDhjJ75RaFyiGN66Q" />
	<title>${not empty customTitle ? customTitle : 'U.S. Vote Foundation | '.concat(title)}</title>
	<c:if test="${not empty metaDescription }">
	<meta name="description" content="${metaDescription}">
	</c:if>
	<!-- Google tag (gtag.js) --> <script async src="https://www.googletagmanager.com/gtag/js?id=G-BT0B5YVWV2"></script> <script> window.dataLayer = window.dataLayer || []; function gtag(){dataLayer.push(arguments);} gtag('js', new Date()); gtag('config', 'G-BT0B5YVWV2'); </script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/connection/connection-min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/element/element-min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.2.2/font/bootstrap-icons.css">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/main.css"/>"/>
	<!-- page-specific stylesheet -->
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="${sectionCss}"/>"/>
	<c:if test="${not empty externalCss }">
		<link rel="stylesheet" type="text/css" media="screen" href="<c:out value="${externalCss}"/>"/>
	</c:if>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
	<script>
		$(function () {
			if ($('.uibutton')[0] || $('.uitooltip')[0]) {
				/*** Handle jQuery plugin naming conflict between jQuery UI and Bootstrap ***/
				$.widget.bridge('uibutton', $.ui.button);
				$.widget.bridge('uitooltip', $.ui.tooltip);
			}

			$(".usvote-menu-toggle").click(function(){
				$('#navbar-main').slideToggle();
			});
			$(".container-fluid").click(function(){
				$('#navbar-main').slideUp('slow');
			});
			$(".account-sub-nav-header ul li").click(function(){
				$('li').removeClass("active");
				$(this).addClass("active");
			});

			$(".usvote-menu-mobile-toggle").click(function(){
				$('#mobile-navbar-main').slideToggle('slow', function (){
					$(".usvote-menu-mobile-toggle").toggleClass('mobile-menu-active', $(this).is(':visible'));
				});
			});

			$('#mobile-block-usvf-main-menu ul.navbar-nav li.nav-item span.nav-link').click(function () {
				var $this = $(this);
				var $parentLi = $this.parent('li.nav-item');
				var $liSiblings = $parentLi.siblings('li');
				var $childUl = $this.siblings('ul');

				if ($childUl.is(':visible')) {
					$childUl.hide();
				} else {
					$liSiblings.find('ul').hide();
					$childUl.show();
				}
			});

			$('.account_settings .mobile-account-sub-nav-header1, .voter_wrapper .mobile-account-sub-nav-header2').click(function () {
				var $this = $(this);
				var $parentLi = $this.parent('.account_settings, .voter_wrapper');
				var $liSiblings = $parentLi.siblings('.account_settings, .voter_wrapper');
				var $childUl = $this.siblings('ul');

				if ($childUl.is(':visible')) {
					$childUl.hide();
					$(this).removeClass('active-left-nav');
				} else {
					$liSiblings.find('ul').hide();
					$childUl.show();
					$(this).addClass('active-left-nav');
				}
			});

			jQuery("#selectbox").change(function () {
		        location.href = jQuery(this).val();
		    });

		    $('.modal .close').click(function () {
                $('.modal').modal('hide');
            });

		});
	</script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
	<script src="<c:url value="/js/bc-lib.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/bc-jquery.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/js/ovf.js"/>" type="text/javascript"></script>
</head>
<c:choose>
	<c:when test="${not empty userDetails and userDetails.id ne 0}">
		<c:set var="logged" value=" logged-in "/>
	</c:when>
	<c:otherwise>
		<c:set var="logged" value=" not-logged-in "/>
	</c:otherwise>
	</c:choose>
	<c:set var="baseURL" value="https://${pageContext.request.serverName}" />
	<body class="html not-front ${sectionName}${logged}">
	<!-- Google Tag Manager (noscript) -->
	<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-K3DD34"
	height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
	<!-- End Google Tag Manager (noscript) -->
	<div id="skip-link" style="display:none;">
		<a href="#main-content" class="element-invisible element-focusable">Skip to main content</a>
	</div>
	<c:import url="/WEB-INF/${relativePath}/pages/statics/Header.jsp" />
	<div class="container-fluid p-0">
	<c:choose>
	<c:when test="${not empty userDetails and userDetails.id ne 0 and fn:contains(sectionName, 'mva')}">
		<div class="voting-top-section">
			<div class="container">
				<div class="row mva-account">
					<div class="container">
						<div class="block-page-title-block">
							<h1 class="title">My Voter Account</h1>
						</div>
						<div class="col-xs-12 account-nav">
							<div class="row align-items-center">
								<div class="col-xs-2" style="display: none;">
									<img class="memberBadge" width="97" height="89" src="<c:url value="/img/icons/avatar_1.jpg"/>">
								</div>
								<div class="col-xs-2">
									<h3>
										<c:if test="${not empty user.name.firstName}">${user.name.firstName}</c:if>
										<c:if test="${not empty user.name.lastName}">${user.name.lastName}</c:if>
									</h3>
								</div>
								<div class="col-xs-3">
									<fmt:formatDate value="${user.created}" pattern="dd-MM-yyyy" var="userCreated"/>
									<h5><span class="member-name">Member Since</span> <span class="member-date">${userCreated}</span></h5>
								</div>
								<div class="col-xs-1" style="border-left: 2px solid antiquewhite; height: 75px; margin-top: 3px"></div>
								<div class="col-xs-2 edit-select">
                                    <select id="selectbox">
										<option value="">- Select Options -</option>
										<option value="${baseURL}/vote/UpdateAccount.htm">Edit Account</option>
                                    	<option value="${baseURL}/vote/ChangePassword.htm">Change Password</option>
                                    	<option value="${baseURL}/vote/RemoveAccount.htm">Remove Account</option>
                                    	<option value="${baseURL}/vote/logout">Log Out</option>
                                    </select>

								</div>
								<div class="col-xs-2"></div>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</c:when>
	</c:choose>
	<c:choose>
	<c:when test="${sectionName ne 'rava' || fn:contains(requestScope['javax.servlet.forward.request_uri'], 'voter-registration-absentee-voting.htm') || fn:contains(requestScope['javax.servlet.forward.request_uri'], 'FwabStart.htm')}">
		<div class="main-content bottom-padding">
	</c:when>
	<c:otherwise>
		<div class="main-content">
	</c:otherwise>
	</c:choose>
			<div class="main-container container">
				<div class="main-content">
					<section class="col-sm-12">
						<div class="content">
							<!-- content goes here -->
							<c:choose>
								<c:when test="${sectionName eq 'home' || sectionName eq 'login'}">
									<c:import url="/WEB-INF/${relativePath}/pages/blocks/Home.jsp"/>
								</c:when>
								<c:when test="${not empty userDetails and userDetails.id ne 0 and fn:contains(sectionName, 'mva') and showLeftMenu}">
									<div class="row left-navigation--right-section">
										<div class="account-sub-nav col-xs-3">
											<div class="col-xs-12 col-sm-10 account_settings">
												<h3 class="account-sub-nav-header">ACCOUNT SETTINGS</h3>
													<ul>
														<li class="profile<c:if test="${menuId eq 1}"> active</c:if>"><a href="${baseURL}/vote/MyVotingInformation.htm">Voting Address and Information</a></li>
														<li class="profile<c:if test="${menuId eq 2}"> active</c:if>"><a href="${baseURL}/vote/MyDemocracyProfile.htm">My Democracy Profile</a></li>
														<li class="profile<c:if test="${menuId eq 3}"> active</c:if>"><a href="${baseURL}/vote/ManageSubscription.htm">Voter Alerts - Manage Subscription</a></li>
													</ul>
											</div>
											<div class="col-xs-12 col-sm-10 voter_wrapper">
												<h3 class="account-sub-nav-header">VOTER ACCOUNT TOOLS AND SERVICES</h3>
												<ul>
													<li class="profile<c:if test="${menuId eq 4}"> active</c:if>"><a href="${baseURL}/vote/UpcomingElections.htm">Upcoming Elections</a></li>
													<li class="profile<c:if test="${menuId eq 5}"> active</c:if>"><a href="${baseURL}/vote/AmIRegistered.htm">Am I registered</a></li>
													<li class="profile<c:if test="${menuId eq 6}"> active</c:if>"><a href="${baseURL}/vote/VoterRegisterBallotRequest.htm">Register to Vote / Request Ballot</a></li>
													<li class="profile<c:if test="${menuId eq 7}"> active</c:if>"><a href="${baseURL}/vote/QuickTakeToVoting.htm">Quick Take on Voting in my State</a></li>
													<li class="profile<c:if test="${menuId eq 8}"> active</c:if>"><a href="${baseURL}/vote/MyStateVotingTools.htm">My State's Voting Tools</a></li>
													<li class="profile<c:if test="${menuId eq 9}"> active</c:if>"><a href="${baseURL}/vote/ElectionOfficeAndContacts.htm">My Election Office and Contacts</a></li>
													<!-- <li class="profile<c:if test="${menuId eq 10}"> active</c:if>"><a href="${baseURL}/vote/MyReps.htm">My Elected Representatives</a></li> -->
												</ul>
												<a href="https://voterhelpdesk.usvotefoundation.org/" target="_blank" class="voter_help_desk_btn">Go To Voter Help Desk</a>
												<a href="${baseURL}/donate" class="donate_btn">Donate</a>
											</div>
										</div>
										<div class="col-xs-9">
											<!-- desktop content starts here -->
											<c:import url="${content}">
												<c:param name="contentType" value="desktop"/>
											</c:import>
											<!-- desktop content ends here -->
										</div>
									</div>
									<div class="mobile-row">
										<div class="mobile-account-sub-nav col-xs-3">
											<div class="col-xs-12 col-sm-12 account_settings">
												<h3 class="mobile-account-sub-nav-header1">ACCOUNT SETTINGS</h3>
												<ul>
													<li class="profile<c:if test="${menuId eq 1}"> active</c:if>"><a href="${baseURL}/vote/MyVotingInformation.htm">Voting Address and Information</a></li>
													<li class="profile<c:if test="${menuId eq 2}"> active</c:if>"><a href="${baseURL}/vote/MyDemocracyProfile.htm">My Democracy Profile</a></li>
													<li class="profile<c:if test="${menuId eq 3}"> active</c:if>"><a href="${baseURL}/vote/ManageSubscription.htm">Voter Alerts - Manage Subscription</a></li>
												</ul>
											</div>
											<div class="col-xs-12 col-sm-12 voter_wrapper">
												<h3 class="mobile-account-sub-nav-header2">VOTER ACCOUNT TOOLS AND SERVICES</h3>
												<ul>
													<li class="profile<c:if test="${menuId eq 4}"> active</c:if>"><a href="${baseURL}/vote/UpcomingElections.htm">Upcoming Elections</a></li>
													<li class="profile<c:if test="${menuId eq 5}"> active</c:if>"><a href="${baseURL}/vote/AmIRegistered.htm">Am I registered</a></li>
													<li class="profile<c:if test="${menuId eq 6}"> active</c:if>"><a href="${baseURL}/vote/VoterRegisterBallotRequest.htm">Register to Vote / Request Ballot</a></li>
													<li class="profile<c:if test="${menuId eq 7}"> active</c:if>"><a href="${baseURL}/vote/QuickTakeToVoting.htm">Quick Take on Voting in my State</a></li>
													<li class="profile<c:if test="${menuId eq 8}"> active</c:if>"><a href="${baseURL}/vote/MyStateVotingTools.htm">My State's Voting Tools</a></li>
													<li class="profile<c:if test="${menuId eq 9}"> active</c:if>"><a href="${baseURL}/vote/ElectionOfficeAndContacts.htm">My Election Office and Contacts</a></li>
													<!-- <li class="profile<c:if test="${menuId eq 10}"> active</c:if>"><a href="${baseURL}/vote/MyReps.htm">My Elected Representatives</a></li> -->
												</ul>
												<a href="https://voterhelpdesk.usvotefoundation.org/" target="_blank" class="voter_help_desk_btn">Go To Voter Help Desk</a>
												<a href="${baseURL}/donate" class="donate_btn">Donate</a>
											</div>
										</div>
										<div class="col-xs-9">
											<!-- mobile content starts here -->
											<c:import url="${content}">
												<c:param name="contentType" value="mobile"/>
											</c:import>
											<!-- mobile content ends here -->
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<!-- other content starts here -->
									<c:import url="${content}">
										<c:param name="contentType" value="desktop"/>
									</c:import>
									<!-- other content ends here -->
								</c:otherwise>
							</c:choose>
							<!-- content ends here -->
						</div>
					</section>
				</div>
			</div>
		</div>

	<c:choose>
	<c:when test="${sectionName ne 'rava' || fn:contains(requestScope['javax.servlet.forward.request_uri'], 'voter-registration-absentee-voting.htm') || fn:contains(requestScope['javax.servlet.forward.request_uri'], 'FwabStart.htm')}">
		<footer class="site-footer">
	</c:when>
	<c:otherwise>
		<footer class="site-footer">
	</c:otherwise>
	</c:choose>
		<c:if test="${sectionName ne 'rava' || fn:contains(requestScope['javax.servlet.forward.request_uri'], 'voter-registration-absentee-voting.htm') || fn:contains(requestScope['javax.servlet.forward.request_uri'], 'FwabStart.htm')}">

			<div class="featured-bottom">

				<div class="state-list-info">
					<div class="state-list-callout">
					</div>
				</div>

			</div>

			<div class="site-footer__top clearfix">
				<section class="region region-footer-first">
					<div class="container">
						<div id="block-usvotelogo" class="block block-usvote-blocks block-usvote-logo-block">
							<div class="content">
								<ul>
									<li class="usvote-logo">
										<a href="#"><img src="${baseURL}/themes/custom/usvf/images/usvote-logo.png" alt="USVOTE Foundation" width="250"></a>
									</li>
									<li class="ovf-logo">
										<a href="#"><img src="${baseURL}/themes/custom/usvf/images/ovf_logo.png" alt="Overseas Vote Foundation" width="110"></a>
									</li>
								</ul>
							</div>
						</div>
						<div id="block-donatebutton-2" class="block block-block-content block-block-contentef64f9ee-6735-461f-9daa-202e9bcdcadd block--type-basic block--view-mode-full">
							<div class="content">
								<div class="clearfix text-formatted field field--name-body field--type-text-with-summary field--label-hidden field__item"><a href="https://usvotefoundation.networkforgood.com/" class="btn btn-primary btn-donate" target="_blank">DONATE</a></div>
							</div>
						</div>
					</div>
				</section>

				<section class="region region-footer-second">
					<div class="container">
						<nav role="navigation" aria-labelledby="block-1stcolumnfootermenu-menu" id="block-1stcolumnfootermenu" class="block block-menu navigation menu--menu-footer-menu">
							<h2 class="visually-hidden" id="block-1stcolumnfootermenu-menu">1st Column Footer Menu</h2>

							<ul class="clearfix nav">
								<li class="nav-item nav-item--about-us menu-item--expanded">
									<span class="nav-link nav-link-">About Us</span>
									<ul class="menu">
										<li class="nav-item nav-item--who-we-are">
											<a href="${baseURL}/who-we-are" class="nav-link nav-link--who-we-are">Who We Are</a>
										</li>
										<li class="nav-item nav-item--what-we-do">
											<a href="${baseURL}/what-we-do" class="nav-link nav-link--what-we-do">What We Do</a>
										</li>
										<li class="nav-item nav-item--vision-and-mission">
											<a href="${baseURL}/vision-and-mission" class="nav-link nav-link--vision-and-mission">Vision and Mission</a>
										</li>
										<li class="nav-item nav-item--our-story">
											<a href="${baseURL}/USVote-Origin-Story" class="nav-link nav-link--usvote-origin-story">Our Story</a>
										</li>
										<li class="nav-item nav-item--our-impact">
											<a href="${baseURL}/USVote-Impact-Statement" class="nav-link nav-link--usvote-impact-statement">Our Impact</a>
										</li>
										<li class="nav-item nav-item--contact">
											<a href="${baseURL}/contact" class="nav-link nav-link--contact">Contact</a>
										</li>
									</ul>
							</li>
							<li class="nav-item nav-item--initiatives menu-item--expanded">
									<span class="nav-link nav-link-">Initiatives</span>

									<ul class="menu">
				                      	<li class="nav-item nav-item--_020-voter-experiences-study-report">
					                        <a href="${baseURL}/2020-USVoteSurvey" class="nav-link nav-link-__020-usvotesurvey">2020 Voter Experiences Study Report</a>
					                    </li>
				                        <li class="nav-item nav-item--research-program">
				                        	<a href="${baseURL}/research" class="nav-link nav-link--research">Research Program</a>
				                      	</li>
				                        <li class="nav-item nav-item--future-of-voting---e2e-viv">
				                        	<a href="${baseURL}/E2E-VIV-Research-Project" class="nav-link nav-link--e2e-viv-research-project">Future of Voting / E2E VIV</a>
				                      	</li>
				                        <li class="nav-item nav-item--locelections-initiative">
				                        	<a href="${baseURL}/LOCelections-ExecSumm" class="nav-link nav-link--locelections-execsumm">LOCelections Initiative</a>
				                      	</li>
				              		</ul>

							</li>
							<li class="nav-item nav-item--outreach-tools menu-item--expanded">
									<span class="nav-link nav-link-">Outreach Tools</span>
									<ul class="menu">
										<li class="nav-item nav-item--voter-outreach-banner-kit">
					                        <a href="${baseURL}/banner-kit" class="nav-link nav-link--banner-kit">Voter Outreach Banner Kit</a>
					                    </li>
										<li class="nav-item nav-item--study-abroad--vote-toolkit">
											<a href="${baseURL}/Study-Abroad-and-Vote-Toolkit" class="nav-link nav-link--study-abroad-and-vote-toolkit">Study Abroad &amp; Vote! Toolkit</a>
										</li>
										<li class="nav-item nav-item--_-2-3-vote-from-overseas">
											<a href="${baseURL}/be-2020-us-voter-abroad" class="nav-link nav-link--be-2020-us-voter-abroad">1-2-3 Vote from Overseas</a>
										</li>
										<li class="nav-item nav-item--voter-reward-badges">
					                        <a href="${baseURL}/voter-reward-badge" class="nav-link nav-link--voter-reward-badge">Voter Reward Badges</a>
					                    </li>
									</ul>
							</li>
							<li class="nav-item nav-item--media menu-item--expanded">
									<span class="nav-link nav-link-">Media</span>
										<ul class="menu">
										<li class="nav-item nav-item--press-releases">
											<a href="${baseURL}/press-releases" class="nav-link nav-link--press-releases">Press Releases</a>
										</li>
										<li class="nav-item nav-item--digital-assets">
                        <a href="${baseURL}/digital-assets" class="nav-link nav-link--digital-assets" data-drupal-link-system-path="node/308" data-uw-rm-brl="false">Digital Assets</a>
                      </li>
									</ul>
							</li>

							<li class="nav-item nav-item--developers menu-item--expanded">
									<span class="nav-link nav-link-">Developers</span>
									<ul class="menu">
										<li class="nav-item nav-item--civic-data-products-and-api">
											<a href="https://civicdata.usvotefoundation.org/" class="nav-link nav-link-https--civicdatausvotefoundationorg-" target="_blank">Civic Data Products and API</a>
										</li>
										<li class="nav-item nav-item--request-access">
											<a href="https://civicdata.usvotefoundation.org/#request-access" class="nav-link nav-link-https--civicdatausvotefoundationorg-request-access" target="_blank">Request Access</a>
										</li>
										<li class="nav-item nav-item--custom-website-licensing">
                        <a href="${baseURL}/custom-website-licensing" class="nav-link nav-link--custom-website-licensing" data-drupal-link-system-path="node/104" data-uw-rm-brl="false">Custom Website Licensing</a>
                    </li>
									  <li class="nav-item nav-item--overseas-vote-widget">
                        <a href="${baseURL}/ov-widget" class="nav-link nav-link--ov-widget" target="_blank" data-drupal-link-system-path="node/10171" data-uw-rm-brl="false" aria-label="Overseas Vote Widget - opens in new tab" data-uw-rm-ext-link="" uw-rm-external-link-id="https://www.usvotefoundation.org/ov-widget$overseasvotewidget">Overseas Vote Widget</a>
                    </li>
									</ul>
							</li>
							</ul>
						</nav>

						<div id="block-takemetothevoterhelpdeskblock" class="block block-block-content block-block-contentb32b2a24-387b-469b-9737-7649cf0a87a8 block--type-simple-action block--view-mode-footer">
							<div class="content">
								<div class="_none">
									<div class="w-100">
										<div class="layout row no-gutters layout-builder__layout">
											<div class="col-12">
												<div class="block block-layout-builder block-field-blockblock-contentsimple-actionfield-title">
													<div class="content">
													<div class="field field--name-field-title field--type-string field--label-hidden field__item">Take Me to the Voter Help Desk</div>
													</div>
												</div>

												<div class="block block-layout-builder block-field-blockblock-contentsimple-actionfield-description">
													<div class="content">
														<div class="clearfix text-formatted field field--name-field-description field--type-text-long field--label-hidden field__item"><p>US Vote offers actionable answers to domestic voter questions, overseas voter issues. Our help desk team provides individual, personalized answers to your voting questions.</p>
													</div>
													</div>
												</div>

												<div class="block block-layout-builder block-field-blockblock-contentsimple-actionfield-button-title">
													<div class="content">
														<div class="field field--name-field-button-title field--type-string field--label-hidden field__item">My Question is about:</div>
													</div>
												</div>

												<div class="block block-layout-builder block-field-blockblock-contentsimple-actionfield-action-link">
													<div class="content">
														<div class="field field--name-field-action-link field--type-link field--label-hidden field__items">
															<div class="field__item">
																<a href="https://voterhelpdesk.usvotefoundation.org/" target="_blank" class="button fancybox fancybox.iframe">- Domestic Voting -</a>
															</div>

															<div class="field__item">
																<a href="https://voterhelpdesk.usvotefoundation.org/" target="_blank" class="button fancybox fancybox.iframe">- Overseas Voting -</a>
															</div>
														</div>

													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</section>

				<section class="region region-footer-third">
					<div class="container">
						<div id="block-socialmediaicons" class="block block-usvote-blocks block-social-media-icons">
							<div class="content">
								<ul class="social-media-icon">
									<li>
										<a href="https://twitter.com/us_vote" target="_blank">
											<img src="${baseURL}/themes/custom/usvf/images/socialmedia_twitter.png" alt="twitter">
										</a>
									</li>
									<li>
										<a href="https://www.instagram.com/usvote/" target="_blank">
											<img src="${baseURL}/themes/custom/usvf/images/socialmedia_instagram.png" alt="instagram">
										</a>
									</li>
									<li>
										<a href="https://www.facebook.com/USVote" target="_blank">
											<img src="${baseURL}/themes/custom/usvf/images/socialmedia_facebook.png" alt="facebook">
										</a>
									</li>
									<li>
										<a href="https://www.youtube.com/channel/UCTV-3fUu72tPvxp9uRomP_w" target="_blank">
											<img src="${baseURL}/themes/custom/usvf/images/socialmedia_youtube.png" alt="youtube">
										</a>
									</li>
									<li>
										<a href="https://www.linkedin.com/company/u.s.-vote-foundation/" target="_blank">
											<img src="${baseURL}/themes/custom/usvf/images/socialmedia_linkedin.png" alt="linkedin">
										</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</section>
				</c:if>

				<section class="region region-footer-fourth">
					<div class="container">
						<nav role="navigation" aria-labelledby="block-footermiscellaneousmenu-menu" id="block-footermiscellaneousmenu" class="block block-menu navigation menu--footer-misc-menu">
							<h2 class="visually-hidden" id="block-footermiscellaneousmenu-menu">Footer Miscellaneous Menu</h2>
							<ul class="clearfix nav">
								<li class="nav-item nav-item--privacy-policy">
									<a href="${baseURL}/privacy-policy" class="nav-link nav-link--privacy-policy">Privacy Policy</a>
								</li>
								<li class="nav-item nav-item--terms-of-use">
									<a href="${baseURL}/Terms-of-Use" class="nav-link nav-link--terms-of-use">Terms of Use</a>
								</li>
								<li class="nav-item nav-item--contact-us">
									<a href="${baseURL}/contact" class="nav-link nav-link--contact">Contact Us</a>
								</li>
							</ul>
						</nav>
					</div>
				</section>

				<section class="region region-footer-fifth">
					<div class="container">
						<div id="block-copyright" class="block block-usvote-blocks block-copyright">
							<div class="content">
								&copy; <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>  U.S. Vote Foundation. All rights reserved. <a href="https://www.overseasvotefoundation.org" target="_blank">Overseas Vote</a> is an Initiative of U.S. Vote Foundation.
							</div>
						</div>
					</div>
				</section>
			</div>
		</footer>
	</div>
	<c:import url="/WEB-INF/faces/basic/pages/statics/BrowserCheck.jsp"/>
	<script>(function(d){var s = d.createElement("script");s.setAttribute("data-account", "55r2z0XWKx");s.setAttribute("src", "https://cdn.userway.org/widget.js");(d.body || d.head).appendChild(s);})(document)</script>
	<noscript>Please ensure Javascript is enabled for purposes of <a href="https://userway.org">website accessibility</a></noscript>
</body>
</html>
