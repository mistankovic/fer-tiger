'use strict';

app.controller('CollectingUnitsController', function($scope, $route, $window, collectingUnitService, utilsService){
	$scope.collectingUnits = {};
	
	init();
	
	function init(){
		collectingUnitService.getAll(function(data){
			$scope.collectingUnits = data;
		});
	};
	
	$scope.archive = function(collectingUnit){
		collectingUnitService.archive(collectingUnit.id, function(data){
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
	
	$scope.collectAll = function(){
		var result = $window.confirm("This operation might take a while. Proceed?");
		if(result == true){
			collectingUnitService.collectAll(function(data){
				$window.alert(data);
				$route.reload();
			});
		}
	};
	
	$scope.collect = function(collectingUnit){
		collectingUnitService.collect(collectingUnit.id, function(data){
			$window.alert(data);
			$route.reload();
		});
	};
	
	$scope.formatDate = function(timestamp){
		return utilsService.formatDate(timestamp);
	};
});