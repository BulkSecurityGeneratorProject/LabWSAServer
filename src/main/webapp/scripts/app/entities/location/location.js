'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('location', {
                parent: 'entity',
                url: '/locations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Locations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/location/locations.html',
                        controller: 'LocationController'
                    }
                },
                resolve: {
                }
            })
            .state('location.detail', {
                parent: 'entity',
                url: '/location/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Location'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/location/location-detail.html',
                        controller: 'LocationDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Location', function($stateParams, Location) {
                        return Location.get({id : $stateParams.id});
                    }]
                }
            })
            .state('location.new', {
                parent: 'location',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/location/location-dialog.html',
                        controller: 'LocationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    xPosition: null,
                                    yPosition: null,
                                    time: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('location', null, { reload: true });
                    }, function() {
                        $state.go('location');
                    })
                }]
            })
            .state('location.edit', {
                parent: 'location',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/location/location-dialog.html',
                        controller: 'LocationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Location', function(Location) {
                                return Location.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('location', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
