jQuery.support.cors = true;
//==============================================================================
//NEW VERSION - June 2016
//==============================================================================
function AppObjectHCM() {
	var AppHCMObjectParams={
		'ObjectsHtmlID':{'ActPartID':'#AppHCM-ActPart'},
		'ObjectsPartID':{'NavHpID':'AppHCM_History', 'NavMpID':'AppHCM_Measure', 'CanvasID':'AppHCM_Canvas'}, 
		'HistoryDataSizes':{
			'VisPartWidth':1078,'CanvasHeight':60,'OneItemWidth':{1:154,2:154,3:98}, 
			'VisibleItems':{1:7,2:7,3:11}
		}
	};
	
	var LanguageSupport = [['en','en-US'], ['da','da-Dk'], ['de','de-AT'], ['it'], ['nl'], ['sr'], ['mk'], 
	['bg','bg-BG'], ['hr'], ['ro','ro-RO']];
	
	var MeasureTxtArr=[], MeasureValueTxtArr=[], HistoryTxtArr=[], ShortDaysArr=[], 
	SystemMessagesArr=[],MeasureInfoTxtArr=[], ColorLegendObj={'Label':'', 'Colors':[]};
	var ColorObj = {'BasicColor': ['#e6e6e6', '#ffd600', '#64dd17', '#ffab00'], 
	'FontColor':['#666666', '#666666', '#ffffff', '#ffffff'], 
	'OrderColorKeys':[1,2,3], 'LegendColorIDs':[1,2,3]};
					
	var WhatView=2; //2 - HistoryDataView, 1 - MeasureDataView
	var MeasureDataView = 2; //0 - heart rate, 1 - SPO2, 2 - blood pressure
	var HistoryDataView = 1; //1 - day, 2 - week, 3 - month
	var ExternalHistoryData=[{},{},{}];
	var ExternalMeasurementData={"Basic":{},"Additional":{}};
	var ReadyExternalHistoryData={};
	var ReadyExternalMeasurementData=[];
	
	var ToDay={},HistoryStartDates={},HistoryEndDates={};
	
	var MeasurementParam={"CountReq":[5,5,15], "IntervalReqSec":[10,10,10]};
	var MeasurementParamOrder=[2,1];
	var CountMeasurementReq=0,IntervalMeasurement=0;
	var MeasurementDateTimePeriod={};
	
	var CountStartedRequests=1,CountFinishedRequests=0;
	
	var UserName="",UserID=0,UserLang="",UserTimezoneOlson="",UserLangApp="en", UserIteractionData={};
	var ExternalParamNames=[["heartRate"], ["oxygenSaturation"], ["systolicBloodPressure", "diastolicBloodPressure"]];
	var ExternalParamNormalRanges=[
		{'heartRate':{'min':65, 'max':85}},
		{'oxygenSaturation':{'min':80, 'max':100}},
		{'systolicBloodPressure':{'min':110, 'max':130}, 'diastolicBloodPressure':{'min':75, 'max':85}}
	];
	
	var PathToSB_Vitals="",PathToExecuteDirectory="";
	
	var myScrollHorizontal=null;
	
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
			url: PathToExecuteDirectory+"xml/app_HCM_lang_"+UserLangApp+".xml",
			type: "GET",
			dataType: "xml",
			async: true,
			cache: true,
			error: function() {return false;},
			success: function(xml) {
				if(InitLangAppData(xml)) {
					InitHistoryDatePeriods();
					InitUserIteractionData();
					CreateAppBaseElements();
				}
			}
		});
	}
	//==============================================================================
	//INIT FUNCTIONS
	//==============================================================================
	function InitExtUserAndDomainPathParams() {
		PathToSB_Vitals = "";
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
			PathToSB_Vitals = PathFromApiObj.protocol+'//'+PathFromApiObj.host;
			if(env.length > 0) {
				PathToSB_Vitals += '/applications-'+env;
			}
			PathToSB_Vitals += '/service-brick-vitals';
			
			return true;
		} else {
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
			ColorLegendObj.Colors[LCID_Key] = '';
		}
		
		$(xml).find("ItemGroup").each(function() {
			if($(this).attr("name") == "MeasureTxt") {
				$(this).children('item').each(function (index) {
					MeasureTxtArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "MeasureValueTxt") {
				$(this).children('item').each(function (index) {
					MeasureValueTxtArr[index]=[];
					if(ExternalParamNames[index].length == 1) {
						MeasureValueTxtArr[index].push($(this).text());
					} else {
						$(this).children('subitem').each(function () {
							MeasureValueTxtArr[index].push($(this).text());
						});
					}
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
			if($(this).attr("name") == "SystemMessages") {
				$(this).children('item').each(function (index) {
					SystemMessagesArr[index]=$(this).text();
				});
			}
			if($(this).attr("name") == "MeasureInfoTxt") {
				$(this).children('item').each(function (index) {
					MeasureInfoTxtArr[index]=[];
					$(this).children('subitem').each(function () {
						MeasureInfoTxtArr[index].push($(this).text());
					});
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
		
		HistoryStartDates = {
			1:{"start_date":start_date_1, "start_date_ms":Date.parse(start_date_1)}, 
			2:{"start_date":start_date_2, "start_date_ms":Date.parse(start_date_2)},
			3:{"start_date":start_date_3, "start_date_ms":Date.parse(start_date_3)}
		};
		
		HistoryEndDates = {
			1:{"end_date":end_date, "end_date_ms":Date.parse(end_date), "end_date_add":end_date_1, "end_date_add_ms":Date.parse(end_date_1)}, 
			2:{"end_date":end_date, "end_date_ms":Date.parse(end_date), "end_date_add":end_date_2, "end_date_add_ms":Date.parse(end_date_2)}, 
			3:{"end_date":end_date, "end_date_ms":Date.parse(end_date), "end_date_add":end_date_3, "end_date_add_ms":Date.parse(end_date_3)}
		};
	}
	//******************************************************************************
	function InitUserIteractionData() {
		UserIteractionData = {'applicationName':'myHealthApplication', 
			'iteractionData':{
				'1':{
					'1':{'buttonId':'saturationButton', 'comment':'User measures O2 Saturation'}, 
					'2':{'buttonId':'bloodPressureButton', 'comment':'User measures Blood pressure'}
				}, 
				'2':{
					'1':{'buttonId':'todayButton', 'comment':'User selects Today'}, 
					'2':{'buttonId':'lastWeekButton', 'comment':'User selects Last week'}, 
					'3':{'buttonId':'lastMonthButton', 'comment':'User selects Last month'}
				}
			}
		};
	}
	//******************************************************************************
	function InitMeasurementDatePeriod() {
		var seconds = MeasurementParam.CountReq[MeasureDataView]*MeasurementParam.IntervalReqSec[MeasureDataView];
		
		var from_date = new Date(); from_date.setMilliseconds(0);
		var to_date = ReturnDateShiftDays(from_date, 1);
		
		MeasurementDateTimePeriod={"start_date":from_date, "start_date_ms":Date.parse(from_date), 
		"end_date":to_date, "end_date_ms":Date.parse(to_date)};
	}
	//==============================================================================
	//CREATE HTML ELEMENTS
	//==============================================================================
	function CreateAppBaseElements() {
		$('#AppHCM_AppDataContainer').empty();
		$('#AppHCM_AppDataContainer').append($('<div>').attr('id', 'AppHCM-PartAll'));
		$('#AppHCM_AppDataContainer').append($('<div>').addClass('AppHCM-overlay'));
				
		$('#AppHCM-PartAll').append($('<div>').attr('id', 'AppHCM-NavPart'));
		$('#AppHCM-PartAll').append($('<div>').attr('id', 'AppHCM-ActPart'));
		
		$('#AppHCM-NavPart').append($('<div>').addClass('AppHCM-mp'));
		$('#AppHCM-NavPart').append($('<div>').addClass('AppHCM-hp'));
		
		$('#AppHCM-NavPart .AppHCM-mp').append($('<h1>').text(SystemMessagesArr[0]));
		$('#AppHCM-NavPart .AppHCM-mp').append($('<ul>'));
		var NavMpElementID='',NavMpImgSrc='';		
		for(var j_order in MeasurementParamOrder) {
			var j = MeasurementParamOrder[j_order];
			
			NavMpElementID=AppHCMObjectParams.ObjectsPartID.NavMpID+'_'+j;
			NavMpImgSrc=PathToExecuteDirectory+'images/AppHCM_measure_'+j+'.png';
			
			$('#AppHCM-NavPart .AppHCM-mp ul').append($('<li>').attr('id', NavMpElementID));
			$('#AppHCM-NavPart .AppHCM-mp ul li:last').append($('<div>'));
			$('#AppHCM-NavPart .AppHCM-mp ul li:last div').append($('<img>').attr({'src':NavMpImgSrc}));
			$('#AppHCM-NavPart .AppHCM-mp ul li:last div').append($('<p>').append(MeasureTxtArr[j]));
		}
		
		$('#AppHCM-NavPart .AppHCM-hp').append($('<h1>').text(SystemMessagesArr[1]));
		$('#AppHCM-NavPart .AppHCM-hp').append($('<ul>'));
		var NavHpElementID='',NavHpImgSrc='';
		for(var j in HistoryTxtArr) {
			NavHpElementID=AppHCMObjectParams.ObjectsPartID.NavHpID+'_'+((j*1)+1);
			NavHpImgSrc=PathToExecuteDirectory+'images/AppHCM_history_'+j+'.png';
			
			$('#AppHCM-NavPart .AppHCM-hp ul').append($('<li>').attr('id', NavHpElementID));
			$('#AppHCM-NavPart .AppHCM-hp ul li:last').append($('<div>'));
			$('#AppHCM-NavPart .AppHCM-hp ul li:last div').append($('<img>').attr({'src':NavHpImgSrc}));
			$('#AppHCM-NavPart .AppHCM-hp ul li:last div').append($('<p>').append(HistoryTxtArr[j]));
		}
		
		$('#AppHCM-NavPart .AppHCM-mp ul li').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				var measure_part_res = $(this).attr('id').split("_");
				if(measure_part_res.length == 3) {MeasureDataView = parseInt(measure_part_res[2]);}
				
				WhatView=1;
				MeasurePartChangeStyleSelectedItem();
				CountStartedRequests=0;
				CountFinishedRequests=0;
				CountMeasurementReq=0;
				
				LogUserInteraction();
				
				ViewOrHideOverlayElement(false);
				ViewMeasurementInfo();
			}
		});
		
		$('#AppHCM-NavPart .AppHCM-hp ul li').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				var history_part_res = $(this).attr('id').split("_");
				if(history_part_res.length == 3) {HistoryDataView = parseInt(history_part_res[2]);}
				
				WhatView=2;
				HistoryPartChangeStyleSelectedItem();
				CountStartedRequests=1;
				CountFinishedRequests=0;
				
				LogUserInteraction();
				
				ViewOrHideOverlayElement(true);
				GetExternalHistoryData();
			}
		});
		
		if(WhatView == 2) {
			HistoryPartChangeStyleSelectedItem();
			CountStartedRequests=1;
			CountFinishedRequests=0;
			ViewOrHideOverlayElement(true);
			GetExternalHistoryData();
		}
		if(WhatView == 1) {
			MeasurePartChangeStyleSelectedItem();
			CountStartedRequests=0;
			CountFinishedRequests=0;
			CountMeasurementReq=0;
			ViewOrHideOverlayElement(false);
			ViewMeasurementInfo();
		}
	}
	//******************************************************************************
	function ViewMeasurementInfo() {
		$('#AppHCM-ActPart').empty();
		$('#AppHCM-ActPart').append($('<div>').addClass('AppHCM-mp-info'));
		$('#AppHCM-ActPart').append($('<div>').addClass('AppHCM-mp-data'));
		$('#AppHCM-ActPart').append($('<div>').addClass('AppHCM-mp-button'));
		
		$('#AppHCM-ActPart .AppHCM-mp-button').append($('<div>').addClass('AppHCM-dbs-1'));
		$('#AppHCM-ActPart .AppHCM-mp-button').append($('<div>').addClass('AppHCM-dbs-2'));
		$('#AppHCM-ActPart .AppHCM-mp-button .AppHCM-dbs-2').append($('<p>').text(SystemMessagesArr[2]));
		
		var my_index=MeasureDataView;
		$('#AppHCM-ActPart .AppHCM-mp-info').append($('<div>').addClass('AppHCM-img img-'+my_index));
		
		for(var m in MeasureInfoTxtArr[my_index]) {
			$('#AppHCM-ActPart .AppHCM-mp-info').append($('<p>').html(MeasureInfoTxtArr[my_index][m]));
		}
		
		var ObjectID=AppHCMObjectParams.ObjectsHtmlID.ActPartID+' .AppHCM-mp-data';
		$('#AppHCM-ActPart .AppHCM-mp-button').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				CountStartedRequests=1;
				CountFinishedRequests=0;
				ViewOrHideOverlayElement(true);
				EmptyElementContent(ObjectID, 1);
				InitMeasurementDatePeriod();
				GetExternalMeasurementData();
			}
		});
	}
	//==============================================================================
	//CHANGE STYLE FUNCTIONS
	//==============================================================================
	function MeasurePartChangeStyleSelectedItem() {
		$('#AppHCM-NavPart ul li').removeClass();
		$('#AppHCM-NavPart .AppHCM-mp ul #'+AppHCMObjectParams.ObjectsPartID.NavMpID+'_'+MeasureDataView).addClass("AppHCM-el-sel");
	}
	//******************************************************************************
	function HistoryPartChangeStyleSelectedItem() {
		$('#AppHCM-NavPart ul li').removeClass();
		$('#AppHCM-NavPart .AppHCM-hp ul #'+AppHCMObjectParams.ObjectsPartID.NavHpID+'_'+HistoryDataView).addClass("AppHCM-el-sel");
	}
	//==============================================================================
	//GET EXTERNAL DATA
	//==============================================================================
	function GetExternalHistoryData() {
		var from_date_format = ReturnDateStrFormatForServiceBrick(HistoryStartDates[HistoryDataView]["start_date"]);
		var to_date_format = ReturnDateStrFormatForServiceBrick(HistoryEndDates[HistoryDataView]["end_date_add"]);
		
		var ObjectID=AppHCMObjectParams.ObjectsHtmlID.ActPartID;
		
		var url_blood_pressure = PathToSB_Vitals+"/v1/"+UserName+"/bloodpressure";
		var url_heart_rate = PathToSB_Vitals+"/v1/"+UserName+"/heartrate";
		var url_oxygen_saturation = PathToSB_Vitals+"/v1/"+UserName+"/oxygensaturation";
		
		EmptyElementContent(ObjectID, 1);
		
		var ReqBloodPressure = ewallApp.ajax({
			url: url_blood_pressure,
			type: "GET",
			data: {from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		var ReqHeartRate = ewallApp.ajax({
			url: url_heart_rate,
			type: "GET",
			data: {from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		var ReqOxygenSaturation = ewallApp.ajax({
			url: url_oxygen_saturation,
			type: "GET",
			data: {from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqBloodPressure, ReqHeartRate, ReqOxygenSaturation).done(function (DataBloodPressure, DataHeartRate, DataOxygenSaturation) {
			ExternalHistoryData[0]=DataHeartRate[0]["heartRateMeasurements"];
			ExternalHistoryData[1]=DataOxygenSaturation[0]["oxygenSaturationMeasurements"];
			ExternalHistoryData[2]=DataBloodPressure[0]["bloodPressureMeasurements"];
			
			RemoveElementLoadingClass(ObjectID);
			
			PrepareExternalHistoryData();
		});
	}
	//******************************************************************************
	function GetExternalMeasurementData() {
		clearInterval(IntervalMeasurement);
		
		var from_date_format = ReturnDateTimeStrFormatForServiceBrick(MeasurementDateTimePeriod["start_date"]);
		var to_date_format = ReturnDateStrFormatForServiceBrick(MeasurementDateTimePeriod["end_date"]);
		
		var url_heartrate = PathToSB_Vitals+"/v1/"+UserName+"/heartrate";
		//var url_heartrate = PathToExecuteDirectory+"heartrate_day.json";
		
		if(MeasureDataView == 0) {
			var url_measure = PathToSB_Vitals+"/v1/"+UserName+"/heartrate";
			//var url_measure = PathToExecuteDirectory+"heartrate_day.json";
		}
		if(MeasureDataView == 1) {
			var url_measure = PathToSB_Vitals+"/v1/"+UserName+"/oxygensaturation";
			//var url_measure = PathToExecuteDirectory+"oxygensaturation_day.json";
		} 
		if(MeasureDataView == 2) {
			var url_measure = PathToSB_Vitals+"/v1/"+UserName+"/bloodpressure";
			//var url_measure = PathToExecuteDirectory+"bloodpressure_day.json";
		}
				
		CountMeasurementReq=CountMeasurementReq+1;
			
		var ReqMeasure = ewallApp.ajax({
			url: url_measure,
			type: "GET",
			data: {from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		var ReqMeasureHeartrate = ewallApp.ajax({
			url: url_heartrate,
			type: "GET",
			data: {from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqMeasure, ReqMeasureHeartrate).done(function (DataMeasure, DataMeasureHeartrate) {
			if(MeasureDataView == 0) {
				ExternalMeasurementData.Basic=DataMeasure[0]["heartRateMeasurements"];
				ExternalMeasurementData.Additional=DataMeasureHeartrate[0]["heartRateMeasurements"];
			}
			if(MeasureDataView == 1) {
				ExternalMeasurementData.Basic=DataMeasure[0]["oxygenSaturationMeasurements"];
				ExternalMeasurementData.Additional=DataMeasureHeartrate[0]["heartRateMeasurements"];
			} 
			if(MeasureDataView == 2) {
				ExternalMeasurementData.Basic=DataMeasure[0]["bloodPressureMeasurements"];
				ExternalMeasurementData.Additional=DataMeasureHeartrate[0]["heartRateMeasurements"];
			}
			
			PrepareExternalMeasurementData();
		});		
	}
	//==============================================================================
	//PREPARE EXTERNAL DATA
	//==============================================================================
	function PrepareExternalHistoryData() {
		var LocalExternalHistoryData=[];
		ReadyExternalHistoryData={'DateMS':[],'ExtDataReal':[],'ExtDataConcat':[],'MinVal':[],'MaxVal':[],
		'FlagIsData':[],'IsExtData':false};
		
		for(var pn in ExternalParamNames) {
			ReadyExternalHistoryData.FlagIsData[pn]=0;
			ReadyExternalHistoryData.ExtDataReal[pn]=[];
			ReadyExternalHistoryData.ExtDataConcat[pn]=[];
			ReadyExternalHistoryData.MinVal[pn]=[];
			ReadyExternalHistoryData.MaxVal[pn]=[];
			
			LocalExternalHistoryData[pn]={'DateMS':[],'Values':[]};
			
			for(var pn_real in ExternalParamNames[pn]) {
				ReadyExternalHistoryData.ExtDataReal[pn][pn_real]=[];
				ReadyExternalHistoryData.MinVal[pn][pn_real]=Infinity;
				ReadyExternalHistoryData.MaxVal[pn][pn_real]=-Infinity;
				
				LocalExternalHistoryData[pn].Values[pn_real]=[];
			}
		}
		
		var ext_date_str="",ext_date={},ext_date_ms=0,ext_value=0,current_index=-1,prev_index=-1;
		var sum_val_arr=[],count_val_arr=[],concat_val_arr=[],index_br=0;
		
		if(HistoryDataView == 1) {
			for(var pn in ExternalHistoryData) {
				for(var i in ExternalHistoryData[pn]) {
					ext_date_str = ExternalHistoryData[pn][i]["timestamp"];
					ext_date = ReturnDateAndTimeWithoutSecConvertFromServiceBrickWithT(ext_date_str);
					ext_date_ms = Date.parse(ext_date);
					
					if(!ReadyExternalHistoryData.IsExtData) ReadyExternalHistoryData.IsExtData=true;
					if(ReadyExternalHistoryData.FlagIsData[pn] == 0) ReadyExternalHistoryData.FlagIsData[pn] = 1;
					
					if($.inArray(ext_date_ms, ReadyExternalHistoryData.DateMS) < 0) {
						ReadyExternalHistoryData.DateMS[ReadyExternalHistoryData.DateMS.length]=ext_date_ms;
					}
					
					if($.inArray(ext_date_ms, LocalExternalHistoryData[pn].DateMS) < 0) {
						LocalExternalHistoryData[pn].DateMS[LocalExternalHistoryData[pn].DateMS.length]=ext_date_ms;
						for(var pn_real in ExternalParamNames[pn]) {
							ext_value=parseInt(ExternalHistoryData[pn][i][ExternalParamNames[pn][pn_real]]);
							LocalExternalHistoryData[pn].Values[pn_real][LocalExternalHistoryData[pn].Values[pn_real].length]=ext_value;
						}
					}
				}
			}
			if(ReadyExternalHistoryData.IsExtData) {
				ReadyExternalHistoryData.DateMS.sort();
				for(var pn in ExternalParamNames) {
					for(var index_sort in ReadyExternalHistoryData.DateMS) {
						ext_date_ms = ReadyExternalHistoryData.DateMS[index_sort];
						current_index = $.inArray(ext_date_ms, LocalExternalHistoryData[pn].DateMS);
						concat_val_arr=[];
						for(var pn_real in ExternalParamNames[pn]) {
							if(current_index >= 0) {
								ReadyExternalHistoryData.ExtDataReal[pn][pn_real][index_sort]=LocalExternalHistoryData[pn].Values[pn_real][current_index];
							} else {
								ReadyExternalHistoryData.ExtDataReal[pn][pn_real][index_sort]=0;
							}
							concat_val_arr[concat_val_arr.length]=ReadyExternalHistoryData.ExtDataReal[pn][pn_real][index_sort];
						}
						ReadyExternalHistoryData.ExtDataConcat[pn][index_sort]=concat_val_arr.join(';');
					}
					
					index_sort = (index_sort*1) + 1;
					var min_visible_items = AppHCMObjectParams.HistoryDataSizes.VisibleItems[HistoryDataView];
					if(index_sort < min_visible_items) {
						for(var temp_index_sort = index_sort; temp_index_sort < min_visible_items; temp_index_sort++) {
							concat_val_arr=[];
							for(var pn_real in ExternalParamNames[pn]) {
								ReadyExternalHistoryData.ExtDataReal[pn][pn_real][temp_index_sort]=0;
								concat_val_arr[concat_val_arr.length]=0;
							}
							ReadyExternalHistoryData.ExtDataConcat[pn][temp_index_sort]=concat_val_arr.join(';');
						}
					}
				}
			} else {
				var h=0,temp_current_date_ms=0,temp_start_date=HistoryStartDates[HistoryDataView]["start_date"];
				index_br=0;
				for(h=6; h<=24; h+=3) {
					temp_current_date_ms=Date.parse(new Date(temp_start_date.getFullYear(), temp_start_date.getMonth(), temp_start_date.getDate(), h));
					ReadyExternalHistoryData.DateMS[index_br]=temp_current_date_ms;
					
					for(var pn in ExternalParamNames) {
						concat_val_arr=[];
						for(var pn_real in ExternalParamNames[pn]) {
							ReadyExternalHistoryData.ExtDataReal[pn][pn_real][index_br]=0;
							concat_val_arr[concat_val_arr.length]=0;
						}
						ReadyExternalHistoryData.ExtDataConcat[pn][index_br]=concat_val_arr.join(';');
					}
					index_br++;
				}
			}
			LocalExternalHistoryData=[];
		}
		if(HistoryDataView == 2 || HistoryDataView == 3) {
			var current_date_ms = HistoryStartDates[HistoryDataView]['start_date_ms'];
			index_br=0;
			while(current_date_ms < HistoryEndDates[HistoryDataView]['end_date_add_ms']) {
				ReadyExternalHistoryData.DateMS[index_br]=current_date_ms;
				for(var pn in ExternalParamNames) {
					concat_val_arr=[];
					for(var pn_real in ExternalParamNames[pn]) {
						ReadyExternalHistoryData.ExtDataReal[pn][pn_real][index_br]=0;
						concat_val_arr[concat_val_arr.length]=0;
					}
					ReadyExternalHistoryData.ExtDataConcat[pn][index_br]=concat_val_arr.join(';');
				}
				current_date_ms = Date.parse(ReturnDateShiftDays(current_date_ms, 1));
				index_br++;
			}
			for(var pn in ExternalHistoryData) {
				sum_val_arr=[]; count_val_arr=[]; concat_val_arr=[]; prev_index=-1;
				for(var i in ExternalHistoryData[pn]) {
					ext_date_str  = ExternalHistoryData[pn][i]['timestamp'];
					ext_date      = ReturnOnlyDateConvertFromServiceBrickWithT(ext_date_str);
					ext_date_ms   = Date.parse(ext_date);
					current_index = $.inArray(ext_date_ms, ReadyExternalHistoryData.DateMS);
					
					if(current_index >= 0) {
						if(!ReadyExternalHistoryData.IsExtData) ReadyExternalHistoryData.IsExtData=true;
						if(ReadyExternalHistoryData.FlagIsData[pn] == 0) ReadyExternalHistoryData.FlagIsData[pn] = 1;
						
						if(current_index != prev_index) {
							sum_val_arr=[]; count_val_arr=[]; prev_index=current_index;
						}
						
						concat_val_arr=[];
						for(var pn_real in ExternalParamNames[pn]) {
							ext_value=parseInt(ExternalHistoryData[pn][i][ExternalParamNames[pn][pn_real]]);					
							if(pn_real in sum_val_arr) {
								sum_val_arr[pn_real]=sum_val_arr[pn_real]+ext_value;
								count_val_arr[pn_real]=count_val_arr[pn_real]+1;
							} else {
								sum_val_arr[pn_real]=ext_value;
								count_val_arr[pn_real]=1;
							}
							ReadyExternalHistoryData.ExtDataReal[pn][pn_real][current_index]=Math.round(sum_val_arr[pn_real]/count_val_arr[pn_real]);
							concat_val_arr[concat_val_arr.length]=ReadyExternalHistoryData.ExtDataReal[pn][pn_real][current_index];
						}
						ReadyExternalHistoryData.ExtDataConcat[pn][current_index]=concat_val_arr.join(';');
					}
				}
			}
		}
		
		ExternalHistoryData=[{},{},{}];
		LoadExternalHistoryData();
	}
	//******************************************************************************
	function PrepareExternalMeasurementData() {
		var ObjectID=AppHCMObjectParams.ObjectsHtmlID.ActPartID+' .AppHCM-mp-data';
		var basic_data_length = ExternalMeasurementData.Basic.length;
		if(basic_data_length <= 0) {
			if(CountMeasurementReq < MeasurementParam.CountReq[MeasureDataView]) {
				IntervalMeasurement = setInterval(GetExternalMeasurementData, (MeasurementParam.IntervalReqSec[MeasureDataView]*1000));
			} else {
				clearInterval(IntervalMeasurement);
				
				RemoveElementLoadingClass(ObjectID);
				CountMeasurementReq=0;
				
				LoadExternalMeasurementData();
			}
		} else {
			ReadyExternalMeasurementData=[];
			var additional_data_length = ExternalMeasurementData.Additional.length;
			for(var i in ExternalParamNames) {
				ReadyExternalMeasurementData[i]=[];
				for(var p in ExternalParamNames[i]) {
					ReadyExternalMeasurementData[i][p]=0;
					if(i == MeasureDataView) {
						ReadyExternalMeasurementData[i][p]=parseInt(ExternalMeasurementData.Basic[(basic_data_length-1)][ExternalParamNames[i][p]]);
					} else if(i == 0 && additional_data_length > 0) {
						ReadyExternalMeasurementData[i][p]=parseInt(ExternalMeasurementData.Additional[(additional_data_length-1)][ExternalParamNames[i][p]]);
					}
				}
			}
			
			ExternalMeasurementData={"Basic":{},"Additional":{}};
			
			RemoveElementLoadingClass(ObjectID);
			CountMeasurementReq=0;
			
			LoadExternalMeasurementData();
		}
	}
	//==============================================================================
	//LOAD EXTERNAL DATA
	//==============================================================================
	function LoadExternalHistoryData() {
		$('#AppHCM-ActPart').empty();
		$('#AppHCM-ActPart').append($('<div>').addClass('AppHCM-hp'));
		$('#AppHCM-ActPart .AppHCM-hp').append($('<div>').addClass('AppHCM-hp-lp'));
		$('#AppHCM-ActPart .AppHCM-hp').append($('<div>').addClass('AppHCM-hp-vp'));
		$('#AppHCM-ActPart .AppHCM-hp').append($('<div>').addClass('AppHCM-hp-cp'));
		
		$('#AppHCM-ActPart .AppHCM-hp-cp').append($('<label>').text(ColorLegendObj.Label));
		$('#AppHCM-ActPart .AppHCM-hp-cp').append($('<ul>'));
		var ColorKey=0, ColorKeyNew=0, ColorLegendBG='', ColorLegendTxt='';
		for(var color_order_key in ColorObj.OrderColorKeys) {
			ColorKey          = ColorObj.OrderColorKeys[color_order_key];
			ColorLegendBG     = ColorObj.BasicColor[ColorKey];
			ColorKeyNew       = (ColorKey*1)-1;
			ColorLegendTxt    = '';
			if(ColorKeyNew in ColorLegendObj.Colors) {
				ColorLegendTxt = ColorLegendObj.Colors[ColorKeyNew];
			}
			$('#AppHCM-ActPart .AppHCM-hp-cp ul').append($('<li>'));
			$('#AppHCM-ActPart .AppHCM-hp-cp ul li:last').append($('<div>'));
			$('#AppHCM-ActPart .AppHCM-hp-cp ul li:last div').append($('<span>').css({'background-color':ColorLegendBG}));
			$('#AppHCM-ActPart .AppHCM-hp-cp ul li:last div').append($('<p>').text(ColorLegendTxt));
		}
		
		var txt_h1='',txt_h2='';
		if(HistoryDataView == 1) {
			txt_h1 = ShortDaysArr[HistoryStartDates[HistoryDataView]["start_date"].getDay()];
			txt_h2 = moment(HistoryStartDates[HistoryDataView]["start_date"]).format('DD.MM.YYYY');
			
			$('#AppHCM-ActPart .AppHCM-hp-lp').append($('<div>'));
			$('#AppHCM-ActPart .AppHCM-hp-lp div').append($('<h1>').append(txt_h1));
			$('#AppHCM-ActPart .AppHCM-hp-lp div').append($('<h2>').append(txt_h2));
		}
		
		var HpLpSingleElementHeight=100,HpLpSingleElementOffset=9,HpLpMultiElementOffset=2,
		HpLpElementHeight=0,HpLpElementOffset=0;
		$('#AppHCM-ActPart .AppHCM-hp-lp').append($('<ul>'));
		for(var pn in ExternalParamNames) {
			if(ExternalParamNames[pn].length == 1) {
				HpLpElementHeight=HpLpSingleElementHeight;
				HpLpElementOffset=9;
			} else {
				HpLpElementHeight=(ExternalParamNames[pn].length*HpLpSingleElementHeight)+HpLpMultiElementOffset;
				HpLpElementOffset=0;
			}
			$('#AppHCM-ActPart .AppHCM-hp-lp ul').append($('<li>').css({'height':HpLpElementHeight,'margin-bottom':HpLpElementOffset}));
			$('#AppHCM-ActPart .AppHCM-hp-lp ul li:last').append($('<p>').css({'height':HpLpElementHeight}).append(MeasureTxtArr[pn]));
		}
		
		var NumberItems=ReadyExternalHistoryData.DateMS.length;
		if(NumberItems < AppHCMObjectParams.HistoryDataSizes.VisibleItems[HistoryDataView]) {
			NumberItems = AppHCMObjectParams.HistoryDataSizes.VisibleItems[HistoryDataView];
		}
		var ItemWidth=AppHCMObjectParams.HistoryDataSizes.OneItemWidth[HistoryDataView], 
		VisiblePartWidth=AppHCMObjectParams.HistoryDataSizes.VisPartWidth, 
		ObjectWidth=NumberItems*ItemWidth, ObjectScrollLeft=0;
		if(ObjectWidth > VisiblePartWidth) ObjectScrollLeft=VisiblePartWidth-ObjectWidth;
		
		$('#AppHCM-ActPart .AppHCM-hp-vp').append($('<div>').addClass('AppHCM-hp-vp-all').css({'width':ObjectWidth}));
		$('#AppHCM-ActPart .AppHCM-hp-vp-all').append($('<ul>').addClass('AppHCM-RowHead').css({'width':ObjectWidth}));
		
		for(var index_res in ReadyExternalHistoryData.DateMS) {
			var current_date_ms = ReadyExternalHistoryData.DateMS[index_res];
			var current_date = new Date(current_date_ms);
			
			if(HistoryDataView == 1) {
				if(current_date_ms >= HistoryEndDates[HistoryDataView]["end_date_add_ms"]) {
					txt_h1 = '24:00';
				} else {
					txt_h1 = moment(current_date).format('HH:mm');
				}
				txt_h2 = '&nbsp;';
			}
			if(HistoryDataView == 2 || HistoryDataView == 3) { 
				txt_h1 = ShortDaysArr[current_date.getDay()];
				txt_h2 = moment(current_date).format('DD.MM.YYYY');
			}
			$('#AppHCM-ActPart .AppHCM-hp-vp-all ul.AppHCM-RowHead').append($('<li>').css({'width':(ItemWidth-2)}));
			$('#AppHCM-ActPart .AppHCM-hp-vp-all ul.AppHCM-RowHead li:last').append($('<h1>').append(txt_h1));
			$('#AppHCM-ActPart .AppHCM-hp-vp-all ul.AppHCM-RowHead li:last').append($('<h2>').append(txt_h2));
		}
		
		var ObjCanvasIDPart=AppHCMObjectParams.ObjectsPartID.CanvasID,
		ObjCanvasID='',CanvasTop=0,RowDataTop=50,
		CanvasHeight=AppHCMObjectParams.HistoryDataSizes.CanvasHeight;
		var ext_value=0,ext_value_arr=[],print_value='',el_color_index=0;
		for(var pn in ExternalParamNames) {
			for(var pn_real in ExternalParamNames[pn]) {
				ObjCanvasID=ObjCanvasIDPart+'_'+pn+'_'+pn_real;
				CanvasTop=RowDataTop+35;
				$('#AppHCM-ActPart .AppHCM-hp-vp-all').append($('<ul>').addClass('AppHCM-RowData').css({'width':ObjectWidth,'top':RowDataTop}));
				$('#AppHCM-ActPart .AppHCM-hp-vp-all').append($('<canvas>').attr({'id':ObjCanvasID,'width':ObjectWidth,'height':CanvasHeight}).css({'top':CanvasTop,'left':0,'position':'absolute'}));
				
				for(var index_res in ReadyExternalHistoryData.ExtDataReal[pn][pn_real]) {
					ext_value=ReadyExternalHistoryData.ExtDataReal[pn][pn_real][index_res];
					ext_value_arr=ReadyExternalHistoryData.ExtDataConcat[pn][index_res].split(";");
					
					if(ext_value > 0 && ext_value < ReadyExternalHistoryData.MinVal[pn][pn_real])
						ReadyExternalHistoryData.MinVal[pn][pn_real]=ext_value;
					if(ext_value > 0 && ext_value > ReadyExternalHistoryData.MaxVal[pn][pn_real])
						ReadyExternalHistoryData.MaxVal[pn][pn_real]=ext_value;
					
					el_color_index=ReturnColorIndex(pn, ext_value_arr);
					
					if(ext_value <= 0) {print_value = "&nbsp;";}
					else if(ext_value_arr.length == 1) {print_value = ext_value;}
					else {print_value = MeasureValueTxtArr[pn][pn_real]+': '+ext_value;}
					
					$('#AppHCM-ActPart .AppHCM-hp-vp-all ul.AppHCM-RowData:last').append($('<li>').css({'width':(ItemWidth-2), 'background-color':ColorObj.BasicColor[el_color_index]}));
					$('#AppHCM-ActPart .AppHCM-hp-vp-all ul.AppHCM-RowData:last li:last').append($('<p>').css({'color':ColorObj.FontColor[el_color_index]}).append(print_value));
				}
				
				RowDataTop=RowDataTop+HpLpSingleElementHeight;
				if(ExternalParamNames[pn].length == 1) {RowDataTop=RowDataTop+HpLpSingleElementOffset;} 
				else {RowDataTop=RowDataTop+HpLpMultiElementOffset;}
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
			myScrollHorizontal = new IScroll('#AppHCM-ActPart .AppHCM-hp .AppHCM-hp-vp', {scrollbars:'custom', resizeScrollbars:false, scrollX:true, scrollY:false, interactiveScrollbars:true, momentum:false, mouseWheel:true, startX:ObjectScrollLeft});
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
	function LoadExternalMeasurementData() {
		$('#AppHCM-ActPart .AppHCM-mp-data').empty();
		if(ReadyExternalMeasurementData.length <= 0) {
			$('#AppHCM-ActPart .AppHCM-mp-data').append($('<div>'));
			$('#AppHCM-ActPart .AppHCM-mp-data div').append($('<h2>').text(SystemMessagesArr[3]));
		} else {
			var ext_value_arr=[],el_color_index=0,txt1='',txt2='';
			
			$('#AppHCM-ActPart .AppHCM-mp-data').append($('<ul>'));
			for(var i in ReadyExternalMeasurementData) {
				ext_value_arr=ReadyExternalMeasurementData[i];
				if(ext_value_arr[0] > 0) {
					el_color_index=ReturnColorIndex(i, ext_value_arr);
					$('#AppHCM-ActPart .AppHCM-mp-data ul').append($('<li>'));
					$('#AppHCM-ActPart .AppHCM-mp-data ul li:last').append($('<div>').css({'background-color':ColorObj.BasicColor[el_color_index]}));
					
					for(var j in ext_value_arr) {
						if(ext_value_arr.length == 1) {
							txt1=ext_value_arr[j];
							txt2=MeasureValueTxtArr[i][j]+' '+MeasureTxtArr[i];
						} else {
							txt1=MeasureValueTxtArr[i][j]+': '+ext_value_arr[j];
							txt2=MeasureTxtArr[i];
						}
						$('#AppHCM-ActPart .AppHCM-mp-data ul li:last div').append($('<h1>').css({'color':ColorObj.FontColor[el_color_index]}).append(txt1));
					}
					
					$('#AppHCM-ActPart .AppHCM-mp-data ul li:last div').append($('<h2>').css({'color':ColorObj.FontColor[el_color_index]}).append(txt2));
				}
			}
			
			$('#AppHCM-ActPart').append($('<div>').addClass('AppHCM-Legend'));
			$('#AppHCM-ActPart .AppHCM-Legend').append($('<ul>'));
			$('#AppHCM-ActPart .AppHCM-Legend ul').append($('<li>').addClass('AppHCM-Legend-Box'));
			$('#AppHCM-ActPart .AppHCM-Legend ul li:last').append($('<p>').text(ColorLegendObj.Label));
			var ColorKey=0, ColorKeyNew=0, ColorLegendBG='', ColorLegendTxt='';
			for(var color_order_key in ColorObj.OrderColorKeys) {
				ColorKey          = ColorObj.OrderColorKeys[color_order_key];
				ColorLegendBG     = ColorObj.BasicColor[ColorKey];
				ColorKeyNew       = (ColorKey*1)-1;
				ColorLegendTxt    = '';
				if(ColorKeyNew in ColorLegendObj.Colors) {
					ColorLegendTxt = ColorLegendObj.Colors[ColorKeyNew];
				}
				$('#AppHCM-ActPart .AppHCM-Legend ul').append($('<li>'));
				$('#AppHCM-ActPart .AppHCM-Legend ul li:last').append($('<div>').css({'background-color':ColorLegendBG}));
				$('#AppHCM-ActPart .AppHCM-Legend ul li:last').append($('<p>').text(ColorLegendTxt));
			}
		}
		
		ReadyExternalMeasurementData=[];
		CountFinishedRequests = CountFinishedRequests+1;
		
		if(CountStartedRequests == CountFinishedRequests) {
			ViewOrHideOverlayElement(false);
		}
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
	function ReturnDateTimeStrFormatForServiceBrick(date_for_format) {
		if(!(date_for_format instanceof Date && !isNaN(date_for_format.valueOf()))) { 
			date_for_format = new Date(date_for_format);
		}
		
		var arr_date = [date_for_format.getFullYear(), date_for_format.getMonth(), date_for_format.getDate(), date_for_format.getHours(), date_for_format.getMinutes(), date_for_format.getSeconds()];
		var string_format = 'YYYY-MM-DDTHH:mm:ss.SSSZ';
		var date_for_return = moment.tz(arr_date, UserTimezoneOlson).format(string_format);
					
		return date_for_return;
	}
	//==============================================================================
	function ReturnDateAndTimeConvertFromServiceBrickWithT(date_for_convert) {
		var timestamp_arr = date_for_convert.substring(0, 19).split("T");
		var date_arr = timestamp_arr[0].split("-");
		var time_arr = timestamp_arr[1].split(":");
		return new Date((date_arr[0]*1), ((date_arr[1]*1)-1), (date_arr[2]*1), (time_arr[0]*1), (time_arr[1]*1), (time_arr[2]*1));
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
	function ReturnColorIndex(param_index, param_value_arr) {
		var arr_flags=[],min_val=0,max_val=0,ColorIndex=0;
		
		for(var v in param_value_arr) {
			if(param_value_arr[v] > 0) {
				var NormalRange=ExternalParamNormalRanges[param_index][ExternalParamNames[param_index][v]];
				min_val=NormalRange.min;
				max_val=NormalRange.max;
				
				if(param_value_arr[v] < min_val) arr_flags[arr_flags.length]=1;
				else if(param_value_arr[v] >= min_val && param_value_arr[v] <= max_val) arr_flags[arr_flags.length]=2;
				else arr_flags[arr_flags.length]=3;
			}
		}
		
		if(arr_flags.length == ExternalParamNames[param_index].length) {
			arr_flags.sort(function(a, b){return b-a});
			ColorIndex=arr_flags[0];
		}
		
		return ColorIndex;
	}
	//==============================================================================
	function CreateTrendCanvas() {
		var el_w=AppHCMObjectParams.HistoryDataSizes.OneItemWidth[HistoryDataView], 
		canvas_h=AppHCMObjectParams.HistoryDataSizes.CanvasHeight,
		rect_w=10,rect_h=10,rect_fill='#888888',
		el_w_half=(el_w/2),canvas_h_half=(canvas_h/2),rect_w_half=(rect_w/2),rect_h_half=(rect_h/2),
		rect_x=0,rect_y=0,rect_y_range=(canvas_h-rect_h),rect_prev_x=0,rect_prev_y=0,one_unit_h=0;
		
		var max_val=0,min_val=0,range_val= max_val-min_val;
		var current_value=0,prev_value=0;
		
		var PartObjCanvasID=AppHCMObjectParams.ObjectsPartID.CanvasID;
		
		for(var pn_key in ReadyExternalHistoryData.ExtDataReal) {
			if(ReadyExternalHistoryData.FlagIsData[pn_key] == 1) {
				for(var pn_real_key in ReadyExternalHistoryData.ExtDataReal[pn_key]) {
					max_val = ReadyExternalHistoryData.MaxVal[pn_key][pn_real_key];
					min_val = ReadyExternalHistoryData.MinVal[pn_key][pn_real_key];
					range_val = max_val-min_val;
					
					if(range_val > 0) one_unit_h = Math.round((rect_y_range/range_val)*100000)/100000;
					else one_unit_h=0;
					
					rect_x=0; rect_y=0; rect_prev_x=0; rect_prev_y=0;
					rect_x=rect_x+el_w_half-rect_w_half;
					
					var canvas = $('#'+PartObjCanvasID+'_'+pn_key+'_'+pn_real_key);
					var context = canvas[0].getContext('2d');
					context.beginPath();
					context.fillStyle=rect_fill;
					context.lineWidth=2;
					context.strokeStyle=rect_fill;
					for(var index_res_key in ReadyExternalHistoryData.ExtDataReal[pn_key][pn_real_key]) {
						current_value = ReadyExternalHistoryData.ExtDataReal[pn_key][pn_real_key][index_res_key];
						prev_value = 0;
						if(index_res_key > 0) {
							prev_value = ReadyExternalHistoryData.ExtDataReal[pn_key][pn_real_key][(index_res_key-1)];
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
	//==============================================================================
	function ViewOrHideOverlayElement(flag_view) {
		if(flag_view) {
			$('#AppHCM_AppDataContainer .AppHCM-overlay').css({'display':'block'});
		} else {
			$('#AppHCM_AppDataContainer .AppHCM-overlay').css({'display':'none'});
		}
	}
	//==============================================================================
	function EmptyElementContent(object_id, flag_class_loading) {
		$(object_id).empty();
		
		if(flag_class_loading == 1)
			$(object_id).removeClass('AppHCM-loading').addClass('AppHCM-loading');
	}
	//==============================================================================
	function RemoveElementLoadingClass(object_id) {
		$(object_id).removeClass('AppHCM-loading');
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
	function LogUserInteraction() {
		var param1 = '', param2 = '', param3 = '';
		
		param1 = UserIteractionData.applicationName;
		if(WhatView == 1) {
			param2 = UserIteractionData.iteractionData[WhatView][MeasureDataView].buttonId;
			param3 = UserIteractionData.iteractionData[WhatView][MeasureDataView].comment;
		}
		if(WhatView == 2) {
			param2 = UserIteractionData.iteractionData[WhatView][HistoryDataView].buttonId;
			param3 = UserIteractionData.iteractionData[WhatView][HistoryDataView].comment;
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