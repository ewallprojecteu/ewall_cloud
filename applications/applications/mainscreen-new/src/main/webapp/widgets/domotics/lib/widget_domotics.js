var domoticsWidget = angular.module("domoticsWidget", []);

//assign angular controller to module
domoticsWidget.controller('domoticsWidgetController', ['$scope', '$stateParams', '$interval', function ($scope, $stateParams, $interval) {
	var myRandom = Math.random();
	console.log("Loaded domoticsWidgetController #####" + myRandom);

	var WidgetObjectDomotics_NewInstance = new WidgetObjectDomotics();
	WidgetObjectDomotics_NewInstance.SetIntervalMS(10 * 1000);
	/*set interval in milliseconds: 5*1000=5sec; 0 - get data from SBs only once;*/
	WidgetObjectDomotics_NewInstance.InitWidgetObject();
		
    var stopRecurringDomoticsRequest = function (promise) {
    	console.log("Stopping Domotics Widget scheduled requests #####" + myRandom)
        $interval.cancel(promise);
    }
    
    ewallApp.currentSession.stopRecurringDomoticsRequest = stopRecurringDomoticsRequest;

    ewallApp.currentSession.recurringDomoticsRequest = $interval(function() {
    	WidgetObjectDomotics_NewInstance.StartGetExternalData();
    }, WidgetObjectDomotics_NewInstance.GetIntervalMS());
    

jQuery.support.cors = true;
//==============================================================================
function WidgetObjectDomotics() {
	var ExternalStatusData=[];
	var EnvLocationSupportStr='livingroom';
	var ExternalParamNames=['temperature', 'humidity'];
	var ExternalParamDimensions=['&deg;C', '%'];
	var ObjectsHtmlID=['#domotics-widget-temp span', '#domotics-widget-humidity span'];
	
	var Domain=window.location.hostname,PathToSB_Domotics="";
	var UserName="",UserTimezoneOlson="";
	
	var isGettingData=false;
	var IntervalMS=0,IntervalWidgetObject=0;
	
	var AppToken='';
	var ObjectLocalIndependentTest=null;
	//==============================================================================
	//==============================================================================
	this.GetIntervalMS=function() {
		return IntervalMS;
	}
	this.SetIntervalMS=function(ms) {
		if(ms > 1000)
			IntervalMS = ms;
	}
	//==============================================================================
	this.SetObjectLocalIndependentTest=function(my_object) {
		ObjectLocalIndependentTest = my_object;
	}
	//==============================================================================
	this.InitWidgetObject=function() {
		if(!InitExtUserAndDomainPathParams()) {
			return false;
		}
		InitCurrentContent();
		GetExternalData();
		
	}
	//==============================================================================
	//GET EXTERNAL DATA
	//==============================================================================
	this.StartGetExternalData=function() {
		if(!isGettingData) {
			isGettingData=true;
			GetExternalData();
		}
	}
	//==============================================================================
	//INIT FUNCTIONS
	//==============================================================================
	function InitExtUserAndDomainPathParams() {
		if(typeof ewallApp == 'object' && ewallApp != null) {
			UserName = ewallApp.currentSession.user.username;
			UserTimezoneOlson=ewallApp.currentSession.user.userProfile.vCardSubProfile.timezoneid;
			
			var current_domain = window.location.protocol+'//'+window.location.hostname;
			var current_pathname = window.location.pathname;
			var part_sub_dir = '';
			
			if(current_domain != Domain) {
				var path_from_api='';
				if(typeof ewallAppServices == 'object' && ewallAppServices != null) {
					var queue = ewallAppServices._invokeQueue;
					for(i in queue) {
						if(queue[i].length == 3) {
							if(queue[i][1] == 'constant' && queue[i][2].length == 2 && queue[i][2][0] == 'REST_API') {
								if("login" in queue[i][2][1]) {
								    path_from_api=queue[i][2][1]["login"];
									break;
								}
							}
						}
					}
				}
				
				if(path_from_api.length <= 0) {
					return false;
				}
				
   				var path_from_api_parts = GetLocation(path_from_api);  			
   				var domain_from_api=path_from_api_parts.protocol+'//'+path_from_api_parts.host;
   				var pathname_from_api=path_from_api_parts.pathname;
   				
   				PathToSB_Domotics = domain_from_api;
				part_sub_dir = ReturnPartSubDirFromPathname(pathname_from_api);
			} else {
				PathToSB_Domotics = current_domain;
				part_sub_dir = ReturnPartSubDirFromPathname(current_pathname);
			}
			
			if(part_sub_dir.length > 0) {
				PathToSB_Domotics += '/applications-'+part_sub_dir;
			}
			
			PathToSB_Domotics += '/service-brick-domotics';
			
			return true;
		}
		else if(typeof ObjectLocalIndependentTest == 'object' && ObjectLocalIndependentTest != null) {
			UserName=ObjectLocalIndependentTest.UserName;
			UserTimezoneOlson=ObjectLocalIndependentTest.UserTimezoneOlson;
			AppToken=ObjectLocalIndependentTest.token;
			
			PathToSB_Domotics = Domain+'/applications-'+ObjectLocalIndependentTest.PartSubDir+'/service-brick-domotics';
			return true;
		}
		else {
			return false;
		}
	}
	//==============================================================================
	function InitCurrentContent() {
		for(el_id in ObjectsHtmlID) {
			$(ObjectsHtmlID[el_id]).empty();
			$(ObjectsHtmlID[el_id]).html('---');
		}
	}
	//==============================================================================
	function GetExternalData() {
		console.log("***Getting EXTERNAL DOM data");
		var url_domotics_status = PathToSB_Domotics+"/v1/"+UserName+"/domotics";
		if(typeof ewallApp == 'object') {
			var ReqDomoticsWidget = ewallApp.ajax({
				url: url_domotics_status,
				type: "GET",
				dataType: "json", 
				async: true,
				cache: true,
				error: function(){isGettingData=false;}
			});
		} else {
			var ReqDomoticsWidget = $.ajax({
				url: url_domotics_status,
				headers: {'X-Auth-Token':AppToken},
				type: "GET",
				dataType: "json", 
				async: true,
				cache: true,
				error: function(){isGettingData=false;}
			});
		}
		
		$.when(ReqDomoticsWidget).done(function (DataDomoticsWidget) {
			ExternalStatusData=DataDomoticsWidget;
			LoadExternalData();
		});
	}
	//==============================================================================
	//LOAD EXTERNAL DATA
	//==============================================================================
	function LoadExternalData() {
		var current_date = new Date(); 
		current_date.setHours(0); current_date.setMinutes(0); 
		current_date.setSeconds(0); current_date.setMilliseconds(0);
		var current_date_ms = Date.parse(current_date);
		var flag_load_data=false;
		var ext_date_str="",ext_date={},ext_date_ms=0,ext_location='',ext_value=0,print_value='';
		for(i in ExternalStatusData) {
			ext_date_str = ExternalStatusData[i]['timestamp'];
			ext_location = ExternalStatusData[i]['location'].toLowerCase();
			ext_date       = ReturnOnlyDateConvertFromServiceBrickWithT(ext_date_str);
			ext_date_ms    = Date.parse(ext_date);
			
			if(current_date_ms != ext_date_ms) {continue;}
			if(ext_location !== EnvLocationSupportStr)  {continue;}
			
			for(j in ExternalParamNames) {
				ext_value=Math.round(ExternalStatusData[i][ExternalParamNames[j]]);
				if(ext_value <= 0) {print_value = "---";}
				else print_value=ext_value+' '+ExternalParamDimensions[j];
				
				$(ObjectsHtmlID[j]).empty();
				$(ObjectsHtmlID[j]).html(print_value);
				flag_load_data=true;
			}
		}
		if(!flag_load_data) {
			InitCurrentContent();
		}
		isGettingData=false;
	}
	//==============================================================================
	//HELP FUNCTIONS
	//==============================================================================
	function ReturnOnlyDateConvertFromServiceBrickWithT(date_for_convert) {
		var timestamp_arr = date_for_convert.substring(0, 19).split("T");
		var date_arr = timestamp_arr[0].split("-");
		return new Date((date_arr[0]*1), ((date_arr[1]*1)-1), (date_arr[2]*1));
	}
	//==============================================================================
	//==============================================================================
	function GetLocation(href) {
	    var match = href.match(/^(https?\:)\/\/(([^:\/?#]*)(?:\:([0-9]+))?)(\/[^?#]*)(\?[^#]*|)(#.*|)$/);
	    return match && {
	        protocol: match[1],
	        host: match[2],
	        hostname: match[3],
	        port: match[4],
	        pathname: match[5],
	        search: match[6],
	        hash: match[7]
	    }
	}
	//==============================================================================
	function ReturnPartSubDirFromPathname(pathname) {
		var match_platform = pathname.match(/^(\/platform-)(([-]*[_]*[0-9]*[a-z]*)+)/);
		var match_applications = pathname.match(/^(\/applications-)(([-]*[_]*[0-9]*[a-z]*)+)/);
		
		if(match_platform != null) {
			return match_platform[2];
		} else if(match_applications != null) {
			return match_applications[2];
		} else {
			return '';
		}
	}
	//==============================================================================
	//==============================================================================
}// end of object

}]);   