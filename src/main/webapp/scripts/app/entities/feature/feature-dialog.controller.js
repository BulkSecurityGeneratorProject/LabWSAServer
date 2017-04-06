'use strict';

angular.module('thesisplatformApp').controller('FeatureDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Feature', 'Agent', 'Assessment',
        function($scope, $stateParams, $modalInstance, entity, Feature, Agent, Assessment) {

        $scope.feature = entity;
        $scope.agents = Agent.query();
        $scope.assessments = Assessment.query();
        $scope.load = function(id) {
            Feature.get({id : id}, function(result) {
                $scope.feature = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thesisplatformApp:featureUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.feature.id != null) {
                Feature.update($scope.feature, onSaveFinished);
            } else {
                Feature.save($scope.feature, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
