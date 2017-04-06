'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('presencestatus', {
                parent: 'entity',
                url: '/presencestatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Presencestatuss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/presencestatus/presencestatuss.html',
                        controller: 'PresencestatusController'
                    }
                },
                resolve: {
                }
            })
            .state('presencestatus.detail', {
                parent: 'entity',
                url: '/presencestatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Presencestatus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/presencestatus/presencestatus-detail.html',
                        controller: 'PresencestatusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Presencestatus', function($stateParams, Presencestatus) {
                        return Presencestatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('presencestatus.new', {
                parent: 'presencestatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/presencestatus/presencestatus-dialog.html',
                        controller: 'PresencestatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    presenceStatusName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('presencestatus', null, { reload: true });
                    }, function() {
                        $state.go('presencestatus');
                    })
                }]
            })
            .state('presencestatus.edit', {
                parent: 'presencestatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/presencestatus/presencestatus-dialog.html',
                        controller: 'PresencestatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Presencestatus', function(Presencestatus) {
                                return Presencestatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('presencestatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
