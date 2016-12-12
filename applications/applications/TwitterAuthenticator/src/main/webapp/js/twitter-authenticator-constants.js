'use strict';

var twitterAuthenticatorConstants = angular.module('twitterAuthenticatorConstants', [
    'ngResource'
]);

twitterAuthenticatorConstants.config(['$resourceProvider', function($resourceProvider) {
	$resourceProvider.defaults.stripTrailingSlashes = false;
}]);

twitterAuthenticatorConstants
		.constant(
				'REST_API',
				{
					LOGIN_ADMIN: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/authenticateAdmin/',
					TOKEN_UPDATE: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/renew/',
					PROFILING_SERVER: '[protocol]//[hostname]/platform-[env]/profiling-server',
					FUSIONER_TWITTER: '[protocol]//[hostname]/applications-[env]/fusioner-twitter',
					REDIRECT_URI_TWITTER: '[protocol]//[hostname]/applications-[env]/TwitterAuthenticator/dummy/callback',
					OPEN_AUTH: '[protocol]//[hostname]/applications-[env]/TwitterAuthenticator/dummy/prepare'

					//	Uncomment the following lines and comment out the above lines, to test in local envs
					/*LOGIN_ADMIN: 'http://localhost:8080/ewall-platform-login/v1/users/authenticateAdmin/',
					TOKEN_UPDATE: 'http://localhost:8080/ewall-platform-login/v1/users/renew/',
					PROFILING_SERVER: 'http://localhost:8080/profiling-server',
					FUSIONER_TWITTER: 'http://localhost:8080/fusioner-twitter',
					REDIRECT_URI_TWITTER: 'http://localhost:8082/TwitterAuthenticator/dummy/callback',
					OPEN_AUTH: 'http://localhost:8082/TwitterAuthenticator/dummy/prepare'*/
				});
