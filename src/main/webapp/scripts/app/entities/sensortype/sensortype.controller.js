'use strict';

angular.module('thesisplatformApp')
    .controller('SensortypeController', function ($scope, Sensortype, ParseLinks) {
        $scope.sensortypes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Sensortype.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.sensortypes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Sensortype.get({id: id}, function(result) {
                $scope.sensortype = result;
                $('#deleteSensortypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Sensortype.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSensortypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.sensortype = {
                sensorTypeName: null,
                id: null
            };
        };
    });
