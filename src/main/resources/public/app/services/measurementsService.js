'use strict';

app.service('measurementsService', function($http, utilsService) {
	this.getRecent = function(callback) {
		$http.get('api/measurement/recent').success(function(data) {
			callback(data);
		});
	};

	this.getMeasurementsForGraph = function(sensorFieldsIds, dateFrom, dateTo, successCallback, errorCallback) {
		$http.post('api/measurement/graph?from=' + utilsService.formatForServer(dateFrom) + '&to=' + utilsService.formatForServer(dateTo), sensorFieldsIds)
				.success(function(data) { 
					successCallback(data); 
				}).error(function(data) {
					errorCallback(data);
				});
	};
	
	this.getMeasurementsInCsvUrl = function(sensorFieldsIds, dateFrom, dateTo, successCallback, errorCallback) {
		$http.post('api/measurement/prepare?from=' + utilsService.formatForServer(dateFrom) + '&to=' + utilsService.formatForServer(dateTo), 
				sensorFieldsIds).success(function(data) {
			successCallback('/api/measurement/' + data + '/csv');
		}).error(function(data) {
			errorCallback(data);
		});
	};
});