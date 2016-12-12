'use strict';

var ewallPortalAuthentication = angular.module('ewallPortalAuthentication', ['LocalStorageModule', 'angular-jwt',
    'ewallPortalUsers', 'ewallPortalConstants']);

ewallPortalAuthentication.constant('AUTH_EVENTS', {
  loginSuccess: 'auth-login-success',
  loginFailed: 'auth-login-failed',
  logoutSuccess: 'auth-logout-success',
  sessionTimeout: 'auth-session-timeout',
  notAuthenticated: 'auth-not-authenticated',
  notAuthorized: 'auth-not-authorized',
  userChanged: 'user-changed'
});

ewallPortalAuthentication.factory('AuthService', [
    '$http',
    'Session',
    'USER_ROLES',
    'REST_API',
    function($http, Session, USER_ROLES, REST_API) {
      var authService = {};

    authService.login = function(credentials, rememberMe) {
	    var clientIssDate;
	    var userObj;
	    return $http.post(REST_API.login, "username=" + credentials.username + "&password=" + credentials.password, {
	      headers: {
	        "Content-type": "application/x-www-form-urlencoded"
	      }
	    }).then(function(res) {
	      clientIssDate = new Date();
	      return $http.get(REST_API.users + credentials.username, {
	        headers: {
	          'x-auth-token': res.data.token
	        }
	      }).then(function(user) {
	    	  userObj = user;
	    	  return $http.get(REST_API.users + credentials.username + "/applications", {
	    	        headers: {
	    	          'x-auth-token': res.data.token
	    	        }
	    	      })  
	      }).then(function(apps) {
	       	userObj.data.applications = apps.data;
	        Session.create(clientIssDate, credentials.username, res.data.token, userObj.data, rememberMe);
	        return Session;
	      });
	    })};
    	    
    	authService.isAuthenticated = function() {
        return Session.isValid();
      };

      authService.isAuthorized = function(authorizedRoles) {
        if (!angular.isArray(authorizedRoles)) {
          authorizedRoles = [authorizedRoles];
        }
        return authorizedRoles.indexOf(USER_ROLES.all) !== -1
                || (authService.isAuthenticated() && authorizedRoles.indexOf(Session.user.userRole) !== -1);
      };

      return authService;
    }]);

ewallPortalAuthentication.service('TokenHeaderGenerator', ['Session', function(Session) {
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
      },
      'update': { 
        method:'PUT' 
      }
    };
    for ( var key in actions) {
      baseActions[key] = actions[key];
    }
    return baseActions;
  };
}]);

ewallPortalAuthentication.factory('httpRequestInterceptor', [ function () {
  var interceptor = {};	
  var _token ='';
  interceptor.updateToken = function(token){
	  _token = token;
  }
  interceptor.request = function (config) {
    	if (!(_token=='')) {
    		config.headers['x-auth-token'] = _token;
 		  }     	     
      return config;
  };
  return interceptor;
}]);


