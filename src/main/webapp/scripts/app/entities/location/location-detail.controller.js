'use strict';

angular.module('thesisplatformApp')
    .controller('LocationDetailController', function ($scope, $rootScope, $stateParams, entity, Location, Agent) {
        $scope.location = entity;
        $scope.load = function (id) {
            Location.get({id: id}, function(result) {
                $scope.location = result;
            });
        };
        $rootScope.$on('thesisplatformApp:locationUpdate', function(event, result) {
            $scope.location = result;
        });
    });
