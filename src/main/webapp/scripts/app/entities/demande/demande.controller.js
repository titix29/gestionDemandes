'use strict';

angular.module('gestionDemandesApp')
    .controller('DemandeController', function ($scope, Demande, DemandeSearch, ParseLinks) {
        $scope.demandes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Demande.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.demandes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Demande.get({id: id}, function(result) {
                $scope.demande = result;
                $('#deleteDemandeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Demande.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDemandeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            DemandeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.demandes = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.demande = {
                titre: null,
                description: null,
                dateDeBesoin: null,
                statut: null,
                id: null
            };
        };
    });
