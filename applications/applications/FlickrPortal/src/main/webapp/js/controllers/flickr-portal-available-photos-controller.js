'use strict';

var flickrPortalAvailablePhotos = angular.module('flickrPortalAvailablePhotos', []);

flickrPortalAvailablePhotos.controller('AvailablePhotoController', ['$scope', 'Photos',
    function ($scope, Photos) {
		
		$scope.searchPhotos = function() {
			$scope.showLoading();

			if ($scope.photo.tags) {
				var splittedTags = $scope.photo.tags.split(',');
				var tagsConcatenated = '';
				for (var i = 0; i < splittedTags.length - 1; i++) {
					tagsConcatenated += splittedTags[i].trim() + '+';
				}
				var finalEl = splittedTags[splittedTags.length - 1];
				if (finalEl) {
					tagsConcatenated += finalEl.trim();
				}
			}

			Photos.query({username: $scope.username, tags: tagsConcatenated, privacy_status: $scope.photo.privacy, size: $scope.photo.size}, 
				function success(data) {
					$scope.photos = data;
					$scope.hideLoading();
				},
				function error(e) {
					console.log(e);
					$scope.hideLoading();
				});
		};
    }]);
