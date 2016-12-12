var ewallRecommendationNotification = angular.module("ewallRecommendationNotification", []);

var addList = [
    {
        id: 'SLEEP_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-2-sleep-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-2-sleep-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-2-sleep-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-2-sleep-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-2-sleep-IT.jpg'
        }
    },
    {
        id: 'COGNITIVE_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-4-cognitive-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-4-cognitive-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-4-cognitive-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-4-cognitive-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-4-cognitive-IT.jpg'
        }
    },
    {
        id: 'BIKE_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-1-bike-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-1-bike-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-1-bike-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-1-bike-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-1-bike-IT.jpg'
        }
    },
    {
        id: 'VIDEO_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-3-video-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-3-video-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-3-video-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-3-video-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-3-video-IT.jpg'
        }
    },
    {
        id: 'BREAKFAST_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-5-breakfast-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-5-breakfast-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-5-breakfast-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-5-breakfast-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-5-breakfast-IT.jpg'
        }
    },
     {
        id: 'MUSIC_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-6-music-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-6-music-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-6-music-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-6-music-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-6-music-IT.jpg'
        }
    },
     {
        id: 'SOCIAL_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-7-social-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-7-social-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-7-social-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-7-social-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-7-social-IT.jpg'
        }
    },
    {
        id: 'BOOK_ACTIVITY_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-8-book-activity-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-8-book-activity-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-8-book-activity-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-8-book-activity-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-8-book-activity-IT.jpg'
        }
    },
    {
        id: 'BOOK_SLEEP_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-9-book-sleep-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-9-book-sleep-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-9-book-sleep-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-9-book-sleep-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-9-book-sleep-IT.jpg'
        }
    },
    {
        id: 'BOOK_HEALTH_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-10-book-health-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-10-book-health-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-10-book-health-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-10-book-health-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-10-book-health-IT.jpg'
        }
    },
    {
        id: 'BOOK_DAY_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-11-book-day-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-11-book-day-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-11-book-day-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-11-book-day-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-11-book-day-IT.jpg'
        }
    },
    {
        id: 'BOOK_HELP_AD',
        src: {
            'en': 'notifications/recommendation/assets/EN/ad-12-book-help-EN.jpg',
            'nl': 'notifications/recommendation/assets/NL/ad-12-book-help-NL.jpg',
            'de': 'notifications/recommendation/assets/AT/ad-12-book-help-DE.jpg',
            'da': 'notifications/recommendation/assets/DA/ad-12-book-help-DA.jpg',
            'it': 'notifications/recommendation/assets/IT/ad-12-book-help-IT.jpg'
        }
    },
    {
        id: 'NONE',
        src: {
            'en': 'notifications/recommendation/assets/nosignal.jpg',
            'nl': 'notifications/recommendation/assets/nosignal.jpg',
            'de': 'notifications/recommendation/assets/nosignal.jpg',
            'da': 'notifications/recommendation/assets/nosignal.jpg',
            'it': 'notifications/recommendation/assets/nosignal.jpg'
        }
    }
    /* If this advertisment list needs to be extendede, use the template below
    {
        id: '',
        src: {
            'en': 'notifications/recommendation/assets/EN/.jpg',
            'nl': 'notifications/recommendation/assets/NL/.jpg',
            'de': 'notifications/recommendation/assets/AT/.jpg',
            'da': 'notifications/recommendation/assets/DA/.jpg',
            'it': 'notifications/recommendation/assets/IT/.jpg'
        }
    },
    */
    
];

ewallRecommendationNotification.controller('ewallRecommendationNotificationController', ['$scope', '$stateParams', '$interval', 'REST_API', function ($scope, $stateParams, $interval, REST_API) {


    $scope.currentAddSrc = "";

	var myRandom = Math.random();
	console.log("Loaded ewallRecommendationNotificationController #####" + myRandom);

	var setAdvertisment = function (src) {
    	console.log("Setting advertisement #####" + myRandom);
       $scope.currentAddSrc = "background-image: url(" + src + ")";
        console.log($scope.currentAddSrc);
    }
    
    var stopRecommendationNotifications = function (promise) {
     	console.log("Stopping recommendationNotificationLoop...");
        $interval.cancel(promise);
    }  

    var getAdvertisment = function() {        
            
            //New impolementation based on the IDSS wellbeing advertisments service            
            // get the user prefered language
            var preferedLanguage = ewallApp.preferedLanguage;
            // get the username
            var username = ewallApp.currentSession.user.username;
            
            //IDSS wellbeing advertisments service url
            var idssWellbeingAddsURL = REST_API.wellbeing_ads + username + '/get';
            
            //request a new advertisment
            ewallApp.ajax({ 
             url: idssWellbeingAddsURL,                
             type: "GET",
             beforeSend: function (xhr) {
                 xhr.setRequestHeader("Content-type", "text/plain");
             },
             success: function (data) {
                 if(data){
                     var newAdd = addList.filter(function(add){
                         return add.id === data;
                     });                    
                     setAdvertisment(newAdd[0].src[preferedLanguage]);
                } else {
                    setAdvertisment('notifications/recommendation/assets/nosignal.jpg');
                }
             }
             });      
            
    }
    // when the document is loaded
    angular.element(document).ready(function () {
        getAdvertisment();
        ewallApp.currentSession.recommendationNotificationLoop = $interval(getAdvertisment, 29999);        
        ewallApp.currentSession.stopRecommendationNotifications = stopRecommendationNotifications; 
    });

}]);