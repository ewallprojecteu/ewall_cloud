'use strict';

var flickrPortalAuthorizationService = angular.module('flickrPortalAuthorizationService', []);

flickrPortalAuthorizationService.factory('CredentialsHelper', ['$http', 'REST_API', function ($http, REST_API) {
	return {
        doCredentialsExist: function (username) {
        	return $http.get(REST_API.OAUTH + username + '/credentials/exist');
        }
    };
}]);

flickrPortalAuthorizationService.factory('RequestTokenWithUrl', ['$resource', 'REST_API', function ($resource, REST_API) {
    return $resource(REST_API.FLICKR_AUTH_URL, {});
}]);

flickrPortalAuthorizationService.factory('Credentials', ['$resource', 'REST_API', function ($resource, REST_API) {
    return $resource(REST_API.CREDENTIALS, {});
}]);