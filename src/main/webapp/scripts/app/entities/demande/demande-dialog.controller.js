'use strict';

angular.module('gestionDemandesApp').controller('DemandeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Demande', 'User',
        function($scope, $stateParams, $modalInstance, entity, Demande, User) {

        $scope.demande = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Demande.get({id : id}, function(result) {
                $scope.demande = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('gestionDemandesApp:demandeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.demande.id != null) {
                Demande.update($scope.demande, onSaveFinished);
            } else {
                Demande.save($scope.demande, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
