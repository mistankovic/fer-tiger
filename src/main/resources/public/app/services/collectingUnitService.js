'use strict';

app.service('collectingUnitService', function($http){
	this.getAll = function(callback){
		$http.get('api/collecting-unit').success(function(data){
			callback(data);
		});
	};
	
	this.get = function(id, callback){
		$http.get('api/collecting-unit/' + id).success(function(data){
			callback(data);
		});
	};
	
	this.save = function(data, callback, errorCallback){
		console.log(data);
		$http.post('api/collecting-unit', data).success(function(data){
			callback(data);
		})
		.error(function(data, status){
			errorCallback(status, data);
		});
	};
	
	this.archive = function(id, successCallback, errorCallback){
		$http.post('api/collecting-unit/' + id + '/archive').success(function(data){
			successCallback(data);
		}).error(function(data, status) {
			errorCallback(status, data);
		});
	};
	
	this.unarchive = function(id, successCallback, errorCallback) {
		$http.post('api/collecting-unit/' + id + '/unarchive').success(function(data) {
			successCallback(data);
		}).error(function(data, status) {
			errorCallback(status, data);
		});
	};
	
	this.delete = function(id, successCallback, errorCallback){
		$http.delete('api/collecting-unit/' + id + '?archive=false').success(function(data){
			successCallback(data);
		}).error(function(data, status) {
			errorCallback(status, data);
		});
	};
	
	this.fetchSensors = function(cu, successCallback, errorCallback){
		$http.post('api/collecting-unit/fetch-sensors', cu).success(function(data){
			successCallback(data);
		}).error(function(data){
			errorCallback(data);
		});
	};
	
	this.getAllTypes = function(callback){
		$http.get('api/collecting-unit-type').success(function(data) {
			callback(data);
		});
	};
	
	this.collect = function(id, callback){
		$http.post('api/collecting-unit/' + id +'/collect').success(function(data){
			callback(data);
		});
	};
	
	this.collectAll = function(callback){
		$http.post('api/collecting-unit/collect/').success(function(data){
			callback(data);
		});
	};
	
	this.archived = function(callback){
		$http.get('api/collecting-unit?archived=true').success(function(data){
			callback(data);
		});
	};
	
	this.update = function(id, collectingUnit, sucecssCallback, errorCallback){
		$http.put('api/collecting-unit/' + id, collectingUnit).success(function(data){
			sucecssCallback(data);
		}).error(function(data, status) {
			errorCallback(status, data);
		});
	};
});