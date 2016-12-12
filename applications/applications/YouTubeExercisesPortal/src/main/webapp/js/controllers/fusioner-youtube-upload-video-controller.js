'use strict';

var fusionerYoutubeUploadVideo = angular.module('fusionerYoutubeUploadVideo', []);

fusionerYoutubeUploadVideo.controller('UploadVideoController', ['$scope', 'locale', 'HealthImpairments', 'Videos',
    function ($scope, locale, HealthImpairments, Videos) {
		$scope.healthImpairmentIntensities = [];
	    $scope.healthImpairmentFlags = [];
	    $scope.additionalHealthImpairmentTags = [];
		$scope.healthImpairmentIntensityStates = [];
		$scope.healthImpairmentFlagStates = [];
		$scope.additionalHealthImpairmentTagsAvailableStates = ['False', 'True'];
		$scope.additionalHealthImpairmentTagsStates = [];
		
		$scope.resetHealthImpairmantsTables = function () {
        	for (var i = 0; i < $scope.healthImpairmentIntensities.length; i++) {
	        	var propertiesList = $scope.healthImpairmentIntensities[i].classPropertiesList;
	        	if (propertiesList != null && propertiesList.length > 0) {
	        		$scope.healthImpairmentIntensityStates[i] = propertiesList[0];
	        	}
	        }
	        for (var i = 0; i < $scope.healthImpairmentFlags.length; i++) {
	        	var propertiesList = $scope.healthImpairmentFlags[i].classPropertiesList;
	        	if (propertiesList != null && propertiesList.length > 0) {
	        		$scope.healthImpairmentFlagStates[i] = propertiesList[0];
	        	}
	        }
        };
        $scope.resetAdditionalHealthImpairmantsTagsTables = function () {
        	for (var i = 0; i < $scope.additionalHealthImpairmentTags.length; i++) {
	        	$scope.additionalHealthImpairmentTagsStates[i] = $scope.additionalHealthImpairmentTagsAvailableStates[0];
	        }
        };
	
		HealthImpairments.getReflectedMapPromise().then(function (reflectedMap) {
            $scope.healthImpairmentIntensities = reflectedMap.data['Intensity'];
            $scope.healthImpairmentFlags = reflectedMap.data['boolean'];
            $scope.resetHealthImpairmantsTables();
        });
        HealthImpairments.getAdditionalTagsPromise().then(function (additionalTags) {
            $scope.additionalHealthImpairmentTags = additionalTags.data;
            $scope.resetAdditionalHealthImpairmantsTagsTables();
        });

        $scope.uploadVideoData = function () {
        	var healthImpairmentReflectedMap = {};
        	var additionalHealthImpairmentTags = [];
        	
        	for (var i = 0; i < $scope.healthImpairmentIntensities.length; i++) {
	        	var item = $scope.healthImpairmentIntensities[i];
	        	var itemState = $scope.healthImpairmentIntensityStates[i];
	        	item.value = itemState;
	        }
        	for (var i = 0; i < $scope.healthImpairmentFlags.length; i++) {
	        	var item = $scope.healthImpairmentFlags[i];
	        	var itemState = $scope.healthImpairmentFlagStates[i];
	        	item.value = itemState;
	        }
        	for (var i = 0; i < $scope.additionalHealthImpairmentTags.length; i++) {
	        	var item = $scope.additionalHealthImpairmentTags[i];
	        	var itemState = $scope.additionalHealthImpairmentTagsStates[i];
	        	item.suffers = (itemState === 'True');
	        	additionalHealthImpairmentTags.push(item);
	        }
        	healthImpairmentReflectedMap['Intensity'] = $scope.healthImpairmentIntensities;
        	healthImpairmentReflectedMap['boolean'] = $scope.healthImpairmentFlags;
            var videoData = {
                content: $scope.video.encoded,
                title: $scope.videoTitle,
                description: $scope.videoDescription,
                privacy: $scope.videoPrivacy,
                healthImpairmentReflectedMap: healthImpairmentReflectedMap,
                additionalHealthImpairmentTags: additionalHealthImpairmentTags
            };
            $scope.resetForm();
            Videos.uploadDataPromise($scope.username, JSON.stringify(videoData))
                .success(function () {
                    locale.ready('uploadVideo').then(function() {
                        $scope.showSuccessMessage(locale.getString('uploadVideo.successfullyUploaded'));
                    });
                })
                .error(function () {
                    locale.ready('uploadVideo').then(function() {
                        $scope.showErrorMessage(locale.getString('uploadVideo.failedUploading'));
                    });
                });
        };

        $scope.resetForm = function () {
            $scope.videoTitle = '';
            $scope.videoDescription = '';
            $scope.videoPrivacy = 'Public';
            $scope.resetHealthImpairmantsTables();
            $scope.resetAdditionalHealthImpairmantsTagsTables();
            if ($("#collapsedPartIntensity").is(":visible")) {
            	$("#collapsedPartIntensity").collapse('hide');
            }
            if ($("#collapsedPartFlag").is(":visible")) {
            	$("#collapsedPartFlag").collapse('hide');
            }
            if ($("#collapsedPartAdditional").is(":visible")) {
            	$("#collapsedPartAdditional").collapse('hide');
            }
            $("#txtTitle").focus();
        };
    }]);
