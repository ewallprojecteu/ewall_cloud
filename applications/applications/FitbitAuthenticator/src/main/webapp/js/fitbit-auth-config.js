'use strict';
var FitbitAuthConfig = { 
	'LocationData': {
		'protocol': window.location.protocol, 
		'hostname': window.location.host, 
		'pathname': window.location.pathname
	},
	'ValidAdminRoleAndCheckRegion': {'REGION_ADMINISTRATOR':true, 'ADMINISTRATOR':false}, 
	'UserSelectionUserRole': ['PRIMARY_USER'], 
	'RenewAuthTokenIntervalMS': 60*1000, /*60 sec*/ 
	'CheckPopupLocationIntervalMS': 500
};