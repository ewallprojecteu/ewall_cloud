'use strict';

var fusionerYoutubeAuthenticationService = angular.module('fusionerYoutubeAuthenticationService', []);

fusionerYoutubeAuthenticationService.factory('Authentication', ['$http', 'REST_API', function ($http, REST_API) {
    return {
        getAuthUrlPromise: function () {
            return $http.get(REST_API.FUSIONER_YOUTUBE + '/oauth/url');
        },
        postUsernameCodeForCredentialsPromise: function (username, code) {
            return $http.post(REST_API.FUSIONER_YOUTUBE + '/oauth/' + username + '/credentials', code);
        },
        doCredentialsExistForUsernamePromise: function (username) {
            return $http.get(REST_API.FUSIONER_YOUTUBE + '/oauth/' + username + '/credentials/exist');
        },
        deleteCredentialsAndRevokeAccessForUsernamePromise: function (username) {
            return $http.delete(REST_API.FUSIONER_YOUTUBE + '/oauth/' + username + '/credentials');
        }
    };
}]);