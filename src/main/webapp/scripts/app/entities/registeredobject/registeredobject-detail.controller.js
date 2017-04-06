'use strict';

angular.module('thesisplatformApp')
    .controller('RegisteredobjectDetailController', function ($scope, $rootScope, $stateParams, entity, Registeredobject, Agent, Assessment) {
        $scope.registeredobject = entity;
        $scope.load = function (id) {
            Registeredobject.get({id: id}, function(result) {
                $scope.registeredobject = result;
            });
        };
        $rootScope.$on('thesisplatformApp:registeredobjectUpdate', function(event, result) {
            $scope.registeredobject = result;
        });
    });
