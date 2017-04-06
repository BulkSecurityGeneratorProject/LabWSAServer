'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensortype', {
                parent: 'entity',
                url: '/sensortypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Sensortypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensortype/sensortypes.html',
                        controller: 'SensortypeController'
                    }
                },
                resolve: {
                }
            })
            .state('sensortype.detail', {
                parent: 'entity',
                url: '/sensortype/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Sensortype'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensortype/sensortype-detail.html',
                        controller: 'SensortypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Sensortype', function($stateParams, Sensortype) {
                        return Sensortype.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sensortype.new', {
                parent: 'sensortype',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sensortype/sensortype-dialog.html',
                        controller: 'SensortypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    sensorTypeName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sensortype', null, { reload: true });
                    }, function() {
                        $state.go('sensortype');
                    })
                }]
            })
            .state('sensortype.edit', {
                parent: 'sensortype',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sensortype/sensortype-dialog.html',
                        controller: 'SensortypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Sensortype', function(Sensortype) {
                                return Sensortype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sensortype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
