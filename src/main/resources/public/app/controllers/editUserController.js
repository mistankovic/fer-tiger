'use strict';

app.controller('EditUserController', function($scope, $routeParams, $location, $window, userService) {
	$scope.user = {};
	$scope.allRoles = [];
	$scope.passwordConfirmation = null;

	init();

	function init() {
		var id = $routeParams.id;
		userService.getUser(id, function(data) {
			$scope.user = data;
			prepareRoles();
		}, function(errorData) {
			$window.alert(errorData);
			$location.path('/users');
		});
	}

	function prepareRoles() {
		userService.getAllRoles(function(data) {
			var allRoles = data;
			var namesArray = toNames($scope.user.roles);
			
			for (var int = 0; int < allRoles.length; int++) {
				var isRoleChecked = false;
				if (namesArray.indexOf(allRoles[int].name) >= 0) {
					isRoleChecked = true;
				}
				allRoles[int].checked = isRoleChecked;
			}
			
			$scope.allRoles = allRoles;
		});

	}
	
	function toNames(roles){
		var namesArray = [];
		for (var int = 0; int < roles.length; int++) {
			namesArray.push(roles[int].name);
		}
		return namesArray;
	}

	$scope.update = function() {
		if($scope.user.password != null && $scope.user.password !== $scope.passwordConfirmation){
			$window.alert("Passwords do not match. Please enter again.");
			return;
		}
		
		var checkedRoles = [];
		for (var int = 0; int < $scope.allRoles.length; int++) {
			var role = $scope.allRoles[int];
			if(role.checked){
				checkedRoles.push(role);
			}
		}
		
		$scope.user.roles = checkedRoles;
		
		userService.update($routeParams.id, $scope.user, function(data){
			$window.alert("Successfully updated user!");
			$location.path('/users');
		}, function(status, data) {
			if(status == 400){
				$window.alert("You have entered some bad value. Please check and try again.");
			}
			else {
				$window.alert(data);
			}
		});
	};
});