'use strict';

angular.module('thesisplatformApp')
    .controller('ReadoutDetailController', function ($scope, $rootScope, $stateParams, entity, Readout, Sensor) {
        $scope.readout = entity;
        $scope.load = function (id) {
            Readout.get({id: id}, function(result) {
                $scope.readout = result;
            });
        };
        $rootScope.$on('thesisplatformApp:readoutUpdate', function(event, result) {
            $scope.readout = result;
        });
    });
