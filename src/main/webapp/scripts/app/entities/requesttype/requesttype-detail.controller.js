'use strict';

angular.module('thesisplatformApp')
    .controller('RequesttypeDetailController', function ($scope, $rootScope, $stateParams, entity, Requesttype, Communicativeaction) {
        $scope.requesttype = entity;
        $scope.load = function (id) {
            Requesttype.get({id: id}, function(result) {
                $scope.requesttype = result;
            });
        };
        $rootScope.$on('thesisplatformApp:requesttypeUpdate', function(event, result) {
            $scope.requesttype = result;
        });
    });
