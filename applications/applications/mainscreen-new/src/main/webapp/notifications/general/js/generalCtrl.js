// make angular module
var ewallGeneralIDSSNotification = angular.module("ewallGeneralIDSSNotification", []);
var notification = {
    show: false,
    title: "NOTIFICATION",
    statements: [{
        "type": "question",
        "text": ""
    }],
    feedback: [
        {
            type: "button",
            label: "OK",
            url: ""
     }
          ]

};


//assign angular controller to module
ewallGeneralIDSSNotification.controller('ewallGeneralIDSSNotificationController', ['$scope', '$stateParams', '$interval', 'REST_API', function ($scope, $stateParams, $interval, REST_API) {
    //ewallGeneralIDSSNotification.controller('ewallGeneralIDSSNotificationController', function ($scope, $stateParams, $interval) {
    $scope.notification = notification;
    $scope.notificationContent = "";
    $scope.isInDialogue = false;
    $scope.userFirstName = ewallApp.currentSession.user.firstName;

    var getEwallGeneralNotification = function (notificationManagerURL) {

        try {

            ewallApp.ajax({
                url: notificationManagerURL,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "text/plain");
                },
                data: {
                    user: ewallApp.currentSession.user.username,
                    userID: ewallApp.currentSession.user.username,
                    device: 0
                },
                dataType: "text",
                async: true,
                success: function (data) {
                    if (data == 'NaN') {
                        // do not show notification
                        //$scope.showNotification = false;
                        //$('#main-screen-body').removeClass('blur');

                    } else {
                        //$scope.showNotification = false;
                        //$('#main-screen-body').removeClass('blur');
                        if (data) {
                            // show notification

                            $scope.showNotification = true;
                            console.log(data);

                            var notificationJSON = JSON.parse(data.split('content=')[1]);
                            console.log(notificationJSON);
                            $scope.stepId = notificationJSON.stepId;
                            if(notificationJSON.stepId != null) {
                                $scope.isInDialogue = true;
                                console.log("notificationJSON.stepId = " + notificationJSON.stepId);
                                $scope.notificationContent = "";
                                // add motivational notification message
                                if(notificationJSON.motivational) {
                                    $scope.notificationContent += notificationJSON.motivational + '  ';
                                }
                                
                                
                                //add suggestion to the notification message
                                if(notificationJSON.suggestion) {
                                    $scope.notificationContent += notificationJSON.suggestion + '  ';
                                }
                                
                                // if there are additional statements, concatenate them to the notification message
                                for (i = 0; i < notificationJSON.statements.length; i++) {
                                    $scope.notificationContent += notificationJSON.statements[i].text + " ";
                                }
                                
                                $scope.notificationFeedbackSet = notificationJSON.feedback;
                                
                                $scope.showNotificationSpinner = false;
                                $scope.$apply();
                                console.log('notification feedback');
                                console.log(notificationJSON.feedback)
                               //$('#main-screen-body').addClass('blur');
                                
                            } else {
                                if($scope.isInDialogue == false){
                                   console.log("notificationJSON.stepId = " + notificationJSON.stepId);
                                    $scope.notificationContent = "";
                                    // add motivational notification message
                                if(notificationJSON.motivational) {
                                    $scope.notificationContent += notificationJSON.motivational + '  ';
                                }
                                
                                
                                //add suggestion to the notification message
                                if(notificationJSON.suggestion) {
                                    $scope.notificationContent += notificationJSON.suggestion + '  ';
                                }
                                
                                // if there are additional statements, concatenate them to the notification message
                                    if(notificationJSON.statements ) {
                                        for (i = 0; i < notificationJSON.statements.length; i++) {
                                            $scope.notificationContent += notificationJSON.statements[i].text + " ";
                                        }
                                    } else {
                                        var motivational = "";
                                        var suggestion= "";
                                        if(notificationJSON.motivational) {
                                            motivational = notificationJSON.motivational;
                                        }
                                        if(notificationJSON.suggestion) {
                                            suggestion = notificationJSON.suggestion;
                                        }
                                        $scope.notificationContent = motivational + suggestion;
                                    }
                                    $scope.notificationFeedbackSet = notificationJSON.feedback;

                                    $scope.showNotificationSpinner = false;
                                    $scope.$apply();
                                    console.log('notification feedback');
                                    console.log(notificationJSON.feedback)
                                   // $('#main-screen-body').addClass('blur'); 
                                }
                                    
                            } 
                            
                            
                        }

                    }


                }
            }).always(function () {

            });

        } catch (notificationManagerERR) {
            console.log(notificationManagerERR);
        };



    };
    // Use this for localhost only
    // Use this for deployment version
     $scope.notificationManagerURL = REST_API.notifications;
    //window.setInterval(getEwallGeneralNotification($scope.notificationManagerURL), 10000);

    $scope.closeNotification = function () {
        $scope.showNotification = false;
        // $('#main-screen-body').removeClass('blur');
    };
    //window.setTimeout(getEwallGeneralNotification($scope.notificationManagerURL), 9000);
    $scope.sendEwallGeneralNotificationFeedback = function (feedbackURL) {
        console.log("Sending feedback to: " + feedbackURL);
        $scope.notification.show = false;

        //$scope.$apply();
    };

    $scope.sendFeedback = function (feedbackURL) {
        for(i=0; i< $scope.notificationFeedbackSet.length; i++)
         {
             if($scope.notificationFeedbackSet[i].type == 'range'){
                    feedbackURL += $scope.notificationFeedbackSet[i].url + $scope.notificationFeedbackSet[i].value;
                }
 
         }
                                                                                   
        if($scope.isInDialogue && $scope.stepId != null && $scope.stepId.indexOf("Final") >= 0){
                  console.log("is in final dialoge");                                                                 
                  $scope.isInDialogue = false;                                                                 
                  $scope.closeNotification(); 
        } else { 
             if($scope.isInDialogue) {
                  $scope.notificationContent = "Hmmm"
                  $scope.notificationFeedbackSet = [];
                  $scope.showNotificationSpinner = true;
            } else {
                $scope.closeNotification();                         
            }
                  
        }                                                                          
     
        try {
         ewallApp.ajax({           
                url: feedbackURL,
                type: "GET",
                /* for localhost testing only
                headers: {
                    'X-Auth-Token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiU1lTVEVNIiwiYWxsc2VydmljZXMiOnRydWUsImV4cCI6NDU4NDM0NDMzMSwic3ViIjoibHItc2xlZXAtbW9uaXRvciJ9.9D5w879LTQlKNsVCoVdfEZRVEKuZle0ympHx4dHhqYY'
                },
                */
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "text/plain");
                },
                async: true,
                success: function () {
                    console.log('fedback received :)')
                }
            }).always(function () {

            });
        } catch (notificationFeedbackERR) {
            console.log(notificationFeedbackERR);
        }

    };

    var stopGeneralNotifications = function (promise) {
    	console.log("Stopping generalNotificationLoop...");
        $interval.cancel(promise);
    }

    ewallApp.currentSession.stopGeneralNotifications = stopGeneralNotifications;

    angular.element(document).ready(function () {
       /* if (!('Notification' in window)) {
            alert('Web Notification is not supported');
            return;
        }*/

        ewallApp.currentSession.generalNotificationLoop = $interval(function () {
            /*remove this before deploy*/
            //console.log('notification manager url: ' + $scope.notificationManagerURL);
            
            getEwallGeneralNotification($scope.notificationManagerURL);
            //$scope.$apply();
        }, 10000);
    });
}]);