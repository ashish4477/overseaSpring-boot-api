var tryCnt = 0;
var maxTry = 5;
var userSuccess = false;

/**
* Tries to pre-populate form fields using FB user info.
* Will not try to populate any field that is already set.
*/

function prePopulateForm(event, params) {
	tryCnt++;
	var sform = document.getElementById('userForm');

	//var callback = params.callback;
	//var callbackParams = params.callbackParams;
	if(sform && FB){
		// get user info
		FB.api('/me', function(response) {
			if(response && !response.error){
				userSuccess = true;
				
				function setFieldValue(name){
					if (field.value == "" || field.value == response.first_name){
						field.value = name;
					}	
				}
				
				var field = document.getElementById('firstName');
				setFieldValue(response.first_name);

				field = document.getElementById('middleName');
				setFieldValue(response.middle_name);
				//sets middle name to blank id not defined
				if (field.value == "undefined"){field.value = "";}

				field = document.getElementById('lastName');
				setFieldValue(response.last_name);

				field = document.getElementById('username');
				setFieldValue(response.email);

				// if Current City contains a comma, split else put whole thing into city
				var currentLocation = response.location.name;
				if (currentLocation.indexOf(",") != -1){
				currentLocation = currentLocation.split(','); 
				/* city & state combined by facebook with ajax autofill type hint */
				field = document.getElementById('currentCity');
				setFieldValue(currentLocation[0]);
				}
				else{
				field = document.getElementById('currentCity');
				setFieldValue(currentLocation);
				}

				/* set state
				field = document.getElementById('currentAddress.state');
				if(response.hometown && field.selectedIndex == 0){
					var hometown = response.hometown.name;
					if(hometown){
						for(i =0 ; i < field.options.length ; i++){
							var opt = field.options[i];
							if(hometown.toLowerCase().indexOf(opt.text.toLowerCase()) >= 0){
								field.selectedIndex = i;
							}
						}
					}
					field.value = response.hometown.name;
					callback(event, callbackParams);
				}*/

				// set gender 
				field = document.getElementById('gender');
				if(response.gender){
					for(i =0 ; i < field.options.length ; i++){
						var opt = field.options[i];
						if(response.gender.toLowerCase().indexOf(opt.text.toLowerCase()) >= 0){
							field.selectedIndex = i;
						}
					}
				}
				// set birthday fields
				if(response.birthday && response.birthday.match(/^\d\d\/\d\d\/\d\d\d\d$/)){
					var bd = response.birthday.split('/');
					field = document.getElementById('birthMonth');
					if(field && field.selectedIndex == 0){
						field.selectedIndex = bd[0];
					}
					field = document.getElementById('birthDate');
					if(field && field.selectedIndex == 0){
						field.selectedIndex = bd[1];
					}
					field = document.getElementById('birthYear');
					if(field && field.selectedIndex == 0){
						for(i =0 ; i < field.options.length ; i++){
							var opt = field.options[i]; 
							if(opt.text == bd[2]){
								field.selectedIndex = i;							
							}
						}
					}
				}
			}
			
			if (userSuccess == true){
				document.getElementById('status').style.display="inline-block";
				document.getElementById('fbText').innerHTML = 'Logout';
				}
			
			// re-try if necessary. Sometimes the API call is flaky on the initial page load
			if(!userSuccess && tryCnt < maxTry) {
				document.getElementById('status').style.display="none";
				setTimeout("prePopulateForm()", 1000);
			}
		});
	}
}  // function prePopulateForm()

