'use strict';

var fusionerYoutubeHealthImpairmentTagsService = angular.module('fusionerYoutubeHealthImpairmentTagsService', []);

fusionerYoutubeHealthImpairmentTagsService.factory('HealthImpairments', ['$http', 'REST_API', function ($http, REST_API) {
    return {
    	getTagsPreviewPromise: function () {
            return $http.get(REST_API.FUSIONER_YOUTUBE + '/healthImpairments/tags/preview');
        },
        getAdditionalTagsPromise: function () {
            return $http.get(REST_API.FUSIONER_YOUTUBE + '/healthImpairments/tags/additional');
        },
        getReflectedMapPromise: function () {
            return $http.get(REST_API.FUSIONER_YOUTUBE + '/healthImpairments/reflectedMap');
        },
        addTagPromise: function (healthImpairmentTag) {
            return $http.post(REST_API.FUSIONER_YOUTUBE + '/healthImpairments/tags/additional', healthImpairmentTag);
        }
    };
}]);