'use strict';

angular.module('gestionDemandesApp')
    .factory('Analytics', function ($resource) {
        return $resource('api/analytics/:report', {}, {
            'get': { 
            	method: 'GET'
            }
        });
    });
