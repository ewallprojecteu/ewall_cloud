var physicalActivityWidget = angular.module("physicalActivityWidget", []);

//assign angular controller to module
physicalActivityWidget.controller('physicalActivityWidgetController', ['$scope', '$stateParams', '$interval', function ($scope, $stateParams, $interval) {
	var myRandom = Math.random();
	console.log("Loaded physicalActivityWidgetController #####" + myRandom);
	
	var WidgetActivityObjectDPAM_NewInstance = new WidgetActivityObjectDPAM();
	WidgetActivityObjectDPAM_NewInstance.SetIntervalMS(10 * 1000);
	/*set interval in milliseconds: 5*1000=5sec; 0 - get data from SBs only once;*/
	WidgetActivityObjectDPAM_NewInstance.InitWidgetActivityObject();
		
    var stopRecurringDPAMRequest = function (promise) {
    	console.log("Stopping DPAM Widget scheduled requests #####" + myRandom)
        $interval.cancel(promise);
    }
    
    ewallApp.currentSession.stopRecurringDPAMRequest = stopRecurringDPAMRequest;

    ewallApp.currentSession.recurringDPAMRequest = $interval(function() {
    	WidgetActivityObjectDPAM_NewInstance.StartGetExternalData();
    }, WidgetActivityObjectDPAM_NewInstance.GetIntervalMS());
    

jQuery.support.cors = true;
//==============================================================================
//VERSION FOR NEW MAINSCREEN 
//==============================================================================
function WidgetActivityObjectDPAM() {
	var DataColorArr = ['#ffab00','#ffd600','#aeea00','#64dd17','#12c700'];
	var ExternalMovementData={"Steps":0,"Kilometers":0,"Calories":0};
	var DefaultUserGoalDaySteps=8000,UserGoalDaySteps=0,UserMaxDaySteps=0;
	var isGettingData=false;
	
	var Domain=window.location.hostname,
	PathToSB_PhysicalActivity="",PathToActivityGoal="";
	var UserName="",UserTimezoneOlson="";
	
	var IntervalMS=0;
	
	var ToDay={},DatesForExternalCall={};
	var FlagLoadUserGoalDaySteps=0;
	
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
	this.InitWidgetActivityObject=function() {
		if(!InitExtUserAndDomainPathParams()) {
			return false;
		}
		
		InitDatesForExternalCall();
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
   				
   				PathToSB_PhysicalActivity = domain_from_api;
				PathToActivityGoal = domain_from_api;
				part_sub_dir = ReturnPartSubDirFromPathname(pathname_from_api);
			} else {
				PathToSB_PhysicalActivity = current_domain;
				PathToActivityGoal = current_domain;
				part_sub_dir = ReturnPartSubDirFromPathname(current_pathname);
			}
			
			if(part_sub_dir.length > 0) {
				PathToSB_PhysicalActivity += '/applications-'+part_sub_dir;
				PathToActivityGoal += '/platform-'+part_sub_dir;
			}
			
			PathToSB_PhysicalActivity += '/service-brick-physicalactivity';
			PathToActivityGoal += '/idss-automatic-goal-setting';
			
			return true;
		}
		else if(typeof ObjectLocalIndependentTest == 'object' && ObjectLocalIndependentTest != null) {
			UserName=ObjectLocalIndependentTest.UserName;
			UserTimezoneOlson=ObjectLocalIndependentTest.UserTimezoneOlson;
			AppToken=ObjectLocalIndependentTest.token;
			
			PathToSB_PhysicalActivity = Domain+'/applications-'+ObjectLocalIndependentTest.PartSubDir+'/service-brick-physicalactivity';
			PathToActivityGoal = Domain+'/platform-'+ObjectLocalIndependentTest.PartSubDir+'/idss-automatic-goal-setting';
			return true;
		}
		else {
			return false;
		}
	}
	//==============================================================================
	function InitDatesForExternalCall() {
		ToDay = new Date(); ToDay.setHours(0); ToDay.setMinutes(0); ToDay.setSeconds(0); ToDay.setMilliseconds(0);
		
		var start_date = new Date(ToDay.getFullYear(), ToDay.getMonth(), ToDay.getDate());
		var end_date = new Date(start_date.getFullYear(), start_date.getMonth(), (start_date.getDate()+1));
		
		DatesForExternalCall={"StartDate":start_date, "EndDate":end_date};
	}
	//==============================================================================
	//==============================================================================
	function InitUserMaxDaySteps() {
		if(UserGoalDaySteps > 0)
			UserMaxDaySteps=Math.round(UserGoalDaySteps/0.81);
		else
			UserMaxDaySteps=Math.round(DefaultUserGoalDaySteps/0.81);
	}
	//==============================================================================
	function GetExternalData() {
		console.log("@#@#@# Getting data #####" + myRandom)
		var current_date = new Date();
		current_date.setHours(0); current_date.setMinutes(0); current_date.setSeconds(0); current_date.setMilliseconds(0);
		
		if(Date.parse(ToDay) != Date.parse(current_date)) {
			InitDatesForExternalCall();
			FlagLoadUserGoalDaySteps=0;
		}
		
		if(FlagLoadUserGoalDaySteps == 0) {
			GetExternalActivitiGoalAndMovementData();
		} else {
			GetExternalMovementData();
		}
	}
	//==============================================================================
	function GetExternalActivitiGoalAndMovementData() {
		console.log("***Getting EXTERNAL MOV and ACTIVITY data");
		var url_activity_goal = PathToActivityGoal+"/activitygoal";
		var act_goal_date_format = ReturnDateStrFormatForActivityGoal(DatesForExternalCall.StartDate);
		
		if(typeof ewallApp == 'object') {
			var ReqActivityGoalWidget = ewallApp.ajax({
				url: url_activity_goal,
				type: "GET",
				data: {userid: UserName, date: act_goal_date_format},
				dataType: "json",
				async: true,
				cache: true,
				error: function(){isGettingData=false;}
			});
		} else { 
			var ReqActivityGoalWidget = $.ajax({
				url: url_activity_goal, 
				headers: {'X-Auth-Token':AppToken},
				type: "GET",
				data: {userid: UserName, date: act_goal_date_format},
				dataType: "json",
				async: true,
				cache: true,
				error: function(){isGettingData=false;}
			});
		}
		
		$.when(ReqActivityGoalWidget).done(function (DataActivityGoalWidget) {
			if(DataActivityGoalWidget!==undefined && DataActivityGoalWidget["value"] !== null && typeof DataActivityGoalWidget["value"] === 'object' && DataActivityGoalWidget["value"]["goal"] > 0) {
				UserGoalDaySteps=DataActivityGoalWidget["value"]["goal"];
			} else {
				UserGoalDaySteps=0;
			}
			FlagLoadUserGoalDaySteps=1;
			InitUserMaxDaySteps();
			
			GetExternalMovementData();
		});
	}
	//==============================================================================
	function GetExternalMovementData() {
		console.log("***Getting EXTERNAL MOV data");
		var current_aggregation = "1d";
		var url_movement = PathToSB_PhysicalActivity+"/v1/"+UserName+"/movement";
		var from_date_format = ReturnDateStrFormatForServiceBrick(DatesForExternalCall.StartDate);
		var to_date_format = ReturnDateStrFormatForServiceBrick(DatesForExternalCall.EndDate);
		
		if(typeof ewallApp == 'object') {
			var ReqMovementWidget = ewallApp.ajax({
				url: url_movement,
				type: "GET",
				data: {from: from_date_format, to: to_date_format, aggregation:current_aggregation},
				dataType: "json", 
				async: true,
				cache: true,
				error: function(){isGettingData=false;}
			});
		} else {
			var ReqMovementWidget = $.ajax({
				url: url_movement, 
				headers: {'X-Auth-Token':AppToken},
				type: "GET",
				data: {from: from_date_format, to: to_date_format, aggregation:current_aggregation},
				dataType: "json",
				async: true,
				cache: true,
				error: function(){isGettingData=false;}
			});
		}
		
		$.when(ReqMovementWidget).done(function (DataMovementWidget) {
			if(DataMovementWidget!==undefined && DataMovementWidget["movements"].length == 1) {
				ExternalMovementData.Steps      = DataMovementWidget["movements"][0]["steps"];
				ExternalMovementData.Kilometers = DataMovementWidget["movements"][0]["kilometers"];
				ExternalMovementData.Calories   = DataMovementWidget["movements"][0]["burnedCalories"];
				
			} else {
				ExternalMovementData.Steps      = 0;
				ExternalMovementData.Kilometers = 0;
				ExternalMovementData.Calories   = 0;
			}
			
			LoadExternalMovementData();
		});
	}
	//==============================================================================
	//LOAD EXTERNAL DATA
	//==============================================================================
	function LoadExternalMovementData() {
		var current_user_steps=Math.round(ExternalMovementData.Steps);
		var max_user_steps=UserMaxDaySteps;
		
		if(current_user_steps > max_user_steps) {
			max_user_steps=current_user_steps;
		}
		
		var p1=Math.round((current_user_steps*100)/max_user_steps);
		var current_color=ReturnColor(p1);
		
		$('#step-badge').empty();
		$('#step-badge').text(current_user_steps);
		$('#step-badge').css({'border-color':current_color});
		
		$('.blobs div span').css({'background-color':current_color});
		
		isGettingData=false;
	}
	//==============================================================================
	//HELP FUNCTIONS
	//==============================================================================
	function ReturnDateStrFormatForServiceBrick(date_for_format) {
		if(!(date_for_format instanceof Date && !isNaN(date_for_format.valueOf()))) { 
			date_for_format = new Date(date_for_format);
		}
		
		var arr_date = [date_for_format.getFullYear(), date_for_format.getMonth(), date_for_format.getDate()];
		var string_format = 'YYYY-MM-DDTHH:mm:ss.SSSZ';
		var date_for_return = moment.tz(arr_date, UserTimezoneOlson).format(string_format);
		
		return date_for_return;
	}
	//==============================================================================
	function ReturnDateStrFormatForActivityGoal(date_for_format) {
		if(!(date_for_format instanceof Date && !isNaN(date_for_format.valueOf()))) { 
			date_for_format = new Date(date_for_format);
		}
		
		var arr_date = [date_for_format.getFullYear(), date_for_format.getMonth(), date_for_format.getDate()];
		var string_format = 'YYYY-MM-DD';
		var date_for_return = moment(arr_date).format(string_format);
		
		return date_for_return;
	}
	//==============================================================================
	function ReturnColor(p1) {
		var Color="";
		if(p1 <= 20) Color=DataColorArr[0];
		else if(p1 > 20 && p1 <= 40) Color=DataColorArr[1];
		else if(p1 > 40 && p1 <= 60) Color=DataColorArr[2];
		else if(p1 > 60 && p1 <= 80) Color=DataColorArr[3];
		else Color=DataColorArr[4];
		return Color;
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
}//end of object

}]);         


