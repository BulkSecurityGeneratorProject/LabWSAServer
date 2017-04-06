'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('assessment', {
                parent: 'entity',
                url: '/assessments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Assessments'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/assessment/assessments.html',
                        controller: 'AssessmentController'
                    }
                },
                resolve: {
                }
            })
            .state('assessment.detail', {
                parent: 'entity',
                url: '/assessment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Assessment'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/assessment/assessment-detail.html',
                        controller: 'AssessmentDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Assessment', function($stateParams, Assessment) {
                        return Assessment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('assessment.new', {
                parent: 'assessment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/assessment/assessment-dialog.html',
                        controller: 'AssessmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    assessmentValue: null,
                                    estimationTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('assessment', null, { reload: true });
                    }, function() {
                        $state.go('assessment');
                    })
                }]
            })
            .state('assessment.edit', {
                parent: 'assessment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/assessment/assessment-dialog.html',
                        controller: 'AssessmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Assessment', function(Assessment) {
                                return Assessment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('assessment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
