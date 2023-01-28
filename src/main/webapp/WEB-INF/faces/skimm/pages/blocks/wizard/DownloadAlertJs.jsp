<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
					<script type="text/javascript">

						// create the download alert popup
						YAHOO.util.Event.onContentReady("ravaForm", function() {
							
							//get buttons;	
							var contBtn = YAHOO.util.Dom.get('continue-button');
							var downloadBtn = YAHOO.util.Dom.get('download-link');
							
							// build the alert DOM
							var alrt = document.createElement("div");
							YAHOO.util.Dom.addClass(alrt, 'tooltip');
									
							var titleLine = document.createElement("div");
							YAHOO.util.Dom.addClass(titleLine, 'tooltip-title');
					
							var text = document.createElement("div");
							YAHOO.util.Dom.addClass(text, 'tooltip-text');
							text.innerHTML = "<h2>You have not downloaded your form!<"+"/h2> Please remember that you must <b>download, print, sign and mail<"+"/b> this form in order to complete the process.  <"+"/p><br /><br /><h2>Would you like to download the form now?<"+"/h2><p style=\"text-align: center; font-size: 16px; font-weight: bold;\"><a id=\"download-link2\" href=\"<c:url value='/CreatePdf.htm'/>\" target=\"_new\" id=\"download-link2\">Yes<"+"/a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"YAHOO.util.Dom.get('ravaForm').submit();\">No<"+"/a><"+"/p>";
			
							alrt.appendChild(titleLine);
							alrt.appendChild(text);
							
							var parent = YAHOO.util.Dom.getAncestorByTagName(downloadBtn, "p");
							parent.appendChild(alrt);
			
							var downloadBtn2 = YAHOO.util.Dom.get('download-link2');
							// build overlay
							var wrapper = new YAHOO.widget.Overlay(alrt, {
								visible: false,
								monitorresize: false,
								effect: {
									effect: YAHOO.widget.ContainerEffect.FADE,
									duration: 0.2
								}
							});
							
							wrapper.hide();
							
							// callback functions
							var enableContinue = function(ev,el){
								YAHOO.util.Event.removeListener(contBtn,'click');					
								YAHOO.util.Event.addListener(contBtn, 'click', function(){YAHOO.util.Dom.get('ravaForm').submit();});
							};
							var closeAlert = function(ev,el){
								enableContinue();
								wrapper.hide();
							};
							
							// add listeners to the buttons
							YAHOO.util.Event.addListener(contBtn, 'click', wrapper.show, wrapper, true);
							YAHOO.util.Event.addListener(downloadBtn, 'click', enableContinue);
							YAHOO.util.Event.addListener(downloadBtn2, 'click', closeAlert);				
						});	
	                </script>
