'use strict';

var ewallPortalUsers = angular.module('ewallPortalUsers', ['ui.router', 'ewallPortalHelpers',
    'ewallPortalApplications', 'ewallPortalRegions', 'ewallPortalConstants', 'ngResource', 'ewallPortalAuthentication',
    'ngLocalize', 'frapontillo.bootstrap-duallistbox']);

ewallPortalUsers.constant('USER_ROLES', {
  all: 'All',
  platformAdministrator: 'ADMINISTRATOR',
  regionAdministrator: 'REGION_ADMINISTRATOR',
  formalCaregiver: 'FORMAL_CAREGIVER',
  informalCaregiver: 'INFORMAL_CAREGIVER',
  primaryUser: 'PRIMARY_USER'
});

ewallPortalUsers.filter('role', ['$filter', 'USER_ROLES', function($filter, USER_ROLES) {
  return function(input, role) {
    if (role === USER_ROLES.all) return input;
    return $filter('filter')(input, {
      userRole: role
    });
  };
}]);

ewallPortalUsers.filter('region', ['$filter', function($filter) {
  return function(input, region) {
    if (region === 'all') return input;
    return $filter('filter')(input, {
      userProfile: {
        eWallSubProfile: {
          regionName: region
        }
      }
    });
  };
}]);

ewallPortalUsers.directive('passwordMatch', [function () {
  return {
    restrict: 'A',
    scope:true,
    require: 'ngModel',
    link: function (scope, elem, attrs, ctrl) {
    	var checker = function () {
            var e1 = scope.$eval(attrs.ngModel); 
            var e2 = scope.$eval(attrs.passwordMatch);
            return e1 == e2;
        };
        scope.$watch(checker, function (n) {
        	ctrl.$setValidity("matching", n);
        });
    }
  }
}]);



ewallPortalUsers.directive('userRole', ['USER_ROLES', 'locale', 'localeEvents',
    function(USER_ROLES, locale, localeEvents) {
      var rolesByValue = {};
      angular.forEach(USER_ROLES, function(value, key) {
        rolesByValue[value] = key;
      });
      function update(elm, value) {
        locale.ready('users').then(function() {
          elm.html(locale.getString('users.' + rolesByValue[value]));
        });
      }
      return {
        restrict: 'A',
        link: function(scope, elm, attrs, ctrl) {
          attrs.$observe('userRole', function(newVal, oldVal) {
            if (newVal && newVal != oldVal) {
              update(elm, newVal);
            }
          });
          scope.$on(localeEvents.localeChanges, function(event, data) {
            update(elm, attrs.userRole);
          });
        }
      };
    }]);

