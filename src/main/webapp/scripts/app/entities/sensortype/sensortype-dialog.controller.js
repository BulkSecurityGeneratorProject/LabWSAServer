'use strict';

angular.module('thesisplatformApp').controller('SensortypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Sensortype', 'Sensor',
        function($scope, $stateParams, $modalInstance, entity, Sensortype, Sensor) {

        $scope.sensortype = entity;
        $scope.sensors = Sensor.query();
        $scope.load = function(id) {
            Sensortype.get({id : id}, function(result) {
                $scope.sensortype = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:sensortypeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.sensortype.id != null) {
                Sensortype.update($scope.sensortype, onSaveFinished);
            } else {
                Sensortype.save($scope.sensortype, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
