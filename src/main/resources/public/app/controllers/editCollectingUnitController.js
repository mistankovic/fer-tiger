'use strict';

app.controller('EditCollectingUnitController', function($scope, $routeParams, $location, $window, collectingUnitService) {
	$scope.collectingUnit = null;
	$scope.allSensors = [];
	
	init();
	
	function init(){
		var id = $routeParams.id;
		collectingUnitService.get(id, function(data) {
			$scope.collectingUnit = data;
			prepareSensors();
		});
	}

	function prepareSensors(){
		collectingUnitService.fetchSensors($scope.collectingUnit, function(data) {
			selectExistingSensors(data);
		}, function(data){
			$window.alert("Error fetching sensors from collecting unit!");
			selectExistingSensors($scope.collectingUnit.sensors);
		});
	}
	
	function selectExistingSensors(allSensors){
		var sensorsNames = toNames($scope.collectingUnit.sensors);
		
		for (var int = 0; int < allSensors.length; int++) {
			var isSensorChecked = false;
			if (sensorsNames.indexOf(allSensors[int].name) >= 0) {
				isSensorChecked = true;
			}
			allSensors[int].checked = isSensorChecked;
		}
		
		$scope.allSensors = allSensors;
	}
	
	function toNames(sensors){
		var namesArray = [];
		for (var int = 0; int < sensors.length; int++) {
			namesArray.push(sensors[int].name);
		}
		return namesArray;
	}
	
	$scope.update = function() {
		var checkedSensors = [];
		for (var int = 0; int < $scope.allSensors.length; int++) {
			var sensor = $scope.allSensors[int];
			if(sensor.checked){
				checkedSensors.push(sensor);
			}
		}
		
		$scope.collectingUnit.sensors = checkedSensors;
		
		collectingUnitService.update($routeParams.id, $scope.collectingUnit, function(data) {
			$location.path("/collecting-units");
		}, function(status, data) {
			if(status == 400){
				$window.alert('Please check if name and URL of collecting unit You entered is unique and is all data filled. Updating failed with message:\n' + data);
			}
			else{
				$window.alert(data);
			}
		});
	};
});