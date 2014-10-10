'use strict';

var app = angular.module('tigerWebApp', [
		 'ngAnimate',
		 'ngCookies',
		 'ngResource',
		 'ngSanitize',
		 'ngRoute',
		 'ngQuickDate',
		 'nvd3ChartDirectives'
]);
                                          

// This configures the routes and associates each route with a view and a
// controller
app.config(function($routeProvider) {
	$routeProvider
		.when('/dashboard', {
			controller : 'DashboardController',
			templateUrl : 'app/views/dashboard.html'
	})
	.when('/collecting-units', {
		controller : 'CollectingUnitsController',
		templateUrl : 'app/views/collectingUnits.html'
	})
	.when('/data', {
		controller : 'DataController',
		templateUrl : 'app/views/data.html'
	})
	.when('/add-collecting-unit', {
		controller : 'AddCollectingUnitController',
		templateUrl : 'app/views/addCollectingUnit.html'
	})
	.when('/collecting-units/archived', {
		controller : 'ArchivedCollectingUnitsController',
		templateUrl : 'app/views/archivedCollectingUnits.html'
	})
	.when('/collecting-unit/:id', {
		controller : 'EditCollectingUnitController',
		templateUrl : 'app/views/editCollectingUnit.html'
	})
	.when('/users', {
		controller : 'UsersController',
		templateUrl : 'app/views/users.html'
	})
	.when('/add-user', {
		controller : 'AddUserController',
		templateUrl : 'app/views/addUser.html'
	})
	.when('/edit-user/:id', {
		controller : 'EditUserController',
		templateUrl : 'app/views/editUser.html'
	})
	.otherwise({
		redirectTo : '/dashboard'
	});
});