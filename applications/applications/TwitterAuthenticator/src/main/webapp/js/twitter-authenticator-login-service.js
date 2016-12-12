'use strict';

var twitterAuthenticatorLoginService = angular.module('twitterAuthenticatorLoginService', []);

twitterAuthenticatorLoginService.factory('LoginHelper', ['$http', 'localStorageService',
                                           function ($http, localStorageService) {
	var loginConstant = "login";
    var userRoleWithRegionConstant = "userRoleWithRegion";
    
    return {
        setToken: function (token) {
            localStorageService.set(loginConstant, token);
            $http.defaults.headers.common['x-auth-token'] = token;
        },
        isTokenSet: function (callback) {
            var token = $http.defaults.headers.common['x-auth-token'];
            if (!token) {
                token = localStorageService.get(loginConstant);
                if (!token) {
                    callback(false);
                } else {
                    $http.defaults.headers.common['x-auth-token'] = token;
                    callback(true);
                }
            } else {
                callback(true);
            }
        },
        setUserRoleWithRegion: function(userRoleWithRegion) {
            localStorageService.set(userRoleWithRegionConstant, userRoleWithRegion);
        },
        getUserRoleWithRegion: function(callback) {
            callback(localStorageService.get(userRoleWithRegionConstant));
        }
    };
}]);
