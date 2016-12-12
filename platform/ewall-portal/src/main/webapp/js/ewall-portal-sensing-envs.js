'use strict';

var ewallPortalSensingEnvs = angular.module('ewallPortalSensingEnvs', ['ui.router', 'ewallPortalHelpers',
	'ewallPortalUsers', 'ewallPortalRegions', 'ewallPortalConstants', 'ngResource', 'ewallPortalAuthentication', 'acute.select']);

ewallPortalSensingEnvs.filter('region', ['$filter', function($filter) {
  return function(input, region) {
    if (region === 'all') return input;
    return $filter('filter')(input, {
      userProfile: {
        eWallSubProfile: {
          regionName: region
        }
      }
    });
  };
}]);

ewallPortalSensingEnvs.controller('SensingEnvsCtrl', ['$scope', 'SensingEnvs', 'TableManager',
	function($scope, SensingEnvs, TableManager) {
		$scope.showLoading();

		if (!$scope.isPlatformAdmin()) {
			var filterRegion = $scope.currentSession.user.userProfile.eWallSubProfile.regionName;
			$scope.sensingEnvs = SensingEnvs.query({ regionName: filterRegion }, function() {
				$scope.hideLoading();
				$scope.tableParams = TableManager.generateTableParams($scope, $scope.sensingEnvs, 'query', 'uuid');
			});

		} else {
			$scope.sensingEnvs = SensingEnvs.query({}, function() {
				$scope.hideLoading();
				$scope.tableParams = TableManager.generateTableParams($scope, $scope.sensingEnvs, 'query', 'uuid');
			});
		}
		
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
			SensingEnvs.removeIt($scope);
		};
		$scope.showDeleteModal = function(sensingEnv) {
			SensingEnvs.showDeleteModal(sensingEnv);
		};
	}]);

ewallPortalSensingEnvs.controller('SensingEnvEditCtrl', [
	'$scope',
	'$state',
	'$stateParams',
	'$filter',
	'locale',
	'SensingEnvs',
	'Users',
	'ModalService',
	function($scope, $state, $stateParams, $filter, locale, SensingEnvs, Users, ModalService) {
		$scope.showLoading();

		$scope.tempData = {
	        primaryUser: null
	    };

		$scope.getNotAssociatedWithSensEnvPrimaryUsers = function(callback) {
	        Users.getTargetUsers({ associatedWithSensEnvFilter: 'ONLY_NOT_ASSOCIATED_USERS' }, function(data) {
	        	var usernames = [];

	        	if (!$scope.isPlatformAdmin()) {
	        		$scope.filterRegion = $scope.currentSession.user.userProfile.eWallSubProfile.regionName;
	        	} else {
	        		$scope.filterRegion = 'all';
	        	}
	        	data = $filter('region')(data, $scope.filterRegion);

	        	for (var i = 0; i < data.length; i++) {
	        		usernames.push(data[i].username);
	        	}		
	        	callback(usernames);
	        });
		};

		function setSelectedUser() {
			$scope.sensingEnv.primaryUser = $scope.tempData.primaryUser;
		};

		$scope.sensingEnv = SensingEnvs.defaultSensingEnv();
		if ($stateParams.uuid) {
			$scope.editing = true;
			$scope.sensingEnv = SensingEnvs.get({
				uuid: $stateParams.uuid
			});
		} else if (!$stateParams.uuid) {
			$scope.editing = false;
			$scope.hideLoading();
		}

		$scope.addIndoorPlace = function() {
			ModalService.showModal({
				templateUrl: "partials/home-sensing-env-add-place-modal.html",
				controller: "SensingEnvAddPlaceModalController"
			}).then(function (modal) {
				modal.element.modal();
				modal.close.then(function (indoorPlace) {
					if (!indoorPlace || !indoorPlace.name || !indoorPlace.indoorPlaceArea) {
						return;
					}

					/* Name must be unique in list */
					var nameExists = false;
					angular.forEach($scope.sensingEnv.indoorPlaces, function(place, key) {
						if (!nameExists && place.name === indoorPlace.name) {
							nameExists = true;
						}
					});
					if (!nameExists) {
						$scope.sensingEnv.indoorPlaces.push(indoorPlace);
					}
				});
			});
		};

		$scope.deleteIndoorPlace = function(i) {
			var divName = $scope.sensingEnv.indoorPlaces[i].name;
			$scope.sensingEnv.indoorPlaces.splice(i, 1);
		};

		function setCurrentTimesOnSensEnv() {
			$scope.sensingEnv.pointOfContact.creationTime = new Date().getTime();
			$scope.sensingEnv.pointOfContact.lastModifiedTime = new Date().getTime();
		}

		function indoorPlacesNamesUnique() {
			var indoorPlacesList = $scope.sensingEnv.indoorPlaces;
			for (var i = 0; i < indoorPlacesList.length; i++) {
				for (var j = 0; j < indoorPlacesList.length; j++) {
					if (i === j) {
						continue;
					}
					if (indoorPlacesList[i].name === indoorPlacesList[j].name) {
						return false;
					}
				}
			}
			return true;
		}

		$scope.submit = function() {
			if (!indoorPlacesNamesUnique()) {
				locale.ready('sensingEnvs').then(function() {
					$scope.showError(locale.getString('sensingEnvs.errorAddingPlaceNotUnique'));
				});
				return;
			}
			setCurrentTimesOnSensEnv();
			setSelectedUser();
			SensingEnvs.submit($scope, $scope.sensingEnv, $stateParams.uuid);
		};

		$scope.$watch('sensingEnv.$resolved', function(newValue, oldValue) {
			if (newValue === true) {
				$scope.hideLoading();
				$scope.tempData.primaryUser = $scope.sensingEnv.primaryUser;
			}
		});

	}]);

