'use strict';

var fusionerYoutubeHome = angular.module('fusionerYoutubeHome', []);

fusionerYoutubeHome.controller('HomeController', ['$scope', '$state', '$rootScope', '$interval', '$http', 'locale', 'LoginHelper', 'Authentication', 'UserHelper', 'REST_API',
                                                  function ($scope, $state, $rootScope, $interval, $http, locale, LoginHelper, Authentication, UserHelper, REST_API) {
    
    var REFRESH_TOKEN_INTERVAL = 1 * 30 * 1000; // 30 seconds

    LoginHelper.isTokenSet(function(set) {
        if (!set) {
            $state.go('login');
            return;
        }

        Authentication.doCredentialsExistForUsernamePromise($scope.username).then(function (doExistFlag) {
            $scope.isConnected = doExistFlag.data;
        });
    });

    var refreshingTokenPollTimer = $interval(function () {
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

	UserHelper.getStorageSelected(function (user) {
		if (!user) {
			$state.go('userSelection');
		}
		$scope.username = user.username;
		$scope.firstName = user.firstName;
	});
	
	$scope.changeUser = function () {
		UserHelper.clearStorageSelected();
		$state.go('userSelection');
	};

    $scope.logoutUser = function () {
        LoginHelper.setToken(null);
        LoginHelper.setUserRoleWithRegion(null);
        UserHelper.clearStorageSelected();
        $state.go('login');
    };
	
	$scope.connectToGoogle = function () {
        authorizeUser();
    };
    
    function authorizeUser() {
    	var authWindow = window.open(REST_API.OPEN_AUTH);
    	Authentication.getAuthUrlPromise().then(function (authUrl) {
            authorizeAndSaveCredentialsInWindow(authUrl.data, authWindow);
        });
    }
    
    $scope.disconnectFromGoogle = function () {
    	Authentication.deleteCredentialsAndRevokeAccessForUsernamePromise($scope.username)
            .success(function () {
    			$scope.isConnected = false;
                locale.ready('home').then(function() {
                    $scope.showSuccessMessage(locale.getString('home.successfullyRevokedAccess'));
                });
            })
            .error(function () {
        		locale.ready('home').then(function() {
                    $scope.showSuccessMessage(locale.getString('home.errorRevokingAccess'));
                });
        	});
    };
    
    function authorizeAndSaveCredentialsInWindow(authUrl, authWindow) {
    	authWindow.location = authUrl;

        var pollTimer = window.setInterval(function () {
            try {
                var url = authWindow.document.URL;
                if (url.indexOf(REST_API.REDIRECT_URI_GOOGLE) != -1) {
                    window.clearInterval(pollTimer);
                    var code = getUrlQueryParameter(url, 'code');

                    Authentication.postUsernameCodeForCredentialsPromise($scope.username, code).then(function (data) {
                    	authWindow.close();

                        var statusCode = data.status;
                        locale.ready('home').then(function() {
                            if (statusCode === 200) {
                                $scope.isConnected = true;
                                $scope.showSuccessMessage(locale.getString('home.successfullyAuthorized'));
                            } else if (statusCode === 409) {
                                $scope.showErrorMessage(locale.getString('home.errorAuthorizingExistAnother'));
                            } else {
                                $scope.showErrorMessage(locale.getString('home.errorAuthorizing'));
                            }
                        });
                    });
                }
            } catch (e) {
                // block frame warning
            }
        }, 500);
    }
}]);