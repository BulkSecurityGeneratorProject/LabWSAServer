'use strict';

angular.module('thesisplatformApp')
    .controller('InteractionprotocolDetailController', function ($scope, $rootScope, $stateParams, entity, Interactionprotocol, Communicativeaction) {
        $scope.interactionprotocol = entity;
        $scope.load = function (id) {
            Interactionprotocol.get({id: id}, function(result) {
                $scope.interactionprotocol = result;
            });
        };
        $rootScope.$on('thesisplatformApp:interactionprotocolUpdate', function(event, result) {
            $scope.interactionprotocol = result;
        });
    });
