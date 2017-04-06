'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('unittype', {
                parent: 'entity',
                url: '/unittypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Unittypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/unittype/unittypes.html',
                        controller: 'UnittypeController'
                    }
                },
                resolve: {
                }
            })
            .state('unittype.detail', {
                parent: 'entity',
                url: '/unittype/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Unittype'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/unittype/unittype-detail.html',
                        controller: 'UnittypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Unittype', function($stateParams, Unittype) {
                        return Unittype.get({id : $stateParams.id});
                    }]
                }
            })
            .state('unittype.new', {
                parent: 'unittype',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/unittype/unittype-dialog.html',
                        controller: 'UnittypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    unitTypeName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('unittype', null, { reload: true });
                    }, function() {
                        $state.go('unittype');
                    })
                }]
            })
            .state('unittype.edit', {
                parent: 'unittype',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/unittype/unittype-dialog.html',
                        controller: 'UnittypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Unittype', function(Unittype) {
                                return Unittype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('unittype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
