'use strict';

angular.module('thesisplatformApp')
    .controller('FeatureDetailController', function ($scope, $rootScope, $stateParams, entity, Feature, Agent, Assessment) {
        $scope.feature = entity;
        $scope.load = function (id) {
            Feature.get({id: id}, function(result) {
                $scope.feature = result;
            });
        };
        $rootScope.$on('thesisplatformApp:featureUpdate', function(event, result) {
            $scope.feature = result;
        });
    });
