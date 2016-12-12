'use strict';

var flickrPortalHome = angular.module('flickrPortalHome', []);

flickrPortalHome.controller('HomeController', ['$http', '$scope', '$interval', '$rootScope', '$state', 'REST_API', 'localStorageService', 'UserHelper', 'LoginHelper',
                                                  function ($http, $scope, $interval, $rootScope, $state, REST_API, localStorageService, UserHelper, LoginHelper) {
    
	var REFRESH_TOKEN_INTERVAL = 1 * 30 * 1000; // 30 seconds

	LoginHelper.isTokenSet(function(set) {
		if (!set) {
			$state.go('login');
		}
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
			return;
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
}]);