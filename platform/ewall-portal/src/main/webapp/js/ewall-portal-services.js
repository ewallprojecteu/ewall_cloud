'use strict';

var ewallPortalServices = angular.module('ewallPortalServices', ['ui.router', 'ewallPortalHelpers',
    'ewallPortalConstants', 'ngResource', 'ewallPortalAuthentication']);

ewallPortalServices.controller('ServicesCtrl', ['$scope', 'Services', 'TableManager',
    function($scope, Services, TableManager) {
      $scope.showLoading();
      $scope.services = Services.query({}, function() {
        $scope.hideLoading();
        $scope.tableParams = TableManager.generateTableParams($scope, $scope.services, 'query', 'name');
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
        Services.removeIt($scope);
      };
      $scope.showDeleteModal = function(service) {
        Services.showDeleteModal(service);
      };
    }]);

ewallPortalServices.controller('ServiceEditCtrl', ['$scope', '$state', '$stateParams', 'Services',
    function($scope, $state, $stateParams, Services) {
      $scope.showLoading();
      $scope.service = Services.defaultService();
      if ($stateParams.name) {
        $scope.service = Services.get({
          name: $stateParams.name
        }, $scope.hideLoading);
      } else if (!$stateParams.name) {
        $scope.hideLoading();
      }

      $scope.submit = function() {
        Services.submit($scope, $scope.service, $stateParams.name);
      };

    }]);

ewallPortalServices.factory('Services', ['$resource', 'REST_API', 'SubmitManager', 'TokenHeaderGenerator',
    'DeleteManager', function($resource, REST_API, SubmitManager, TokenHeaderGenerator, DeleteManager) {
      var api = $resource(REST_API.services + ':name', {
        name: ''
      }, TokenHeaderGenerator.generateActions({}));

      api.submit = function(scope, object, objectNameToUpdate) {
        SubmitManager.submit(api, scope, object, 'name', objectNameToUpdate, 'home.services');
      };

      api.removeIt = function(scope) {
        DeleteManager.remove(scope, scope.services, api, 'name', scope.tableParams);
      };
      api.showDeleteModal = function(service) {
        var serviceDeleteWarning = 'service ' + service.name;
        DeleteManager.showDeleteModal(service, service.name, serviceDeleteWarning);
      };

      api.defaultService = function() {
        return {
          "links": [{
            "href": ""
          }],
          "name": "",
          "uuid": ""
        };
      };

      return api;
    }]);
