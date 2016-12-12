jQuery.support.cors = true;
//==============================================================================
//NEW VERSION - September 2016
//==============================================================================
function AppObjectDomotics() {
	var LanguageSupport = [['en','en-US'], ['da','da-Dk'], ['de','de-AT'], ['it'], ['nl'], ['sr'], ['mk'], 
	['bg','bg-BG'], ['hr'], ['ro','ro-RO']];
	
	var ToDay={},HistoryStartDates={},HistoryEndDates={}, 
	CountStartedRequests=0,CountFinishedRequests=0;
	
	var UserName="",UserID=0,UserLang="",UserTimezoneOlson="",UserLangApp="en",UserIteractionData={};
	var PathToExecuteDirectory="",PathToSB_Domotics="",PathToACP="";
	
	var AppDomoticsObjectParams = {
		'ObjectsHtmlID':{'AppDataContainer':'#AppDomotics-DataContainer', 'ActPartID':'#AppDomotics-ActPart', 
			'ActuatorForLoad':'#AppDomotics-ActPart .AppDomotics-actuator .AppDomotics-ForLoad'},
		'ObjectsPartID':{'NavHpID':'AppDomotics_History', 'NavLocationID':'AppDomotics_ViewRoom', 'LocationID':'AppDomotics_Room', 'CanvasID':'AppDomotics_Canvas'}, 
		'HistoryDataSizes':{'VisPartWidth':910,'CanvasHeight':60,'OneItemWidth':{1:130,2:130,3:91}}, 
		'ActuatorDataSizes':{'VisPartWidth':190, 'NumberItems':10, 'OneItemX':19, 'CanvasHeight':35}
	};
	
	var AppLabelsTxtArr=['---'],NavigationTxtArr=[],HistoryTxtArr=[],ShortDaysArr=[],
	ParamLabelTxtArr=[],ParamDimensionTxtArr=[],LocationTxtArr=[],ColorLegendObj={'Label':'', 'Colors':[]};
	var ColorObj = {'BasicColor': ['#e6e6e6', '#ffd600', '#64dd17', '#ffab00'], 
	'FontColor':['#3D475B', '#3D475B', '#ffffff', '#ffffff'], 
	'OrderColorKeys':[1,2,3], 'LegendColorIDs':[1,2,3]};
	
	var WhatView=2; //1 - HistoryDataView, 2 - StatusDataView
	var HistoryDataView = 1; //1 - day, 2 - week, 3 - month
	var HistoryDataLocationView = 1; //0 - kitchen, 1 - livingroom, 2 - bedroom, 3 - bathroom
	
	var ExternalHistoryData=[], ReadyExternalHistoryData={}, 
	ExternalStatusData=[], ReadyExternalStatusData=[];
	
	var LightControlActuatorsSupport={'ActuatorEnvLocation':'livingroom', 'ActuatorGUILabel':'Philips HUE', 
	'ActuatorGUIParam':'illuminance'};
	
	var ActuatorConfigData={
	'ActuatorParams':{'IsActuatorExist':false, 'ActuatorUUID':'', 'StatusVal':0, 'IntensityVal':0, 'IntensityChange':0}, 
	'MoveParams':{'DragStart':0, 'PrevIndex':0, 'CurrentIndex':0, 'Positions':[], 'Values':[], 'ValuesChange':[]}
	};
	
	var EnvLocationsSupportArr=['kitchen', 'livingroom', 'bedroom', 'bathroom'];
	var EnvLocationsOrderArr=[1, 0, 2, 3];
	var ExternalParamNames=['temperature', 'humidity']; /*, 'illuminance'*/
	var ExternalParamNormalRanges={ 
		'temperature':{ 
			'kitchen':{'min':20, 'max':26}, 
			'livingroom':{'min':20, 'max':26},
			'bedroom':{'min':20, 'max':26}, 
			'bathroom':{'min':20, 'max':26}
		}, 
		'humidity':{ 
			'kitchen':{'min':25, 'max':55}, 
			'livingroom':{'min':25, 'max':55}, 
			'bedroom':{'min':25, 'max':55}, 
			'bathroom':{'min':25, 'max':55}
		}, 
		'illuminance':{ 
			'kitchen':{'min':300, 'max':500}, 
			'livingroom':{'min':200, 'max':500}, 
			'bedroom':{'min':300, 'max':500}, 
			'bathroom':{'min':200, 'max':500}
		}
	};
	
	var myScrollHorizontal=null, myDraggableObject = null;;
	
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
			url: PathToExecuteDirectory+"xml/AppDomotics_lang_"+UserLangApp+".xml",
			type: "GET",
			dataType: "xml",
			async: true,
			cache: true,
			error: function() {return false;},
			success: function(xml) {
				if(InitLangAppData(xml)) {
					InitHistoryDatePeriods();
					InitUserIteractionData();
					ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.AppDataContainer, true);
					GetLightActuatorData();
				}
			}
		});
	}
	//==============================================================================
	//INIT FUNCTIONS
	//==============================================================================
	function InitExtUserAndDomainPathParams() {
		PathToSB_Domotics=""; PathToACP="";
		var HasUserData = false, PathFromApi = "", PathFromApiObj = null;
		
		if(typeof ewallApp === 'object' && ewallApp != null) {
			UserName = ewallApp.currentSession.user.username;
			UserLang = ewallApp.preferedLanguage;
			UserTimezoneOlson = ewallApp.currentSession.user.userProfile.vCardSubProfile.timezoneid;
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
			
			PathToSB_Domotics = PathFromApiObj.protocol+'//'+PathFromApiObj.host;
			PathToACP = PathFromApiObj.protocol+'//'+PathFromApiObj.host;
			if(env.length > 0) {
				PathToSB_Domotics += '/applications-'+env;
				PathToACP += '/platform-'+env;
			}
			PathToSB_Domotics += '/service-brick-domotics';
			PathToACP += '/cloud-gateway';
			return true;
		} else {
			return false;
		}
	}
	//******************************************************************************
	function InitUserLang() {
		for(var i in LanguageSupport) {
			if($.inArray(UserLang, LanguageSupport[i]) >= 0) {
				UserLangApp = UserLang;
				break;
			}
		}
	}
	//******************************************************************************
	function InitLangAppData(xml) {
		for(var LCID_Key in ColorObj.LegendColorIDs) {
			ColorLegendObj.Colors[LCID_Key] = '';
		}
		
		$(xml).find("ItemGroup").each(function() {
			if($(this).attr("name") == "NavigationTxt") {
				$(this).children('item').each(function (index) {
					NavigationTxtArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "HistoryTxt") {
				$(this).children('item').each(function (index) {
					HistoryTxtArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "ShortDays") {
				$(this).children('item').each(function (index) {
					ShortDaysArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "ParamLabelTxt") {
				$(this).children('item').each(function (index) {
					ParamLabelTxtArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "ParamDimensionTxt") {
				$(this).children('item').each(function (index) {
					ParamDimensionTxtArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "LocationTxt") {
				$(this).children('item').each(function (index) {
					LocationTxtArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "AppLabels") {
				$(this).children('item').each(function (index) {
					AppLabelsTxtArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "ColorLegendLabels") {
				ColorLegendObj.Label=$(this).children('Label').text();
				$(this).children('Color').each(function () {
					var color_txt_key = $.inArray(parseInt($(this).attr("id")), ColorObj.LegendColorIDs);
					if(color_txt_key > -1) {
						ColorLegendObj.Colors[color_txt_key] = $(this).text();
					}
				});
			}
		});
		return true;
	}
	//******************************************************************************
	function InitHistoryDatePeriods() {
		ToDay = new Date(); ToDay.setHours(0); ToDay.setMinutes(0); ToDay.setSeconds(0); ToDay.setMilliseconds(0);
		
		var end_date = new Date(ToDay.getFullYear(), ToDay.getMonth(), ToDay.getDate());
		
		var start_date_1 = end_date;
		var start_date_2 = ReturnDateShiftDays(end_date, -(7-1));
		var start_date_3 = ReturnPreviousDateShiftMonths(end_date, 1);
		
		var end_date_1 = ReturnDateShiftDays(end_date, 1);
		var end_date_2 = ReturnDateShiftDays(end_date, 1);
		var end_date_3 = ReturnDateShiftDays(end_date, 1);
		
		HistoryStartDates={};
		HistoryEndDates={};
		
		HistoryStartDates = {1:{"start_date":start_date_1, "start_date_ms":Date.parse(start_date_1)},
		2:{"start_date":start_date_2, "start_date_ms":Date.parse(start_date_2)},
		3:{"start_date":start_date_3, "start_date_ms":Date.parse(start_date_3)}};
		
		HistoryEndDates = {1:{"end_date":end_date, "end_date_ms":Date.parse(end_date), "end_date_add":end_date_1, "end_date_add_ms":Date.parse(end_date_1)},
		2:{"end_date":end_date, "end_date_ms":Date.parse(end_date), "end_date_add":end_date_2, "end_date_add_ms":Date.parse(end_date_2)},
		3:{"end_date":end_date, "end_date_ms":Date.parse(end_date), "end_date_add":end_date_3, "end_date_add_ms":Date.parse(end_date_3)}};
	}
	//******************************************************************************
	function InitLightControlActuatorData(UUID) {
		ActuatorConfigData.ActuatorParams.IsActuatorExist = true;
		ActuatorConfigData.ActuatorParams.ActuatorUUID = UUID;
		ActuatorConfigData.ActuatorParams.StatusVal = 0;
		ActuatorConfigData.ActuatorParams.IntensityVal = 50;
		ActuatorConfigData.ActuatorParams.IntensityChange = 0;
	}
	//******************************************************************************
	function InitLightControlActuatorPositions() {
		var slider_items = AppDomoticsObjectParams.ActuatorDataSizes.NumberItems, 
		slider_one_item_x = AppDomoticsObjectParams.ActuatorDataSizes.OneItemX, 
		slider_width = AppDomoticsObjectParams.ActuatorDataSizes.VisPartWidth;
		for(var sl_pos_index = 0; sl_pos_index <= slider_items; sl_pos_index++) {
			var intensity_val = sl_pos_index*10;
			var intensity_change = -100 + (sl_pos_index*20);
			if(intensity_val == ActuatorConfigData.ActuatorParams.IntensityVal) {
				ActuatorConfigData.MoveParams.CurrentIndex = sl_pos_index;
			}
			ActuatorConfigData.MoveParams.Positions[sl_pos_index] = sl_pos_index*slider_one_item_x;
			ActuatorConfigData.MoveParams.Values[sl_pos_index] = intensity_val;
			ActuatorConfigData.MoveParams.ValuesChange[sl_pos_index] = intensity_change;
		}
	}
	//******************************************************************************
	function InitUserIteractionData() {
		UserIteractionData = {'applicationName':'domoticsApplication', 
			'iteractionData':{
				'statusPart':{
					'0':{'buttonId':'statusOverview', 'comment':'User selects Status overview'}
				}, 
				'historyPart':{
					'1':{'buttonId':'todayOverview', 'comment':'User selects Today overview'}, 
					'2':{'buttonId':'lastWeekOverview', 'comment':'User selects Last week overview'}, 
					'3':{'buttonId':'lastMonthOverview', 'comment':'User selects Last Month overview'}
				}, 
				'actionPartHistory':{
					'1':{ 
						'0':{'buttonId':'todayOverviewKitchen', 'comment':'In today overview user seletcs Kitchen'}, 
						'1':{'buttonId':'todayOverviewLivingroom', 'comment':'In today overview user seletcs Livingroom'}, 
						'2':{'buttonId':'todayOverviewBedroom', 'comment':'In today overview user seletcs Bedroom'}, 
						'3':{'buttonId':'todayOverviewBathroom', 'comment':'In today overview user seletcs Bathroom'}, 
					}, 
					'2':{ 
						'0':{'buttonId':'weekOverviewKitchen', 'comment':'In last week overview user seletcs Kitchen'}, 
						'1':{'buttonId':'weekOverviewLivingroom', 'comment':'In last week overview user seletcs Livingroom'}, 
						'2':{'buttonId':'weekOverviewBedroom', 'comment':'In last week overview user seletcs Bedroom'}, 
						'3':{'buttonId':'weekOverviewBathroom', 'comment':'In last week overview user seletcs Bathroom'}, 
					}, 
					'3':{ 
						'0':{'buttonId':'monthOverviewKitchen', 'comment':'In last month overview user seletcs Kitchen'}, 
						'1':{'buttonId':'monthOverviewLivingroom', 'comment':'In last month overview user seletcs Livingroom'}, 
						'2':{'buttonId':'monthOverviewBedroom', 'comment':'In last month overview user seletcs Bedroom'}, 
						'3':{'buttonId':'monthOverviewBathroom', 'comment':'In last month overview user seletcs Bathroom'}, 
					}
				}, 
				'lightActuator':{
					'statusOn':{'buttonId':'hueOnButton', 'comment':'User switches Philips Hue On'}, 
					'statusOff':{'buttonId':'hueOffButton', 'comment':'User switches Philips Hue Off'}, 
					'intensity':{'buttonId':'hueIntensitySlider', 'comment':'User changes Philips Hue intensity'}
				}
			}
		};
	}
	//==============================================================================
	//CREATE HTML ELEMENTS
	//==============================================================================
	function CreateAppBaseElements() {
		$('#AppDomotics-DataContainer').empty();
		$('#AppDomotics-DataContainer').append($('<div>').attr('id', 'AppDomotics-PartAll'));
		$('#AppDomotics-DataContainer').append($('<div>').addClass('AppDomotics-overlay'));
		ViewOrHideOverlayElement(true);
		
		$('#AppDomotics-PartAll').append($('<div>').attr('id', 'AppDomotics-NavPart'));
		$('#AppDomotics-PartAll').append($('<div>').attr('id', 'AppDomotics-ActPart'));
		
		var NavSPImgSrc=PathToExecuteDirectory+'images/AppDomotics_status.png';
		$('#AppDomotics-NavPart').append($('<div>').addClass('AppDomotics-sp'));
		$('#AppDomotics-NavPart .AppDomotics-sp').append($('<ul>'));
		$('#AppDomotics-NavPart .AppDomotics-sp ul').append($('<li>'));
		$('#AppDomotics-NavPart .AppDomotics-sp ul li:last').append($('<img>').attr({'src':NavSPImgSrc}));
		$('#AppDomotics-NavPart .AppDomotics-sp ul li:last').append($('<p>').append(NavigationTxtArr[0]));	
		
		$('#AppDomotics-NavPart').append($('<div>').addClass('AppDomotics-hp'));
		$('#AppDomotics-NavPart .AppDomotics-hp').append($('<ul>'));
		var NavHPElementID='',NavHPImgSrc='';
		for(var j in HistoryTxtArr) {
			NavHPElementID=AppDomoticsObjectParams.ObjectsPartID.NavHpID+'_'+((j*1)+1);
			NavHPImgSrc=PathToExecuteDirectory+'images/AppDomotics_history_'+j+'.png';
			
			$('#AppDomotics-NavPart .AppDomotics-hp ul').append($('<li>').attr('id', NavHPElementID));
			$('#AppDomotics-NavPart .AppDomotics-hp ul li:last').append($('<img>').attr({'src':NavHPImgSrc}));
			$('#AppDomotics-NavPart .AppDomotics-hp ul li:last').append($('<p>').append(HistoryTxtArr[j]));	
		}
		
		$('#AppDomotics-NavPart .AppDomotics-sp ul li').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				StatusPartChangeStyleSelectedItem();
				LogUserInteraction('statusPart', 0, -1);
				WhatView=2;
				
				CountStartedRequests=1;	CountFinishedRequests=0;
				ViewOrHideOverlayElement(true);
				GetExternalStatusData();
			}
		});
		
		$('#AppDomotics-NavPart .AppDomotics-hp ul li').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				var history_part_res = $(this).attr('id').split("_");
				if(history_part_res.length == 3) {HistoryDataView = parseInt(history_part_res[2]);}
				
				HistoryPartChangeStyleSelectedItem();
				LogUserInteraction('historyPart', HistoryDataView, -1);
				WhatView=1;
				HistoryDataLocationView = 1;
				
				CountStartedRequests=1;	CountFinishedRequests=0;
				ViewOrHideOverlayElement(true);
				GetExternalHistoryData();
			}
		});
		
		if(WhatView == 1) {
			HistoryPartChangeStyleSelectedItem();
			CountStartedRequests=1; CountFinishedRequests=0;
			GetExternalHistoryData();
		}
		if(WhatView == 2) {
			StatusPartChangeStyleSelectedItem();
			CountStartedRequests=1; CountFinishedRequests=0;
			GetExternalStatusData();
		}
	}
	//******************************************************************************
	function CreateColorLegend() {
		$('#AppDomotics-ActPart .AppDomotics-cp').append($('<label>').text(ColorLegendObj.Label));
		$('#AppDomotics-ActPart .AppDomotics-cp').append($('<ul>'));
		var ColorKey=0, ColorKeyNew=0, ColorLegendBG='', ColorLegendTxt='';
		for(var color_order_key in ColorObj.OrderColorKeys) {
			ColorKey          = ColorObj.OrderColorKeys[color_order_key];
			ColorLegendBG     = ColorObj.BasicColor[ColorKey];
			ColorKeyNew       = (ColorKey*1)-1;
			ColorLegendTxt    = '';
			if(ColorKeyNew in ColorLegendObj.Colors) {
				ColorLegendTxt = ColorLegendObj.Colors[ColorKeyNew];
			}
			$('#AppDomotics-ActPart .AppDomotics-cp ul').append($('<li>'));
			$('#AppDomotics-ActPart .AppDomotics-cp ul li:last').append($('<div>'));
			$('#AppDomotics-ActPart .AppDomotics-cp ul li:last div').append($('<span>').css({'background-color':ColorLegendBG}));
			$('#AppDomotics-ActPart .AppDomotics-cp ul li:last div').append($('<p>').text(ColorLegendTxt));
		}
	}
	//******************************************************************************
	function CreateActuatorElements(IsActuatorHtmlElement) {
		if(myDraggableObject != null) {
			myDraggableObject = null;
		}
		
		if(!IsActuatorHtmlElement) {
			return false;
		}
		
		var ObjCanvasIDPart=AppDomoticsObjectParams.ObjectsPartID.CanvasID,
		ObjCanvasID=ObjCanvasIDPart+'_'+LightControlActuatorsSupport.ActuatorEnvLocation, 
		CanvasHeight=AppDomoticsObjectParams.ActuatorDataSizes.CanvasHeight;
		
		var actuator_param = LightControlActuatorsSupport.ActuatorGUIParam;
		var actuator_label = LightControlActuatorsSupport.ActuatorGUILabel;
		var LightImgSrc = PathToExecuteDirectory+'images/AppDomotics_icon_'+actuator_param+'_big.png';
		
		$('#AppDomotics-ActPart .AppDomotics-actuator').append($('<div>').addClass('AppDomotics-actuator-cs'));
		$('#AppDomotics-ActPart .AppDomotics-actuator').append($('<div>').addClass('AppDomotics-actuator-ci'));
		
		$('#AppDomotics-ActPart .AppDomotics-actuator').append($('<div>').addClass('AppDomotics-ForLoad').append($('<div>')));
		
		$('#AppDomotics-ActPart .AppDomotics-actuator-cs').append($('<img>').attr({'src':LightImgSrc}));
		$('#AppDomotics-ActPart .AppDomotics-actuator-cs').append($('<p>').text(actuator_label));
		$('#AppDomotics-ActPart .AppDomotics-actuator-cs').append($('<div>').addClass('AppDomotics-actuator-OnOff'));
		
		$('#AppDomotics-ActPart .AppDomotics-actuator-OnOff').append($('<div>').addClass('AppDomotics-actuator-On').css({'display':'none'}));
		$('#AppDomotics-ActPart .AppDomotics-actuator-OnOff').append($('<div>').addClass('AppDomotics-actuator-Off').css({'display':'none'}));
		
		$('#AppDomotics-ActPart .AppDomotics-actuator-ci').append($('<canvas>').attr({'id':ObjCanvasID, 'width':240, 'height':CanvasHeight}).css({'top':35, 'left':0, 'position':'absolute'}));
		$('#AppDomotics-ActPart .AppDomotics-actuator-ci').append($('<div>').addClass('AppDomotics-actuator-bg-base'));
		$('#AppDomotics-ActPart .AppDomotics-actuator-ci').append($('<div>').addClass('AppDomotics-actuator-bg-fill'));
		$('#AppDomotics-ActPart .AppDomotics-actuator-ci').append($('<div>').addClass('AppDomotics-actuator-vp'));
		$('#AppDomotics-ActPart .AppDomotics-actuator-vp').append($('<div>').addClass('AppDomotics-actuator-container'));
		$('#AppDomotics-ActPart .AppDomotics-actuator-container').append($('<div>').addClass('AppDomotics-actuator-slider').css({'top':0, 'left':0, 'position':'relative'}));
		//$('#AppDomotics-ActPart .AppDomotics-actuator-slider').append($('<div>').addClass('AppDomotics-actuator-handle-base'));
		$('#AppDomotics-ActPart .AppDomotics-actuator-slider').append($('<div>').addClass('AppDomotics-actuator-handle'));
		
		CreateCanvasActuator();
		
		if(!ActuatorConfigData.ActuatorParams.IsActuatorExist) {
			ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, true);
		}
		
		myDraggableObject = new Draggabilly('.AppDomotics-actuator-slider', { 
			axis: 'x', 
			containment: '.AppDomotics-actuator-container', 
			handle: '.AppDomotics-actuator-handle'
		});
		myDraggableObject.on('dragStart', function(event, pointer) {
			var draggie_position_x = this.position.x;
			ActuatorConfigData.MoveParams.DragStart = draggie_position_x;
			ActuatorConfigData.MoveParams.PrevIndex = Math.round(draggie_position_x/AppDomoticsObjectParams.ActuatorDataSizes.OneItemX);
		});
		myDraggableObject.on('dragEnd', function(event, pointer) {
			var draggie_position_x = this.position.x;
			if(draggie_position_x <= ActuatorConfigData.MoveParams.DragStart) {
				ActuatorConfigData.MoveParams.CurrentIndex = Math.floor(draggie_position_x/AppDomoticsObjectParams.ActuatorDataSizes.OneItemX);
			} else {
				ActuatorConfigData.MoveParams.CurrentIndex = Math.ceil(draggie_position_x/AppDomoticsObjectParams.ActuatorDataSizes.OneItemX);
			}
			ActuatorConfigData.ActuatorParams.IntensityVal = ActuatorConfigData.MoveParams.Values[ActuatorConfigData.MoveParams.CurrentIndex];
			ActuatorConfigData.ActuatorParams.IntensityChange = ActuatorConfigData.MoveParams.ValuesChange[ActuatorConfigData.MoveParams.CurrentIndex];
			ActuatorSliderMove();
			
			CountStartedRequests=1; CountFinishedRequests=0;
			ViewOrHideOverlayElement(true);
			ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, true);
			ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', true);
			
			LogUserInteraction('lightActuator', 'intensity', -1);
			ChangeLightActuatorIntensity();
			//SetLightActuatorIntensity();
			
		});
		
		$('#AppDomotics-ActPart .AppDomotics-actuator-On').click(function() {
			if(CountStartedRequests == CountFinishedRequests) {
				ActuatorConfigData.ActuatorParams.StatusVal = 1;
				ActuatorOnOffButtonChange();
				
				CountStartedRequests=1; CountFinishedRequests=0;
				ViewOrHideOverlayElement(true);
				ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, true);
				ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', true);
				
				LogUserInteraction('lightActuator', 'statusOn', -1);
				SetLightActuatorStatus();
			}
		});
		$('#AppDomotics-ActPart .AppDomotics-actuator-Off').click(function() {
			if(CountStartedRequests == CountFinishedRequests) {
				ActuatorConfigData.ActuatorParams.StatusVal = 0;
				ActuatorOnOffButtonChange();
				
				CountStartedRequests=1; CountFinishedRequests=0;
				ViewOrHideOverlayElement(true);
				ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, true);
				ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', true);
				
				LogUserInteraction('lightActuator', 'statusOff', -1);
				SetLightActuatorStatus();
			}
		});
		
		ActuatorOnOffButtonChange();
		ActuatorSliderMove();
		
		if(ActuatorConfigData.ActuatorParams.IsActuatorExist) {
			ViewOrHideOverlayElement(false);
			ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, false);
			ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', false);
			/*CountStartedRequests=2; CountFinishedRequests=0;
			ViewOrHideOverlayElement(true);
			ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, true);
			ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', true);
			
			SetLightActuatorStatus(true);*/
		}
	}
	//==============================================================================
	//CHANGE STYLE AND MOVE FUNCTIONS
	//==============================================================================
	function HistoryPartChangeStyleSelectedItem() {
		$('#AppDomotics-NavPart ul li').removeClass();
		$('#AppDomotics-NavPart .AppDomotics-hp ul #'+AppDomoticsObjectParams.ObjectsPartID.NavHpID+'_'+HistoryDataView).addClass("AppDomotics-el-sel");
	}
	//******************************************************************************
	function StatusPartChangeStyleSelectedItem() {
		$('#AppDomotics-NavPart ul li').removeClass();
		$('#AppDomotics-NavPart .AppDomotics-sp ul li').addClass("AppDomotics-el-sel");
	}
	//******************************************************************************
	function ActionPartHistoryChangeStyleSelectedItem() {
		$('#AppDomotics-ActPart .AppDomotics-hp .AppDomotics-hp-bp ul li').removeClass();
		$('#AppDomotics-ActPart .AppDomotics-hp .AppDomotics-hp-bp ul #'+AppDomoticsObjectParams.ObjectsPartID.NavLocationID+'_'+HistoryDataLocationView).addClass("AppDomotics-hp-bp-sel");
	}
	//******************************************************************************
	function ActuatorOnOffButtonChange() {
		$('#AppDomotics-ActPart .AppDomotics-actuator-On').css({'display':'none'});
		$('#AppDomotics-ActPart .AppDomotics-actuator-Off').css({'display':'none'});
		if(ActuatorConfigData.ActuatorParams.StatusVal == 1) {
			$('#AppDomotics-ActPart .AppDomotics-actuator-Off').css({'display':'block'});
			if(myDraggableObject != null) {
				myDraggableObject.enable();
			}
		} else {
			$('#AppDomotics-ActPart .AppDomotics-actuator-On').css({'display':'block'});
			if(myDraggableObject != null) {
				myDraggableObject.disable();
			}
		}
	}
	//******************************************************************************
	function ActuatorSliderMove() {
		var actuator_slider_left = ActuatorConfigData.MoveParams.Positions[ActuatorConfigData.MoveParams.CurrentIndex];
		$('#AppDomotics-ActPart .AppDomotics-actuator-slider').css({'left':actuator_slider_left});
	}
	//==============================================================================
	//SHOW OR HIDE FUNCTIONS
	//==============================================================================
	function ShowOrHideHistoryRoomData() {
		for(var temp_env_loc in EnvLocationsSupportArr) {
			if($('#'+AppDomoticsObjectParams.ObjectsPartID.LocationID+'_'+temp_env_loc).css('display') != 'none') {
				$('#'+AppDomoticsObjectParams.ObjectsPartID.LocationID+'_'+temp_env_loc).hide(400, 
					function() {
						$('#'+AppDomoticsObjectParams.ObjectsPartID.LocationID+'_'+HistoryDataLocationView).show(400);
					}
				);
				return true;
			}
		}
	}
	//==============================================================================
	//GET EXTERNAL DATA
	//==============================================================================
	function GetExternalHistoryData() {
		var from_date_format = ReturnDateStrFormatForServiceBrick(HistoryStartDates[HistoryDataView]["start_date"]);
		var to_date_format = ReturnDateStrFormatForServiceBrick(HistoryEndDates[HistoryDataView]["end_date_add"]);
		
		var url_temperature = PathToSB_Domotics+"/v1/"+UserName+"/roomtemperature";
		var url_humidity = PathToSB_Domotics+"/v1/"+UserName+"/roomhumidity";
		
		var ObjectID=AppDomoticsObjectParams.ObjectsHtmlID.ActPartID;
		ElementClearContent(ObjectID);
		ElementAddOrRemoveLoading(ObjectID, true);
		
		var ReqRoomTemp = ewallApp.ajax({
			url: url_temperature,
			type: "GET",
			data: {from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		var ReqRoomHumidity = ewallApp.ajax({
			url: url_humidity,
			type: "GET",
			data: {from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqRoomTemp, ReqRoomHumidity).done(function (DataRoomTemp, DataRoomHumidity) {
			ExternalHistoryData[0]=DataRoomTemp[0]["temperatureEvents"];
			ExternalHistoryData[1]=DataRoomHumidity[0]["humidityEvents"];
			ElementAddOrRemoveLoading(ObjectID, false);
			PrepareExternalHistoryData();
		});
	}
	//******************************************************************************
	function GetExternalStatusData() {
		var url_domotics_status = PathToSB_Domotics+"/v1/"+UserName+"/domotics";
		
		var ObjectID=AppDomoticsObjectParams.ObjectsHtmlID.ActPartID;
		ElementClearContent(ObjectID);
		ElementAddOrRemoveLoading(ObjectID, true);
		
		var ReqDomoticsStatus = ewallApp.ajax({
			url: url_domotics_status,
			type: "GET",
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqDomoticsStatus).done(function (DataDomoticsStatus) {
			ExternalStatusData=DataDomoticsStatus;
			ElementAddOrRemoveLoading(ObjectID, false);
			PrepareExternalStatusData();
		});
	}
	//******************************************************************************
	function GetLightActuatorData() {
		var url_actuator = PathToACP+"/users/"+UserName+"/actuators";
		
		var env_location_str = LightControlActuatorsSupport.ActuatorEnvLocation;
		var actuator_type_str = 'LIGHT_ACTUATOR';
		
		var ReqActuatorData = ewallApp.ajax({
			url: url_actuator,
			type: "GET",
			data: {type: actuator_type_str, room: env_location_str},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqActuatorData).done(function (ReceiveActuatorData) {
			if($.isArray(ReceiveActuatorData) && ReceiveActuatorData.length > 0) {
				InitLightControlActuatorData(ReceiveActuatorData[0]["uuid"]);
				InitLightControlActuatorPositions();
			}
			ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.AppDataContainer, false);
			CreateAppBaseElements();
		});
		$.when(ReqActuatorData).fail(function () {
			ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.AppDataContainer, false);
			CreateAppBaseElements();
		});
	}
	//==============================================================================
	//CONTROL ACTUATORS
	//==============================================================================
	function SetLightActuatorStatus(FlagSLAI) {
		var FlagCallSetIntensity = false;
		var request_method     = "PUT";
		var actuator_cmd_type  = "SET_STATUS";
		var actuator_cmd_value = ActuatorConfigData.ActuatorParams.StatusVal;
		
		if(arguments.length == 1 && FlagSLAI) { FlagCallSetIntensity = true; }
		
		var url_actuator_set_status = PathToACP+"/users/"+UserName+"/actuators/";
		url_actuator_set_status += ActuatorConfigData.ActuatorParams.ActuatorUUID;
		url_actuator_set_status += "/sendCommand";
		url_actuator_set_status += "?cmd_type="+actuator_cmd_type+"&cmd_value="+actuator_cmd_value;
		
		var SetActuatorStatus = ewallApp.ajax({
			url: url_actuator_set_status,
			type: request_method,
			async: true,
			cache: true
		});
		
		$.when(SetActuatorStatus).done(function () {
			CountFinishedRequests = CountFinishedRequests + 1;
			if(CountStartedRequests == CountFinishedRequests) {
				ViewOrHideOverlayElement(false);
				ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, false);
				ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', false);
			}
			
			if(FlagCallSetIntensity) {
				SetLightActuatorIntensity();
			}
		});
		$.when(SetActuatorStatus).fail(function () {
			if(ActuatorConfigData.ActuatorParams.StatusVal == 1) {
				ActuatorConfigData.ActuatorParams.StatusVal = 0;
			} else {
				ActuatorConfigData.ActuatorParams.StatusVal = 1;
			}
			
			if(FlagCallSetIntensity) {
				CountFinishedRequests = CountFinishedRequests + 1;
			}
			
			CountFinishedRequests = CountFinishedRequests + 1;
			if(CountStartedRequests == CountFinishedRequests) {
				ViewOrHideOverlayElement(false);
				ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, false);
				ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', false);
			}
			ActuatorOnOffButtonChange();
		});
	}
	//******************************************************************************
	function SetLightActuatorIntensity() {
		var request_method     = "PUT";
		var actuator_cmd_type  = "SET_INTENSITY";
		var actuator_cmd_value = ActuatorConfigData.ActuatorParams.IntensityVal;
		
		var url_actuator_set_intensity = PathToACP+"/users/"+UserName+"/actuators/";
		url_actuator_set_intensity += ActuatorConfigData.ActuatorParams.ActuatorUUID;
		url_actuator_set_intensity += "/sendCommand";
		url_actuator_set_intensity += "?cmd_type="+actuator_cmd_type+"&cmd_value="+actuator_cmd_value;
		
		var SetActuatorIntensity = ewallApp.ajax({
			url: url_actuator_set_intensity,
			type: request_method,
			async: true,
			cache: true
		});
		
		$.when(SetActuatorIntensity).done(function () {
			CountFinishedRequests = CountFinishedRequests + 1;
			if(CountStartedRequests == CountFinishedRequests) {
				ViewOrHideOverlayElement(false);
				ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, false);
				ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', false);
			}
		});
		$.when(SetActuatorIntensity).fail(function () {
			ActuatorConfigData.MoveParams.CurrentIndex = ActuatorConfigData.MoveParams.PrevIndex;
			ActuatorConfigData.ActuatorParams.IntensityVal = ActuatorConfigData.MoveParams.Values[ActuatorConfigData.MoveParams.CurrentIndex];
			CountFinishedRequests = CountFinishedRequests + 1;
			if(CountStartedRequests == CountFinishedRequests) {
				ViewOrHideOverlayElement(false);
				ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, false);
				ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', false);
			}
			ActuatorSliderMove();
		});
	}
	//******************************************************************************
	function ChangeLightActuatorIntensity() {
		var request_method     = "PUT";
		var actuator_cmd_type  = "CHANGE_INTENSITY";
		var actuator_cmd_value = ActuatorConfigData.ActuatorParams.IntensityChange;
		
		var url_actuator_change_intensity = PathToACP+"/users/"+UserName+"/actuators/";
		url_actuator_change_intensity += ActuatorConfigData.ActuatorParams.ActuatorUUID;
		url_actuator_change_intensity += "/sendCommand";
		url_actuator_change_intensity += "?cmd_type="+actuator_cmd_type+"&cmd_value="+actuator_cmd_value;
		
		var ChangeActuatorIntensity = ewallApp.ajax({
			url: url_actuator_change_intensity,
			type: request_method,
			async: true,
			cache: true
		});
		
		$.when(ChangeActuatorIntensity).done(function () {
			ActuatorConfigData.MoveParams.CurrentIndex = ActuatorConfigData.MoveParams.PrevIndex;
			ActuatorConfigData.ActuatorParams.IntensityChange = ActuatorConfigData.MoveParams.ValuesChange[ActuatorConfigData.MoveParams.CurrentIndex];
			CountFinishedRequests = CountFinishedRequests + 1;
			if(CountStartedRequests == CountFinishedRequests) {
				ViewOrHideOverlayElement(false);
				ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, false);
				ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', false);
			}
			ActuatorSliderMove();
		});
		$.when(ChangeActuatorIntensity).fail(function () {
			ActuatorConfigData.MoveParams.CurrentIndex = ActuatorConfigData.MoveParams.PrevIndex;
			ActuatorConfigData.ActuatorParams.IntensityChange = ActuatorConfigData.MoveParams.ValuesChange[ActuatorConfigData.MoveParams.CurrentIndex];
			CountFinishedRequests = CountFinishedRequests + 1;
			if(CountStartedRequests == CountFinishedRequests) {
				ViewOrHideOverlayElement(false);
				ElementDisplayOrNot(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad, false);
				ElementAddOrRemoveLoading(AppDomoticsObjectParams.ObjectsHtmlID.ActuatorForLoad+' div', false);
			}
			ActuatorSliderMove();
		});
	}
	//==============================================================================
	//PREPARE EXTERNAL DATA
	//==============================================================================
	function PrepareExternalHistoryData() {
		ReadyExternalHistoryData={'DateMS':[],'ExtData':[], 
		'MinVal':[], 'MaxVal':[], 'MinValAll':[], 'MaxValAll':[], 
		'FlagIsData':[],'IsExtData':false};
		
		for(var pn in ExternalParamNames) {
			ReadyExternalHistoryData.FlagIsData[pn]=[];
			ReadyExternalHistoryData.ExtData[pn]=[];
			ReadyExternalHistoryData.MinVal[pn]=[];
			ReadyExternalHistoryData.MaxVal[pn]=[];
			ReadyExternalHistoryData.MinValAll[pn]=Infinity;
			ReadyExternalHistoryData.MaxValAll[pn]=-Infinity;
			for(var env_loc in EnvLocationsSupportArr) {
				ReadyExternalHistoryData.FlagIsData[pn][env_loc]=0;
				ReadyExternalHistoryData.ExtData[pn][env_loc]=[];
				ReadyExternalHistoryData.MinVal[pn][env_loc]=Infinity;
				ReadyExternalHistoryData.MaxVal[pn][env_loc]=-Infinity;
			}
		}
		
		var current_date_ms = HistoryStartDates[HistoryDataView]['start_date_ms'];
		var ext_date_str="",ext_date={},ext_date_ms=0,ext_value=0,ext_location='',
		current_index=-1,location_index=-1;
		
		if(HistoryDataView == 1) {
			var end_date_add_ms = HistoryEndDates[HistoryDataView]['end_date_add_ms'];
			var time_now = new Date();
			time_now.setHours(time_now.getHours()+1); time_now.setMinutes(0); 
			time_now.setSeconds(0); time_now.setMilliseconds(0);
			var time_now_add_ms = Date.parse(time_now);
			
			if(time_now_add_ms >= current_date_ms && time_now_add_ms < HistoryEndDates[HistoryDataView]['end_date_add_ms']) {
				end_date_add_ms = time_now_add_ms;
			}
			
			while(current_date_ms < end_date_add_ms) {
				ReadyExternalHistoryData.DateMS[ReadyExternalHistoryData.DateMS.length]=current_date_ms;
				for(var pn in ExternalParamNames) {
					for(var env_loc in EnvLocationsSupportArr) {
						ReadyExternalHistoryData.ExtData[pn][env_loc][ReadyExternalHistoryData.ExtData[pn][env_loc].length]=0;
					}
				}
				current_date_ms = Date.parse(ReturnDateShiftHours(current_date_ms, 1));
			}
		}
		else if(HistoryDataView == 2 || HistoryDataView == 3) {
			while(current_date_ms < HistoryEndDates[HistoryDataView]['end_date_add_ms']) {
				ReadyExternalHistoryData.DateMS[ReadyExternalHistoryData.DateMS.length]=current_date_ms;
				for(var pn in ExternalParamNames) {
					for(var env_loc in EnvLocationsSupportArr) {
						ReadyExternalHistoryData.ExtData[pn][env_loc][ReadyExternalHistoryData.ExtData[pn][env_loc].length]=0;
					}
				}
				current_date_ms = Date.parse(ReturnDateShiftDays(current_date_ms, 1));
			}
		}
		
		for(var i in ExternalHistoryData) {
			for(var j in ExternalHistoryData[i]) {
				ext_date_str = ExternalHistoryData[i][j]['from'];
				ext_location = ExternalHistoryData[i][j]['location'].toLowerCase();
				ext_value    = Math.round(ExternalHistoryData[i][j][ExternalParamNames[i]]);
				
				if(HistoryDataView == 1) ext_date = ReturnDateAndHoursConvertFromServiceBrickWithT(ext_date_str);
				else ext_date = ReturnOnlyDateConvertFromServiceBrickWithT(ext_date_str);
				
				ext_date_ms  = Date.parse(ext_date);
				
				current_index  = $.inArray(ext_date_ms, ReadyExternalHistoryData.DateMS);
				location_index = $.inArray(ext_location, EnvLocationsSupportArr);
				
				if(current_index >= 0 && location_index >= 0) {
					if(!ReadyExternalHistoryData.IsExtData) ReadyExternalHistoryData.IsExtData=true;
					if(ReadyExternalHistoryData.FlagIsData[i][location_index] == 0) ReadyExternalHistoryData.FlagIsData[i][location_index] = 1;
					
					ReadyExternalHistoryData.ExtData[i][location_index][current_index]=ext_value;
				}
			}
		}
		
		ExternalHistoryData=[];
		LoadExternalHistoryData();
	}
	//******************************************************************************
	function PrepareExternalStatusData() {
		ReadyExternalStatusData=[];
		for(var env_loc in EnvLocationsSupportArr) {
			ReadyExternalStatusData[env_loc]=[];
			for(var pn in ExternalParamNames) {
				ReadyExternalStatusData[env_loc][pn]=0;
			}
		}
		
		var current_date_ms = Date.parse(ToDay);
		var ext_date_str="",ext_date={},ext_date_ms=0,ext_location='',location_index=-1;
		for(var i in ExternalStatusData) {
			ext_date_str = ExternalStatusData[i]['timestamp'];
			ext_location = ExternalStatusData[i]['location'].toLowerCase();
			
			ext_date       = ReturnOnlyDateConvertFromServiceBrickWithT(ext_date_str);
			ext_date_ms    = Date.parse(ext_date);
			location_index = $.inArray(ext_location, EnvLocationsSupportArr);
			
			if(current_date_ms != ext_date_ms) {continue;}
			if(location_index < 0)  {continue;}
			
			for(var j in ExternalParamNames) {
				ReadyExternalStatusData[location_index][j]=Math.round(ExternalStatusData[i][ExternalParamNames[j]]);
			}
		}
		
		ExternalStatusData=[];
		LoadExternalStatusData();
	}
	//==============================================================================
	//LOAD EXTERNAL DATA
	//==============================================================================
	function LoadExternalHistoryData() {
		$('#AppDomotics-ActPart').empty();
		$('#AppDomotics-ActPart').append($('<div>').addClass('AppDomotics-hp'));
		$('#AppDomotics-ActPart .AppDomotics-hp').append($('<div>').addClass('AppDomotics-hp-lp'));
		$('#AppDomotics-ActPart .AppDomotics-hp').append($('<div>').addClass('AppDomotics-hp-vp'));
		$('#AppDomotics-ActPart .AppDomotics-hp').append($('<div>').addClass('AppDomotics-cp'));
		$('#AppDomotics-ActPart .AppDomotics-hp').append($('<div>').addClass('AppDomotics-hp-bp'));
		
		CreateColorLegend();
		
		var txt_h1='',txt_h2='';
		if(HistoryDataView == 1) {
			txt_h1 = ShortDaysArr[HistoryStartDates[HistoryDataView]["start_date"].getDay()];
			txt_h2 = moment(HistoryStartDates[HistoryDataView]["start_date"]).format('DD.MM.YYYY');
			
			$('#AppDomotics-ActPart .AppDomotics-hp-lp').append($('<div>'));
			$('#AppDomotics-ActPart .AppDomotics-hp-lp div').append($('<h1>').append(txt_h1));
			$('#AppDomotics-ActPart .AppDomotics-hp-lp div').append($('<h2>').append(txt_h2));
		}
		$('#AppDomotics-ActPart .AppDomotics-hp-lp').append($('<ul>'));
		for(var pn in ExternalParamNames) {
			var ActHP_LPImgSrc=PathToExecuteDirectory+'images/AppDomotics_icon_'+ExternalParamNames[pn]+'.png';
			$('#AppDomotics-ActPart .AppDomotics-hp-lp ul').append($('<li>'));
			$('#AppDomotics-ActPart .AppDomotics-hp-lp ul li:last').append($('<div>').addClass('AppDomotics-hp-lp-el'));
			$('#AppDomotics-ActPart .AppDomotics-hp-lp ul li:last .AppDomotics-hp-lp-el:last').append($('<img>').attr({'src':ActHP_LPImgSrc}));
			$('#AppDomotics-ActPart .AppDomotics-hp-lp ul li:last').append($('<div>').addClass('AppDomotics-hp-lp-el'));
			$('#AppDomotics-ActPart .AppDomotics-hp-lp ul li:last .AppDomotics-hp-lp-el:last').append($('<p>').append(ParamLabelTxtArr[pn]));
		}
		
		var ObjNavLocationID='', ObjNavLocationIDPart=AppDomoticsObjectParams.ObjectsPartID.NavLocationID;
		$('#AppDomotics-ActPart .AppDomotics-hp-bp').append($('<ul>'));
		for(var env_loc_order in EnvLocationsOrderArr) {
			var env_loc = EnvLocationsOrderArr[env_loc_order];
			var ActHP_LocationImgSrc=PathToExecuteDirectory+'images/AppDomotics_icon_white_'+EnvLocationsSupportArr[env_loc]+'.png';
			ObjNavLocationID=ObjNavLocationIDPart+'_'+env_loc;
			$('#AppDomotics-ActPart .AppDomotics-hp-bp ul').append($('<li>').attr({'id':ObjNavLocationID}));
			$('#AppDomotics-ActPart .AppDomotics-hp-bp ul li:last').append($('<img>').attr({'src':ActHP_LocationImgSrc}));
			$('#AppDomotics-ActPart .AppDomotics-hp-bp ul li:last').append($('<p>').append(LocationTxtArr[env_loc]));
		}
		ActionPartHistoryChangeStyleSelectedItem();
		$('#AppDomotics-ActPart .AppDomotics-hp-bp ul li').click(function() { 
			var location_part_res = $(this).attr('id').split("_");
			if(location_part_res.length == 3) {HistoryDataLocationView = parseInt(location_part_res[2]);}
			
			ActionPartHistoryChangeStyleSelectedItem();
			LogUserInteraction('actionPartHistory', HistoryDataView, HistoryDataLocationView);
			ShowOrHideHistoryRoomData();
		});
		
		var NumberItems=ReadyExternalHistoryData.DateMS.length, 
		ItemWidth=AppDomoticsObjectParams.HistoryDataSizes.OneItemWidth[HistoryDataView], 
		VisiblePartWidth=AppDomoticsObjectParams.HistoryDataSizes.VisPartWidth, 
		ObjectWidth=NumberItems*ItemWidth, ObjectScrollLeft=0;
		
		if(ObjectWidth > VisiblePartWidth) ObjectScrollLeft=VisiblePartWidth-ObjectWidth;
		
		$('#AppDomotics-ActPart .AppDomotics-hp-vp').append($('<div>').addClass('AppDomotics-hp-vp-all').css({'width':ObjectWidth}));
		$('#AppDomotics-ActPart .AppDomotics-hp-vp-all').append($('<ul>').addClass('AppDomotics-RowHead').css({'width':ObjectWidth}));
		
		for(var i in ReadyExternalHistoryData.DateMS) {
			var current_date_ms = ReadyExternalHistoryData.DateMS[i];
			var current_date = new Date(current_date_ms);
			
			if(HistoryDataView == 1) {
				txt_h1 = moment(current_date).format('HH:mm')+' - ';
				var current_hour = current_date.getHours();
				if(current_hour == 23) {
					txt_h1 += '24:00';
				} else {
					var next_date = new Date(current_date.getFullYear(), current_date.getMonth(), current_date.getDate(), (current_hour+1));
					txt_h1 += moment(next_date).format('HH:mm');
				}
				txt_h2 = '&nbsp;';
			}
			if(HistoryDataView == 2 || HistoryDataView == 3) { 
				txt_h1 = ShortDaysArr[current_date.getDay()];
				txt_h2 = moment(current_date).format('DD.MM.YYYY');
			}
			
			$('#AppDomotics-ActPart .AppDomotics-hp-vp ul.AppDomotics-RowHead').append($('<li>').css({'width':(ItemWidth-2)}));
			$('#AppDomotics-ActPart .AppDomotics-hp-vp ul.AppDomotics-RowHead li:last').append($('<h1>').append(txt_h1));
			$('#AppDomotics-ActPart .AppDomotics-hp-vp ul.AppDomotics-RowHead li:last').append($('<h2>').append(txt_h2));
		}
		
		var ext_value=0,print_value='',el_color_index=0,
		ObjCanvasID='', ObjCanvasIDPart=AppDomoticsObjectParams.ObjectsPartID.CanvasID, 
		ObjLocationID='', ObjLocationIDPart=AppDomoticsObjectParams.ObjectsPartID.LocationID;
		var CanvasHeight=AppDomoticsObjectParams.HistoryDataSizes.CanvasHeight,
		CanvasTop=0,RowDataTop=0;
		for(var env_loc in EnvLocationsSupportArr) {
			ObjLocationID=ObjLocationIDPart+'_'+env_loc;
			$('#AppDomotics-ActPart .AppDomotics-hp-vp .AppDomotics-hp-vp-all').append($('<div>').attr({'id':ObjLocationID}).addClass('AppDomotics-RowRoom').css({'width':ObjectWidth}));
			
			if(env_loc == HistoryDataLocationView) $('#'+ObjLocationID).show(); 
			else $('#'+ObjLocationID).hide();
			
			CanvasTop=0; RowDataTop=0;
			for(var pn in ExternalParamNames) {
				CanvasTop=RowDataTop+35;
				ObjCanvasID=ObjCanvasIDPart+'_'+pn+'_'+env_loc;
				$('#'+ObjLocationID).append($('<ul>').addClass('AppDomotics-RowData').css({'width':ObjectWidth, 'top':RowDataTop}));
				$('#'+ObjLocationID).append($('<canvas>').attr({'id':ObjCanvasID, 'width':ObjectWidth, 'height':CanvasHeight}).css({'top':CanvasTop, 'left':0, 'position':'absolute'}));
				
				for(var num_column in ReadyExternalHistoryData.ExtData[pn][env_loc]) {
					ext_value=ReadyExternalHistoryData.ExtData[pn][env_loc][num_column];
					
					if(ext_value > 0 && ext_value < ReadyExternalHistoryData.MinVal[pn][env_loc])
						ReadyExternalHistoryData.MinVal[pn][env_loc]=ext_value;
					if(ext_value > 0 && ext_value > ReadyExternalHistoryData.MaxVal[pn][env_loc])
						ReadyExternalHistoryData.MaxVal[pn][env_loc]=ext_value;
						
					if(ext_value > 0 && ext_value < ReadyExternalHistoryData.MinValAll[pn])
						ReadyExternalHistoryData.MinValAll[pn]=ext_value;
					if(ext_value > 0 && ext_value > ReadyExternalHistoryData.MaxValAll[pn])
						ReadyExternalHistoryData.MaxValAll[pn]=ext_value;
						
					el_color_index=ReturnColorIndex(env_loc, pn, ext_value);
					
					if(ext_value <= 0) {print_value = "&nbsp;";} 
					else print_value=ext_value+' '+ParamDimensionTxtArr[pn];
					
					$('#'+ObjLocationID+' ul:last').append($('<li>').css({'width':(ItemWidth-2), 'background-color':ColorObj.BasicColor[el_color_index]}));
					$('#'+ObjLocationID+' ul:last li:last').append($('<p>').css({'color':ColorObj.FontColor[el_color_index]}).html(print_value));
				}
				RowDataTop=RowDataTop+100+5;
			}
		}
		
		CreateTrendCanvas();
		ReadyExternalHistoryData={};
				
		if(myScrollHorizontal != null) {
			myScrollHorizontal.destroy();
			myScrollHorizontal=null;
		}
		if(ObjectWidth > VisiblePartWidth) {	
			var currScrollX=ObjectScrollLeft;
			myScrollHorizontal = new IScroll('#AppDomotics-ActPart .AppDomotics-hp-vp', {scrollbars:'custom', resizeScrollbars:false, scrollX:true, scrollY:false, interactiveScrollbars:true, momentum:false, mouseWheel:true, startX:ObjectScrollLeft});
			myScrollHorizontal.on('scrollEnd', function () {
				if(currScrollX > myScrollHorizontal.getScrollX()) {
					currScrollX=Math.floor(myScrollHorizontal.getScrollX()/ItemWidth)*ItemWidth;
				} else {
					currScrollX=Math.ceil(myScrollHorizontal.getScrollX()/ItemWidth)*ItemWidth;
				}
				myScrollHorizontal.scrollTo(currScrollX, 0);
			});
		}
		
		CountFinishedRequests = CountFinishedRequests+1;
		if(CountStartedRequests == CountFinishedRequests) {
			ViewOrHideOverlayElement(false);
		}
	}
	//******************************************************************************
	function LoadExternalStatusData() {
		var FlagActuator = false;
		
		$('#AppDomotics-ActPart').empty();
		$('#AppDomotics-ActPart').append($('<div>').addClass('AppDomotics-sp'));
		$('#AppDomotics-ActPart .AppDomotics-sp').append($('<div>').addClass('AppDomotics-sp-data'));
		$('#AppDomotics-ActPart .AppDomotics-sp').append($('<div>').addClass('AppDomotics-cp'));
		
		CreateColorLegend();
		
		$('#AppDomotics-ActPart .AppDomotics-sp-data').append($('<ul>'));
		var ext_value=0,print_value='',el_color_index=0;
		for(var env_loc_order in EnvLocationsOrderArr) {
			var env_loc_key = EnvLocationsOrderArr[env_loc_order];
			var env_loc_keyword = EnvLocationsSupportArr[env_loc_key];
			var ActSP_LocationImgSrc=PathToExecuteDirectory+'images/AppDomotics_icon_white_'+env_loc_keyword+'.png';
			$('#AppDomotics-ActPart .AppDomotics-sp-data ul').append($('<li>'));
			$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last').append($('<div>').addClass('AppDomotics-sp-head'));
			$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-head').append($('<img>').attr({'src':ActSP_LocationImgSrc}));
			$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-head').append($('<p>').append(LocationTxtArr[env_loc_key]));
			
			for(var pn_key in ExternalParamNames) {
				ext_value=ReadyExternalStatusData[env_loc_key][pn_key];
				el_color_index=ReturnColorIndex(env_loc_key, pn_key, ext_value);
				
				if(ext_value <= 0) print_value = AppLabelsTxtArr[0];
				else print_value=ext_value+' '+ParamDimensionTxtArr[pn_key];
				
				var ActSP_ParamImgSrc=PathToExecuteDirectory+'images/AppDomotics_icon_'+ExternalParamNames[pn_key]+'.png';
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last').append($('<div>').addClass('AppDomotics-sp-data-one'));
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-data-one:last').append($('<div>').addClass('AppDomotics-sp-data-label'));
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-data-one:last .AppDomotics-sp-data-label').append($('<div>').addClass('AppDomotics-sp-data-label-el'));
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-data-label-el:last').append($('<img>').attr({'src':ActSP_ParamImgSrc}));
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-data-one:last .AppDomotics-sp-data-label').append($('<div>').addClass('AppDomotics-sp-data-label-el'));
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-data-label-el:last').append($('<p>').append(ParamLabelTxtArr[pn_key]));
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-data-one:last').append($('<div>').addClass('AppDomotics-sp-data-value').css({'background-color':ColorObj.BasicColor[el_color_index]}));
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-data-one:last .AppDomotics-sp-data-value').append($('<p>').css({'color':ColorObj.FontColor[el_color_index]}).html(print_value));
				
				if(ext_value <= 0) {
					$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last .AppDomotics-sp-data-one:last .AppDomotics-sp-data-value p').css({'font-size':17});
				}
			}
			
			if(LightControlActuatorsSupport.ActuatorEnvLocation == env_loc_keyword) {
				FlagActuator = true;
				$('#AppDomotics-ActPart .AppDomotics-sp-data ul li:last').append($('<div>').addClass('AppDomotics-actuator'));
			}
		}
		
		CountFinishedRequests = CountFinishedRequests+1;
		if(CountStartedRequests == CountFinishedRequests) {
			ViewOrHideOverlayElement(false);
		}
		
		CreateActuatorElements(FlagActuator);
	}
	//==============================================================================
	//CREATE FUNCTIONS
	//==============================================================================
	function CreateTrendCanvas() {
		var el_w=AppDomoticsObjectParams.HistoryDataSizes.OneItemWidth[HistoryDataView], 
		canvas_h=AppDomoticsObjectParams.HistoryDataSizes.CanvasHeight,
		rect_w=10,rect_h=10,rect_fill_arr=['#6c7ea2', '#6c7ea2', '#6c7ea2'],rect_fill='',
		el_w_half=(el_w/2),canvas_h_half=(canvas_h/2),rect_w_half=(rect_w/2),rect_h_half=(rect_h/2),
		rect_x=0,rect_y=0,rect_y_range=(canvas_h-rect_h),rect_prev_x=0,rect_prev_y=0,one_unit_h=0;
		
		var max_val=0,min_val=0,range_val= max_val-min_val;
		var current_value=0,prev_value=0;
		
		var PartObjCanvasID=AppDomoticsObjectParams.ObjectsPartID.CanvasID;
		
		for(var env_loc_key in EnvLocationsSupportArr) {
			for(var pn_key in ExternalParamNames) {
				if(ReadyExternalHistoryData.FlagIsData[pn_key][env_loc_key] == 1) {
					max_val = ReadyExternalHistoryData.MaxVal[pn_key][env_loc_key];
					min_val = ReadyExternalHistoryData.MinVal[pn_key][env_loc_key];
					//max_val = ReadyExternalHistoryData.MaxValAll[pn_key];
					//min_val = ReadyExternalHistoryData.MinValAll[pn_key];
					range_val = max_val-min_val;
					
					if(range_val > 0) one_unit_h = Math.round((rect_y_range/range_val)*100000)/100000;
					else one_unit_h=0;
					
					rect_x=0; rect_y=0; rect_prev_x=0; rect_prev_y=0;
					rect_x=rect_x+el_w_half-rect_w_half;
					
					rect_fill=rect_fill_arr[pn_key];
					var canvas = $('#'+PartObjCanvasID+'_'+pn_key+'_'+env_loc_key);
					var context = canvas[0].getContext('2d');
					context.beginPath();
					context.fillStyle=rect_fill;
					context.lineWidth=2;
					context.strokeStyle=rect_fill;
					
					for(var num_column_key in ReadyExternalHistoryData.ExtData[pn_key][env_loc_key]) {
						current_value = ReadyExternalHistoryData.ExtData[pn_key][env_loc_key][num_column_key];
						prev_value = 0;
						if(num_column_key > 0) {
							prev_value = ReadyExternalHistoryData.ExtData[pn_key][env_loc_key][(num_column_key-1)];
						}
						if(current_value > 0) {
							if(range_val == 0) {
								rect_y=canvas_h_half-rect_h_half;
							} else {
								rect_y=(max_val-current_value)*one_unit_h;
							}
							rect_y=Math.round(rect_y);
							context.fillRect(rect_x,rect_y,rect_w,rect_h);
							
							if(prev_value != 0) {
								context.moveTo((rect_prev_x+rect_w_half), (rect_prev_y+rect_h_half));
								context.lineTo((rect_x+rect_w_half), (rect_y+rect_h_half));
							}
						}
						rect_prev_x=rect_x;
						rect_prev_y=rect_y;
						rect_x=rect_x+el_w;
					}
					
					context.stroke();
					context.closePath();
				}
			}
		}
	}
	//******************************************************************************
	function CreateCanvasActuator() {
		var rect_fill='#6c7ea2', rect_x=25, rect_y=4, rect_w=1, rect_h=15,
		txt_x=0, txt_y=AppDomoticsObjectParams.ActuatorDataSizes.CanvasHeight, my_txt='';
		
		var PartObjCanvasID=AppDomoticsObjectParams.ObjectsPartID.CanvasID;
		var canvas = $('#'+PartObjCanvasID+'_'+LightControlActuatorsSupport.ActuatorEnvLocation);
		var context = canvas[0].getContext('2d');
		
		context.beginPath();				
		context.fillStyle=rect_fill;
		context.font = "18px Arial";
		
		for(var br = 0; br <= AppDomoticsObjectParams.ActuatorDataSizes.NumberItems; br++) {
			if(br == 0) {
				txt_x = rect_x-20;
				my_txt = '-100';
			}
			if(br == (AppDomoticsObjectParams.ActuatorDataSizes.NumberItems/2)) {
				txt_x = rect_x-4;
				my_txt = '0';
			}
			if(br == AppDomoticsObjectParams.ActuatorDataSizes.NumberItems) {
				txt_x = rect_x-24;
				my_txt = '+100';
			}
			
			if(br == 0 || br == (AppDomoticsObjectParams.ActuatorDataSizes.NumberItems/2) || br == AppDomoticsObjectParams.ActuatorDataSizes.NumberItems) {
				context.fillText(my_txt, txt_x, txt_y);
				rect_h=15; rect_y=4;
			} else {
				rect_h=10; rect_y=7;
			}
			
			context.fillRect(rect_x, rect_y, rect_w, rect_h);
			rect_x = rect_x + AppDomoticsObjectParams.ActuatorDataSizes.OneItemX;
		}
		context.stroke();
		context.closePath();
	}
	//==============================================================================
	//HELP FUNCTIONS
	//==============================================================================
	function ReturnDateStrFormatForServiceBrick(date_for_format) {
		if(!(date_for_format instanceof Date && !isNaN(date_for_format.valueOf()))) { 
			date_for_format = new Date(date_for_format);
		}
		
		var arr_date = [date_for_format.getFullYear(), date_for_format.getMonth(), date_for_format.getDate()];
		var string_format = 'YYYY-MM-DDTHH:mm:ss.SSSZ';
		var date_for_return = moment.tz(arr_date, UserTimezoneOlson).format(string_format);
					
		return date_for_return;
	}
	//==============================================================================
	function ReturnDateAndHoursConvertFromServiceBrickWithT(date_for_convert) {
		var timestamp_arr = date_for_convert.substring(0, 19).split("T");
		var date_arr = timestamp_arr[0].split("-");
		var time_arr = timestamp_arr[1].split(":");
		return new Date((date_arr[0]*1), ((date_arr[1]*1)-1), (date_arr[2]*1), (time_arr[0]*1));
	}
	//==============================================================================
	function ReturnDateAndTimeWithoutSecConvertFromServiceBrickWithT(date_for_convert) {
		var timestamp_arr = date_for_convert.substring(0, 19).split("T");
		var date_arr = timestamp_arr[0].split("-");
		var time_arr = timestamp_arr[1].split(":");
		return new Date((date_arr[0]*1), ((date_arr[1]*1)-1), (date_arr[2]*1), (time_arr[0]*1), (time_arr[1]*1));
	}
	//==============================================================================
	function ReturnOnlyDateConvertFromServiceBrickWithT(date_for_convert) {
		var timestamp_arr = date_for_convert.substring(0, 19).split("T");
		var date_arr = timestamp_arr[0].split("-");
		return new Date((date_arr[0]*1), ((date_arr[1]*1)-1), (date_arr[2]*1));
	}
	//==============================================================================
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
	function ReturnPreviousDateShiftMonths(current_date, num_months) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		
		if(IsEndOfTheMonth(current_date)) {
			var date_for_return = new Date(current_date.getFullYear(), (current_date.getMonth()-num_months+1), 1);
		} else {
			var date_for_return_check = new Date(current_date.getFullYear(), (current_date.getMonth()-num_months), (current_date.getDate()+1));
			if(current_date.getMonth() == date_for_return_check.getMonth()) {
				var last_day = GetLastDayOfMonth((date_for_return_check.getMonth()-num_months), date_for_return_check.getFullYear());
				var date_for_return = new Date(date_for_return_check.getFullYear(), (date_for_return_check.getMonth()-num_months), last_day);
			} else {
				var date_for_return = date_for_return_check;
			}
		}
		return date_for_return;
	}
	//==============================================================================
	//==============================================================================
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
	function GetLastDayOfMonth(check_month, check_year) {
		var month = (check_month*1) + 1;
		var last_day = 0;
	
		if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			last_day = 31;
		} else if(month == 4 || month == 6 || month == 9 || month == 11) {
	  		last_day = 30;
	  	} else if(month == 2) {
	   		if(isLeapYear(check_year))
	   			last_day = 29;
	   		else
	   			last_day = 28;
	 	}
		return last_day;
	}
	//==============================================================================
	function isLeapYear(Year) {
		if(((Year % 4)==0) && ((Year % 100)!=0) || ((Year % 400)==0))
	    	return true;
	    else
			return false;
	}
	//==============================================================================
	//==============================================================================
	function ReturnColorIndex(location_index, param_index, param_value) {
		var NormalRange=ExternalParamNormalRanges[ExternalParamNames[param_index]][EnvLocationsSupportArr[location_index]];
		var min_val=NormalRange.min,max_val=NormalRange.max,ColorIndex=0;
		
		if(param_value > 0) {
			if(param_value < min_val) ColorIndex=1;
			else if(param_value >= min_val && param_value <= max_val) ColorIndex=2;
			else ColorIndex=3;
		}
		
		return ColorIndex;
	}
	//==============================================================================
	//==============================================================================
	function ViewOrHideOverlayElement(flag_view) {
		if(flag_view) {
			$('#AppDomotics-DataContainer .AppDomotics-overlay').css({'display':'block'});
		} else {
			$('#AppDomotics-DataContainer .AppDomotics-overlay').css({'display':'none'});
		}
	}
	//******************************************************************************
	function ElementClearContent(obj_element_id) {
		$(obj_element_id).empty();
	}
	//******************************************************************************
	function ElementDisplayOrNot(obj_element_id, flag_display) {
		$(obj_element_id).css({'display':'none'});
		if(flag_display) $(obj_element_id).css({'display':'block'});
	}
	//******************************************************************************
	function ElementAddOrRemoveLoading(obj_element_id, flag_add) {
		$(obj_element_id).removeClass('AppDomotics-loading');
		if(flag_add) $(obj_element_id).addClass('AppDomotics-loading');
	}
	//==============================================================================
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
	//==============================================================================
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
	//==============================================================================
	function LogUserInteraction(in_param1, in_param2, in_param3) {
		var param1 = '', param2 = '', param3 = '';
		
		param1 = UserIteractionData.applicationName;
		if(in_param3 == -1) {
			param2 = UserIteractionData.iteractionData[in_param1][in_param2].buttonId;
			param3 = UserIteractionData.iteractionData[in_param1][in_param2].comment;
		} else {
			param2 = UserIteractionData.iteractionData[in_param1][in_param2][in_param3].buttonId;
			param3 = UserIteractionData.iteractionData[in_param1][in_param2][in_param3].comment;
		}
		
		if(param1.length > 0 && param2.length > 0 && param3.length > 0) {
			if(typeof userInteractionLogger == 'object' && userInteractionLogger != null && userInteractionLogger.hasOwnProperty('sendInteractionWithComment')) {
				userInteractionLogger.sendInteractionWithComment(param1, param2, param3);
			}
		}
	}
	//==============================================================================
	//==============================================================================
}//end of object