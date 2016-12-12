'use strict';

var fusionerYoutubeDirectives = angular.module('fusionerYoutubeDirectives', []);

fusionerYoutubeDirectives.directive('successModal', function () {
    return {
        restrict: 'E',
        templateUrl: 'partials/modals/success-modal.html'
    }
});

fusionerYoutubeDirectives.directive('errorModal', function () {
    return {
        restrict: 'E',
        templateUrl: 'partials/modals/error-modal.html'
    }
});

fusionerYoutubeDirectives.directive('loadingModal', function () {
    return {
        restrict: 'E',
        templateUrl: 'partials/modals/loading-modal.html'
    }
});

fusionerYoutubeDirectives.directive("fileread", [function () {
    return {
        scope: {
            fileread: "="
        },
        link: function (scope, element, attributes) {
            element.bind("change", function (changeEvent) {
                var reader = new FileReader();
                reader.onload = function (loadEvent) {
                    scope.$apply(function () {
                        scope.fileread = loadEvent.target.result.match(/,(.*)$/)[1];
                    });
                }
                reader.readAsDataURL(changeEvent.target.files[0]);
            });
        }
    }
}]);