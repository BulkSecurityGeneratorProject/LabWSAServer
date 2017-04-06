'use strict';

angular.module('thesisplatformApp')
    .factory('Graphicalreadout', function ($resource, DateUtils) {
        return $resource('api/graphicalreadouts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.readoutTime = DateUtils.convertDateTimeFromServer(data.readoutTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
