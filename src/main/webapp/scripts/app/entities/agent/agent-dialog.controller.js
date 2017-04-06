'use strict';

angular.module('thesisplatformApp').controller('AgentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Agent', 'Sensor', 'Location', 'Communicativeaction', 'Agenttype',
        function($scope, $stateParams, $modalInstance, entity, Agent, Sensor, Location, Communicativeaction, Agenttype) {

        $scope.agent = entity;
        $scope.sensors = Sensor.query();
        $scope.locations = Location.query();
        $scope.communicativeactions = Communicativeaction.query();
        $scope.agenttypes = Agenttype.query();
        $scope.load = function(id) {
            Agent.get({id : id}, function(result) {
                $scope.agent = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:agentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.agent.id != null) {
                Agent.update($scope.agent, onSaveFinished);
            } else {
                Agent.save($scope.agent, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
