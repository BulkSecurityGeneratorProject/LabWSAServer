'use strict';

angular.module('thesisplatformApp')
    .controller('LocationController', function ($scope, Location, ParseLinks) {
        $scope.locations = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Location.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.locations = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Location.get({id: id}, function(result) {
                $scope.location = result;
                $('#deleteLocationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Location.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLocationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.location = {
                xPosition: null,
                yPosition: null,
                time: null,
                id: null
            };
        };
    });
