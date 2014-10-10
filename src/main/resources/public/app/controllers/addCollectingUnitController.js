'use strict';

app.controller('AddCollectingUnitController', function($scope, $window, collectingUnitService){
	$scope.collectingUnit = {};
	$scope.cuSensors = [];
	
	init();
	
	function init(){
		$scope.collectingUnit.name = null;
		$scope.collectingUnit.username = null;
		$scope.collectingUnit.password = null;
		$scope.collectingUnit.locationUrl = null;
		$scope.collectingUnit.collectingDelay = null;
		$scope.collectingUnit.type = null;
		$scope.collectingUnit.sensors = [];
		$scope.collectingUnitTypes = [];
		$scope.sensorsRetrieved = false;
		
		collectingUnitService.getAllTypes(function(data){
			console.log(data);
			$scope.collectingUnitTypes = data;
		});
	};
	
	$scope.save = function(){
		var checkedSensors = [];
		for (var int = 0; int < $scope.cuSensors.length; int++) {
			var sensor = $scope.cuSensors[int];
			if(sensor.checked){
				checkedSensors.push(sensor);
			}
		}
		
		$scope.collectingUnit.sensors = checkedSensors;
		
		collectingUnitService.save($scope.collectingUnit, function(data){
			$window.location.href = "#/collecting-units"
		}, function(status, data){
			if(status == 400){
				$scope.sensorsRetrieved = false;
				$scope.collectingUnit.sensors = [];
				$window.alert('Please check if name and URL of collecting unit You entered is unique and is all data filled. Saving failed with message:\n' + data);
			}
			else{
				$window.alert(data);
			}
		});
	};
	
	$scope.fetchSensors = function(){
		collectingUnitService.fetchSensors($scope.collectingUnit, function(data){
			$scope.cuSensors = data;
			$scope.sensorsRetrieved = true;
		}, function(data) {
			$window.alert("Unable to access collecting unit and retrieve sensors with provided data.");
		});
	};
});