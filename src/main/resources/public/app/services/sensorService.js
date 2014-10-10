'use strict';

app.service('sensorService', function($http){
	this.getAllFromCollectingUnit = function(collectingUnitId, callback){
		$http.get('api/collecting-unit/' + collectingUnitId + '/sensor').success(function(data) {
			callback(data);
		});
	};
});