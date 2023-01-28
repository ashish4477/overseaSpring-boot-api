<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<script>
    $(function () {
        $(document).mouseup(function (e) {
            var container = $(".voter-account-links");
            if (container.has(e.target).length === 0) {
                $(".voter-account-links").removeClass('active');
            }
        });
        $('.usvote-account').click(function () {
            $(".voter-account-links").toggleClass('active');
            $('#navbar-main').slideUp('slow');
        });
        function hidemenu() {
            $('#navbar-main').slideUp('slow');
        }
    });
</script>
<header id="header" class="header" role="banner" aria-label="Site header" onmouseleave="hidemenu();">
    <div style="height: 97.19px;"></div>
    <nav class="navbar navbar-light bg-white affix" id="navbar-top" data-bs-toggle="affix">
        <div class="container">
            <section class="row region region-top-header">
                <a href="/" title="Home" rel="home" class="navbar-brand" data-uw-rm-brl="false">
                    <img src="${baseURL}/themes/custom/usvf/images/usvote-logo-sm.png" alt="Home" class="img-fluid d-inline-block" data-uw-rm-ima-original="home">
                </a>
                <div class="d-inline-block site-name-slogan">Every Citizen is a Voter</div>
            </section>
            <div class="form-inline navbar-form ml-auto">
                <section class="row region region-top-header-form">
                    <div id="block-usvotemenutoggle" class="block block-usvote-blocks block-usvote-menu-toggle-block">
                        <div class="content">
                            <div class="usvote-menu-toggle">
                                <a href="#" data-uw-rm-brl="false">VOTER TOOLS MENU</a>
                            </div>
                        </div>
                    </div>
                    <div id="block-usvoteaccount" class="block block-usvote-blocks block-usvote-account-block">
                        <div class="content">
                            <div class="usvote-account">

                                <c:if test="${empty userDetails or userDetails.id eq 0}">
                                <span class="text">Voter Account</span>
                                <div class="voter-account-branding">
										<span class="img">
											<div data-uw-rm-brl="false"><img src="${baseURL}/themes/custom/usvf/images/logo-small.jpg"
                                                                             alt="Check with U.S. Flag overlay"
                                                                             data-uw-rm-ima-original="check with u.s. flag overlay"></div>
										</span>
                                </div>
                                <div class="voter-account-branding last">
										<span class="img">
											<div data-uw-rm-brl="false"><img src="${baseURL}/themes/custom/usvf/images/ovf_logo.png"
                                                                             alt="Check with U.S. Flag overlay"
                                                                             data-uw-rm-ima-original="check with u.s. flag overlay"></div>
										</span>
                                    <span class="text">
											<div data-uw-rm-brl="false"> </div>
										</span>
                                </div>
                                </c:if>
                                <c:choose>
                                <c:when test="${not empty userDetails and userDetails.id ne 0}">
                                <ul class="voter-account-mobile-links" id="logged-in-menu">
                                    <li>
                                        <a href="/vote/MyVotingInformation.htm" data-uw-rm-brl="false" style="font-weight: 600;">MY DASHBOARD</a>
                                    </li>
                                    <li class="create-account">
                                        <a href="/vote/logout" class="register-link" style="color: #000000;" data-uw-rm-brl="false">Log
                                            Out</a>
                                    </li>
                                </ul>
                                </c:when>
                                <c:otherwise>
                                <ul class="voter-account-mobile-links">
                                    <li>
                                        <a href="/vote/Login.htm" data-uw-rm-brl="false">Account Login</a>
                                    </li>
                                    <li class="create-account">
                                        <a href="/vote/CreateAccount.htm" class="register-link" data-uw-rm-brl="false">Create
                                            Account</a>
                                    </li>
                                </ul>
                                </c:otherwise>
                                </c:choose>
                                <c:if test="${empty userDetails or userDetails.id eq 0}">
                                <ul class="voter-account-links">
                                    <li>
                                        <a href="/vote/Login.htm" data-uw-rm-brl="false">U.S. Vote Login</a>
                                    </li>
                                    <li>
                                        <a href="/vote/Login.htm" data-uw-rm-brl="false">Overseas Vote Login</a>
                                    </li>
                                    <li class="create-account">
                                        <a href="/vote/CreateAccount.htm" class="register-link" data-uw-rm-brl="false">Create
                                            Account</a>
                                    </li>
                                    <li>
                                        <a href="/why-create-voter-account" class="register-link" data-uw-rm-brl="false">Why
                                            should I create an account?</a>
                                    </li>
                                </ul>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div id="block-usvotemenutogglemobile"
                         class="block block-usvote-blocks block-usvote-menu-toggle-mobile-block">
                        <div class="content">
                            <button class="usvote-menu-mobile-toggle">
                                <span class="btn-text">MENU</span>
                                <span class="btn-hbg">
										<span></span>
										<span></span>
										<span></span>
									</span>
                            </button>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </nav>
    <nav class="navbar navbar-light bg-white navbar-expand-md" id="mobile-navbar-main" style="display: none;">
        <div class="container">
            <nav role="navigation" aria-labelledby="block-usvf-main-menu-menu" id="mobile-block-usvf-main-menu"
                 class="block block-menu navigation menu--main">
                <ul class="clearfix nav navbar-nav">
                    <li class="nav-item menu-item--expanded">
                        <span class="nav-link nav-link-">Register / Request Ballot</span>
                        <ul>
                            <li class="dropdown-item">
                                <a href="/vote/register.htm?flow=voter-registration"
                                   class="nav-link--vote-voter-registration-absentee-votinghtm"
                                   data-uw-rm-brl="false">Register to Vote</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/register.htm?flow=domestic-absentee-ballot"
                                   class="nav-link--vote-voter-registration-absentee-votinghtm"
                                   data-uw-rm-brl="false">Request Absentee Ballot</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/register.htm?flow=overseas-military-absentee-ballot"
                                   class="nav-link--vote-voter-registration-absentee-votinghtm"
                                   data-uw-rm-brl="false">Overseas Registration / Ballot Request</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/register.htm?flow=overseas-military-absentee-ballot"
                                   class="nav-link--vote-voter-registration-absentee-votinghtm"
                                   data-uw-rm-brl="false">Military Registration / Ballot Request</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/am-i-registered" class="nav-link--am-i-registered"
                                   data-drupal-link-system-path="am-i-registered" data-uw-rm-brl="false">Am I Registered to Vote?</a>
                            </li>
                        </ul>
                    </li>
                    <li class="nav-item menu-item--expanded">
                        <span class="nav-link nav-link-">Election Information</span>
                        <ul>
                            <li class="dropdown-item">
                                <a href="/vote/state-elections/state-election-dates-deadlines.htm"
                                   class="nav-link--vote-state-elections-state-election-dates-deadlineshtm"
                                   data-uw-rm-brl="false">Election Dates &amp; Deadlines</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/sviddomestic.htm"
                                   class="nav-link--vote-sviddomestichtm" data-uw-rm-brl="false">State Voter Information</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/state-voting-methods-and-options"
                                   class="nav-link--state-voting-methods-and-options"
                                   data-drupal-link-system-path="state-voting-methods-and-options"
                                   data-uw-rm-brl="false">Voting Methods &amp; Options</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/eoddomestic.htm"
                                   class="nav-link--vote-eoddomestichtm" data-uw-rm-brl="false">Election Office Contact Info</a>
                            </li>
                        </ul>

                    </li>
                    <li class="nav-item menu-item--expanded">
                        <span class="nav-link nav-link-">Voter Assistance</span>
                        <ul>
                            <li class="dropdown-item">
                                <a href="/how-to-vote-guide-all-states"
                                   class="nav-link--how-to-vote-guide-all-states" data-drupal-link-system-path="node/10193"
                                   data-uw-rm-brl="false">How to Vote Guide</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote-from-abroad"
                                   class="nav-link--vote-from-abroad" data-drupal-link-system-path="node/226"
                                   data-uw-rm-brl="false">Vote from Abroad/Overseas</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/disabled-voter-guide"
                                   class="nav-link--disabled-voter-guide" data-drupal-link-system-path="node/10187"
                                   data-uw-rm-brl="false">Voters with Disabilities Guide</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/voting-rights-restoration"
                                   class="nav-link--voting-rights-restoration"
                                   data-drupal-link-system-path="voting-rights-restoration" data-uw-rm-brl="false">Voting Rights Restoration</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/us-voter-faq" class="nav-link--us-voter-faq"
                                   data-drupal-link-system-path="node/10134" data-uw-rm-brl="false">US Voting FAQs</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/overseas-voter-faq"
                                   class="nav-link--overseas-voter-faq" data-drupal-link-system-path="node/10135"
                                   data-uw-rm-brl="false">Overseas Voting FAQs</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="https://voterhelpdesk.usvotefoundation.org/" target="_blank" rel="noopener"
                                   class="nav-link-https--voterhelpdeskusvotefoundationorg-"
                                   aria-label="Voter Help Desk - opens in new tab"
                                   uw-rm-external-link-id="https://voterhelpdesk.usvotefoundation.org/$voterhelpdesk"
                                   data-uw-rm-brl="false" data-uw-rm-ext-link="na">Voter Help Desk</a>
                            </li>
                        </ul>

                    </li>
                    <li class="nav-item menu-item--expanded">
                        <span class="nav-link nav-link-">Charts / Resources</span>
                        <ul>
                            <li class="dropdown-item">
                                <a href="/wheres-my-ballot"
                                   class="nav-link--wheres-my-ballot" data-drupal-link-system-path="node/299"
                                   data-uw-rm-brl="false">Where's My Ballot?</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/ballot-return-options"
                                   class="nav-link--ballot-return-options" data-drupal-link-system-path="node/216"
                                   data-uw-rm-brl="false">Ballot Return Options Chart</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/early-voting-dates"
                                   class="nav-link--early-voting-dates" data-drupal-link-system-path="node/229"
                                   data-uw-rm-brl="false">Early Voting Dates Chart</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/Can-I-Change-My-Mind"
                                   class="nav-link--can-i-change-my-mind" data-drupal-link-system-path="node/304"
                                   data-uw-rm-brl="false">Absentee to In-person Voting</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/sample-ballot-lookup"
                                   class="nav-link--sample-ballot-lookup" data-drupal-link-system-path="node/10195"
                                   data-uw-rm-brl="false">Sample Ballot Lookup</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/find-my-polling-place"
                                   class="nav-link--find-my-polling-place"
                                   data-drupal-link-system-path="find-my-polling-place" data-uw-rm-brl="false">Polling Place Finder</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/blog" class="nav-link--blog"
                                   data-drupal-link-system-path="blog" data-uw-rm-brl="false">US Vote Blog</a>
                            </li>
                        </ul>

                    </li>
                </ul>
            </nav>
        </div>
    </nav>
    <nav class="navbar navbar-light bg-white navbar-expand-md" id="navbar-main" style="">
        <div class="container">
            <nav role="navigation" aria-labelledby="block-usvf-main-menu-menu" id="block-usvf-main-menu"
                 class="block block-menu navigation menu--main">
                <h2 class="visually-hidden" id="block-usvf-main-menu-menu">Main Menu</h2>
                <ul class="clearfix nav navbar-nav">
                    <li class="nav-item menu-item--expanded">
                        <span class="nav-link nav-link-">Register / Request Ballot</span>
                        <ul>
                            <li class="dropdown-item">
                                <a href="/vote/register.htm?flow=voter-registration"
                                   class="nav-link--vote-voter-registration-absentee-votinghtm"
                                   data-uw-rm-brl="false">Register to Vote</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/register.htm?flow=domestic-absentee-ballot"
                                   class="nav-link--vote-voter-registration-absentee-votinghtm"
                                   data-uw-rm-brl="false">Request Absentee Ballot</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/register.htm?flow=overseas-military-absentee-ballot"
                                   class="nav-link--vote-voter-registration-absentee-votinghtm"
                                   data-uw-rm-brl="false">Overseas Absentee Ballot</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/register.htm?flow=overseas-military-absentee-ballot"
                                   class="nav-link--vote-voter-registration-absentee-votinghtm"
                                   data-uw-rm-brl="false">Military Absentee Ballot</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/am-i-registered" class="nav-link--am-i-registered"
                                   data-drupal-link-system-path="am-i-registered" data-uw-rm-brl="false">Am I Registered to Vote?</a>
                            </li>
                        </ul>
                    </li>
                    <li class="nav-item menu-item--expanded">
                        <span class="nav-link nav-link-">Election Information</span>
                        <ul>
                            <li class="dropdown-item">
                                <a href="/vote/state-elections/state-election-dates-deadlines.htm"
                                   class="nav-link--vote-state-elections-state-election-dates-deadlineshtm"
                                   data-uw-rm-brl="false">Election Dates &amp; Deadlines</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/sviddomestic.htm"
                                   class="nav-link--vote-sviddomestichtm" data-uw-rm-brl="false">State Voter Information</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/state-voting-methods-and-options"
                                   class="nav-link--state-voting-methods-and-options"
                                   data-drupal-link-system-path="state-voting-methods-and-options"
                                   data-uw-rm-brl="false">Voting Methods &amp; Options</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote/eoddomestic.htm"
                                   class="nav-link--vote-eoddomestichtm" data-uw-rm-brl="false">Election Office Contact Info</a>
                            </li>
                        </ul>

                    </li>
                    <li class="nav-item menu-item--expanded">
                        <span class="nav-link nav-link-">Voter Assistance</span>
                        <ul>
                            <li class="dropdown-item">
                                <a href="/how-to-vote-guide-all-states"
                                   class="nav-link--how-to-vote-guide-all-states" data-drupal-link-system-path="node/10193"
                                   data-uw-rm-brl="false">How to Vote Guide</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/vote-from-abroad"
                                   class="nav-link--vote-from-abroad" data-drupal-link-system-path="node/226"
                                   data-uw-rm-brl="false">Vote from Abroad/Overseas</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/disabled-voter-guide"
                                   class="nav-link--disabled-voter-guide" data-drupal-link-system-path="node/10187"
                                   data-uw-rm-brl="false">Voters with Disabilities Guide</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/voting-rights-restoration"
                                   class="nav-link--voting-rights-restoration"
                                   data-drupal-link-system-path="voting-rights-restoration" data-uw-rm-brl="false">Voting
                                    Rights Restoration</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/us-voter-faq" class="nav-link--us-voter-faq"
                                   data-drupal-link-system-path="node/10134" data-uw-rm-brl="false">US Voting FAQs</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/overseas-voter-faq"
                                   class="nav-link--overseas-voter-faq" data-drupal-link-system-path="node/10135"
                                   data-uw-rm-brl="false">Overseas Voting FAQs</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="https://voterhelpdesk.usvotefoundation.org/" target="_blank" rel="noopener"
                                   class="nav-link-https--voterhelpdeskusvotefoundationorg-"
                                   aria-label="Voter Help Desk - opens in new tab"
                                   uw-rm-external-link-id="https://voterhelpdesk.usvotefoundation.org/$voterhelpdesk"
                                   data-uw-rm-brl="false" data-uw-rm-ext-link="na">Voter Help Desk</a>
                            </li>
                        </ul>

                    </li>
                    <li class="nav-item menu-item--expanded">
                        <span class="nav-link nav-link-">Charts / Resources</span>
                        <ul>
                            <li class="dropdown-item">
                                <a href="/wheres-my-ballot"
                                   class="nav-link--wheres-my-ballot" data-drupal-link-system-path="node/299"
                                   data-uw-rm-brl="false">Where's My Ballot?</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/ballot-return-options"
                                   class="nav-link--ballot-return-options" data-drupal-link-system-path="node/216"
                                   data-uw-rm-brl="false">Ballot Return Options Chart</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/early-voting-dates"
                                   class="nav-link--early-voting-dates" data-drupal-link-system-path="node/229"
                                   data-uw-rm-brl="false">Early Voting Dates Chart</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/Can-I-Change-My-Mind"
                                   class="nav-link--can-i-change-my-mind" data-drupal-link-system-path="node/304"
                                   data-uw-rm-brl="false">Absentee to In-person Voting</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/sample-ballot-lookup"
                                   class="nav-link--sample-ballot-lookup" data-drupal-link-system-path="node/10195"
                                   data-uw-rm-brl="false">Sample Ballot Lookup</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/find-my-polling-place"
                                   class="nav-link--find-my-polling-place"
                                   data-drupal-link-system-path="find-my-polling-place" data-uw-rm-brl="false">Polling Place
                                    Finder</a>
                            </li>
                            <li class="dropdown-item">
                                <a href="/blog" class="nav-link--blog"
                                   data-drupal-link-system-path="blog" data-uw-rm-brl="false">US Vote Blog</a>
                            </li>
                        </ul>

                    </li>
                </ul>
            </nav>
        </div>
    </nav>
</header>