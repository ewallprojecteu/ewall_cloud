var ewallVirtualCoachNotification = angular.module("ewallVirtualCoachNotification", []);

ewallVirtualCoachNotification.controller('ewallVirtualCoachPAMMController', ['$scope', '$interval', 'REST_API', '$http', function ($scope, $interval, REST_API, $http) {
    // to do 
    // initialize global variables
    $scope.virtualCoachURL = REST_API.virtual_coach;
    $scope.virtualCoachPollURL = REST_API.virtual_coach + 'poll';
    $scope.PAMMAvailable = false; // flag to switch between showing small Robin with the hands down (false), or up (true)
    $scope.PAMMAvailableNr = 0; // number which will show in small Robin;s raised hand, when available   
    // define scope functions
    
    var getPAMMAvailable = function (virtualCoachPollURL) {
        try {
            // ajax calling the /poll service
            ewallApp.ajax({
                url: virtualCoachPollURL,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "text/plain");
                },
                data: {
                    username: ewallApp.currentSession.user.username
                        /* available: true */
                },
                dataType: "json",
                async: true,
                success: function (data) {
                    if (data) { // if data is not null or undefined
                        console.log(data);
                        if (Array.isArray(data)) { // it data is an array
                            if (data.length > 0) { // if data is not an empty array                                
                                $scope.PAMMAvailable = true; // set PAMMAvailable falg - this wil show the small Robin with the raised hand
                                $scope.PAMMAvailableNr = data.length; // set PAMMAvailableNr to the number if PAMM available types of messages - this will show in the yellow counter in small Robin's raised hand
                                
                                $scope.PAMMAvailableData = data;
                                if ($scope.PAMMAvailableData.type == undefined) {
                                    $scope.PAMMAvailableData.type = "available";
                                }

                                // set the service response in the $scope
                                $scope.$apply(); // force the controller to render the changed derrived from the above changes to the $scope model
                            }
                        } else {
                            console.log('This is not an array');
                            console.log(data);
                        }

                    }
                }
            });

        } catch (virtualCoachError) {
            console.log('virtualCoachError:');
            console.log(virtualCoachError);
        }
    }

    $scope.sendPAMMAvailable = function () {
        if ($scope.PAMMAvailableData.length == 1) { // the /poll response has a single element, on touching small Robin, send PAMM available
            // call PAMM available url 
            ewallApp.ajax({
                url: $scope.PAMMAvailableData[0].callbackURL,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "text/plain");
                },               
                dataType: "json",
                async: true,
                success: function (data) {
                    if (data.type !== "error") { // handle message
                        $scope.$parent.virtualCoach = {
                            showBigRobin: true,
                            bigRobinData: data,
                            inDialogue: true
                        };

                        $scope.inDialogue = true;
                        $scope.PAMMAvailable = false;
                    } else { // handle error
                        $scope.$parent.virtualCoach = {
                            showBigRobin: true,
                            bigRobinData: {
                                type: 'error',
                                subtype: '',
                                statements: [
                                    {
                                        type: 'text',
                                        value: 'Uh oh! Something went wrong'
                                    }
                            ],
                                responses: [
                                    {
                                        type: 'basic',
                                        label: 'OK',
                                        action: 'error',
                                        url: REST_API.virtual_coach + 'resetUser?username=' + ewallApp.currentSession.user.username
                                }
                            ]
                            },
                            inDialogue: true
                        };
                        $scope.inDialogue = true;
                        $scope.PAMMAvailable = false;

                    }
                    $('#main-screen-body').addClass('blur');
                    $scope.$apply();
                }
            });


        }
        
        if ($scope.PAMMAvailableData.length > 1) { // if /poll response is a set with mode then 1 element, sent the response to the $parent scope 
            $scope.$parent.virtualCoach = {
                showBigRobin: true,
                bigRobinData: $scope.PAMMAvailableData,
                inDialogue: false
            };
            // $scope.PAMMAvailable = false;
            $('#main-screen-body').addClass('blur');
            // $scope.PAMMAvailableNr -=1;
            // getPAMMAvailable($scope.virtualCoachPollURL);
        }
         
    }

    $scope.sendUserDialogue = function () {
        ewallApp.ajax({
            url: $scope.virtualCoachURL + 'startDialogue?username=' + ewallApp.currentSession.user.username + '&dialogueTypeId=HelloRobin',
            type: "GET",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "text/plain");
            },            
            dataType: "json",
            async: true,
            success: function (data) {
                if (data.type !== "error") { // handle message
                    $scope.$parent.virtualCoach = {
                        showBigRobin: true,
                        bigRobinData: data,
                        inDialogue: true
                    };
                    $scope.inDialogue = true;
                    $scope.PAMMAvailable = false;
                } else { // handle error
                    $scope.$parent.virtualCoach = {
                        showBigRobin: true,
                        bigRobinData: {
                            type: 'error',
                            subtype: '',
                            statements: [
                                {
                                    type: 'text',
                                    value: 'Uh oh! Something went wrong'
                                    }
                            ],
                            responses: [
                                {
                                    type: 'basic',
                                    label: 'OK',
                                    action: 'error',
                                    url: REST_API.virtual_coach + 'resetUser?username=' + ewallApp.currentSession.user.username
                                }
                            ]
                        },
                        inDialogue: true
                    };
                    $scope.inDialogue = true;
                    $scope.PAMMAvailable = false;

                }
                $('#main-screen-body').addClass('blur');
                $scope.$apply();
            }
        });
        
    }

    var stopVirtualCoachPAMMNotifications = function (promise) {
        console.log("Stopping virtualCoachPAMMNotificationLoop...");
        $interval.cancel(promise);
    }

    ewallApp.currentSession.stopVirtualCoachPAMMNotifications = stopVirtualCoachPAMMNotifications;


    // when angular renders the controller, initialize the virtual coach object
    angular.element(document).ready(function () {
        $scope.$parent.virtualCoach = { // initialized the $parent scope object for holding large /poll responses
            showBigRobin: false,
            bigRobinData: [],
            inDialogue: false,
            pollWaitDuration: 30
        };
        
        
        // bind and interval to the ewallAPP current session in which the PAMM /poll serivce is polled
        ewallApp.currentSession.virtualCoachPAMMNotificationLoop = $interval(function () {
            // look in this $scope Parent is the big Robin is on screen
            // only poll is the big Robin is not shown
           if($scope.$parent.virtualCoach.pollWaitDuration <= 0) {
               getPAMMAvailable($scope.virtualCoachPollURL);
               $scope.$parent.virtualCoach.pollWaitDuration = 30;
           } else {
               $scope.$parent.virtualCoach.pollWaitDuration -=1; 
           }            
            
        }, 1000); // check for the PAMM every 2 minutes
    });

}]);

