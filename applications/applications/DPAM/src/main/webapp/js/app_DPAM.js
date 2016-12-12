var initStates = function () {
	ewallApp.stateProvider.state('main.dailyPhysicalActivityMonitoring',{
		url:'/',
		templateUrl:'../DPAM/index.html',
		data: {
			authenticationRequired: true
		}
	});
};


//assign angular controller to module
ewallApp.controller('physicalActivityAppController', function($scope, $state) {
	console.log("Loaded physicalActivityApp controller");

	var stateAlreadyRegistered = _.find($state.get(), function (s) {
		return s.name == 'main.dailyPhysicalActivityMonitoring';
	});
	if (!stateAlreadyRegistered) {
		initStates();
	}


	var AppObjectDPAM_NewInstance = new AppObjectDPAM();
	AppObjectDPAM_NewInstance.SetPathToExecuteDirectory('../DPAM/');
	AppObjectDPAM_NewInstance.InitAppObject();
    



jQuery.support.cors = true;
$.jqplot.config.enablePlugins = true;
//==============================================================================
//NEW VERSION - April 2016
//==============================================================================
function AppObjectDPAM() {
	var LanguageSupport=[['en','en-US'], ['da','da-Dk'], ['de','de-AT'], ['it'], ['nl'], ['sr'], ['mk'], 
	['bg','bg-BG'], ['hr'], ['ro','ro-RO']];
	var AppObjectParams = {
		'ObjectsHtmlID':{'AppDataContainer':'#AppDPAM_AppDataContainer', 
						'SliderForLoad':'#AppDPAM-Slider .AppDPAM-ForLoad', 
						'ChartForLoad':'#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-ForLoad', 
						'EventPart':'#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-EventPart', 
						'MessagePart':'#AppDPAM-ExtData .AppDPAM-Message'},
		'ObjectsPartID':{'NavChartID':'AppDPAM_ChartView', 
						'ChartID':'AppDPAM_Chart', 'ChartCanvasID':'AppDPAM_ChartCanvas', 
						'NavActivityID':'AppDPAM_ActivityView', 'SliderID':'AppDPAM_ElementID'}, 
		'SliderSizes':{'VisPartWidth':(7*162), 'OneItemWidth':162}
	};
	var AppObjectTextsData={'LongMonthsArr':[], 'ShortMonthsArr':[], 'LongDaysArr':[], 'ShortDaysArr':[],
	'ChartNavLabelsArr':[], 'ActNavLabelsArr':[], 'ChartLabelsObj':{}, 'MessagesObj':{}, 'AppLabelsObj':{}, 
	'ColorLegendObj':{'Label':'', 'Colors':[]}};
	
	var ColorObj={'BasicColor':['#ffab00','#ffd600','#aeea00','#64dd17','#12c700'], 
	'AdditionalColor':['#c48300', '#c4a500', '#86b400', '#4eab14', '#0e9900'], 
	'OrderColorKeys':[0,1,2,3,4], 'LegendColorIDs':[1,2,3,4,5]};
	
	var ToDay={}, SliderConfigData={'StartDates':{}, 'EndDates':{}, 'MoveParams':{}, 
	'DatesMsForUpdate':{}, 'FlagGetExtData':{}}, 
	SliderReadyData={}, GoalStepsReadyData={}, ChartReadyData={}, LocalChartExternalData={};
	
	var ExternalParamNames=['steps', 'kilometers', 'burnedCalories'];
	var PhysicalActEventsSupport={'ForActivityDuration':['resting','walking','running','exercising'], 
	'ForChartActivityData':['running','exercising']};
	
	var ChartSelectedViewID = 1; //1 - steps, 2 - kilometers, 3 - calories
	var ActivitySelectedViewID = 1; //1 - day, 2 - week, 3 - month
	var SliderSelectedObjectID = 0;
	var CountStartedRequests=3, CountFinishedRequests=0;
	
	var UserName="", UserID=0, UserFirstName="", UserLang="", UserTimezoneOlson="", UserLangApp="en", UserIteractionData={};
	var Domain=window.location.hostname, 
	PathToSB_PhysicalActivity="", PathToActivityGoal="", PathToExecuteDirectory="";
	
	var myScrollHorizontal=null;
	
	var AppToken='';
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
			return false;
		}
		
		InitUserLang();
					
		$.ajax({
			url: PathToExecuteDirectory+"xml/app_DPAM_lang_"+UserLangApp+".xml",
			type: "GET",
			dataType: "xml",
			async: true,
			cache: true,
			error: function() {return false;},
			success: function(xml) {
				if(InitLangAppData(xml)) {
					ElementAddOrRemoveLoading(AppObjectParams.ObjectsHtmlID.AppDataContainer, true);
					
					InitSliderConfigData();
					InitSliderReadyData();
					InitChartReadyData();
					InitGoalStepsReadyData();
					InitUserIteractionData();
					
					GetExternalDataGoalSteps();
				}
			}
		});
	}
	//==============================================================================
	//INIT FUNCTIONS
	//==============================================================================
	function InitExtUserAndDomainPathParams() {
		if(typeof ewallApp == 'object' && ewallApp != null) {
			UserName = ewallApp.currentSession.user.username;
			UserFirstName = ewallApp.currentSession.user.firstName;
			UserLang = ewallApp.preferedLanguage;
			UserTimezoneOlson=ewallApp.currentSession.user.userProfile.vCardSubProfile.timezoneid;
			
			var current_domain = window.location.protocol+'//'+window.location.hostname;
			var current_pathname = window.location.pathname;
			var part_sub_dir = '';
			
			if(current_domain != Domain) {
				if(typeof ewallAppServices == 'object' && ewallAppServices != null) {
					var queue = ewallAppServices._invokeQueue;
					for(var i in queue) {
						if(queue[i].length == 3) {
							if(queue[i][1] == 'constant' && queue[i][2].length == 2 && queue[i][2][0] == 'REST_API') {
								if("login" in queue[i][2][1]) {
								    path_from_api=queue[i][2][1]["login"];
									break;
								}
							}
						}
					}
				}
				
				if(path_from_api.length <= 0) {
					return false;
				}
				
   				var path_from_api_parts = GetLocation(path_from_api);  			
   				var domain_from_api=path_from_api_parts.protocol+'//'+path_from_api_parts.host;
   				var pathname_from_api=path_from_api_parts.pathname;
   				
   				PathToSB_PhysicalActivity = domain_from_api;
				PathToActivityGoal = domain_from_api;
				part_sub_dir = ReturnPartSubDirFromPathname(pathname_from_api);
				
			} else {
				PathToSB_PhysicalActivity = current_domain;
				PathToActivityGoal = current_domain;
				part_sub_dir = ReturnPartSubDirFromPathname(current_pathname);
			}
			
			if(part_sub_dir.length > 0) {
				PathToSB_PhysicalActivity += '/applications-'+part_sub_dir;
				PathToActivityGoal += '/platform-'+part_sub_dir;
			}
			
			PathToSB_PhysicalActivity += '/service-brick-physicalactivity';
			PathToActivityGoal += '/idss-automatic-goal-setting';
			
//			PathToSB_PhysicalActivity = 'http://localhost:8080/service-brick-physicalactivity';
//			PathToActivityGoal = 'http://localhost:8080/idss-automatic-goal-setting';

			return true;
		}
		else if(typeof ObjectLocalIndependentTest == 'object' && ObjectLocalIndependentTest != null) {
			UserName=ObjectLocalIndependentTest.UserName;
			UserFirstName = ObjectLocalIndependentTest.UserFirstName;
			UserLang=ObjectLocalIndependentTest.UserLang;
			UserTimezoneOlson=ObjectLocalIndependentTest.UserTimezoneOlson;
			AppToken=ObjectLocalIndependentTest.token;
			
			PathToSB_PhysicalActivity = Domain+'/applications-'+ObjectLocalIndependentTest.PartSubDir+'/service-brick-physicalactivity';
			PathToActivityGoal = Domain+'/platform-'+ObjectLocalIndependentTest.PartSubDir+'/idss-automatic-goal-setting';
			
//			PathToSB_PhysicalActivity = 'http://localhost:8080/service-brick-physicalactivity';
//			PathToActivityGoal = 'http://localhost:8080/idss-automatic-goal-setting';

			return true;
		}
		else {
			return false;
		}
	}
	//******************************************************************************
	function InitUserLang() {
		for(var i in LanguageSupport) {
			for(var j in LanguageSupport[i]) {
				if(LanguageSupport[i][j] == UserLang) {
					UserLangApp = LanguageSupport[i][0];
					break;
				}
			}
		}
	}
	//******************************************************************************
	function InitLangAppData(xml) {
		for(var LCID_Key in ColorObj.LegendColorIDs) {
			AppObjectTextsData.ColorLegendObj.Colors[LCID_Key] = '';
		}
		
		$(xml).find("ItemGroup").each(function() {
			if($(this).attr("name") == "LongMonths") {
				$(this).children('item').each(function () {
					AppObjectTextsData.LongMonthsArr[AppObjectTextsData.LongMonthsArr.length]=CapitalizeFirstLetter($(this).text(), false);
				});
			}
			if($(this).attr("name") == "ShortMonths") {
				$(this).children('item').each(function () {
					AppObjectTextsData.ShortMonthsArr[AppObjectTextsData.ShortMonthsArr.length]=CapitalizeFirstLetter($(this).text(), false);
				});
			}
			if($(this).attr("name") == "LongDays") {
				$(this).children('item').each(function () {
					AppObjectTextsData.LongDaysArr[AppObjectTextsData.LongDaysArr.length]=CapitalizeFirstLetter($(this).text(), true);
				});
			}
			if($(this).attr("name") == "ShortDays") {
				$(this).children('item').each(function () {
					AppObjectTextsData.ShortDaysArr[AppObjectTextsData.ShortDaysArr.length]=CapitalizeFirstLetter($(this).text(), true);
				});
			}
			if($(this).attr("name") == "ChartNavLabels") {
				$(this).children('item').each(function () {
					AppObjectTextsData.ChartNavLabelsArr[AppObjectTextsData.ChartNavLabelsArr.length]=$(this).text();
				});
			}
			if($(this).attr("name") == "NavActLabels") {
				$(this).children('item').each(function () {
					AppObjectTextsData.ActNavLabelsArr[AppObjectTextsData.ActNavLabelsArr.length]=$(this).text();
				});
			}
			if($(this).attr("name") == "ChartTitle") {
				AppObjectTextsData.ChartLabelsObj.ChartTitle=[];
				$(this).children('item').each(function () {
					AppObjectTextsData.ChartLabelsObj.ChartTitle[AppObjectTextsData.ChartLabelsObj.ChartTitle.length]=$(this).text();
				});
			}
			if($(this).attr("name") == "yTitle") {
				AppObjectTextsData.ChartLabelsObj.yTitle=[];
				$(this).children('item').each(function () {
					AppObjectTextsData.ChartLabelsObj.yTitle[AppObjectTextsData.ChartLabelsObj.yTitle.length]=$(this).text();
				});
			}
			if($(this).attr("name") == "Messages") {
				AppObjectTextsData.MessagesObj.Title=$(this).children('item').children('Title').text();
				AppObjectTextsData.MessagesObj.Content=$(this).children('item').children('Content').text();
			}
			if($(this).attr("name") == "AppLabels") {
				AppObjectTextsData.AppLabelsObj.Shorthour=$(this).children('item').children('shorthour').text();
				AppObjectTextsData.AppLabelsObj.Steps=$(this).children('item').children('steps').text();
			}
			if($(this).attr("name") == "ColorLegendLabels") {
				AppObjectTextsData.ColorLegendObj.Label=$(this).children('Label').text();
				$(this).children('Color').each(function () {
					var color_txt_key = $.inArray(parseInt($(this).attr("id")), ColorObj.LegendColorIDs);
					if(color_txt_key > -1) {
						AppObjectTextsData.ColorLegendObj.Colors[color_txt_key] = $(this).text();
					}
				});
			}
		});		
		return true;
	}
	//==============================================================================
	function InitSliderConfigData() {
		ToDay = new Date(); ToDay.setHours(0); ToDay.setMinutes(0); ToDay.setSeconds(0); ToDay.setMilliseconds(0);
		
		var end_date = new Date(ToDay.getFullYear(), ToDay.getMonth(), ToDay.getDate());
		var start_date_1 = ReturnPreviousDateShiftExactMonths(end_date, 1);
		var start_date_2 = ReturnFirstDateOfWeekShiftWeeks(end_date, -((7*3)-1));
		var start_date_3 = new Date((end_date.getFullYear()-1), (end_date.getMonth()+1), 1);
		var end_date_1 = ReturnDateShiftDays(ToDay, 1);
		var end_date_2 = ReturnFirstDateOfWeekShiftWeeks(end_date, 1);
		var end_date_3 = ReturnFirstDateOfMonthShiftMonths(end_date, 1);
		
		SliderConfigData.StartDates = { 
			1:{"date":start_date_1, "date_ms":Date.parse(start_date_1)}, 
			2:{"date":start_date_2, "date_ms":Date.parse(start_date_2)}, 
			3:{"date":start_date_3, "date_ms":Date.parse(start_date_3)}
		};
		SliderConfigData.EndDates = { 
			1:{"date":end_date, "date_ms":Date.parse(end_date), "date_add":end_date_1, "date_add_ms":Date.parse(end_date_1)}, 
			2:{"date":end_date, "date_ms":Date.parse(end_date), "date_add":end_date_2, "date_add_ms":Date.parse(end_date_2)}, 
			3:{"date":end_date, "date_ms":Date.parse(end_date), "date_add":end_date_3, "date_add_ms":Date.parse(end_date_3)} 
		};
		SliderConfigData.DatesMsForUpdate = { 
			1:SliderConfigData.EndDates[1]["date_ms"], 
			2:Date.parse(ReturnFirstDateOfWeekShiftWeeks(SliderConfigData.EndDates[2]["date"], 0)), 
			3:Date.parse(ReturnFirstDateOfMonthShiftMonths(SliderConfigData.EndDates[3]["date"], 0))
		};
		
		SliderConfigData.MoveParams={'CurrentPos':0, 'NextPos':0, 'MaxPos':0, 'MinPos':0, 'ObjectULWidth':0};
		SliderConfigData.FlagGetExtData={1:0,2:0,3:0};
	}
	//******************************************************************************
	function InitSliderReadyData() {
		SliderReadyData={};
		var my_date_start_ms=0;
		for(var act_view_key in SliderConfigData.StartDates) {
			SliderReadyData[act_view_key]={'IsDataLoad':false, 'MaxSteps':0, 
			'StepsCurrentMoment':0, 'SliderData':{}};
			my_date_start_ms = SliderConfigData.StartDates[act_view_key].date_ms;
			while(my_date_start_ms <= SliderConfigData.EndDates[act_view_key].date_ms) {
				SliderReadyData[act_view_key].SliderData[my_date_start_ms]={'CountDays':0, 
				'GoalSteps':0, 'GoalMaxSteps':0, 'Steps':0};
				
				if(act_view_key == 1) {
					SliderReadyData[act_view_key].SliderData[my_date_start_ms].CountDays=1;
					my_date_start_ms = Date.parse(ReturnDateShiftDays(my_date_start_ms, 1));
				}
				if(act_view_key == 2) {
					if(my_date_start_ms == SliderConfigData.DatesMsForUpdate[act_view_key] && ToDay.getDay() > 0) {
						SliderReadyData[act_view_key].SliderData[my_date_start_ms].CountDays=ToDay.getDay();
					} else {
						SliderReadyData[act_view_key].SliderData[my_date_start_ms].CountDays=7;
					}
					my_date_start_ms = Date.parse(ReturnDateShiftDays(my_date_start_ms, 7));
				}
				if(act_view_key == 3) {
					if(my_date_start_ms == SliderConfigData.DatesMsForUpdate[act_view_key]) {
						SliderReadyData[act_view_key].SliderData[my_date_start_ms].CountDays=ToDay.getDate();
					} else {
						SliderReadyData[act_view_key].SliderData[my_date_start_ms].CountDays=ReturnCountDaysOfMonth(my_date_start_ms);
					}
					my_date_start_ms = Date.parse(ReturnDateShiftMonths(my_date_start_ms, 1));
				}
			}
		}
	}
	//******************************************************************************
	function InitChartReadyData() {
		ChartReadyData={'ChartData':[], 'ChartColors':[], 'ChartTitle':[], 
		'ChartxLabels':[], 'ChartxAxisTitle':'', 'FlagIsDataReady':false, 'ChartActivityData':{}};
		
		ChartReadyData.ChartTitle = AppObjectTextsData.ChartLabelsObj.ChartTitle;
		ChartReadyData.ChartActivityData = {'FlagIsData':false, 'FlagIsDataReady':false, 
		'IndexForEachHour':[], 'DataByIndex':[], 
		'ObjImages':{'ImagesSrc':{}, 'FlagIsDataReady':false}
		};
		
		var my_index=0;
		for(var my_hour=0; my_hour < 24; my_hour++) {
			ChartReadyData.ChartActivityData.IndexForEachHour[my_hour] = my_index;
			if(!(my_index in ChartReadyData.ChartActivityData.DataByIndex)) {
				ChartReadyData.ChartActivityData.DataByIndex[my_index] = [];
				for(var act_index in PhysicalActEventsSupport.ForChartActivityData) {
					ChartReadyData.ChartActivityData.DataByIndex[my_index][act_index] = false;
				}
			}
			if(my_hour >= 5 && (my_hour%2) == 1) {
				my_index++;
			}
		}
	}
	//******************************************************************************
	function InitGoalStepsReadyData() {
		GoalStepsReadyData={};
		var my_date_start_ms=SliderConfigData.StartDates[3].date_ms;
		while(my_date_start_ms <= SliderConfigData.EndDates[3].date_ms) {
			GoalStepsReadyData[my_date_start_ms]={'GoalSteps':0, 'GoalMaxSteps':0};
			my_date_start_ms = Date.parse(ReturnDateShiftDays(my_date_start_ms, 1));
		}
	}
	//******************************************************************************
	function InitUserIteractionData() {
		UserIteractionData = {'applicationName':'myActivityApplication', 
			'iteractionData':{
				'activityNav':{
					'1':{'buttonId':'dayView', 'comment':'User selects Day view'}, 
					'2':{'buttonId':'weekView', 'comment':'User selects Week view'}, 
					'3':{'buttonId':'monthView', 'comment':'User selects Month view'}
				}, 
				'chartNav':{
					'1':{'buttonId':'stepsView', 'comment':'User selects Steps'}, 
					'2':{'buttonId':'kilometersView', 'comment':'User selects Kilometers'}, 
					'3':{'buttonId':'caloriesView', 'comment':'User select Calories'}
				}, 
				'sliderEl':{
					'1':{'buttonId':'specificDayView', 'comment':'User selects Specific Day'}, 
					'2':{'buttonId':'specificWeekView', 'comment':'User selects Specific Week'}, 
					'3':{'buttonId':'specificMonthView', 'comment':'User selects Specific Month'}
				}
			}
		};
	}
	//==============================================================================
	//CLEAR FUNCTIONS
	//==============================================================================
	function ClearChartAndActivityDataInChartReadyData() {
		ClearOnlyChartDataInChartReadyData();
		ClearOnlyChartActivityDataInChartReadyData();
	}
	//******************************************************************************
	function ClearOnlyChartDataInChartReadyData() {
		ChartReadyData.ChartData = [];
		ChartReadyData.ChartColors = [];
		ChartReadyData.ChartxLabels = [];
		ChartReadyData.ChartxAxisTitle = '';
		ChartReadyData.FlagIsDataReady = false;
	}
	//******************************************************************************
	function ClearOnlyChartActivityDataInChartReadyData() {
		ChartReadyData.ChartActivityData.FlagIsData = false;
		ChartReadyData.ChartActivityData.FlagIsDataReady = false;
		for(var data_index in ChartReadyData.ChartActivityData.DataByIndex) {
			for(var act_index in PhysicalActEventsSupport.ForChartActivityData) {
				ChartReadyData.ChartActivityData.DataByIndex[data_index][act_index] = false;
			}
		}
	}
	//==============================================================================
	//CREATE HTML ELEMENTS
	//==============================================================================
	function CreateAppBaseElements() {
		ElementAddOrRemoveLoading(AppObjectParams.ObjectsHtmlID.AppDataContainer, false);
		
		$('#AppDPAM_AppDataContainer').empty();
		$('#AppDPAM_AppDataContainer').append($('<div>').addClass('AppDPAM-BaseTop'));
		$('#AppDPAM_AppDataContainer').append($('<div>').addClass('AppDPAM-Parts'));
		$('#AppDPAM_AppDataContainer').append($('<div>').addClass('AppDPAM-overlay'));
		ViewOrHideOverlayElement(true);
		
		$('#AppDPAM_AppDataContainer .AppDPAM-Parts').append($('<div>').attr('id', 'AppDPAM-ExtData'));
		$('#AppDPAM_AppDataContainer .AppDPAM-Parts').append($('<div>').attr('id', 'AppDPAM-Activity'));
		$('#AppDPAM_AppDataContainer .AppDPAM-Parts').append($('<div>').attr('id', 'AppDPAM-Slider'));
		
		$('#AppDPAM-ExtData').append($('<div>').addClass('AppDPAM-Container'));
		$('#AppDPAM-Activity').append($('<div>').addClass('AppDPAM-Container'));
		$('#AppDPAM-Slider').append($('<div>').addClass('AppDPAM-SlBorder'));
		$('#AppDPAM-Slider').append($('<div>').addClass('AppDPAM-Container'));
		
		$('#AppDPAM-ExtData .AppDPAM-Container').append($('<div>').addClass('AppDPAM-Chart'));
		$('#AppDPAM-ExtData .AppDPAM-Container').append($('<div>').addClass('AppDPAM-Message'));
		$('#AppDPAM-ExtData .AppDPAM-Container').append($('<div>').addClass('AppDPAM-Legend'));
		
		$('#AppDPAM-ExtData .AppDPAM-Legend').append($('<ul>'));
		$('#AppDPAM-ExtData .AppDPAM-Legend ul').append($('<li>').addClass('AppDPAM-Legend-Box'));
		$('#AppDPAM-ExtData .AppDPAM-Legend ul li:last').append($('<p>').text(AppObjectTextsData.ColorLegendObj.Label));
		var ColorKey=0, ColorLegendBG='', ColorLegendBorder='', ColorLegendTxt='';
		for(var color_order_key in ColorObj.OrderColorKeys) {
			ColorKey          = ColorObj.OrderColorKeys[color_order_key];
			ColorLegendBG     = ColorObj.BasicColor[ColorKey];
			ColorLegendBorder = ColorObj.AdditionalColor[ColorKey];
			ColorLegendTxt    = '';
			
			if(ColorKey in AppObjectTextsData.ColorLegendObj.Colors) {
				ColorLegendTxt = AppObjectTextsData.ColorLegendObj.Colors[ColorKey];
			}
			
			$('#AppDPAM-ExtData .AppDPAM-Legend ul').append($('<li>'));
			$('#AppDPAM-ExtData .AppDPAM-Legend ul li:last').append($('<div>').css({'background-color':ColorLegendBG,'border':1, 'border-style':'solid', 'border-color':ColorLegendBorder}));
			$('#AppDPAM-ExtData .AppDPAM-Legend ul li:last').append($('<p>').text(ColorLegendTxt));
		}
		
		$('#AppDPAM-ExtData .AppDPAM-Message').append($('<div>').addClass('AppDPAM-header'));
		$('#AppDPAM-ExtData .AppDPAM-Message').append($('<div>').addClass('AppDPAM-content'));
		$('#AppDPAM-ExtData .AppDPAM-Message .AppDPAM-header').append($('<img>').attr('src', PathToExecuteDirectory+'images/AppDPAM_img_iga.png'));
		$('#AppDPAM-ExtData .AppDPAM-Message .AppDPAM-header').append($('<h1>').text(AppObjectTextsData.MessagesObj.Title));
		$('#AppDPAM-ExtData .AppDPAM-Message .AppDPAM-header').append($('<h2>').text(UserFirstName));
		$('#AppDPAM-ExtData .AppDPAM-Message .AppDPAM-content').append($('<p>').text(AppObjectTextsData.MessagesObj.Content));
		ElementDisplayOrNot(AppObjectParams.ObjectsHtmlID.MessagePart, false);
		
		$('#AppDPAM-ExtData .AppDPAM-Chart').append($('<div>').addClass('AppDPAM-VisPart'));
		$('#AppDPAM-ExtData .AppDPAM-Chart').append($('<div>').addClass('AppDPAM-ForLoad').append($('<div>')));
		$('#AppDPAM-ExtData .AppDPAM-Chart').append($('<div>').addClass('AppDPAM-Line'));
		$('#AppDPAM-ExtData .AppDPAM-Chart').append($('<div>').addClass('AppDPAM-NavPart'));
		$('#AppDPAM-ExtData .AppDPAM-Chart').append($('<div>').addClass('AppDPAM-EventPart'));
		
		var ChartElementID='', ChartNavElementID='';
		$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-VisPart').append($('<ul>'));
		$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-NavPart').append($('<ul>'));
		for(var chart_nav_key in AppObjectTextsData.ChartNavLabelsArr) {
			ChartElementID=AppObjectParams.ObjectsPartID.ChartID+'_'+((chart_nav_key*1)+1);
			ChartNavElementID=AppObjectParams.ObjectsPartID.NavChartID+'_'+((chart_nav_key*1)+1);
			$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-VisPart ul').append($('<li>').attr('id', ChartElementID));
			$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-NavPart ul').append($('<li>').attr('id', ChartNavElementID));
			$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-NavPart ul li:last').append($('<div>').append(AppObjectTextsData.ChartNavLabelsArr[chart_nav_key]));
		}
		ChartNavChangeStyleSelectedItem();
		
		var ActivityNavElementID='';
		$('#AppDPAM-Activity .AppDPAM-Container').append($('<ul>'));
		for(var act_nav_key in AppObjectTextsData.ActNavLabelsArr) {
			$('#AppDPAM-Activity ul').append($('<li>'));
			if(act_nav_key == 0) {
				$('#AppDPAM-Activity ul li:last').append($('<p>').text(AppObjectTextsData.ActNavLabelsArr[act_nav_key]));
			} else {
				ActivityNavElementID=AppObjectParams.ObjectsPartID.NavActivityID+'_'+(act_nav_key*1);
				$('#AppDPAM-Activity ul li:last').attr('id', ActivityNavElementID).addClass('AppDPAM-pointer');
				$('#AppDPAM-Activity ul li:last').append($('<div>').addClass('AppDPAM-el-left'));
				$('#AppDPAM-Activity ul li:last').append($('<div>').addClass('AppDPAM-el-txt').text(AppObjectTextsData.ActNavLabelsArr[act_nav_key]));
				$('#AppDPAM-Activity ul li:last').append($('<div>').addClass('AppDPAM-el-right'));
			}
		}
		ActivityNavChangeStyleSelectedItem();
		
		$('#AppDPAM-Slider .AppDPAM-Container').append($('<div>').addClass('AppDPAM-SlArrow').addClass('AppDPAM-SlLeft'));
		$('#AppDPAM-Slider .AppDPAM-Container').append($('<div>').addClass('AppDPAM-SlVisPart'));
		$('#AppDPAM-Slider .AppDPAM-Container').append($('<div>').addClass('AppDPAM-ForLoad').append($('<div>')));
		$('#AppDPAM-Slider .AppDPAM-Container').append($('<div>').addClass('AppDPAM-SlArrow').addClass('AppDPAM-SlRight'));
		
		CreateSliderElements();
		
		$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-NavPart ul li').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				var chart_nav_res = $(this).attr('id').split("_");
				if(chart_nav_res.length == 3) {ChartSelectedViewID = parseInt(chart_nav_res[2]);}
				
				ChartNavChangeStyleSelectedItem();
				LogUserInteraction('chartNav', ChartSelectedViewID);
				
				ChartViewSelectedItem();
			}
		});
		$('#AppDPAM-Activity ul li').click(function() { 
			if($(this).attr('id')) {
				if(CountStartedRequests == CountFinishedRequests) {
					var act_nav_res = $(this).attr('id').split("_");
					if(act_nav_res.length == 3) {ActivitySelectedViewID = parseInt(act_nav_res[2]);}
					
					ActivityNavChangeStyleSelectedItem();
					LogUserInteraction('activityNav', ActivitySelectedViewID);
					
					CountStartedRequests=3; CountFinishedRequests=0;
					ViewOrHideOverlayElement(true);
					CreateSliderElements();
				}
			}
		});
		$('#AppDPAM-Slider .AppDPAM-SlLeft').click(function() {
			if(CountStartedRequests == CountFinishedRequests) {
				SliderMoveLeftRightIScroll(1);
			}
		});
		$('#AppDPAM-Slider .AppDPAM-SlRight').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				SliderMoveLeftRightIScroll(0);
			}
		});
	}
	//==============================================================================
	function CreateSliderElements() {
		ElementDisplayOrNotAddOrRemoveLoading(AppObjectParams.ObjectsHtmlID.SliderForLoad, AppObjectParams.ObjectsHtmlID.SliderForLoad+' div', true, true);
		
		$('#AppDPAM-Slider .AppDPAM-SlVisPart').empty();
		$('#AppDPAM-Slider .AppDPAM-SlVisPart').append('<ul>');
		
		var my_date_ms = SliderConfigData.StartDates[ActivitySelectedViewID].date_ms, 
		my_date=new Date(my_date_ms), NumberItems=0, SliderElementID='', 
		el2_label='';
		while(my_date_ms <= SliderConfigData.EndDates[ActivitySelectedViewID].date_ms) {
			NumberItems = NumberItems+1;
			my_date = new Date(my_date_ms);
			
			if(ActivitySelectedViewID == 1) { 
				el2_label=ReturnDateStrFormatPrint(1, my_date);
			}
			if(ActivitySelectedViewID == 2) {
				el2_label=ReturnDateStrFormatPrint(2, my_date, ReturnDateShiftDays(my_date, 6));
			}
			if(ActivitySelectedViewID == 3) {
				el2_label=ReturnDateStrFormatPrint(3, my_date);
			}
					
			SliderElementID=AppObjectParams.ObjectsPartID.SliderID+'_'+my_date_ms;
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul').append($('<li>').attr('id', SliderElementID));
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li:last').append($('<div>').addClass('AppDPAM-SlEl'));
			
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li:last .AppDPAM-SlEl').append($('<div>').addClass('AppDPAM-el1'));
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li:last .AppDPAM-SlEl').append($('<div>').addClass('AppDPAM-el2'));
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li:last .AppDPAM-SlEl').append($('<div>').addClass('AppDPAM-column'));
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li:last .AppDPAM-SlEl').append($('<div>').addClass('AppDPAM-goal'));
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li:last .AppDPAM-SlEl').append($('<div>').addClass('AppDPAM-box'));
			
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li:last .AppDPAM-el1').append($('<label>'));
			$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li:last .AppDPAM-el2').append($('<label>').text(el2_label));
			
			if(ActivitySelectedViewID == 1)
				my_date_ms = Date.parse(ReturnDateShiftDays(my_date, 1));
			if(ActivitySelectedViewID == 2)
				my_date_ms = Date.parse(ReturnDateShiftDays(my_date, 7));
			if(ActivitySelectedViewID == 3)
				my_date_ms = Date.parse(ReturnDateShiftMonths(my_date, 1));
		}
		
		SliderConfigData.MoveParams.ObjectULWidth = AppObjectParams.SliderSizes.OneItemWidth*NumberItems;
		SliderConfigData.MoveParams.MaxPos = -(SliderConfigData.MoveParams.ObjectULWidth-AppObjectParams.SliderSizes.VisPartWidth);
		SliderConfigData.MoveParams.CurrentPos = SliderConfigData.MoveParams.MaxPos;
		
		$('#AppDPAM-Slider .AppDPAM-SlVisPart ul').css({'width':SliderConfigData.MoveParams.ObjectULWidth, 'left':0});
				
		if(myScrollHorizontal != null) {
			myScrollHorizontal.destroy();
			myScrollHorizontal=null;
		}
		myScrollHorizontal = new IScroll('#AppDPAM-Slider .AppDPAM-SlVisPart', {scrollX:true, scrollY:false, momentum:false, mouseWheel:true, startX:SliderConfigData.MoveParams.MaxPos, click:true});
		myScrollHorizontal.on('scrollEnd', function () {
			if(CountStartedRequests != CountFinishedRequests) {
				myScrollHorizontal.scrollTo(SliderConfigData.MoveParams.CurrentPos, 0);
			}
			if(SliderConfigData.MoveParams.CurrentPos > myScrollHorizontal.getScrollX()) {
				SliderConfigData.MoveParams.CurrentPos=Math.floor(myScrollHorizontal.getScrollX()/AppObjectParams.SliderSizes.OneItemWidth)*AppObjectParams.SliderSizes.OneItemWidth;
			} else {
				SliderConfigData.MoveParams.CurrentPos=Math.ceil(myScrollHorizontal.getScrollX()/AppObjectParams.SliderSizes.OneItemWidth)*AppObjectParams.SliderSizes.OneItemWidth;
			}
			myScrollHorizontal.scrollTo(SliderConfigData.MoveParams.CurrentPos, 0);
		});
		
		SliderSelectedObjectID = SliderConfigData.DatesMsForUpdate[ActivitySelectedViewID];
		SliderChangeStyleSelectedItem();
		
		SliderReadyData[ActivitySelectedViewID].IsDataLoad=false;
		GetExternalDataChart();
		GetExternalDataPhysicalActivity();
				
		$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li').click(function() {
			if(CountStartedRequests == CountFinishedRequests) {
				var slider_element_id_res = $(this).attr('id').split("_");
				if(slider_element_id_res.length == 3) {SliderSelectedObjectID = parseInt(slider_element_id_res[2]);}
				
				SliderCenterOneItemIScroll($(this).position().left);
				SliderChangeStyleSelectedItem();
				LogUserInteraction('sliderEl', ActivitySelectedViewID);
				
				CountStartedRequests=2; CountFinishedRequests=0;
				ViewOrHideOverlayElement(true);
				GetExternalDataChart();
				GetExternalDataPhysicalActivity();
			}
		});
	}
	//==============================================================================
	//CHANGE STYLE AND MOVE FUNCTIONS
	//==============================================================================
	function ChartNavChangeStyleSelectedItem() {
		$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-NavPart ul li').removeClass();
		$('#'+AppObjectParams.ObjectsPartID.NavChartID+'_'+ChartSelectedViewID).addClass("AppDPAM-el-sel");
	}
	//******************************************************************************
	function ChartViewSelectedItem() {
		var pos_top = -($('#'+AppObjectParams.ObjectsPartID.ChartID+'_'+ChartSelectedViewID).position().top);
		$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-VisPart ul').animate({top:pos_top}, 400, 'swing', function() { });
	}
	//******************************************************************************
	function ActivityNavChangeStyleSelectedItem() {
		$('#AppDPAM-Activity ul li').removeClass('AppDPAM-el-sel');
		$('#'+AppObjectParams.ObjectsPartID.NavActivityID+'_'+ActivitySelectedViewID).addClass("AppDPAM-el-sel");
	}
	//******************************************************************************
	function SliderChangeStyleSelectedItem() {
		$('#AppDPAM-Slider .AppDPAM-SlVisPart ul li').removeClass('AppDPAM-el-sel');
		$('#'+AppObjectParams.ObjectsPartID.SliderID+'_'+SliderSelectedObjectID).addClass("AppDPAM-el-sel");
	}
	//******************************************************************************
	function SliderMoveLeftRightIScroll(direction) {
		if(myScrollHorizontal == null) return false;
		SliderConfigData.MoveParams.CurrentPos=myScrollHorizontal.getScrollX();
		if(direction == 0) {
			if(SliderConfigData.MoveParams.CurrentPos > SliderConfigData.MoveParams.MaxPos) {
				SliderConfigData.MoveParams.NextPos = SliderConfigData.MoveParams.CurrentPos - AppObjectParams.SliderSizes.VisPartWidth;
				if(SliderConfigData.MoveParams.NextPos < SliderConfigData.MoveParams.MaxPos) SliderConfigData.MoveParams.NextPos = SliderConfigData.MoveParams.MaxPos;
				SliderConfigData.MoveParams.CurrentPos = SliderConfigData.MoveParams.NextPos;
				myScrollHorizontal.scrollTo(SliderConfigData.MoveParams.NextPos, 0, 400, IScroll.utils.ease.quadratic);
		}}
		if(direction == 1) {
			if(SliderConfigData.MoveParams.CurrentPos < SliderConfigData.MoveParams.MinPos) {
				SliderConfigData.MoveParams.NextPos = SliderConfigData.MoveParams.CurrentPos + AppObjectParams.SliderSizes.VisPartWidth;
				if(SliderConfigData.MoveParams.NextPos > SliderConfigData.MoveParams.MinPos) SliderConfigData.MoveParams.NextPos = SliderConfigData.MoveParams.MinPos;
				SliderConfigData.MoveParams.CurrentPos = SliderConfigData.MoveParams.NextPos;
				myScrollHorizontal.scrollTo(SliderConfigData.MoveParams.NextPos, 0, 400, IScroll.utils.ease.quadratic);
		}}
	}
	//******************************************************************************
	function SliderCenterOneItemIScroll(current_position) {
		if(myScrollHorizontal == null) return false;
		var new_position = -(current_position-((AppObjectParams.SliderSizes.VisPartWidth/2)-(AppObjectParams.SliderSizes.OneItemWidth/2)));
		if(new_position > 0) new_position = 0;
		if(new_position < -(SliderConfigData.MoveParams.ObjectULWidth-AppObjectParams.SliderSizes.VisPartWidth)) {
			new_position = -(SliderConfigData.MoveParams.ObjectULWidth-AppObjectParams.SliderSizes.VisPartWidth);
		}
		SliderConfigData.MoveParams.NextPos = new_position;
		SliderConfigData.MoveParams.CurrentPos = SliderConfigData.MoveParams.NextPos;
		myScrollHorizontal.scrollTo(new_position, 0, 400, IScroll.utils.ease.quadratic);
	}
	//==============================================================================
	//GET EXTERNAL DATA
	//==============================================================================
	function GetExternalDataGoalSteps() {
		var from_date_goal = ReturnDateStrFormatForGoalSteps(SliderConfigData.StartDates[3].date);
		var to_date_goal = ReturnDateStrFormatForGoalSteps(SliderConfigData.EndDates[3].date);
		var url_activity_goal = PathToActivityGoal+"/activitygoals";
		
		if(typeof ewallApp == 'object') {
			var ReqGoalStepsDPAM = ewallApp.ajax({
				url: url_activity_goal,
				type: "GET",
				data: {userid: UserName, from: from_date_goal, to: to_date_goal},
				dataType: "json",
				async: true,
				cache: true
			});
		} else {
			var ReqGoalStepsDPAM = $.ajax({
				url: url_activity_goal,
				headers: {'X-Auth-Token':AppToken},
				type: "GET",
				data: {userid: UserName, from: from_date_goal, to: to_date_goal}, 
				dataType: "json", 
				async: true,
				cache: true
			});
		}
		
		$.when(ReqGoalStepsDPAM).done(function (DataGoalStepsDPAM) {
			ProcessExternalDataGoalSteps(DataGoalStepsDPAM);
		});
	}
	//******************************************************************************
	function GetExternalDataSliderSteps() {
		if(SliderConfigData.FlagGetExtData[ActivitySelectedViewID] == 1) {
			LoadReadyDataSlider();
			return true;
		}
		
		var current_aggregation = "";
		if(ActivitySelectedViewID == 1) {
			current_aggregation = '1d';
		}
		if(ActivitySelectedViewID == 2) {
			current_aggregation = '1wk';
		}
		if(ActivitySelectedViewID == 3) {
			current_aggregation = '1mo';
		}
		
		var from_date = ReturnDateStrFormatForServiceBrick(SliderConfigData.StartDates[ActivitySelectedViewID].date);
		var to_date = ReturnDateStrFormatForServiceBrick(SliderConfigData.EndDates[ActivitySelectedViewID].date_add);
		var url_walking_distance = PathToSB_PhysicalActivity+"/v1/"+UserName+"/movement";
		
		if(typeof ewallApp == 'object') {
			var ReqWalkingDistanceSlider = ewallApp.ajax({
				url: url_walking_distance,
				type: "GET",
				data: {from: from_date, to: to_date, aggregation:current_aggregation},
				dataType: "json", 
				async: true,
				cache: true
			});
		} else {
			var ReqWalkingDistanceSlider = $.ajax({
				url: url_walking_distance,
				headers: {'X-Auth-Token':AppToken},
				type: "GET",
				data: {from: from_date, to: to_date, aggregation:current_aggregation},
				dataType: "json", 
				async: true,
				cache: true
			});
		}
		
		$.when(ReqWalkingDistanceSlider).done(function (DataWalkingDistanceSlider) {
			SliderConfigData.FlagGetExtData[ActivitySelectedViewID]=1;
			ProcessExternalDataSliderSteps(DataWalkingDistanceSlider["movements"]);
		});
	}
	//******************************************************************************
	function GetExternalDataChart() {
		ElementDisplayOrNotAddOrRemoveLoading(AppObjectParams.ObjectsHtmlID.ChartForLoad, AppObjectParams.ObjectsHtmlID.ChartForLoad+' div', true, true);
		ElementDisplayOrNot(AppObjectParams.ObjectsHtmlID.MessagePart, false);
		
		var current_aggregation = "";
		var from_date_ms = SliderSelectedObjectID, to_date_ms=0;
		
		if(ActivitySelectedViewID == 1) {
			current_aggregation = '2h';
			to_date_ms = Date.parse(ReturnDateShiftDays(from_date_ms, 1));
		}
		if(ActivitySelectedViewID == 2) {
			current_aggregation = '1d';
			to_date_ms = Date.parse(ReturnDateShiftDays(from_date_ms, 7));
		}
		if(ActivitySelectedViewID == 3) {
			current_aggregation = '1d';
			to_date_ms = Date.parse(ReturnDateShiftMonths(from_date_ms, 1));
		}
		
		var from_date_format = ReturnDateStrFormatForServiceBrick(from_date_ms);
		var to_date_format = ReturnDateStrFormatForServiceBrick(to_date_ms);
		
		var url_walking_distance = PathToSB_PhysicalActivity+"/v1/"+UserName+"/movement";
			
		if(typeof ewallApp == 'object') {
			var ReqWalkingDistanceChart = ewallApp.ajax({
				url: url_walking_distance,
				type: "GET",
				data: {from: from_date_format, to: to_date_format, aggregation:current_aggregation},
				dataType: "json", 
				async: true,
				cache: true
			});
		} else {
			var ReqWalkingDistanceChart = $.ajax({
				url: url_walking_distance,
				headers: {'X-Auth-Token':AppToken},
				type: "GET",
				data: {from: from_date_format, to: to_date_format, aggregation:current_aggregation},
				dataType: "json", 
				async: true,
				cache: true
			});
		}
		
		$.when(ReqWalkingDistanceChart).done(function (DataWalkingDistanceChart) {
			ProcessSliderDataStepsCurrentMoment(DataWalkingDistanceChart["movements"]);
		});
	}
	//******************************************************************************
	function GetExternalDataPhysicalActivity() {
		ElementClearContent(AppObjectParams.ObjectsHtmlID.EventPart);
		if(ActivitySelectedViewID != 1) {
			CountFinishedRequests = CountFinishedRequests + 1;
			return true;
		}
		ElementAddOrRemoveLoading(AppObjectParams.ObjectsHtmlID.EventPart, true);
		
		var from_date_ms = SliderSelectedObjectID, 
		to_date_ms=Date.parse(ReturnDateShiftDays(from_date_ms, 1));
		
		var from_date_format = ReturnDateStrFormatForServiceBrick(from_date_ms);
		var to_date_format = ReturnDateStrFormatForServiceBrick(to_date_ms);
		
		var url_physical_activity = PathToSB_PhysicalActivity+"/v1/"+UserName+"/physicalactivity";
		
		if(typeof ewallApp == 'object') {
			var ReqPhysicalActivity = ewallApp.ajax({
				url: url_physical_activity,
				type: "GET",
				data: {from: from_date_format, to: to_date_format},
				dataType: "json", 
				async: true,
				cache: true
			});
		} else {
			var ReqPhysicalActivity = $.ajax({
				url: url_physical_activity,
				headers: {'X-Auth-Token':AppToken},
				type: "GET",
				data: {from: from_date_format, to: to_date_format},
				dataType: "json", 
				async: true,
				cache: true
			});
		}
		
		$.when(ReqPhysicalActivity).done(function (DataPhysicalActivity) {
			LoadExternalDataPhysicalActivity(DataPhysicalActivity["physicalActivityEvents"]);
		});
	}
	//==============================================================================
	//PROCESS EXTERNAL DATA
	//==============================================================================
	function ProcessExternalDataGoalSteps(ExtDataGoalSteps) {
		var ext_date='', ext_date_ms=0, ext_goal=0, local_ext_data={};
		for(var ext_key in ExtDataGoalSteps) {
			ext_date    = ReturnDateConvertFromSB(ExtDataGoalSteps[ext_key]["date"]);
			ext_date_ms = Date.parse(ext_date);
			ext_goal    = ExtDataGoalSteps[ext_key]["goal"];
			local_ext_data[ext_date_ms]=ext_goal;
			if(ext_date_ms in GoalStepsReadyData) {
				GoalStepsReadyData[ext_date_ms].GoalSteps=ext_goal;
				GoalStepsReadyData[ext_date_ms].GoalMaxSteps=CalculateMaxStepsBasedGoal(ext_goal);
			}
		}
		
		var local_date_ms=SliderConfigData.StartDates[3].date_ms;
		var start_date_ms_day = SliderConfigData.StartDates[1].date_ms, 
		end_date_ms_day = Date.parse(ReturnDateShiftDays(start_date_ms_day, 1)), 
		start_date_ms_week = SliderConfigData.StartDates[2].date_ms, 
		end_date_ms_week = Date.parse(ReturnDateShiftDays(start_date_ms_week, 6)), 
		start_date_ms_month = SliderConfigData.StartDates[3].date_ms, 
		end_date_ms_month = Date.parse(ReturnLastDateOfMonthShiftMonths(start_date_ms_month, 1));
		
		var flag_stop_week=true, flag_stop_month=true, 
		goal_steps=0, max_steps=0, goal_steps_week=0, goal_steps_month=0, max_steps_week=0, max_steps_month=0;
		
		while(local_date_ms <= SliderConfigData.EndDates[3].date_ms) {
			if(local_date_ms in local_ext_data) goal_steps=local_ext_data[local_date_ms];
			else goal_steps=0;
			
			if(local_date_ms >= start_date_ms_week && local_date_ms <= end_date_ms_week) {
				goal_steps_week = goal_steps_week + goal_steps;
			}
			if(local_date_ms >= start_date_ms_month && local_date_ms <= end_date_ms_month) {
				goal_steps_month = goal_steps_month + goal_steps;
			}
			
			if(local_date_ms >= start_date_ms_day) {
				if(local_date_ms in SliderReadyData[1].SliderData) {
					max_steps = CalculateMaxStepsBasedGoal(goal_steps);
					SliderReadyData[1].SliderData[local_date_ms].GoalSteps=goal_steps;
					SliderReadyData[1].SliderData[local_date_ms].GoalMaxSteps=max_steps;
					
					if(max_steps > SliderReadyData[1].MaxSteps)
						SliderReadyData[1].MaxSteps = max_steps;
				}
				start_date_ms_day = end_date_ms_day;
				end_date_ms_day = Date.parse(ReturnDateShiftDays(start_date_ms_day, 1));
			}
			
			if(local_date_ms == end_date_ms_week) {
				if(start_date_ms_week in SliderReadyData[2].SliderData) {
					goal_steps_week = Math.round(goal_steps_week/SliderReadyData[2].SliderData[start_date_ms_week].CountDays);
					max_steps_week = CalculateMaxStepsBasedGoal(goal_steps_week);
					SliderReadyData[2].SliderData[start_date_ms_week].GoalSteps = goal_steps_week;
					SliderReadyData[2].SliderData[start_date_ms_week].GoalMaxSteps = max_steps_week;
					
					if(max_steps_week > SliderReadyData[2].MaxSteps)
						SliderReadyData[2].MaxSteps = max_steps_week;
				}
				start_date_ms_week = Date.parse(ReturnDateShiftDays(start_date_ms_week, 7)); 
				end_date_ms_week = Date.parse(ReturnDateShiftDays(start_date_ms_week, 6));
				goal_steps_week=0; max_steps_week=0;
			} else if(local_date_ms >= SliderConfigData.EndDates[2].date_ms && flag_stop_week) {
				if(start_date_ms_week in SliderReadyData[2].SliderData) {
					goal_steps_week = Math.round(goal_steps_week/SliderReadyData[2].SliderData[start_date_ms_week].CountDays);
					max_steps_week = CalculateMaxStepsBasedGoal(goal_steps_week);
					SliderReadyData[2].SliderData[start_date_ms_week].GoalSteps = goal_steps_week;
					SliderReadyData[2].SliderData[start_date_ms_week].GoalMaxSteps = max_steps_week;
					
					if(max_steps_week > SliderReadyData[2].MaxSteps)
						SliderReadyData[2].MaxSteps = max_steps_week;
				}
				flag_stop_week = false;
			}
			
			if(local_date_ms == end_date_ms_month) {
				if(start_date_ms_month in SliderReadyData[3].SliderData) {
					goal_steps_month = Math.round(goal_steps_month/SliderReadyData[3].SliderData[start_date_ms_month].CountDays);
					max_steps_month = CalculateMaxStepsBasedGoal(goal_steps_month);
					SliderReadyData[3].SliderData[start_date_ms_month].GoalSteps = goal_steps_month;
					SliderReadyData[3].SliderData[start_date_ms_month].GoalMaxSteps = max_steps_month;
					
					if(max_steps_month > SliderReadyData[3].MaxSteps)
						SliderReadyData[3].MaxSteps = max_steps_month;
				}
				start_date_ms_month = Date.parse(ReturnFirstDateOfMonthShiftMonths(start_date_ms_month, 1));
				end_date_ms_month = Date.parse(ReturnLastDateOfMonthShiftMonths(start_date_ms_month, 1));
				goal_steps_month=0; max_steps_month=0;
			} else if(local_date_ms >= SliderConfigData.EndDates[3].date_ms && flag_stop_month) {
				if(start_date_ms_month in SliderReadyData[3].SliderData) {
					goal_steps_month = Math.round(goal_steps_month/SliderReadyData[3].SliderData[start_date_ms_month].CountDays);
					max_steps_month = CalculateMaxStepsBasedGoal(goal_steps_month);
					SliderReadyData[3].SliderData[start_date_ms_month].GoalSteps = goal_steps_month;
					SliderReadyData[3].SliderData[start_date_ms_month].GoalMaxSteps = max_steps_month;
					
					if(max_steps_month > SliderReadyData[3].MaxSteps)
						SliderReadyData[3].MaxSteps = max_steps_month;
				}
				flag_stop_month=false;
			}
			local_date_ms = Date.parse(ReturnDateShiftDays(local_date_ms, 1));
		}
		
		CreateAppBaseElements();
	}
	//******************************************************************************
	function ProcessSliderDataStepsCurrentMoment(ExtDataChart) {
		LocalChartExternalData={};
		var ext_date='', ext_date_ms=0, ext_value=0, steps_for_update=0;
		for(var ext_key in ExtDataChart) {
			ext_date    = ReturnDateConvertFromServiceBrickWithT(ExtDataChart[ext_key]["from"]);
			ext_date_ms = Date.parse(ext_date);
			LocalChartExternalData[ext_date_ms]=[];
			for(var epn_key in ExternalParamNames) {
				if(epn_key == 1) /*kilometers*/ {
					ext_value = parseInt(ExtDataChart[ext_key][ExternalParamNames[epn_key]]*100)/100;
				} else {
					ext_value = parseInt(ExtDataChart[ext_key][ExternalParamNames[epn_key]]);
				}
				
				if(ext_value < 0) ext_value = 0;
				
				LocalChartExternalData[ext_date_ms][epn_key]=ext_value;
				if(epn_key == 0) /*steps*/ {
					steps_for_update=steps_for_update+ext_value;
				}
			}
		}
		
		var FlagToLoadReadyDataSlider=false;
		if(SliderSelectedObjectID == SliderConfigData.DatesMsForUpdate[ActivitySelectedViewID]) {
			steps_for_update = Math.round(steps_for_update/SliderReadyData[ActivitySelectedViewID].SliderData[SliderSelectedObjectID].CountDays);
			SliderReadyData[ActivitySelectedViewID].StepsCurrentMoment = steps_for_update;
			
			if(SliderReadyData[ActivitySelectedViewID].IsDataLoad) {
				if(steps_for_update > SliderReadyData[ActivitySelectedViewID].MaxSteps) {
					FlagToLoadReadyDataSlider=true;
				}
				if(steps_for_update > SliderReadyData[ActivitySelectedViewID].SliderData[SliderSelectedObjectID].Steps) {
					FlagToLoadReadyDataSlider=true;
				}
				
				if(FlagToLoadReadyDataSlider) {
					LoadReadyDataSlider(FlagToLoadReadyDataSlider);
				} else {
					ProcessExternalDataChart();
				}
			} else {
				GetExternalDataSliderSteps();
			}
		} else {
			ProcessExternalDataChart();
		}
	}
	//******************************************************************************
	function ProcessExternalDataChart() {
		ClearOnlyChartDataInChartReadyData();
		
		if(SliderReadyData[ActivitySelectedViewID].SliderData[SliderSelectedObjectID].GoalSteps > 0 && SliderReadyData[ActivitySelectedViewID].SliderData[SliderSelectedObjectID].Steps >= SliderReadyData[ActivitySelectedViewID].SliderData[SliderSelectedObjectID].GoalSteps) {
			ElementDisplayOrNot(AppObjectParams.ObjectsHtmlID.MessagePart, true);
		}
		
		var start_date_ms=SliderSelectedObjectID, end_date_ms=0, current_date_ms=start_date_ms;
		if(ActivitySelectedViewID == 1) {
			end_date_ms = Date.parse(ReturnDateShiftHours(start_date_ms, 22));
			ChartReadyData.ChartxAxisTitle = ReturnDateStrFormatPrint(4, start_date_ms);
		}
		if(ActivitySelectedViewID == 2) {
			end_date_ms = Date.parse(ReturnDateShiftDays(start_date_ms, 6));
			ChartReadyData.ChartxAxisTitle = ReturnDateStrFormatPrint(5, start_date_ms, end_date_ms);
		}
		if(ActivitySelectedViewID == 3) {
			end_date_ms = Date.parse(ReturnLastDateOfMonthShiftMonths(start_date_ms, 1));
			ChartReadyData.ChartxAxisTitle = ReturnDateStrFormatPrint(3, start_date_ms);
		}
		
		var br=0, br_index=0, local_chart_max_steps=[], max_steps_hours=0, str_xLabel="";
		if(ActivitySelectedViewID == 1) {
			if(start_date_ms in GoalStepsReadyData && GoalStepsReadyData[start_date_ms].GoalMaxSteps > 0) {
				max_steps_hours = Math.round(GoalStepsReadyData[start_date_ms].GoalMaxSteps/12);
			} else if(start_date_ms in SliderReadyData[ActivitySelectedViewID].SliderData && SliderReadyData[ActivitySelectedViewID].SliderData[start_date_ms].GoalMaxSteps > 0) {
				max_steps_hours = Math.round(SliderReadyData[ActivitySelectedViewID].SliderData[start_date_ms].GoalMaxSteps/12);
			} else {
				max_steps_hours = Math.round(SliderReadyData[ActivitySelectedViewID].MaxSteps/12);
			}
		}
		while(current_date_ms <= end_date_ms) {
			br++;
			for(var epn_key in ExternalParamNames) {
				if(!(epn_key in ChartReadyData.ChartData)) {
					ChartReadyData.ChartData[epn_key]=[];
				}
				if(!(br_index in ChartReadyData.ChartData[epn_key])) {
					ChartReadyData.ChartData[epn_key][br_index]=0;
				}
				if(current_date_ms in LocalChartExternalData) {
					ChartReadyData.ChartData[epn_key][br_index]=ChartReadyData.ChartData[epn_key][br_index]+LocalChartExternalData[current_date_ms][epn_key];
				}
			}
			
			if(!(br_index in local_chart_max_steps)) {
				local_chart_max_steps[br_index]=0;
			}
			if(ActivitySelectedViewID == 1) {
				if(br_index == 0) {
					local_chart_max_steps[br_index]=(max_steps_hours*3);
				} else {
					local_chart_max_steps[br_index]=max_steps_hours;
				}
			} else {
				if(current_date_ms <= SliderConfigData.EndDates[ActivitySelectedViewID].date_ms) {
					if(current_date_ms in GoalStepsReadyData && GoalStepsReadyData[current_date_ms].GoalMaxSteps > 0) {
						local_chart_max_steps[br_index]=local_chart_max_steps[br_index]+GoalStepsReadyData[current_date_ms].GoalMaxSteps;
					} else if(start_date_ms in SliderReadyData[ActivitySelectedViewID].SliderData && SliderReadyData[ActivitySelectedViewID].SliderData[start_date_ms].GoalMaxSteps > 0) {
						local_chart_max_steps[br_index]=local_chart_max_steps[br_index]+SliderReadyData[ActivitySelectedViewID].SliderData[start_date_ms].GoalMaxSteps;
					} else {
						local_chart_max_steps[br_index]=local_chart_max_steps[br_index]+SliderReadyData[ActivitySelectedViewID].MaxSteps;
					}
				}
			}
			
			if(ActivitySelectedViewID == 1) {
				if(br >= 3) {
					if(br == 3) {
						str_xLabel = '00';
					} else {
						str_xLabel = ((br-1)*2) < 10 ? '0'+((br-1)*2) : ((br-1)*2);
					}
					str_xLabel += '-';
					str_xLabel += (br*2) < 10 ? '0'+(br*2) : (br*2);
					str_xLabel += ' '+AppObjectTextsData.AppLabelsObj.Shorthour;
					ChartReadyData.ChartxLabels[br_index]=str_xLabel;
					br_index++;
				}
				current_date_ms = Date.parse(ReturnDateShiftHours(current_date_ms, 2));
			}
			if(ActivitySelectedViewID == 2) {
				if((br_index+1) == AppObjectTextsData.LongDaysArr.length) {
					ChartReadyData.ChartxLabels[br_index]=AppObjectTextsData.LongDaysArr[0];
				} else {
					ChartReadyData.ChartxLabels[br_index]=AppObjectTextsData.LongDaysArr[(br_index+1)];
				}
				br_index++;
				current_date_ms = Date.parse(ReturnDateShiftDays(current_date_ms, 1));
			}
			if(ActivitySelectedViewID == 3) {
				if((br%3)==1) {
					str_xLabel = br < 10 ? '0'+br : br;
					str_xLabel += '-';
				}
				if((br%3)==0) {
					str_xLabel += br < 10 ? '0'+br : br;
					ChartReadyData.ChartxLabels[br_index]=str_xLabel;
					br_index++;
				}
				current_date_ms = Date.parse(ReturnDateShiftDays(current_date_ms, 1));
			}
		}
		
		LocalChartExternalData={};
		
		var percentage=0, max_steps=0, color_index=0;
		for(var record_index in local_chart_max_steps) {
			max_steps = local_chart_max_steps[record_index];
			if(max_steps > 0) {
				percentage = Math.round((ChartReadyData.ChartData[0][record_index]*100)/max_steps);
			} else {
				percentage = 0;
			}
			color_index = ReturnColorIndex(percentage);
			ChartReadyData.ChartColors[record_index]=ColorObj.BasicColor[color_index];
		}
		
		if(ActivitySelectedViewID == 3) {
			var last_day_month = ReturnCountDaysOfMonth(start_date_ms);
			if(ChartReadyData.ChartColors.length != ChartReadyData.ChartxLabels.length) {
				str_xLabel += last_day_month;
				ChartReadyData.ChartxLabels[ChartReadyData.ChartxLabels.length]=str_xLabel;
			}
			
			var aggregation_days=3, count_elements = Math.ceil(last_day_month/aggregation_days),  
			local_chart_divisor_arr=[], divisor=3, count_days_by_index=0, 
			last_day_month_selected = last_day_month;
			if(SliderSelectedObjectID == SliderConfigData.DatesMsForUpdate[ActivitySelectedViewID]) {
				last_day_month_selected = SliderConfigData.EndDates[ActivitySelectedViewID].date.getDate();
			}
			for(var aggregation_index = 0; aggregation_index < count_elements; aggregation_index++) {
				divisor=3;
				count_days_by_index = (aggregation_index+1)*divisor;
				if(count_days_by_index > last_day_month_selected) {
					divisor = aggregation_days - (count_days_by_index - last_day_month_selected);
					if(divisor < 0) divisor = 0;
				}
				
				if(divisor > 0) {
					for(var epn_key in ChartReadyData.ChartData) {
						if(epn_key == 1) {
							ChartReadyData.ChartData[epn_key][aggregation_index]=Math.round((ChartReadyData.ChartData[epn_key][aggregation_index]*100)/divisor)/100;
						} else {
							ChartReadyData.ChartData[epn_key][aggregation_index]=Math.round(ChartReadyData.ChartData[epn_key][aggregation_index]/divisor);
						}
					}
				}
			}
		}
		ChartReadyData.FlagIsDataReady = true;
		
		LoadReadyDataChart();
	}
	//******************************************************************************
	function ProcessExternalDataSliderSteps(ExtDataSliderSteps) {
		var ext_date='', ext_steps=0, ext_date_ms=0;
		for(var ext_steps_key in ExtDataSliderSteps) {
			ext_date = ReturnDateConvertFromServiceBrickWithT(ExtDataSliderSteps[ext_steps_key]["from"]);
			ext_steps = parseInt(ExtDataSliderSteps[ext_steps_key]["steps"]);
			ext_date_ms = Date.parse(ext_date);
			
			if(ext_steps < 0) ext_steps = 0;
			
			if(ext_date_ms in SliderReadyData[ActivitySelectedViewID].SliderData) {
				ext_steps = Math.round(ext_steps/SliderReadyData[ActivitySelectedViewID].SliderData[ext_date_ms].CountDays);
				SliderReadyData[ActivitySelectedViewID].SliderData[ext_date_ms].Steps=ext_steps;
				
				if(ext_steps > SliderReadyData[ActivitySelectedViewID].SliderData[ext_date_ms].GoalMaxSteps) {
					if(ext_steps > SliderReadyData[ActivitySelectedViewID].MaxSteps) {
						SliderReadyData[ActivitySelectedViewID].MaxSteps = ext_steps;
					}
				} else if(SliderReadyData[ActivitySelectedViewID].SliderData[ext_date_ms].GoalMaxSteps > SliderReadyData[ActivitySelectedViewID].MaxSteps) {
					SliderReadyData[ActivitySelectedViewID].MaxSteps = SliderReadyData[ActivitySelectedViewID].SliderData[ext_date_ms].GoalMaxSteps;
				}
			}
		}
		
		LoadReadyDataSlider();
	}
	//==============================================================================
	//LOAD EXTERNAL OR READY DATA
	//==============================================================================
	function LoadReadyDataSlider(FlagReloadData) {
		if(SliderReadyData[ActivitySelectedViewID].StepsCurrentMoment > SliderReadyData[ActivitySelectedViewID].MaxSteps) {
			SliderReadyData[ActivitySelectedViewID].MaxSteps = SliderReadyData[ActivitySelectedViewID].StepsCurrentMoment;
		}
		if(SliderReadyData[ActivitySelectedViewID].StepsCurrentMoment > SliderReadyData[ActivitySelectedViewID].SliderData[SliderSelectedObjectID].Steps) {
			SliderReadyData[ActivitySelectedViewID].SliderData[SliderSelectedObjectID].Steps = SliderReadyData[ActivitySelectedViewID].StepsCurrentMoment;
		}
			
		var element_id='', html_element_id='', label_steps='', str_steps='', str_goal_steps='',  
		steps=0, goal_steps=0, goal_max_steps=0, color_index=0, percentage=0, 
		max_steps=SliderReadyData[ActivitySelectedViewID].MaxSteps;
		
		var MaxColumnHeight=152, MinColumnHeight=6, MinColumnTop=28, DistanceGoalTop=11, 
		MinGoalTop=(29+MinColumnTop-DistanceGoalTop), MaxGoalTop=155, GoalHeight=24;
		
		var css_goal_top=155, css_column_top=0, css_column_height=0, 
		css_bgcolor_column1=ColorObj.AdditionalColor[color_index], 
		css_bgcolor_column2=ColorObj.BasicColor[color_index];
		
		if(ActivitySelectedViewID == 1) {
			label_steps=AppObjectTextsData.AppLabelsObj.Steps;
		} else {
			label_steps ='<br>';
			label_steps+=AppObjectTextsData.AppLabelsObj.Steps+' / ';
			label_steps+=AppObjectTextsData.ActNavLabelsArr[1];
			
			MaxColumnHeight = MaxColumnHeight - 20;
			MinColumnTop = MinColumnTop + 20;
			MinGoalTop = MinGoalTop + 20;
		}
		
		for(var element_key in SliderReadyData[ActivitySelectedViewID].SliderData) {
			steps = SliderReadyData[ActivitySelectedViewID].SliderData[element_key].Steps;
			goal_steps = SliderReadyData[ActivitySelectedViewID].SliderData[element_key].GoalSteps;
			goal_max_steps = SliderReadyData[ActivitySelectedViewID].SliderData[element_key].GoalMaxSteps;
			
			element_id = AppObjectParams.ObjectsPartID.SliderID+'_'+element_key;
			html_element_id = '#AppDPAM-Slider .AppDPAM-SlVisPart #'+element_id;
			
			str_steps = ReturnNumberToStringWithThousandSeparator(steps)+' '+label_steps;
			$(html_element_id+' .AppDPAM-el1 label').empty();
			$(html_element_id+' .AppDPAM-el1 label').html(str_steps);
			
			$(html_element_id+' .AppDPAM-column').empty();
			if(steps > 0) {
				if(goal_max_steps > 0) percentage = Math.round((steps*100)/goal_max_steps);
				else percentage = Math.round((steps*100)/max_steps);
				
				color_index = ReturnColorIndex(percentage);
				css_column_height = Math.round((steps*MaxColumnHeight)/max_steps);
				
				if(css_column_height < MinColumnHeight) css_column_height = MinColumnHeight;
				if(css_column_height > MaxColumnHeight) css_column_height = MaxColumnHeight;
				
				css_column_top = (MaxColumnHeight - css_column_height) + MinColumnTop;
				css_bgcolor_column1 = ColorObj.AdditionalColor[color_index];
				css_bgcolor_column2 = ColorObj.BasicColor[color_index];
				
				$(html_element_id+' .AppDPAM-column').append($('<div>').addClass('AppDPAM-column-c1'));
				$(html_element_id+' .AppDPAM-column').append($('<div>').addClass('AppDPAM-column-c2'));
				$(html_element_id+' .AppDPAM-column').css({'height':css_column_height, 'top':css_column_top});
				$(html_element_id+' .AppDPAM-column .AppDPAM-column-c1').css({'height':css_column_height, 'background-color':css_bgcolor_column1});
				$(html_element_id+' .AppDPAM-column .AppDPAM-column-c2').css({'height':(css_column_height-1), 'background-color':css_bgcolor_column2});
			} else {
				$(html_element_id+' .AppDPAM-column').css({'height':0, 'top':0});
			}
			
			$(html_element_id+' .AppDPAM-goal').empty();
			if(goal_steps > 0) {
				str_goal_steps = ReturnNumberToStringWithThousandSeparator(goal_steps);
				css_goal_top = (MinColumnTop + MaxColumnHeight - DistanceGoalTop) - Math.round((goal_steps*MaxColumnHeight)/max_steps);
				if(css_goal_top < MinGoalTop) css_goal_top = MinGoalTop;
				if(css_goal_top > MaxGoalTop) css_goal_top = MaxGoalTop;
				
				$(html_element_id+' .AppDPAM-goal').css({'height':GoalHeight, 'top':css_goal_top});
				$(html_element_id+' .AppDPAM-goal').append($('<div>').addClass('AppDPAM-line'));
				$(html_element_id+' .AppDPAM-goal').append($('<label>').text(str_goal_steps));
			} else {
				$(html_element_id+' .AppDPAM-goal').css({'height':0, 'top':0});
			}
		}
		
		SliderReadyData[ActivitySelectedViewID].IsDataLoad = true;
		if(arguments.length <= 0 || !FlagReloadData) {
			ElementDisplayOrNotAddOrRemoveLoading(AppObjectParams.ObjectsHtmlID.SliderForLoad, AppObjectParams.ObjectsHtmlID.SliderForLoad+' div', false, false);
			CountFinishedRequests = CountFinishedRequests + 1;
		}
		ProcessExternalDataChart();
	}
	//******************************************************************************
	function LoadExternalDataPhysicalActivity(ExtDataPhysicalActivity) {
		ClearOnlyChartActivityDataInChartReadyData();
		
		var local_ext_data={}, ext_data_length = ExtDataPhysicalActivity.length;
		for(var act_event_key in PhysicalActEventsSupport.ForActivityDuration) {
			local_ext_data[act_event_key] = 0;
		}
		
		var ext_activity='', ext_activity_key=-1, flag_is_ext_data=false, 
		ext_timestamp='', ext_timestamp_hours=0, ext_timestamp_next='', activity_duration=0, 
		chart_index=0, chart_activity_key=-1;
		
		for(var ext_key in ExtDataPhysicalActivity) {
			ext_activity = ExtDataPhysicalActivity[ext_key]["activityType"].toLowerCase();
			
			ext_activity_key = $.inArray(ext_activity, PhysicalActEventsSupport.ForActivityDuration);
			if(ext_activity_key < 0) {
				continue;
			}
			
			ext_timestamp = ReturnDateConvertFromServiceBrickWithT(ExtDataPhysicalActivity[ext_key]["timestamp"]);
			ext_timestamp.setMilliseconds(0);
			ext_timestamp_hours = ext_timestamp.getHours();
			if(ext_timestamp_hours >= 6) {
				activity_duration = 0;
				if((parseInt(ext_key)+1) < ext_data_length) {
					ext_timestamp_next = ReturnDateConvertFromServiceBrickWithT(ExtDataPhysicalActivity[(parseInt(ext_key)+1)]["timestamp"]);
					ext_timestamp_next.setMilliseconds(0);
				} else if(SliderSelectedObjectID != Date.parse(ToDay)) {
					ext_timestamp_next = ReturnDateShiftDays(SliderSelectedObjectID, 1);
				} else {
					ext_timestamp_next = ext_timestamp;
				}
				
				activity_duration = ext_timestamp_next - ext_timestamp;
				if(activity_duration > 0) {
					local_ext_data[ext_activity_key] = local_ext_data[ext_activity_key] + activity_duration;
					
					if(!flag_is_ext_data) flag_is_ext_data = true;
				}
				
				chart_activity_key = $.inArray(ext_activity, PhysicalActEventsSupport.ForChartActivityData);
				if(chart_activity_key >= 0) {
					if(ext_timestamp_hours in ChartReadyData.ChartActivityData.IndexForEachHour) {
						chart_index = ChartReadyData.ChartActivityData.IndexForEachHour[ext_timestamp_hours];
						
						if(!ChartReadyData.ChartActivityData.FlagIsData)
							ChartReadyData.ChartActivityData.FlagIsData = true;
						
						if(!ChartReadyData.ChartActivityData.DataByIndex[chart_index][chart_activity_key])
							ChartReadyData.ChartActivityData.DataByIndex[chart_index][chart_activity_key] = true;
					}
				}
			}
		}
		
		ChartReadyData.ChartActivityData.FlagIsDataReady = true;
		
		$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-EventPart').empty();
		if(flag_is_ext_data) {
			var ActEventIconSrc='', activity_duration_str='', 
			activity_duration_hours=0, activity_duration_minutes=0, activity_duration_seconds=0;
			$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-EventPart').append($('<ul>'));
			for(var activity_type_key in local_ext_data) {
				ActEventIconSrc = PathToExecuteDirectory+'images/AppDPAM_act_icon_'+PhysicalActEventsSupport.ForActivityDuration[activity_type_key]+'.png';
				activity_duration = local_ext_data[activity_type_key];
				activity_duration = activity_duration/1000;
				
				activity_duration_seconds = Math.floor(activity_duration % 60);
				if(activity_duration_seconds > 0 && activity_duration < 60) {
					activity_duration = activity_duration + (60 - activity_duration_seconds);
					activity_duration_seconds = 0;
				}
				activity_duration = activity_duration/60;
				activity_duration_minutes = Math.floor(activity_duration % 60);
				activity_duration = activity_duration/60;
				activity_duration_hours = Math.floor(activity_duration % 24);
				
				activity_duration_str = activity_duration_hours < 10 ? '0'+activity_duration_hours : activity_duration_hours;
				activity_duration_str+= ':';
				activity_duration_str+= activity_duration_minutes < 10 ? '0'+activity_duration_minutes : activity_duration_minutes;
				
				$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-EventPart ul').append($('<li>'));
				$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-EventPart ul li:last').append($('<img>').attr({'src': ActEventIconSrc}));
				$('#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-EventPart ul li:last').append($('<p>').text(activity_duration_str));
			}
		}
		
		ElementAddOrRemoveLoading(AppObjectParams.ObjectsHtmlID.EventPart, false);
		CountFinishedRequests = CountFinishedRequests + 1;
		
		if(ChartReadyData.ChartActivityData.FlagIsData) {
			if(!ChartReadyData.ChartActivityData.ObjImages.FlagIsDataReady) {
				var loadedImages=0, numImages=PhysicalActEventsSupport.ForChartActivityData.length, srcImage='';
				for(var act_event_image_key in PhysicalActEventsSupport.ForChartActivityData) {
					srcImage = PathToExecuteDirectory+'images/AppDPAM_act_icon_';
					srcImage+= PhysicalActEventsSupport.ForChartActivityData[act_event_image_key]+'.png'; 
					
					ChartReadyData.ChartActivityData.ObjImages.ImagesSrc[act_event_image_key] = new Image();
					ChartReadyData.ChartActivityData.ObjImages.ImagesSrc[act_event_image_key].onload = function() {
						if(++loadedImages >= numImages) {
							ChartReadyData.ChartActivityData.ObjImages.FlagIsDataReady = true;
							LoadReadyDataChart();
						}
					};
					ChartReadyData.ChartActivityData.ObjImages.ImagesSrc[act_event_image_key].src=srcImage; 
				}
			} else {
				LoadReadyDataChart();
			}
		} else {
			LoadReadyDataChart();
		}
	}
	//******************************************************************************
	function LoadReadyDataChart() {
		if(ActivitySelectedViewID == 1) {
			if(!ChartReadyData.ChartActivityData.FlagIsDataReady || !ChartReadyData.FlagIsDataReady) {
				return true;
			}
		}
		
		$.jqplot.sprintf.thousandsSeparator = ' ';
		var PointLabelFormatString = "%'i";
		var gridPaddingTop = null;
		var XshowGridline = false;
		if(ActivitySelectedViewID == 1 && ChartReadyData.ChartActivityData.FlagIsData) { 
			gridPaddingTop = 65;
			XshowGridline = true;
		}
		
		var chart_element_id='', html_element_id='';
		for(var chart_index in ChartReadyData.ChartData) {
			if(chart_index == 1) PointLabelFormatString = "%#.2f";
			else PointLabelFormatString = "%'i";
			
			chart_element_id = AppObjectParams.ObjectsPartID.ChartID+'_'+((chart_index*1)+1);
			html_element_id = '#AppDPAM-ExtData .AppDPAM-Chart .AppDPAM-VisPart #'+chart_element_id;
			
			$(html_element_id).empty();
			$(html_element_id).jqplot([ChartReadyData.ChartData[chart_index]], { 
				title: ChartReadyData.ChartTitle[chart_index], 
				seriesColors: ChartReadyData.ChartColors, 
				series: [{renderer:$.jqplot.BarRenderer, 
						rendererOptions:{varyBarColor: true}, 
						pointLabels:{show:true, formatString:PointLabelFormatString, ypadding:0}
						}], 
				axes: { 
					xaxis: {renderer: $.jqplot.CategoryAxisRenderer, 
							label: ChartReadyData.ChartxAxisTitle, 
							labelRenderer: $.jqplot.CanvasAxisLabelRenderer, 
							labelOptions: {fontFamily:'Arial', fontSize:'14pt'}, 
							ticks: ChartReadyData.ChartxLabels, 
							tickRenderer: $.jqplot.CanvasAxisTickRenderer, 
							tickOptions: {
								showGridline:XshowGridline, angle:-30, fontFamily:'Arial', fontSize:'11pt', labelPosition: 'middle'
							}
						},
					yaxis: {
							tickOptions: {showGridline:false, showMark:false, showLabel:false, shadow:false}
						}
					},
				grid: {
						drawGridLines: false,
						background: '#F6F6F6',
						borderColor: '#999999',
	        			borderWidth: 1.0,
	        			shadow: true,
						shadowAngle: 45,
						shadowOffset: 1.0,
				        shadowWidth: 2,
				        shadowDepth: 2 
					},
				gridPadding: {top:gridPaddingTop, right:3}
			});	
		}
		
		if(ActivitySelectedViewID == 1 && ChartReadyData.ChartActivityData.FlagIsData) {
			var ObjCanvasHeight = 22;
			for(var chart_index in ChartReadyData.ChartData) {
				var ObjCanvasID = AppObjectParams.ObjectsPartID.ChartCanvasID+'_'+((chart_index*1)+1);
				var chart_element_id = AppObjectParams.ObjectsPartID.ChartID+'_'+((chart_index*1)+1);
				
				$('#'+chart_element_id).append($('<canvas>').attr({'id':ObjCanvasID,'width':630,'height':25}).css({'top':35,'left':13,'position':'absolute'}));
				
				var img_x = 0;
				var canvas = $('#'+ObjCanvasID);
				var context = canvas[0].getContext('2d');
				
				for(var chart_period_index in ChartReadyData.ChartActivityData.DataByIndex) {
					for(var act_type_index in ChartReadyData.ChartActivityData.DataByIndex[chart_period_index]) {
						if(!ChartReadyData.ChartActivityData.DataByIndex[chart_period_index][act_type_index]) {
							img_x = img_x + 6.5 + 25;
						} else {
							img_x = img_x + 6.5;
							context.drawImage(ChartReadyData.ChartActivityData.ObjImages.ImagesSrc[act_type_index], img_x, 0, 25, 25);
							img_x = img_x + 25;
						}
					}
				}
			}
		}
		
		ElementDisplayOrNotAddOrRemoveLoading(AppObjectParams.ObjectsHtmlID.ChartForLoad, AppObjectParams.ObjectsHtmlID.ChartForLoad+' div', false, false);
		
		ChartSelectedViewID = 1;
		ChartNavChangeStyleSelectedItem();
		ChartViewSelectedItem();
		
		ClearChartAndActivityDataInChartReadyData();
		
		CountFinishedRequests = CountFinishedRequests + 1;
		if(CountStartedRequests == CountFinishedRequests) {
			ViewOrHideOverlayElement(false);
		}
	}
	//==============================================================================
	//HELP FUNCTIONS
	//==============================================================================
	function ReturnDateShiftHours(current_date, num_hours) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		var date_for_return = new Date(current_date.getFullYear(), current_date.getMonth(), current_date.getDate(), (current_date.getHours()+num_hours));
		return date_for_return;
	}
	//==============================================================================
	function ReturnDateShiftDays(current_date, num_days) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		var date_for_return = new Date(current_date.getFullYear(), current_date.getMonth(), (current_date.getDate()+num_days));
		return date_for_return;
	}
	//==============================================================================
	function ReturnFirstDateOfWeekShiftWeeks(current_date, num_weeks) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		var current_day_of_week = current_date.getDay();
		if(current_day_of_week == 0) current_day_of_week = 7;
		var first_week_day = current_date.getDate()-(current_day_of_week-1)+(7*(num_weeks));
		return new Date(current_date.getFullYear(), current_date.getMonth(), first_week_day);
	}
	//==============================================================================
	function ReturnFirstDateOfMonthShiftMonths(current_date, num_months) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		var date_for_return = new Date(current_date.getFullYear(), (current_date.getMonth()+num_months), 1);
		return date_for_return;
	}
	//******************************************************************************
	function ReturnLastDateOfMonthShiftMonths(current_date, months_num) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		var first_date_next = ReturnFirstDateOfMonthShiftMonths(current_date, months_num);
		return new Date(first_date_next.getFullYear(), first_date_next.getMonth(), (first_date_next.getDate()-1));
	}
	//******************************************************************************
	function ReturnPreviousDateShiftExactMonths(current_date, num_months) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		if(IsEndOfTheMonth(current_date)) {
			var date_for_return = new Date(current_date.getFullYear(), (current_date.getMonth()-num_months+1), 1);
		} else {
			var date_for_return_check = new Date(current_date.getFullYear(), (current_date.getMonth()-num_months), (current_date.getDate()+1));
			if(current_date.getMonth() == date_for_return_check.getMonth()) {
				var last_day = ReturnCountDaysOfMonth(new Date(date_for_return_check.getFullYear(), (date_for_return_check.getMonth()-num_months), 1));
				var date_for_return = new Date(date_for_return_check.getFullYear(), (date_for_return_check.getMonth()-num_months), last_day);
			} else {
				var date_for_return = date_for_return_check;
			}
		}
		return date_for_return;
	}
	//******************************************************************************
	function ReturnDateShiftMonths(current_date, num_months) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		var date_for_return = new Date(current_date.getFullYear(), (current_date.getMonth()+num_months), current_date.getDate());
		return date_for_return;
	}
	//******************************************************************************
	function ReturnCountDaysOfMonth(current_date) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		var first_date_next = ReturnFirstDateOfMonthShiftMonths(current_date, 1);
		var last_date_current = new Date(first_date_next.getFullYear(), first_date_next.getMonth(), (first_date_next.getDate()-1));
		return last_date_current.getDate();
	}
	//******************************************************************************
	function IsEndOfTheMonth(check_date) {
		if(!(check_date instanceof Date && !isNaN(check_date.valueOf()))) { 
			check_date = new Date(check_date);
		}
		var next_day = new Date(check_date.getFullYear(), check_date.getMonth(), (check_date.getDate()+1));
		
		if((check_date.getMonth() < next_day.getMonth()) || (check_date.getFullYear() < next_day.getFullYear()))
			return true;
		else
			return false;
	}
	//==============================================================================
	function ReturnDateStrFormatForGoalSteps(date_for_format) {
		if(!(date_for_format instanceof Date && !isNaN(date_for_format.valueOf()))) { 
			date_for_format = new Date(date_for_format);
		}
		var arr_date = [date_for_format.getFullYear(), date_for_format.getMonth(), date_for_format.getDate()];
		var string_format = 'YYYY-MM-DD';
		var date_for_return = moment(arr_date).format(string_format);
		return date_for_return;
	}
	//******************************************************************************
	function ReturnDateStrFormatForServiceBrick(date_for_format) {
		if(!(date_for_format instanceof Date && !isNaN(date_for_format.valueOf()))) { 
			date_for_format = new Date(date_for_format);
		}
		var arr_date = [date_for_format.getFullYear(), date_for_format.getMonth(), date_for_format.getDate()];
		var string_format = 'YYYY-MM-DDTHH:mm:ss.SSSZ';
		return moment.tz(arr_date, UserTimezoneOlson).format(string_format);
	}
	//==============================================================================
	function ReturnDateConvertFromSB(date_for_convert) {
		var date_arr = date_for_convert.split("-");
		return new Date((date_arr[0]*1), ((date_arr[1]*1)-1), (date_arr[2]*1));
	}
	//******************************************************************************
	function ReturnDateConvertFromServiceBrickWithT(date_for_convert) {
		var timestamp_arr = date_for_convert.substring(0, 19).split("T");
		var date_arr = timestamp_arr[0].split("-");
		var time_arr = timestamp_arr[1].split(":");
		return new Date((date_arr[0]*1), ((date_arr[1]*1)-1), (date_arr[2]*1), (time_arr[0]*1), (time_arr[1]*1), (time_arr[2]*1));
	}
	//==============================================================================
	function ReturnDateStrFormatPrint(type_format, date_start, date_end) {
		if(!(date_start instanceof Date && !isNaN(date_start.valueOf()))) { 
			date_start = new Date(date_start);
		}
		if(date_end && (!(date_end instanceof Date && !isNaN(date_end.valueOf())))) { 
			date_end = new Date(date_end);
		}
		
		var date_str="";
		switch (type_format) {
			case 1: /*slider day*/
			date_str = AppObjectTextsData.ShortDaysArr[date_start.getDay()];
			date_str += ' ';
			date_str += date_start.getDate() < 10 ? '0'+date_start.getDate() : date_start.getDate();
			date_str += ' '+AppObjectTextsData.ShortMonthsArr[date_start.getMonth()];
			date_str += ' '+date_start.getFullYear();
			break;
			
			case 2: /*slider week*/
			var start_year=date_start.getFullYear(), end_year=date_end.getFullYear();
			var start_month=date_start.getMonth(), end_month=date_end.getMonth();
			var start_day=date_start.getDate(), end_day=date_end.getDate();
			var start_month_print = (start_month+1) < 10 ? '0'+(start_month+1) : (start_month+1);
			var end_month_print = (end_month+1) < 10 ? '0'+(end_month+1) : (end_month+1);
			start_day = start_day < 10 ? '0'+start_day : start_day;
			end_day = end_day < 10 ? '0'+end_day : end_day;
			if(start_year != end_year) {
				date_str = start_day+'.'+start_month_print+'.'+start_year.toString().substr(2,2);
				date_str += ' - '+end_day+'.'+end_month_print+'.'+end_year.toString().substr(2,2);
			} else if(start_month != end_month) {
				date_str = start_day+'.'+start_month_print;
				date_str += ' - '+end_day+'.'+end_month_print+'.'+end_year;
			} else {
				date_str = start_day+' - '+end_day;
				date_str += ' '+AppObjectTextsData.ShortMonthsArr[start_month]+' '+start_year;
			}
			break;
			
			case 3: /*slider month, chart xAxis month*/
			date_str = AppObjectTextsData.LongMonthsArr[date_start.getMonth()]+' '+date_start.getFullYear();
			break;
			
			case 4: /*chart xAxis day*/
			date_str = AppObjectTextsData.LongDaysArr[date_start.getDay()];
			date_str += ' ';
			date_str += date_start.getDate() < 10 ? '0'+date_start.getDate() : date_start.getDate();
			date_str += ' '+AppObjectTextsData.LongMonthsArr[date_start.getMonth()];
			date_str += ' '+date_start.getFullYear();
			break;
			
			case 5: /*chart xAxis week*/
			var start_year=date_start.getFullYear(), end_year=date_end.getFullYear();
			var start_month=date_start.getMonth(), end_month=date_end.getMonth();
			var start_day=date_start.getDate(), end_day=date_end.getDate();
			start_day = start_day < 10 ? '0'+start_day : start_day;
			end_day = end_day < 10 ? '0'+end_day : end_day;
			if(start_year != end_year) {
				date_str = start_day+' '+AppObjectTextsData.LongMonthsArr[start_month]+' '+start_year;
				date_str += ' - ';
				date_str += end_day+' '+AppObjectTextsData.LongMonthsArr[end_month]+' '+end_year;
			} else if(start_month != end_month) {
				date_str = start_day+' '+AppObjectTextsData.LongMonthsArr[start_month];
				date_str += ' - ';
				date_str += end_day+' '+AppObjectTextsData.LongMonthsArr[end_month]+' '+end_year;
			} else {
				date_str = start_day+' - '+end_day;
				date_str += ' '+AppObjectTextsData.LongMonthsArr[start_month]+' '+start_year;
			}
			break;
			
			default:
				date_str="";
		}
		return date_str;
	}
	//==============================================================================
	function CalculateMaxStepsBasedGoal(goal) {
		return Math.round(goal/0.81);
	}
	//******************************************************************************
	function ReturnColorIndex(p1) {
		var ColorIndex=0;
		if(p1 <= 20) ColorIndex = 0;
		else if(p1 > 20 && p1 <= 40) ColorIndex = 1;
		else if(p1 > 40 && p1 <= 60) ColorIndex = 2;
		else if(p1 > 60 && p1 <= 80) ColorIndex = 3;
		else ColorIndex = 4;
		return ColorIndex;
	}
	//******************************************************************************
	function ReturnNumberToStringWithThousandSeparator(num) {
		var num_str = num.toString();
		var R = new RegExp('(-?[0-9]+)([0-9]{3})');
		while(R.test(num_str)) {
            num_str = num_str.replace(R, '$1 $2');
        }
		return num_str;
	}
	//******************************************************************************
	function CapitalizeFirstLetter(str, flag_all_lang) {
		if(flag_all_lang) {
			return str.charAt(0).toUpperCase() + str.slice(1);
		} else {
			if(UserLangApp == "nl") {
				return str;
			} else {
				return str.charAt(0).toUpperCase() + str.slice(1);
			}
		}
	}
	//==============================================================================
	function ViewOrHideOverlayElement(flag_view) {
		if(flag_view) {
			$('#AppDPAM_AppDataContainer .AppDPAM-overlay').css({'display':'block'});
		} else {
			$('#AppDPAM_AppDataContainer .AppDPAM-overlay').css({'display':'none'});
		}
	}
	//******************************************************************************
	function ElementClearContent(obj_element_id) {
		$(obj_element_id).empty();
	}
	//******************************************************************************
	function ElementDisplayOrNotAddOrRemoveLoading(obj_display_id, obj_loading_id, flag_display, flag_add_loading) {
		ElementDisplayOrNot(obj_display_id, flag_display);
		ElementAddOrRemoveLoading(obj_loading_id, flag_add_loading);
	}
	//******************************************************************************
	function ElementDisplayOrNot(obj_element_id, flag_display) {
		$(obj_element_id).css({'display':'none'});
		if(flag_display) $(obj_element_id).css({'display':'block'});
	}
	//******************************************************************************
	function ElementAddOrRemoveLoading(obj_element_id, flag_add) {
		$(obj_element_id).removeClass('AppDPAM-loading');
		if(flag_add) $(obj_element_id).addClass('AppDPAM-loading');
	}
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
	function LogUserInteraction(iteraction_param, iteraction_val) {
		var param1 = '', param2 = '', param3 = '';
		
		param1 = UserIteractionData.applicationName;
		param2 = UserIteractionData.iteractionData[iteraction_param][iteraction_val].buttonId;
		param3 = UserIteractionData.iteractionData[iteraction_param][iteraction_val].comment;
		if(param1.length > 0 && param2.length > 0 && param3.length > 0) {
			if(typeof userInteractionLogger == 'object' && userInteractionLogger != null && userInteractionLogger.hasOwnProperty('sendInteractionWithComment')) {
				userInteractionLogger.sendInteractionWithComment(param1, param2, param3);
			}
		}
	}
	//==============================================================================
}//end of object

});         
