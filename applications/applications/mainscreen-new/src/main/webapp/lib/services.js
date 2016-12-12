'use strict';

/* Services */

var ewallAppServices = angular.module('ewallAppServices', ['ngResource', 'LocalStorageModule', 'angular-jwt']);

ewallAppServices.config(['localStorageServiceProvider', function(localStorageServiceProvider) {
  localStorageServiceProvider.setPrefix('eWALL-App');
}]);

ewallAppServices.config(['$resourceProvider', function($resourceProvider) {
  // Don't strip trailing slashes from calculated URLs
  $resourceProvider.defaults.stripTrailingSlashes = false;
}]);

ewallAppServices.constant('AUTH_EVENTS', {
  loginSuccess: 'auth-login-success',
  loginFailed: 'auth-login-failed',
  logoutSuccess: 'auth-logout-success',
  sessionTimeout: 'auth-session-timeout',
  notAuthenticated: 'auth-not-authenticated',
  notAuthorized: 'auth-not-authorized',
  userChanged: 'user-changed'
});

ewallAppServices
        .constant(
                'REST_API',
                {
      
                    login: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/authenticate/',
                    tokenUpdate: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/renew/',
                    users: '[protocol]//[hostname]/platform-[env]/profiling-server/users/',
                    regions: '[protocol]//[hostname]/platform-[env]/profiling-server/regions/',
                    services: '[protocol]//[hostname]/platform-[env]/profiling-server/services/',
                    applications: '[protocol]//[hostname]/platform-[env]/profiling-server/applications/',
                    sensingEnvs: '[protocol]//[hostname]/platform-[env]/cloud-gateway/sensingenvironments/',
					notifications: '[protocol]//[hostname]/platform-[env]/notification-manager/getNot',
                    virtual_coach: '[protocol]//[hostname]/platform-[env]/idss-reasoner-activitycoach/',
                    wellbeing_ads: '[protocol]//[hostname]/platform-[env]/idss-reasoner-wellbeing-ads/'

//	Uncomment the following lines and comment out the above lines, to test in local envs                    	
/*   	
                 login: 'http://localhost/users/authenticate/',
                 tokenUpdate: 'http://localhost/users/renew/',
                 users: 'http://localhost/profiling-server/users/',
                 regions: 'http://localhost/profiling-server/regions/',
                 services: 'http://localhost/profiling-server/services/',
                 applications: 'http://localhost/applications/',
                 sensingEnvs: 'http://localhost/cloud-gateway/sensingenvironments/',
                 notifications: 'http://localhost/notification-manager/getNot/',
                 virtual_coach: 'http://localhost/idss-reasoner-activitycoach/',
                 wellbeing_ads: 'http://localhost/idss-reasoner-wellbeing-ads/'	
*/
                });

ewallAppServices.factory('AuthService', ['$http', 'Session', 'REST_API', '$rootScope', function($http, Session, REST_API, $rootScope) {
  var authService = {};
  /* For localhost development, comment the REST_API string parsing
  Uncomment before deployment   */
 
  var hostname = window.location.hostname;
  var pathname = window.location.pathname;
  var protocol = window.location.protocol;
  console.log("hostname: " + hostname);
  console.log("pathname: " + pathname);
  console.log("protocol: " + protocol); 

  var env = "";
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
  console.log("Env: " + env);
 
  REST_API.login = REST_API.login.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
  REST_API.tokenUpdate = REST_API.tokenUpdate.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
  REST_API.users = REST_API.users.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
  REST_API.regions = REST_API.regions.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
  REST_API.services = REST_API.services.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
  REST_API.applications = REST_API.applications.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
  REST_API.sensingEnvs = REST_API.sensingEnvs.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
  REST_API.notifications = REST_API.notifications.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env); 
  REST_API.virtual_coach = REST_API.virtual_coach.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env); 
  REST_API.wellbeing_ads = REST_API.wellbeing_ads.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
  
  /**/
  
  $rootScope.updateTokenDelay = 30000;
    
  var lastLoginAttemptTimestamp=0;
  authService.login = function(credentials, rememberMe, relogin) {
	Session.isLoggingIn = true;
    var clientIssDate;
    var userObj;
    var tokenUpdateFrequencySecs = $rootScope.updateTokenDelay/1000;
    return $http.post(REST_API.login, "username=" + credentials.username + "&password=" + credentials.password + "&tokenUpdateFrequencySecs=" + tokenUpdateFrequencySecs, {
      headers: {
        "Content-type": "application/x-www-form-urlencoded"
      },
      timeout: 10000
    }).then(function(res) {
      clientIssDate = new Date();
      return $http.get(REST_API.users + credentials.username, {
        headers: {
          'x-auth-token': res.data.token
        },
        timeout: 10000
      }).then(function(user) {
    	  userObj = user;
    	  return $http.get(REST_API.users + credentials.username + "/applications", {
    	        headers: {
    	          'x-auth-token': res.data.token
    	        },
    	        timeout: 10000
    	      })  
      }).then(function(apps) {
       	userObj.data.applications = apps.data;
        Session.create(clientIssDate, credentials, res.data.token, userObj.data, rememberMe, relogin);
    	Session.isLoggingIn = false;
        return Session;
      });
    }, function() {
    	var res = false;
    	Session.isLoggingIn = false;
    	return res;
    });
    
  };

  authService.isAuthenticated = function() {
    return Session.isValid();
  };

  return authService;
}]);

