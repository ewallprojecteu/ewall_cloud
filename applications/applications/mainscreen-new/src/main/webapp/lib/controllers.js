'use strict';

/* Controllers */

var ewallAppControllers = angular.module('ewallAppControllers', ['ewallAppServices']);


ewallAppControllers.factory('ajaxCall', function() {
    return function(url, method, data, successCallback ) {
        try {
            ewallApp.ajax({
                url: url,
                method: method,
                data: JSON.stringify(data),
                contentType: 'application/json; charset=utf-8',
                dataType   : 'json',
                success: successCallback
            })
        } catch (service_call_err) {
            console.log('ERROR: ', service_call_err);
        }
    };
});

ewallAppControllers.controller('LoginFormCtrl', ['$scope', '$rootScope', '$state', 'AuthService', 'AUTH_EVENTS', 
    'REST_API', 'Session',
    function ($scope, $rootScope, $state, AuthService, AUTH_EVENTS, REST_API, Session) {
	
		$scope.automaticRelogin = Session.getLocalStorageReloginChoice();
        if (AuthService.isAuthenticated() && $rootScope.dimmed==false) {
            $state.go('main');
        }

        ewallApp.login = function (credentials) {
            AuthService.login(credentials, true, $scope.automaticRelogin).then(function () {
                $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                if ($rootScope.dimmed==false) {
                	$state.go('main');
                }
            }, function () {
                $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
            });
        };
        
        $scope.reloadRoute = function() {
        	console.log("@#@#@#@#: RELOADING STATE!");
            $state.reload();
        };

        $rootScope.$on(AUTH_EVENTS.sessionTimeout, function (event, next) {
        	console.log("Got session timeout!");
    		if (ewallApp.currentSession.relogin === true && ewallApp.currentSession.token && ewallApp.currentSession.isLoggingIn === false) {
    	    	console.log("Token has expired, trying to re-login automatically...");
    			var credentials = Session.getLocalStorageCreds();
    			var relogin = ewallApp.currentSession.relogin;
    			//Session.destroy();
    			AuthService.login(credentials, true, relogin).then(function (res) {
    				if(res !== false) { 
	    				$rootScope.backendUnavailable=false;
	    				$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
	    		        if ($rootScope.dimmed==false) {
	    		        	$state.go('main');
	    		        	$scope.reloadRoute();
	    		        }
	    			}
    	        }, function () {
    	            //$rootScope.$broadcast(AUTH_EVENTS.loginFailed);
    	        	console.log("...re-login FAILED")
    	        });
    		  }
    		else {
    			if(ewallApp.currentSession.isLoggingIn === true) {
    				console.log("Skipping re-login (already ongoing)...");
    			} else {
    				console.log("relogin: " + relogin + " and token expired. Logging out...");
        			$scope.doLogout();
    			}
    		}
        });

        
    }]);



ewallAppControllers.controller('ErrorPageCtrl', ['$scope', '$rootScope', '$state', 'TranslationService', '$stateParams',
	function ($scope, $rootScope, $state, TranslationService, $stateParams) {
		$scope.errorMessage='';
		console.log("error type:", $stateParams.type);
	    var language = ewallApp.preferedLanguage;
	    if(language == null) {
	    	language = ewallApp.currentSession.user.userProfile.userPreferencesSubProfile.systemPreferences.preferredLanguage;
	    	if(language == null){
	    		console.log("No language set for user neither from login mask, nor in the preferences profile. Defaulting to English")
	    		language = 'en';
	    	}
	    }
	    var errorMessage = TranslationService.getError(language, $stateParams.type);
		$scope.errorMessage = errorMessage;

}]);

