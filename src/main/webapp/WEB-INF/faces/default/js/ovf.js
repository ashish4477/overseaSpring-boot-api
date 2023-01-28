/******************************************************************************
This is the custom Javascript code for handing the Overseas Vote ajaxy code.

@author Nicholas Husher (nhusher@bear-code.com)
@version .7

JSLint Passed!

******************************************************************************/

YAHOO.namespace("ovf");

if(YAHOO.env.ua.ie == 6) {
	YAHOO.util.Event.addListener(window, "scroll", function(){
		var underlay = document.getElementById('underlay'),
			scrollTop = document.documentElement.scrollTop;

		YAHOO.util.Dom.setStyle(underlay, "top", scrollTop+"px");
	});
}

YAHOO.util.Event.onContentReady("account", function() {
	YAHOO.ovf.accountPanel = new YAHOO.bc.ExpandingPanel("account");
	
	YAHOO.ovf.accountPanel.onShow.subscribe(function() {
		YAHOO.util.Dom.setStyle("account-controls","background-position","0px -25px");
	});
	YAHOO.ovf.accountPanel.onHide.subscribe(function() {
		YAHOO.util.Dom.setStyle("account-controls","background-position","0px 0px");
	});
});

/**
The following code inititalizes any other things that need to be running by the time the page fully loads.
**/
YAHOO.util.Event.onDOMReady(function() {
	// custom function that empties form fields of their default values when they get focus and returns
	// that value when it loses focus if it has nothing in it.
	// operates only on <input> fields of the named class.
	YAHOO.bc.formSweeper('sweep');
});

YAHOO.util.Event.onContentReady("eod-corrections", function() {	
	var f = function(el) {
		var v = new YAHOO.bc.ExpandingPanel(el);
		
		// disables form fields as they are hidden
		var onHideFunc = function() {
			var e = v.getElement()
			var d = e.getElementsByTagName("input");
			for(var i = 0; i < d.length; i=i+1) {
				d[i].disabled = true;
			}
			
			d = e.getElementsByTagName("textarea");
			for(i = 0; i < d.length; i=i+1) {
				d[i].disabled = true;
			}
		}
		// enables form fields as they are shown
		var onShowFunc = function() {
			var e = v.getElement()
			var d = e.getElementsByTagName("input");
			for(var i = 0; i < d.length; i=i+1) {
				d[i].disabled = false;
			}
			
			d = e.getElementsByTagName("textarea");
			for(i = 0; i < d.length; i=i+1) {
				d[i].disabled = false;
			}
		}
		onHideFunc();
				
		v.onHide.subscribe(onHideFunc);
		v.onShow.subscribe(onShowFunc);
		
		return v;
	};
	
	var editableFields = YAHOO.util.Dom.getElementsByClassName("corrected","*","eodForm", f);
});


YAHOO.util.Event.onContentReady("eod-correction-approve", function() {
	var DOM = YAHOO.util.Dom;
	var els = YAHOO.util.Dom.getElementsByClassName('approve-deny', 'tr', 'eodForm');
	for(var i = 0; i < els.length; i = i+1) {
		YAHOO.bc.approveDenyHandler(els[i]);
	}
	var selectAlls = YAHOO.util.Dom.getElementsByClassName('select-all', 'input', 'eodForm');
	for(var i = 0; i < selectAlls.length; i = i+1) {
		var allControl = selectAlls[i];
		var enabled = allControl.checked;				
		var toggleAll = function(evt, allCtl) {
			var controls = DOM.getElementsByClassName('ctrl','input','eodForm');			
			for(var i = 0; i < controls.length; i = i+1) {
				var ctl = controls[i];
				ctl.checked = allCtl.checked;
				YAHOO.bc.approveDenyDelegate(ctl);
			}
		}		
		YAHOO.util.Event.addListener(allControl, 'change', toggleAll, allControl);
	}
});

/* The following code implements various function for working with AJAX and so on... */

YAHOO.ovf.sendSnapshotUrl = null;
YAHOO.ovf.sendSnapshot = function( params, responseId ) {

    var callback = {
        success: function(response) {
            var div = document.getElementById(responseId);
            if(response.responseText !== undefined && div != null) {
              div.innerHTML = "<br/>" + response.responseText;
              alert(response.responseText);
            }
        },
        failure: function(response) {
            YAHOO.log("AJAX Error, could not get regions! Returned: '"+response.statusText+"'", "error", "ovf.js");
        }
    };
    if ( YAHOO.ovf.sendSnapshotUrl == null ) {
        YAHOO.log("Configuration error: URL for AJAX call hasn't been defined", "error", "ovf.js");
        return;
   }
    YAHOO.util.Connect.setForm("emptyForm");
    YAHOO.util.Connect.asyncRequest('get', YAHOO.ovf.sendSnapshotUrl+'?'+params, callback);

};

