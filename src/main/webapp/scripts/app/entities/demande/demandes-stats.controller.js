'use strict';

angular.module('gestionDemandesApp')
    .controller('DemandesStatsController', function ($scope, User) {
		var colors = d3.scale.category10();
		
		$scope.chartData = [
			{label: "one", value: 12.2, color: colors(0)}, 
			{label: "two", value: 45, color: colors(1)},
			{label: "three", value: 10, color: colors(2)}
		];
		$scope.chartOptions = {thickness: 200};
    });
