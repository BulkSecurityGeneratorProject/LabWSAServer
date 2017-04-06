'use strict';

angular.module('thesisplatformApp')
    .controller('AssessmentController', function ($scope, Assessment, ParseLinks) {
        $scope.assessments = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Assessment.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.assessments = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Assessment.get({id: id}, function(result) {
                $scope.assessment = result;
                $('#deleteAssessmentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Assessment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAssessmentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.assessment = {
                assessmentValue: null,
                estimationTime: null,
                id: null
            };
        };
    });
