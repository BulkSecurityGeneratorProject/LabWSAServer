'use strict';

angular.module('thesisplatformApp')
    .factory('Readout', function ($resource, DateUtils) {
        return $resource('api/readouts/:id', {}, {
            'query': { method: 'GET', isArray: true},
             'labels': {method: 'GET', url: 'api/chart_labels/:s', isArray: true},
             'readouts': {method: 'GET', url: 'api/chart_readouts/:s', isArray: true},
             'readoutswithtime' : {method: 'GET', url: 'api/chart_readouts_with_timerange', isArray: true},
             'readouts_for_agent' : {method: 'GET', url: 'api/readouts_for_agent', isArray: true},
             'labelswithtime' : {method: 'GET', url: 'api/chart_labels_with_time_range', isArray: true},
             'agents': {method: 'GET', url: 'api/readouts/agents', isArray: true},
             'sensors': {method: 'GET', url: 'api/readouts/sensors/:a', isArray: true},
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
