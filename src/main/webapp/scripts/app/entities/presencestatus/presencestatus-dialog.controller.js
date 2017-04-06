'use strict';

angular.module('thesisplatformApp').controller('PresencestatusDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Presencestatus', 'Presencerequest',
        function($scope, $stateParams, $modalInstance, entity, Presencestatus, Presencerequest) {

        $scope.presencestatus = entity;
        $scope.presencerequests = Presencerequest.query();
        $scope.load = function(id) {
            Presencestatus.get({id : id}, function(result) {
                $scope.presencestatus = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:presencestatusUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.presencestatus.id != null) {
                Presencestatus.update($scope.presencestatus, onSaveFinished);
            } else {
                Presencestatus.save($scope.presencestatus, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
