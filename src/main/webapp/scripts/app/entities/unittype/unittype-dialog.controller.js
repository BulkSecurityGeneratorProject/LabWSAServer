'use strict';

angular.module('thesisplatformApp').controller('UnittypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Unittype', 'Sensor',
        function($scope, $stateParams, $modalInstance, entity, Unittype, Sensor) {

        $scope.unittype = entity;
        $scope.sensors = Sensor.query();
        $scope.load = function(id) {
            Unittype.get({id : id}, function(result) {
                $scope.unittype = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:unittypeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.unittype.id != null) {
                Unittype.update($scope.unittype, onSaveFinished);
            } else {
                Unittype.save($scope.unittype, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
