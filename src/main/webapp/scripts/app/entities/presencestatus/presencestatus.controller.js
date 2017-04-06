'use strict';

angular.module('thesisplatformApp')
    .controller('PresencestatusController', function ($scope, Presencestatus, ParseLinks) {
        $scope.presencestatuss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Presencestatus.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.presencestatuss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Presencestatus.get({id: id}, function(result) {
                $scope.presencestatus = result;
                $('#deletePresencestatusConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Presencestatus.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePresencestatusConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.presencestatus = {
                presenceStatusName: null,
                id: null
            };
        };
    });
