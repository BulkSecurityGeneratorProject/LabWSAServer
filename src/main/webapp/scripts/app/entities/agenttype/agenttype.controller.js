'use strict';

angular.module('thesisplatformApp')
    .controller('AgenttypeController', function ($scope, Agenttype, ParseLinks) {
        $scope.agenttypes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Agenttype.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.agenttypes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Agenttype.get({id: id}, function(result) {
                $scope.agenttype = result;
                $('#deleteAgenttypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Agenttype.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAgenttypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.agenttype = {
                agentTypeName: null,
                id: null
            };
        };
    });
