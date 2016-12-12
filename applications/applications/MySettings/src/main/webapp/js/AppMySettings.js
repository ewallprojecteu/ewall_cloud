function AppObjectMySettings() {
	var LanguageData = {}, PagesSettings = {}, 
	MyNotificationsSettings = {}, MyAppearanceSettings = {}, MySharingSettings = {};
	
	var UserName="",UserFirstName="",UserLang="",UserLangApp="en";
	var PathToSB="",PathToExecuteDirectory="";
	
	var ObjectLocalIndependentTest=null;
	//==============================================================================
	this.SetPathToExecuteDirectory=function(path) {
		PathToExecuteDirectory = path;
	}
	//==============================================================================
	this.SetObjectLocalIndependentTest=function(my_object) {
		ObjectLocalIndependentTest = my_object;
	}
	//==============================================================================
	this.InitAppObject=function() {
		if(!InitExtUserAndDomainPathParams()) {
			return;
		}
		
		$.ajax({
			url: PathToExecuteDirectory+"xml/AppMySettings_lang_"+UserLangApp+".xml",
			type: "GET",
			dataType: "xml",
			async: true,
			cache: true,
			error: function() {},
			success: function(xml) {
				if(InitLangAppData(xml)) {
					InitObjectsSettings();
					CreateAppElements();
				}
			}
		});
	}
	//==============================================================================
	//INIT FUNCTIONS
	//==============================================================================
	function InitExtUserAndDomainPathParams() {
		PathToSB="";
		var HasUserData = false, PathFromApi = "", PathFromApiObj = null;
		
		if(typeof ewallApp === 'object' && ewallApp != null) {
			UserName = ewallApp.currentSession.user.username;
			UserLang = ewallApp.preferedLanguage;
			UserFirstName = ewallApp.currentSession.user.firstName;
			HasUserData = true;
		}
		
		if(!HasUserData) { return false; }
		
		if(typeof ewallAppServices === 'object' && ewallAppServices != null) {
			$.each(ewallAppServices._invokeQueue, function(inx, queue_data) {
		    	if(queue_data.length == 3) {
		    		if(queue_data[1] === 'constant' && queue_data[2].length == 2 && queue_data[2][0] === 'REST_API') {
		    			if(queue_data[2][1].hasOwnProperty('login')) {
		    				PathFromApi = queue_data[2][1].login;
		    				return false;
		   				}
					}
		    	}
		    });
		} else if(typeof ObjectLocalIndependentTest === 'object' && ObjectLocalIndependentTest != null) {
			PathFromApi = ObjectLocalIndependentTest.Path;
		}
		
		if(PathFromApi.length <= 0) { return false; }
		
		PathFromApiObj = GetLocation(PathFromApi);
		if(typeof PathFromApiObj === 'object' && PathFromApiObj != null) {
			var env = ReturnPartSubDirFromPathname(PathFromApiObj.pathname);
			PathToSB = PathFromApiObj.protocol+'//'+PathFromApiObj.host;
			if(env.length > 0) {
				PathToSB += '/applications-'+env;
			}
			PathToSB += '/service-brick-mysettings';
			
			return true;
		} else {
			return false;
		}
	}
	//******************************************************************************
	function InitLangAppData(xml) {
		LanguageData = {};
		$(xml).find("ItemGroup").each(function() {
			var ItemGroupName = $(this).attr("name");
			LanguageData[ItemGroupName]={};
			$(this).children('item').each(function () {
				var itemName = $(this).attr("name");
				if(itemName == "UserSettings") {
					LanguageData[ItemGroupName][itemName] = $(this).text().replace("{firstName}", UserFirstName);
				} else {
					LanguageData[ItemGroupName][itemName] = $(this).text();
				}
			});
		});
		return true;
	}
	//==============================================================================
	function InitObjectsSettings() {
		InitPagesSettings();
		InitMyNotificationsSettings();
		InitMyAppearanceSettings();
		InitMySharingSettings();
	}
	//******************************************************************************
	function InitPagesSettings() {
		PagesSettings.LabelKeys = ['StartPage', 'SharingPage', 'NotificationsPage', 'AppearancePage'];
		PagesSettings.SelectedPageKey = 0;
	}
	//******************************************************************************
	function InitMyNotificationsSettings() {
		MyNotificationsSettings.ExtParamNames = ['healthNotifications', 'houseNotifications', 'medicalNotifications'];
		MyNotificationsSettings.ExtParamValues = ['Value0', 'Value1', 'Value2'];
		MyNotificationsSettings.SelectedValues = {'healthNotifications':0, 'houseNotifications':0, 'medicalNotifications':0};
		MyNotificationsSettings.ReadyData = {'HasData':false, 'HasError':false, 
				'Data':{'healthNotifications':0, 'houseNotifications':0, 'medicalNotifications':0}
		};
	}
	//******************************************************************************
	function InitMyAppearanceSettings() {
		MyAppearanceSettings.LabelKeys = {'Wallpaper':['WP0', 'WP1', 'WP2'], 
				'Books':['mySleep', 'myHealth', 'myDailylife', 'myDailyactivity', 'myGames'], 
				'Photoframe':['PF0', 'PF1'], 'Screensaver':['SSOff', 'SSOn']};
		MyAppearanceSettings.GroupKeys = {'Wallpaper':['wallpaper', 'wallpaper', 'wallpaper'], 
				'Books':['mySleep', 'myHealth', 'myDailylife', 'myDailyactivity', 'myGames'], 
				'Photoframe':['photoframe', 'photoframe'], 'Screensaver':['screensaver', 'screensaver']};
		MyAppearanceSettings.SelectedValues = {'wallpaper':0, 'mySleep':0, 'myHealth':0, 'myDailylife':0, 
				'myDailyactivity':0, 'myGames':0, 'photoframe':0, 'screensaver':0};
		MyAppearanceSettings.ReadyData = {'HasData':false, 'HasError':false, 
				'Data':{'wallpaper':0, 'mySleep':0, 'myHealth':0, 'myDailylife':0, 
					'myDailyactivity':0, 'myGames':0, 'photoframe':0, 'screensaver':0}, 
				'Types':{'wallpaper':'int', 'mySleep':'boolean', 'myHealth':'boolean', 'myDailylife':'boolean', 
						'myDailyactivity':'boolean', 'myGames':'boolean', 'photoframe':'int', 'screensaver':'boolean'}
		};
	}
	//******************************************************************************
	function InitMySharingSettings() {
		MySharingSettings.LabelKeys = ['Nobody', 'Everybody', 'Somebody'];
		MySharingSettings.LabelKeysList2 = ['sharingFamily', 'sharingGP'];
		MySharingSettings.SelectedValues = {'list1':-1, 'list2':{'sharingFamily':-1, 'sharingGP':-1}, 'list3':[]};
		MySharingSettings.ReadyData = {'HasData':false, 'HasError':false, 
				'Data':{'sharingFamily':0, 'sharingGP':0, 'sharingCaregivers':[], 'caregiverNames':[]},
				'Types':{'sharingFamily':'boolean', 'sharingGP':'boolean', 
					'sharingCaregivers':'array', 'caregiverNames':'array'}
		};
	}
	//==============================================================================
	//CREATE HTML ELEMENTS
	//==============================================================================
	function CreateAppElements() { 
		$('#AppMySettings-container').empty();
		$('#AppMySettings-container')
		.append($('<div>').addClass('AppMySettings-header')
			.append($('<p>'))
		)
		.append($('<div>').addClass('AppMySettings-body'))
		.append($('<div>').addClass('AppMySettings-footer')
				.append($('<div>').addClass('AppMySettings-btn').text(LanguageData.Buttons.Back))
		)
		.append($('<div>').addClass('AppMySettings-overlay'))
		.append($('<div>').addClass('AppMySettings-loading AppMySettings-loadingimg'));
		
		$('#AppMySettings-container .AppMySettings-footer .AppMySettings-btn').hide();
		
		$('#AppMySettings-container .AppMySettings-footer .AppMySettings-btn').on('click', function() {
			PagesSettings.SelectedPageKey = 0;
			CreateOnePage();
		});
		
		ViewOrHideOverlayElement(true);
		CreateOnePage();
	}
	//******************************************************************************
	function CreateOnePage() {
		ChangeHeaderText();
		$('#AppMySettings-container .AppMySettings-body').empty();
		$('#AppMySettings-container .AppMySettings-body').append($('<div>').addClass('AppMySettings-Page'+PagesSettings.SelectedPageKey));
		
		switch(PagesSettings.SelectedPageKey) {
			case 0:
				CreateStartPage();
				$('#AppMySettings-container .AppMySettings-footer .AppMySettings-btn').hide();
				break;
			case 1:
				CreateSharingPage();
				$('#AppMySettings-container .AppMySettings-footer .AppMySettings-btn').show();
				break;
			case 2:
				CreateNotificationsPage();
				$('#AppMySettings-container .AppMySettings-footer .AppMySettings-btn').show();
				break;
			case 3:
				CreateAppearancePage();
				$('#AppMySettings-container .AppMySettings-footer .AppMySettings-btn').show();
				break;
			default:
				CreateStartPage();
				$('#AppMySettings-container .AppMySettings-footer .AppMySettings-btn').hide();
				break;
		}
	}
	//******************************************************************************
	function CreateStartPage() {		
		var page_label = "";
		$('#AppMySettings-container .AppMySettings-Page0').append($('<ul>'));
		$.each(PagesSettings.LabelKeys, function(page_key, page) {
			if(page_key == PagesSettings.SelectedPageKey) { return true; }
			page_label = LanguageData[page].Title;
			$('#AppMySettings-container .AppMySettings-Page0 ul').append(
				$('<li>').attr('data-mysettpage', page_key).text(page_label)
			);
		});
		$('#AppMySettings-container .AppMySettings-Page0 ul li').on('click', function() {
			PagesSettings.SelectedPageKey = $(this).data('mysettpage');
			CreateOnePage();
		});
		ViewOrHideOverlayElement(false);
	}
	//******************************************************************************
	function CreateSharingPage() {
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey)
		.append($('<p>').addClass('AppMySettings-txt-h2').text(LanguageData.SharingPage.SubTitle+' '+LanguageData.AppData.SubTitle))
		.append($('<p>').addClass('AppMySettings-txt-h1').text(LanguageData.AppData.UserSettings))
		.append($('<p>').addClass('AppMySettings-txt-h2').text(LanguageData.SharingPage.Message));
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey)
		.append($('<div>').addClass('AppMySettings-SharingEl1')
				.append($('<ul>').addClass('AppMySettings-SharingList1'))
		)
		.append($('<div>').addClass('AppMySettings-SharingEl2')
				.append($('<ul>').addClass('AppMySettings-SharingList2'))
		);
		
		$.each(MySharingSettings.LabelKeys, function(key, label) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul:first')
			.append($('<li>').attr('data-group', 'list1').attr('data-val', key).text(LanguageData.SharingPage[label]));
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey)
		.append($('<div>').addClass('AppMySettings-forbtn')
				.append($('<div>').addClass('AppMySettings-btn').text(LanguageData.Buttons.Save))
		);
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul.AppMySettings-SharingList1 li').on('click', function() {
			SharingList1ItemControl($(this));
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-btn').on('click', function() {
			SendSharingData();
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').hide();
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-SharingEl2').hide();
		
		GetSharingData();
	}
	//******************************************************************************
	function CreateNotificationsPage() {
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey)
		.append($('<p>').addClass('AppMySettings-txt-h2').text(LanguageData.NotificationsPage.SubTitle+' '+LanguageData.AppData.SubTitle))
		.append($('<p>').addClass('AppMySettings-txt-h1').text(LanguageData.AppData.UserSettings));
		
		$.each(MyNotificationsSettings.ExtParamNames, function(key, ext_name) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey)
			.append($('<p>').addClass('AppMySettings-txt-h3').text(LanguageData.NotificationsPage[ext_name]))
			.append($('<ul>'));
			
			$.each(MyNotificationsSettings.ExtParamValues, function(inx, val) {
				$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul:last')
				.append($('<li>').attr('data-group', ext_name).attr('data-val', inx).text(LanguageData.NotificationsPage[val]));
			});
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey)
		.append($('<div>').addClass('AppMySettings-forbtn')
				.append($('<div>').addClass('AppMySettings-btn').text(LanguageData.Buttons.Save))
		);
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul li').on('click', function() {
			NotificationsItemControl($(this));
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-btn').on('click', function() {
			SendNotificationsData();
		});
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').hide();
		
		GetNotificationsData();
	}
	//******************************************************************************
	function CreateAppearancePage() {
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey)
		.append($('<p>').addClass('AppMySettings-txt-h2').text(LanguageData.NotificationsPage.SubTitle+' '+LanguageData.AppData.SubTitle))
		.append($('<p>').addClass('AppMySettings-txt-h1').text(LanguageData.AppData.UserSettings))
		.append($('<table>').addClass('AppMySettings-table1'));
		
		$.each(MyAppearanceSettings.LabelKeys, function(label, arr) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' table')
			.append($('<tr>')
					.append($('<td>').addClass('AppMySettings-cell-1').text(LanguageData.AppearancePage[label]))
					.append($('<td>').attr('data-forgroup', label))
			);
			
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' [data-forgroup="'+label+'"]')
			.append($('<ul>').addClass('AppMySettings-'+label));
			$.each(arr, function(inx, val) {
				if(label == 'Wallpaper') {
					$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul:last')
					.append($('<li>').addClass('AppMySettings-'+val)
							.attr('data-groupfull', label).attr('data-group', MyAppearanceSettings.GroupKeys[label][inx]).attr('data-val', inx));
				} else if(label == 'Books') {
					$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul:last')
					.append($('<li>').attr('data-groupfull', label).attr('data-group', MyAppearanceSettings.GroupKeys[label][inx]).attr('data-val', 1).text(LanguageData.AppearancePage[val]));
				} else {
					$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul:last')
					.append($('<li>').attr('data-groupfull', label).attr('data-group', MyAppearanceSettings.GroupKeys[label][inx]).attr('data-val', inx).text(LanguageData.AppearancePage[val]));
				}
			});
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey)
		.append($('<div>').addClass('AppMySettings-forbtn')
				.append($('<div>').addClass('AppMySettings-btn').text(LanguageData.Buttons.Save))
		);
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul li').on('click', function() {
			AppearanceItemControl($(this));
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-btn').on('click', function() {
			SendAppearanceData();
		});
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').hide();
				
		GetAppearanceData();
	}
	//==============================================================================
	//CHANGE FUNCTIONS
	//==============================================================================
	function ChangeHeaderText() {
		var headerTxt = LanguageData[PagesSettings.LabelKeys[PagesSettings.SelectedPageKey]].Title;
		$('#AppMySettings-container .AppMySettings-header p').text(headerTxt);
	}
	//******************************************************************************
	function ViewOrHideOverlayElement(flag_view) {
		if(flag_view) {
			$('#AppMySettings-container .AppMySettings-overlay').show();
		} else {
			$('#AppMySettings-container .AppMySettings-overlay').hide();
		}
	}
	//******************************************************************************
	function ViewOrHideLoadingElement(flag_view) {
		if(flag_view) {
			$('#AppMySettings-container .AppMySettings-loading').show();
		} else {
			$('#AppMySettings-container .AppMySettings-loading').hide();
		}
		ViewOrHideOverlayElement(flag_view);
	}
	//==============================================================================
	//CONTROL FUNCTIONS
	//==============================================================================
	function SharingList1ItemControl(current_object) {
		MySharingSettings.SelectedValues.list1 = current_object.data('val');
		current_object.parent().children().removeClass('AppMySettings-selected');
		current_object.addClass('AppMySettings-selected');
		
		if(MySharingSettings.SelectedValues.list1 == 2) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-SharingEl2').show();
		} else {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-SharingEl2').hide();
		}
	}
	//******************************************************************************
	function SharingList2ItemControl(current_object) {
		if(current_object.hasClass('AppMySettings-selected')) {
			current_object.removeClass('AppMySettings-selected');
			MySharingSettings.SelectedValues.list2[current_object.data('group')] = 0;
		} else {
			current_object.addClass('AppMySettings-selected');
			MySharingSettings.SelectedValues.list2[current_object.data('group')] = current_object.data('val');
		}
	}
	//******************************************************************************
	function SharingList3ItemControl(current_object) {
		if(current_object.hasClass('AppMySettings-selected')) {
			current_object.removeClass('AppMySettings-selected');
			MySharingSettings.SelectedValues.list3[current_object.data('group')] = 0;
		} else {
			current_object.addClass('AppMySettings-selected');
			MySharingSettings.SelectedValues.list3[current_object.data('group')] = current_object.data('val');
		}
	}
	//******************************************************************************
	function NotificationsItemControl(current_object) {
		MyNotificationsSettings.SelectedValues[current_object.data('group')] = current_object.data('val');
		current_object.parent().children().removeClass('AppMySettings-selected');
		current_object.addClass('AppMySettings-selected');
	}
	//******************************************************************************
	function AppearanceItemControl(current_object) {
		if(current_object.data('groupfull') == 'Books') {
			if(current_object.hasClass('AppMySettings-selected')) {
				current_object.removeClass('AppMySettings-selected');
				MyAppearanceSettings.SelectedValues[current_object.data('group')] = 0;
			} else {
				current_object.addClass('AppMySettings-selected');
				MyAppearanceSettings.SelectedValues[current_object.data('group')] = current_object.data('val');
			}
		} else {
			MyAppearanceSettings.SelectedValues[current_object.data('group')] = current_object.data('val');
			current_object.parent().children().removeClass('AppMySettings-selected');
			current_object.addClass('AppMySettings-selected');
		}
	}
	//==============================================================================
	//GET DATA
	//==============================================================================
	function GetSharingData() {
		InitMySharingSettings();
		ViewOrHideLoadingElement(true);
		
		var urlSharingData = PathToSB+"/sharingdata/"+UserName;
		var ReqShD = ewallApp.ajax({
			url: urlSharingData,
			type: "GET",
			dataType: "json", 
			async: true,
			cache: true
		});
		$.when(ReqShD).done(function (ReceiveShD) {
			$.each(MySharingSettings.ReadyData.Types, function(param, param_type) {
				if(ReceiveShD.hasOwnProperty(param)) {
					if(!MySharingSettings.ReadyData.HasData) {
						MySharingSettings.ReadyData.HasData = true;
					}
					if(param_type == 'array') {
						MySharingSettings.ReadyData.Data[param] = ReceiveShD[param];
					} else {
						if(ReceiveShD[param]) {
							MySharingSettings.ReadyData.Data[param] = 1;
						} else {
							MySharingSettings.ReadyData.Data[param] = 0;
						}
					}
				}
			});
			
			if(MySharingSettings.ReadyData.Data.sharingFamily == 1 && 
					MySharingSettings.ReadyData.Data.sharingGP == 1 && 
					MySharingSettings.ReadyData.Data.sharingCaregivers.length == MySharingSettings.ReadyData.Data.caregiverNames.length) {
				MySharingSettings.SelectedValues.list1 = 1;
				MySharingSettings.SelectedValues.list2.sharingFamily = 0;
				MySharingSettings.SelectedValues.list2.sharingGP = 0;
				$.each(MySharingSettings.ReadyData.Data.caregiverNames, function(inx_cg_name, cg_name) {
					MySharingSettings.SelectedValues.list3[inx_cg_name] = 0;
				});
			} else {
				if(MySharingSettings.ReadyData.Data.sharingFamily == 0 && 
						MySharingSettings.ReadyData.Data.sharingGP == 0 && 
						MySharingSettings.ReadyData.Data.sharingCaregivers.length == 0) {
					MySharingSettings.SelectedValues.list1 = 0;
					MySharingSettings.SelectedValues.list2.sharingFamily = 0;
					MySharingSettings.SelectedValues.list2.sharingGP = 0;
					$.each(MySharingSettings.ReadyData.Data.caregiverNames, function(inx_cg_name, cg_name) {
						MySharingSettings.SelectedValues.list3[inx_cg_name] = 0;
					});
				} else {
					MySharingSettings.SelectedValues.list1 = 2;
					MySharingSettings.SelectedValues.list2.sharingFamily = MySharingSettings.ReadyData.Data.sharingFamily;
					MySharingSettings.SelectedValues.list2.sharingGP = MySharingSettings.ReadyData.Data.sharingGP;
					$.each(MySharingSettings.ReadyData.Data.caregiverNames, function(inx_cg_name, cg_name) {
						if($.inArray(cg_name, MySharingSettings.ReadyData.Data.sharingCaregivers) >= 0) {
							MySharingSettings.SelectedValues.list3[inx_cg_name] = 1;
						} else {
							MySharingSettings.SelectedValues.list3[inx_cg_name] = 0;
						}
					});
				}
			}
			LoadSharingData();
			ViewOrHideLoadingElement(false);
		});
		$.when(ReqShD).fail(function () { 
			MySharingSettings.ReadyData.HasError = true;
			LoadSharingData();
			ViewOrHideLoadingElement(false);
		});
	}
	//******************************************************************************
	function GetNotificationsData() {
		InitMyNotificationsSettings();
		ViewOrHideLoadingElement(true);
		
		var urlNotificationsData = PathToSB+"/mynotifications/"+UserName;
		var ReqND = ewallApp.ajax({
			url: urlNotificationsData,
			type: "GET",
			dataType: "json", 
			async: true,
			cache: true
		});
		$.when(ReqND).done(function (ReceiveND) {
			$.each(MyNotificationsSettings.ExtParamNames, function(key, param) {
				if(ReceiveND.hasOwnProperty(param)) {
					MyNotificationsSettings.SelectedValues[param] = ReceiveND[param];
					MyNotificationsSettings.ReadyData.Data[param] = ReceiveND[param];
					if(!MyNotificationsSettings.ReadyData.HasData) {
						MyNotificationsSettings.ReadyData.HasData = true;
					}
				}
			});
			LoadNotificationsData();
			ViewOrHideLoadingElement(false);
		});
		$.when(ReqND).fail(function () { 
			MyNotificationsSettings.ReadyData.HasError = true;
			LoadNotificationsData();
			ViewOrHideLoadingElement(false);
		});
	}
	//******************************************************************************
	function GetAppearanceData() {
		InitMyAppearanceSettings();
		ViewOrHideLoadingElement(true);
		
		var urlAppearanceData = PathToSB+"/myewallappearance/"+UserName;
		var ReqAD = ewallApp.ajax({
			url: urlAppearanceData,
			type: "GET",
			dataType: "json", 
			async: true,
			cache: true
		});
		$.when(ReqAD).done(function (ReceiveAD) { 
			$.each(MyAppearanceSettings.ReadyData.Data, function(param, val) {
				if(ReceiveAD.hasOwnProperty(param)) {
					if(!MyAppearanceSettings.ReadyData.HasData) {
						MyAppearanceSettings.ReadyData.HasData = true;
					}
					if(MyAppearanceSettings.ReadyData.Types[param] == 'int') {
						MyAppearanceSettings.SelectedValues[param] = ReceiveAD[param];
						MyAppearanceSettings.ReadyData.Data[param] = ReceiveAD[param];
					} else {
						if(ReceiveAD[param]) {
							MyAppearanceSettings.SelectedValues[param] = 1;
							MyAppearanceSettings.ReadyData.Data[param] = 1;
						} else {
							MyAppearanceSettings.SelectedValues[param] = 0;
							MyAppearanceSettings.ReadyData.Data[param] = 0;
						}
					}
				}
			});
			LoadAppearanceData();
			ViewOrHideLoadingElement(false);
		});
		$.when(ReqAD).fail(function () { 
			MyAppearanceSettings.ReadyData.HasError = true;
			LoadAppearanceData();
			ViewOrHideLoadingElement(false);
		});
	}
	//==============================================================================
	//SEND DATA
	//==============================================================================
	function SendSharingData() {
		ViewOrHideLoadingElement(true);
		
		var urlSharingData = PathToSB+"/sharingdata";
		var dataSend = {'username':UserName};
		if(MySharingSettings.SelectedValues.list1 == 0) {
			dataSend.sharingFamily = false;
			dataSend.sharingGP = false;
			dataSend.sharingCaregivers = '';
		}
		if(MySharingSettings.SelectedValues.list1 == 1) {
			dataSend.sharingFamily = true;
			dataSend.sharingGP = true;
			var myArr = MySharingSettings.ReadyData.Data.caregiverNames;
			dataSend.sharingCaregivers = myArr.toString();
		}
		if(MySharingSettings.SelectedValues.list1 == 2) {
			if(MySharingSettings.SelectedValues.list2.sharingFamily == 1) {
				dataSend.sharingFamily = true;
			} else {
				dataSend.sharingFamily = false;
			}
			if(MySharingSettings.SelectedValues.list2.sharingGP == 1) {
				dataSend.sharingGP = true;
			} else {
				dataSend.sharingGP = false;
			}
			var myArr = [];
			$.each(MySharingSettings.SelectedValues.list3, function(inx, val) {
				if(val == 1) {
					myArr[myArr.length] = MySharingSettings.ReadyData.Data.caregiverNames[inx];
				}
			});
			dataSend.sharingCaregivers = myArr.toString();
		}
		
		var SendShD = ewallApp.ajax({
			url: urlSharingData,
			type: "POST",
			data: dataSend,
			async: true,
			cache: true
		});
		$.when(SendShD).done(function () {
			GetSharingData();
		});
		$.when(SendShD).fail(function () { 
			GetSharingData();
		});
	}
	//******************************************************************************
	function SendNotificationsData() {
		ViewOrHideLoadingElement(true);
		
		var urlNotificationsData = PathToSB+"/mynotifications";
		var dataSend = {'username':UserName};
		$.each(MyNotificationsSettings.SelectedValues, function(group, val) {
			dataSend[group] = val;
		});
		
		var SendND = ewallApp.ajax({
			url: urlNotificationsData,
			type: "POST",
			data: dataSend,
			async: true,
			cache: true
		});
		$.when(SendND).done(function () {
			GetNotificationsData();
		});
		$.when(SendND).fail(function () { 
			GetNotificationsData();
		});
	}
	//******************************************************************************
	function SendAppearanceData() {
		ViewOrHideLoadingElement(true);
		
		var urlAppearanceData = PathToSB+"/myewallappearance";
		var dataSend = {'username':UserName};
		$.each(MyAppearanceSettings.SelectedValues, function(group, val) {
			if(MyAppearanceSettings.ReadyData.Types[group] == 'int') {
				dataSend[group] = val;
			} else {
				if(val == 0) {
					dataSend[group] = false;
				} else {
					dataSend[group] = true;
				}
			}
		});
		
		var SendAD = ewallApp.ajax({
			url: urlAppearanceData,
			type: "POST",
			data: dataSend,
			async: true,
			cache: true
		});
		$.when(SendAD).done(function () { 
			GetAppearanceData();
		});
		$.when(SendAD).fail(function () { 
			GetAppearanceData();
		});
	}
	//==============================================================================
	//LOAD DATA
	//==============================================================================
	function LoadSharingData() {
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul.AppMySettings-SharingList2').empty();
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul li').removeClass('AppMySettings-selected');
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').hide();
		
		if(!MySharingSettings.ReadyData.HasData) {
			return false;
		}
		
		$.each(MySharingSettings.LabelKeysList2, function(key, label) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul.AppMySettings-SharingList2')
			.append($('<li>').attr('data-groupsub', 'list2').attr('data-group', label).attr('data-val', 1)
					.text(LanguageData.SharingPage[label]));
		});
		
		$.each(MySharingSettings.ReadyData.Data.caregiverNames, function(inx, cg_name) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul.AppMySettings-SharingList2')
			.append($('<li>').attr('data-groupsub', 'list3').attr('data-group', inx).attr('data-val', 1)
					.text(cg_name));
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' [data-groupsub="list2"]').on('click', function() {
			SharingList2ItemControl($(this));
		});
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' [data-groupsub="list3"]').on('click', function() {
			SharingList3ItemControl($(this));
		});
				
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' [data-group="list1"][data-val="'+MySharingSettings.SelectedValues.list1+'"]').trigger('click');
		$.each(MySharingSettings.SelectedValues.list2, function(group, val) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' [data-groupsub="list2"][data-group="'+group+'"][data-val="'+val+'"]').trigger('click');
		});
		$.each(MySharingSettings.SelectedValues.list3, function(group, val) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' [data-groupsub="list3"][data-group="'+group+'"][data-val="'+val+'"]').trigger('click');
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').show();
	}
	//******************************************************************************
	function LoadNotificationsData() {
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul li').removeClass('AppMySettings-selected');
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').hide();
		
		if(!MyNotificationsSettings.ReadyData.HasData) {
			return false;
		}
		
		$.each(MyNotificationsSettings.SelectedValues, function(group, val) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' [data-group="'+group+'"][data-val="'+val+'"]').trigger('click');
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').show();
	}
	//******************************************************************************
	function LoadAppearanceData() { 
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' ul li').removeClass('AppMySettings-selected');
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').hide();
		
		if(!MyAppearanceSettings.ReadyData.HasData) {
			return false;
		}
		
		$.each(MyAppearanceSettings.SelectedValues, function(group, val) {
			$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' [data-group="'+group+'"][data-val="'+val+'"]').trigger('click');
		});
		
		$('#AppMySettings-container .AppMySettings-Page'+PagesSettings.SelectedPageKey+' .AppMySettings-forbtn').show();
	}
	//==============================================================================
	//HELP FUNCTIONS
	//==============================================================================
	function GetLocation(href) {
	    var match = href.match(/^(https?\:)\/\/(([^:\/?#]*)(?:\:([0-9]+))?)(\/[^?#]*)(\?[^#]*|)(#.*|)$/);
	    return match && {
	        protocol: match[1],
	        host: match[2],
	        hostname: match[3],
	        port: match[4],
	        pathname: match[5],
	        search: match[6],
	        hash: match[7]
	    }
	}
	//******************************************************************************
	function ReturnPartSubDirFromPathname(pathname) {
		var match_platform = pathname.match(/^(\/platform-)(([-]*[_]*[0-9]*[a-z]*)+)/);
		var match_applications = pathname.match(/^(\/applications-)(([-]*[_]*[0-9]*[a-z]*)+)/);
		
		if(match_platform != null) {
			return match_platform[2];
		} else if(match_applications != null) {
			return match_applications[2];
		} else {
			return '';
		}
	}
	//==============================================================================
}//end of object