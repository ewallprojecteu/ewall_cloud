'use strict';

var ewallPortalApplications = angular.module('ewallPortalApplications', ['ui.router', 'ewallPortalHelpers',
    'ewallPortalServices', 'ngLocalize', 'ewallPortalConstants', 'ngResource', 'ewallPortalAuthentication',
    'frapontillo.bootstrap-duallistbox']);

ewallPortalApplications.constant('APP_TYPES', {
  HEALTH: 'health',
  CONTACTS: 'contacts',
  HOUSE: 'house',
  GAMES: 'games',
  MINIMIZED: 'minimized',
  HIDDEN: 'hidden',
  WEB_APP: 'web_app'
});

ewallPortalApplications.directive('appType', ['APP_TYPES', 'locale', 'localeEvents',
    function(APP_TYPES, locale, localeEvents) {
      function update(elm, value) {
        locale.ready('applications').then(function() {
          elm.html(locale.getString('applications.' + APP_TYPES[value]));
        });
      }
      return {
        restrict: 'A',
        link: function(scope, elm, attrs, ctrl) {
          attrs.$observe('appType', function(newVal, oldVal) {
            if (newVal && newVal != oldVal) {
              update(elm, newVal);
            }
          });
          scope.$on(localeEvents.localeChanges, function(event, data) {
            update(elm, attrs.appType);
          });
        }
      };
    }]);

ewallPortalApplications.controller('ApplicationsCtrl', ['$scope', 'Applications', 'TableManager',
    function($scope, Applications, TableManager) {
      $scope.showLoading();
      $scope.applications = Applications.query({}, function() {
        $scope.hideLoading();
        $scope.tableParams = TableManager.generateTableParams($scope, $scope.applications, 'query', 'name');
      });
      $scope.headerClass = function(name) {
        return TableManager.headerClass($scope.tableParams, name);
      };
      $scope.headerClick = function(name) {
        TableManager.headerClick($scope.tableParams, name);
      };
      $scope.$watch("query", function(newvalue, oldvalue) {
        if ($scope.tableParams) $scope.tableParams.reload();
      });

      $scope.remove = function() {
        Applications.removeIt($scope);
      };
      $scope.showDeleteModal = function(application) {
        Applications.showDeleteModal(application);
      };
    }]);

ewallPortalApplications.controller('ApplicationEditCtrl', [
    '$scope',
    '$state',
    '$stateParams',
    'Applications',
    'Services',
    'Utils',
    'locale',
    'localeEvents',
    function($scope, $state, $stateParams, Applications, Services, Utils, locale, localeEvents) {
      $scope.showLoading();
      $scope.application = Applications.defaultApplication();

      function setDuallistboxLocales() {
        Utils.setDuallistbox('services');
        locale.ready('applications').then(
                function() {
                  $('#services').bootstrapDualListbox('setSelectedListLabel',
                          locale.getString('applications.dependsOnServices'));
                  $('#services').bootstrapDualListbox('setNonSelectedListLabel',
                          locale.getString('applications.availableServices'));
                });
      }
      $scope.services = Services.query({}, function() {
        if ($stateParams.name) {
          $scope.application = Applications.get({
            name: $stateParams.name
          }, function() {
         	  $scope.hideLoading();
         	  $scope.serviceNames = Utils.filterValues($scope.services, 'name'); 
           $scope.application.encompassingServiceNames = Utils.filterList($scope.serviceNames, $scope.application.encompassingServiceNames);
          });
        } else if (!$stateParams.name) {
          $scope.hideLoading();
          $scope.serviceNames = Utils.filterValues($scope.services, 'name'); 
        }
        setDuallistboxLocales();
      });
      $scope.$on(localeEvents.localeChanges, function(event, data) {
        setDuallistboxLocales();
      });

      $scope.editing = $stateParams.name;

      $scope.submit = function() {
        Applications.submit($scope, $scope.application, $stateParams.name);
      };

    }]);

ewallPortalApplications.factory('Applications', ['$resource', 'REST_API', 'SubmitManager', 'TokenHeaderGenerator',
    'DeleteManager', function($resource, REST_API, SubmitManager, TokenHeaderGenerator, DeleteManager) {
      var api = $resource(REST_API.applications + ':name', {
        name: ''
      }, TokenHeaderGenerator.generateActions({}));

      api.submit = function(scope, object, objectNameToUpdate) {
        SubmitManager.submit(api, scope, object, 'name', objectNameToUpdate, 'home.applications');
      };

      api.removeIt = function(scope) {
        DeleteManager.remove(scope, scope.applications, api, 'name', scope.tableParams);
      };
      api.showDeleteModal = function(application) {
        var applicationDeleteWarning = 'application ' + application.name;
        DeleteManager.showDeleteModal(application, application.name, applicationDeleteWarning);
      };

      api.defaultApplication = function() {
        return {
          "name": "",
          "type": "",
          "encompassingServiceNames": [],
          "links": [{
            "href": ""
          }]
        };
      };

      return api;
    }]);