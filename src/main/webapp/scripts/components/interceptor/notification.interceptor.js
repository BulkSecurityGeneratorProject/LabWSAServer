 'use strict';

angular.module('thesisplatformApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-thesisplatformApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-thesisplatformApp-params')});
                }
                return response;
            }
        };
    });
