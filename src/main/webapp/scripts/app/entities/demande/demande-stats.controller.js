'use strict';

angular.module('gestionDemandesApp')
    .controller('DemandeStatsController', function ($scope, Analytics, User) {
		$scope.loadDemandesByState = function() {
			Analytics.get({report: 'demandesByState'}, function(result) {
				var colors = d3.scale.category10();
				
				$scope.chartData = [];
				// Convert JSON result into chartData-compatible JSON object
				Object.keys(result).filter(function(key) {
					// Remove angularjs properties (ex : $promise or $resolved)
					return key.indexOf('$') == -1;
				}).forEach(function(key, index) {
					$scope.chartData.push({label: key, value: result[key], color: colors(index)});
				});
				
				$scope.chartOptions = {thickness: 75};
			});
		};
		
    	$scope.loadDemandesByState();
    });