ewallPortalSensingEnvs.factory('SensingEnvs', ['$resource', 'REST_API', 'SubmitManager', 'TokenHeaderGenerator',
	'DeleteManager', function($resource, REST_API, SubmitManager, TokenHeaderGenerator, DeleteManager) {
		var api = $resource(REST_API.sensingEnvs + ':uuid', {
			id: ''
		}, TokenHeaderGenerator.generateActions({}));

		api.submit = function(scope, object, objectNameToUpdate) {
			SubmitManager.submit(api, scope, object, 'uuid', objectNameToUpdate, 'home.sensingEnvs');
		};

		api.removeIt = function(scope) {
			DeleteManager.remove(scope, scope.sensingEnvs, api, 'uuid', scope.tableParams);
		};
		api.showDeleteModal = function(sensingEnv) {
			var sensEnvWithCascadeDeleteWarning = 'sensing environment ' + sensingEnv.uuid + ' with associated devices';
			DeleteManager.showDeleteModal(sensingEnv, sensingEnv.uuid, sensEnvWithCascadeDeleteWarning);
		};

		api.defaultSensingEnv = function() {
			return {
				"name": "",
				"registrationStatus": "NOT_REGISTERED",
					"eWallDevices" : [],
					"indoorPlaces" : [ {
						"indoorPlaceArea" : "KITCHEN",
						"name" : "kitchen"
					}, {
						"indoorPlaceArea" : "BEDROOM",
						"name" : "bedroom"
					}, {
						"indoorPlaceArea" : "BATHROOM",
						"name" : "bathroom"
					}, {
						"indoorPlaceArea" : "LIVING_ROOM",
						"name" : "livingroom"
					} ],
					"pointOfContact": {
					"proxyStatus": "OFFLINE"
				},
				"primaryUser": "",
				"regionName": null,
				"enabled": true
			};
		};

		return api;
	}]);


ewallPortalSensingEnvs.factory('SensingEnvConfig', [
	'$resource',
	'REST_API',
	'SubmitManager',
	'TokenHeaderGenerator',
	'DeleteManager',
	function($resource, REST_API, SubmitManager, TokenHeaderGenerator, DeleteManager) {

			var api = $resource(REST_API.sensingEnvConfig, {
			}, TokenHeaderGenerator.generateActions({}));

			api.submit = function(scope, object, objectNameToUpdate) {
				SubmitManager.submit(api, scope, object, 'sensEnvId',
					objectNameToUpdate, 'home.sensingEnvs');
			};

			return api;
		}]);


ewallPortalSensingEnvs.controller('SensingEnvConfigCtrl', [
	'$scope',
	'$http',
	'$state',	
	'$stateParams',
	'locale',
	'SensingEnvConfig',
	function($scope, $http, $state, $stateParams, locale, SensingEnvConfig) {

		$scope.showLoading();			

		$scope.sensEnvConfiguration = SensingEnvConfig.get({
			sensEnvId: $stateParams.uuid
		},
		function success() {
			$scope.hideLoading();
		},
		function error(res) {
			$scope.sensEnvBtnDisabled = true;

			if (res.status === 405) { // Method Not Allowed
				locale.ready('sensingEnvs').then(function() {
					$scope.hideLoading();
					$scope.showError(locale.getString('sensingEnvs.errorLongPollingDisabled'));}
					);
			} else {
				locale.ready('common').then(function() {
					$scope.hideLoading();
					$scope.showError(locale.getString('common.dataError'));}
					);
			}
		}
		);

		$scope.submit = function() {
			SensingEnvConfig.submit($scope, $scope.sensEnvConfiguration, $stateParams.uuid);
		}
	}]);

