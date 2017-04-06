'use strict';

angular.module('thesisplatformApp')
    .controller('SensortypeDetailController', function ($scope, $rootScope, $stateParams, entity, Sensortype, Sensor) {
        $scope.sensortype = entity;
        $scope.load = function (id) {
            Sensortype.get({id: id}, function(result) {
                $scope.sensortype = result;
            });
        };
        $rootScope.$on('thesisplatformApp:sensortypeUpdate', function(event, result) {
            $scope.sensortype = result;
        });
    });
