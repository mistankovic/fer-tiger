'use strict';

app.controller('UsersController', function($scope, $route, $window, userService, utilsService) {
	$scope.users = [];

	init();
	
	function init() {
		userService.getAllUsers(function(data) {
			$scope.users = data;
		});
	}
	
	$scope.delete = function(user) {
		if($scope.users.length <= 1){
			$window.alert("You can not delete last user!");
			return;
		}
		var result = $window.confirm("Delete user " + user.firstName + " " + user.lastName + "?");
		if(result == true){
			userService.delete(user.id, function(data) {
				$window.alert(data);
				$route.reload();
			}, function(status, data) {
				$window.alert(data);
			});
		}
	};
	
	$scope.formatDate = function(d){
		return utilsService.formatDate(d);
	};
});