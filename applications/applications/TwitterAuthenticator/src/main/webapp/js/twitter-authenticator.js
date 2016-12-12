'use strict';

var videoTraining = angular.module('twitterAuthenticator', [
    'ui.router',
    'LocalStorageModule',
    'twitterHome',
    'twitterAuthenticatorApplication',
    'twitterAuthenticatorLogin',
    'twitterAuthenticatorConstants',
    'twitterAuthenticatorDirectives',
    'twitterAuthenticatorUsersService',
    'twitterAuthenticatorCredentialsService',
    'twitterAuthenticatorLoginService'
]);

videoTraining.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/login');
    $stateProvider
	    .state('login', {
          url: '/login',
          templateUrl: 'partials/login.html',
          controller: 'LoginController'
      })
      .state('home', {
          url: '/home',
          templateUrl: 'partials/home.html',
          controller: 'HomeController'
      });
}]);

videoTraining.config(function (localStorageServiceProvider) {
  localStorageServiceProvider.setPrefix('TwitterAuthenticator');
  localStorageServiceProvider.setStorageType('sessionStorage');
});