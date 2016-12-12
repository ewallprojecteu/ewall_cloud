var sessionUsername = ewallApp.currentSession.user.username;
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
var patientProfileData = null;
var patientData = null;
var roomsData = null;
var stepData = {};

var initStates = function () {
	ewallApp.stateProvider.state('main.fallsPreventionChecklist.mainPie',{
		url:'/',
		templateUrl:'../fallPrevention/views/mainPie.html',
		controller: 'fallPreventionCtrl',
		data: {
			authenticationRequired: true
		}
	}).state('main.fallsPreventionChecklist.factor',{
		url: '/factor',
		templateUrl: '../fallPrevention/views/factor.html',
		controller: 'factorCtrl',
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

ewallApp.controller('fallPreventionCtrl', function($scope, $state, $http, ajaxCall) {
	if (model == null) {
		var language = ewallApp.currentSession.user.userProfile.userPreferencesSubProfile.systemPreferences.preferredLanguage;
		console.log('User language: ', language);
		language = language.toLowerCase();
		if (language != 'en' && language != 'dk' && language != 'it' && language != 'nl' && language != 'de') {
			language = 'en';
		}
		$http
			.get("../fallPrevention/model_" + language + ".json")
			.success(function (data) {
				model = data;
				model.username = sessionUsername;
				model.firstname = ewallApp.currentSession.user.firstName;
				model.selectedSlice = -1;
				$scope.model = model;

				//GET REQUESTs
				//User profile
				ajaxCall(platformUrl + 'profiling-server/users/' + sessionUsername, 'GET', {}, function(data) {
					patientData = data;
					patientProfileData = data.userProfile.healthSubProfile;
					console.log('Patient data: ', patientData);
					var slices = model.slices;

					//user medication-related risks
					if(!patientProfileData.takesMedications) {
						patientProfileData.takesMedications = {
							medicationsList: []
						};
					}
					if(patientProfileData.takesMedications.medicationsList.length > 4) {
						updateSlices(slices, 'Medication', 'polipharmacotherapy');
					}
					if (patientProfileData.takesMedications.medicationsList.indexOf("Drugs acting on the CNS") > -1 ||
						patientProfileData.takesMedications.medicationsList.indexOf("Analgesics and anti-inflammatory") > -1 ||
						patientProfileData.takesMedications.medicationsList.indexOf("Anti-hypertensive") > -1 ||
						patientProfileData.takesMedications.medicationsList.indexOf("Metabolics and endocrine drugs") > -1 ||
						patientProfileData.takesMedications.medicationsList.indexOf("Digoxin") > -1 ||
						patientProfileData.takesMedications.medicationsList.indexOf("Laxatives") > -1) {
						updateSlices(slices, 'Medication', 'frid');
					}

					//user vitals-related-risks - OK
					if(patientProfileData.hypertension) {
						updateSlices(slices, 'Vitals', 'hypertension');
					}
					if(patientProfileData.dizziness) {
						updateSlices(slices, 'Vitals', 'dizziness');
					}

					//user eyesight-related risks - OK
					var healthImpairs = patientProfileData.healthImpairments;
					if(healthImpairs.farSightedness) {
						updateSlices(slices, 'Eyesight', 'multifocal');
					}
					else {
						if (healthImpairs.colorBlindness || healthImpairs.lightSensitivity || (healthImpairs.visionLoss !== 'NONE') ) {
							updateSlices(slices, 'Eyesight', 'impairments');
						}
					}

					//user cognitive-related risks
					//Extrapyramidal symptoms
					if(patientProfileData.takesMedications.medicationsList.indexOf("Anti-psychotic or anti-depressant") > -1) {
						updateSlices(slices, 'Cognitive', 'extrapyramidal');
					}
					//Cognitive-related diagnosis: MCI
					if(patientProfileData.healthDiagnosisType.indexOf("MCI") > -1) {
						updateSlices(slices, 'Cognitive', 'cognitive');
					}
					//Fear of falling, social isolation
					if(patientData.userProfile.emotionalStatesSubProfile != null) {
						var emotianalArray = patientData.userProfile.emotionalStatesSubProfile.emotionalStateCategoriesSet;
						if(contains.call(emotianalArray, "FEAR_OF_FALLING")) {
							updateSlices(slices, 'Cognitive', 'fear');
						}
						if(contains.call(emotianalArray, "SOCIAL_AVOIDANCE")) {
							updateSlices(slices, 'Social', 'avoidance');
						}
					}

					//user feet-related risks
					if(healthImpairs.footProblemsList.indexOf("DISABLING_FOOT_PAIN") > -1) {
						updateSlices(slices, 'Feet', 'pain');
					}
					if(healthImpairs.footProblemsList.indexOf("DECREASED_ANKLE_FLEXIBILITY") > -1) {
						updateSlices(slices, 'Feet', 'ankle');
					}
					if(healthImpairs.footProblemsList.indexOf("HALLUX_VALGUS_SEVERE") > -1) {
						updateSlices(slices, 'Feet', 'hallux');
					}
					if(healthImpairs.footProblemsList.indexOf("DECREASED_PLANTAR_TACTILE_SENSITIVITY") > -1) {
						updateSlices(slices, 'Feet', 'tactile');
					}
					if(healthImpairs.footProblemsList.indexOf("DECREASED_TOE_PLANTAR_FLEXOR_STRENGTH") > -1) {
						updateSlices(slices, 'Feet', 'toe');
					}

					//Legs-related Risks
					if( patientProfileData.healthImpairments.kneeImpairment === 'MEDIUM'
					    || patientProfileData.healthImpairments.kneeImpairment === 'HIGH' ) {
						updateSlices(slices, 'Legs', 'knee');
					}
				});
				//User rooms
				ajaxCall(platformUrl + 'profiling-server/users/' + sessionUsername + '/rooms', 'GET', {}, function(data) {
					roomsData = data;
					console.log('Home data: ', roomsData);
					var slices = model.slices;
					if(roomsData[0].looseCarpet) {
						updateSlices(slices, 'Home', 'carpets');
					}
					if(roomsData[0].poorLight) {
						updateSlices(slices, 'Home', 'poorLight');
					}
					if(roomsData[0].slipperyFloor) {
						updateSlices(slices, 'Home', 'floors');
					}
					if(roomsData[0].stairsExist) {
						updateSlices(slices, 'Home', 'stairs');
					}
				});
				//Physical activity service brick: getMovement
				var d1 = new Date();
				d1 = new Date(d1.getFullYear(), d1.getMonth(), d1.getDate(), 0, 0, 0, 0);
				var d2 = new Date(d1.getTime() - 15*24*3600000);
				var tzo = -d1.getTimezoneOffset();
				var dif = tzo >= 0 ? '%2B' : '%2D';
				var pad = function(num) {
						var norm = Math.abs(Math.floor(num));
						return (norm < 10 ? '0' : '') + norm;
					};
				d1 = d1.getFullYear() + '-' + pad(d1.getMonth() + 1) + '-' + pad(d1.getDate()) + 'T00:00:00.000' + dif + pad(tzo / 60)	+ ':' + pad(tzo % 60);
				d2 = d2.getFullYear() + '-' + pad(d2.getMonth() + 1) + '-' + pad(d2.getDate()) + 'T00:00:00.000' + dif + pad(tzo / 60)	+ ':' + pad(tzo % 60);
				model.slices[7].constituents[0].danger = false;
				model.slices[7].constituents[0].inactiveText = model.fitbitnodata;
				ajaxCall(applicationsUrl + 'service-brick-physicalactivity/v1/' + sessionUsername +
					'/movement?from=' + d2 + '&to=' + d1 + '&aggregation=1d', 'GET', {}, function(data) {
					console.log('Activity data: ', data.movements);
					stepData.days = data.movements.length;
					if (stepData.days > 0) {
						var avgSteps = 0;
						for (var i = 0; i < stepData.days; i++) {
							avgSteps += data.movements[i].steps;
						}
						avgSteps /= stepData.days;
						stepData.avgSteps = avgSteps;
						console.log('Average steps: ', stepData.avgSteps);
						model.slices[7].constituents[0].inactiveText = model.fitbitok;
						//Step activity
						if (stepData.avgSteps < 10000) {
							model.slices[7].constituents[0].danger = true;
							i = model.slices[7].constituents[0].activeText.indexOf("XXX");
							model.slices[7].constituents[0].activeText = model.slices[7].constituents[0].activeText.substring(0, i) +
								Math.round(stepData.avgSteps).toLocaleString() + model.slices[7].constituents[0].activeText.substring(i+3);
							i = model.slices[7].constituents[0].activeText.indexOf("YYY");
							model.slices[7].constituents[0].activeText = model.slices[7].constituents[0].activeText.substring(0, i) +
								stepData.days + model.slices[7].constituents[0].activeText.substring(i+3);
						}
						else {
							model.slices[7].constituents[0].danger = false;
							i = model.slices[7].constituents[0].inactiveText.indexOf("XXX");
							model.slices[7].constituents[0].inactiveText = model.slices[7].constituents[0].inactiveText.substring(0, i) +
								Math.round(stepData.avgSteps).toLocaleString() + model.slices[7].constituents[0].inactiveText.substring(i+3);
							i = model.slices[7].constituents[0].inactiveText.indexOf("YYY");
							model.slices[7].constituents[0].inactiveText = model.slices[7].constituents[0].inactiveText.substring(0, i) +
								stepData.days + model.slices[7].constituents[0].inactiveText.substring(i+3);
						}
					} else {
						model.slices[7].constituents[0].danger = false;
						model.slices[7].constituents[0].inactiveText = model.fitbitnosteps;
					}
					$scope.$apply(function(){
						$scope.model = model;
					});
				});
			})
			.error(function (error) {
				$scope.error = error;
			});
	} else {
		$scope.model = model;
	}
	var stateAlreadyRegistered = _.find($state.get(), function (s) {
		return s.name == 'main.fallsPreventionChecklist.mainPie';
	});
	if (!stateAlreadyRegistered) {
		initStates();
	}
});

ewallApp.controller('factorCtrl', function($scope, ajaxCall){
	$scope.model = model;
	console.log('$SCOPE IN FACTORCTRL: ', $scope);

	$scope.calculateDanger = function(index) {
		var danger = false;
		var constituents = $scope.model.slices[index].constituents;
		for (var i = 0; i < constituents.length; i++) {
			if (constituents[i].danger) {
				danger = true;
				break;
			}
		}
		$scope.model.slices[model.selectedSlice].danger = danger;
		//make data object to send back to server
		updateUserData(model, patientData, roomsData);

		console.log('model: ', model);
		console.log('patientData: ', patientData);
		console.log('roomsData: ', roomsData);

		//POST REQUEST back to server To save the results
		ajaxCall(platformUrl + 'profiling-server/users/' + sessionUsername, 'POST', patientData, function(data) {
			$scope.$apply();  // in Angular, you need to force rendering in asynchronous functions
		});
		ajaxCall(platformUrl + 'profiling-server/users/' + sessionUsername + '/rooms', 'POST', roomsData[0], function(data) {
			$scope.$apply();  // in Angular, you need to force rendering in asynchronous functions
		});
	};
	$scope.toggleDanger = function(index, cidx) {
		$scope.model.slices[index].constituents[cidx].danger = !$scope.model.slices[index].constituents[cidx].danger;
	};
});

function updateSlices(slices, slice, constituent) {
	for(var i=0; i<slices.length; i++) {
		if(slices[i].id !== slice) {
			continue;
		}
		else {
			if(constituent) {
				for(var j=0; j < slices[i].constituents.length; j++) {
					if(slices[i].constituents[j].id === constituent) {
						slices[i].constituents[j].danger = true;
					}
				}
			} else {
				for(j=0; j < slices[i].constituents.length; j++) {
					slices[i].constituents[j].danger = true;
				}
			}
			slices[i].danger = true;
		}
	}
}

function updateUserData(model, userData, roomsData) {
	var data = model.slices[model.selectedSlice].constituents;
	var index;
	var emotionProf = userData.userProfile.emotionalStatesSubProfile;

	switch(model.selectedSlice) {
		case 0: // Home
			roomsData[0].looseCarpet = data[0].danger;
			roomsData[0].slipperyFloor = data[1].danger;
			roomsData[0].stairsExist = data[2].danger;
			roomsData[0].poorLight = data[3].danger;
			break;

		case 1: // Medication - OK (only doctors)
			break;

		case 2: // Vitals
			//Orthostatic hypertension: From doctor
			//Dizziness:
			userData.userProfile.healthSubProfile.dizziness = data[1].danger;
			break;

		case 3: // Eyesight - OK (only doctors)
			break;

		case 4: // Cognitive
			for(var i=0; i<data.length; i++) {
				if(data[i].mode !== "user") {
					continue;
				}
				if(data[i].id === "fear") {
					//Fear of falling
					if(data[i].danger) {
						if(emotionProf) {
							if(!contains(emotionProf.emotionalStateCategoriesSet), "FEAR_OF_FALLING") {
								emotionProf.emotionalStateCategoriesSet.push("FEAR_OF_FALLING");
							}
						}
						else {
							userData.userProfile.emotionalStatesSubProfile = {
								emotionalStateCategoriesSet: ["FEAR_OF_FALLING"]
							};
						}
					}
					else {
						if(emotionProf) {
							if(contains(emotionProf.emotionalStateCategoriesSet), "FEAR_OF_FALLING") {
								index = emotionProf.emotionalStateCategoriesSet.indexOf("FEAR_OF_FALLING");
								if (index > -1) {
									emotionProf.emotionalStateCategoriesSet.splice(index, 1);
								}
							}
						}
						else {
							userData.userProfile.emotionalStatesSubProfile = {
								emotionalStateCategoriesSet: []
							};
						}
					}
				}
				if(data[i].id === "extrapyramidal") {
					//Extrapyramidal symptoms
					var healthProf = userData.userProfile.healthSubProfile.takesMedications;
					if(data[i].danger) {
						if(healthProf) {
							if(!contains(healthProf.medicationsList), "Anti-psychotic or anti-depressant") {
								healthProf.medicationsList.push("Anti-psychotic or anti-depressant");
							}
						}
						else {
							userData.userProfile.healthSubProfile.takesMedications = {
								medicationsList: ["Anti-psychotic or anti-depressant"]
							};
						}
					}
					else {
						if(healthProf) {
							if(contains(healthProf.medicationsList), "Anti-psychotic or anti-depressant") {
								index = healthProf.medicationsList.indexOf("Anti-psychotic or anti-depressant");
								if (index > -1) {
									healthProf.medicationsList.splice(index, 1);
								}
							}
						}
						else {
							userData.userProfile.healthSubProfile.takesMedications = {
								medicationsList: []
							};
						}
					}
				}
			}
			break;

		case 5: // Feet
			//Disabling foot pain - the rest are entered by the doctors
			if (data[0].danger) {
				if (userData.userProfile.healthSubProfile.healthImpairments.footProblemsList.indexOf("DISABLING_FOOT_PAIN") == -1) {
					userData.userProfile.healthSubProfile.healthImpairments.footProblemsList.push("DISABLING_FOOT_PAIN");
				}
			}
			else {
				index = userData.userProfile.healthSubProfile.healthImpairments.footProblemsList.indexOf("DISABLING_FOOT_PAIN");
				if (index > -1) {
					userData.userProfile.healthSubProfile.healthImpairments.footProblemsList.splice(index, 1);
				}
			}
			break;

		case 6: // Social
			//Social avoidance
			if(data[0].danger) {
				if(emotionProf) {
					if(!contains(emotionProf.emotionalStateCategoriesSet), "SOCIAL_AVOIDANCE") {
						emotionProf.emotionalStateCategoriesSet.push("SOCIAL_AVOIDANCE");
					}
				}
				else {
					userData.userProfile.emotionalStatesSubProfile = {
						emotionalStateCategoriesSet: ["SOCIAL_AVOIDANCE"]
					};
				}
			}
			else {
				if(emotionProf) {
					if(contains(emotionProf.emotionalStateCategoriesSet), "SOCIAL_AVOIDANCE") {
						index = emotionProf.emotionalStateCategoriesSet.indexOf("SOCIAL_AVOIDANCE");
						if (index > -1) {
							emotionProf.emotionalStateCategoriesSet.splice(index, 1);
						}
					}
				}
				else {
					userData.userProfile.emotionalStatesSubProfile = {
						emotionalStateCategoriesSet: []
					};
				}
			}
			break;

		case 7: // Legs
			//Step activity: From Fitbit
			//Knee impairment
			if(data[1].danger) {
				userData.userProfile.healthSubProfile.healthImpairments.kneeImpairment = "HIGH";
			} else {
				userData.userProfile.healthSubProfile.healthImpairments.kneeImpairment = "LOW";
			}
			break;
	}
}

var contains = function(needle) {
	// Per spec, the way to identify NaN is that it is not equal to itself
	var findNaN = needle !== needle;
	var indexOf;

	if(!findNaN && typeof Array.prototype.indexOf === 'function') {
		indexOf = Array.prototype.indexOf;
	} else {
		indexOf = function(needle) {
			var i = -1, index = -1;

			for(i = 0; i < this.length; i++) {
				var item = this[i];

				if((findNaN && item !== item) || item === needle) {
					index = i;
					break;
				}
			}

			return index;
		};
	}

	return indexOf.call(this, needle) > -1;
};
