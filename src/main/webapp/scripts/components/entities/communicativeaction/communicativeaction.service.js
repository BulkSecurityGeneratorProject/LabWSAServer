'use strict';

angular.module('thesisplatformApp')
    .factory('Communicativeaction', function ($resource, DateUtils) {
        return $resource('api/communicativeactions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'filtrdata': {method: 'GET', url: 'api/filter_communicativeactions/:filterType', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.actionTime = DateUtils.convertDateTimeFromServer(data.actionTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
