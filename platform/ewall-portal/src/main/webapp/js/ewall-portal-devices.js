'use strict';

var ewallPortalDevices = angular.module('ewallPortalDevices', ['ui.router', 'ewallPortalHelpers',
	'ewallPortalConstants', 'ngResource', 'ewallPortalAuthentication', 'acute.select']);

ewallPortalDevices.filter('region', ['$filter', function($filter) {
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

ewallPortalDevices.controller('DevicesCtrl', ['$scope', '$filter', 'Devices', 'Users', 'SensingEnvs',
	function($scope, $filter, Devices, Users, SensingEnvs) {

		$scope.tempData = {
	        primaryUser: null
	    };

		$scope.getAssociatedWithSensEnvPrimaryUsers = function(callback) {
			Users.getTargetUsers({ associatedWithSensEnvFilter: 'ONLY_ASSOCIATED_USERS' }, function(data) {
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

		$scope.showDevicesAndHandleDevicesSetBtn = function() {
			var found = false;

			SensingEnvs.query({}, function(data) {
				for (var i = 0; i < data.length; i++) {
					if (data[i].primaryUser === $scope.tempData.primaryUser) {
						$scope.sensEnvUuid = data[i].uuid;
						found = true;
						break;
					}
				}
				if (!found) {
					$scope.sensEnvUuid = null;
					$scope.devices = [];
					return;
				}
				prepareDevicesTableAndHandleDevicesSetBtn();
			});
		};

		function prepareDevicesTableAndHandleDevicesSetBtn() {
			Devices.query({sensEnvId: $scope.sensEnvUuid, deviceId: null}, function(data) {
				$scope.devices = data;
				if ($scope.devices) {
					filterDevices();
				}
				if ($scope.devices == null || $scope.devices.length === 0) {
					$scope.disableDevicesSetBtn = false;
				} else {
					$scope.disableDevicesSetBtn = true;
				}
			});	
		}

		$scope.$watch('currentPage + numPerPage', function() {
			if ($scope.devices) {
				filterDevices();
			}
		});

		/* Necessary because of async delete operation */
		$scope.$watch('devices.length', function(newValue, oldValue) {
			if (newValue < oldValue) {
				filterDevices();
			}
		});

		function filterDevices() {
			var begin = (($scope.currentPage - 1) * $scope.numPerPage), 
				end = begin + $scope.numPerPage;
			$scope.filteredDevices = $scope.devices.slice(begin, end);
		}

		$scope.remove = function() {
			Devices.removeIt($scope);
		};
		$scope.showDeleteModal = function(device) {
			Devices.showDeleteModal(device);
		};

		$scope.showDevicesSetConfirmModal = function(sensEnvUuid) {
			Devices.showDevicesSetConfirmModal(sensEnvUuid);
		};
		$scope.postDefaultDevicesSet = function() {
			Devices.postDefaultDevicesSet($scope, function() {
				prepareDevicesTableAndHandleDevicesSetBtn();
			});
		};
	}]);

ewallPortalDevices.controller('DeviceEditCtrl', [
	'$scope',
	'$state',
	'$stateParams',
	'$q',
	'$filter',
	'locale',
	'Devices',
	'Users',
	'SensingEnvs',
	function($scope, $state, $stateParams, $q, $filter, locale, Devices, Users, SensingEnvs) {
		$scope.showLoading();

		function prepareDeviceAndAssociatedUser() {
			$scope.device = Devices.get({
				sensEnvId: $stateParams.sensEnvId,
				deviceId: $stateParams.deviceId
			});
			SensingEnvs.query({}, function (data) {
				for (var i = 0; i < data.length; i++) {
					if (data[i].uuid === $stateParams.sensEnvId) {
						$scope.tempData.primaryUser = data[i].primaryUser;
					}
				}
			});
		}

		$scope.device = Devices.defaultDevice();
		if ($stateParams.sensEnvId && $stateParams.deviceId) {
			prepareDeviceAndAssociatedUser();
		} else {
			$scope.hideLoading();
		}

		$scope.tempData = {
	        primaryUser: null
	    };

		$scope.getAssociatedWithSensEnvPrimaryUsers = function(callback) {
			Users.getTargetUsers({ associatedWithSensEnvFilter: 'ONLY_ASSOCIATED_USERS' }, function(data) {
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

		function promiseSensEnvUuid() {
			var defer = $q.defer();

			SensingEnvs.query({}, function (data) {
				for (var i = 0; i < data.length; i++) {
					if (data[i].primaryUser === $scope.tempData.primaryUser) {
						defer.resolve(data[i].uuid);
					}
				}
			});

			return defer.promise;
		}

		$scope.submit = function() {
			promiseSensEnvUuid().then(function(sensEnvUuid) {
				$scope.device.sensingEnvironmentUUID = sensEnvUuid;

				// If query params exists, device is editing; else device is inserting
				if ($stateParams.sensEnvId && $stateParams.deviceId) {
					Devices.put($scope, $scope.device);
				} else {
					Devices.post($scope, $scope.device);
				}
			});
		};

		$scope.$watch('device.$resolved', function(newValue, oldValue) {
			if (newValue === true) {
				$scope.hideLoading();
			}
		});
	}]);

ewallPortalDevices.factory('Devices', ['$resource', 'REST_API', 'SubmitManager', 'TokenHeaderGenerator',
	'DeleteManager', function($resource, REST_API, SubmitManager, TokenHeaderGenerator, DeleteManager) {
		var api = $resource(REST_API.devices, {}, TokenHeaderGenerator.generateActions({}));
		var apiSet = $resource(REST_API.devicesSet, {}, TokenHeaderGenerator.generateActions({}));

		api.put = function(scope, object) {
			SubmitManager.putDevice(api, scope, object, 'home.devices');
		};

		api.post = function(scope, object) {
			SubmitManager.postDevice(api, scope, object, 'home.devices');
		};

		api.removeIt = function(scope) {
			DeleteManager.removeDevice(scope, scope.devices, api);
		};
		api.showDeleteModal = function(device) {
			var deviceDeleteWarning = 'device ' + device.uuid;
			DeleteManager.showDeleteModal(device, device.uuid, deviceDeleteWarning);
		};

		api.showDevicesSetConfirmModal = function(sensEnvUuid) {
			SubmitManager.showDevicesSetConfirmModal(sensEnvUuid);
		};
		api.postDefaultDevicesSet = function(scope, callback) {
			SubmitManager.postDefaultDevicesSet(scope, apiSet, function() {
				callback();
			});
		};

		api.defaultDevice = function() {
			return {
				"name": "",
				"manufacturer": {
					"name": "",
					"description": ""
				},
				"type": "",
				"indoorPlaceName": "",
				"sensingEnvironmentUUID": "",
				"actuator": false,
				"sensor": false,
				"wearable": false
			};
		};

		return api;
	}]);
