'use strict';

app.controller('ArchivedCollectingUnitsController', function($scope, $route, $window, collectingUnitService, utilsService){
	$scope.collectingUnits = [];
	
	init();
	
	function init(){
		collectingUnitService.archived(function(data){
			$scope.collectingUnits = data;
		});
	};
	
	$scope.unarchive = function(collectingUnit){
		collectingUnitService.unarchive(collectingUnit.id, function(data){
			$route.reload();
		}, function(status, data) {
			$window.alert(data);
		});
	};
	
	$scope.delete = function(collectingUnit){
		var result = $window.confirm("Permanently delete collecting unit " + collectingUnit.name + " and all of it's collected measurements?");
		if(result == true){
			collectingUnitService.delete(collectingUnit.id, function(data){
				$window.alert(data);
				$route.reload();
			}, function(status, data) {
				$window.alert(data);
			});
		}
	};
	
	$scope.formatDate = function(timestamp) {
		return utilsService.formatDate(timestamp);
	}
});