ewallAppControllers.controller('welcomeHueCtrl', ['$scope', 'AuthService', 'Session', 'welcomeHueTranslation', 'AUTH_EVENTS', '$state', '$http', '$templateCache',
    function ($scope, AuthService, Session, welcomeHueTranslation, AUTH_EVENTS, $state, $http, $templateCache) {                                         		
		
	console.log("Welcome Hue! Your ip is "+Session.hueIp);
		
	    var language = ewallApp.preferedLanguage;
	    if(language == null) {
	    	language = ewallApp.currentSession.user.userProfile.userPreferencesSubProfile.systemPreferences.preferredLanguage;
	    	if(language == null){
	    		console.log("No language set for user neither from login mask, nor in the preferences profile. Defaulting to English")
	    		language = 'en';
	    	}
	    }
	
		$scope.labels = welcomeHueTranslation.getLabels(language);
		$scope.showPage=0;
		$scope.goToMain = function(){
			$state.go('main');
		}
		
		
		$scope.sendPost = function(n){
			if(n==1){
				$http({
					  method: 'POST',
					  url: 'http://'+ Session.hueIp + '/api',
					  data: {'devicetype':'ewallApp'}
				})
			} else if(n==2){
				console.log("Second post");
				 $http({
					  method: 'POST',
					  url: 'http://'+ Session.hueIp + '/api',
					  data: {'devicetype':'ewallApp'}
				}).then(function (response){
					var tempUser = response.data[0].success.username
					console.log("New Hue user is "+ tempUser);
					Session.setLocalStorageHueUser(tempUser);
				})
			}
		}

}]);


ewallAppControllers.controller('ApplicationController', ['$scope', 'AuthService', 'Session', 'AUTH_EVENTS', '$state', '$http', '$templateCache',
    function ($scope, AuthService, Session, AUTH_EVENTS, $state, $http, $templateCache) {
        ewallApp.currentSession = Session;

		$http.get('error.html', {cache:$templateCache});
		$http.get('assets/1-login-bck.png', {cache:$templateCache});
		
		console.log("templateCache: %o", $templateCache);

        
        ewallApp.get = function (path, success) {
        	if(!Session.isBackendAvailable()) {
        		console.log("Backend is unavailable. Skipping http GET call:" + path);
        		return;
        	}
            $http.get(path, {
                headers: {
                    'x-auth-token': Session.token
                }
            }).success(function (data, status, headers, config) {
                success(data);
            });
        };
        ewallApp.post = function (path, data, success) {
        	if(!Session.isBackendAvailable()) {
        		console.log("Backend is unavailable. Skipping http POST call:" + path);
        		return;
        	}
           $http.post(path, data, {
                headers: {
                    'x-auth-token': Session.token
                }
            }).success(function (data, status, headers, config) {
                success(data);
            });
        };
        ewallApp.ajax = function (settings) {
        	if(!Session.isBackendAvailable()) {
        		console.log("Backend is unavailable. Skipping ajax call:" + settings);
        		return;
        	}
            if (!settings.headers) {
                settings.headers = {};
            }
            settings.headers['x-auth-token'] = Session.token;
            return $.ajax(settings);
        };

        $scope.$on(AUTH_EVENTS.sessionTimeout, function (event, next) {
        	if(ewallApp.currentSession.relogin === false){
        		//alert(AUTH_EVENTS.sessionTimeout);
        	}
        });

        $scope.$on(AUTH_EVENTS.loginFailed, function (event, next) {
        	if(ewallApp.currentSession.relogin === false){
        		alert(AUTH_EVENTS.loginFailed);
        	}
        });

        $scope.doLogout = function () {
        	userInteractionLogger.sendInteractionWithComment("mainScreen", "logoutButton", "User taps Logout button");
            console.log("Destroying session");
            ewallApp.currentSession.destroy();
            $state.go('login');
        }
    }]);

