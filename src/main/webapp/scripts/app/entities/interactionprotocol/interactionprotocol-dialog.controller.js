'use strict';

angular.module('thesisplatformApp').controller('InteractionprotocolDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Interactionprotocol', 'Communicativeaction',
        function($scope, $stateParams, $modalInstance, entity, Interactionprotocol, Communicativeaction) {

        $scope.interactionprotocol = entity;
        $scope.communicativeactions = Communicativeaction.query();
        $scope.load = function(id) {
            Interactionprotocol.get({id : id}, function(result) {
                $scope.interactionprotocol = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:interactionprotocolUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.interactionprotocol.id != null) {
                Interactionprotocol.update($scope.interactionprotocol, onSaveFinished);
            } else {
                Interactionprotocol.save($scope.interactionprotocol, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
