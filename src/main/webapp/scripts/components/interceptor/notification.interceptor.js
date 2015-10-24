 'use strict';

angular.module('gestionDemandesApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-gestionDemandesApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-gestionDemandesApp-params')});
                }
                return response;
            }
        };
    });
