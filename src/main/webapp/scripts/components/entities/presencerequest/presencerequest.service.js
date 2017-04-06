'use strict';

angular.module('thesisplatformApp')
    .factory('Presencerequest', function ($resource, DateUtils) {
        return $resource('api/presencerequests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.presenceRequestTime = DateUtils.convertDateTimeFromServer(data.presenceRequestTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
