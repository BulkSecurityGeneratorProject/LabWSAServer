'use strict';

angular.module('thesisplatformApp')
    .controller('RequesttypeController', function ($scope, Requesttype, ParseLinks) {
        $scope.requesttypes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Requesttype.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.requesttypes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Requesttype.get({id: id}, function(result) {
                $scope.requesttype = result;
                $('#deleteRequesttypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Requesttype.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteRequesttypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.requesttype = {
                requestTypeName: null,
                id: null
            };
        };
    });
