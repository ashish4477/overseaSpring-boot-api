YAHOO.namespace("ovf");

/* The following code implements various function for working with AJAX and so on... */
YAHOO.ovf.getRegionUrl = null;// the URI to go out and get data from AJAX call
YAHOO.util.Event.addListener("select_state", "change", function() {
	//var url = "/vote/ajax/GetRegions.htm";
	var callback = {
		success: function(response) {
			var div = document.getElementById('ajax_region_select');
			if(response.responseText !== undefined) {
                var resultText = response.responseText;
                resultText = resultText.replace("<span>Choose a Region:</span>", "");
                div.innerHTML = resultText;
			}
		},
		failure: function(response) {
			YAHOO.log("AJAX Error, could not get regions! Returned: '"+response.statusText+"'", "error", "ovf.js");
		}
	};
	if ( YAHOO.ovf.getRegionUrl == null ) {
        YAHOO.log("Configuration error: URL for AJAX call hasn't been defined", "error", "ovf.js");
        return;
   }
	YAHOO.util.Connect.setForm("expressYourVote");
	YAHOO.util.Connect.asyncRequest('get', YAHOO.ovf.getRegionUrl, callback);

	var placeholder = "<select class='input-select'><option value='0' selected='selected'>Choose your voting region</option></select>";

	document.getElementById('ajax_region_select').innerHTML = placeholder;
});

YAHOO.util.Event.addListener("expressYourVote", "submit", function() {
    var form = document.getElementById("expressYourVote");
    var newInput = document.createElement("input");
    newInput.setAttribute("type","hidden");
    newInput.setAttribute("name","submission");
    newInput.setAttribute("value","true");
    form.appendChild(newInput);
    return true;
});