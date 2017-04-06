'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('communicativeaction', {
                parent: 'entity',
                url: '/communicativeactions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Communicativeactions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/communicativeaction/communicativeactions.html',
                        controller: 'CommunicativeactionController'
                    }
                },
                resolve: {
                }
            })
            .state('communicativeaction.detail', {
                parent: 'entity',
                url: '/communicativeaction/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Communicativeaction'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/communicativeaction/communicativeaction-detail.html',
                        controller: 'CommunicativeactionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Communicativeaction', function($stateParams, Communicativeaction) {
                        return Communicativeaction.get({id : $stateParams.id});
                    }]
                }
            })
            .state('communicativeaction.new', {
                parent: 'communicativeaction',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/communicativeaction/communicativeaction-dialog.html',
                        controller: 'CommunicativeactionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    actionTime: null,
                                    content: null,
                                    language: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('communicativeaction', null, { reload: true });
                    }, function() {
                        $state.go('communicativeaction');
                    })
                }]
            })
            .state('communicativeaction.edit', {
                parent: 'communicativeaction',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/communicativeaction/communicativeaction-dialog.html',
                        controller: 'CommunicativeactionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Communicativeaction', function(Communicativeaction) {
                                return Communicativeaction.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('communicativeaction', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