ewallPortalUsers.controller('UsersCtrl', [
  '$scope',
  '$rootScope',
    '$filter',
    'Users',
    'USER_ROLES',
    'Regions',
    'TableManager',
    'localeEvents',
    'AUTH_EVENTS',
    '$timeout',
    '$http',
    'REST_API',
    function($scope, $rootScope, $filter, Users, USER_ROLES, Regions, TableManager, localeEvents, AUTH_EVENTS, $timeout, $http, REST_API) {

      //console.log("user: %O", $scope.currentSession.user.userProfile.eWallSubProfile.regionName);
      $scope.query = "";
      $scope.showLoading();
      $rootScope.users = Users.query({}, function() {
        if (!$scope.isPlatformAdmin()) {
        	$rootScope.users = $.grep($rootScope.users, function(el) {
            return [USER_ROLES.platformAdministrator, USER_ROLES.regionAdministrator].indexOf(el.userRole) == -1;
          });
        	$rootScope.users.$resolved = true;
        }

     	  if($rootScope.users!=undefined){
  	          angular.forEach($rootScope.users, function(user) {
  	            $http.get(REST_API.users+user.username+'/heartbeat/isalive', {
  	              headers: {
  	                'x-auth-token':  $scope.currentSession.token,
  	                'Cache-Control': 'no-cache'
  	              }
  	            }).success(function(data) {
  	              	console.log(user.username + ": " + data.onlineStatus);
  	              	user.onlineStatus = data.onlineStatus;
  	            }).error(function(data){
  	            	console.log("Error in updating user status");
  	            });
	  	      });
         }

     	  $scope.tableParams = TableManager.generateTableParams($scope, $rootScope.users, 'query', 'username', function(
                orderedData) {
          orderedData = $filter('role')(orderedData, $scope.filterUserRole);
          orderedData = $filter('region')(orderedData, $scope.filterRegion);
          return orderedData;
        });
      });
      
     $scope.headerClass = function(name) {
        return TableManager.headerClass($scope.tableParams, name);
      };
      $scope.headerClick = function(name) {
        TableManager.headerClick($scope.tableParams, name);
      };

      function filterRoles(res) {
        if (!$scope.isPlatformAdmin()) { return $.grep(res, function(el) {
          return [USER_ROLES.platformAdministrator, USER_ROLES.regionAdministrator].indexOf(el.value) == -1;
        }); }
        return res;
      }
      $scope.roles = [];
      Users.getRoles(true).then(function(res) {
        $scope.roles = filterRoles(res);
        $scope.filterUserRole = USER_ROLES.all;
      });
      $scope.$on(localeEvents.localeChanges, function(event, data) {
        Users.getRoles(true).then(function(res) {
          var roles = filterRoles(res);
          for (var i = 0; i < $scope.roles.length; i++) {
            $scope.roles[i].lable = roles[i].lable;
          }
        });
      });

      $scope.filterRegion = 'all';
      $scope.regions = Regions.getLabels();

      $scope.remove = function() {
        Users.removeIt($scope);
      };
      $scope.showDeleteModal = function(user) {
        Users.showDeleteModal(user);
      };

      $scope.$watch('users.$resolved && regions.$resolved', function(newValue, oldValue) {
        if (newValue === true) {
          if (!$scope.isPlatformAdmin()) {
            $scope.filterRegion = $scope.currentSession.user.userProfile.eWallSubProfile.regionName;
          }
          $scope.hideLoading();
        }
      });

      $scope.$watch("filterUserRole", function(newvalue, oldvalue) {
        if ($scope.tableParams) $scope.tableParams.reload();
      });

      $scope.$watch("filterRegion", function(newvalue, oldvalue) {
        if ($scope.tableParams) $scope.tableParams.reload();
      });

      $scope.$watch("query", function(newvalue, oldvalue) {
        if ($scope.tableParams) $scope.tableParams.reload();
      });
    }]);

ewallPortalUsers.directive("fileread", [function () {
    return {
        scope: {
            fileread: "="
        },
        link: function (scope, element, attributes) {
            element.bind("change", function (changeEvent) {
                var reader = new FileReader();
                reader.onload = function (loadEvent) {
                    scope.$apply(function () {
                        scope.fileread = loadEvent.target.result;
                    });
                }
                reader.readAsDataURL(changeEvent.target.files[0]);
            });
        }
    }
}]);

