'use strict';

angular.module('thesisplatformApp')
    .controller('RegisteredobjectController', function ($scope, Registeredobject, ParseLinks) {
        $scope.registeredobjects = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Registeredobject.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.registeredobjects = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Registeredobject.get({id: id}, function(result) {
                $scope.registeredobject = result;
                $('#deleteRegisteredobjectConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Registeredobject.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteRegisteredobjectConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.registeredobject = {
                objectName: null,
                objectDescription: null,
                registrationTime: null,
                id: null
            };
        };
    });