ewallPortalAuthentication.service('Session', ['localStorageService', '$timeout', '$http', 'REST_API', 'jwtHelper',
    '$rootScope', 'AUTH_EVENTS','httpRequestInterceptor',
    function(localStorageService, $timeout, $http,  REST_API, jwtHelper, $rootScope, AUTH_EVENTS,httpRequestInterceptor) {
      var localStorageName = 'session';
      var updateTokenDelay = 60000;
      var updateUserStatusDelay = 30000;
      var updateUserStatusTimeout;
      var timeout;
      var session = {};
      
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
        return session.expDate.valueOf() <= new Date().valueOf();
      };

      var updateToken = function() {
        $http.get(REST_API.tokenUpdate, {
          headers: {
            'x-auth-token': session.token,
            'Cache-Control': 'no-cache'
          }
        }).success(function(data, status, headers, config) {
          var newDecodedToken = decodeToken(data.token);
          session.clientServerTimeDiff = new Date().valueOf() - session.issDate.valueOf();
          var oldDecodedToken = jwtHelper.decodeToken(session.token);
          session.token = data.token;
          httpRequestInterceptor.updateToken(session.token);          
          if (session.isValid()) {
            if (isTokenChanged(oldDecodedToken, newDecodedToken)) {
             var userObj;
             $http.get(REST_API.users + session.user.username, {
                headers: {
                  'x-auth-token': session.token
                }
              }).then(function(user) {
            	  userObj = user;
            	  return $http.get(REST_API.users + session.user.username + "/applications", {
            	        headers: {
            	          'x-auth-token': session.token
            	        }
            	      })  
              }).then(function(apps) {
	              userObj.data.applications = apps.data;
	              session.user = userObj.data;
                $rootScope.$broadcast(AUTH_EVENTS.userChanged);
              });
            }
            //launchTokenBeforeExpiration();
        	refreshTokenAtFixedRate();
            localStorageService.set(localStorageName, session);
          }
        }).error(function () {
        	console.log("Scheduling token renew after renewal error")
        	refreshTokenAtFixedRate();
        });
      };

        var refreshUserStatusAtFixedRate = function() {
  	      console.log("in refreshUserStatusAtFixedRate()");
  	      updateUserStatusTimeout = $timeout(updateUserStatus, updateUserStatusDelay);
  	      console.log("updateUserStatusTimeout: " + updateUserStatusDelay);
        };

         var updateUserStatus = function() {
	       	  console.log("updating users status")
	       	  if($rootScope.users!=undefined){
	  	          angular.forEach($rootScope.users, function(user) {
	  	            $http.get(REST_API.users+user.username+'/heartbeat/isalive', {
	  	              headers: {
	  	                'x-auth-token': session.token,
	  	                'Cache-Control': 'no-cache'
	  	              }
	  	            }).success(function(data) {
	  	              	console.log(user.username + ": " + data.onlineStatus);
	  	              	user.onlineStatus = data.onlineStatus;
	  	            }).error(function(data){
	  	            	console.log("Error in updating user status");
	  	            });
 	  	          });
	         }
             refreshUserStatusAtFixedRate(); 
         }
         
         $rootScope.$on(AUTH_EVENTS.loginSuccess, function(event, next){
      	   refreshUserStatusAtFixedRate();
         })
         

         $rootScope.$on(AUTH_EVENTS.sessionTimeout, function(event, next){
      	   $timeout.cancel(updateUserStatusTimeout);
         })

         var refreshTokenAtFixedRate = function() {
      	console.log("in refreshTokenAtFixedRate()");
          timeout = $timeout(updateToken, updateTokenDelay);
      	  console.log("timeout: " + updateTokenDelay);
        };

      var launchTokenBeforeExpiration = function() {
        var delay = session.expDate.valueOf() - new Date().valueOf() + session.clientServerTimeDiff;
        if (delay >= 0) {
          timeout = $timeout(updateToken, Math.max(0, delay - updateTokenDelay));
        }
      };

      session.isValid = function() {
        var isValid = session.token && !isExpired();
        if (!isValid) {
          if (session.token) {
            $rootScope.$broadcast(AUTH_EVENTS.sessionTimeout);
          }
          session.destroy();
        }
        return isValid;
      };

      session.create = function(clientIssDate, username, token, user, remember) {
        session.destroy();
        session.token = token;
        session.user = user;
        decodeToken(token);
        httpRequestInterceptor.updateToken(session.token);
        session.clientServerTimeDiff = clientIssDate.valueOf() - session.issDate.valueOf();
        if (session.isValid()) {
          //launchTokenBeforeExpiration();
        	refreshTokenAtFixedRate();

          if (remember === true) {
            localStorageService.set(localStorageName, session);
          }
        }
      };

      session.destroy = function() {
        session.token = null;
        session.expDate = null;
        session.user = null;
        session.issDate = null;
        session.clientServerTimeDiff = null;
        $timeout.cancel(timeout);
   	   	$timeout.cancel(updateUserStatusTimeout);
       localStorageService.remove(localStorageName);
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
        }
      }

      return session;
    }]);

ewallPortalAuthentication.directive('password', function() {
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.password = function(modelValue, viewValue) {
        var len = viewValue.length;
        if (len == 0) return true;
        if (len < 8) return false;
        if (!/\d/.test(viewValue)) return false;
        if (!/[a-zA-z]/.test(viewValue)) return false;
        return true;
      };
    }
  };
});