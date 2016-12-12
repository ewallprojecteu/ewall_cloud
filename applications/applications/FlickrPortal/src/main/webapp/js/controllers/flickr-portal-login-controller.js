'use strict';

var flickrPortalLogin = angular.module('flickrPortalLogin', []);

flickrPortalLogin.controller('LoginController', ['$scope', '$state', '$http', 'localStorageService', 'REST_API', 'LoginHelper', 'locale',
	function($scope, $state, $http, localStorageService, REST_API, LoginHelper, locale) {

		LoginHelper.isTokenSet(function(set) {
			if (set) {
				$state.go('userSelection');
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
				
				$http.get(REST_API.USERS + credentials.username).success(function(user) {
					var userRoleWithRegion = {
						role: user.userRole,
						region: user.userProfile.eWallSubProfile.regionName
					};

					LoginHelper.setUserRoleWithRegion(userRoleWithRegion);
					$scope.hideLoading();
					$state.go('userSelection');

				}).error(function(e) {
					LoginHelper.setToken(null);
					handleError(e, locale);
				});

			}).error(function(e) {
				handleError(e, locale);
			});
		};

		function handleError(e, locale) {
			console.log(e);
			$scope.hideLoading();
			locale.ready('common').then(function() {
				$scope.showErrorMessage(locale.getString('common.loginFailed'));
			});
		}
	}
]);