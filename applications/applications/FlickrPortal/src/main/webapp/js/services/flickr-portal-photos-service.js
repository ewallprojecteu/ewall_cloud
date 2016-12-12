'use strict';

var flickrPortalPhotosService = angular.module('flickrPortalPhotosService', []);

flickrPortalPhotosService.factory('Photos', ['$resource', 'REST_API', 
    function ($resource, REST_API) {

        return $resource(REST_API.PHOTOS, {});
    }]);