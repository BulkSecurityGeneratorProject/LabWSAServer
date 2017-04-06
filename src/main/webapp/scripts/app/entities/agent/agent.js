'use strict';

angular.module('thesisplatformApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('agent', {
                parent: 'entity',
                url: '/agents',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Agents'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/agent/agents.html',
                        controller: 'AgentController'
                    }
                },
                resolve: {
                }
            })
             .state('currently_present_agents', {
                            parent: 'entity',
                            url: '/currently_present_agents',
                            data: {
                                authorities: ['ROLE_USER'],
                                pageTitle: 'Agents'
                            },
                            views: {
                                'content@': {
                                    templateUrl: 'scripts/app/entities/agent/currently_present_agents.html',
                                    controller: 'AgentController'
                                }
                            },
                            resolve: {
                            }
                        })
            .state('send_message_to_agent', {
                                        parent: 'entity',
                                        url: '/messages_to_agents',
                                        data: {
                                          authorities: ['ROLE_USER'],
                                          pageTitle: 'Communication wirh agents'
                                        },
                                        views: {
                                            'content@': {
                                                templateUrl: 'scripts/app/entities/agent/messages.html',
                                                controller: 'AgentController'
                                            }
                                        },
                                        resolve: {
                                        }
                                    })
            .state('agent.detail', {
                parent: 'entity',
                url: '/agent/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Agent'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/agent/agent-detail.html',
                        controller: 'AgentDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Agent', function($stateParams, Agent) {
                        return Agent.get({id : $stateParams.id});
                    }]
                }
            })
            .state('agent.new', {
                parent: 'agent',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/agent/agent-dialog.html',
                        controller: 'AgentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    agentName: null,
                                    agentDescription: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('agent', null, { reload: true });
                    }, function() {
                        $state.go('agent');
                    })
                }]
            })
            .state('agent.edit', {
                parent: 'agent',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/agent/agent-dialog.html',
                        controller: 'AgentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Agent', function(Agent) {
                                return Agent.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('agent', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
