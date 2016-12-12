var sessionLanguage = ewallApp.currentSession.user.userProfile.userPreferencesSubProfile.systemPreferences.preferredLanguage;
var sessionUsername = ewallApp.currentSession.user.username;
var baseUrl='[protocol]//[hostname]/applications-[env]/service-brick-quiz-admin/v1/';
var resultUrl = '[protocol]//[hostname]/applications-[env]/service-brick-logicgames/v1/result/';

var hostname = window.location.hostname;
var pathname = window.location.pathname;
var protocol = window.location.protocol;
var env = "dev";

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

if(hostname == 'localhost') {
	baseUrl = "http://localhost/service-brick-quiz-admin/v1/";
	resultUrl = "http://localhost/service-brick-logicgames/v1/result/";
}
else {
	baseUrl = baseUrl.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
	resultUrl = resultUrl.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
}

var initStates = function () {
	ewallApp.stateProvider.state('main.snapPuzzle.welcome',{
		url:'/',
		templateUrl:'../snap-puzzle/views/main.html',
		controller: 'snapWelcomeCtrl',
		data: {
			authenticationRequired: true
		}
	}).state('main.snapPuzzle.game',{
		url: '/:level',
		templateUrl: '../snap-puzzle/views/game.html',
		controller: 'gameCtrl',
		params: {
			image: null
		},
		data: {
			authenticationRequired: true
		}
	}).state('main.snapPuzzle.end',{
		url: '/end',
		templateUrl: '../snap-puzzle/views/end.html',
		controller: 'endGameCtrl',
		params: { 
			time: null,
			moves: null 
		},
		data: {
			authenticationRequired: true
		}
	});
};

ewallApp.directive('httpSrc', ['$http', function($http){
	var directive={
		link: link,
		restrict: 'A'
	};
	return directive;

	function link(scope, element, attrs){
		var requestConfig={
			method:'GET',
			url: attrs.httpSrc,
			responseType: 'arraybuffer',
			headers: {
				'X-Auth-Token': ewallApp.currentSession.token
			}
		};

		$http(requestConfig)
			.success(function(data){
				var arr = new Uint8Array(data);
				var raw='';
				var i, j, subArray, chunk=5000;
				for(i=0, j=arr.length; i<j; i+=chunk){
					subArray = arr.subarray(i, i+chunk);
					raw += String.fromCharCode.apply(null,subArray);
				}
				var b64 = btoa(raw);
				attrs.$set('src', "data:image/jpeg;base64," + b64);
			});
}}]);

ewallApp.controller('snapWelcomeCtrl', function($scope, $state, snapTranslation) {
	$scope.items = [
	                baseUrl + 'all-users/images/city',
	                baseUrl + 'all-users/images/flower',
	                baseUrl + 'all-users/images/food',
	                baseUrl + 'all-users/images/water'
	                ];
    
	$scope.selected = '';
    
	ewallApp.get(baseUrl+sessionUsername+'/images',
			function(data) {
				//$scope.items = data;
				for(var i = 0; i < data.length; i++){
					$scope.items.push(data[i]);
				}
				$scope.labels = snapTranslation.getLabels(sessionLanguage);
				var stateAlreadyRegistered = _.find($state.get(), function (s) {
					return s.name == 'main.snapPuzzle.welcome';
				});
				if (!stateAlreadyRegistered) {
					initStates();
				}
				

			    $scope.selectPic = function($index){
			        $scope.selectedIndex = $index;
			        $scope.selected = $scope.items[$index];
			        var string = $scope.selected.split('/v1/');
			        $scope.imageName = string[string.length-1].toString();
			        console.log('Selected image: ' + $scope.imageName);
			    };
			    
			});
	 
});

ewallApp.controller('endGameCtrl', function($scope, $state, $stateParams, snapTranslation) {
	$scope.labels = snapTranslation.getLabels(sessionLanguage);
	$scope.time = $stateParams.time;
	$scope.moves = $stateParams.moves;
});

ewallApp.controller('gameCtrl', function($scope, $state, $stateParams, $timeout, hueNotificationService, snapTranslation){
	
	var timeDiff  =  
	{
		start:function (){
			d = new Date();
			time  = d.getTime();
		},
		end:function (){
			d = new Date();
			return (d.getTime()-time);
		}
	}
	
	var pCont = document.getElementById('puzzle-containment');
	var angScope = angular.element(pCont).scope();
	
	$scope.labels = snapTranslation.getLabels(sessionLanguage);
	$scope.moves = 0;
	$scope.image = baseUrl + $stateParams.image;
	$scope.resultId = '';
	
	$scope.startPuzzle = function(x){
		timeDiff.start();
		
		var data = {
				'gameName': 'snap-puzzle',
				'username': sessionUsername,
				'type': 'game',
				'level': $stateParams.level,
				'nrOfMoves': 0,
				'elapsedTimeSecs': 0,
				'nrOfLevels': 3,
				'minMoves': x * x, //
				'minElapsedTimeSecs': x*x + 2*x, //
				'completed': 'false'
				}
		
		uploadResults(resultUrl + 'snap-puzzle/' + sessionUsername, data);
		console.log('Game started; starting result is: %O', data);
		
        $('#puzzle_solved').hide();
        $('#source_image').snapPuzzle({
            rows: x, columns: x,
            pile: '#pile',
            containment: '#puzzle-containment',
            scope: angScope,
            onComplete: function(){
                $('#source_image').fadeOut(150).fadeIn();
                $('#puzzle_solved').show();
                $scope.finished = true;
                $scope.time = Math.ceil(timeDiff.end()/1000);
                
    			var endingData = {
    					'resultId': $scope.resultId,
    					'nrOfMoves': $scope.moves,
						'minMoves': x * x, //
						'minElapsedTimeSecs': x*x + 2*x, //
  					'elapsedTimeSecs': $scope.time,
    					'completed': 'true'
    					}
    			console.log('Uploading final results: %O', endingData);
    			uploadResults(resultUrl + $scope.resultId, endingData);
    			
    			hueNotificationService.throwNotification('green');
                
    			console.log("Finished with " + $scope.moves + " moves in " + $scope.time + " seconds");              
            	ewallApp.logEWallet('GAMES', 10);
            }
        });
    };
    
    $scope.size = 3;
    $scope.finished = false;
    
    $scope.gameInterrupted = function () {
    	console.log('Game interrupted');
    	hueNotificationService.throwNotification('red');
    	$state.go('main.snapPuzzle.welcome');
    };

    $scope.restartPuzzle = function () {
    	hueNotificationService.throwNotification('yellow');
    	console.log('Game restarted');
    	$scope.moves = 0;
        $('#source_image').snapPuzzle('destroy');
        $scope.startPuzzle($scope.size);
        $scope.finished = false;
    };

    $scope.init = function(){
        $('#pile').height($('#source_image').height());
        if($stateParams.level == 'easy'){
        	$scope.size = 3;
			console.log('Easy level');
		} else if($stateParams.level == 'medium'){
			$scope.size = 4;
			console.log('Medium level');
		}  else {
			$scope.size = 5;
			console.log('Hard level');
		}
        $scope.startPuzzle($scope.size);
    };
    
	var uploadResults = function (url, body) {
		ewallApp.ajax({
			method: 'POST',
			url: url,
			data: body,
			success: function (data) {
				if($scope.resultId === undefined || $scope.resultId === '')
					$scope.resultId = data.resultId;
			},
			error: function() {
				console.log('Something went wrong...');
			}
		})
	};

});
