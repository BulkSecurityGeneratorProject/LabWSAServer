'use strict';

angular.module('thesisplatformApp')
    .factory('Registeredobject', function ($resource, DateUtils) {
        return $resource('api/registeredobjects/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.registrationTime = DateUtils.convertDateTimeFromServer(data.registrationTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
