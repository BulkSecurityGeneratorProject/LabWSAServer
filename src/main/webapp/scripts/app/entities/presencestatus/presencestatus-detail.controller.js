'use strict';

angular.module('thesisplatformApp')
    .controller('PresencestatusDetailController', function ($scope, $rootScope, $stateParams, entity, Presencestatus, Presencerequest) {
        $scope.presencestatus = entity;
        $scope.load = function (id) {
            Presencestatus.get({id: id}, function(result) {
                $scope.presencestatus = result;
            });
        };
        $rootScope.$on('thesisplatformApp:presencestatusUpdate', function(event, result) {
            $scope.presencestatus = result;
        });
    });
