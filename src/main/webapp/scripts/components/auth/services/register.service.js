'use strict';

angular.module('gestionDemandesApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


