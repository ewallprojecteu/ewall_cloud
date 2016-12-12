'use strict';

var fusionerYoutubeAddExternalVideo = angular.module('fusionerYoutubeAddExternalVideo', []);

fusionerYoutubeAddExternalVideo.controller('AddExternalVideoController', ['$scope', 'locale', 'HealthImpairments', 'Videos',
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

        $scope.addExternalVideoData = function () {
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
                link: $scope.videoLink,
                healthImpairmentReflectedMap: healthImpairmentReflectedMap,
                additionalHealthImpairmentTags: additionalHealthImpairmentTags
            };
            $scope.resetForm();
            Videos.addExternalPromise($scope.username, JSON.stringify(videoData))
                .success(function () {
                    locale.ready('addExternalVideo').then(function() {
                        $scope.showSuccessMessage(locale.getString('addExternalVideo.successfullyAdded'));
                    });
                })
                .error(function () {
                    locale.ready('addExternalVideo').then(function() {
                        $scope.showErrorMessage(locale.getString('addExternalVideo.failedAdding'));
                    });
                });
        };
        
        $scope.resetForm = function () {
            $scope.videoLink = '';
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
            $("#txtLink").focus();
        };
    }]);
