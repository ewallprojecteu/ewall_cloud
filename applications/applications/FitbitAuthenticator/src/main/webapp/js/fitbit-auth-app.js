'use strict';
function FitbitAuthApp() {
	var AppLang="en", LanguageData, AdminUserData, CheckPopupLocationIntervalID=0, PopupWin = null, 
	PopupWinURLData;
	
	var HelperFitbitAuth = new FitbitAuthHelper();
	var ScreenFitbitAuth = new FitbitAuthScreen();
	//******************************************************************************
	this.Init=function() {
		ScreenFitbitAuth.ConfigAPIPublic(FitbitAuthConfig.LocationData.protocol, FitbitAuthConfig.LocationData.hostname, FitbitAuthConfig.LocationData.pathname);
		
		$('#buttonLogout').click(function() { 
			CreateLoginForm();
		});
		
		InitAdminUserData();
		InitPopupWinURLData();
		GetLanguageFile();
	}
	//==============================================================================
	//INIT FUNCTIONS
	//==============================================================================
	function InitAdminUserData() {
		AdminUserData={'isLogin':false, 'username':'', 'userRole':'',  'regionName':''};
	}
	//******************************************************************************
	function InitPopupWinURLData() {
		PopupWinURLData={'URLForPopup':'', 'URLFromPopup':''};
	}
	//==============================================================================
	//CLEAR FUNCTIONS 
	//==============================================================================
	this.PageUnload = function() {
		ClearAllDataAndTimers();
	}
	//******************************************************************************
	function ClearAllDataAndTimers() {
		InitAdminUserData();
		InitPopupWinURLData();
		StopCheckPopupLocation();
		
		ScreenFitbitAuth.StopUpdateTokenPublic();
		ScreenFitbitAuth.ResetTokenDataPublic();
	}
	//==============================================================================
	//CREATE HTML ELEMENTS
	//==============================================================================
	function CreateLoginForm() {
		ClearAllDataAndTimers();
		$('#navContent #buttonLogout').css({'display':'none'});
		$('#dynamicContent').empty();
		$('#dynamicContent').append($('<form>').addClass('app-fitbit-form'));
		$('#dynamicContent form').append($('<h2>').attr({'data-translate':'_titleLogin'}).addClass('app-fitbit-form-heading'));
		$('#dynamicContent form').append($('<label>').attr({'for':'inputUsername', 'data-translate':'_username'}).addClass('sr-only'));
		$('#dynamicContent form').append($('<input>').attr({'type':'text', 'id':'inputUsername', 'data-translate-placeholder':'_username', 'placeholder':''}).addClass('form-control'));
		$('#dynamicContent form').append($('<label>').attr({'for':'inputPassword', 'data-translate':'_password'}).addClass('sr-only'));
		$('#dynamicContent form').append($('<input>').attr({'type':'password', 'id':'inputPassword', 'data-translate-placeholder':'_password', 'placeholder':''}).addClass('form-control'));
		$('#dynamicContent form').append($('<button>').attr({'type':'button', 'id':'buttonSingIn', 'data-translate':'_signIn'}).addClass('btn btn-lg btn-primary btn-block'));
		
		HelperFitbitAuth.TranslateDynamicContent(LanguageData);
		
		$('#inputUsername').focus();
		
		$('#buttonSingIn').click(function() { 
			var form_username = $('#inputUsername').val().trim();
			var form_password = $('#inputPassword').val().trim();
			
			if(form_username.length > 0 && form_password.length > 0) {
				$('#loadingModal').modal('show');
				GetUserAuthenticate(form_username, form_password);
			}
		});
	}
	//******************************************************************************
	function CreateUserSelectionForm() {
		$('#navContent #buttonLogout').css({'display':'inline'});
		
		$('#dynamicContent').empty();
		$('#dynamicContent').append($('<form>').addClass('app-fitbit-form'));
		$('#dynamicContent form').append($('<h2>').attr({'data-translate':'_titleUserSelection'}).addClass('app-fitbit-form-heading'));
		$('#dynamicContent form').append($('<select>').attr({'id':'UserSelection'}).addClass('form-control'));
		$('#dynamicContent form').append($('<button>').attr({'type':'button', 'id':'buttonAuthUser', 'data-translate':'_authUser'}).addClass('btn btn-lg btn-primary btn-block'));
		
		$('#UserSelection').append($('<option>').attr({'data-translate':'_selectUserHint', 'value':0}).prop({'disabled':true, 'selected':true}));
		$('#buttonAuthUser').attr({'disabled':'disabled'});
		
		HelperFitbitAuth.TranslateDynamicContent(LanguageData);
		
		$('#UserSelection').change(function() { 
			if($('#UserSelection option').length > 1) {
				var selected_user = $(this).val();
				if(selected_user !== 0) {
					StopCheckPopupLocation();
					
					if(ScreenFitbitAuth.GetIsAuthTokenValidPublic()) {
						$('#loadingModal').modal('show');
						GetFitbitAuthorizePage(selected_user);
					} else {
						CreateLoginForm();
					}
				}
			}
		});
		
		$('#buttonAuthUser').click(function() { 
			if(PopupWinURLData.URLForPopup.length > 0) {
				$('#waitingModal').modal('show');
				OpenPopup(PopupWinURLData.URLForPopup);
				StartCheckPopupLocation();
			}
		});
		
		if(ScreenFitbitAuth.GetIsAuthTokenValidPublic()) {
			GetUsersData();
		} else {
			CreateLoginForm();
		}
	}
	//==============================================================================
	//REQUESTS
	//==============================================================================
	function GetLanguageFile() {
		$(".container").hide();
		var ReqLangFile = $.ajax({
			url: "languages/lang-"+AppLang+".json",
			type: "GET",
			dataType: "json",
			async: true,
			cache: true
		});
		$.when(ReqLangFile).done(function (ReceiveLangData) {
			LanguageData = ReceiveLangData;
			HelperFitbitAuth.Translate(LanguageData);
			if(AdminUserData.isLogin) {
				CreateUserSelectionForm();
			} else {
				CreateLoginForm();
			}
			$(".container").show();
		});
		$.when(ReqLangFile).fail(function () {
			return false;
		});
	}
	//******************************************************************************
	function GetUserAuthenticate(login_username, login_pass) {
		var ReqUserAuth = $.ajax({
			url: ScreenFitbitAuth.GetAPIbyNamePublic('LOGIN_ADMIN'),
			type: "POST",
			data: {username: login_username, password: login_pass},
			dataType: "json", 
			async: true,
			cache: true
		});
		$.when(ReqUserAuth).done(function (ReceiveUserAuth) {
			AdminUserData.isLogin = true;
			AdminUserData.username = login_username;
			
			ScreenFitbitAuth.SetAuthTokenPublic(ReceiveUserAuth.token);
			ScreenFitbitAuth.SetIsAuthTokenValidPublic(true);
			
			GetUserProfile();
		});
		$.when(ReqUserAuth).fail(function () {
			InitAdminUserData();
			$('#loadingModal').modal('hide');
			ViewErrorModal(LanguageData._loginFailed);
		});
	}
	//******************************************************************************
	function GetUserProfile() {
		var ReqUserProfile = $.ajax({
			url: ScreenFitbitAuth.GetAPIbyNamePublic('USERS') + AdminUserData.username, 
			headers: {'X-Auth-Token': ScreenFitbitAuth.GetAuthTokenPublic()}, 
			type: "GET",
			dataType: "json", 
			async: true,
			cache: true
		});
		$.when(ReqUserProfile).done(function (ReceiveUserProfile) {
			if(ReceiveUserProfile.userRole in FitbitAuthConfig.ValidAdminRoleAndCheckRegion) {
				AdminUserData.userRole = ReceiveUserProfile.userRole;
				AdminUserData.regionName = ReceiveUserProfile.userProfile.eWallSubProfile.regionName;
				
				ScreenFitbitAuth.StartUpdateTokenPublic();
				
				$('#loadingModal').modal('hide');
				CreateUserSelectionForm();
			} else {
				InitAdminUserData();
				$('#loadingModal').modal('hide');
				ViewErrorModal(LanguageData._loginFailed);
			}
		});
		$.when(ReqUserProfile).fail(function () {
			InitAdminUserData();
			$('#loadingModal').modal('hide');
			ViewErrorModal(LanguageData._loginFailed);
		});
	}
	//******************************************************************************
	function GetUsersData() {
		var ReqUsersData = $.ajax({
			url: ScreenFitbitAuth.GetAPIbyNamePublic('USERS'), 
			headers: {'X-Auth-Token': ScreenFitbitAuth.GetAuthTokenPublic()}, 
			type: "GET",
			data: {associatedWithSensEnvFilter: 'ALL_USERS'},
			dataType: "json", 
			async: true,
			cache: true
		});
		$.when(ReqUsersData).done(function (ReceiveUsersData) {
			LoadUsersData(ReceiveUsersData);	
		});
		$.when(ReqUsersData).fail(function () {
			ViewErrorModal(LanguageData._getUsersFailed);
		});
	}
	//******************************************************************************
	function GetFitbitAuthorizePage(current_user) {
		PopupWinURLData.URLForPopup='';
		var ReqFitbitAuthorizePage = $.ajax({
			url: ScreenFitbitAuth.GetAPIbyNamePublic('FUSIONER_FITBIT_AUTH'), 
			headers: {'X-Auth-Token': ScreenFitbitAuth.GetAuthTokenPublic()},
			type: "GET",
			data: {user: current_user},
			async: true,
			cache: true
		});
		$.when(ReqFitbitAuthorizePage).done(function (ReceiveFitbitAuthorizePage) {
			$('#loadingModal').modal('hide');
			var DecodeFitbitAuthorizePage = decodeURIComponent(ReceiveFitbitAuthorizePage);
			if(DecodeFitbitAuthorizePage.search(ScreenFitbitAuth.GetAPIbyNamePublic('FUSIONER_FITBIT_CALLBACK')) > 0) {
				PopupWinURLData.URLForPopup = DecodeFitbitAuthorizePage;
				$('#buttonAuthUser').removeAttr('disabled');
			} else {
				ViewErrorModal(LanguageData._getAuthPageFailed);
			}
		});
		$.when(ReqFitbitAuthorizePage).fail(function () {
			$('#loadingModal').modal('hide');
			ViewErrorModal(LanguageData._getAuthPageFailed);
		});
	}
	//******************************************************************************
	function GetFitbitAuthorizeCallback() {
		var ReqFitbitAuthorizeCallback = $.ajax({
			url: PopupWinURLData.URLFromPopup, 
			headers: {'X-Auth-Token': ScreenFitbitAuth.GetAuthTokenPublic()}, 
			type: "GET",
			async: true,
			cache: true
		});
		$.when(ReqFitbitAuthorizeCallback).done(function (ReceiveFitbitAuthorizeCallback) {
			InitPopupWinURLData();
			$('#buttonAuthUser').attr({'disabled':'disabled'});
			$('#UserSelection').val(0);
			
			$('#waitingModal').modal('hide');
			ViewSuccessModal(LanguageData._successAuth);
		});
		$.when(ReqFitbitAuthorizeCallback).fail(function () {
			PopupWinURLData.URLFromPopup='';
			$('#waitingModal').modal('hide');
			ViewErrorModal(LanguageData._failedAuth);
		});
	}
	//==============================================================================
	//LOAD EXTERNAL DATA
	//==============================================================================
	function LoadUsersData(GetUsersData) {
		var LocalUserData = [];
		for(var receive_index in GetUsersData) {
			var username = GetUsersData[receive_index].username;
			var userRole = GetUsersData[receive_index].userRole;
			var regionName = GetUsersData[receive_index].userProfile.eWallSubProfile.regionName;
			
			if(FitbitAuthConfig.UserSelectionUserRole.length > 0 && $.inArray(userRole, FitbitAuthConfig.UserSelectionUserRole) <= -1) {
				continue;
			}
			if(FitbitAuthConfig.ValidAdminRoleAndCheckRegion[AdminUserData.userRole]) {
				if(regionName === AdminUserData.regionName) {
					LocalUserData[LocalUserData.length] = username;
				}
			} else {
				LocalUserData[LocalUserData.length] = username;
			}
		}
		for(var user_index in LocalUserData) {
			var current_user = LocalUserData[user_index];
			$('#UserSelection').append($('<option>').attr({'value':current_user}).text(current_user));
		}
	}
	//==============================================================================
	//POPUP WINDOW
	//==============================================================================
	function OpenPopup(popup_url) {
		if(CheckPopupExists()) {
			PopupWin.focus();
		} else {
			PopupWin = window.open(popup_url, 'importwindow');
		}
	}
	//******************************************************************************
	function ClosePopup() {
		if(CheckPopupExists()) {
			PopupWin.close();
		}
		PopupWin = null;
	}
	//******************************************************************************
	function CheckPopupExists() {
		if(PopupWin == null || PopupWin.closed) {
			return false;
		} else {
			return true;
		}
	}
	//******************************************************************************
	function StartCheckPopupLocation() {
		CheckPopupLocationIntervalID = window.setInterval(function() {
			if(CheckPopupExists()) {
				try {
					var url = PopupWin.document.URL;
					var url_check = ScreenFitbitAuth.GetAPIbyNamePublic('FUSIONER_FITBIT_CALLBACK');
					if(url.indexOf(url_check) != -1) {
						StopCheckPopupLocation();
						PopupWinURLData.URLFromPopup=url;
						if(ScreenFitbitAuth.GetIsAuthTokenValidPublic()) {
							GetFitbitAuthorizeCallback();
						} else {
							$('#waitingModal').modal('hide');
							CreateLoginForm();
						}
					}
				} catch (e) {
					//console.log(e);
				}
			} else {
				StopCheckPopupLocation();
			}
		}, FitbitAuthConfig.CheckPopupLocationIntervalMS);
	}
	//******************************************************************************
	function StopCheckPopupLocation() {
		window.clearInterval(CheckPopupLocationIntervalID);
		ClosePopup();
	}
	//==============================================================================
	//VIEW OR HIDE MODALS
	//==============================================================================
	function ViewErrorModal(error_message) {
		$('#errorModal #errorModalMessage').text(error_message);
		$('#errorModal').modal('show');
	}
	//******************************************************************************
	function ViewSuccessModal(success_message) {
		$('#successModal #successModalMessage').text(success_message);
		$('#successModal').modal('show');
	}
	//==============================================================================
}//end of object