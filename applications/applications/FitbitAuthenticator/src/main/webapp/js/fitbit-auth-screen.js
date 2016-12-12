function FitbitAuthScreen() {
	var REST_API = {
		LOGIN_ADMIN: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/authenticateAdmin/',
		TOKEN_UPDATE: '[protocol]//[hostname]/platform-[env]/ewall-platform-login/v1/users/renew/',
		USERS: '[protocol]//[hostname]/platform-[env]/profiling-server/users/',
		FUSIONER_FITBIT_AUTH: '[protocol]//[hostname]/applications-[env]/fusioner-fitbit2/authorize',
		FUSIONER_FITBIT_CALLBACK: '[protocol]//[hostname]/applications-[env]/fusioner-fitbit2/authorize_callback'
	};
	//******************************************************************************
	var AuthToken=null, RenewAuthTokenIntervalID=0, IsAuthTokenValid=false;
	//==============================================================================
	this.ConfigAPIPublic = function(protocol, hostname, pathname) {
        var env = ReturnPartSubDirFromPathname(pathname);
		REST_API.LOGIN_ADMIN=REST_API.LOGIN_ADMIN.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
		REST_API.TOKEN_UPDATE=REST_API.TOKEN_UPDATE.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
		REST_API.USERS=REST_API.USERS.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
		REST_API.FUSIONER_FITBIT_AUTH=REST_API.FUSIONER_FITBIT_AUTH.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
		REST_API.FUSIONER_FITBIT_CALLBACK=REST_API.FUSIONER_FITBIT_CALLBACK.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env);
    }
	//******************************************************************************
    this.GetAPIbyNamePublic = function(api_name) {
    	return REST_API[api_name];
    }
    //******************************************************************************
    this.GetAuthTokenPublic = function() {
    	return AuthToken;
    }
     //******************************************************************************
    this.GetIsAuthTokenValidPublic = function() {
    	return IsAuthTokenValid;
    }
    //******************************************************************************
    this.SetAuthTokenPublic = function(new_token) {
    	AuthToken = new_token;
    }
    //******************************************************************************
    this.SetIsAuthTokenValidPublic = function(new_val) {
    	IsAuthTokenValid = new_val;
    }
    //******************************************************************************
    this.StartUpdateTokenPublic = function() {
    	RenewAuthTokenIntervalID = window.setInterval(UpdateToken, FitbitAuthConfig.RenewAuthTokenIntervalMS);
    }
    //******************************************************************************
    this.StopUpdateTokenPublic = function() {
    	StopUpdateToken();
    }
    //******************************************************************************
    this.ResetTokenDataPublic = function() {
    	ResetTokenData();
    }
	//==============================================================================
	function StopUpdateToken() {
    	window.clearInterval(RenewAuthTokenIntervalID);
    }
    //******************************************************************************
    function ResetTokenData() {
    	AuthToken = null;
		IsAuthTokenValid=false;
    }
	//******************************************************************************
    function UpdateToken() {
    	var current_token = AuthToken;
    	var ReqUpdateToken = $.ajax({
    		url: REST_API.TOKEN_UPDATE, 
    		headers: {'X-Auth-Token': current_token}, 
			type: "GET", 
			dataType: "json", 
			async: true, 
			cache: true
		});
		$.when(ReqUpdateToken).done(function (ReceiveUpdateToken) {
			AuthToken = ReceiveUpdateToken.token;
			IsAuthTokenValid=true;
		});
		$.when(ReqUpdateToken).fail(function () {
			StopUpdateToken();
			ResetTokenData();
		});
    }
	//==============================================================================
    function ReturnPartSubDirFromPathname(pathname) {
		var match_platform = pathname.match(/^(\/platform-)(([-]*[_]*[0-9]*[a-z]*)+)/);
		var match_applications = pathname.match(/^(\/applications-)(([-]*[_]*[0-9]*[a-z]*)+)/);
		if(match_applications != null) {
			return match_applications[2];
		} else if(match_platform != null) {
			return match_platform[2];
		} else {
			return '';
		}
	}
	//==============================================================================
}//end of object