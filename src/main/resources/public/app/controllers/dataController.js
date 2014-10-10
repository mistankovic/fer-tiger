'use strict';

app.controller('DataController', function($scope, $window, collectingUnitService,
		sensorService, measurementsService) {
	$scope.collectingUnits = [];
	
	$scope.selectedCollectingUnits = [];
	$scope.sensors = [];
	$scope.selectedSensors = [];
	$scope.selectedFields = [];
	
	$scope.selectedCollectingUnit = null;
	$scope.selectedCuSensors = null;
	$scope.selectedSensor = null;
	$scope.fieldSelected = null;
	
	$scope.dateFrom = null;
	$scope.dateTo = null;
	$scope.graphData = [];
	$scope.showGraph = false;

	var cuIndex = 0;

	init();

	function init() {
		collectingUnitService.getAll(function(data) {
			$scope.collectingUnits = data;
		});
	}
	;

	$scope.collectingUnitChanged = function() {
		sensorService.getAllFromCollectingUnit($scope.selectedCollectingUnit.id,
				function(data) {
					$scope.selectedCuSensors = data;
				});
	};
	
	$scope.collectingUnitChangedInCollection= function(index){
		var cu = $scope.selectedCollectingUnits[index].cu; 
		sensorService.getAllFromCollectingUnit(cu.id,
				function(data) {
					$scope.selectedSensors[index] = data;
					$scope.selectedFields[index] = null;
				});	
	};
	
	$scope.addFieldChooser = function() {
		var cu = $scope.selectedCollectingUnit;
		var sensor = $scope.selectedSensor;
		var field = $scope.fieldSelected;
		var selectedCuSensors = $scope.selectedCuSensors;
		
		$scope.selectedCollectingUnits.push({'id':cuIndex, 'cu': cu});
		cuIndex++;
		$scope.sensors.push(selectedCuSensors);
		$scope.selectedSensors.push(sensor);
		$scope.selectedFields.push(field);
		
		$scope.selectedCollectingUnit = null;
		$scope.selectedCuSensors = null;
		$scope.selectedSensor = null;
		$scope.fieldSelected = null;
	};

	$scope.drawGraph = function() {
		var fieldsIds = getFieldsIds();
		measurementsService.getMeasurementsForGraph(fieldsIds,
				$scope.dateFrom, $scope.dateTo, function(data) {
					$scope.graphData = data;
					$scope.showGraph = true;
				}, function(data) {
					$window.alert("Unable to parse data You entered. Please check if dates are and sensor fields are selected.");
				});
	};
	
	$scope.createCSV = function() {
		var fieldsIds = getFieldsIds();
		measurementsService.getMeasurementsInCsvUrl(fieldsIds, $scope.dateFrom, $scope.dateTo, 
			function(data) {
				var element = angular.element('<a/>');
			     element.attr({
			         href: data,
			         download: 'data.csv'
			     })[0].click();
			}, function(data) {
				$window.alert(data);
		});
	};

	$scope.xAxisTickFormatFunction = function() {
		return function(d) {
			return d3.time.format('%m/%d-%H:%M:%S')(new Date(d));
		}
	};

	$scope.yAxisTickFormatFunction = function() {
		return function(d) {
			return d;
		}
	};
	
	function getFieldsIds(){
		var fieldsIds = [];
		$scope.selectedFields.forEach(function(field) {
			fieldsIds.push(field.id);
		});
		if($scope.fieldSelected != null){
			fieldsIds.push($scope.fieldSelected.id);
		}
		
		return fieldsIds;
	}

});