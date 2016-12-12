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

var initStates = function () {
	ewallApp.stateProvider.state('main.myKitchenManager.main',{
		url:'/',
		templateUrl:'../myKitchenManager/views/main.html',
		controller: 'myKitchenManagerCtrl',
		data: {
			authenticationRequired: true
		}
	});
};

ewallApp.factory('ajaxCall', function() {
	return function(url, method, data, successCallback, errorCallback ) {
		try {
			ewallApp.ajax({
				url: url,
				method: method,
				data: JSON.stringify(data),
				contentType: 'application/json; charset=utf-8',
				dataType   : 'json',
				success: successCallback,
                error: errorCallback
			})
		} catch (service_call_err) {
			console.log('ERROR: ', service_call_err);
		}
	};
});

ewallApp.controller('myKitchenManagerCtrl', function($scope, $state, $http, ajaxCall) {
    console.log('Reading JSON data');
	$http
		.get("../myKitchenManager/model.json")
		.success(function (data) {
			$scope.model = data;
            $http
                .get("../myKitchenManager/ingredients.json")
                .success(function (data) {
                    $scope.ingredients = data;
                    $http
                        .get("../myKitchenManager/recipes.json")
                        .success(function (data) {
                            $scope.recipes = data.recipes;
                            $scope.resetRecipes();
                            console.log('Get fridge: ' + platformUrl + 'ekitchen/fridge?username=' + sessionUsername);
                            ajaxCall(platformUrl + 'ekitchen/fridge?username=' + sessionUsername, 'GET', {}, function(data) {
								if (data != null) {
									data = data.inFridge;
									if (data != '' && data != null) {
										data = data.split(',');
										for (var i = 0; i < data.length; i++) {
											data[i] = parseInt(data[i], 10); // Explicitly include base
										}
										$scope.$apply(function () {
											$scope.model.inFridge = data;
										});
									}
								}
								ajaxCall(platformUrl + 'ekitchen/pantry?username=' + sessionUsername, 'GET', {}, function(data) {
									if (data != null) {
										data = data.inPantry;
										if (data != '' && data != null) {
											data = data.split(',');
											for (var i = 0; i < data.length; i++) {
												data[i] = parseInt(data[i], 10); // Explicitly include base
											}
											$scope.$apply(function () {
												$scope.model.inPantry = data;
											});
										}
									}
									ajaxCall(platformUrl + 'ekitchen/shopping?username=' + sessionUsername, 'GET', {}, function(data) {
										if (data != null) {
											data = data.inShopping;
											if (data != '' && data != null) {
												data = data.split(',');
												for (var i = 0; i < data.length; i++) {
													data[i] = parseInt(data[i], 10); // Explicitly include base
												}
												$scope.$apply(function () {
													$scope.model.inList = data;
												});
											}
										}
										console.log($scope);
									});
								});
                            });
                        })
                        .error(function (error) {
                            $scope.error = error;
                        });
                })
                .error(function (error) {
                    $scope.error = error;
                });
		})
		.error(function (error) {
			$scope.error = error;
		});

	$scope.resetRecipes = function () {
		$scope.model.selectedRecipes = [];
		for (var i = 0; i < $scope.recipes.length; i++)
			$scope.model.selectedRecipes.push(i);
		$scope.model.currentRecipe = $scope.model.selectedRecipes[0];
	};

	$scope.toggleFridge = function (id) {
		var idx = $scope.model.inFridge.indexOf(id);
		if (idx == -1)
			$scope.model.inFridge.push(id);
		else
			$scope.model.inFridge.splice(idx, 1);
		var ingredients = $scope.model.inFridge.join(',');
		ajaxCall(platformUrl + 'ekitchen/fridge?username=' + sessionUsername + '&ingredients=' + ingredients, 'POST', {});
	};

	$scope.togglePantry = function (id) {
		var idx = $scope.model.inPantry.indexOf(id);
		if (idx == -1)
			$scope.model.inPantry.push(id);
		else
			$scope.model.inPantry.splice(idx, 1);
		var ingredients = $scope.model.inPantry.join(',');
		ajaxCall(platformUrl + 'ekitchen/pantry?username=' + sessionUsername + '&ingredients=' + ingredients, 'POST', {});
	};

	$scope.toggleSearch = function (id) {
		var idx = $scope.model.forSearch.indexOf(id);
		if (idx == -1)
			$scope.model.forSearch.push(id);
		else
			$scope.model.forSearch.splice(idx, 1);
	};

	$scope.toggleList = function (id, action) {
		var ingredients;
		var idx = $scope.model.inList.indexOf(id);
		if (idx == -1)
			$scope.model.inList.push(id);
		else {
			$scope.model.inList.splice(idx, 1);
			if (action == 1 && $scope.model.inFridge.indexOf(id) == -1) {
				$scope.model.inFridge.push(id);
				ingredients = $scope.model.inFridge.join(',');
				ajaxCall(platformUrl + 'ekitchen/fridge?username=' + sessionUsername + '&ingredients=' + ingredients, 'POST', {});
			}
			else if (action == 2 && $scope.model.inPantry.indexOf(id) == -1) {
				$scope.model.inPantry.push(id);
				ingredients = $scope.model.inPantry.join(',');
				ajaxCall(platformUrl + 'ekitchen/pantry?username=' + sessionUsername + '&ingredients=' + ingredients, 'POST', {});
			}
		}
		ingredients = $scope.model.inList.join(',');
		ajaxCall(platformUrl + 'ekitchen/shopping?username=' + sessionUsername + '&ingredients=' + ingredients, 'POST', {});
	};

	$scope.addToList = function (id) {
		var idx = $scope.model.inList.indexOf(id);
		if (idx == -1)
			$scope.model.inList.push(id);
		var ingredients = $scope.model.inList.join(',');
		ajaxCall(platformUrl + 'ekitchen/shopping?username=' + sessionUsername + '&ingredients=' + ingredients, 'POST', {});
	};

	$scope.searchForRecipes = function () {
		$scope.model.selectedRecipes = [];
		$scope.model.currentRecipe = 0;
		var matchedIngredients = [];
		var matchedNumber = [];
		var keys = $scope.model.forSearch;
		for (var r = 0; r < $scope.recipes.length; r++) {
			matchedIngredients[r] = [];
			matchedNumber[r] = 0;
			var ingredients = $scope.recipes[r].extendedIngredients;
			for (var i = 0; i < ingredients.length; i++) {
				for (var k = 0; k < keys.length; k++) {
					if (keys[k] == ingredients[i].id) {
						matchedIngredients[r].push(keys[k]);
						matchedNumber[r]++;
					}
				}
			}
		}
		sortWithIndices(matchedNumber);
		console.log(matchedNumber);
		for (r = 0; r < $scope.recipes.length; r++) {
			if (matchedNumber[r] > 0) {
				$scope.model.selectedRecipes.push(matchedNumber.sortIndices[r]);
			}
		}
		console.log('Selected recipes: ' + $scope.model.selectedRecipes);
	};

	function sortWithIndices(toSort) {
		for (var i = 0; i < toSort.length; i++) {
			toSort[i] = [toSort[i], i];
		}
		toSort.sort(function(left, right) {
			return left[0] > right[0] ? -1 : 1;
		});
		toSort.sortIndices = [];
		for (var j = 0; j < toSort.length; j++) {
			toSort.sortIndices.push(toSort[j][1]);
			toSort[j] = toSort[j][0];
		}
		return toSort;
	}

	var stateAlreadyRegistered = _.find($state.get(), function (s) {
		return s.name == 'main.myKitchenManager.main';
	});

	if (!stateAlreadyRegistered) {
		initStates();
	}
});
