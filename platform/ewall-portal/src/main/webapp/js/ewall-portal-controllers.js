'use strict';

/* Controllers */

var ewallPortalControllers = angular.module('ewallPortalControllers', ['ui.router', 'ewallPortalAuthentication',
    'ewallPortalUsers', 'ngLocalize', 'ewallPortalHelpers', 'language-picker', 'LocalStorageModule']);

ewallPortalAuthentication.config(['localStorageServiceProvider','$httpProvider', function(localStorageServiceProvider,$httpProvider) {
  localStorageServiceProvider.setPrefix('eWALL-Portal');
  $httpProvider.interceptors.push('httpRequestInterceptor');
}]);

ewallPortalControllers.controller('LoginFormCtrl', ['$scope', '$rootScope', '$state', 'AuthService', 'AUTH_EVENTS',
    function($scope, $rootScope, $state, AuthService, AUTH_EVENTS) {
      if (AuthService.isAuthenticated()) {
        $state.go('home.welcome');
      }

      $scope.rememberMe = false;
      $scope.credentials = {
        username: '',
        password: ''
      };

      $scope.login = function(credentials) {
        $scope.showLoading();
        AuthService.login(credentials, $scope.rememberMe).then(function() {
          $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
          $scope.hideLoading();
          $state.go('home.welcome');
        }, function() {
          $scope.hideLoading();
          $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
        });
      };
    }]);

ewallPortalControllers.controller('ApplicationController', [
    '$scope',
    'USER_ROLES',
    'AuthService',
    'Session',
    'AUTH_EVENTS',
    'locale',
    'localeSupported',
    'localStorageService',
    function($scope, USER_ROLES, AuthService, Session, AUTH_EVENTS, locale, localeSupported, localStorageService) {
      $scope.currentSession = Session;
      $scope.userRoles = USER_ROLES;
      $scope.isAuthorized = AuthService.isAuthorized;

      $scope.isPlatformAdmin = function() {
        return $scope.currentSession.user.userRole == $scope.userRoles.platformAdministrator;
      };

      $scope.errorMessage = '';
      $scope.showError = function(message) {
        $scope.errorMessage = message;
        $('#errorModal').modal('show');
      };
      
      $scope.successMessage = '';
      $scope.showSuccessMessage = function(message) {
        $scope.successMessage = message;
        $('#successModal').modal('show');
      };
      
      $scope.showLoading = function() {
        $('#pleaseWaitDialog').modal('show');
      };
      $scope.hideLoading = function() {
        $('#pleaseWaitDialog').modal('hide');
      };

      function getCountry(locale) {
        var splitLocale = locale.split('-');
        if (splitLocale.length > 1) { return splitLocale[1].toLowerCase(); }
        return locale;
      }

      function createLanguageObj(locale) {
        var language = languageMappingList[locale] || {
          nativeName: locale,
          englishName: locale
        };
        language.code = locale;
        language.country = language.country || getCountry(locale);
        return language;
      }

      $scope.supportedLangs = localeSupported;

      var localStorageName = 'language';
      var savedLanguage = localStorageService.get(localStorageName);
      var language = (savedLanguage || window.navigator.userLanguage || window.navigator.language).replace(
              /(\w+)[-_]?(\w*)/, function(match, p1, p2) {
                if (p2) return p1.toLowerCase() + '-' + p2.toUpperCase();
                return p1.toLowerCase();
              });
      locale.setLocale(language);
      $scope.currentlanguage = createLanguageObj(locale.getLocale());
      localStorageService.set(localStorageName, language);

      $scope.onLanguageChange = function(language) {
        locale.setLocale(language.code);
        $scope.currentlanguage = language;
        localStorageService.set(localStorageName, language.code);
      };

      $scope.$on(AUTH_EVENTS.notAuthorized, function(event, next) {
        $scope.showError(AUTH_EVENTS.notAuthorized);
      });

      $scope.$on(AUTH_EVENTS.sessionTimeout, function(event, next) {
        locale.ready('common').then(function() {
          $scope.showError(locale.getString('common.sessionExpired'));
        });
      });

      $scope.$on('$stateChangeStart', function(event, next) {
        $scope.hideLoading();
      });

      $scope.$on(AUTH_EVENTS.loginFailed, function(event, next) {
        locale.ready('common').then(function() {
          $scope.showError(locale.getString('common.loginFailed'));
        });
      });
    }]);

ewallPortalControllers.controller('HomeCtrl', ['$scope', '$state', 'DeleteManager',
    function($scope, $state, DeleteManager) {
      $scope.DeleteManager = DeleteManager;
      $scope.logout = function() {
        $scope.currentSession.destroy();
        $state.go('login');
      }
    }]);
