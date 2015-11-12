'use strict';

angular.module('gestionDemandesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('demande', {
                parent: 'entity',
                url: '/demandes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Demandes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/demande/demandes.html',
                        controller: 'DemandeController'
                    }
                },
                resolve: {
                }
            })
			.state('demande.stats', {
				parent: 'demande',
				url: '/stats',
				views : {
					'content@': {
						templateUrl: 'scripts/app/entities/demande/demande-stats.html',
						controller: 'DemandeStatsController'
					}
				}
			})
            .state('demande.detail', {
                parent: 'entity',
                url: '/demande/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Demande'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/demande/demande-detail.html',
                        controller: 'DemandeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Demande', function($stateParams, Demande) {
                        return Demande.get({id : $stateParams.id});
                    }]
                }
            })
            .state('demande.new', {
                parent: 'demande',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/demande/demande-dialog.html',
                        controller: 'DemandeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    titre: null,
                                    description: null,
                                    dateDeBesoin: null,
                                    statut: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('demande', null, { reload: true });
                    }, function() {
                        $state.go('demande');
                    })
                }]
            })
            .state('demande.edit', {
                parent: 'demande',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/demande/demande-dialog.html',
                        controller: 'DemandeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Demande', function(Demande) {
                                return Demande.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('demande', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
