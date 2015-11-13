'use strict';

angular.module('gestionDemandesApp')
    .controller('DemandeStatsController', function ($scope, Analytics, User) {
		$scope.loadDemandesByState = function() {
			Analytics.get({report: 'demandesByState'}, function(result) {
				var colors = d3.scale.category10();
				
				$scope.chartData = [];
				var i = 0;
				// cf. http://stackoverflow.com/questions/9007065/how-to-iterate-json-hashmap
				// but remove angularjs properties (ex : $promise or $resolved)
				// TODO : use map to convert from result to chartData
				for (var state in result) {
					if (result.hasOwnProperty(state) && state.indexOf('$') == -1) {
						$scope.chartData.push({label: state, value: result[state], color: colors(i++)});
					}
				}
				$scope.chartOptions = {thickness: 75};
			});
		};
		
    	$scope.loadDemandesByState();
    });
