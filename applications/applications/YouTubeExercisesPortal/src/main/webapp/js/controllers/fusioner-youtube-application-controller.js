'use strict';

var fusionerYoutubeApplication = angular.module('fusionerYoutubeApplication', ['language-picker']);

fusionerYoutubeApplication.controller('ApplicationController', ['$scope', '$http', 'locale', 'localeSupported', 'localStorageService', 'REST_API',
    function ($scope, $http, locale, localeSupported, localStorageService, REST_API) {
        $scope.successMessage = '';
        $scope.errorMessage = '';
        
        var hostname = window.location.hostname;
        var pathname = window.location.pathname;
        var protocol = window.location.protocol;
        fusionerYoutubeApplication.extractPreparingEnvironmentController($scope, hostname, pathname, protocol, REST_API);

        $scope.showSuccessMessage = function (message) {
            $scope.successMessage = message;
            $('#successModal').modal('show');
        }

        $scope.showErrorMessage = function (message) {
            $scope.errorMessage = message;
            $('#errorModal').modal('show');
        }

        $scope.showLoading = function() {
            $('#pleaseWaitDialog').modal('show');
        };
        $scope.hideLoading = function() {
            $('#pleaseWaitDialog').modal('hide');
        };

        function getCountry(locale) {
            var splitLocale = locale.split('-');
            if (splitLocale.length > 1) {
                return splitLocale[1].toLowerCase();
            }
            return locale;
        }

        function createLanguageObj(locale) {
            var language = languageMappingList[locale] || {
                nativeName: locale,
                englishName: locale
            };
            language.code = locale;
            language.country = language.country || getCountry(locale);
            return language;
        }

        $scope.supportedLangs = localeSupported;

        var localStorageName = 'language';
        var savedLanguage = localStorageService.get(localStorageName);
        var language = (savedLanguage || window.navigator.userLanguage || window.navigator.language).replace(
            /(\w+)[-_]?(\w*)/, function (match, p1, p2) {
                if (p2) return p1.toLowerCase() + '-' + p2.toUpperCase();
                return p1.toLowerCase();
            });
        locale.setLocale(language);
        $scope.currentlanguage = createLanguageObj(locale.getLocale());
        localStorageService.set(localStorageName, language);

        $scope.onLanguageChange = function (language) {
            locale.setLocale(language.code);
            $scope.currentlanguage = language;
            localStorageService.set(localStorageName, language.code);
        };
    }]);