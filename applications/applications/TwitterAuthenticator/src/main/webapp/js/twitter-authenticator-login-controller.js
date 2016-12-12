'use strict';

var twitterAuthenticatorLogin = angular.module('twitterAuthenticatorLogin', []);

twitterAuthenticatorLogin.controller('LoginController', ['$scope', '$state', '$http', 'localStorageService', 'REST_API', 'LoginHelper',
	function($scope, $state, $http, localStorageService, REST_API, LoginHelper) {

		LoginHelper.isTokenSet(function(set) {
			if (set) {
				$state.go('home');
			}
		});

		$scope.login = function(credentials) {
			$scope.showLoading();
			$http.post(REST_API.LOGIN_ADMIN, "username=" + credentials.username + "&password=" + credentials.password, {
				headers: {
					"Content-type": "application/x-www-form-urlencoded"
				}
			}).success(function(res) {
				LoginHelper.setToken(res.token);

				$http.get(REST_API.PROFILING_SERVER + '/users/' + credentials.username).success(function(user) {
					var userRoleWithRegion = {
						role: user.userRole,
						region: user.userProfile.eWallSubProfile.regionName
					};

					LoginHelper.setUserRoleWithRegion(userRoleWithRegion);
					$scope.hideLoading();
					$state.go('home');

				}).error(function(e) {
					LoginHelper.setToken(null);
					handleError(e);
				});

			}).error(function(e) {
				handleError(e);
			});
		};

		function handleError(e) {
			console.log(e);
			$scope.hideLoading();
			$scope.showErrorMessage('Log in failed');
		}
	}
]);