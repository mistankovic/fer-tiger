'use strict';

app.controller('AddUserController', function($scope, $location, $window, userService) {
	$scope.user = {};
	$scope.allRoles = [];
	$scope.passwordConfirmation = null;
	
	init();
	
	function init() {
		$scope.user.email = null;
		$scope.user.firstName = null;
		$scope.user.lastName = null;
		$scope.user.password = null;
		$scope.user.roles = null;
		
		userService.getAllRoles(function(data) {
			var allRoles = data;
			for (var int = 0; int < allRoles.length; int++) {
				allRoles[int].checked = true;
			}
			
			$scope.allRoles = allRoles;
		});
	}
	
	$scope.save = function(){
		if($scope.user.password == null || $scope.user.password !== $scope.passwordConfirmation){
			$window.alert("Password not entered or does not match with repeated. Please enter again.");
			$scope.passwordConfirmation = null;
			$scope.user.password = null;
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
		
		userService.save($scope.user, function(data) {
			$location.path('/users');
		}, function(status, data) {
			if(status == 400){
				$window.alert('Please check if email is unique and is all data filled. Saving failed with message:\n' + data);
			}
			else{
				$window.alert(data);
			}
		});
	};
});