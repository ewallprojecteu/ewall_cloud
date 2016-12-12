'use strict';

var fusionerYoutubeUserHelperService = angular.module('fusionerYoutubeUserHelperService', []);

fusionerYoutubeUserHelperService.factory('UserHelper', ['$http', 'localStorageService',
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