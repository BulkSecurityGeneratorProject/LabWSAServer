'use strict';

angular.module('thesisplatformApp')
    .factory('Sensortype', function ($resource, DateUtils) {
        return $resource('api/sensortypes/:id', {}, {
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
