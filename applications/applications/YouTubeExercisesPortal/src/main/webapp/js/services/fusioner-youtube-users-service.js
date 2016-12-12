'use strict';

var fusionerYoutubeUsersService = angular.module('fusionerYoutubeUsersService', []);

fusionerYoutubeUsersService.factory('Users', ['$http', 'REST_API', function ($http, REST_API) {
	return {
    	getPromise: function () {
	        return $http.get(REST_API.PROFILING_SERVER + '/users/');
        }
    };
}]);