ewallAppServices.service('Session', ['localStorageService', '$timeout', '$http', 'REST_API', 'jwtHelper', '$rootScope', '$interval', 'AUTH_EVENTS', '$state', 
     function(localStorageService, $timeout, $http, REST_API, jwtHelper, $rootScope, $interval, AUTH_EVENTS, $state) {
    var localStorageName = 'session';
    var localStorageCreds = 'creds';
    var localStorageReloginChoice = 'relogin';
    
      var checkHttpConnectionDelay = 45000;
      var updateTokenTimeout;
      var checkHttpTimeout;
      var session = {};
      
      $rootScope.backendUnavailable = false;
      $rootScope.networkUnavailable = false;
      $rootScope.dimmed = false;
      
      session.isBackendAvailable = function(){
    	  //console.log("Check on backend availability: " + $rootScope.backendUnavailable + " ," + $rootScope.networkUnavailable);
    	return  ($rootScope.backendUnavailable==false &&  $rootScope.networkUnavailable==false);
      };
      
      var decodeToken = function(token) {
        var decodedToken = jwtHelper.decodeToken(token);
        session.expDate = new Date(0);
        session.expDate.setUTCSeconds(decodedToken.exp);
        session.issDate = new Date(0);
        session.issDate.setUTCSeconds(parseInt(decodedToken.iss));
        return decodedToken;
      };

      var isTokenChanged = function(oldToken, newToken) {
        var omitKeys = ['exp', 'iss'];
        return !_.isEqual(_.omit(oldToken, omitKeys), _.omit(newToken, omitKeys));
      };

      var isExpired = function() {
    	  console.log("Expire date: " + session.expDate);
    	  console.log("Current date: " + new Date());
        return session.expDate.valueOf() <= (new Date().valueOf()-session.clientServerTimeDiff);
      };

      var stateAlreadyRegistered = _.find($state.get(), function (s) {
          return s.name == 'error';
      });
      
      if (!stateAlreadyRegistered) {
          ewallApp.stateProvider.state('error', {
              url: '/error',
              templateUrl: 'error.html',
              controller: 'ErrorPageCtrl',
              params: { type: null },
              data: {
                authenticationRequired: false
              }
          }).state('welcomeHue', {
              url: '/welcome-hue',
              templateUrl: 'welcomeHue.html',
              controller: 'welcomeHueCtrl',
              data: {
                authenticationRequired: true
              }
            });
      }

      
      var updateToken = function() {
    	  if($rootScope.networkUnavailable===true) {
    		  console.log("Skipping token update until network connection is back");
              refreshTokenAtFixedRate();
              return;
    	  }
    	  console.log("updating token")
        $http.get(REST_API.tokenUpdate, {
          headers: {
            'x-auth-token': session.token,
            'Cache-Control': 'no-cache'
          },
          params: { tokenUpdateFrequencySecs: $rootScope.updateTokenDelay/1000}, 
          timeout: 10000
        }).success(function(data, status, headers, config) {
          var newDecodedToken = decodeToken(data.token);
          session.clientServerTimeDiff = new Date().valueOf() - session.issDate.valueOf();
          var oldDecodedToken = jwtHelper.decodeToken(session.token);
          session.token = data.token;
          
          if (session.isValid()) {
        	  
              //Get the user profile and put it in session (needed for reward system)
              var userObj;
              $http.get(REST_API.users + session.user.username, {
                 headers: {
                   'x-auth-token': session.token
                 },
                 timeout: 10000
               }).then(function(user) {
	            	  	userObj = user;
		                userObj.data.applications = session.user.applications;
		                session.user = userObj.data;
		                
		                if (isTokenChanged(oldDecodedToken, newDecodedToken)) {
		                	console.log("****** TOKEN HAS CHANGED ******")
		                	$http.get(REST_API.users + session.user.username + "/applications", {
	            	        headers: {
	            	          'x-auth-token': session.token
	            	        },
	                        timeout: 10000
	            	      }).then(function(apps) {
	    	            	  console.log("****** BROADCASTING PROFILE CHANGE ******");
	    	                userObj.data.applications = apps.data;
	    	                session.user = userObj.data;
	    	                $rootScope.$broadcast(AUTH_EVENTS.userChanged);
	    	              });
		                }
               })
                 
        	$rootScope.backendUnavailable=false;
        	if($rootScope.dimmed==false && $state.is('error')){
           		$state.go('main');
            }
            //launchTokenBeforeExpiration();
            refreshTokenAtFixedRate();
            localStorageService.set(localStorageName, session);
          }
        }).error(function () {        	
        	console.log("Scheduling token renew after renewal error")
        	if(!session.isValid()){
        		 console.log("Session is not valid!");
        	}
        	refreshTokenAtFixedRate();
        	if($rootScope.networkUnavailable==false){
        		showErrorPage('backendUnavailable');
            	$rootScope.backendUnavailable=true;
        	}
        });
      };

      var checkHttpConnection = function() {
    	  console.log("checking http connection")
		  var method = 'JSONP';
		  var url = 'https://angularjs.org/greet.php?callback=JSON_CALLBACK&name=eWALL';
    	  
    	  var statusCode = null;
    	  var response = null;

          $http.jsonp(url).
	    	  then(function(response) {
	    		  statusCode = response.status;
	    		  response = response.data;
	    		  console.log("Connection is OK");
	    		  console.log("$rootScope.backendUnavailable: " + $rootScope.backendUnavailable)
	            	$rootScope.networkUnavailable=false;
	            	if($rootScope.dimmed==false && $state.is('error') && $rootScope.backendUnavailable === false){
	            		$state.go('main');
	            	}
	    	  	}, function(response) {
	    	  		response = response.data || "Request failed";
	    	  		statusCode = response.status;
	    	  		console.log("Http Check FAILED! Code: " + statusCode + " , response: " + response);
	            	$rootScope.networkUnavailable=true;
	    	  		showErrorPage('networkUnavailable');      	
	    	});
    	  checkHttpConnectionAtFixedRate();
      };
      
      var checkHueConnection = function(){
    	  console.log("Checking Hue connection...");
    	  session.hueUser = localStorageService.get('hueUser');
    	  if(session.hueUser!=null){ //hue is present and user is already set
    	  	console.log('Hue is ready to be used: ip is '+session.hueIp+'; user is ' + session.hueUser);
    	  } else{
    	    console.log('Hue is present, but user is not set');
    	    $state.go('welcomeHue');
    	  }
         //GvN408H7AmGiCVljPqWzbr9rpKO2LQkbvjykNxT6  
      };
      
      
      function showErrorPage(errorType){
    	  stopRecurringRequests();
    	  if($rootScope.dimmed==false){
	  		$state.go('error', {type: errorType})
          	.then(
          			function (state) {
          				console.log("SUCCESS in changing state %o", state)
          			}, 
          			function (state) {
          				console.log("ERROR in changing state! %o", state)
          			});       	
    	  }
      }
      
      
      $rootScope.$on('$stateChangeError',
    		  function(event, toState, toParams, fromState, fromParams, error){
    		      console.log("*********ERROR: %o", error);
    		  });
    		  
      var launchTokenBeforeExpiration = function() {
    	  console.log("in launchTokenBeforeExpiration()");
    	  console.log("session.clientServerTimeDiff: " + session.clientServerTimeDiff);
        var delay = session.expDate.valueOf() - new Date().valueOf() + session.clientServerTimeDiff;
  	    console.log("delay: " + delay);
        if (delay >= 0) {
          updateTokenTimeout = $timeout(updateToken, Math.max(0, delay - $rootScope.updateTokenDelay));
    	    console.log("updateTokenTimeout: " + Math.max(0, delay - $rootScope.updateTokenDelay));

        }
      };

      var refreshTokenAtFixedRate = function() {
    	console.log("in refreshTokenAtFixedRate()");
        updateTokenTimeout = $timeout(updateToken, $rootScope.updateTokenDelay);
    	console.log("updateTokenTimeout: " + $rootScope.updateTokenDelay);
      };
      
      var checkHttpConnectionAtFixedRate = function() {
    	console.log("in checkHttpConnectionAtFixedRate()");
    	checkHttpTimeout = $timeout(checkHttpConnection, checkHttpConnectionDelay);
  	  if(session.hueIp!=''){
  		  checkHueConnection();
  	  }
    	console.log("checkHttpTimeout: " + checkHttpConnectionDelay);
      };
       
      session.isValid = function() {
        var isValid = session.token && !isExpired();
/*        if(isExpired()) {
        	stopRecurringRequests();
        }
*/        if (!isValid) {
          if (session.token) {
        	  
            $rootScope.$broadcast(AUTH_EVENTS.sessionTimeout);
          }
          if(session.relogin === false && isExpired()) {
          	session.destroy();
          }
        }
        return isValid;
      };

      session.create = function(clientIssDate, credentials, token, user, remember, relogin) {
    	  
        session.destroy();
        session.token = token;
        session.user = user;
        session.relogin = relogin;

        decodeToken(token);
        session.clientServerTimeDiff = clientIssDate.valueOf() - session.issDate.valueOf();
        if (session.isValid()) {
          //launchTokenBeforeExpiration();
        	refreshTokenAtFixedRate();
        	checkHttpConnectionAtFixedRate();
          if (remember === true) {
              localStorageService.set(localStorageName, session);
          }
          if(relogin === true) {
              localStorageService.set(localStorageCreds, credentials);       	  
          }
          localStorageService.set(localStorageReloginChoice, relogin);
        }
      };
      
      session.getLocalStorageCreds = function() {
    	  return localStorageService.get(localStorageCreds);
      }

      session.getLocalStorageReloginChoice = function() {
    	  return (localStorageService.get(localStorageReloginChoice)==="true");
      }
      
	  session.hueIp = '';
      
	  $http({
		  method: 'GET',
		  url: 'https://www.meethue.com/api/nupnp',
	  }).then(function (result) {
		  if(result.data.length>0){
			  session.hueIp = result.data[0].internalipaddress;
		  	  console.log('Hue is present: ip is '+session.hueIp);
		  }
		  else //hue is not present 
			  console.log('Hue not present or not reachable');
	  });
	  
	  session.setLocalStorageHueUser = function(user) {
    	  session.hueUser = user;
    	  localStorageService.set('hueUser', user);
    	  console.log('From localStorage Hue user is: '+localStorageService.get('hueUser'));
      }

      session.destroy = function() {
        session.token = null;
        session.expDate = null;
        session.user = null;
        session.issDate = null;
        session.clientServerTimeDiff = null;
        $timeout.cancel(updateTokenTimeout);
        $timeout.cancel(checkHttpTimeout);

        stopRecurringRequests();
        
        localStorageService.remove(localStorageName);
        localStorageService.remove(localStorageCreds);
      };

      var savedSession = localStorageService.get(localStorageName);
      if (savedSession) {
        session.token = savedSession.token;
        session.user = savedSession.user;
        decodeToken(session.token);
        session.clientServerTimeDiff = savedSession.clientServerTimeDiff;
        if (session.isValid()) {
          //launchTokenBeforeExpiration();
            refreshTokenAtFixedRate();
            checkHttpConnectionAtFixedRate();
        }
      }

      var stopRecurringRequests = function() {
          if(session.generalNotificationLoop != null) {
          	session.stopGeneralNotifications(session.generalNotificationLoop);
          }
          if(session.virtualCoachPAMMNotificationLoop != null) {
          	session.stopVirtualCoachPAMMNotifications(session.virtualCoachPAMMNotificationLoop);
          }
          if(session.recommendationNotificationLoop != null) {
          	session.stopRecommendationNotifications(session.recommendationNotificationLoop);
          }
          
          if(session.recurringWeatherRequest != null) {
          	session.stopRecurringWeatherRequest(session.recurringWeatherRequest);
          }
          
          if(session.recurringDPAMRequest != null) {
          	session.stopRecurringDPAMRequest(session.recurringDPAMRequest);
          }
          
          if(session.recurringDomoticsRequest != null) {
          	session.stopRecurringDomoticsRequest(session.recurringDomoticsRequest);
          }
          
          if(session.repeatUserProfileLookup != null) {
          	session.stopRepeatUserProfileLookup(session.repeatUserProfileLookup);
          }
          
          if(session.AgendaWidgetInterval != null) {
          	session.stopAgendaWidgetInterval(session.AgendaWidgetInterval);
          }
          
          if(session.recurringPhotoFrameRequest != null) {
            	session.stopRecurringPhotoFrameRequest(session.recurringPhotoFrameRequest);
            }   	  

          if(session.AdvFadeInterval != null) {
            	session.stopAdvFadeInterval(session.AdvFadeInterval);
            }   	  
      }
      
      return session;
    }]);

