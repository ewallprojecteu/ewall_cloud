var backgroundSwitch = angular.module('ewallBackgroundSwitch', []);
backgroundSwitch.controller('backgroundSwitchController', ['$scope', '$interval', function ($scope, $interval) {
    /*
    $scope.unlockedOptions = [
       'WALLPAPER_ZERO_LEAVES',
       'WALLPAPER_ONE_SIXTIES',
       'WALLPAPER_TWO_STRIPES',
       'WALLPAPER_THREE_BLUE',
       'WALLPAPER_FOUR_YELLOW'
       
    ]; */
    
    var userProfileLookup = function () {
           // get the set of unlocked background from the user profile
            $scope.unlockedOptions = [];
            $scope.unlockedOptions = $scope.unlockedOptions.concat(ewallApp.currentSession.user.userProfile.eWallSubProfile.rewardsList);  
            console.log('++++ looking for changes in the user options');
    };
    
    var stopRepeatUserProfileLookup = function (promise) {
    	console.log("Stopping repeatUserProfileLookup...");
        $interval.cancel(promise);
    }

    ewallApp.currentSession.stopRepeatUserProfileLookup = stopRepeatUserProfileLookup;
    
    ewallApp.currentSession.repeatUserProfileLookup = $interval(
        function () {
            userProfileLookup();
            $scope.$applyAsync();
            
        }, 15*1000);
   
    userProfileLookup();
}]);