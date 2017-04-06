'use strict';

angular.module('thesisplatformApp').controller('RequesttypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Requesttype', 'Communicativeaction',
        function($scope, $stateParams, $modalInstance, entity, Requesttype, Communicativeaction) {

        $scope.requesttype = entity;
        $scope.communicativeactions = Communicativeaction.query();
        $scope.load = function(id) {
            Requesttype.get({id : id}, function(result) {
                $scope.requesttype = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:requesttypeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.requesttype.id != null) {
                Requesttype.update($scope.requesttype, onSaveFinished);
            } else {
                Requesttype.save($scope.requesttype, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
