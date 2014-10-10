'use strict';

app.service('utilsService', function(){
	this.formatDate = function(timestamp){
		return d3.time.format('%Y-%m-%d %H:%M:%S')(new Date(timestamp));
	};
	
	this.formatForServer = function(date) {
		return d3.time.format('%Y-%m-%d %H:%M:%S')(date);
	}
});