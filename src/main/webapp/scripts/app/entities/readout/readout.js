'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('readout', {
                parent: 'entity',
                url: '/readouts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Readouts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/readout/readouts.html',
                        controller: 'ReadoutController'
                    }
                },
                resolve: {
                }
            })
            .state('readout.statistics', {
                            parent: 'entity',
                            url: '/readouts_statisctics',
                            data: {
                                authorities: ['ROLE_USER'],
                                pageTitle: 'Readouts'
                            },
                            views: {
                                'content@': {
                                    templateUrl: 'scripts/app/entities/readout/readouts_statistics.html',
                                    controller: 'ReadoutController'
                                }
                            },
                            resolve: {
                            }
                        })
            .state('readout.detail', {
                parent: 'entity',
                url: '/readout/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Readout'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/readout/readout-detail.html',
                        controller: 'ReadoutDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Readout', function($stateParams, Readout) {
                        return Readout.get({id : $stateParams.id});
                    }]
                }
            })
            .state('readout.new', {
                parent: 'readout',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/readout/readout-dialog.html',
                        controller: 'ReadoutDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    readoutValue: null,
                                    readoutTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('readout', null, { reload: true });
                    }, function() {
                        $state.go('readout');
                    })
                }]
            })
             .state('readout.charts', {
              parent: 'entity',
              url: '/charts',
              data: {
               authorities: ['ROLE_USER'],
               pageTitle: 'Readouts'
               },
                views: {
                 'content@': {
                  templateUrl: 'scripts/app/entities/readout/charts.html',
                   controller: 'ReadoutChartsController'
                    }
                      },
                     resolve: {
                   }
          })
            .state('readout.edit', {
                parent: 'readout',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/readout/readout-dialog.html',
                        controller: 'ReadoutDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Readout', function(Readout) {
                                return Readout.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('readout', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