YAHOO.util.Event.onDOMReady(function() {
	var rbs = YAHOO.util.Dom.getElementsByClassName('rava-bubble');
	var b, elementHeight;

	var getHeight = function(type, args, me) {
		var tooltipHeight;
		
		if(YAHOO.env.ua.ie > 0 &&  YAHOO.env.ua.ie < 7) {
			elementHeight = document.getElementById('wrapper').scrollHeight;
		} else {
			elementHeight = YAHOO.util.Dom.getRegion('wrapper').bottom;
		}
		
		if(YAHOO.env.ua.ie > 0 && YAHOO.env.ua.ie < 7) {
			tooltipHeight = elementHeight + YAHOO.util.Dom.getElementsByClassName('tooltip','*',this.getElement())[0].scrollHeight + 10;
		} else {
			tooltipHeight = YAHOO.util.Dom.getRegion(this.getElement()).bottom;
		}

		if(window.parent) {
			if(this.isShown()) {
				window.parent.YAHOO.ovf.resizeRavaFrame(tooltipHeight);
			} else {
				window.parent.YAHOO.ovf.resizeRavaFrame(elementHeight);
			}
		}			
	};

	for(var i = 0; i < rbs.length; i++) {
		b = new YAHOO.bc.TooltipBubble(rbs[i]);
		b.beforeShowEvent.subscribe(getHeight, b, true);
		b.hideEvent.subscribe(getHeight, b, true);
	}
});

YAHOO.util.Event.onContentReady("progress-bar", function() {
	YAHOO.ovf.ravaProgressBar = new YAHOO.bc.ProgressBar("progress-bar");
	YAHOO.ovf.ravaProgressBar.forwardButtonEnabled(true);
});

YAHOO.util.Event.onContentReady("more-page-links-opener", function(el) {
	YAHOO.util.Event.addListener('more-page-links-opener', "click", function() {
		YAHOO.util.Dom.setStyle('more-page-links', 'display', 'inline');
		YAHOO.util.Dom.setStyle('more-page-links-opener', 'display', 'none');
	});
});

YAHOO.util.Event.onContentReady("more-page-links", function() {
	YAHOO.util.Dom.setStyle('more-page-links', 'display', 'none');
});

YAHOO.util.Event.onDOMReady(function() {
    if(document.getElementById("fwab-start-box")) {
        var Event = YAHOO.util.Event,
            yes_check = document.getElementById('fwab_start_yes'),
            no_check = document.getElementById('fwab_start_no'),
            unsure_check = document.getElementById('fwab_start_unsure');
        Event.on(yes_check, "click", function(eventObj) {
            if(yes_check.checked){
                no_check.checked = false;
                unsure_check.checked = false;
            }
        });
        Event.on(no_check, "click", function(eventObj) {
            if(no_check.checked){
                yes_check.checked = false;
                unsure_check.checked = false;
            }
        });
        Event.on(unsure_check, "click", function(eventObj) {
            if(unsure_check.checked){
                yes_check.checked = false;
                no_check.checked = false;
            }
        });
    }
});

// YAHOO.util.Event.onDOMReady(function() {
// 	if(document.getElementById("fwab-start-box")) {
// 		var Dom = YAHOO.util.Dom,
// 			Event = YAHOO.util.Event,
// 			options = document.getElementById("who-can-use-fwab");
// 			goButton = document.getElementById("go-button");
		
// 		var value = 0;
// 		var hide = function() {
// 			Dom.setStyle(goButton,'visibility','hidden');
// 		};
// 		var show = function() {
// 			Dom.setStyle(goButton,'visibility','visible');
// 		};

// 		if(YAHOO.util.Anim) {
// 			Dom.setStyle(goButton, 'opacity', 0);
			
// 			var animIn = new YAHOO.util.Anim(goButton, { 'opacity': { to: 1 }}, 0.2);
// 			var animOut = new YAHOO.util.Anim(goButton, { 'opacity': { to: 0}}, 0.2);
			
// 			animIn.onStart.subscribe(function() {
// 				Dom.setStyle(goButton, 'visibility','visible');
// 			});
// 			animOut.onComplete.subscribe(function() {
// 				Dom.setStyle(goButton, 'visibility','hidden');
// 			});
			
// 			show = function() {
// 				if(animOut.isAnimated()) {
// 					animOut.stop();
// 				}
// 				animIn.animate();
// 			};
// 			hide = function() {
// 				if(animIn.isAnimated()) {
// 					animIn.stop();
// 				}
// 				animOut.animate();
// 			}
// 		}
		
// 		Event.on(options, "click", function(eventObj) {
// 			var target = Event.getTarget(eventObj);
			
// 			if(Dom.hasClass(target, 'step')) {
// 				value += (target.checked) ? 1 : -1;
// 			}
			
// 			if(value >= 1){
// 				show();
// 			} else {
// 				hide();
// 			}
// 		});
// 	}
// });

