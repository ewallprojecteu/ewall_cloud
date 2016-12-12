'use strict';

var fusionerYoutubeConstants = angular.module('fusionerYoutubeConstants', [
    'ngResource'
]);

fusionerYoutubeConstants.config(['$resourceProvider', function($resourceProvider) {
	$resourceProvider.defaults.stripTrailingSlashes = false;
}]);

fusionerYoutubeConstants
		.constant(
				'REST_API',
				{
					LOGIN_ADMIN: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/authenticateAdmin/',
					TOKEN_UPDATE: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/renew/',
					PROFILING_SERVER: '[protocol]//[hostname]/platform-[env]/profiling-server',
					FUSIONER_YOUTUBE: '[protocol]//[hostname]/applications-[env]/fusioner-youtube',
					REDIRECT_URI_GOOGLE: '[protocol]//[hostname]/applications-[env]/YouTubeExercisesPortal/dummy/callback',
					OPEN_AUTH: '[protocol]//[hostname]/applications-[env]/YouTubeExercisesPortal/dummy/prepare'

					//	Uncomment the following lines and comment out the above lines, to test in local envs
					/*LOGIN_ADMIN: 'http://localhost:8080/ewall-platform-login/v1/users/authenticateAdmin/',
					TOKEN_UPDATE: 'http://localhost:8080/ewall-platform-login/v1/users/renew/',
					PROFILING_SERVER: 'http://localhost:8080/profiling-server',
					FUSIONER_YOUTUBE: 'http://localhost:8080/fusioner-youtube',
					REDIRECT_URI_GOOGLE: 'http://localhost:8082/YouTubeExercisesPortal/dummy/callback',
					OPEN_AUTH: 'http://localhost:8082/YouTubeExercisesPortal/dummy/prepare'*/
				});
