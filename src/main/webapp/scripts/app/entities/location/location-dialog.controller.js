'use strict';

angular.module('thesisplatformApp').controller('LocationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Location', 'Agent',
        function($scope, $stateParams, $modalInstance, entity, Location, Agent) {

        $scope.location = entity;
        $scope.agents = Agent.query();
        $scope.load = function(id) {
            Location.get({id : id}, function(result) {
                $scope.location = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:locationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.location.id != null) {
                Location.update($scope.location, onSaveFinished);
            } else {
                Location.save($scope.location, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
