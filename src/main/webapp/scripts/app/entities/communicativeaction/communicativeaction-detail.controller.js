'use strict';

angular.module('thesisplatformApp')
    .controller('CommunicativeactionDetailController', function ($scope, $rootScope, $stateParams, entity, Communicativeaction, Interactionprotocol, Requesttype, Agent) {
        $scope.communicativeaction = entity;
        $scope.load = function (id) {
            Communicativeaction.get({id: id}, function(result) {
                $scope.communicativeaction = result;
            });
        };
        $rootScope.$on('thesisplatformApp:communicativeactionUpdate', function(event, result) {
            $scope.communicativeaction = result;
        });
    });
