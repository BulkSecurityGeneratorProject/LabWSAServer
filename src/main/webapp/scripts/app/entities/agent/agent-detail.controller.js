'use strict';

angular.module('thesisplatformApp')
    .controller('AgentDetailController', function ($scope, $rootScope, $stateParams, entity, Agent, Sensor, Location, Communicativeaction, Agenttype) {
        $scope.agent = entity;
        $scope.load = function (id) {
            Agent.get({id: id}, function(result) {
                $scope.agent = result;
            });
        };
        $rootScope.$on('thesisplatformApp:agentUpdate', function(event, result) {
            $scope.agent = result;
        });
    });
