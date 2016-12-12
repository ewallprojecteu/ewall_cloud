 var $jq = $;
 var weatherJSON = {
     id: "2756071",
     dt: "1408706225",
     name: "Enschede",
     cod: 200,
     coord: {
         lat: 52.22,
         lon: 6.9
     },
     sys: {
         message: 0.1018,
         country: "NL",
         sunrise: 1408681693,
         sunset: 1408732930
     },
     main: {
         temp: 20.17,
         humidity: 63,
         temp_min: 16.67,
         temp_max: 18.9,
         pressure: 1010,
         sea_level: 0,
         grnd_level: 0
     },
     wind: {
         speed: 5.1,
         deg: 210.0,
         gust: 0.0
     },
     clouds: {
         all: 75
     },
     weather: [
         {
             id: 803,
             main: "Clouds",
             description: "broken clouds",
             icon: "http://openweathermap.org/img/w/04d.png"
                }
                ]
 };
// the full description of all codes is presented here: http://openweathermap.org/weather-conditions
 var weatherAnimations = [
     { 
       weatherCode: "01d",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-clear.mp4"
     },
     { 
       weatherCode: "01n",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-clear.mp4"
     },
     { 
       weatherCode: "02d",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-clear.mp4"
     },
     { 
       weatherCode: "02n",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-clear.mp4"
     },
      { 
       weatherCode: "03d",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-cloudy.mp4"
     },
     { 
       weatherCode: "03n",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-cloudy.mp4"
     },
      { 
       weatherCode: "04d",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-cloudy.mp4"
     },
     { 
       weatherCode: "04n",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-cloudy.mp4"
     },
     { 
       weatherCode: "09d",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-rainy.mp4"
     },
     { 
       weatherCode: "09n",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-rainy.mp4"
     },
     { 
       weatherCode: "10d",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-rainy.mp4"
     },
     { 
       weatherCode: "10n",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-rainy.mp4"
     },
     { 
       weatherCode: "11d",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-rainy.mp4"
     },
     { 
       weatherCode: "11n",
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-rainy.mp4"
     },
     { 
       weatherCode: "13d", // to do: replace with propper animation
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-rainy.mp4"
     },
     { 
       weatherCode: "13n", // to do: replace with propper animation
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-rainy.mp4"
     },
     { 
       weatherCode: "50d", // to do: replace with propper animation
       weatherAnimationSrc: "widgets/weather/assets/1-fall-day-cloudy.mp4"
     },
     { 
       weatherCode: "50n", // to do: replace with propper animation
       weatherAnimationSrc: "widgets/weather/assets/1-fall-night-cloudy.mp4"
     }     
 ];
      
 
 function getWeatherData(serviceBrickURL, $scope) {
     try {

         ewallApp.ajax({
             url: serviceBrickURL,
             type: "GET",
             beforeSend: function (xhr) {
                 xhr.setRequestHeader("Content-type", "text/plain");
             },
             success: function (data) {
                 if (data) {
                     weatherJSON = data;
                     var tomorrow = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
                    // console.log("tomorrow is: " + tomorrow.getDay());
                     var tomorrow_icon_index =0;
                     var sum_temp = 0;
                     var sum_elements = 0;
                     var tomorrw_avg_temp = 0;
                     for(i=0; i<weatherJSON.list.length; i++){
                        var element_date = new Date(weatherJSON.list[i].dt_txt); 
                        //console.log("element["+i+"] is: " + element_date.getDay());
                         if(tomorrow.getDay() == element_date.getDay())
                         {
                             if(sum_elements == 4) {
                                tomorrow_icon_index = i;
                             }; 
                             sum_temp += weatherJSON.list[i].main.temp;
                             sum_elements += 1;
                             
                         
                         }
                     }
                    // console.log("weatherJSON");
                    // console.log(weatherJSON);
                    
                     if(sum_temp) {
                     tomorrw_avg_temp = sum_temp/8
                     };
                     $scope.weather = weatherJSON;                     
                     $scope.weather.tomorrow_avg_temp = tomorrw_avg_temp;
                     $scope.weather.tomorrow_icon = weatherJSON.list[tomorrow_icon_index].weather[0].icon;
                     for(i in weatherAnimations) {
                                            
                         if(weatherAnimations[i].weatherCode == weatherJSON.list[0].weather[0].icon)
                             {                            
                             $scope.weather.animationSrc = weatherAnimations[i].weatherAnimationSrc;
                             console.log(weatherAnimations[i].weatherAnimationSrc);
                             }
                     }
                     $scope.$apply();
                     
                 }
             }
         });
        
     } catch (weatherServiceERR) {
         console.log(weatherServiceERR);
     }

 };

 // make angular module
 var weatherW = angular.module("ewallWeather", []);

 //assign angular controller to module
 weatherW.controller('WeatherWidgetController', ['$scope', '$stateParams', '$interval', function ($scope, $stateParams, $interval) {
     var username = ewallApp.currentSession.user.username;
     var preferedLanguage = ewallApp.preferedLanguage; //get the prefered language from the query string
     var city = ewallApp.currentSession.user.userProfile.vCardSubProfile.city; // get the current city from the query string
     var serviceBrickURL = "";
     if (city) {
           serviceBrickURL = "../service-brick-weather/forecast?userid=" + username + "&lang=" + preferedLanguage + "&city=" + city + "&unit=metric";

     } else {
           serviceBrickURL = "../service-brick-weather/forecast?userid=" + username + "&lang=" + preferedLanguage + "&unit=metric"; 
     };
     $scope.Math = window.Math;
     
     getWeatherData(serviceBrickURL, $scope);
     
     var stopRecurringWeatherRequest = function (promise) {
     	console.log("Stopping recurringWeatherRequest loop...");
         $interval.cancel(promise);
     }

     ewallApp.currentSession.stopRecurringWeatherRequest = stopRecurringWeatherRequest;
     
          
     ewallApp.currentSession.recurringWeatherRequest = $interval(function() {
         getWeatherData(serviceBrickURL, $scope);
     }, 300000);
}]);
