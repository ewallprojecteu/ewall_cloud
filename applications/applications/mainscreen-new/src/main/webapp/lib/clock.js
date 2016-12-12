// clock JS functionality
var local_date_for_clock_widget_options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
function setTime(){
	var currentTime = new Date(),
		hour = currentTime.getHours(),
		minute = currentTime.getMinutes(),
		second = currentTime.getSeconds(),
		hourDeg = (360/12)*hour,
		minuteDeg = (360/60)*minute,
		secondDeg = (360/60)*second;
        

	$('#hour').css('transform', 'rotate('+hourDeg+'deg)');

	$('#minute').css('transform', 'rotate('+minuteDeg+'deg)');

	$('#second').css('transform', 'rotate('+secondDeg+'deg)');
  
  if(minute<10) {
      $('#digital-clock').html(hour+":0"+minute);
  } else {
    $('#digital-clock').html(hour+":"+minute);
  }
    
   $('#date-clock').html(currentTime.toLocaleDateString(ewallApp.preferedLanguage, local_date_for_clock_widget_options));
}

setInterval(function(){
	setTime();
}, 1000);

function clockWidgetToPassive(element) {
var $jq = $;
    $jq(element).css("z-index", "1010");
    $jq(element).css("background", "rgba(255, 255, 255, 0.7)");
};

function clockWidgetToActive(element) {
var $jq = $;
     $jq(element).css("z-index", "901");
    $jq(element).css("background", "#C2CED4");
};


