'use strict';

var flickrPortalUsersService = angular.module('flickrPortalUsersService', []);

flickrPortalUsersService.factory('Users', ['$resource', 'REST_API', 
    function ($resource, REST_API) {

        return $resource(REST_API.USERS, {});
    }]);

flickrPortalUsersService.factory('UserHelper', ['$http', 'localStorageService',
                                           function ($http, localStorageService) {
	var selectedUserConstant = "selectedUser";
	
	return {
        setStorageSelected: function (user) {
        	localStorageService.set(selectedUserConstant, user);
        },
        getStorageSelected: function (callback) {
        	callback(localStorageService.get(selectedUserConstant));
        },
        clearStorageSelected: function () {
        	localStorageService.remove(selectedUserConstant);
        }
    };
}]);