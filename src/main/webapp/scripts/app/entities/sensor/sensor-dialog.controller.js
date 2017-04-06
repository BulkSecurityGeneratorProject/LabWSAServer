'use strict';

angular.module('thesisplatformApp').controller('SensorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Sensor', 'Agent', 'Readout', 'Unittype', 'Sensortype',
        function($scope, $stateParams, $modalInstance, entity, Sensor, Agent, Readout, Unittype, Sensortype) {

        $scope.sensor = entity;
        $scope.agents = Agent.query();
        $scope.readouts = Readout.query();
        $scope.unittypes = Unittype.query();
        $scope.sensortypes = Sensortype.query();
        $scope.load = function(id) {
            Sensor.get({id : id}, function(result) {
                $scope.sensor = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:sensorUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.sensor.id != null) {
                Sensor.update($scope.sensor, onSaveFinished);
            } else {
                Sensor.save($scope.sensor, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
