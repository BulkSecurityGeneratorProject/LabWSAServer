'use strict';

angular.module('thesisplatformApp').controller('AgenttypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Agenttype', 'Agent',
        function($scope, $stateParams, $modalInstance, entity, Agenttype, Agent) {

        $scope.agenttype = entity;
        $scope.agents = Agent.query();
        $scope.load = function(id) {
            Agenttype.get({id : id}, function(result) {
                $scope.agenttype = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:agenttypeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.agenttype.id != null) {
                Agenttype.update($scope.agenttype, onSaveFinished);
            } else {
                Agenttype.save($scope.agenttype, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
