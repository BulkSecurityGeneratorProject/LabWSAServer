'use strict';

angular.module('thesisplatformApp')
    .controller('FeatureController', function ($scope, Feature, ParseLinks) {
        $scope.features = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Feature.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.features = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Feature.get({id: id}, function(result) {
                $scope.feature = result;
                $('#deleteFeatureConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Feature.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFeatureConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.feature = {
                featureName: null,
                registrationTime: null,
                id: null
            };
        };
    });