ewallAppControllers.controller('MainscreenCtrl', [
    '$scope',
    '$rootScope',
    '$state',
    '$filter',
    'AUTH_EVENTS',
    'TranslationService',
    'ajaxCall',
    '$interval',
    '$timeout',
    function ($scope, $rootScope, $state, $filter, AUTH_EVENTS, TranslationService, ajaxCall, $interval, $timeout) {
        //console.log('Panda dice: questo è l\'oggetto ewallApp.currentSession %O', ewallApp.currentSession);
        $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            if (toState.name == 'main') {
                $('#AppWindow').hide();
                console.log("back to main view");
                //hideWindows();

            } else if (toState.name != 'login') {
                /*if (authorizedAppState(toState.name)) {
                    
                    if (toState.name == 'main.sleep' || toState.name == 'main.dailyPhysicalActivityMonitoring' || toState.name == 'main.healthcareMonitor' || toState.name == 'main.dailyFunctioningMonitoring' || toState.name == 'main.memoryQuiz' || toState.name == 'main.physicalTrainer') {
                        $scope.appTitle = toState.data.title;
                        //hideWindows();
                        $('#AppWindow').show();
                        //$('#window-book-filler').show();
                        //$('#layer-8-book2').show();
                        $('#app-window-close-button').show();            
                        
                        console.log("In APP: sleep state");
                    };
                    if (toState.name == 'main.0hn0LogicGame' || toState.name == 'main.memoryCard' || toState.name == 'main.memoryTest' || toState.name == 'main.testApp') {
                        $scope.appTitle = toState.data.title;
                        //hideWindows();
                        $('#AppWindow').show();
                        //$('#window-book-filler').show();
                        //$('#layer-8-book2').show();
                        $('#app-window-close-button').show();            
                        
                        console.log("In APP: sleep state");
                    };
                    */
                if (authorizedAppState(toState.name)) {
                    $scope.appTitle = toState.data.title;
                    $('#AppWindow').show();
                    $('#app-window-close-button').show();
                };
            } else {
                $state.go("main", {}, {notify:false});
            }


        });
       

        $scope.reloadRoute = function() {
        	console.log("@#@#@#@#: RELOADING STATE!");
            $state.reload();
        };
        
        $scope.switchDimmer = function() {
        	$rootScope.dimmed=!$rootScope.dimmed;
        }
        /**
         * In case we want to delete the session upon browser tab close or refresh
         */

        window.onbeforeunload = function (event) {
            if (typeof event == 'undefined') {
                event = window.event;
            }
            if (event) {
                console.log("Closing mainscreen");
                ewallApp.close();
            }
        }
        
        $scope.arrayChessIcons = ['assets/white-queen.png', 'assets/black-king.png', 'assets/white-king.png', 'assets/black-bishop.png', 'assets/black-horse.png', 'assets/white-bishop.png', 'assets/black-tower.png', 'assets/white-tower.png'];

        var myRandom = Math.random();
        var stopAdvFadeInterval = function (promise) {
         	console.log("Stopping AdvFadeInterval loop... #####" + myRandom);
            $interval.cancel(promise);
        }

        ewallApp.currentSession.stopAdvFadeInterval = stopAdvFadeInterval;
        
        ewallApp.currentSession.AdvFadeInterval = $interval(function() {
        	console.log("Fading... #####" + myRandom);
            $('#advertisment').fadeToggle("slow", "linear")
        }, 30000);
        
        
        /*
         * Before deploy, uncomment this function and comment the one below it
         */
         
        function initAppsForCategory(category) {
            var categoryApps = $filter('filter')(ewallApp.currentSession.user.applications, {
                type: category
            });
            var language = ewallApp.preferedLanguage;
            if(language == null) {
            	language = ewallApp.currentSession.user.userProfile.userPreferencesSubProfile.systemPreferences.preferredLanguage;
            	if(language == null){
            		console.log("No language set for user neither from login mask, nor in the preferences profile. Defaulting to English")
            		language = 'en';
            	}
            }
            var myLabels = TranslationService.getAppNames(category, language);

            for (var i = 0; (i < categoryApps.length); i++) {
                categoryApps[i].offset = i * 110;
                categoryApps[i].camelCaseName = categoryApps[i].name.toCamelCase();
                if(myLabels !== null) {
                	categoryApps[i].localizedName = myLabels[categoryApps[i].camelCaseName];
                }
                var stateAlreadyRegistered = _.find($state.get(), function (s) {
                    return s.name == 'main.' + categoryApps[i].camelCaseName;
                });
                if (!stateAlreadyRegistered) {
                    ewallApp.stateProvider.state('main.' + categoryApps[i].camelCaseName, {
                        url: '/' + categoryApps[i].camelCaseName,
                        templateUrl: categoryApps[i].links[0].href,
                        data: {
                            authenticationRequired: true,
                            title: categoryApps[i].name
                        }
                    });
                }
            }
            console.log(categoryApps);
            return categoryApps;
        }
         /**/
        /*
         * For localhost development, use this function instead of the above
         */
         /*
         function initAppsForCategory(category) {
             var categoryApps = $filter('filter')(ewallApp.currentSession.user.applications, {
                 type: category
             });
             for (var i = 0; i < categoryApps.length; ++i) {
                 categoryApps[i].offset = i * 110;
                 categoryApps[i].camelCaseName = categoryApps[i].name.toCamelCase();
                 var stateAlreadyRegistered = _.find($state.get(), function (s) {
                     return s.name == 'main.' + categoryApps[i].camelCaseName;
                 });
                 if (!stateAlreadyRegistered) {
                     ewallApp.stateProvider.state('main.' + categoryApps[i].camelCaseName, {
                         url: '/' + categoryApps[i].camelCaseName,
                         templateUrl: '../../../../' + categoryApps[i].camelCaseName + '/src/main/webapp/index.html',
                         data: {
                             authenticationRequired: true,
                             title: categoryApps[i].name
                         }
                     });
                 }
             }
             console.log(categoryApps);
             return categoryApps;
         }

         */

        function initMinimizedApps() {
            var categoryApps = $filter('filter')(ewallApp.currentSession.user.applications, {
                type: 'MINIMIZED'
            });
            return categoryApps;
        }

        function updateMenu() {
            $scope.healthApps = initAppsForCategory('HEALTH');
            //console.log("--- user's health apps ---");
            // console.log($scope.healthApps);
            // console.log("--- --- ---");
            $scope.contactsApps = initAppsForCategory('CONTACTS');
            // console.log("--- user's contacts apps ---");
            // console.log($scope.contactsApps);
            // console.log("--- --- ---");
            $scope.houseApps = initAppsForCategory('HOUSE');
            // console.log("--- user's house apps ---");
            // console.log($scope.houseApps);
            // console.log("--- --- ---");
            $scope.gamesApps = initAppsForCategory('GAMES');
            // console.log("--- user's games apps ---");
            // console.log($scope.gamesApps);
            // console.log("--- --- ---");

            $scope.MinimizedApps = initMinimizedApps();

            $scope.$applyAsync();
        }

        updateMenu();

        function authorizedAppState(appStateName) {
            return _.contains(_.map(ewallApp.currentSession.user.applications, function (app) {
                return 'main.' + app.camelCaseName;
            }), appStateName);
        }


        $scope.$on(AUTH_EVENTS.userChanged, function (event, next) {
            updateMenu();
            if (!authorizedAppState($state.current.name) && $rootScope.dimmed==false) {
                $state.go('main');
            }
        });

        ewallApp.loadApp = function (app, event) {
        	event.preventDefault();
            console.log("Launch: " + app.name + ', localized name: ' + app.localizedName);
            $scope.currentApp = app;
            $state.go('main.' + app.camelCaseName);
        };
        
        $scope.loadApp = ewallApp.loadApp;
        
        ewallApp.closeApp = function () {
            $state.go('main');
        };
        $scope.closeApp = ewallApp.closeApp;

        ewallApp.logInteraction = function (placeOfInteraction, sourceOfInteraction, comment) {
            console.log("**********logInteraction: " + placeOfInteraction + " " + sourceOfInteraction  + " " + comment);
            userInteractionLogger.sendInteractionWithComment(placeOfInteraction,sourceOfInteraction,comment);

        };
        
        $scope.logInteraction = ewallApp.logInteraction;

        // Record usage for ewallet
        ewallApp.logEWallet = function (category, coins) {
            console.log("**********logEWallet: " + ewallApp.currentSession.user.username + "gets " + coins + " coins in " + category);
            var hostname = window.location.hostname;
            var pathname = window.location.pathname;
            var protocol = window.location.protocol;
            var platformUrl = "[protocol]//[hostname]/platform-[env]/";
            var env = "local";
            if(pathname.match(/-dev/g)) {
                env = "dev"
            } else if(pathname.match(/-int-level-1/g)) {
                env = "int-level-1"
            } else if(pathname.match(/-int-level-2/g)) {
                env = "int-level-2"
            } else if(pathname.match(/-prod-aws/g)) {
                env = "prod-aws"
            } else if(pathname.match(/-prod/g)) {
                env = "prod"
            }
            if (env != 'local') {
                platformUrl = platformUrl.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
            } else {
                platformUrl = "http://localhost/";
            }
            ajaxCall(platformUrl + 'ewallet/earnCoins?username=' + ewallApp.currentSession.user.username + '&coins=' + coins + '&from=' + category, 'POST', {}, function(data) {
            });
        };
        $scope.logEWallet = ewallApp.logEWallet;

        /**
         * In case we want only to delete the session
         */
        ewallApp.close = function () {
            console.log("Destroying session and closing");
            ewallApp.currentSession.destroy();
        }
        $scope.close = ewallApp.close;

        ewallApp.logout = function () {
            console.log("Destroying session");
            ewallApp.currentSession.destroy();
            $state.go('login');
        }
                
}]);