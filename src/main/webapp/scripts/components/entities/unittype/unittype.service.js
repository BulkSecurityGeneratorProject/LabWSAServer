'use strict';

angular.module('thesisplatformApp')
    .factory('Unittype', function ($resource, DateUtils) {
        return $resource('api/unittypes/:id', {}, {
            'query': { method: 'GET', isArray: true},
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
