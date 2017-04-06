'use strict';

angular.module('thesisplatformApp')
    .factory('Interactionprotocol', function ($resource, DateUtils) {
        return $resource('api/interactionprotocols/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.openingTime = DateUtils.convertDateTimeFromServer(data.openingTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
