<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<link rel="stylesheet" href="<c:url value="/css/jquery-ui.css"/>">

<div class="body-content row ${fn:toLowerCase(wizardContext.flowType)} column wide-content">
<div class="page-form col-xs-12 col-sm-10">
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
    <div class="bd" id="overseas-vote-foundation-short">
		<div class="bd-inner">
            <c:if test="${not formValid}">
                <div class="download-box">
                    <c:choose>
                        <c:when test="${expired}">
                        <script>
                         jQuery(function() {
                          var url = document.location.href+"&expired=true";
                          document.location = url;
                         });
                        </script>
                            <div id="update-message" class="alert alert-warning">The download link has expired. Please restart the data entry process or <a href="<c:url value="/Login.htm"/>">login to continue</a>.</div>
                        </c:when>
                        <c:otherwise>
                            <div id="update-message" class="alert alert-warning">There is no form to download. Please restart the data entry process or <a href="<c:url value="/Login.htm"/>">login to continue</a>.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <c:if test="${formValid}">
            <c:choose>
            <c:when test="${wizardContext.flowType == 'FWAB'}">
     		<c:set var="formType" value="Ballot"/>
            </c:when>
            <c:otherwise>
     		<c:set var="formType" value="Form"/>
            </c:otherwise>
        	</c:choose>
        	  <div class="preparing">
            <h3>Preparing your File, Please Wait...</h3>
             <div id="progressbar"></div>
          </div>
          <div class="download-box complete" style="display:none;">
                <div class="download-box">
	                <div id="update-message" class="alert alert-success"><span class="glyphicon glyphicon-ok"></span>&nbsp; Your Prepared ${formType} is Available for Download.</div>
					<h3>To Complete Your Request You Must Take The Following Steps:</h3>
           <br/>
					<ol>
						<li> Download Your Completed ${formType}</li>
						<li> Verify Your Information for Accuracy</li>
						<li> Print, Sign, and Submit Your Document to the Election Office Address Provided with Your Document and Instructions</li> 
						<c:if test="${wizardContext.wizardResults.votingAddress.state != 'MN'}">
						<li> Send the Signed, Original Printed Document by Mail in Order to Assure You Meet the Signature Requirements <span class="hint">(even if you fax or email your document)</span></li>
						</c:if>
					</ol>

                       <div style="padding-left:50px; padding-bottom:15px;">
	                         <a href="<c:url value="${wizardUrl}"><c:param name="back" value="true"/></c:url>" id="back-button" class="back button">Back</a>
							<span id="pdf-download-link"><a href="<c:url value="/CreatePdf.htm?flow=${wizardContext.flowType}&&generationId=${generationId}"/>" target="_new" id="download-link" class="button">Download Your Form</a></span>
						</div>

					<p>If necessary, use the <strong>Back</strong> button to make changes to your <span style="text-transform: lowercase; ">${formType}</span> and then repeat the download.</p>
				    <p>Your document is a PDF file  which can be read by Adobe Acrobat Reader. (<a href="http://get.adobe.com/reader" target="_blank">Get Adobe Reader</a>).</p>


          <div class="security-recommendation">
              <c:set var="downloadExpiration" value="900000"/>
              <c:set var="expires" value="${downloadExpiration/60}"/>

               <h2>* Download your form to receive a link via email so that you can print, sign, and return your personalized form at your convenience. The link will remain active for 7 days.</h2>
          </div>
  
        </div>

      <!-- this submission triggers an invalidation of the download link -->
      <form action="<c:url value="${pageUrl}"/>" method="post" class="last-page" name="adForm" id="invalidationForm">
        <input type="hidden" name="submission" value="true"/>
      </form>
      </c:if>
    </div>
	</div>
</div>
</div>

<c:if test="${formValid}">
<script type="text/javascript"  language="JavaScript">
    function checkPdfTaskStatus(id) {
      setTimeout(function() {
        var jqxhr = jQuery.ajax( '<c:url value="/ajax/checkFormTrack.htm"><c:param name="trackId">${generationId}</c:param></c:url>' )
          .done(function(data) {
            //console.log(id, data);

            if (data.status === 2 ) {
            // generation finished
                jQuery(".complete").show();
                jQuery(".preparing" ).hide();

                setTimeout(function() {
                 // Expire the form
                jQuery( "#invalidationForm" ).submit();
                }, ${downloadExpiration});
            } else if (data.status === 0 || data.status === 1) {
            // new and in_progress statuses
              checkPdfTaskStatus(id);

            } else if (data.status === 3) {    // error status
                jQuery("#progressbar").text("Unable to create your form");
            } else if ( data.status === 4) {
                jQuery("#progressbar").text(data.message);
            }
          })
          .fail(function() {
          });
      }, 2000); // check every 2 seconds
    }

  jQuery(function() {
    jQuery("#progressbar").progressbar({ value:false});
    checkPdfTaskStatus(${generationId});
  });


</script>
</c:if>