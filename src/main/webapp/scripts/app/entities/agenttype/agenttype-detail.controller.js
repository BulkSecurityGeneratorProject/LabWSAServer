'use strict';

angular.module('thesisplatformApp')
    .controller('AgenttypeDetailController', function ($scope, $rootScope, $stateParams, entity, Agenttype, Agent) {
        $scope.agenttype = entity;
        $scope.load = function (id) {
            Agenttype.get({id: id}, function(result) {
                $scope.agenttype = result;
            });
        };
        $rootScope.$on('thesisplatformApp:agenttypeUpdate', function(event, result) {
            $scope.agenttype = result;
        });
    });
