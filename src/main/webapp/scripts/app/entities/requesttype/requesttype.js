'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('requesttype', {
                parent: 'entity',
                url: '/requesttypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Requesttypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/requesttype/requesttypes.html',
                        controller: 'RequesttypeController'
                    }
                },
                resolve: {
                }
            })
            .state('requesttype.detail', {
                parent: 'entity',
                url: '/requesttype/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Requesttype'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/requesttype/requesttype-detail.html',
                        controller: 'RequesttypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Requesttype', function($stateParams, Requesttype) {
                        return Requesttype.get({id : $stateParams.id});
                    }]
                }
            })
            .state('requesttype.new', {
                parent: 'requesttype',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/requesttype/requesttype-dialog.html',
                        controller: 'RequesttypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    requestTypeName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('requesttype', null, { reload: true });
                    }, function() {
                        $state.go('requesttype');
                    })
                }]
            })
            .state('requesttype.edit', {
                parent: 'requesttype',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/requesttype/requesttype-dialog.html',
                        controller: 'RequesttypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Requesttype', function(Requesttype) {
                                return Requesttype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('requesttype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
