'use strict';

angular.module('thesisplatformApp').controller('CommunicativeactionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Communicativeaction', 'Interactionprotocol', 'Requesttype', 'Agent',
        function($scope, $stateParams, $modalInstance, entity, Communicativeaction, Interactionprotocol, Requesttype, Agent) {

        $scope.communicativeaction = entity;
        $scope.interactionprotocols = Interactionprotocol.query();
        $scope.requesttypes = Requesttype.query();
        $scope.agents = Agent.query();
        $scope.load = function(id) {
            Communicativeaction.get({id : id}, function(result) {
                $scope.communicativeaction = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:communicativeactionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.communicativeaction.id != null) {
                Communicativeaction.update($scope.communicativeaction, onSaveFinished);
            } else {
                Communicativeaction.save($scope.communicativeaction, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
