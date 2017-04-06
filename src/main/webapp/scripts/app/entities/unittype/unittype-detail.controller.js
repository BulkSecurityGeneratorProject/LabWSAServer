'use strict';

angular.module('thesisplatformApp')
    .controller('UnittypeDetailController', function ($scope, $rootScope, $stateParams, entity, Unittype, Sensor) {
        $scope.unittype = entity;
        $scope.load = function (id) {
            Unittype.get({id: id}, function(result) {
                $scope.unittype = result;
            });
        };
        $rootScope.$on('thesisplatformApp:unittypeUpdate', function(event, result) {
            $scope.unittype = result;
        });
    });
