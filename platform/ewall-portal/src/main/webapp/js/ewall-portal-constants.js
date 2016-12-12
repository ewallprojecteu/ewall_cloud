'use strict';

var ewallPortalConstants = angular.module('ewallPortalConstants',
		[ 'ngResource' ]);

ewallPortalConstants.config([ '$resourceProvider', function($resourceProvider) {
	$resourceProvider.defaults.stripTrailingSlashes = false;
} ]);

ewallPortalConstants
		.constant(
				'REST_API',
				{
					login : '../ewall-platform-login/v1/users/authenticate/',
					tokenUpdate : '../ewall-platform-login/v1/users/renew/',
					users : '../profiling-server/users/',
					userPhoto : '../profiling-server/users/:username/photo',
					regions : '../profiling-server/regions/',
					services : '../profiling-server/services/',
					applications : '../profiling-server/applications/',
					sensingEnvs : '../cloud-gateway/sensingenvironments/',
					devices : '../cloud-gateway/sensingenvironments/:sensEnvId/devices/:deviceId',
					devicesSet : '../cloud-gateway/sensingenvironments/:sensEnvId/devices/defaultSet',

					sensingEnvStatus : '../cloud-gateway/sensingenvironments/:sensEnvId/status',
					sensingEnvConfig : '../cloud-gateway/sensingenvironments/:sensEnvId/configuration'

/*					login : 'http://localhost:8080/ewall-platform-login/v1/users/authenticate/',
					tokenUpdate : 'http://localhost:8080/ewall-platform-login/v1/users/renew/',
					users : 'http://localhost:8080/profiling-server/users/',
					userPhoto : 'http://localhost:8080/profiling-server/users/:username/photo',
					regions : 'http://localhost:8080/profiling-server/regions/',
					services : 'http://localhost:8080/profiling-server/services/',
					applications : 'http://localhost:8080/profiling-server/applications/',
					sensingEnvs : 'http://localhost:8080/platform-dev/cloud-gateway/sensingenvironments/',
					devices : 'http://localhost:8080/platform-dev/cloud-gateway/sensingenvironments/:sensEnvId/devices/:deviceId',
					devicesSet : 'http://localhost:8080/platform-dev/cloud-gateway/sensingenvironments/:sensEnvId/devices/defaultSet',

					sensingEnvStatus : 'http://localhost:8080/platform-dev/cloud-gateway/sensingenvironments/:sensEnvId/status',
					sensingEnvConfig : 'http://localhost:8080/platform-dev/cloud-gateway/sensingenvironments/:sensEnvId/configuration'
*/
				});
