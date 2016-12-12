'use strict';

var flickrPortalUserSelection = angular.module('flickrPortalUserSelection', []);

flickrPortalUserSelection.controller('UserSelectionController', ['$scope', '$rootScope', '$interval', '$http', '$state', 'locale', 'Users', 'UserHelper', 'CredentialsHelper', 'LoginHelper', 'RequestTokenWithUrl', 'Credentials', 'REST_API',
	function($scope, $rootScope, $interval, $http, $state, locale, Users, UserHelper, CredentialsHelper, LoginHelper, RequestTokenWithUrl, Credentials, REST_API) {

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
			Users.query({}, function success(data) {
				$scope.users = data;
			});
		}

		function getUsersByRegion(region) {
			Users.query({}, function success(data) {
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
			$interval.cancel(refreshingTokenPollTimer);
		});

		UserHelper.getStorageSelected(function(user) {
			if (user) {
				$state.go('home.availablePhotos');
			}
		});

		function refreshCredentialsStatus(username) {
			CredentialsHelper.doCredentialsExist(username).then(function(response) {
				$scope.credentialsExist = response.data;
			});
		}

		$scope.getCredentialsFor = function(username) {
			refreshCredentialsStatus(username);
		};

		$scope.setUser = function() {
			UserHelper.setStorageSelected($scope.user);
		};

		$scope.authorizeUser = function() {
			var authWindow = window.open(REST_API.OPEN_AUTH_FLICKR);
			RequestTokenWithUrl.get({}, function(flickrRequestTokenWithUrl) {
				authorizeAndSaveCredentialsInWindow(flickrRequestTokenWithUrl, authWindow);
			});
		};

		function authorizeAndSaveCredentialsInWindow(flickrRequestTokenWithUrl, authWindow) {
			authWindow.location = flickrRequestTokenWithUrl.authUrl;

			var pollTimer = window.setInterval(function() {
				try {
					var url = authWindow.document.URL;
					if (url.indexOf(REST_API.REDIRECT_URI_FLICKR) != -1) {
						window.clearInterval(pollTimer);
						var verifier = getUrlQueryParameter(url, 'oauth_verifier');
						var flickrRequestTokenWithVerifier = {
							'requestToken': flickrRequestTokenWithUrl.requestToken,
							'requestTokenSecret': flickrRequestTokenWithUrl.requestTokenSecret,
							'verifier': verifier
						};

						Credentials.save({
								username: $scope.user.username
							}, flickrRequestTokenWithVerifier,
							function success() {
								refreshCredentialsStatus($scope.user.username);
								locale.ready('userSelection').then(function() {
									$scope.showSuccessMessage(locale.getString('userSelection.successfullyAuthorized'));
								});
								authWindow.close();
							},
							function error(e) {
								console.log(e);
								locale.ready('userSelection').then(function() {
									$scope.showErrorMessage(locale.getString('userSelection.failedAuthorizing'));
								});
								authWindow.close();
							});
					}
				} catch (e) {
					console.log(e);
				}
			}, 500);
		}
	}
]);