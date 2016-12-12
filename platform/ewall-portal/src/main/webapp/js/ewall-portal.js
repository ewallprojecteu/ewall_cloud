'use strict';

/* App Module */

var ewallPortal = angular.module('ewallPortal', ['ui.router', 'ngLocalize', 'ewallPortalControllers',
    'ewallPortalUsers', 'ewallPortalRegions', 'ewallPortalServices', 'ewallPortalApplications',
    'ewallPortalSensingEnvs', 'ewallPortalDevices', 'ewallPortalAuthentication', 'angularModalService', 'ewallSensEnvAddPlaceModal']);

ewallPortal.value('localeConf', {
  basePath: 'languages',
  defaultLocale: 'en-US',
  sharedDictionary: 'common',
  fileExtension: '.lang.json',
  persistSelection: true,
  cookieName: 'COOKIE_LOCALE_LANG',
  observableAttrs: new RegExp('^data-(?!ng-|i18n)'),
  delimiter: '::'
}).value('localeSupported',
        ['bg-BG', 'da-DK', 'de-AT', 'en-US', 'hr-HR', 'it-IT', 'mk-MK', 'nl-NL', 'ro-RO', 'sr-SP'].sort()).value(
        'localeFallbacks', {
          'en': 'en-US',
          'it': 'it-IT',
          'bg': 'bg-BG',
          'da': 'da-DK',
          'de': 'de-AT',
          'hr': 'hr-HR',
          'mk': 'mk-MK',
          'nl': 'nl-NL',
          'ro': 'ro-RO',
          'sr': 'sr-SP'
        });

ewallPortal.config(['$stateProvider', '$urlRouterProvider', 'USER_ROLES',
    function($stateProvider, $urlRouterProvider, USER_ROLES) {
      $urlRouterProvider.otherwise("/login");
      $stateProvider.state('login', {
        url: '/login',
        templateUrl: 'partials/login.html',
        controller: 'LoginFormCtrl',
        data: {
          authorizedRoles: [USER_ROLES.all],
          authenticationRequired: false
        }
      }).state('home', {
        abstract: true,
        url: '/home',
        templateUrl: 'partials/home.html',
        controller: 'HomeCtrl',
      }).state('home.welcome', {
        url: '/welcome',
        templateUrl: 'partials/home-welcome.html',
        data: {
          authorizedRoles: [USER_ROLES.all],
          authenticationRequired: true
        }
      }).state('home.users', {
        url: '/users',
        templateUrl: 'partials/home-users.html',
        controller: 'UsersCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator, USER_ROLES.regionAdministrator],
          authenticationRequired: true
        }
      }).state('home.editUser', {
        url: '/user/edit/{username}',
        templateUrl: 'partials/home-user-edit.html',
        controller: 'UserEditCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator, USER_ROLES.regionAdministrator],
          authenticationRequired: true
        }
      }).state('home.regions', {
        url: '/regions',
        templateUrl: 'partials/home-regions.html',
        controller: 'RegionsCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator],
          authenticationRequired: true
        }
      }).state('home.editRegion', {
        url: '/region/edit/{name}',
        templateUrl: 'partials/home-region-edit.html',
        controller: 'RegionEditCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator],
          authenticationRequired: true
        }
      }).state('home.services', {
        url: '/services',
        templateUrl: 'partials/home-services.html',
        controller: 'ServicesCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator],
          authenticationRequired: true
        }
      }).state('home.editService', {
        url: '/service/edit/{name}',
        templateUrl: 'partials/home-service-edit.html',
        controller: 'ServiceEditCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator],
          authenticationRequired: true
        }
      }).state('home.applications', {
        url: '/applications',
        templateUrl: 'partials/home-applications.html',
        controller: 'ApplicationsCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator],
          authenticationRequired: true
        }
      }).state('home.editApplication', {
        url: '/application/edit/{name}',
        templateUrl: 'partials/home-application-edit.html',
        controller: 'ApplicationEditCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator],
          authenticationRequired: true
        }
      }).state(
	    'home.sensingEnvs',
	    {
	    url: '/sensingEnvs',
	    templateUrl: 'partials/home-sensingEnvs.html',
	    controller: 'SensingEnvsCtrl',
	    data: {
	      authorizedRoles: [USER_ROLES.platformAdministrator,
	        USER_ROLES.regionAdministrator],
	        authenticationRequired: true
	    }
	  }).state(
        'home.editSensingEnv',
        {
        url: '/sensing-env/edit/{uuid}',
        templateUrl: 'partials/home-sensing-env-edit.html',
        controller: 'SensingEnvEditCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator,
              USER_ROLES.regionAdministrator],
          authenticationRequired: true
        }
      }).state(
      'home.sensingEnvConfig',
      {
        url: '/sensing-env/{uuid}/config',
        templateUrl: 'partials/home-sensing-env-config.html',
        controller: 'SensingEnvConfigCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator,
              USER_ROLES.regionAdministrator],
          authenticationRequired: true
        }
      }).state(
      'home.devices',
      {
        url: '/devices',
        templateUrl: 'partials/home-devices.html',
        controller: 'DevicesCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator,
            USER_ROLES.regionAdministrator],
            authenticationRequired: true
      }
    }).state(
        'home.editDevice',
        {
        url: '/devices/edit?sens_env_id={sensEnvId}&device_id={deviceId}',
        templateUrl: 'partials/home-device-edit.html',
        controller: 'DeviceEditCtrl',
        data: {
          authorizedRoles: [USER_ROLES.platformAdministrator,
              USER_ROLES.regionAdministrator],
          authenticationRequired: true
        }
      });
}]);

ewallPortal.run(['$rootScope', '$state', 'AUTH_EVENTS', 'AuthService',
    function($rootScope, $state, AUTH_EVENTS, AuthService) {

      $rootScope.$on('$stateChangeStart', function(event, next) {
        var authorizedRoles = next.data.authorizedRoles;
        if (!AuthService.isAuthorized(authorizedRoles)) {
          event.preventDefault();
          if (AuthService.isAuthenticated()) {
            // user is not allowed
            $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
          } else {
            // user is not logged in
            $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
          }
        } else {
          if (next.data.authenticationRequired && !AuthService.isAuthenticated()) {
            event.preventDefault();
            $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
          }
        }
      });

      $rootScope.$on(AUTH_EVENTS.notAuthenticated, function(event, next) {
        $state.transitionTo('login', {}, {
          location: "replace"
        });
      });

    }]);