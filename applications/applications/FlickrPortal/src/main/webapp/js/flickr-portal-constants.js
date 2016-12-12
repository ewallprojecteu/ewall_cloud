'use strict';

var flickrPortalConstants = angular.module('flickrPortalConstants', [
    'ngResource'
]);

flickrPortalConstants.config(['$resourceProvider', function($resourceProvider) {
	$resourceProvider.defaults.stripTrailingSlashes = false;
}]);

flickrPortalConstants
		.constant(
				'REST_API',
				{
					LOGIN_ADMIN: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/authenticateAdmin/',
					TOKEN_UPDATE: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/renew/',
					USERS: '[protocol]//[hostname]/platform-[env]/profiling-server/users/',
					OPEN_AUTH_FLICKR: '[protocol]//[hostname]/applications-[env]/FlickrPortal/dummy/prepare',
					REDIRECT_URI_FLICKR: '[protocol]//[hostname]/applications-[env]/FlickrPortal/dummy/callback',
                    FLICKR_AUTH_URL: '[protocol]//[hostname]/applications-[env]/fusioner-flickr/oauth/requestTokenWithUrl',
                    CREDENTIALS: '[protocol]//[hostname]/applications-[env]/fusioner-flickr/oauth/:username/credentials',
                    FUSIONER_FLICKR: '[protocol]//[hostname]/applications-[env]/fusioner-flickr',
                    OAUTH: '[protocol]//[hostname]/applications-[env]/fusioner-flickr/oauth/',
                    PHOTOS: '[protocol]//[hostname]/applications-[env]/fusioner-flickr/photos/:username'

					//	Uncomment the following lines and comment out the above lines, to test in local envs       
					/*LOGIN_ADMIN: 'http://localhost:8080/ewall-platform-login/v1/users/authenticateAdmin/',
					TOKEN_UPDATE: 'http://localhost:8080/ewall-platform-login/v1/users/renew/',
					USERS: 'http://localhost:8080/profiling-server/users/',
					OPEN_AUTH_FLICKR: 'http://localhost:8082/FlickrPortal/dummy/prepare',
					REDIRECT_URI_FLICKR: 'http://localhost:8082/FlickrPortal/dummy/callback',
					FLICKR_AUTH_URL: 'http://localhost:8080/fusioner-flickr/oauth/requestTokenWithUrl',
					CREDENTIALS: 'http://localhost:8080/fusioner-flickr/oauth/:username/credentials',
					FUSIONER_FLICKR: 'http://localhost:8080/fusioner-flickr',
					OAUTH: 'http://localhost:8080/fusioner-flickr/oauth/',
					PHOTOS: 'http://localhost:8080/fusioner-flickr/photos/:username'*/
				});
