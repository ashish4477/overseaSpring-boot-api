<div class="wide-content">
	<div class="hd">
	
		<h2 class="title">Voter Help Desk</h2>
		
	</div>
	<div class="bd">
		<script type="text/javascript">
		/* <![CDATA[ */
		(function() {

			// matching the get string line with its
			// corresponding category number. these
			// seem to be generated at random, or in
			// some arcane way that will make this
			// particular way of doing things
			// somewhat brittle. Changes to the
			// kayako system may break this script.
			var categories = {
				'address-related': 1,
				'balloting-issues': 25,
				'deadlines': 4,
				'eligibility': 11,
				'fwab': 7,
				'form-related-questions': 9,
				'glossary': 12,
				'party-affiliation': 8,
				'primary-elections': 10,
				'registration-issues': 26,
				'related-issues': 13,
				'security': 15,
				'us-citizens-without-residency': 5,
				'uniformed-services-voters': 20,
				'voter-identification': 14
			};

			var url = "/vhd/index.php";
			var prefix = "?_m=knowledgebase&_a=view&parentcategoryid=";
			var suffix = "&pcid=&nav=";


			var iFrameHeight = function() {
				var frame = document.getElementById("blockrandom");
				var frameDocument = null;

				/*
					Different browsers have different ways of accessing the inner document
					element of an iframe. The following lines detect which one is in use
					by the browser and abstract it for later code.

					Safari and Mozilla use frame.contentDocument
					IE6 (and IE7?) use frame.contentWindow
					Opera (and others?) use frame.document
				*/
				if(frame.contentDocument) {
					frameDocument = frame.contentDocument;
				} else if(frame.contentWindow) {
					frameDocument = frame.contentWindow.document;
				} else {
					frameDocument = frame.document;
				}

				/*
					A container function for a timed repeating function that checks the
					height of the iframe content and resizes the iframe box accordingly.
				*/
				var resize = function() {
					var repeats = 5;
					var baseHeight = -1;
					var timeout = null;
					var interval = 200;

					var _resizeFunc = function() {
						/* If the height of the content is different than the height of the iframe, reset the count and
						change the height of the frame. */
						if(baseHeight < frameDocument.body.scrollHeight || baseHeight > frameDocument.body.scrollHeight) {
							repeats = 5;
							baseHeight = frameDocument.body.scrollHeight;
							frame.height = baseHeight;
							timeout = setTimeout(_resizeFunc, interval);

						/* If the height is the same, but the count isn't zero, decrement the count and set the function to
						call again in the given number of miliseconds. If it is zero, clear the timeout. */
						} else if(baseHeight == frameDocument.body.scrollHeight) {
							if(repeats == 0) {
								clearTimeout(timeout);
							} else {
								repeats = repeats - 1;
								timeout = setTimeout(_resizeFunc, interval);
							}
						}					
					}
					// set the ball rolling
					_resizeFunc();
				}
				resize();

				// if text is entered into one of the fields, call resize on it.
				YAHOO.util.Event.addListener(frameDocument.body, 'keypress', resize);
			}

			YAHOO.util.Event.addListener('blockrandom', 'load', iFrameHeight);

			YAHOO.util.Event.addListener(window, 'load', function() {	
				var b = document.getElementById('blockrandom');
				var reg = /category=([\w-]+)/;
				if(reg.test(window.location)) {
					var category = reg.exec(window.location)[1];

					if(category != 'default') {
						b.src = url + prefix + categories[category] + suffix;
					}
				}

				if(document.getElementById('blockrandom').readyState && YAHOO.util.Dom.get('blockrandom').readyState == 'complete') {
					iFrameHeight();
				}
			});

		})();
		/* ]]> */
		</script>

		<p>
			<iframe width="100%" scrolling="no" frameborder="0" align="top" class="wrappersupportwra" src="/vhd" name="iframe" id="blockrandom">
				<br />
				This option will not work correctly. Unfortunately, your browser does not support Inline Frames
				<br />
			</iframe>
		</p>
	</div>
</div><!-- end bd -->
