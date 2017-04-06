'use strict';

angular.module('thesisplatformApp').controller('AssessmentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Assessment', 'Registeredobject', 'Feature', 'Agent',
        function($scope, $stateParams, $modalInstance, entity, Assessment, Registeredobject, Feature, Agent) {

        $scope.assessment = entity;
        $scope.registeredobjects = Registeredobject.query();
        $scope.features = Feature.query();
        $scope.agents = Agent.query();
        $scope.load = function(id) {
            Assessment.get({id : id}, function(result) {
                $scope.assessment = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:assessmentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.assessment.id != null) {
                Assessment.update($scope.assessment, onSaveFinished);
            } else {
                Assessment.save($scope.assessment, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
