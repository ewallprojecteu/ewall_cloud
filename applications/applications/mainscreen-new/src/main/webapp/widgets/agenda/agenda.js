var agendaW = angular.module("ewallAgenda", []);
agendaW.controller('AgendaWidgetController', ['$scope', '$stateParams', '$interval', function ($scope, $stateParams, $interval) {
	var AgendaWidgetInterval;
	var AgendaWidgetIntervalMs = 60*1000; /*1 minute*/
	var aw_username = ewallApp.currentSession.user.username;
	var aw_usertimezone = ewallApp.currentSession.user.userProfile.vCardSubProfile.timezoneid;
	var aw_use_usertimezone = false;
	
	var AWPathToSB = "../service-brick-calendar";
	
	var AWEndPoints = [
		'/medicalCheckup?username='+aw_username+'&date=', 
		'/exercises?username='+aw_username+'&date=', 
		'/selfcare?username='+aw_username+'&date=', 
		'/socializing?username='+aw_username+'&date=', 
		'/task?username='+aw_username+'&date='
	];
	
	var myRandom = Math.random();
	console.log("Loaded AgendaWidgetController #####" + myRandom);

	$scope.loading = true;
	$scope.agendaItem = [];
	if(aw_use_usertimezone) {
		$scope.today = moment.tz(aw_usertimezone).format("DD-MM-YYYY");
	} else {
		$scope.today = moment().format("DD-MM-YYYY");
	}
	
    GetAgendaWidgetItems(AWPathToSB, AWEndPoints, aw_usertimezone, aw_use_usertimezone, PrepareAgendaWidgetItems, $scope);
    
    var stopAgendaWidgetInterval = function (promise) {
     	console.log("Stopping AgendaWidgetInterval loop...");
        $interval.cancel(promise);
    }

    ewallApp.currentSession.stopAgendaWidgetInterval = stopAgendaWidgetInterval;
    
    ewallApp.currentSession.AgendaWidgetInterval = $interval(function() {
    	if(!$scope.loading) {
    		$scope.loading = true;
    		GetAgendaWidgetItems(AWPathToSB, AWEndPoints, aw_usertimezone, aw_use_usertimezone, PrepareAgendaWidgetItems, $scope);
        }
    }, AgendaWidgetIntervalMs);
    
//	$scope.$on('$destroy', function() {
//		$interval.cancel(AgendaWidgetInterval);
//	});

    function GetAgendaWidgetItems(path_sb, end_points_arr, user_timezone, use_usertimezone, callback, $scope) {
    	console.log("Getting agenda items #####" + myRandom);
    	var count_end_points = end_points_arr.length, result_end_points = [];
        if(count_end_points == 0) {   
        	callback(result_end_points, user_timezone, use_usertimezone, $scope);
            return;
        }
    	
        var url_sb = "", user_moment_timestamp = 0; 
        if(use_usertimezone) {
        	user_moment_timestamp = moment.tz(user_timezone).valueOf();
        } else {
        	user_moment_timestamp = moment().valueOf();
        }
        
        $.each(end_points_arr, function(inx, endPoint) {
        	url_sb = path_sb+endPoint+user_moment_timestamp;
        	
        	ewallApp.ajax({
    			url: url_sb,
    			type: "GET",
    			dataType: "json", 
    			async: true,
    			cache: true, 
    			success: function(ReceiveAgendaWidgetData) {
    				$.merge(result_end_points, ReceiveAgendaWidgetData);
    				count_end_points = count_end_points - 1;
    	            if(count_end_points == 0) {
    	                callback(result_end_points, user_timezone, use_usertimezone, $scope);
    	            }
    			}, 
    			error: function() {
    				count_end_points = count_end_points - 1;
    	            if(count_end_points == 0) {
    	                callback(result_end_points, user_timezone, use_usertimezone, $scope);
    	            }
    			}
    		});
    	});
    };

    function PrepareAgendaWidgetItems(receive_agenda_data, user_timezone, use_usertimezone, $scope) {
    	$scope.agendaItem = [];
    	if(use_usertimezone) {
    		$scope.today = moment.tz(user_timezone).format("DD-MM-YYYY");
    	} else {
    		$scope.today = moment().format("DD-MM-YYYY");
    	}
    	
    	if(use_usertimezone) {
    		var user_moment = moment.tz(user_timezone);
    	} else {
    		var user_moment = moment();
    	}
    	var user_moment_arr = user_moment.toArray();
    	var eventItems = [];
    	
    	$.each(receive_agenda_data, function(inx, receive_data) {
    		var receive_moment = receive_data.startDate;
    		var utc_receive_moment = moment.utc(receive_moment);
    		var utc_receive_moment_arr = utc_receive_moment.toArray();
    		
    		if(user_moment_arr[0] == utc_receive_moment_arr[0] && user_moment_arr[1] == utc_receive_moment_arr[1] && user_moment_arr[2] == utc_receive_moment_arr[2]) {
    			if(utc_receive_moment_arr[3] > user_moment_arr[3]) {
    				eventItems[eventItems.length] = {'startMoment':utc_receive_moment, 'title':receive_data.title};
    			} else if(utc_receive_moment_arr[3] == user_moment_arr[3] && utc_receive_moment_arr[4] >= user_moment_arr[4]) {
    				eventItems[eventItems.length] = {'startMoment':utc_receive_moment, 'title':receive_data.title};
    			}
    		}
    	});
    	
    	eventItems.sort(function(a, b) {
    		return a["startMoment"] - b["startMoment"];
    	});
    	
    	$.each(eventItems, function(ready_key, ready_data) {
    		$scope.agendaItem[ready_key] = {'start':ready_data.startMoment.format("HH:mm"), 'title':ready_data.title};
    		if(ready_key >= 1) {
    			return false;
    		}
    	});
    	
    	$scope.loading = false;
    };
}]);

