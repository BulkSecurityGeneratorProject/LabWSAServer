'use strict';

angular.module('thesisplatformApp').controller('PresencerequestDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Presencerequest', 'Presencestatus', 'Agent',
        function($scope, $stateParams, $modalInstance, entity, Presencerequest, Presencestatus, Agent) {

        $scope.presencerequest = entity;
        $scope.presencestatuss = Presencestatus.query();
        $scope.agents = Agent.query();
        $scope.load = function(id) {
            Presencerequest.get({id : id}, function(result) {
                $scope.presencerequest = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:presencerequestUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.presencerequest.id != null) {
                Presencerequest.update($scope.presencerequest, onSaveFinished);
            } else {
                Presencerequest.save($scope.presencerequest, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