ewallVirtualCoachNotification.controller('ewallVirtualCoachNotificationController', ['$scope', '$interval', '$timeout', 'REST_API', '$http', function ($scope, $interval, $timeout, REST_API, $http) {
    $scope.inDialogue = false; // this is true only if the user is in a dialogue with Robin
    $scope.dialogueData = null;
    $scope.statementContent = '';
    $scope.choices = [];
    $scope.userFirstName = ewallApp.currentSession.user.firstName;
    
    /*
    //hue interaction
    var hueBaseUrl = 'http://' + ewallApp.currentSession.hueIp +'/api/'+ ewallApp.currentSession.hueUser;
    $scope.lightState = {};
    //saving previous hue status
    $http({
    	  method: 'GET',
    	  url: hueBaseUrl + '/lights/' + 1
    }).then(function(response){
    	console.log('Saving the previous Hue status: %O', response.data);
    	//$scope.lightState[$scope.selected].color=id;
    	$scope.lightState.isOn = response.data.state.on;
    	$scope.lightState.color = response.data.state.hue;
    	$scope.lightState.bri = response.data.state.bri;
    });
    */
    // watch for changes in the $parent virtual coach object
    $scope.$watch('virtualCoach', function (virtualCoach) { // $watch if the virtualCoach has changed in the $parent scope
        console.log('virtualCoach changed');
        
        /*
        //send notification
        $http({
 				  method: 'PUT',
 				  url: hueBaseUrl + '/lights/' + 1 + '/state',
 				  data: {
 						"on":true,
 						"hue":46920,
 						"bri":254,
 						"alert":"lselect"
 				  		}
 			}).then(function(response){
 				console.log('Sending Hue notification: %O', response.data);
 				//$scope.lightState[$scope.selected].color=id;
 			}, function(){
 				console.log("Error in sending Hue notification")
 			});
        */
        if ($scope.$parent.virtualCoach.showBigRobin) {
            $scope.showNotification = $scope.$parent.virtualCoach.showBigRobin;
        } else {
            $scope.showNotification = false;
        }

        if ($scope.$parent.virtualCoach.inDialogue) {
            $scope.inDialogue = true;
        } else {
            $scope.inDialogue = false;
        }

        if ($scope.$parent.virtualCoach.bigRobinData) {
            if ($scope.inDialogue) {
                $scope.dialogueData = $scope.$parent.virtualCoach.bigRobinData;
            } else {
                $scope.choices = $scope.$parent.virtualCoach.bigRobinData;
            }

        } else {
            $scope.choices = [];
        }
    });

    $scope.hideBigRobin = function () {
    	console.log('hideBigRobin');
        $scope.$parent.virtualCoach.showBigRobin = false;
        $scope.showNotification = false;
        $scope.$parent.virtualCoach.bigRobinData = [];
        $scope.dialogueData = null;
        $scope.statementContent = '';
        $('#main-screen-body').removeClass('blur');
        /*
        //restore previous hue status
        if($scope.lightState.isOn==true){
	        $http({
				  method: 'PUT',
				  url: hueBaseUrl + '/lights/' + 1 + '/state',
				  data: {
						"on":$scope.lightState.isOn,
						"hue":$scope.lightState.color,
						"bri":$scope.lightState.bri,
						"alert":"none"
				  		}
			}).then(function(response){
				console.log('PUT Response: %O', response.data);
			});
        } else {
        	$http({
				  method: 'PUT',
				  url: hueBaseUrl + '/lights/' + 1 + '/state',
				  data: {
						"on":false
				  		}
			}).then(function(response){
				console.log('PUT Response: %O', response.data);
			});	
        }*/
    };
    

    $scope.sendChoice = function (url) { // send the PAMM choice to the [url]
        console.log('data from the choice: ' + url);
        $scope.inDialogue = true;
        $scope.waitingForServer = true;
        ewallApp.ajax({
            url: url,
            type: "GET",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "text/plain");
            },
            dataType: "json",
            async: true,
            success: function (data) {
                $scope.waitingForServer = false;

                console.log("choice data");
                console.log(data);
                if (data.type !== 'error') {
                    // build the statement text
                    $scope.dialogueData = data; // record the raw data in the $scope  
                    $scope.inDialogue = true;
                } else {
                    $scope.dialogueData = {
                        type: 'error',
                        subtype: '',
                        statements: [
                            {
                                type: 'text',
                                value: 'Uh oh! Something went wrong'
                                    }
                            ],
                        responses: [
                            {
                                type: 'basic',
                                label: 'OK',
                                action: 'error',
                                url: REST_API.virtual_coach + 'resetUser?username=' + ewallApp.currentSession.user.username
                                }
                            ]
                    };


                };
                
                $scope.$apply();
            }

        });
    };

    $scope.responseAction = function (action, url) {
        // <this is temporary> hide big Robin when clicking on any button
        //$scope.hideBigRobin();
        // first: interpret action        
        var actionSet = action.split(':'); // split the action string int an array with [0] action verb and [1] object to take action on
        if (actionSet.length > 1) {
            actionSet[1] = actionSet[1].toCamelCase();
        }

        // take action
        switch (actionSet[0]) {
        case 'open_app':
            console.log('opening the app:' + actionSet[1]);
            // take action
            if (actionSet[1] !== '') {
                //jQuery('#physiscalActivityWidget').click();
                $timeout(function () {
                    if (actionSet[1] == "cabinet-chessboard") {
                        angular.element('.chessboard-button').trigger('click');
                    } else {
                        angular.element('.' + actionSet[1]).trigger('click');
                    }
                }, 100);
            }
            // call url
            ewallApp.ajax({
                url: url,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "text/plain");
                },
                dataType: "json",
                async: true,
                success: function () {
                    // force a getPAMM
                    $scope.$parent.virtualCoach.pollWaitDuration = 0;        
                }
            });
            
            // hide this notification
            $scope.hideBigRobin();
            $scope.inDialogue = false;
            break;

        case 'finished':
            console.log('finished conversation');
            // take no action

            // hide the notification
            $scope.hideBigRobin();
            $scope.inDialogue = false;

            // call the url
            ewallApp.ajax({
                url: url,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "text/plain");
                },
                dataType: "json",
                async: true,
                success: function () {
                    // force a getPAMM
                    $scope.$parent.virtualCoach.pollWaitDuration = 0;
                }
            });

            break;
        case 'dialogue_continue':
            // show loading animation
            $scope.waitingForServer = true;
            // call the url
            ewallApp.ajax({
                url: url,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "text/plain");
                },
                dataType: "json",
                async: true,
                success: function (data) {

                    // form the new dialogue
                    if (data.type !== 'error') {
                        // build the statement text
                        $scope.dialogueData = data; // record the raw data in the $scope  
                        $scope.inDialogue = true;
                    } else {
                        $scope.dialogueData = {
                            type: 'error',
                            subtype: '',
                            statements: [
                                {
                                    type: 'text',
                                    value: 'Uh oh! Something went wrong'
                                    }
                            ],
                            responses: [
                                {
                                    type: 'basic',
                                    label: 'OK',
                                    action: 'error',
                                    url: REST_API.virtual_coach + 'resetUser?username=' + ewallApp.currentSession.user.username
                                }
                            ]
                        };

                    };
                    // hide loading animation
                    $scope.waitingForServer = false;
                    $scope.$apply();


                }
            });
            console.log('I am a dialogue_continue');
            break;

        case 'error':
            console.log('finished conversation');
            // take no action

            // call the url
            ewallApp.ajax({
                url: url,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "text/plain");
                },
                dataType: "json",
                async: true,
                success: function () {}
            });
            // hide the notification
            $scope.hideBigRobin();
            $scope.inDialogue = false;
            break;

        default:
            // hide the notification
            //$scope.hideBigRobin();
            $scope.inDialogue = false;
            console.log('ActionSet:');
            console.log(actionSet);
            break;
        }
    };


    angular.element(document).ready(function () {

        // do something whent he document loads
    });
            }]);