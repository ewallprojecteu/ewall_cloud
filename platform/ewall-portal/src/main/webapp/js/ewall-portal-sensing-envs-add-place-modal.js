var ewallSensEnvAddPlaceModal = angular.module('ewallSensEnvAddPlaceModal', []);

ewallSensEnvAddPlaceModal.controller('SensingEnvAddPlaceModalController', [
    '$scope', '$element', 'close',
    function($scope, $element, close) {

        $scope.addIndoorPlace = function () {
            $element.modal('hide');
            close($scope.indoorPlace, 500);
        };
    }]);