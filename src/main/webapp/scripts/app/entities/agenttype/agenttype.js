'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('agenttype', {
                parent: 'entity',
                url: '/agenttypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Agenttypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/agenttype/agenttypes.html',
                        controller: 'AgenttypeController'
                    }
                },
                resolve: {
                }
            })
            .state('agenttype.detail', {
                parent: 'entity',
                url: '/agenttype/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Agenttype'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/agenttype/agenttype-detail.html',
                        controller: 'AgenttypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Agenttype', function($stateParams, Agenttype) {
                        return Agenttype.get({id : $stateParams.id});
                    }]
                }
            })
            .state('agenttype.new', {
                parent: 'agenttype',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/agenttype/agenttype-dialog.html',
                        controller: 'AgenttypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    agentTypeName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('agenttype', null, { reload: true });
                    }, function() {
                        $state.go('agenttype');
                    })
                }]
            })
            .state('agenttype.edit', {
                parent: 'agenttype',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/agenttype/agenttype-dialog.html',
                        controller: 'AgenttypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Agenttype', function(Agenttype) {
                                return Agenttype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('agenttype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
