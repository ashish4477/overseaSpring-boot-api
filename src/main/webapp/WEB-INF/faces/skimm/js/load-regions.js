function loadRegions(event, params) {

	var stateInputId = this.stateInputId || params.stateInputId || 'select_state';
	var regionInputId = this.regionInputId || params.regionInputId || 'ajax_region_select';
	var url = this.url || params.url || "/vote/ajax/getRegionsHTMLSelect.htm";
  var selectNameId = this.selectNameId || params.selectNameId || 'votingRegion';

	var stateId = document.getElementById(stateInputId).value;
	if (!stateId) stateId = this.value;

	var region = document.getElementById(regionInputId);
	var regionId = region.value;
	
	var selectName = document.getElementById(selectNameId).name;

	var callback = {
		success: function(response) {
			if(response.responseText !== undefined) {
				document.getElementById('ajax_region_select').innerHTML = response.responseText;
			}
		},
		failure: function(response) {
			YAHOO.log("AJAX Error, could not get regions! Returned: '"+response.statusText+"'", "error", "load-regions.js");
		}
	};

	if(stateId || regionId || regionLabel){
		url += "?1=1";
		if(stateId){
			url += "&stateId="+stateId;
		}
		if(regionId){
			url += "&regionId="+regionId;
		}
		url += "&name="+selectName + "&selectId=" + selectNameId;
	}
	YAHOO.util.Connect.asyncRequest('get', url, callback);
};

function getJsonRegions( params ) {
	var stateInputId = this.stateInputId || params.stateInputId || 'select_state';
	var regionInputId = this.regionInputId || params.regionInputId || 'ajax_region_select';
	var url = this.url || params.url || "/vote/ajax/getJsonRegions.htm";

	var stateId = $("#"+stateInputId).val();

	var region = $("#"+regionInputId);
	var regionId = region.val();
    region.empty();
    region.append( $("<option/>", {value:0, text: "... loading "}));
    region.attr("disabled", true);

    $.getJSON( url, {stateId:stateId, regionId:regionId}, function( data ) {
        region.empty();
        region.attr("disabled", false);
        region.append( $("<option/>", {value:0, text: "- Select A Voting Region -"}));
		$.each( data, function( indx, option ){
			region.append( $("<option/>", {value: option.id, text: option.name}).attr("selected", option.selected) );  //add new options
		})
	});

};

function getJsonRegions2( params ) {
    var stateInputId = this.stateInputId || params.stateInputId || 'select_state';
    var regionInputId = this.regionInputId || params.regionInputId || 'ajax_region_select';
    var url = this.url || params.url || "/vote/ajax/getJsonRegions2.htm";

    var element = $("#"+regionInputId).select2({
        placeholder: "Select voting region",
        allowClear: true,
        ajax: {
            url: url,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    q: params.term, // search term
                    page: params.page || 1,
                    stateId: $("#"+stateInputId).val()
                };
            },
            processResults: function (data, params) {
                // parse the results into the format expected by Select2.
                // since we are using custom formatting functions we do not need to
                // alter the remote JSON data
                var pageN = params.page || 1;
                var more = (pageN * 50) < data.total_count; // whether or not there are more results available

                // notice we return the value of more so Select2 knows if more results can be loaded
                return { results: data.items, pagination: { more: more } };
            },
            cache: true
        }
    });
    return element;
};
