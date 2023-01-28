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
	<div class="page-form row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1">
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
                       <div class="row">
                        <div class="col-xs-12">
                        <c:if test="${lastPage}">
                            <input type="hidden" name="finish" value="true"/>
                        </c:if>
                        <c:choose>
                            <c:when test="${not empty backUrl}" >
                                <a href="<c:url value="${backUrl}" />" class="wizard button back">Back</a>
                            </c:when>
                            <c:otherwise>
                                <div class="spacer"></div>
                            </c:otherwise>
                        </c:choose>
                          <input type="submit" class="pull-right" name="_target${currentStep+1}" value="Continue"/>
                        </div>
                      </div>

                       <div class="modal fade error-message">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">x</span></button>
                                            <h4 class="modal-title text-center">Some of the information you entered is incomplete or inaccurate. <br/>Please review the error message(s) below.</h4>
                                        </div>
                                        <div class="modal-body text-center">
                                            <a class="button" data-dismiss="modal">Continue</a>
                                        </div>
                            </div><!-- /.modal-content -->
                          </div><!-- /.modal-dialog -->
                        </div><!-- /.modal -->

                        <script type="text/javascript">
                            (function() {

                                if ($(".error")[0]){
                                    $('.error-message').modal('show');
                                }

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
    </div>
    </div>
</div>
<script>
jQuery(function($){ 
     $('[data-toggle="tooltip"]').tooltip()
});
</script>