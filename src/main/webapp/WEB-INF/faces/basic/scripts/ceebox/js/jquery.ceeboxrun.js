    jQuery(document).ready(function(){
		debugging = false;
		$.fn.ceebox.videos.base.param.allowScriptAccess = "sameDomain" //added to kill the permissions problem
		$.extend($.fn.ceebox.videos,{
			uctv:{
				siteRgx: /uctv\.tv\/search\-details/i, 
				idRgx: /(?:showID=)([0-9]+)/i, 
				src: "http://www.uctv.tv/player/player_uctv_bug.swf",
				flashvars: {previewImage : "http://www.uctv.tv/images/programs/[id].jpg", movie : "rtmp://webcast.ucsd.edu/vod/mp4:[id]",videosize:0,buffer:1,volume:50,repeat:false,smoothing:true}
			}
		});
		$(".ceebox").ceebox({titles:false, borderColor:'#dcdcdc',boxColor:"#fff"});
		$("map").ceebox();		
		$(".ceebox2").ceebox({unload:function(){}});
	});