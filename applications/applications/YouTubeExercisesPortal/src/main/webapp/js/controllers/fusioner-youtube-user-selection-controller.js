'use strict';

var fusionerYoutubeUserSelection = angular.module('fusionerYoutubeUserSelection', []);

fusionerYoutubeUserSelection.controller('UserSelectionController', ['$scope', '$rootScope', '$http', '$interval', '$state', 'REST_API', 'LoginHelper', 'Users', 'UserHelper',
	function($scope, $rootScope, $http, $interval, $state, REST_API, LoginHelper, Users, UserHelper) {

		var REFRESH_TOKEN_INTERVAL = 1 * 30 * 1000; // 30 seconds

		LoginHelper.isTokenSet(function(set) {
			if (!set) {
				$state.go('login');
				return;
			}

			LoginHelper.getUserRoleWithRegion(function(userRoleWithRegion) {
				if (userRoleWithRegion.role === 'ADMINISTRATOR') {
					getAllUsers();
				} else if (userRoleWithRegion.role === 'REGION_ADMINISTRATOR') {
					getUsersByRegion(userRoleWithRegion.region);
				}
			});
		});

		function getAllUsers() {
			Users.getPromise().success(function(data) {
				$scope.users = data;
			});
		}

		function getUsersByRegion(region) {
			Users.getPromise().success(function(data) {
				var usersWithSelectedRegion = [];
				angular.forEach(data, function(user, key) {
					if (user.userProfile.eWallSubProfile.regionName === region) {
						usersWithSelectedRegion.push(user);
					}
				});
				$scope.users = usersWithSelectedRegion;
			});
		}

		var refreshingTokenPollTimer = $interval(function() {
			$http.get(REST_API.TOKEN_UPDATE).success(function(data) {
				LoginHelper.setToken(data.token);

			}).error(function(e) {
				console.log(e);
			});
		}, REFRESH_TOKEN_INTERVAL);

		$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
			if (toState.name.startsWith('home')) {
				return;
			}
			$interval.cancel(refreshingTokenPollTimer);
		});

		UserHelper.getStorageSelected(function(user) {
			if (user) {
				$state.go('home.availableVideos');
			}
		});

		$scope.setUser = function() {
			UserHelper.setStorageSelected($scope.user);
		}
	}
]);