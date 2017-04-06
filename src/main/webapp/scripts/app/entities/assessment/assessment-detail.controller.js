'use strict';

angular.module('thesisplatformApp')
    .controller('AssessmentDetailController', function ($scope, $rootScope, $stateParams, entity, Assessment, Registeredobject, Feature, Agent) {
        $scope.assessment = entity;
        $scope.load = function (id) {
            Assessment.get({id: id}, function(result) {
                $scope.assessment = result;
            });
        };
        $rootScope.$on('thesisplatformApp:assessmentUpdate', function(event, result) {
            $scope.assessment = result;
        });
    });
