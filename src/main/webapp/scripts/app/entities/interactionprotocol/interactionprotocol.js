'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('interactionprotocol', {
                parent: 'entity',
                url: '/interactionprotocols',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Interactionprotocols'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/interactionprotocol/interactionprotocols.html',
                        controller: 'InteractionprotocolController'
                    }
                },
                resolve: {
                }
            })
            .state('interactionprotocol.detail', {
                parent: 'entity',
                url: '/interactionprotocol/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Interactionprotocol'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/interactionprotocol/interactionprotocol-detail.html',
                        controller: 'InteractionprotocolDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Interactionprotocol', function($stateParams, Interactionprotocol) {
                        return Interactionprotocol.get({id : $stateParams.id});
                    }]
                }
            })
            .state('interactionprotocol.new', {
                parent: 'interactionprotocol',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/interactionprotocol/interactionprotocol-dialog.html',
                        controller: 'InteractionprotocolDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    protocolName: null,
                                    protocolDescription: null,
                                    openingTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('interactionprotocol', null, { reload: true });
                    }, function() {
                        $state.go('interactionprotocol');
                    })
                }]
            })
            .state('interactionprotocol.edit', {
                parent: 'interactionprotocol',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/interactionprotocol/interactionprotocol-dialog.html',
                        controller: 'InteractionprotocolDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Interactionprotocol', function(Interactionprotocol) {
                                return Interactionprotocol.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('interactionprotocol', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
