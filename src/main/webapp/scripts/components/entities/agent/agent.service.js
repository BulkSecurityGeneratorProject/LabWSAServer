'use strict';

angular.module('thesisplatformApp')
    .factory('Agent', function ($resource, DateUtils) {
        return $resource('api/agents/:id', {}, {
            'query': { method: 'GET', isArray: true},
             'get_currently_present': {method: 'GET', url: 'api/currently_present_agents', isArray: true},
             'client_trial': {method: 'GET', url: 'api/client_trial', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
