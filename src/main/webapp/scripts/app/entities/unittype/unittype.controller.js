'use strict';

angular.module('thesisplatformApp')
    .controller('UnittypeController', function ($scope, Unittype, ParseLinks) {
        $scope.unittypes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Unittype.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.unittypes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Unittype.get({id: id}, function(result) {
                $scope.unittype = result;
                $('#deleteUnittypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Unittype.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUnittypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.unittype = {
                unitTypeName: null,
                id: null
            };
        };
    });
