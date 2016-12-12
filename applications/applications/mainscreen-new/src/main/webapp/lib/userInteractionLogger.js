// for localhost testing
/* 
var serverAddress = "http://localhost/user-interaction-logger/logInteraction";
*/

// Uncomment before deployment
var serverAddress = "[protocol]//[hostname]/platform-[env]/user-interaction-logger/logInteraction";

var parseRelativeURL = function (serverAddress) {
  var hostname = window.location.hostname;
  var pathname = window.location.pathname;
  var protocol = window.location.protocol;
  
  var env = "";
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

  return serverAddress.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
};
/* */

var sendInteractionToEWALL = function (placeOfInteraction, sourceOfInteraction, comment) {
     try {
            var this_username = ewallApp.currentSession.user.username;            
            var dateString = moment().format();
               
            ewallApp.ajax({
                // for deployment
                /**/
                url: parseRelativeURL(serverAddress),                
                 
                //for localhost
                /*
                url: serverAddress,
                */
                
                type: "POST",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                },
                data: {
                    userid: this_username,
                    applicationName: placeOfInteraction,
                    buttonId: sourceOfInteraction,
                    comment: comment,
                    timestamp: dateString
                },
                /* dataType: "application/x-www-form-urlencoded", */
                async: true,
                success: {}
            })
        } catch (ui_err) {
            console.log(ui_err);
        }
};

var UILoggerEWALL = function () {
   
   
    this.sendInteraction = function (placeOfInteraction, sourceOfInteraction) {
       sendInteractionToEWALL(placeOfInteraction, sourceOfInteraction, "");
    };
   
     this.sendInteractionWithComment = function (placeOfInteraction, sourceOfInteraction, comment) {
        sendInteractionToEWALL(placeOfInteraction, sourceOfInteraction, comment);         
    };
}