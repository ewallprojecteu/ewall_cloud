/* Extracted from flickr-portal-application-controller.js */
	  
flickrPortalApplication.extractPreparingEnvironmentController = function ($scope, hostname, pathname, protocol, REST_API) {
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
    REST_API.USERS = REST_API.USERS.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.OPEN_AUTH_FLICKR = REST_API.OPEN_AUTH_FLICKR.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.REDIRECT_URI_FLICKR = REST_API.REDIRECT_URI_FLICKR.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.FLICKR_AUTH_URL = REST_API.FLICKR_AUTH_URL.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.CREDENTIALS = REST_API.CREDENTIALS.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.FUSIONER_FLICKR = REST_API.FUSIONER_FLICKR.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.OAUTH = REST_API.OAUTH.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    REST_API.PHOTOS = REST_API.PHOTOS.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
}