ewallPortalUsers.controller('UserEditCtrl', [
    '$scope',
    '$state',
    '$stateParams',
    '$filter',
    'Users',
    'UserPhoto',
    'Credentials',
    'USER_ROLES',
    'Applications',
    'Utils',
    'Regions',
    'locale',
    'localeEvents',
    function($scope, $state, $stateParams, $filter, Users, UserPhoto, Credentials, USER_ROLES, Applications, Utils, Regions, locale,
            localeEvents) {
      if (!$stateParams.username){
    	  $scope.passLabel = 'users.enterPassword';
    	  $scope.showpassowrdfield = false;
    	  
      } else {
    	  $scope.passLabel = 'users.changePassword';
    	  $scope.showpassowrdfield = true;
      }
      $scope.showLoading();
      //console.log('EDIT USER form'+$stateParams.username);
      $scope.user = Users.defaultUser();
      $scope.caregivers = Users.getCaregivers();
      $scope.applications = Applications.query();
      $scope.regions = Regions.getLabels();
      if ($scope.currentSession.user.userProfile && $scope.currentSession.user.userProfile.eWallSubProfile) {
        $scope.userRegion = $scope.currentSession.user.userProfile.eWallSubProfile.regionName;
      }
      //$scope.user.userProfile.eWallSubProfile.regionName = $scope.currentSession.user.userProfile.eWallSubProfile.regionName;
      $scope.pwd = {};
      $scope.placeholder = '';
      $scope.pwd.password2 = $scope.placeholder;
      $scope.pwd.password1 = $scope.placeholder;
      $scope.healthImpairments = {};
  	  $scope.diagnosis = {};
      $scope.base64Image = {};
      $scope.ISOLanguages = getLocaleISOLanguages();

      $scope.editing = $stateParams.username;

      if ($stateParams.username) {
        UserPhoto.get({ username: $stateParams.username }, function (base64Image) {
          if (base64Image.mimeTypeStr && base64Image.base64EncodedImageStr) {
            $scope.base64ProfilePhoto = 'data:' + base64Image.mimeTypeStr + ';base64,' + base64Image.base64EncodedImageStr;
          }
        });
      }

      $scope.preparePhotoForUpload = function(file) {
        var reader = new FileReader();
        reader.onload = function (loadEvent) {
          $scope.base64Image.base64EncodedImageStr = loadEvent.target.result.match(/,(.*)$/)[1];
          $scope.base64Image.mimeTypeStr = file.type;
          $scope.base64Image.name = file.name;
        }
        reader.readAsDataURL(file);
        $scope.requestedRemovingPhoto = false;
      };

      $scope.removePhoto = function() {
        $scope.requestedRemovingPhoto = true;
        $scope.base64Image = {};
        $scope.base64ProfilePhoto = null;
      };
      
      function setUserTimezone() {
    	  if ($scope.user.$resolved === false) {
    		  return;
    	  }
    	  
    	  if ($scope.user.userProfile.vCardSubProfile) {
              $('#userTimeZoneDdl').change(function () {
            	  $scope.user.userProfile.vCardSubProfile.timezoneid = $('#userTimeZoneDdl').val();
              });
    	  }
    	  if ($scope.user.userProfile.vCardSubProfile && $scope.user.userProfile.vCardSubProfile.timezoneid) {
    		  $('#userTimeZoneDdl').val($scope.user.userProfile.vCardSubProfile.timezoneid);
    	  } 
      }
      
      
      
      function prepareUserHealthDiagnosis() {
    	  if (!$scope.user.userProfile) {
    		  return;
    	  }
    	  if (!$scope.user.userProfile.healthSubProfile) {
    		  $scope.user.userProfile.healthSubProfile = {
    				  healthMeasurements: {},
    				  healthImpairments: {},
    				  takesMedications: {},
    				  healthDiagnosisType: []
    		  };
    	  } else if (!$scope.user.userProfile.healthSubProfile.healthDiagnosisType) {
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType = [];
    	  }

    	  var arrayLength = $scope.user.userProfile.healthSubProfile.healthDiagnosisType.length;
    	  for (var i = 0; i < arrayLength; i++) {
    		  
    		  switch ($scope.user.userProfile.healthSubProfile.healthDiagnosisType[i]) {
				case 'MCI':
					$scope.diagnosis.isMci = true;
					break;
				case 'COPD_GOLD1':
					$scope.diagnosis.isCopd1 = true;
					break;
				case 'COPD_GOLD2':
					$scope.diagnosis.isCopd2 = true;
					break;
				case 'COPD_GOLD3':
					$scope.diagnosis.isCopd3 = true;
					break;
				case 'COPD_GOLD4':
					$scope.diagnosis.isCopd4 = true;
					break;
				case 'FRAIL':
					$scope.diagnosis.isFrail = true;
					break;
				case 'PRE_FRAIL':
					$scope.diagnosis.isPreFrail = true;
					break;
				case 'UNKNOWN':
					$scope.diagnosis.isUnknown = true;
					break;
				case 'OTHER':
					$scope.diagnosis.isOther = true;
					break;
				}

    	  }
      }
      
      
      function prepareUserHealthImpairments() {
    	  if (!$scope.user.userProfile) {
    		  return;
    	  }
    	  
    	  /* If objects inside User object are not set, define them */
    	  if (!$scope.user.userProfile.healthSubProfile) {
    		  $scope.user.userProfile.healthSubProfile = {
    				  healthMeasurements: {},
    				  healthImpairments: {},
    				  takesMedications: {},
    				  healthDiagnosisType: []
    		  };
    	  } else if (!$scope.user.userProfile.healthSubProfile.healthImpairments) {
    		  $scope.user.userProfile.healthSubProfile.healthImpairments = {};
    	  }
    	  
    	  /* Sensory related */
    	  var astigmatism = $scope.user.userProfile.healthSubProfile.healthImpairments.astigmatism;
        $scope.healthImpairments.astigmatismDdl = !astigmatism && astigmatism !== false ? "" : String(astigmatism);
        var colorBlindness = $scope.user.userProfile.healthSubProfile.healthImpairments.colorBlindness;
        $scope.healthImpairments.colorBlindnessDdl = !colorBlindness && colorBlindness !== false ? "" : String(colorBlindness);
        var farSightedness = $scope.user.userProfile.healthSubProfile.healthImpairments.farSightedness;
        $scope.healthImpairments.farSightednessDdl = !farSightedness && farSightedness !== false ? "" : String(farSightedness);
        var lightSensitivity = $scope.user.userProfile.healthSubProfile.healthImpairments.lightSensitivity; 
        $scope.healthImpairments.lightSensitivityDdl = !lightSensitivity && lightSensitivity !== false ? "" : String(lightSensitivity);
        var shortSightedness = $scope.user.userProfile.healthSubProfile.healthImpairments.shortSightedness; 
        $scope.healthImpairments.shortSightednessDdl = !shortSightedness && shortSightedness !== false ? "" : String(shortSightedness);
        var visionLoss = $scope.user.userProfile.healthSubProfile.healthImpairments.visionLoss;
        $scope.healthImpairments.visionLossDdl = !visionLoss && visionLoss !== false ? "" : String(visionLoss);
        var hearLoss = $scope.user.userProfile.healthSubProfile.healthImpairments.hearLoss;
        $scope.healthImpairments.hearLossDdl = !hearLoss && hearLoss !== false ? "" : String(hearLoss);

        /* Skeletel related */
        var neckMotorImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.neckMotorImpairment;
        $scope.healthImpairments.neckMotorImpairmentDdl = !neckMotorImpairment ? "" : neckMotorImpairment;
        var shoulderImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.shoulderImpairment;
        $scope.healthImpairments.shoulderImpairmentDdl = !shoulderImpairment ? "" : shoulderImpairment;
        var armImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.armImpairment;
        $scope.healthImpairments.armImpairmentDdl = !armImpairment ? "" : armImpairment;
        var wristImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.wristImpairment;
        $scope.healthImpairments.wristImpairmentDdl = !wristImpairment ? "" : wristImpairment;
        var hipImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.hipImpairment;
        $scope.healthImpairments.hipImpairmentDdl = !hipImpairment ? "" : hipImpairment;
        var kneeImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.kneeImpairment;
        $scope.healthImpairments.kneeImpairmentDdl = !kneeImpairment ? "" : kneeImpairment;
        var legImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.legImpairment;
        $scope.healthImpairments.legImpairmentDdl = !legImpairment ? "" : legImpairment;
        var anckleImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.anckleImpairment;
        $scope.healthImpairments.anckleImpairmentDdl = !anckleImpairment ? "" : anckleImpairment;
        var footImpairment = $scope.user.userProfile.healthSubProfile.healthImpairments.footImpairment;
        $scope.healthImpairments.footImpairmentDdl = !footImpairment ? "" : footImpairment;
  	  
        /* Language related */
        var dislexia = $scope.user.userProfile.healthSubProfile.healthImpairments.dislexia;
        $scope.healthImpairments.dislexiaDdl = !dislexia && dislexia !== false ? "" : String(dislexia);
  	  
        /* Memory related */
        var amnesia = $scope.user.userProfile.healthSubProfile.healthImpairments.amnesia;
        $scope.healthImpairments.amnesiaDdl = !amnesia && amnesia !== false ? "" : String(amnesia);
      }
      
      /* 
       * Intensities (enums, e.g. armImpairment) can be set to undefined;
       * Flags (e.g. astigmatism) can be only 'true' or 'false', so if user selects undefined 'false' will be set to property;
       * If user does not initialized object 'healthImpairments', it will be initialized with all values set to undefined
       * */
      function setUserHealthImpairments() {
    	  /* Sensory related */
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.astigmatism = ($scope.healthImpairments.astigmatismDdl === 'true');
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.colorBlindness = ($scope.healthImpairments.colorBlindnessDdl === 'true');
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.farSightedness = ($scope.healthImpairments.farSightednessDdl === 'true');
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.lightSensitivity = ($scope.healthImpairments.lightSensitivityDdl === 'true');
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.shortSightedness = ($scope.healthImpairments.shortSightednessDdl === 'true');
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.visionLoss = $scope.healthImpairments.visionLossDdl ? $scope.healthImpairments.visionLossDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.hearLoss = $scope.healthImpairments.hearLossDdl ? $scope.healthImpairments.hearLossDdl : null;
    	  
    	  /* Skeletel related */
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.neckMotorImpairment = $scope.healthImpairments.neckMotorImpairmentDdl ? $scope.healthImpairments.neckMotorImpairmentDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.shoulderImpairment = $scope.healthImpairments.shoulderImpairmentDdl ? $scope.healthImpairments.shoulderImpairmentDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.armImpairment = $scope.healthImpairments.armImpairmentDdl ? $scope.healthImpairments.armImpairmentDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.wristImpairment = $scope.healthImpairments.wristImpairmentDdl ? $scope.healthImpairments.wristImpairmentDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.hipImpairment = $scope.healthImpairments.hipImpairmentDdl ? $scope.healthImpairments.hipImpairmentDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.kneeImpairment = $scope.healthImpairments.kneeImpairmentDdl ? $scope.healthImpairments.kneeImpairmentDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.legImpairment = $scope.healthImpairments.legImpairmentDdl ? $scope.healthImpairments.legImpairmentDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.anckleImpairment = $scope.healthImpairments.anckleImpairmentDdl ? $scope.healthImpairments.anckleImpairmentDdl : null;
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.footImpairment = $scope.healthImpairments.footImpairmentDdl ? $scope.healthImpairments.footImpairmentDdl : null;
    	  
    	  /* Language related */
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.dislexia = ($scope.healthImpairments.dislexiaDdl === 'true');
    	  
    	  /* Memory related */
    	  $scope.user.userProfile.healthSubProfile.healthImpairments.amnesia = ($scope.healthImpairments.amnesiaDdl === 'true');
      }
      
      function setUserHealthDiagnosis() {
    	 
    	  $scope.user.userProfile.healthSubProfile.healthDiagnosisType=[];
   
    	  if ($scope.diagnosis.isMci) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("MCI");
    	  if ($scope.diagnosis.isCopd1) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("COPD_GOLD1");
    	  if ($scope.diagnosis.isCopd2) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("COPD_GOLD2");
    	  if ($scope.diagnosis.isCopd3) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("COPD_GOLD3");
    	  if ($scope.diagnosis.isCopd4) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("COPD_GOLD4");
    	  if ($scope.diagnosis.isFrail) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("FRAIL");
    	  if ($scope.diagnosis.isPreFrail) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("PRE_FRAIL");
    	  if ($scope.diagnosis.isOther) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("OTHER");
    	  if ($scope.diagnosis.isUnknown) 
    		  $scope.user.userProfile.healthSubProfile.healthDiagnosisType.push("UNKNOWN");
      }
      
      function setDuallistboxLocales() {
        Utils.setDuallistbox('caregivers');
        Utils.setDuallistbox('applications');
        locale.ready('users').then(
                function() {
                  $('#caregivers').bootstrapDualListbox('setSelectedListLabel',
                          locale.getString('users.assignedCaregivers'));
                  $('#caregivers').bootstrapDualListbox('setNonSelectedListLabel',
                          locale.getString('users.availableCaregivers'));
                  $('#applications').bootstrapDualListbox('setSelectedListLabel',
                          locale.getString('users.enabledApplications'));
                  $('#applications').bootstrapDualListbox('setNonSelectedListLabel',
                          locale.getString('users.availableApplications'));
                  $('#userTimeZoneDdl').timezones();
                  $('#userTimeZoneDdl').prepend('<option value="" default selected>' 
                		  + locale.getString('users.selectTimezone') + '</option>');
                  setUserTimezone();
                  prepareUserHealthImpairments();
                  prepareUserHealthDiagnosis();
                });
      }

      $scope.$watch('caregivers.$resolved && applications.$resolved', function(newValue, oldValue) {
        if (newValue === true && $stateParams.username) {
          $scope.user = Users.get({
            username: $stateParams.username
          }, function() {
            $scope.hideLoading();
            if ($scope.user.userProfile && $scope.user.userProfile.vCardSubProfile) {
              $scope.user.userProfile.vCardSubProfile.birthday = $filter('date')(
                $scope.user.userProfile.vCardSubProfile.birthday, 'yyyy-MM-dd');
              $('#inputBirthday').datepicker('update', $scope.user.userProfile.vCardSubProfile.birthday);

              setUserTimezone();
            }

            prepareUserHealthImpairments();
            prepareUserHealthDiagnosis();
          }, function() {
            locale.ready('common').then(function() {
              $scope.hideLoading();
              $scope.showError(locale.getString('common.dataError'));
            });
          });
        } else if (!$stateParams.username) {
          $scope.hideLoading();
        }

        $scope.caregiversUsernamesList = Utils.filterValues($scope.caregivers, 'username'); 
        $scope.applicationNamesList = Utils.filterValues($scope.applications, 'name'); 
        $scope.user.caregiversUsernamesList = Utils.filterList($scope.caregiversUsernamesList, $scope.user.caregiversUsernamesList);
        $scope.user.applicationNamesList = Utils.filterList($scope.applicationNamesList, $scope.user.applicationNamesList);

        setDuallistboxLocales();
      });
      $scope.$on(localeEvents.localeChanges, function(event, data) {
        setDuallistboxLocales();
      });
      
      $scope.$watch("user.userProfile.vCardSubProfile.birthday", function(newDate, oldDate) {
    	  if (!newDate) {
    		  return;
    	  }
    	  /* It depends on 'inputBirthday' datepicker format */
    	  $scope.user.userProfile.vCardSubProfile.age = moment().diff(new Date(newDate), 'years');
      });

      function filterRoles(res) {
        if (!$scope.isPlatformAdmin()) { return $.grep(res, function(el) {
          return [USER_ROLES.platformAdministrator, USER_ROLES.regionAdministrator].indexOf(el.value) == -1;
        }); }
        return res;
      }
      $scope.roles = [];
      Users.getRoles().then(function(res) {
        $scope.roles = filterRoles(res);
      });
      $scope.$on(localeEvents.localeChanges, function(event, data) {
        Users.getRoles().then(function(res) {
          var roles = filterRoles(res);
          for (var i = 0; i < $scope.roles.length; i++) {
            $scope.roles[i].lable = roles[i].lable;
          }
        });
      });

      $('#inputBirthday').datepicker({
        format: "yyyy-mm-dd"
      });

      function submitUserAndGetCredentials() {
        console.log('Checking credentials User'+$stateParams.username);
        Users.submit($scope, $scope.user, $stateParams.username);
        Credentials.get({'username':$stateParams.username},{}, function() {
            console.log('Credentials in input are '+$scope.pwd.password1);
            if ($scope.placeholder==$scope.pwd.password1){
              //console.log('Going to leave it empty as it has not been reset');
            } else {
              //console.log('Going to change it');
              Credentials.save({'username':$stateParams.username},$scope.pwd.password1);
            }
            
        }, function(response) {
          //console.log('Credentials Search returned an error response: '+$stateParams);
            if(response.status === 404) {
              console.log('Credentials Not Found');
            }
        });
      }

      $scope.submit = function() {
        if ($scope.user.userProfile.healthSubProfile) {
      	  setUserHealthImpairments();
      	  setUserHealthDiagnosis();
        }
    	  
        /* Editing user */
    	  if ($stateParams.username) {

          if ($scope.base64Image.base64EncodedImageStr) {
            UserPhoto.update({ username: $stateParams.username }, $scope.base64Image,
              function success() {
                submitUserAndGetCredentials();
              },
              function error() {
                locale.ready('users').then(function() {
                  $scope.showError(locale.getString('users.submitPhotoError'));
                });
              });

          } else if ($scope.requestedRemovingPhoto) { 
            UserPhoto.remove({ username: $stateParams.username },
              function success() {
                submitUserAndGetCredentials();
              },
              function error() {
                locale.ready('users').then(function() {
                  $scope.showError(locale.getString('users.removePhotoError'));
                });
              });

          } else {
            submitUserAndGetCredentials();
          }
        	
        /* Creating user */
        } else {

          if ($scope.base64Image.base64EncodedImageStr) {
            UserPhoto.update({ username: $scope.user.username }, $scope.base64Image,
              function success() {
                /* Creation of a new user ongoing so I have to setup its passoword */
                Users.submit($scope, $scope.user);
              },
              function error() {
                locale.ready('users').then(function() {
                  $scope.showError(locale.getString('users.submitPhotoError'));
                });
              });

          } else {
              /* Creation of a new user ongoing so I have to setup its passoword */
              Users.submit($scope, $scope.user);
          }

        }
      };
    }]);

