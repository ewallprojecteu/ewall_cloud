var sessionUsername = ewallApp.currentSession.user.username;
var sessionLanguage = ewallApp.currentSession.user.userProfile.userPreferencesSubProfile.systemPreferences.preferredLanguage;
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

if(hostname == 'localhost') 
	resultUrl = "http://localhost:8080/service-brick-logicgames/v1/result/";
else 
	resultUrl = resultUrl.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);

var initStates = function () {
	ewallApp.stateProvider.state('main.sudoku.welcome',{
		url:'/',
		templateUrl:'../sudoku/views/main.html',
		controller: 'sudokuWelcomeCtrl',
		data: {
			authenticationRequired: true
		}
	}).state('main.sudoku.game',{
		url: '/:level',
		templateUrl: '../sudoku/views/game.html',
		controller: 'gameCtrl',
		data: {
			authenticationRequired: true
		}
	}).state('main.sudoku.end',{
		url: '/end',
		templateUrl: '../sudoku/views/end.html',
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

ewallApp.directive("sudokuSquare", function($parse) {
    return {
        restrict: 'A',
        link: function(scope, $element, attrs) {
            var index = attrs.sudokuSquare;
            var row = Math.floor(index / 9);
            var col = index % 9;

            var topRow = row === 0;
            var leftCol = col === 0;
            var thickBottom = row === 2 || row === 5 || row === 8;
            var thickRight = col === 2 || col === 5 || col === 8;
            var value = scope.mask[index];
            $element.addClass("sudoku-square");
            if (value != 0) $element.addClass("hint");
            if (topRow) $element.addClass("sd-top");
            if (leftCol) $element.addClass("sd-left");
            if (thickBottom) $element.addClass("sd-thick-btm");
            if (thickRight) $element.addClass("sd-thick-right");
            if (value == 0) $element.addClass("blank");
        }
    }
});

ewallApp.controller('sudokuWelcomeCtrl', function($scope, $state, sudokuTranslation) {
	$scope.labels = sudokuTranslation.getLabels(sessionLanguage);
	$scope.username = ewallApp.currentSession.user.username;
	var stateAlreadyRegistered = _.find($state.get(), function (s) {
		return s.name == 'main.sudoku.welcome';
	});
	if (!stateAlreadyRegistered) {
		initStates();
	}
});

ewallApp.controller('endGameCtrl', function($scope, $state, $stateParams, sudokuTranslation) {
	$scope.labels = sudokuTranslation.getLabels(sessionLanguage);
	$scope.time = $stateParams.time;
	$scope.moves = $stateParams.moves;
	
});

ewallApp.controller('gameCtrl', function($scope, $state, $stateParams, $timeout, hueNotificationService, sudokuTranslation){
	$scope.labels = sudokuTranslation.getLabels(sessionLanguage);
	$scope.finished = 0;
	$scope.moves = -1;
	$scope.cellStyle = new Array(81);

	$scope.newGame = function() {
		timeDiff.start();
		
		$scope.surrender = false;
		$scope.digits = [{value: 0, text: '-'}, 1, 2, 3, 4, 5, 6, 7, 8, 9];
		$scope.sudoku = new Array(81);
		$scope.mask = new Array(81);
		$scope.hints = new Array(81);
		$scope.minMoves = 0;
		$scope.resultId = '';
		
		shuffle($scope.sudoku);
		
		if($stateParams.level == 'easy'){
			maskBoardEasy($scope.sudoku, $scope.mask);
			console.log('Easy level');
		} else if($stateParams.level == 'medium'){
			maskBoard($scope.sudoku, $scope.mask);
			for(i = 0; i < 12; i++)
			{
				do {
					var cell = Math.floor(Math.random() * 81);
				}
				while($scope.mask[cell] != 0);
				$scope.mask[cell] = $scope.sudoku[cell];
			}
			console.log('Medium level');
		} else {
			maskBoard($scope.sudoku, $scope.mask);
			console.log('Hard level');
		}

		for(var i = 0; i < 81; i++){
			if($scope.mask[i] != 0) 
				$scope.hints[i] = 1;
			else { 
				$scope.hints[i] = 0;
				$scope.minMoves++;
				}
		};
		
		
		var data = {
			'gameName': 'sudoku',
			'username': sessionUsername,
			'type': 'game',
			'level': $stateParams.level,
			'nrOfMoves': 0,
			'elapsedTimeSecs': 0,
			'nrOfLevels': 3,
			'minMoves': $scope.minMoves,
			'minElapsedTimeSecs': 20 * $scope.minMoves,
			'completed': 'false'
			}
	
		uploadResults(resultUrl + 'sudoku/' + sessionUsername, data);
		console.log('Game started; starting result is: %O', data);
		console.log('Full sudoku is: %O', $scope.sudoku);
	};
	

	$scope.$watchCollection('mask', function(newSudoku, oldSudoku){
		$scope.moves ++;
		//console.log('Current sudoku state ' + $scope.mask);
		if($scope.moves > 0){
			var data = {
					'resultId': $scope.resultId,
					'nrOfMoves': $scope.moves,
					'minMoves': $scope.minMoves,
					'minElapsedTimeSecs': 20 * $scope.minMoves,
					'elapsedTimeSecs': Math.ceil(timeDiff.end()/1000-3),
					'completed': 'false'
					}
			console.log('Uploading partial results: %O', data);
			uploadResults(resultUrl + $scope.resultId, data);
		}
		for(var i = 0; i < newSudoku.length; i++) {
			if(newSudoku[i] != oldSudoku[i]) {
				var row = Math.floor(i / 9);
				var col = i%9;
				console.log('Changed ' + oldSudoku[i] + ' to ' + newSudoku[i] + ' at row: ' + row + '; col: '+ col);
				console.log('Total moves: ' + $scope.moves);
				if($scope.moves > 0) {
					var isWrong = showErrors(row, col, newSudoku[i], i);
					if (isWrong == false) {
						$scope.cellStyle[i] = {color:'black'};
						console.log('It could be correct...');
					}
				}
				break;
			}
		}
		$scope.finished = checkGame();
		if($scope.finished != 0 && $scope.surrender == false){
			console.log("Game finished after " + $scope.finished + " seconds!");
			
			var data = {
					'resultId': $scope.resultId,
					'nrOfMoves': $scope.moves,
					'minMoves': $scope.minMoves,
					'minElapsedTimeSecs': 20 * $scope.minMoves,
					'elapsedTimeSecs': $scope.finished,
					'completed': 'true'
					}
			console.log('Uploading final results: %O', data);
			uploadResults(resultUrl + $scope.resultId, data);
			
			ewallApp.logEWallet('GAMES', 10);
			
			hueNotificationService.throwNotification('green');

			$timeout(function(){
				$state.go("main.sudoku.end", {time: $scope.finished, moves: $scope.moves});
			}, 2000);
		}
	});

	$scope.solve = function () {
		$scope.surrender = true;
		for (var i = 0; i < 81; i++) {
			if(($scope.mask[i] == $scope.sudoku[i]) && ($scope.hints[i] == 0)) 
				$scope.cellStyle[i] = {color:'green'};
			else 
				if(($scope.mask[i] != $scope.sudoku[i]) && ($scope.mask[i] != 0)) 
					$scope.cellStyle[i] = {color:'red'};
				else
					$scope.cellStyle[i] = {color:'black'};
			$scope.mask[i] = $scope.sudoku[i];
		}
		hueNotificationService.throwNotification('red');
		console.log('Solved sudoku is... '+ $scope.mask);
	};
	
	var timeDiff  =  {
			start:function (){
				d = new Date();
				time  = d.getTime();
			},
			end:function (){
				d = new Date();
				return (d.getTime()-time);
			}
		}
	
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

	var checkGame = function(){
		for(var i = 0; i < 9; i++)
		{
			for(var j = 0; j < 9; j++)
			{
				var val = $scope.mask[i * 9 + j];
				if((val == 0) || (showErrors(i, j, val, i*9+j) == true))
					return 0;
			}
		}

		return (Math.ceil(timeDiff.end()/1000)-3);
	}
	
	var clear = function(arr) {
        var i = arr.length;
        while (i--) {
            arr[i] = 0;
        }
    };
    
	var shuffle = function (matrix) {
		for (var i = 0; i < 9; i++)
			for (var j = 0; j < 9; j++)
				matrix[i * 9 + j] = (i * 3 + Math.floor(i / 3) + j) % 9 + 1;

		for (var i = 0; i < 42; i++) {
			var n1 = Math.ceil(Math.random() * 9);
			var n2;
			do {
				n2 = Math.ceil(Math.random() * 9);
			}
			while (n1 == n2);

			for (var row = 0; row < 9; row++) {
				for (var col = 0; col < col; k++) {
					if (matrix[row * 9 + col] == n1)
						matrix[row * 9 + col] = n2;
					else if (matrix[row * 9 + col] == n2)
						matrix[row * 9 + col] = n1;
				}
			}
		}

		for (var c = 0; c < 42; c++) {
			var s1 = Math.floor(Math.random() * 3);
			var s2 = Math.floor(Math.random() * 3);

			for (var row = 0; row < 9; row++) {
				var tmp = matrix[row * 9 + (s1 * 3 + c % 3)];
				matrix[row * 9 + (s1 * 3 + c % 3)] = matrix[row * 9 + (s2 * 3 + c % 3)];
				matrix[row * 9 + (s2 * 3 + c % 3)] = tmp;
			}
		}

		for (var s = 0; s < 42; s++) {
			var c1 = Math.floor(Math.random() * 3);
			var c2 = Math.floor(Math.random() * 3);

			for (var row = 0; row < 9; row++) {
				var tmp = matrix[row * 9 + (s % 3 * 3 + c1)];
				matrix[row * 9 + (s % 3 * 3 + c1)] = matrix[row * 9 + (s % 3 * 3 + c2)];
				matrix[row * 9 + (s % 3 * 3 + c2)] = tmp;
			}
		}

		for (var s = 0; s < 42; s++) {
			var r1 = Math.floor(Math.random() * 3);
			var r2 = Math.floor(Math.random() * 3);

			for (var col = 0; col < 9; col++) {
				var tmp = matrix[(s % 3 * 3 + r1) * 9 + col];
				matrix[(s % 3 * 3 + r1) * 9 + col] = matrix[(s % 3 * 3 + r2) * 9 + col];
				matrix[(s % 3 * 3 + r2) * 9 + col] = tmp;
			}
		}
	};
	
	var maskBoardEasy = function (matrix, mask) {
		var i, j, k;
		for (i = 0; i < 81; i++)
			mask[i] = matrix[i];
		for (var i = 0; i < 3; i++) {
			for (j = 0; j < 3; j++) {
				for (k = 0; k < 4; k++) {
					var c;
					do {
						c = Math.floor(Math.random() * 9);
					}
					while (mask[(i * 3 + Math.floor(c / 3)) * 9 + j * 3 + c % 3] == 0);
					mask[(i * 3 + Math.floor(c / 3)) * 9 + j * 3 + c % 3] = 0;
				}
			}
		}
	};

	var maskBoard = function(matrix, mask) {
		var i, j, k, r, c, n = 0, a, hints = 0, cell, val;
		var avail = new Array(9);
		clear(avail);
		var tried = new Array(81);
		clear(tried);
		clear(mask);
		do
		{
			do
			{
				cell = Math.floor(Math.random() * 81);
			}
			while((mask[cell] != 0) || (tried[cell] != 0));
			val = matrix[cell];

			i = getAvailable(mask, cell, null);
			if(i > 1)
			{
				var cnt, row = Math.floor(cell / 9), col = cell % 9;

				cnt = 0;
				for(i = 0; i < 9; i++)
				{
					if(i == col)
						continue;

					j = row * 9 + i;
					if(mask[j] > 0)
						continue;
					a = getAvailable(mask, j, avail);

					for(j = 0; j < a; j++)
					{
						if(avail[j] == val)
						{
							cnt++;
							break;
						}
						avail[j] = 0;
					}
				}
				if(cnt > 0)
				{
					cnt = 0;
					for(i = 0; i < 9; i++)
					{
						if(i == row)
							continue;
						j = i * 9 + col;
						if(mask[j] > 0)
							continue;
						a = getAvailable(mask, j, avail);
						for(j = 0; j < a; j++)
						{
							if(avail[j] == val)
							{
								cnt++;
								break;
							}
							avail[j] = 0;
						}
					}
					if(cnt > 0)
					{
						cnt = 0;
						r = row - row % 3;
						c = col - col % 3;
						for(i = r; i < r + 3; i++)
						{
							for(j = c; j < c + 3; j++)
							{
								if((i == row) && (j == col))
									continue;

								k = i * 9 + j;
								if(mask[k] > 0)
									continue;
								a = getAvailable(mask, k, avail);
								for(k = 0; k < a; k++)
								{
									if(avail[k] == val)
									{
										cnt++;
										break;
									}
									avail[k] = 0;
								}
							}
						}

						if(cnt > 0)
						{
							mask[cell] = val;
							hints++;
						}
					}
				}
			}

			tried[cell] = 1;
			n++;
		}
		while(n < 81);

		do
		{
			do
			{
				cell = Math.floor(Math.random() * 81);
			}
			while((mask[cell] == 0) || (tried[cell] == 0));

			val = mask[cell];

			var t = this;
			var solutions = 0;

			mask[cell] = 0;
			solutions = enumSolutions(mask);

			if(solutions > 1)
				mask[cell] = val;

			tried[cell] = 0;
			hints--;
		}
		while(hints > 0);
	};

	var enumSolutions = function(matrix) {
		var i, j, ret = 0;
		var cell = getCell(matrix);

		if(cell == -1)
			return 1;

		var avail = new Array(9);
		clear(avail);

		j = getAvailable(matrix, cell, avail);
		for(i = 0; i < j; i++)
		{
			matrix[cell] = avail[i];
			ret += enumSolutions(matrix);
			if(ret > 1)
				break;
		}

		matrix[cell] = 0;
		return ret;
	};

	var getAvailable = function(matrix, cell, avail) {
		var i, j, row, col, r, c;
		var arr = new Array(9);
		clear(arr);

		row = Math.floor(cell / 9);
		col = cell % 9;

		for(i = 0; i < 9; i++)
		{
			j = row * 9 + i;
			if(matrix[j] > 0)
				arr[matrix[j] - 1] = 1;
		}

		for(i = 0; i < 9; i++)
		{
			j = i * 9 + col;
			if(matrix[j] > 0)
			{
				arr[matrix[j] - 1] = 1;
			}
		}

		r = row - row % 3;
		c = col - col % 3;
		for(i = r; i < r + 3; i++)
			for(j = c; j < c + 3; j++)
				if(matrix[i * 9 + j] > 0)
					arr[matrix[i * 9 + j] - 1] = 1;

		j = 0;
		if(avail != null)
		{
			for(i = 0; i < 9; i++)
				if(arr[i] == 0)
					avail[j++] = i + 1;
		}
		else
		{
			for(i = 0; i < 9; i++)
				if(arr[i] == 0)
					j++;
			return j;
		}

		if(j == 0)
			return 0;

		for(i = 0; i < 18; i++)
		{
			r = Math.floor(Math.random() * j);
			c = Math.floor(Math.random() * j);
			row = avail[r];
			avail[r] = avail[c];
			avail[c] = row;
		}

		return j;
	};

	var getCell = function(matrix){
		var cell = -1, n = 10, i, j;
		var avail = new Array(9);
		clear(avail);

		for(i = 0; i < 81; i++)
		{
			if(matrix[i] == 0)
			{
				j = getAvailable(matrix, i, null);

				if(j < n)
				{
					n = j;
					cell = i;
				}

				if (n == 1)
					break;
			}
		}

		return cell;
	};

	var showErrors = function(row, col, val, index) {
		if(val!=0){
		//row
		for (var i = 0; i < 9; i++) {
			if(i != col && $scope.mask[row*9 + i] == val){
				console.log(val + ' is already present at row ' + row + '; col ' + i);
				$scope.cellStyle[index] = {color:'red'};
				return true;
			}
		}
		//col
		for (var i = 0; i < 9; i++) {
			if(i != row && $scope.mask[i*9 + col] == val){
				console.log(val + ' is already present at row ' + i + '; col ' + col);
				$scope.cellStyle[index] = {color:'red'};
				return true;
			}
		}
		//block
		var r = row - row % 3;
		var c = col - col % 3;
		for(var i = r; i < r + 3; i++)
			for(var j = c; j < c + 3; j++)
				if(((i != row) || (j != col)) && ($scope.mask[i * 9 + j] == val)) {
					console.log(val + ' is already present in this block!');
					$scope.cellStyle[index] = {color:'red'};
					return true;
				}
		}
		return false;
	};
});
