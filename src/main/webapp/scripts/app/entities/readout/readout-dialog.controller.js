'use strict';

angular.module('thesisplatformApp').controller('ReadoutDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Readout', 'Sensor',
        function($scope, $stateParams, $modalInstance, entity, Readout, Sensor) {

        $scope.readout = entity;
        $scope.sensors = Sensor.query();
        $scope.load = function(id) {
            Readout.get({id : id}, function(result) {
                $scope.readout = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:readoutUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.readout.id != null) {
                Readout.update($scope.readout, onSaveFinished);
            } else {
                Readout.save($scope.readout, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
