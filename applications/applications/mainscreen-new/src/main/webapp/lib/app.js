'use strict';

String.prototype.toCamelCase = function() {
  return this.replace(/^([A-Z])|\s+(\w)/g, function(match, p1, p2, offset) {
    if (p2) return p2.toUpperCase();
    return p1.toLowerCase();
  });
};

///*Empty controller for Sleep Applicatio*/
//var providers = {};
//angular.module('ewallSleep', [], function($controllerProvider, $compileProvider, $provide) {
//    providers = {
//        $controllerProvider: $controllerProvider,
//        $compileProvider: $compileProvider,
//        $provide: $provide
//    };
//});


//angular.bootstrap($('body'), ['ewallSleep']);



/* App Module */
var ewallApp = angular.module('ewallApp', ['ui.router', 'ewallAppControllers', 'ewallAppServices', 'ewallWeather', 'touchSelect','ewallRecommendationNotification', 'ewallGeneralIDSSNotification', 'ewallAgenda', 'ewallVirtualCoachNotification', 'ewallBackgroundSwitch', 'physicalActivityWidget', 'photoFrameWidget', 'ui.bootstrap-slider']);
// old
// var ewallApp = angular.module('ewallApp', ['ui.router', 'ewallAppControllers', 'ewallAppServices', 'ewallWeather']);

ewallApp.config(['$stateProvider', '$urlRouterProvider','$controllerProvider','$provide','$compileProvider',
    function($stateProvider, $urlRouterProvider, $controllerProvider, $provide, $compileProvider) {
      $urlRouterProvider.otherwise('/login');
      ewallApp.stateProvider = $stateProvider.state('login', {
        url: '/login',
        templateUrl: 'login.html',
        controller: 'LoginFormCtrl',
        data: {
          authenticationRequired: false
        }
      }).state('main', {
          url: '/main',
          templateUrl: 'pictureMainScreen.html',
          controller: 'MainscreenCtrl',
          data: {
            authenticationRequired: true
          }
        });
      
      ewallApp._controller = ewallApp.controller;
      ewallApp._service = ewallApp.service;
      ewallApp._factory = ewallApp.factory;
      ewallApp._value = ewallApp.value;
      ewallApp._directive = ewallApp.directive;
      ewallApp._filter = ewallApp.filter;
      // Provider-based controller.
      ewallApp.controller = function( name, constructor ) {
          $controllerProvider.register( name, constructor );
          return( this );
      };

      // Provider-based service.
      ewallApp.service = function( name, constructor ) {
          $provide.service( name, constructor );
          return( this );
      };

      // Provider-based factory.
      ewallApp.factory = function( name, factory ) {
          $provide.factory( name, factory );
          return( this );
      };

      // Provider-based value.
      ewallApp.value = function( name, value ) {
          $provide.value( name, value );
          return( this );
      };

      // Provider-based directive.
      ewallApp.directive = function( name, factory ) {
          $compileProvider.directive( name, factory );
          return( this );
      };
 
      ewallApp.filter = function( name, factory ) {
          $filterProvider.filter( name, factory );
          return( this );
      };
      
    }]);

ewallApp.run(['$rootScope', '$state', 'AUTH_EVENTS', 'AuthService',
    function($rootScope, $state, AUTH_EVENTS, AuthService) {

      $rootScope.$on('$stateChangeStart', function(event, next) {
        if (next.data.authenticationRequired && !AuthService.isAuthenticated()) {
          event.preventDefault();
          $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
        }
      });

      $rootScope.$on(AUTH_EVENTS.notAuthenticated, function(event, next) {
        $state.transitionTo('login', {}, {
          location: 'replace'
        });
      });

    }]);