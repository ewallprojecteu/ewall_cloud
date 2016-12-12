'use strict';

var flickrPortal = angular.module('flickrPortal', [
    'ui.router',
    'ngResource',
    'LocalStorageModule',
    'ngLocalize',
    'flickrPortalHome',
    'flickrPortalLogin',
    'flickrPortalApplication',
    'flickrPortalConstants',
    'flickrPortalDirectives',
    'flickrPortalUserSelection',
    'flickrPortalAvailablePhotos',
    'flickrPortalUploadPhoto',
    'flickrPortalLoginService',
    'flickrPortalUsersService',
    'flickrPortalAuthorizationService',
    'flickrPortalPhotosService'
]);

flickrPortal.value('localeConf', {
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

flickrPortal.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/login');
    $stateProvider
	    .state('login', {
          url: '/login',
          templateUrl: 'partials/login.html',
          controller: 'LoginController'
      })
      .state('userSelection', {
	        url: '/user/selection',
	        templateUrl: 'partials/user-selection.html',
	        controller: 'UserSelectionController'
	    })
      .state('home', {
          abstract: true,
          url: '/home',
          templateUrl: 'partials/home.html',
          controller: 'HomeController'
      })
      .state('home.availablePhotos', {
          url: '/photos/available',
          templateUrl: 'partials/available-photos.html',
          controller: 'AvailablePhotoController'
      })
      .state('home.uploadPhoto', {
          url: '/photos/upload',
          templateUrl: 'partials/upload-photo.html',
          controller: 'UploadPhotoController'
      });
}]);

flickrPortal.config(function (localStorageServiceProvider) {
  localStorageServiceProvider.setPrefix('FlickrPortal');
  localStorageServiceProvider.setStorageType('sessionStorage');
});