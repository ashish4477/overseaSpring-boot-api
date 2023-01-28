<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="overseas" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%-- This has to be done this way in order to access the errors --%>

<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/calendar.css"/>" />
<script src="<c:url value="/js/calendar-min.js"/>" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/blitzer/jquery-ui.css" media="screen" />

<div class="body-content page-form <c:out value="${fn:toLowerCase(wizardContext.flowType)}"/> content column wide-content">
	<div class="page-form">
		<c:choose>
			<c:when test="${wizardContext.flowType == 'FWAB'}">
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value="Federal Write-in Absentee Ballot" />
					<c:param name="progressLabel" value="write-in ballot progress" />
				</c:import>
			</c:when>
			<c:when test="${wizardContext.flowType == 'RAVA'}">
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value="Register to Vote / Absentee Ballot Request" />
					<c:param name="progressLabel" value="registration progress" />
				</c:import>
			</c:when>
			<c:when test="${wizardContext.flowType == 'DOMESTIC_REGISTRATION'}">
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value="Register to Vote" />
					<c:param name="progressLabel" value="registration progress" />
				</c:import>
			</c:when>
			<c:when test="${wizardContext.flowType == 'DOMESTIC_ABSENTEE'}">
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value="Absentee Ballot Request" />
					<c:param name="progressLabel" value="registration progress" />
				</c:import>
			</c:when>
			<c:otherwise>
				<c:import url="/WEB-INF/faces/basic/pages/templates/FrameHeader.jsp">
					<c:param name="title" value=" " />
					<c:param name="progressLabel" value="registration progress" />
				</c:import>
			</c:otherwise>
		</c:choose>
        <div class="bd ${(wizardContext.currentPage.title eq 'Choose your candidates') ? 'fwabAddon' : ''}" id="overseas-vote-foundation-short">
            <div class="bd-inner">
            
<c:if test="${wizardContext.currentPage.title eq 'How Will You Be Sending Your Application?'}">

</c:if>
                <form action="<c:url value="${formUrl}"/>" method="post" id="ravaForm" name="ravaForm">
                    <h2 class="subtitle">${wizardContext.currentPage.title}</h2>
                    <c:forEach items="${wizardContext.currentPage.questions}" var="question" varStatus="vs">
                        <fieldset class="question-group ${question.htmlClassFieldset}">
                            <c:if test="${not empty question.title}">
                                <h2>
                                	${question.title}
                                    <c:if test="${fn:length(question.variants[0].fields) == 1 && question.variants[0].fields[0].required}">
                                        <span class="required">*</span>
                                    </c:if>
                                </h2>
                            </c:if>
                            <authz:authorize ifAllGranted="ROLE_ADMIN">
                                <input type="button" class="btn" name="Edit Question Group" value="Edit Question Group" onclick="window.location.href='<c:url value="/admin/EditQuestionVariant.htm"><c:param name="id" value="${question.variants[0].id}"/></c:url>';"/>
                            </authz:authorize>

                            <c:set var="variant" value="${question.variants[0]}" />
                            <c:choose>
                                <c:when test="${question.name eq 'fwab_choose_candidate'}">
                                    <overseas:variant_choose_candidates variant="${question.variants[0]}"/>
                                </c:when>
                                <c:when test="${question.name eq 'fwab_referendum'}">
                                    <overseas:variant_answer_referendum variant="${question.variants[0]}"/>
                                </c:when>
                                <c:otherwise>
                                    <overseas:variant_default variant="${question.variants[0]}"/>
                                </c:otherwise>
                            </c:choose>
                        </fieldset>
                    </c:forEach>
                    <fieldset class="continue">
                        <c:if test="${lastPage}">
                            <input type="hidden" name="finish" value="true"/>
                        </c:if>
                        <c:choose>
                            <c:when test="${not empty backUrl}" >
                                <a href="<c:url value="${backUrl}" />" id="back-button">
                                    <img src="<c:url value="/img/buttons/back-button.gif" />" alt="Back" />
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="spacer"></div>
                            </c:otherwise>
                        </c:choose>
                        <input type="image" class="btn_continue" src="<c:url value="/img/buttons/continue-button.gif"/>" name="_target${currentStep+1}" value="Continue"/>
                        
                        <div class="break"></div>
                        <script type="text/javascript">
                            (function() {
                                var ravaForm = document.getElementById("ravaForm");
                                var element = null;

                                for(var i = 0; i < ravaForm.elements.length; i++) {
                                    element = ravaForm.elements[i];
                                    var t = element.tagName.toLowerCase();
                                    if((t == 'input' || t == 'select' || t == 'textarea') && (element.type != 'hidden')) {
                                        element.focus();
                                        break;
                                    }
                                }
                            })();

                          // iOS 7 hack: Add an optgroup to every select in order to avoid truncating the content
                          if(window.innerWidth <= 800 && window.innerHeight <= 600) {
                              var selects = document.querySelectorAll("select");
                              for (var i = 0; i < selects.length; i++ ){
                                  selects[i].appendChild(document.createElement("optgroup"));
                              }
                          }
                        </script>
                    </fieldset>
                </form>
            </div>
        </div>
        <div class="ft"><div class="ft-inner"></div></div>
    </div>
</div>

