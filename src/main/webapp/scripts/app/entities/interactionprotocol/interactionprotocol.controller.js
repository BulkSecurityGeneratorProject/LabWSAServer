'use strict';

angular.module('thesisplatformApp')
    .controller('InteractionprotocolController', function ($scope, Interactionprotocol, ParseLinks) {
        $scope.interactionprotocols = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Interactionprotocol.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.interactionprotocols = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Interactionprotocol.get({id: id}, function(result) {
                $scope.interactionprotocol = result;
                $('#deleteInteractionprotocolConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Interactionprotocol.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteInteractionprotocolConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.interactionprotocol = {
                protocolName: null,
                protocolDescription: null,
                openingTime: null,
                id: null
            };
        };
    });
