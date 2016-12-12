ewallApp.service('hueNotificationService', function($http, $timeout){
	
	var lightState={};
	var hueIp = ewallApp.currentSession.hueIp;
	var hueUser = ewallApp.currentSession.hueUser;
	var hueBaseUrl = 'http://'+hueIp+'/api/'+hueUser;
	
	var getPreviousStatus = function(){
		$http({
	    	  method: 'GET',
	    	  url: hueBaseUrl + '/lights/' + 1
	    }).then(function(response){
	    	console.log('Light State %O', response)
		    lightState.isOn = response.data.state.on;
		    lightState.color = response.data.state.hue;
		    lightState.bri = response.data.state.bri;
	    	//console.log('Previous Hue Status: %O',lightState);
	    });
	}
	
	var restorePreviousStatus = function(){
		//console.log('!!! Light state: ' + lightState.isOn);
		if(lightState.isOn==true){
			$http({
				method: 'PUT',
				url: hueBaseUrl + '/lights/1/state',
				data: {
					"on":"true",
					"hue": lightState.color,
					"alert":"none"
					}
			}).then(function(response){
				console.log('Previous hue status correctly restored: now color id is again ' + lightState.color);
			})
		}
		else {
    		$http({
				method: 'PUT',
				url: hueBaseUrl + '/lights/1/state',
				data: {
					"on":false
					}
			}).then(function(response){
				console.log('Previous hue status correctly restored: now hue is turned off again');
			});
		}
	}
	
	return {
		throwNotification: function (color) {
			getPreviousStatus();
			console.log('Color received is: '+color);
			var colorToBeSet = '';
			switch(color){
				case 'red':
					colorToBeSet = 65280;
					break;
				case 'yellow':
					colorToBeSet = 12750;
					break;
				case 'green':
					colorToBeSet = 25500;
					break;
			}
	    	$http({
				  method: 'PUT',
				  url: hueBaseUrl + '/lights/' + 1 + '/state',
				  data: {
					  	"on":true,
					  	"hue": colorToBeSet,
					  	"bri":255,
					  	"transitiontime":"10"
				  		}
			}).then(function(response){
				console.log('Started notification...');
			});
	    	//restore previous status
	    	$timeout(function(){
	    			restorePreviousStatus();
	    		}, 5000);
	    	}
		}
});