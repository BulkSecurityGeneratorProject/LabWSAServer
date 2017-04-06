'use strict';

angular.module('thesisplatformApp')
    .controller('CommunicativeactionController', function ($scope, Communicativeaction, ParseLinks) {
        $scope.communicativeactions = [];
        $scope.page = 0;
        $scope.ftype = "we";

        $scope.loadAll = function() {
            Communicativeaction.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.communicativeactions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Communicativeaction.get({id: id}, function(result) {
                $scope.communicativeaction = result;
                $('#deleteCommunicativeactionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Communicativeaction.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCommunicativeactionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.refreshAfterFiltering = function () {
                    $scope.filterData();
                    $scope.clear();
                };

        $scope.clear = function () {
            $scope.communicativeaction = {
                actionTime: null,
                content: null,
                language: null,
                id: null
            };
        };

        $scope.filterData = function (){
        if($scope.ftype == "all"){
            $scope.loadAll();
        }
        else{
            Communicativeaction.filtrdata( {filterType:$scope.ftype}, function(result, headers) {
                                                                      $scope.links = ParseLinks.parse(headers('link'));
                                                                      $scope.communicativeactions = result;
                                                                  });
        }
        };
    });