ewallPortalUsers.factory('Users', [
    '$resource',
    'REST_API',
    'SubmitManager',
    'USER_ROLES',
    'TokenHeaderGenerator',
    'DeleteManager',
    'locale',
    function($resource, REST_API, SubmitManager, USER_ROLES, TokenHeaderGenerator, DeleteManager, locale) {
      var api = $resource(REST_API.users + ':username', {
        username: ''
      }, TokenHeaderGenerator.generateActions({
        'getCaregivers': {
          method: 'GET',
          isArray: true,
          transformResponse: function(data, headersGetter) {
            var users = angular.fromJson(data);
            var caregivers = $.grep(users, function(element) {
              return element.userRole === USER_ROLES.formalCaregiver
                      || element.userRole === USER_ROLES.informalCaregiver;
            });
            return caregivers;
          }
        },
        'getTargetUsers': {
            method: 'GET',
            isArray: true,
            transformResponse: function(data, headersGetter) {
              var users = angular.fromJson(data);
              var targetUsers = $.grep(users, function(element) {
                return element.userRole === USER_ROLES.primaryUser;
              });
              return targetUsers;
            }
          }
      }));

      api.getRoles = function(withAll) {
        return locale.ready('users').then(function() {
          var roles = $.map(Object.keys(USER_ROLES), function(element, i) {
            return {
              value: USER_ROLES[element],
              lable: locale.getString('users.' + element)
            };
          });
          if (withAll) {
            return roles;
          } else {
            return $.grep(roles, function(element) {
              return element.value != USER_ROLES.all;
            });
          }
        });
      };

      api.submit = function(scope, object, objectNameToUpdate) {
        SubmitManager.submit(api, scope, object, 'username', objectNameToUpdate, 'home.users');
      };

      api.removeIt = function(scope) {
        DeleteManager.remove(scope, scope.users, api, 'username', scope.tableParams);
      };
      api.showDeleteModal = function(user) {
        var usernameWithCascadeDeleteWarning = 'user ' + user.username + ' with associated sensing environment and devices';
        DeleteManager.showDeleteModal(user, user.username, usernameWithCascadeDeleteWarning);
      };

      api.defaultUser = function() {
        return {
          "firstName": "",
          "lastName": "",
          "username": "",
          "userProfile": {
            "healthSubProfile": {
            	"healthDiagnosisType": []
            },
            "userPreferencesSubProfile": {},
            "emotionalStatesSubProfile": {},
            "vCardSubProfile": {},
            "activitiesSubProfile": {},
            "eWallSubProfile": {},
            "psychologicalStateSubProfile": {}
          },
          "applicationNamesList": [],
          "caregiversUsernamesList": []
        };
      };

      return api;
    }]);

ewallPortalUsers.factory('Credentials', [
           '$resource',
           'REST_API',
           'SubmitManager',
           'USER_ROLES',
           'TokenHeaderGenerator',
           'DeleteManager',
           'locale',
           function($resource, REST_API, SubmitManager, USER_ROLES, TokenHeaderGenerator, DeleteManager, locale) {
             var api = $resource(REST_API.users + ':username/credentials', {
             }, TokenHeaderGenerator.generateActions({
            	 setCredentials: {
                 method: 'POST',
                 params: {username:'@username'}
                }
             }));
             return api;
           }]);

ewallPortalUsers.factory('UserPhoto', [
  '$resource',
  'REST_API',
  'TokenHeaderGenerator',
  function($resource, REST_API, TokenHeaderGenerator) {

      var api = $resource(REST_API.userPhoto, {
      }, TokenHeaderGenerator.generateActions({}));
      
      return api;
    }]);

