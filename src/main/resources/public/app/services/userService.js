'use strict';

app.service('userService', function($http) {
	
	this.getAllUsers = function(callback) {
		$http.get('api/user').success(function(data) {
			callback(data);
		});
	};
	
	this.getUser = function(id, successCallback, errorCallback) {
		$http.get('api/user/' + id).success(function(data) {
			successCallback(data);
		}, function(data) {
			errorCallback(data);
		});
	};

	this.getAllRoles = function(callback) {
		$http.get('api/user-role').success(function(data) {
			callback(data);
		});
	};
	
	this.update = function(id, user, successCallback, errorCallback) {
		$http.put('api/user/' + id, user).success(function(data) {
			successCallback(data);
		}).error(function(data, status) {
			errorCallback(status, data);
		});
	};
	
	this.save = function(user, successCallback, errorCallback) {
		$http.post('api/user', user).success(function(data) {
			successCallback(data);
		}).error(function(data, status) {
			errorCallback(status, data);
		});
	};
	
	this.delete = function(id, successCallback, errorCallback){
		$http.delete('api/user/' + id).success(function(data){
			successCallback(data);
		}).error(function(data, status) {
			errorCallback(status, data);
		});
	};
});