ewallAppServices.service('TokenHeaderGenerator', ['Session', function(Session) {
  this.generateActions = function(actions) {
    var baseActions = {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'POST'
      },
      'query': {
        method: 'GET',
        isArray: true
      },
      'remove': {
        method: 'DELETE'
      },
      'delete': {
        method: 'DELETE'
      }
    };
    for ( var key in baseActions) {
      baseActions[key]['headers'] = {
        'x-auth-token': Session.token
      };
    }
    for ( var key in actions) {
      baseActions[key] = actions[key];
      if (baseActions[key]['headers']) {
        baseActions[key]['headers']['x-auth-token'] = Session.token;
      } else {
        baseActions[key]['headers'] = {
          'x-auth-token': Session.token
        };
      }
    }
    return baseActions;
  };
}]);

ewallAppServices.service('TranslationService', [function(){
	var translationService = {};
    var gameLabels = [ 
    {
		lang : 'en',
		labels : {
			ohno : 'On nO',
			memoryTest : 'Memory Test',
			memoryCard : 'Memory Card',
			memoryQuiz : 'Memory Quiz',
			sampleApp : 'Sample App',
			sudoku: 'Sudoku',
			snapPuzzle: 'Puzzle'
		}
	}, {
		lang : 'nl',
		labels : {
			ohno : 'On nO',
			memoryTest : 'Memory Test',
			memoryCard : 'Memory Card',
			memoryQuiz : 'Memory Quiz',
			sampleApp : 'Sample App',
			sudoku: 'Sudoku',
			snapPuzzle: 'Puzzle'
		}
	}, {
		lang : 'da',
		labels : {
			ohno : 'On nO',
			memoryTest : 'Talspillet',
			memoryCard : 'Vendespil',
			memoryQuiz : 'Vidensspillet',
			sampleApp : 'Sample App',
			sudoku: 'Sudoku',
			snapPuzzle: 'Puslespillet'
		}
	}, {
		lang : 'de',
		labels : {
			ohno : 'On nO',
			memoryTest : 'Zahlenreihe',
			memoryCard : 'Memory',
			memoryQuiz : 'Gedächtnis-Quiz',
			sampleApp : 'Sample App',
			sudoku: 'Sudoku',
			snapPuzzle: 'Puzzle'
		}
	}, {
		lang : 'it',
		labels : {
			ohno : 'On nO',
			memoryTest : 'Prova di memoria',
			memoryCard : 'Memory (Carte)',
			memoryQuiz : 'Quiz ad immagini',
			sampleApp : 'Sample App',
			sudoku: 'Sudoku',
			snapPuzzle: 'Puzzle'
		}
	} ];

    var bookLabels = [
      {
       lang: 'en',
       labels: {
           dailyPhysicalActivityMonitoring: 'MY ACTIVITY',
           dailyFunctioningMonitoring: 'MY DAILY LIFE',
           sleep: 'MY SLEEP',
           healthcareMonitor: 'MY HEALTH',
           help: 'HELP'
           }    
       },
       {
           lang: 'nl',
           labels: {
        	   dailyPhysicalActivityMonitoring: 'Activiteit',
        	   dailyFunctioningMonitoring: 'Mijn dag',
        	   sleep: 'Mijn slaap',
        	   healthcareMonitor: 'Gezondheid',
               help: 'Help'
           }    
       },
       {
           lang: 'da',
           labels: {
        	   dailyPhysicalActivityMonitoring: 'Mine aktiviteter',
        	   dailyFunctioningMonitoring: 'Min hverdag',
        	   sleep: 'Min søvn',
        	   healthcareMonitor: 'Min sundhed',
               help: 'Hjælp'
           }    
       },
       {
           lang: 'de',
           labels: {
        	   dailyPhysicalActivityMonitoring: 'Bewegung',
        	   dailyFunctioningMonitoring: 'Tagesverlauf',
        	   sleep: 'Schlaf',
        	   healthcareMonitor: 'Gesundheit',
               help: 'Hilfe'
           }    
       },
       {
           lang: 'it',
           labels: {
        	   dailyPhysicalActivityMonitoring: 'L’Attività',
        	   dailyFunctioningMonitoring: 'La Giornata',
        	   sleep: 'Il mio Sonno',
        	   healthcareMonitor: 'La Salute',
               help: 'Aiuto'
           }    
       }            
   ];

    var errorMessages = [ 
      {
  		lang : 'en',
  		errors : [{
		  			type : 'backendUnavailable',
		  			message : 'eWALL is temporarily unavailable.  The system will be operational again as soon as possible.'
		  		 },
		  		 {
		  			type : 'networkUnavailable',
		  			message : 'It seems the network connection in your home is down.  The system will be operational again as soon as the network is restored.'
		  		 }]
	  	},
	  	{
  		lang : 'nl',
  		errors : [{
  			type : 'backendUnavailable',
  			message : 'eWALL is tijdelijk niet beschikbaar. Het systeem zal zo snel mogelijk weer operationeel zijn.'
  		 },
  		 {
  			type : 'networkUnavailable',
  			message : 'Er lijkt een probleem te zijn met de internet verbinding. Het systeem zal weer beschikbaar zijn zodra de verbinding is hersteld.'
  		 }]
	  	}, 
	  	{
  		lang : 'da',
  		errors : [{
  			type : 'backendUnavailable',
  			message : 'eWALL er midlertidigt ude af drift. Systemet vil være oppe at køre igen snarest muligt'
  		 },
  		 {
  			type : 'networkUnavailable',
  			message : 'Det ser ud til, at netværksforbindelsen i dit hjem er nede. Systemet vil køre igen, så snart netværksforbindelsen er genetableret.'
  		 }]
	  	}, 
	  	{
  		lang : 'de',
  		errors : [{
  			type : 'backendUnavailable',
  			message : 'eWALL ist momentan nicht verfügbar. Das System wird schnellstmöglich wieder funktionsfähig sein.'
  		 },
  		 {
  			type : 'networkUnavailable',
  			message : 'Ihre Internetverbindung scheint getrennt worden zu sein. Das System wird wieder funktionsfähig sein sobald die Verbindung wiederhergestellt ist.'
  		 }]
	  	}, 
	  	{
  		lang : 'it',
  		errors : [{
  			type : 'backendUnavailable',
  			message : 'eWALL é temporaneamente non disponibile. Il sistema tornerá ad essere operativo al piú presto.'
  		 },
  		 {
  			type : 'networkUnavailable',
  			message : 'Pare che la tua connessione di rete domestica non funzioni. Il sistema tornerá ad essere operativo non appena la connessione sará disponibile.'
  		 }]
	  	}
	  ];


    var getGameAppNames = function(language) {
		for (var i = 0; i < gameLabels.length; i++) {
			if (language == gameLabels[i].lang) {
				return gameLabels[i].labels;
			}
		}
		return null;
	}
	var getHealthAppNames = function(language) {
		for (var i = 0; i < bookLabels.length; i++) {
			if (language == bookLabels[i].lang) {
				return bookLabels[i].labels;
			}
		}
		return null;
	}
 
    translationService.getError = function(language, type){
		for (var i = 0; i < errorMessages.length; i++) {
			if (language == errorMessages[i].lang) {
				var errors =  errorMessages[i].errors;
				for(var j = 0; j < errors.length; j++) {
					if(errors[j].type == type) {
						return errors[j].message;
					}
				}
			}
		}
		return null;
    }
    
	translationService.getAppNames = function(category, language) {
		if(category == "HEALTH") {
			return getHealthAppNames(language);
		} else 
			if(category == "GAMES") {
				return getGameAppNames(language);
			} else {
				return null;
			}
	}
	
	return translationService;
	
}]);

