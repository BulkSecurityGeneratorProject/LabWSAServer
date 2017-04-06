'use strict';

angular.module('thesisplatformApp')
    .controller('AgentController', function ($scope, Agent, ParseLinks) {

        $scope.agents = [];
        $scope.currently_present_agents = [];
        $scope.page = 0;
        $scope.message = "";
        $scope.url="";
        $scope.test = false;
        $scope.requestType="POST";
        $scope.responseMessage= [];

        $scope.loadAll = function() {
            Agent.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.agents = result;
            });
        };

         $scope.loadAllPresentAgents = function() {
                    Agent.get_currently_present({page: $scope.page, size: 20}, function(result, headers) {
                        $scope.currently_present_agents = result;
                        console.log(999999, $scope.message);
                    });
                };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();
         $scope.loadAllPresentAgents();

        $scope.delete = function (id) {
            Agent.get({id: id}, function(result) {
                $scope.agent = result;
                $('#deleteAgentConfirmation').modal('show');
            });
        };

        $scope.sendRequestToTheServer = function () {
         console.log(123456, $scope.url);
                    Agent.client_trial({params:$scope.message, requestType:$scope.requestType, urlAddress:$scope.url}, function(result) {
                    $scope.responseMessage=result;

                    });
                };

        $scope.confirmDelete = function (id) {
            Agent.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAgentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.agent = {
                agentName: null,
                agentDescription: null,
                id: null
            };
        };

    });
