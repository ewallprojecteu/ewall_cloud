'use strict';

var flickrPortalUploadPhoto = angular.module('flickrPortalUploadPhoto', []);

flickrPortalUploadPhoto.controller('UploadPhotoController', ['$scope', 'Photos', 'locale',
    function ($scope, Photos, locale) {
		$scope.submit = function() {
			$scope.showUploading();

			var splittedTags = $scope.photo.tags.split(',');
			var tagsFormatted = [];
			for (var i = 0; i < splittedTags.length; i++) {
				tagsFormatted.push(splittedTags[i].trim());
			}

			var flickrData = {
				title: $scope.photo.title,
				description: $scope.photo.description,
				content: $scope.photo.encoded,
				tags: tagsFormatted,
				privacyStatus: $scope.photo.privacy
			}; 

			Photos.save({username: $scope.username}, flickrData, 
				function success() {
					$scope.hideUploading();
					locale.ready('uploadPhoto').then(function() {
                        $scope.showSuccessMessage(locale.getString('uploadPhoto.successfullyUploaded'));
                	});
				},
				function error(e) {
					console.log(e);
					$scope.hideUploading();
					locale.ready('uploadPhoto').then(function() {
                        $scope.showErrorMessage(locale.getString('uploadPhoto.failedUploading'));
                	});
				});

			resetForm();
		};

		function resetForm() {
			$scope.photo.title = '';
			$scope.photo.description = '';
			$scope.photo.tags = '';
			$scope.photo.privacy = 'PRIVATE';
		}
    }]);
