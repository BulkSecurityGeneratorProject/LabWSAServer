'use strict';

angular.module('thesisplatformApp')
    .controller('SensorController', function ($scope, Sensor, ParseLinks) {
        $scope.sensors = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Sensor.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.sensors = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Sensor.get({id: id}, function(result) {
                $scope.sensor = result;
                $('#deleteSensorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Sensor.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSensorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.sensor = {
                sensorName: null,
                sensorAccuracy: null,
                id: null
            };
        };
    });
