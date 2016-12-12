'use strict';

var fusionerYoutubeVideosService = angular.module('fusionerYoutubeVideosService', []);

fusionerYoutubeVideosService.factory('Videos', ['$http', 'REST_API', function ($http, REST_API) {
    return {
        getByHealthImpairmentTagNamePromise: function (tagName) {
            return $http.get(REST_API.FUSIONER_YOUTUBE + '/videos/' + tagName);
        },
        uploadDataPromise: function (username, videoData) {
            return $http.post(REST_API.FUSIONER_YOUTUBE + '/videos/' + username, videoData);
        },
        addExternalPromise: function (username, videoData) {
            return $http.post(REST_API.FUSIONER_YOUTUBE + '/videos/' + username + '/external', videoData);
        }
    };
}]);