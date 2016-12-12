'use strict';

var twitterAuthenticatorDirectives = angular.module('twitterAuthenticatorDirectives', []);

twitterAuthenticatorDirectives.directive('successModal', function () {
    return {
        restrict: 'E',
        templateUrl: 'partials/modals/success-modal.html'
    }
});

twitterAuthenticatorDirectives.directive('errorModal', function () {
    return {
        restrict: 'E',
        templateUrl: 'partials/modals/error-modal.html'
    }
});

twitterAuthenticatorDirectives.directive('loadingModal', function () {
    return {
        restrict: 'E',
        templateUrl: 'partials/modals/loading-modal.html'
    }
});