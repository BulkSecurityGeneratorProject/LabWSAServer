'use strict';

angular.module('thesisplatformApp')
    .controller('PresencerequestDetailController', function ($scope, $rootScope, $stateParams, entity, Presencerequest, Presencestatus, Agent) {
        $scope.presencerequest = entity;
        $scope.load = function (id) {
            Presencerequest.get({id: id}, function(result) {
                $scope.presencerequest = result;
            });
        };
        $rootScope.$on('thesisplatformApp:presencerequestUpdate', function(event, result) {
            $scope.presencerequest = result;
        });
    });
