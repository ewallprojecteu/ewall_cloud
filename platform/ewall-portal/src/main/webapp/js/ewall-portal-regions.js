'use strict';

var ewallPortalRegions = angular.module('ewallPortalRegions', ['ui.router', 'ewallPortalHelpers',
    'ewallPortalConstants', 'ngResource', 'ewallPortalAuthentication']);

ewallPortalRegions.controller('RegionsCtrl', ['$scope', 'Regions', 'TableManager',
    function($scope, Regions, TableManager) {
      $scope.showLoading();
      $scope.regions = Regions.query({}, function() {
        $scope.hideLoading();
        $scope.tableParams = TableManager.generateTableParams($scope, $scope.regions, 'query', 'regionName');
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
        Regions.removeIt($scope);
      };
      $scope.showDeleteModal = function(region) {
        Regions.showDeleteModal(region);
      };
    }]);

ewallPortalRegions.controller('RegionEditCtrl', ['$scope', '$state', '$stateParams', 'Regions',
    function($scope, $state, $stateParams, Regions) {
      $scope.showLoading();
      $scope.region = Regions.defaultRegion();
      if ($stateParams.name) {
        $scope.region = Regions.get({
          regionName: $stateParams.name
        }, $scope.hideLoading);
      } else if (!$stateParams.name) {
        $scope.hideLoading();
      }

      $scope.editing = $stateParams.name;

      $scope.submit = function() {
        Regions.submit($scope, $scope.region, $stateParams.name);
      };

    }]);

ewallPortalRegions.factory('Regions', ['$resource', 'REST_API', 'SubmitManager', 'TokenHeaderGenerator',
    'DeleteManager', function($resource, REST_API, SubmitManager, TokenHeaderGenerator, DeleteManager) {
      var api = $resource(REST_API.regions + ':regionName', {
        regionName: ''
      }, TokenHeaderGenerator.generateActions({
        'getLabels': {
          method: 'GET',
          isArray: true,
          transformResponse: function(data, headersGetter) {
            var regions = $.map(angular.fromJson(data), function(el) {
              return el.regionName;
            });
            return regions;
          }
        }
      }));

      api.submit = function(scope, object, objectNameToUpdate) {
        SubmitManager.submit(api, scope, object, 'regionName', objectNameToUpdate, 'home.regions');
      };

      api.removeIt = function(scope) {
        DeleteManager.remove(scope, scope.regions, api, 'regionName', scope.tableParams);
      };
      api.showDeleteModal = function(region) {
        var regionDeleteWarning = 'region ' + region.regionName;
        DeleteManager.showDeleteModal(region, region.regionName, regionDeleteWarning);
      };

      api.defaultRegion = function() {
        return {
          "country": "",
          "regionName": "",
          "uuid": ""
        };
      };

      return api;
    }]);