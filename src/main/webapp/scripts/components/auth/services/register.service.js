'use strict';

angular.module('thesisplatformApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


