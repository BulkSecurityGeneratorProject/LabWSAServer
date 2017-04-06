'use strict';

angular.module('thesisplatformApp')
    .controller('PresencerequestController', function ($scope, Presencerequest, ParseLinks) {
        $scope.presencerequests = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Presencerequest.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.presencerequests = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Presencerequest.get({id: id}, function(result) {
                $scope.presencerequest = result;
                $('#deletePresencerequestConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Presencerequest.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePresencerequestConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.presencerequest = {
                presenceRequestTime: null,
                id: null
            };
        };
    });
