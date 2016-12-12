'use strict';

var twitterAuthenticatorCredentialsService = angular.module('twitterAuthenticatorCredentialsService', []);

twitterAuthenticatorCredentialsService.factory('Credentials', ['$http', 'REST_API', function ($http, REST_API) {
    return {
        getRequestTokenWithUrlPromise: function () {
            return $http.get(REST_API.FUSIONER_TWITTER + '/oauth/requestTokenWithUrl');
        },
        postTwitterCredentialsForUsernamePromise: function (username, twitterRequestTokenWithVerifier) {
            return $http.post(REST_API.FUSIONER_TWITTER + '/oauth/' + username + '/credentials', twitterRequestTokenWithVerifier);
        },
        getFlagCredentialsExistPromise: function (username) {
            return $http.get(REST_API.FUSIONER_TWITTER + '/oauth/' + username + '/credentials/exist');
        }
    };
}]);