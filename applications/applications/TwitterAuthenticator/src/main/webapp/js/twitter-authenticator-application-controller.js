'use strict';

var twitterAuthenticatorApplication = angular.module('twitterAuthenticatorApplication', []);

twitterAuthenticatorApplication.controller('ApplicationController', 
		['$scope', '$http', 'Users', 'Credentials', 'REST_API',  
		 function ($scope, $http, Users, Credentials, REST_API) {

	$scope.successMessage = '';
    $scope.errorMessage = '';
    
    $scope.showSuccessMessage = function (message) {
        $scope.successMessage = message;
        $('#successModal').modal('show');
    };

    $scope.showErrorMessage = function (message) {
        $scope.errorMessage = message;
        $('#errorModal').modal('show');
    };

    $scope.showLoading = function() {
        $('#pleaseWaitDialog').modal('show');
    };
    $scope.hideLoading = function() {
        $('#pleaseWaitDialog').modal('hide');
    };

    var env = "";
    var hostname = window.location.hostname;
    var pathname = window.location.pathname;
    var protocol = window.location.protocol;

    if (pathname.match(/-dev/g)) {
        env = "dev"
    } else if (pathname.match(/-int-level-1/g)) {
        env = "int-level-1"
    } else if (pathname.match(/-int-level-2/g)) {
        env = "int-level-2"
    } else if (pathname.match(/-prod-aws/g)) {
        env = "prod-aws"
    } else if (pathname.match(/-prod/g)) {
        env = "prod"
    }

    REST_API.LOGIN_ADMIN = REST_API.LOGIN_ADMIN.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.TOKEN_UPDATE = REST_API.TOKEN_UPDATE.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.PROFILING_SERVER = REST_API.PROFILING_SERVER.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.FUSIONER_TWITTER = REST_API.FUSIONER_TWITTER.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.REDIRECT_URI_TWITTER = REST_API.REDIRECT_URI_TWITTER.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.OPEN_AUTH = REST_API.OPEN_AUTH.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
}]);