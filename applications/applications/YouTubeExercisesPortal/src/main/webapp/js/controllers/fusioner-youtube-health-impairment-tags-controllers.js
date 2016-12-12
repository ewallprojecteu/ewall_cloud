'use strict';

var fusionerYoutubeHealthImpairmentTags = angular.module('fusionerYoutubeHealthImpairmentTags', []);

fusionerYoutubeHealthImpairmentTags.controller('AddHealthImpairmentTagController', ['$scope', 'locale', 'HealthImpairments', 
    function ($scope, locale, HealthImpairments) {
        $scope.refreshHealthImpairmentTags = function () {
           HealthImpairments.getAdditionalTagsPromise().then(function (additionalTags) {
            $scope.additionalHealthImpairmentTags = additionalTags.data;
        });
       };

       $scope.refreshHealthImpairmentTags();

       $scope.addHealthImpairmentTag = function () {
        var healthImpairmentTag = {
            name: $scope.tagName,
            description: $scope.tagDescription
        };
        $scope.resetForm();
        HealthImpairments.addTagPromise(healthImpairmentTag)
            .success(function () {
                locale.ready('addHealthImpairmentTag').then(function() {
                    $scope.refreshHealthImpairmentTags();
                    $scope.showSuccessMessage(locale.getString('addHealthImpairmentTag.successfullyAdded'));
                });
            })
            .error(function () {
                locale.ready('addHealthImpairmentTag').then(function() {
                    $scope.showErrorMessage(locale.getString('addHealthImpairmentTag.failedAdding'));
                });
            });
    };
    
    $scope.resetForm = function () {
        $scope.tagName = '';
        $scope.tagDescription = '';
        $("#txtName").focus();
    };
}]);