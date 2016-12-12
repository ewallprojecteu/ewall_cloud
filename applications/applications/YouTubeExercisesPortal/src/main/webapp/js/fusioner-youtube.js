'use strict';

var fusionerYoutube = angular.module('fusionerYoutube', [
    'ui.router',
    'LocalStorageModule',
    'ngLocalize',
    'fusionerYoutubeLogin',
    'fusionerYoutubeHome',
    'fusionerYoutubeApplication',
    'fusionerYoutubeConstants',
    'fusionerYoutubeDirectives',
    'fusionerYoutubeUserSelection',
    'fusionerYoutubeAvailableVideos',
    'fusionerYoutubeUploadVideo',
    'fusionerYoutubeAddExternalVideo',
    'fusionerYoutubeHealthImpairmentTags',
    'fusionerYoutubeUsersService',
    'fusionerYoutubeUserHelperService',
    'fusionerYoutubeAuthenticationService',
    'fusionerYoutubeVideosService',
    'fusionerYoutubeHealthImpairmentTagsService',
    'fusionerYoutubeLoginService'
]);

fusionerYoutube.value('localeConf', {
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

fusionerYoutube.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/user/selection');
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
        .state('home.availableVideos', {
            url: '/videos/available',
            templateUrl: 'partials/available-videos.html',
            controller: 'AvailableVideoController'
        })
        .state('home.uploadVideo', {
            url: '/videos/upload',
            templateUrl: 'partials/upload-video.html',
            controller: 'UploadVideoController'
        })
        .state('home.addExternalVideo', {
            url: '/videos/addExternal',
            templateUrl: 'partials/add-external-video.html',
            controller: 'AddExternalVideoController'
        })
        .state('home.addHealthImpairmentTag', {
            url: '/healthImpairmentTags/add',
            templateUrl: 'partials/add-health-impairment-tag.html',
            controller: 'AddHealthImpairmentTagController'
        });
}]);

fusionerYoutube.config(function (localStorageServiceProvider) {
  localStorageServiceProvider.setPrefix('YouTubeExcercisesPortal');
  localStorageServiceProvider.setStorageType('sessionStorage');
});