// EOD accordion
YAHOO.util.Event.onContentReady("eod-form", function() {
	var elementIds = [
		"addressinfo",
		"addressinfo_uocava",
		"contactdetails",
		"contactdetails_uocava",
		"stateinfo",
		"transoptions",
		"witnotreqs",
		"electiondeadlines",
		"statedeadlines",
		"caucusdeadlines",
		"federaldeadlines",
		"idrequires",
		"eligrequires",
		"idvotingmethods",
		"websitesinfo",
		"additionalinfo",
		"additionalinfo_uocava",
		"transoptionsDomestic",
		"stateinfoDomestic",
		"statelookuptools",
		"statelookuptoolsDomestic"
	];
	
	
	for(var i=0; i < elementIds.length; i++){
		var id = elementIds[i];
		if(document.getElementById(id)){
			YAHOO.ovf.addressinfoPanel = new YAHOO.bc.ExpandingPanel(id);
			YAHOO.ovf.addressinfoPanel.hide();		
		}
	}
});

var CONTENT_URLS = {
	'news': '239',
	'research': '242',
	'move': '243',
	'get-involved': '244'
};
YAHOO.util.Event.onContentReady('secondary-content-tabs', function() {
	var Dom = YAHOO.util.Dom, getElementsByClassName = Dom.getElementsByClassName, getHeight, a,
		panel = document.getElementById('secondary-content-panel'), c;
		
	getHeight = function(el) {
		var height, styleProp, currentStyle = {}, testStyle = {
			'visibility': 'hidden',
			'position': 'absolute',
			'display': 'block',
			'height': 'auto'
		};

		for(styleProp in testStyle) {
			if(el.style[styleProp]) {
				currentStyle[styleProp] = el.style[styleProp];
			}
			el.style[styleProp] = testStyle[styleProp];
		}
		height = el.offsetHeight;

		for(styleProp in testStyle) {
			if(currentStyle[styleProp]) {
				el.style[styleProp] = currentStyle[styleProp];
			} else {
				el.style[styleProp] = '';
			}
		}
		
		//console.log(height);

		return height;
	};
	
	
	jQuery(document).ready(function($) {
      })
	
	
	c = {
		success: function(d) {
			Dom.removeClass(panel, 'loading')
			panel.innerHTML = d.responseText;
			// load the twitter widget
			if(document.getElementById('twtr-profile-widget')){
				$("#twtr-profile-widget").tweet({
		          username: "overseasvote",
		          avatar_size: 0,
		          count: 4,
		          loading_text: "loading tweets..."
		        });						
 			}
		},
		failure: function(d) {}
	};
	
	YAHOO.util.Event.on('secondary-content-tabs', 'click', function(e) {
		var tab = YAHOO.util.Event.getTarget(e).parentNode,
			tabs = getElementsByClassName('tab','*','secondary-content-tabs'),
			reg = /(.+)\s+tab/, tabClass = '';
			
		if(Dom.hasClass(tab, 'active')) { return; }

		for(var i = 0, l = tabs.length; i < l; i++) {
			if(Dom.hasClass(tabs[i], 'active')) {
				Dom.removeClass(tabs[i],'active');
				tabClass = reg.exec(tabs[i].className)[1];
			}
		}

		Dom.removeClass(panel.parentNode.parentNode, tabClass);
		tabClass = reg.exec(tab.className)[1];
		Dom.addClass(panel.parentNode.parentNode, tabClass);
		Dom.addClass(tab, 'active');

		YAHOO.util.Connect.asyncRequest('GET', '/ajax-node/node/' + CONTENT_URLS[tabClass], c);
	});
	
	YAHOO.util.Connect.asyncRequest('GET', '/ajax-node/node/' + CONTENT_URLS['news'], c);
});

// Search form setup
YAHOO.util.Event.onContentReady("search-form", function(el) {
	var searchForm = document.getElementById("search-form");
	var searchTermsInput = document.getElementById("search-terms");
	var searchTypeSelect = document.getElementById('seach-type-select');
	var defaultTerms = 'Enter Search Terms...';
	if(searchTermsInput.value == ''){
		searchTermsInput.value = defaultTerms;
	}

	YAHOO.util.Event.addListener(searchTermsInput, "focus", function(el) {
		if(searchTermsInput.value == defaultTerms){
			searchTermsInput.value = '';
		}
	});
    /* Deprecated
	YAHOO.util.Event.addListener(searchForm, "submit", function(el) {
		switch(searchTypeSelect.options[searchTypeSelect.selectedIndex].value) {
		 case 'website':
		 	searchForm.action = '/search/node';
		 	searchTermsInput.name = 'keys';
		 	break;
		 case 'eod':
		 	searchForm.action = '/vote/EodSearch.htm';
		 	searchTermsInput.name = 'search';
		 	break;
		 case 'helpdesk':
		 	searchForm.action = 'https://vhd.overseasvotefoundation.org/unified/index.php?group=ovf';
		 	searchTermsInput.name = 'searchquery';
		 	break;
		}
	});
	*/
});

YAHOO.util.Event.onContentReady("search-form-top", function(el) {
	var searchTermsInputTop = document.getElementById("search-terms-top");
	var defaultTerms = 'Enter Search Terms...';
	if(searchTermsInputTop.value == ''){
		searchTermsInputTop.value = defaultTerms;
	}

	YAHOO.util.Event.addListener(searchTermsInputTop, "focus", function(el) {
		if(searchTermsInputTop.value == defaultTerms){
			searchTermsInputTop.value = '';
		}
	});
});



