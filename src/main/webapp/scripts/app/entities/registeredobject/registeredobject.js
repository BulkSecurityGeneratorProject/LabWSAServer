'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('registeredobject', {
                parent: 'entity',
                url: '/registeredobjects',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Registeredobjects'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/registeredobject/registeredobjects.html',
                        controller: 'RegisteredobjectController'
                    }
                },
                resolve: {
                }
            })
            .state('registeredobject.detail', {
                parent: 'entity',
                url: '/registeredobject/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Registeredobject'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/registeredobject/registeredobject-detail.html',
                        controller: 'RegisteredobjectDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Registeredobject', function($stateParams, Registeredobject) {
                        return Registeredobject.get({id : $stateParams.id});
                    }]
                }
            })
            .state('registeredobject.new', {
                parent: 'registeredobject',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/registeredobject/registeredobject-dialog.html',
                        controller: 'RegisteredobjectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    objectName: null,
                                    objectDescription: null,
                                    registrationTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('registeredobject', null, { reload: true });
                    }, function() {
                        $state.go('registeredobject');
                    })
                }]
            })
            .state('registeredobject.edit', {
                parent: 'registeredobject',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/registeredobject/registeredobject-dialog.html',
                        controller: 'RegisteredobjectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Registeredobject', function(Registeredobject) {
                                return Registeredobject.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('registeredobject', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
