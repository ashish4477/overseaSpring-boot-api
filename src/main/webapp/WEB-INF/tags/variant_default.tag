<%@ tag body-content="scriptless" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="variant" type="com.bearcode.ovf.model.questionnaire.QuestionVariant" required="true" %>

<c:set var="single" value="${fn:length(variant.fields) == 1}"/>
<c:forEach items="${variant.fields}" var="field">
	<spring:bind path="wizardContext.answersAsMap[${field.id}]">
		<c:choose>
			<c:when test="${not empty field.additionalHelp}">
				<c:set var="additionalHelp" value="has-rava-bubble" />
			</c:when>
			<c:otherwise>
				<c:set var="additionalHelp" value="no-rava-bubble" />
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${single}">
				<c:set var="classname" value="single-field" />
			</c:when>
			<c:otherwise>
				<c:set var="classname" value="multiple-field" />
			</c:otherwise>
		</c:choose>
		<c:if test="${field.type.templateName != 'no_input'}" >
			<input type="hidden" name="fields" value="${field.id}" />
		</c:if>

		<c:set var="error" value="" />
        <c:if test="${status.error}">
			<p class="error"><c:out value="${status.errorMessage}"/></p>
			<c:set var="error" value="error-indicator" />
		</c:if>

        <div class="question form-group ${error}">
            <c:choose>
				<%-- select with options --%>
				<c:when test="${field.type.templateName == 'select'}">
					<div class="one-line ${classname}">
						<c:if test="${not single and not empty field.title }">
							<label class="label">
								${field.title}
                                <c:if test="${field.required}">
                                    <b>*</b>
                                </c:if>
                                <c:if test="${not empty field.additionalHelp}">
							<a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
						</c:if>
                            </label>
                        </c:if>
						<span class="select  ${additionalHelp}">
							<select class="form-control" name="${field.id}">
								<option value=""><fmt:message key="tag.please_choose"/></option>
								<c:forEach items="${field.options}" var="option">
									<c:choose>
										<c:when test="${option.value eq status.value.value}">
											<option value="${option.optionValue}" selected="selected">
												${option.viewValue}
											</option>
										</c:when>
										<c:otherwise>
											<option value="${option.optionValue}">${option.viewValue}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</span>
						<div class="break"></div>
					</div>
				</c:when>

				<%-- radio with options --%>
				<c:when test="${field.type.templateName == 'radio' or field.type.templateName == 'radio_first_selected'}">
					<div class="radio-group ${classname} ${additionalHelp}">

						<c:if test="${not empty field.title}">
							<div class="radio-field-title ${additionalHelp}">
								<label class="title">
								<c:choose>
									<c:when test="${field.required}"> <%--  and single --%>
										<span class="required">${field.title} * <c:if test="${not empty field.additionalHelp}">
	                                <a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
	                            </c:if></span>
									</c:when>
									<c:otherwise>
										${field.title}
									</c:otherwise>
								</c:choose>
	                            </label>
							</div>
						</c:if>

						<c:forEach items="${field.options}" var="option" varStatus="varStat">
                            <c:set var="perOption" value=""/>
                            <c:if test="${not empty variant.question.htmlClassOption}">
                                <c:set var="perOption" value="${variant.question.htmlClassOption}_${varStat.index+1}" />
                            </c:if>
							<div class="one-line ${variant.question.htmlClassOption} ${perOption}">
								<div class="radio">
									<label>
										<c:choose>
											<c:when test="${status.value.value eq option.value or ((empty status.value or empty status.value.value) and varStat.first and field.type.templateName == 'radio_first_selected')}">
												<input name="${field.id}" type="radio" value="${option.optionValue}"
													id="${variant.id}_${field.id}_${option.id}" checked="checked" />
											</c:when>
											<c:otherwise>
												<input name="${field.id}" type="radio" value="${option.optionValue}"
													id="${variant.id}_${field.id}_${option.id}" />
											</c:otherwise>
										</c:choose>
										${option.viewValue}
									</label>
								</div>
							</div>
						</c:forEach>
					</div>
				</c:when>

				<%-- string field --%>
				<c:when test="${field.type.templateName == 'text'}">
					<div class="one-line ${classname}">
						<c:if test="${!single and not empty field.title}">
							<label class="label">
								${field.title}
								<c:if test="${field.required}">
	                                <b>*</b>
	                            </c:if>
	                            <c:if test="${not empty field.additionalHelp}">
							<a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
						</c:if>
							</label>
                        </c:if>

						<span class="text ${additionalHelp}">
                        	<input type="text" class="textfield form-control" name="${field.id}" value="<c:out value="${status.value.value}"/>" />
						</span>
					</div>
				</c:when>

                <%-- string field plus confirm field --%>
                <c:when test="${field.type.templateName == 'text_confirm'}">
                    <div class="one-line ${classname}">
                        <c:if test="${not empty field.title}">
                            <label class="label">
                                ${field.title}
                                <c:if test="${not empty field.title and field.required}">
                                    <b>*</b>
                                </c:if>
                                <c:if test="${not empty field.additionalHelp}">
                            <a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
                        </c:if>
                            </label>
                        </c:if>

                        <span class="text ${additionalHelp}">
                            <input type="text" class="textfield form-control" name="${field.id}" id="YAHOO-bc-${field.id}" value="<c:out value="${status.value.value}"/>" />
                        </span>

                    </div>
                    <div class="one-line ${classname}">
                        <c:if test="${not empty field.title}">
                            <label class="label">
                                <fmt:message key="tag.confirm"/> ${field.title}
                                <c:if test="${not empty field.title and field.required}">
                                    <b>*</b>
                                </c:if>
                            </label>
                       </c:if>

                        <span class="text ${additionalHelp}">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <input type="text" class="textfield form-control" name="${field.id}_cf" id="YAHOO-bc-confirm-${field.id}" value="" />
                                </c:when>
                                <c:otherwise>
                                    <input type="text" class="textfield form-control" name="${field.id}_cf" id="YAHOO-bc-confirm-${field.id}" value="${status.value.value}" />
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <script type="text/javascript">
                        // clear confirmation field on changing main field
                        YAHOO.util.Event.addListener("YAHOO-bc-${field.id}",'change', function() {
                            document.getElementById("YAHOO-bc-confirm-${field.id}").value = '';
                        });
                    </script>
                </c:when>

				<%-- checkbox --%>
				<c:when test="${field.type.templateName == 'checkbox'}">
					<script type="text/javascript">
						
						YAHOO.util.Event.addListener("YAHOO-bc-${field.id}",'change', function() {
							document.getElementById("${field.id}").value = (this.checked) ? 'true' : 'false';
						});
					</script>
					<input type="hidden" id="${field.id}" name="${field.id}" value="${status.value.value}" />

					<div class="one-line ${classname} checkbox">
					<div class="checkbox">
							<label class="label">
								<c:choose>
									<c:when test="${status.value.value eq 'true'}">
										<input class="checkbox-field" type="checkbox" id="YAHOO-bc-${field.id}" checked="checked" />
									</c:when>
									<c:otherwise>
										<input class="checkbox-field" type="checkbox" id="YAHOO-bc-${field.id}" />
									</c:otherwise>
								</c:choose>
							
								<c:if test="${not empty field.title}">
									${field.title}
									<c:if test="${not empty field.title and !single}">
		                            	<c:if test="${field.required}">
			                                <b>*</b>
			                            </c:if>
		                        	</c:if>
		                        	<c:if test="${not empty field.additionalHelp}">
										<a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
									</c:if>	
								</c:if>
							</label>
						</div>
					</div>
				</c:when>

                <%-- checkbox that is checked by default--%>
                <c:when test="${field.type.templateName == 'checkbox_filled'}">
                    <script type="text/javascript">
                        // god, this is such a hack. stupid spring. - nick
                        YAHOO.util.Event.addListener("YAHOO-bc-${field.id}",'change', function() {
                            document.getElementById("${field.id}").value = (this.checked) ? 'true' : 'false';
                        });
                    </script>

                    <div class="one-line ${classname} checkbox">
	                    <div class="checkbox">
	                        <label class="label">
	                            <c:choose>
	                                <c:when test="${status.value.value eq 'false'}">
	                                    <input type="hidden" id="${field.id}" name="${field.id}" value="false" />
	                                    <input class="checkbox-field" type="checkbox" id="YAHOO-bc-${field.id}" />
	                                </c:when>
	                                <c:otherwise>
	                                    <input type="hidden" id="${field.id}" name="${field.id}" value="true" />
	                                    <input class="checkbox-field" type="checkbox" id="YAHOO-bc-${field.id}"  checked="checked"/>
	                                </c:otherwise>
	                            </c:choose>
                        
								<c:if test="${not empty field.title}">
								    ${field.title}
								    <c:if test="${!single}">
								        <c:if test="${field.required}">
								            <b>*</b>
								        </c:if>
								    </c:if>
								    <c:if test="${not empty field.additionalHelp}">
										<a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
									</c:if>
								</c:if>
                        	</label>
                    	</div>
                    </div>
                </c:when>

				<%-- not a input field, just message --%>
				<c:when test="${field.type.templateName == 'no_input'}">
					<div class="no-input ${additionalHelp}">${field.helpText}
						<c:if test="${not empty field.additionalHelp}">
							<a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
						</c:if>
					</div>

				</c:when>

				<%-- text area field --%>
				<c:when test="${field.type.templateName == 'textarea'}">
					<div class="textarea ${additionalHelp}">
						<c:if test="${not single or not empty field.additionalHelp}">
							<div class="textarea-label">
								<c:if test="${not empty field.title}">
									<label class="label">
										${field.title}
										<c:if test="${!single and field.required}">
		                                    <b>*</b>
		                                </c:if>
									</label>
								</c:if>
								<c:if test="${not empty field.additionalHelp}">
									<a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
								</c:if>
							</div>
                        </c:if>
						<textarea name="${field.id}" cols="40" rows="5"><c:out value="${status.value.value}"/></textarea>
					</div>
				</c:when>

				<%-- date field --%>
					<c:when test="${field.type.templateName == 'date'}">
					       <div class="datefield">
					<label id="date" class="oneline ${additionalHelp} datefield">
						<c:if test="${not empty field.title}">
							<label class="label">${field.title}
								<c:if test="${!single}">
		                            <c:if test="${field.required}">
		                                <b>*</b>
		                            </c:if>
		                        </c:if>
		                        <c:if test="${not empty field.additionalHelp}">
									<a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
								</c:if>
	                        </label>
                            </label>
                        </c:if>

                        <label class="oneline"><span class="date-label"><b><fmt:message key="tag.date"/>:</b></span>
                        <input type="text" class="textfield form-control" name="${field.id}" value="${status.value.value}" id="in_${field.id}"/>
               </label>
                      
					</label>
                    </div>
	<script>
		jQuery(function() {
			jQuery( "#in_${field.id}" ).datepicker ({
			autoSize: true
			});
		});
	</script>

				</c:when>

                <%-- replica of other field --%>
                <c:when test="${field.type.templateName == 'replica'}">
                    <div class="one-line ${classname}">
                        <c:if test="${not empty field.title and !single}">
                            <label class="label">
                                ${field.title}
                                <c:if test="${field.required}">
                                    <b>*</b>
                                </c:if>
                                 <c:if test="${not empty field.additionalHelp}">
		                            <a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
		                        </c:if>
                            </label>
                        </c:if>

                        <span class="text ${additionalHelp}">
                            ${field.firstText}
                        </span>

                       
                    </div>
                </c:when>

                <%-- replica with disabled input --%>
                <c:when test="${field.type.templateName == 'replica_plus_input'}">
                    <div class="one-line ${classname}">
                        <c:if test="${not empty field.title and !single}">
                            <label class="label">
                                ${field.title}
                                <c:if test="${field.required}">
                                    <b>*</b>
                                </c:if>
                            </label>
                            <c:if test="${not empty field.additionalHelp}">
                            <a href="#" data-toggle="tooltip" data-placement="auto" title="" data-original-title="${field.additionalHelp}"><span class="glyphicon glyphicon-info-sign tooltip-icon"></span></a>
                        </c:if>
                        </c:if>

                        <span class="text ${additionalHelp}">
                            <input type="text" class="textfield form-control" disabled="disabled" value="${field.firstText}" />
                        </span>
                    </div>
                </c:when>

			</c:choose>
			<div class="break"></div>
		</div>
	</spring:bind>
</c:forEach>
