'use strict';

angular.module('thesisplatformApp')
    .factory('Assessment', function ($resource, DateUtils) {
        return $resource('api/assessments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.estimationTime = DateUtils.convertDateTimeFromServer(data.estimationTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
