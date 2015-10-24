'use strict';

angular.module('gestionDemandesApp')
    .factory('Demande', function ($resource, DateUtils) {
        return $resource('api/demandes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateDeBesoin = DateUtils.convertDateTimeFromServer(data.dateDeBesoin);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
