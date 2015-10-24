'use strict';

angular.module('gestionDemandesApp')
    .factory('DemandeSearch', function ($resource) {
        return $resource('api/_search/demandes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
