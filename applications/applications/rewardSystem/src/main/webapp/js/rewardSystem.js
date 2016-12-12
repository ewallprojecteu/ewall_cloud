var sessionUsername = ewallApp.currentSession.user.username;
var language = ewallApp.currentSession.user.userProfile.userPreferencesSubProfile.systemPreferences.preferredLanguage;
console.log('User language: ', language);
var hostname = window.location.hostname;
var pathname = window.location.pathname;
var protocol = window.location.protocol;
var platformUrl = "[protocol]//[hostname]/platform-[env]/";
var applicationsUrl = "[protocol]//[hostname]/applications-[env]/";
var env = "local";
if(pathname.match(/-dev/g)) {
	env = "dev"
} else if(pathname.match(/-int-level-1/g)) {
	env = "int-level-1"
} else if(pathname.match(/-int-level-2/g)) {
	env = "int-level-2"
} else if(pathname.match(/-prod-aws/g)) {
	env = "prod-aws"
} else if(pathname.match(/-prod/g)) {
	env = "prod"
}
if (env != 'local') {
	platformUrl = platformUrl.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
	applicationsUrl = applicationsUrl.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
} else {
	platformUrl = "http://localhost/";
	applicationsUrl = "http://localhost/";
}
console.log(env + ": platformUrl=" + platformUrl + ", applicationsUrl=" + applicationsUrl);

var model = null;

var initStates = function () {
	ewallApp.stateProvider.state('main.rewardSystem.main',{
		url:'/',
		templateUrl:'../rewardSystem/views/main.html',
		controller: 'rewardSystemCtrl',
		data: {
			authenticationRequired: true
		}
	});
};

ewallApp.factory('ajaxCall', function() {
	return function(url, method, data, successCallback ) {
		try {
			ewallApp.ajax({
				url: url,
				method: method,
				data: JSON.stringify(data),
				contentType: 'application/json; charset=utf-8',
				dataType   : 'json',
				success: successCallback
			})
		} catch (service_call_err) {
			console.log('ERROR: ', service_call_err);
		}
	};
});

ewallApp.controller('rewardSystemCtrl', function($scope, $state, ajaxCall) {
	if (model == null) {
		ajaxCall(platformUrl + 'ewallet/getEWallet?username=' + sessionUsername, 'GET', {}, function(data) {
			model = data.value;
			model.currentCoins = Math.round(model.currentCoins * 10) / 10;
			model.currentCoins = model.currentCoins.toLocaleString(language);
			model.coinsFromActivity = Math.round(model.coinsFromActivity);
			model.coinsFromActivity = model.coinsFromActivity.toLocaleString(language);
			model.coinsFromUsage = Math.round(model.coinsFromUsage);
			model.coinsFromUsage = model.coinsFromUsage.toLocaleString(language);
			model.coinsFromSleep = Math.round(model.coinsFromSleep);
			model.coinsFromSleep = model.coinsFromSleep.toLocaleString(language);
			model.coinsFromExercise = Math.round(model.coinsFromExercise);
			model.coinsFromExercise = model.coinsFromExercise.toLocaleString(language);
			model.coinsFromGames = Math.round(model.coinsFromGames);
			model.coinsFromGames = model.coinsFromGames.toLocaleString(language);
			console.log('EWallet: ', model);
			$scope.$apply(function(){
				$scope.model = model;
			});
		});
	} else {
		$scope.model = model;
	}
	var stateAlreadyRegistered = _.find($state.get(), function (s) {
		return s.name == 'main.rewardSystem.main';
	});
	if (!stateAlreadyRegistered) {
		initStates();
	}
});
