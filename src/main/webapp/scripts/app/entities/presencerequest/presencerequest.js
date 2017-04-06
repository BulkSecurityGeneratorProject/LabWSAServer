'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('presencerequest', {
                parent: 'entity',
                url: '/presencerequests',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Presencerequests'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/presencerequest/presencerequests.html',
                        controller: 'PresencerequestController'
                    }
                },
                resolve: {
                }
            })
            .state('presencerequest.detail', {
                parent: 'entity',
                url: '/presencerequest/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Presencerequest'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/presencerequest/presencerequest-detail.html',
                        controller: 'PresencerequestDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Presencerequest', function($stateParams, Presencerequest) {
                        return Presencerequest.get({id : $stateParams.id});
                    }]
                }
            })
            .state('presencerequest.new', {
                parent: 'presencerequest',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/presencerequest/presencerequest-dialog.html',
                        controller: 'PresencerequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    presenceRequestTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('presencerequest', null, { reload: true });
                    }, function() {
                        $state.go('presencerequest');
                    })
                }]
            })
            .state('presencerequest.edit', {
                parent: 'presencerequest',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/presencerequest/presencerequest-dialog.html',
                        controller: 'PresencerequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Presencerequest', function(Presencerequest) {
                                return Presencerequest.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('presencerequest', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
