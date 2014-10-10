'use strict';

app.controller('DashboardController', function($scope, collectingUnitService, utilsService){
	$scope.lastCollectingUnitsSync = {};
	
	init();
	
	function init(){
		collectingUnitService.getAll(function(data){
			$scope.lastCollectingUnitsSync = data;
		});
	}
	
	$scope.formatDate = function(timestamp) {
		return utilsService.formatDate(timestamp);
	}
});