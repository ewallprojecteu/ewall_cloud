'use strict';

var fusionerYoutubeAvailableVideos = angular.module('fusionerYoutubeAvailableVideos', []);

fusionerYoutubeAvailableVideos.controller('AvailableVideoController', ['$scope', 'HealthImpairments', 'Videos',
    function ($scope, HealthImpairments, Videos) {
		HealthImpairments.getTagsPreviewPromise().then(function (tagsPreview) {
            $scope.healthImpairmentTags = tagsPreview.data;
        });

        $scope.updateVideosTable = function () {
            Videos.getByHealthImpairmentTagNamePromise($scope.healthImpairmentTag.name).then(function (videos) {
                $scope.videosByTag = videos.data;
            });
        }

        $scope.openVideo = function (videoLink) {
            window.open(videoLink);
        };
    }]);
