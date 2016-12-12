/* Extracted from fusioner-youtube-application-controller.js */
	  
fusionerYoutubeApplication.extractPreparingEnvironmentController = function ($scope, hostname, pathname, protocol, REST_API) {
    var env = "";

    if (pathname.match(/-dev/g)) {
        env = "dev"
    } else if (pathname.match(/-int-level-1/g)) {
        env = "int-level-1"
    } else if (pathname.match(/-int-level-2/g)) {
        env = "int-level-2"
    } else if (pathname.match(/-prod-aws/g)) {
        env = "prod-aws"
    } else if (pathname.match(/-prod/g)) {
        env = "prod"
    } 

    REST_API.LOGIN_ADMIN = REST_API.LOGIN_ADMIN.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.TOKEN_UPDATE = REST_API.TOKEN_UPDATE.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.PROFILING_SERVER = REST_API.PROFILING_SERVER.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.FUSIONER_YOUTUBE = REST_API.FUSIONER_YOUTUBE.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.REDIRECT_URI_GOOGLE = REST_API.REDIRECT_URI_GOOGLE.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.OPEN_AUTH = REST_API.OPEN_AUTH.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
}
