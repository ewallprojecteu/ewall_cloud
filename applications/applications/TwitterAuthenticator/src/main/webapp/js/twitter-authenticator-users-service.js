'use strict';

var twitterAuthenticatorUsersService = angular.module('twitterAuthenticatorUsersService', []);

twitterAuthenticatorUsersService.factory('Users', ['$http', 'REST_API', function ($http, REST_API) {
    return {
        getUsersPromise: function () {
            return $http.get(REST_API.PROFILING_SERVER + '/users/');
        }
    };
}]);