'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('graphicalreadout', {
                parent: 'entity',
                url: '/graphicalreadouts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Graphicalreadouts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/graphicalreadout/graphicalreadouts.html',
                        controller: 'GraphicalreadoutController'
                    }
                },
                resolve: {
                }
            })
            .state('graphicalreadout.detail', {
                parent: 'entity',
                url: '/graphicalreadout/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Graphicalreadout'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/graphicalreadout/graphicalreadout-detail.html',
                        controller: 'GraphicalreadoutDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Graphicalreadout', function($stateParams, Graphicalreadout) {
                        return Graphicalreadout.get({id : $stateParams.id});
                    }]
                }
            })
            .state('graphicalreadout.new', {
                parent: 'graphicalreadout',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/graphicalreadout/graphicalreadout-dialog.html',
                        controller: 'GraphicalreadoutDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    image: null,
                                    imageContentType: null,
                                    readoutTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('graphicalreadout', null, { reload: true });
                    }, function() {
                        $state.go('graphicalreadout');
                    })
                }]
            })
            .state('graphicalreadout.edit', {
                parent: 'graphicalreadout',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/graphicalreadout/graphicalreadout-dialog.html',
                        controller: 'GraphicalreadoutDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Graphicalreadout', function(Graphicalreadout) {
                                return Graphicalreadout.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('graphicalreadout', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
