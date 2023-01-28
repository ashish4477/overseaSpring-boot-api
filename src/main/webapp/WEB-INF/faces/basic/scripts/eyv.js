YAHOO.namespace("ovf");

/// now it is done on server side (see EyvFirst.jsp)
///$(document).ready(function(){
	/// break the states list into columns
///	breakColumns("#states010 > li", "#states0%0", 5);
///}); //close doc ready

// universal list break

function breakColumns (what, where, columnsNumber) {
//	var columnsNumber = 4;
	var listSize = $(what).size();
	var columnSize = Math.ceil(listSize / columnsNumber);
	var fillCounter = 0;
	var columnCounter = 0;
	
	var columns = new Array();
	columns[0] = new Array();

	$(what).each(function (i) {
		if (fillCounter >= columnSize) {  // go to the next column
			columnCounter++;
			columns[columnCounter] = new Array();
			fillCounter = 0;
		}

		var li = this;
		columns[columnCounter][fillCounter] = li;
		fillCounter++;
	});


	for (var k = 1; k < columns.length; k++) {
		var cn = k + 1;
		for (var i = 0; i < columns[k].length; i++) {
			$(columns[k][i]).appendTo(where.replace('%', cn));
		}
	}
}


YAHOO.util.Event.onContentReady("content", function() {
	var rbs = YAHOO.util.Dom.getElementsByClassName('rava-bubble','*','content');
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


YAHOO.ovf.resizeRavaFrame = function(newHeight) {
	if(newHeight < 336) { newHeight = 336; }
	var d = document.getElementById("overlay-rava-iframe");
	if(d !== null) {
		var panelTopEdge = parseInt(YAHOO.util.Dom.getStyle("overlay-rava","top"), 10);
		var panelHeight = parseInt(YAHOO.util.Dom.getStyle("overlay-rava","height"), 10);			
		var iFrameHeight = parseInt(YAHOO.util.Dom.getStyle("overlay-rava-iframe","height"), 10);
		
		d.style.height = newHeight + "px";
		YAHOO.widget.Overlay.windowResizeEvent.fire();
	}
};

jQuery(document).ready(function() {
	var POPUP_TEMPLATE = '' +
		'<div id="eyv-faq-popup" style="display:none">' + 
			'<div class="popup-inner">' +
				'<iframe style="visibility:hidden"></iframe>' +
				'<span class="loading-indicator"></span>' +
				'<span class="close"></span>' +
			'</div>' +
		'</div>',
		POPUP_IFRAME, LOADING, POPUP;
		
	var initPopup = function() {
		jQuery(document.body).append(POPUP_TEMPLATE);
		
		POPUP = jQuery('#eyv-faq-popup');
		POPUP_IFRAME = jQuery('#eyv-faq-popup iframe');
		LOADING = jQuery('#eyv-faq-popup .loading-indicator');
		
		POPUP_IFRAME.bind('load', function() {
			POPUP_IFRAME.css('visibility', '');
			LOADING.css('display','none');
		});
		
		POPUP.find('.close').click(function() {
			POPUP.css('display','none');
		});
	};
	
	jQuery('a.frame-link').click(function() {
		if(!POPUP) { initPopup(); }

        window.scrollTo(0,0);
		POPUP_IFRAME.css('visibility','hidden');
		POPUP_IFRAME.attr('src', this.href);		
		POPUP.css('display', '');
		LOADING.css('display','');		
		
		return false;
	});
});

