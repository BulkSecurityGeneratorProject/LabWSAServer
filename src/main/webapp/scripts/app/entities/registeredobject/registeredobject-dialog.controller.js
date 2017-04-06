'use strict';

angular.module('thesisplatformApp').controller('RegisteredobjectDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Registeredobject', 'Agent', 'Assessment',
        function($scope, $stateParams, $modalInstance, entity, Registeredobject, Agent, Assessment) {

        $scope.registeredobject = entity;
        $scope.agents = Agent.query();
        $scope.assessments = Assessment.query();
        $scope.load = function(id) {
            Registeredobject.get({id : id}, function(result) {
                $scope.registeredobject = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:registeredobjectUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.registeredobject.id != null) {
                Registeredobject.update($scope.registeredobject, onSaveFinished);
            } else {
                Registeredobject.save($scope.registeredobject, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
