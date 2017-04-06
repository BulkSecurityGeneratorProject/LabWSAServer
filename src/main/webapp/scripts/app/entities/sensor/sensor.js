'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensor', {
                parent: 'entity',
                url: '/sensors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Sensors'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensor/sensors.html',
                        controller: 'SensorController'
                    }
                },
                resolve: {
                }
            })
            .state('sensor.detail', {
                parent: 'entity',
                url: '/sensor/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Sensor'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensor/sensor-detail.html',
                        controller: 'SensorDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Sensor', function($stateParams, Sensor) {
                        return Sensor.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sensor.new', {
                parent: 'sensor',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sensor/sensor-dialog.html',
                        controller: 'SensorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    sensorName: null,
                                    sensorAccuracy: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sensor', null, { reload: true });
                    }, function() {
                        $state.go('sensor');
                    })
                }]
            })
            .state('sensor.edit', {
                parent: 'sensor',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sensor/sensor-dialog.html',
                        controller: 'SensorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Sensor', function(Sensor) {
                                return Sensor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sensor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
