'use strict';

angular.module('thesisplatformApp')
    .controller('SensorDetailController', function ($scope, $rootScope, $stateParams, entity, Sensor, Agent, Readout, Unittype, Sensortype) {
        $scope.sensor = entity;
        $scope.load = function (id) {
            Sensor.get({id: id}, function(result) {
                $scope.sensor = result;
            });
        };
        $rootScope.$on('thesisplatformApp:sensorUpdate', function(event, result) {
            $scope.sensor = result;
        });
    });
