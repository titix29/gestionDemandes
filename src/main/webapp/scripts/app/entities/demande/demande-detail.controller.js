'use strict';

angular.module('gestionDemandesApp')
    .controller('DemandeDetailController', function ($scope, $rootScope, $stateParams, entity, Demande, User) {
        $scope.demande = entity;
        $scope.load = function (id) {
            Demande.get({id: id}, function(result) {
                $scope.demande = result;
            });
        };
        var unsubscribe = $rootScope.$on('gestionDemandesApp:demandeUpdate', function(event, result) {
            $scope.demande = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
