jQuery.support.cors = true;
//==============================================================================
//NEW VERSION - June 2016
//==============================================================================
function AppObjectDFM(){
	var LanguageSupport = [['en','en-US'], ['da','da-Dk'], ['de','de-AT'], ['it'], ['nl'], ['sr'], ['mk'], 
	['bg','bg-BG'], ['hr'], ['ro','ro-RO']];
	
	var AppObjectParams = {
		'ObjectsHtmlID':{'AppDataContainer':'#AppDFM_AppDataContainer'},
		'ObjectsPartID':{'SliderID':'AppDFM_ElementID'}, 
		'SliderSizes':{'VisPartWidth':(162*7), 'OneItemWidth':162},
		'TimelineSizes':{'VisPartHeight':(84*7), 'OneItemHeight':84, 'VisibleItems':7}
	};
		
	var LongMonthsArr=[],ShortMonthsArr=[],LongDaysArr=[],ShortDaysArr=[];
	var ExtDataMessage=[[]],IntDataMessage=[];
	
	var FunctActColorArr=['#78909C','#64B5F6','#FFA726','#BCAAA4','#4DD0E1','#81C784','#FFD600','#D4E157'];
	var FunctActSupportEventsArr=['sleeping','showering','entertaining','cooking','sanitary_visits','getting_out','houseworks','visits'];
	var FunctActSortEventsArr=[0,1,2,3,4,5,6,7];
	var MoodSupportStatusArr=['positive','negative'];
	var SliderSelectedObjectID = 0;
	
	var ToDay={},SliderStartDateMs=0,SliderEndDateMs=0,SliderMoveParams={},ExtDataMoveParams={};
	var ReadyDataForSlider={};
	
	var CountStartedRequests=1,CountFinishedRequests=0;
	
	var UserName="",UserID=0,UserLang="",UserTimezoneOlson="",UserLangApp="en";
	var PathToSB_DailyFunctioning="",PathToSB_LRMood="",PathToExecuteDirectory="";
	
	var myScrollHorizontal=null,myScrollVertical=null;
	
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
	this.InitAppObject=function(){
		if(!InitExtUserAndDomainPathParams()) {
			return;
		}
		
		InitUserLang();
		InitExtDataMessage();
					
		$.ajax({
			url: PathToExecuteDirectory+"xml/app_DFM_lang_"+UserLangApp+".xml",
			type: "GET",
			dataType: "xml",
			async: true,
			cache: true,
			error: function() {},
			success: function(xml) {
				if(InitLangAppData(xml)) {
					InitDataSlider();
					InitMoveDataExtData();
					
					GetExternalDataMood();
				}
			}
		});
	}
	//==============================================================================
	//INIT FUNCTIONS
	//==============================================================================
	function InitExtUserAndDomainPathParams() {
		PathToSB_DailyFunctioning="";
		PathToSB_LRMood="";
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
			PathToSB_DailyFunctioning = PathFromApiObj.protocol+'//'+PathFromApiObj.host;
			PathToSB_LRMood = PathFromApiObj.protocol+'//'+PathFromApiObj.host;
			if(env.length > 0) {
				PathToSB_DailyFunctioning += '/applications-'+env;
				PathToSB_LRMood += '/platform-'+env;
			}
			PathToSB_DailyFunctioning += '/service-brick-dailyfunctioning';
			PathToSB_LRMood += '/lr-mood';
			
			return true;
		} else {
			return false;
		}
	}
	//******************************************************************************
	function InitExtDataMessage() {
		ExtDataMessage=[[]];
		for(var i in FunctActSupportEventsArr) {
			ExtDataMessage[i]=[];
			for(var j = 0; j < 2; j++)
				ExtDataMessage[i][j]="&nbsp;";
		}
	}
	//******************************************************************************
	function InitDataSlider() {
		ToDay = new Date(); ToDay.setHours(0); ToDay.setMinutes(0); ToDay.setSeconds(0); ToDay.setMilliseconds(0);
		SliderStartDateMs = 0; SliderEndDateMs = 0;
		
		var end_date = new Date(ToDay.getFullYear(), ToDay.getMonth(), (ToDay.getDate()));
		var start_date = ReturnPreviousDateShiftMonths(end_date, 1);
		
		SliderStartDateMs = Date.parse(start_date);
		SliderEndDateMs = Date.parse(end_date);
		
		ReadyDataForSlider={'ReadyData':[],'DatesMood':[]};
		
		var my_date_ms=0,my_date='',date_mood='';
		my_date_ms = SliderStartDateMs;
		while(my_date_ms <= SliderEndDateMs) {
			my_date = new Date(my_date_ms);
			date_mood = ReturnDateStrFormatForMood(ReturnNextDateShiftDays(my_date, -1));
			
			ReadyDataForSlider.ReadyData[ReadyDataForSlider.ReadyData.length]={"date_ms":my_date_ms, "date_object":my_date, "mood_status":""};
			ReadyDataForSlider.DatesMood[ReadyDataForSlider.DatesMood.length]=date_mood;
			
			my_date_ms = Date.parse(ReturnNextDateShiftDays(my_date, 1));
		}
		
		SliderMoveParams = {'CurrentPos':0, 'NextPos':0, 'MaxPos':0, 'MinPos':0, 'ObjectULWidth':0};
		if(SliderSelectedObjectID == 0) {
			SliderSelectedObjectID = Date.parse(ToDay);
		}
	}
	//******************************************************************************
	function InitMoveDataExtData() {
		ExtDataMoveParams = {'CurrentPos':0, 'NextPos':0, 'MaxPos':0, 'MinPos':0, 'ObjectULHeight':0};
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
		$(xml).find("ItemGroup").each(function() {
			if($(this).attr("name") == "LongMonths") {
				$(this).children('item').each(function () {
					LongMonthsArr.push($(this).text());
				});
			}
			if($(this).attr("name") == "ShortMonths") {
				$(this).children('item').each(function () {
					ShortMonthsArr.push($(this).text());
				});
			}
			if($(this).attr("name") == "LongDays") {
				$(this).children('item').each(function () {
					LongDaysArr.push($(this).text());
				});
			}
			if($(this).attr("name") == "ShortDays") {
				$(this).children('item').each(function () {
					ShortDaysArr.push($(this).text());
				});
			}
			if($(this).attr("name") == "AppLabels") {
				$(this).children('item').each(function () {
					IntDataMessage.push($(this).text());
				});
			}
			if($(this).attr("name") == "ExtDataMessage") {
				$(this).children('item').each(function () {
					var temp_key = $.inArray($(this).attr("name"), FunctActSupportEventsArr);
					if(temp_key > -1) {
						ExtDataMessage[temp_key]=[];
						$(this).children('subitem').each(function () {
							ExtDataMessage[temp_key].push($(this).text());
						});
					}
				});
			}
		});
		return true;
	}
	//==============================================================================
	//CREATE HTML ELEMENTS
	//==============================================================================
	function CreateAppBaseElements() {		
		$('#AppDFM_AppDataContainer').empty();
		$('#AppDFM_AppDataContainer').append($('<div>').addClass('AppDFM-BaseTop'));
		$('#AppDFM_AppDataContainer').append($('<div>').addClass('AppDFM-Parts'));
		
		$('#AppDFM_AppDataContainer').append($('<div>').addClass('AppDFM-overlay'));
		ViewOrHideOverlayElement(true);
		
		$('#AppDFM_AppDataContainer .AppDFM-Parts').append($('<div>').attr('id', 'AppDFM-ExtData'));
		$('#AppDFM_AppDataContainer .AppDFM-Parts').append($('<div>').attr('id', 'AppDFM-Slider'));
		
		$('#AppDFM-ExtData').append($('<div>').addClass('AppDFM-Container'));
		$('#AppDFM-Slider').append($('<div>').addClass('AppDFM-SlBorder'));
		$('#AppDFM-Slider').append($('<div>').addClass('AppDFM-Container'));
		
		$('#AppDFM-ExtData .AppDFM-Container').append($('<div>').addClass('AppDFM-TlArrow').addClass('AppDFM-TlUp'));
		$('#AppDFM-ExtData .AppDFM-Container').append($('<div>').addClass('AppDFM-TlVisPart'));
		$('#AppDFM-ExtData .AppDFM-Container').append($('<div>').addClass('AppDFM-TlArrow').addClass('AppDFM-TlDown'));
		$('#AppDFM-ExtData .AppDFM-Container').append($('<div>').addClass('AppDFM-TlInfo'));
		
		$('#AppDFM-Slider .AppDFM-Container').append($('<div>').addClass('AppDFM-SlArrow').addClass('AppDFM-SlLeft'));
		$('#AppDFM-Slider .AppDFM-Container').append($('<div>').addClass('AppDFM-SlVisPart'));
		$('#AppDFM-Slider .AppDFM-Container').append($('<div>').addClass('AppDFM-SlArrow').addClass('AppDFM-SlRight'));
		CreateSliderElements();
		
		$('#AppDFM-ExtData .AppDFM-TlUp').click(function() {
			if(CountStartedRequests == CountFinishedRequests) {
				ExtDataMoveUpDownIScroll(1);
			}
		});
		$('#AppDFM-ExtData .AppDFM-TlDown').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				ExtDataMoveUpDownIScroll(0);
			}
		});
				
		$('#AppDFM-Slider .AppDFM-SlLeft').click(function() {
			if(CountStartedRequests == CountFinishedRequests) {
				SliderMoveLeftRightIScroll(1);
			}
		});
		$('#AppDFM-Slider .AppDFM-SlRight').click(function() { 
			if(CountStartedRequests == CountFinishedRequests) {
				SliderMoveLeftRightIScroll(0);
			}
		});
	}
	//******************************************************************************
	function CreateSliderElements() {
		var my_date_ms=0,my_date='',mood_status="",mood_status_key=-1, 
		NumberItems=0,SliderElementID='',MoodIconSrc='',txt1='',txt2='';
		
		NumberItems=ReadyDataForSlider.ReadyData.length;
		$('#AppDFM-Slider .AppDFM-SlVisPart').empty();
		$('#AppDFM-Slider .AppDFM-SlVisPart').append('<ul>');
		
		for(var i in ReadyDataForSlider.ReadyData) {
			my_date_ms  = ReadyDataForSlider.ReadyData[i]["date_ms"];
			my_date     = ReadyDataForSlider.ReadyData[i]["date_object"];
			mood_status = ReadyDataForSlider.ReadyData[i]["mood_status"];
			
			mood_status_key = $.inArray(mood_status, MoodSupportStatusArr);
			
			txt1 = LongDaysArr[my_date.getDay()];
			txt2 = my_date.getDate() < 10 ? '0'+my_date.getDate() : my_date.getDate();
			txt2+= ' '+ShortMonthsArr[my_date.getMonth()]+' '+my_date.getFullYear();
			
			SliderElementID=AppObjectParams.ObjectsPartID.SliderID+'_'+my_date_ms;
			$('#AppDFM-Slider .AppDFM-SlVisPart ul').append($('<li>').attr('id', SliderElementID));
			$('#AppDFM-Slider .AppDFM-SlVisPart ul li:last').append($('<div>').addClass('AppDFM-SlEl'));
			
			$('#AppDFM-Slider .AppDFM-SlVisPart ul li:last .AppDFM-SlEl').append($('<div>').addClass('AppDFM-box'));
			$('#AppDFM-Slider .AppDFM-SlVisPart ul li:last .AppDFM-SlEl').append($('<div>').addClass('AppDFM-mood'));
			$('#AppDFM-Slider .AppDFM-SlVisPart ul li:last .AppDFM-SlEl').append($('<div>').addClass('AppDFM-el1'));
			$('#AppDFM-Slider .AppDFM-SlVisPart ul li:last .AppDFM-SlEl').append($('<div>').addClass('AppDFM-el2'));
			
			$('#AppDFM-Slider .AppDFM-SlVisPart ul li:last .AppDFM-el1').append($('<label>').text(txt1));
			$('#AppDFM-Slider .AppDFM-SlVisPart ul li:last .AppDFM-el2').append($('<label>').text(txt2));
			
			if(mood_status_key != -1) {
				MoodIconSrc = PathToExecuteDirectory+'images/AppDFM_img_mood_'+mood_status_key+'.png';
				$('#AppDFM-Slider .AppDFM-SlVisPart ul li:last .AppDFM-mood').append($('<img>').attr({'src': MoodIconSrc}));
			}
		}
		
		SliderChangeStyleSelectedItem();
		GetExternalData();
		
		SliderMoveParams['ObjectULWidth'] = AppObjectParams.SliderSizes.OneItemWidth*NumberItems;
		SliderMoveParams['MaxPos'] = -(SliderMoveParams['ObjectULWidth']-AppObjectParams.SliderSizes.VisPartWidth);
		SliderMoveParams['CurrentPos'] = SliderMoveParams['MaxPos'];
		
		$('#AppDFM-Slider .AppDFM-SlVisPart ul').css({'width':SliderMoveParams['ObjectULWidth'], 'left':0});
		
		if(myScrollHorizontal != null) {
			myScrollHorizontal.destroy();
			myScrollHorizontal=null;
		}
		myScrollHorizontal = new IScroll('#AppDFM-Slider .AppDFM-SlVisPart', {scrollX:true, scrollY:false, momentum:false, mouseWheel:true, startX:SliderMoveParams['MaxPos'], click:true});
		myScrollHorizontal.on('scrollEnd', function () {
			if(CountStartedRequests != CountFinishedRequests) {
				myScrollHorizontal.scrollTo(SliderMoveParams['CurrentPos'], 0);
			}
			if(SliderMoveParams['CurrentPos'] > myScrollHorizontal.getScrollX()) {
				SliderMoveParams['CurrentPos']=Math.floor(myScrollHorizontal.getScrollX()/AppObjectParams.SliderSizes.OneItemWidth)*AppObjectParams.SliderSizes.OneItemWidth;
			} else {
				SliderMoveParams['CurrentPos']=Math.ceil(myScrollHorizontal.getScrollX()/AppObjectParams.SliderSizes.OneItemWidth)*AppObjectParams.SliderSizes.OneItemWidth;
			}
			myScrollHorizontal.scrollTo(SliderMoveParams['CurrentPos'], 0);
		});
		
		$('#AppDFM-Slider .AppDFM-SlVisPart ul li').click(function() {
			if(CountStartedRequests == CountFinishedRequests) {
				var slider_element_id_res = $(this).attr('id').split("_");
				if(slider_element_id_res.length == 3) {SliderSelectedObjectID = parseInt(slider_element_id_res[2]);}
				
				CountStartedRequests=1; CountFinishedRequests=0;
				ViewOrHideOverlayElement(true);
				
				SliderCenterOneItemIScroll($(this).position().left);
				SliderChangeStyleSelectedItem();
				GetExternalData();
			}
		});
	}
	//==============================================================================
	//CHANGE STYLE AND MOVE FUNCTIONS
	//==============================================================================
	function SliderChangeStyleSelectedItem() {
		$('#AppDFM-Slider .AppDFM-SlVisPart ul li').removeClass('AppDFM-el-sel');
		$('#'+AppObjectParams.ObjectsPartID.SliderID+'_'+SliderSelectedObjectID).addClass("AppDFM-el-sel");
	}
	//******************************************************************************
	function ExtDataMoveUpDownIScroll(direction) {
		if(myScrollVertical == null) return false;
		
		ExtDataMoveParams['CurrentPos']=myScrollVertical.getScrollY();
		if(direction == 0) {  
			if(ExtDataMoveParams['CurrentPos'] > ExtDataMoveParams['MaxPos']) {
				ExtDataMoveParams['NextPos']  = ExtDataMoveParams['CurrentPos'] - AppObjectParams.TimelineSizes.VisPartHeight;
				if(ExtDataMoveParams['NextPos'] < ExtDataMoveParams['MaxPos']) ExtDataMoveParams['NextPos'] = ExtDataMoveParams['MaxPos'];
				ExtDataMoveParams['CurrentPos'] = ExtDataMoveParams['NextPos'];
				myScrollVertical.scrollTo(0, ExtDataMoveParams['NextPos'], 400, IScroll.utils.ease.quadratic);
		}}
		if(direction == 1) {
			if(ExtDataMoveParams['CurrentPos'] < ExtDataMoveParams['MinPos']) {
				ExtDataMoveParams['NextPos']  = ExtDataMoveParams['CurrentPos'] + AppObjectParams.TimelineSizes.VisPartHeight;
				if(ExtDataMoveParams['NextPos'] > ExtDataMoveParams['MinPos']) ExtDataMoveParams['NextPos'] = ExtDataMoveParams['MinPos'];
				ExtDataMoveParams['CurrentPos'] = ExtDataMoveParams['NextPos'];
				myScrollVertical.scrollTo(0, ExtDataMoveParams['NextPos'], 400, IScroll.utils.ease.quadratic);
		}}
	}
	//******************************************************************************
	function SliderMoveLeftRightIScroll(direction) {
		if(myScrollHorizontal == null) return false;
		
		SliderMoveParams['CurrentPos']=myScrollHorizontal.getScrollX();
		if(direction == 0) {
			if(SliderMoveParams['CurrentPos'] > SliderMoveParams['MaxPos']) {
				SliderMoveParams['NextPos']  = SliderMoveParams['CurrentPos'] - AppObjectParams.SliderSizes.VisPartWidth;
				if(SliderMoveParams['NextPos'] < SliderMoveParams['MaxPos']) SliderMoveParams['NextPos'] = SliderMoveParams['MaxPos'];
				SliderMoveParams['CurrentPos'] = SliderMoveParams['NextPos'];
				myScrollHorizontal.scrollTo(SliderMoveParams['NextPos'], 0, 400, IScroll.utils.ease.quadratic);
		}}
		if(direction == 1) {
			if(SliderMoveParams['CurrentPos'] < SliderMoveParams['MinPos']) {
				SliderMoveParams['NextPos']  = SliderMoveParams['CurrentPos'] + AppObjectParams.SliderSizes.VisPartWidth;
				if(SliderMoveParams['NextPos'] > SliderMoveParams['MinPos']) SliderMoveParams['NextPos'] = SliderMoveParams['MinPos'];
				SliderMoveParams['CurrentPos'] = SliderMoveParams['NextPos'];
				myScrollHorizontal.scrollTo(SliderMoveParams['NextPos'], 0, 400, IScroll.utils.ease.quadratic);
		}}
	}
	//******************************************************************************
	function SliderCenterOneItemIScroll(current_position) {
		if(myScrollHorizontal == null) return false;
		
		var new_position = -(current_position - ((AppObjectParams.SliderSizes.VisPartWidth/2)-(AppObjectParams.SliderSizes.OneItemWidth/2)));
		
		if(new_position > 0) new_position = 0;
		if(new_position < -(SliderMoveParams['ObjectULWidth']-AppObjectParams.SliderSizes.VisPartWidth)) new_position = -(SliderMoveParams['ObjectULWidth']-AppObjectParams.SliderSizes.VisPartWidth);
		
		SliderMoveParams['NextPos'] = new_position;
		SliderMoveParams['CurrentPos'] = SliderMoveParams['NextPos'];
		
		myScrollHorizontal.scrollTo(new_position, 0, 400, IScroll.utils.ease.quadratic);
	}
	//==============================================================================
	//GET EXTERNAL DATA
	//==============================================================================
	function GetExternalDataMood() {
		AddOrRemoveLoadingClass(true);
		var from_date = ReturnNextDateShiftDays(SliderStartDateMs, -1);
		var to_date = ReturnNextDateShiftDays(SliderEndDateMs, -1);
		
		var from_date_format = ReturnDateStrFormatForMood(from_date);
		var to_date_format = ReturnDateStrFormatForMood(to_date);
		
		var url_mood_status = PathToSB_LRMood+"/mood4period";
		
		var ReqMoodStatus = ewallApp.ajax({
			url: url_mood_status,
			type: "GET",
			data: {userid: UserName, from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqMoodStatus).done(function (DataMoodStatus) {
			if(PrepareExternalDataMood(DataMoodStatus["value"])) {
				AddOrRemoveLoadingClass(false);
				CreateAppBaseElements();
			}
		});
	}
	//==============================================================================
	function GetExternalData() {
		EmptyElementContent();
		var from_date = new Date(SliderSelectedObjectID);
		var to_date = ReturnNextDateShiftDays(from_date, 1);
	
		var from_date_format = ReturnDateStrFormatForServiceBrick(from_date);
		var to_date_format = ReturnDateStrFormatForServiceBrick(to_date);
	
		var url_functioning_activity = PathToSB_DailyFunctioning+"/v1/"+UserName+"/functioningactivity";
		
		var ReqFunctioningActivity = ewallApp.ajax({
			url: url_functioning_activity,
			type: "GET",
			data: {from: from_date_format, to: to_date_format},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqFunctioningActivity).done(function (DataFunctioningActivity) {
			LoadExternalData(DataFunctioningActivity["functioningActivityEvents"]);
		});
	}
	//==============================================================================
	//PREPARE EXTERNAL DATA
	//==============================================================================
	function PrepareExternalDataMood(ExternalDataMoodFromSB) {
		var ext_mood_status="",ext_mood_date="",mood_status_key=-1,mood_date_key=-1;
		
		for(var i in ExternalDataMoodFromSB) {
			ext_mood_status = ExternalDataMoodFromSB[i]["mood"].toLowerCase();
			mood_status_key = $.inArray(ext_mood_status, MoodSupportStatusArr);
			if(mood_status_key == -1) {
				continue;
			}
			
			ext_mood_date=ExternalDataMoodFromSB[i]["date"];
			mood_date_key = $.inArray(ext_mood_date, ReadyDataForSlider.DatesMood);
			if(mood_date_key == -1) {
				continue;
			}
			
			ReadyDataForSlider.ReadyData[mood_date_key]["mood_status"]=ext_mood_status;
		}
		return true;
	}
	//==============================================================================
	//LOAD EXTERNAL DATA
	//==============================================================================
	function LoadExternalData(ExternalDataFromSB) {
		RemoveElementLoadingClass();
		InitMoveDataExtData();
		
		var DataForDFM = [], br = 0, extDataLen = ExternalDataFromSB.length;
		var start_date = new Date(SliderSelectedObjectID);
		var end_date = ReturnNextDateShiftDays(start_date, 1);
		var timestamp = null, timestamp_prev = null, timestamp_next = null, 
		activity = '', act_type_current = -1, diff_ms = 0;
		
		$.each(ExternalDataFromSB, function(inx, ext_data) {
			timestamp = ReturnDateConvertFromServiceBrickWithT(ext_data.timestamp);
			timestamp.setMilliseconds(0);
			timestamp.setSeconds(0);
			if(Date.parse(timestamp) >= Date.parse(end_date)) {
				return false;
			}
			
			if((inx + 1) < extDataLen) {
				timestamp_next = ReturnDateConvertFromServiceBrickWithT(ExternalDataFromSB[(inx + 1)].timestamp);
				timestamp_next.setMilliseconds(0);
				timestamp_next.setSeconds(0);
				diff_ms = timestamp_next - timestamp;
			} else {
				if(Date.parse(start_date) != Date.parse(ToDay)) {
					timestamp_next = end_date;
					diff_ms = timestamp_next - timestamp;
				} else {
					timestamp_next = null;
					diff_ms = 0;
				}
			}
			
			activity = ext_data.functioningActivity.toLowerCase();
			act_type_current = $.inArray(activity, FunctActSupportEventsArr);
			
			if(act_type_current == -1) {
				return true;
			}
			
			if(br > 0 && Date.parse(timestamp) <= Date.parse(timestamp_prev)) {
				return true;
			}
			
			DataForDFM[br]={"from":timestamp, "to":timestamp_next, "difference_ms":diff_ms, "type_activity":act_type_current};
			
			timestamp_prev = timestamp;
			br++;
		});
				
		$('#AppDFM-ExtData .AppDFM-TlVisPart').empty();
		$('#AppDFM-ExtData .AppDFM-TlVisPart').append('<ul>');
		
		var diff_ms=0, ActivityIconSrc='';
		var NumberItems=0,ItemWidth=0,MaxItemWidth=480,ItemFontSize1=0,ItemFontSize2=0;
		var txt1='',txt2='',txt2_arr=[];
		
		$.each(DataForDFM, function(key, obj) {
			txt2_arr=[];
			txt1 = moment(obj.from).format('HH:mm');
			txt1+= ' '+IntDataMessage[1];
			if((obj.to instanceof Date && !isNaN(obj.to.valueOf()))) {
				txt1 += ' - ';
				txt1 += moment(obj.to).format('HH:mm');
				txt1 += ' '+IntDataMessage[1];
				
				diff_ms = obj.difference_ms;
				diff_ms = diff_ms/1000;
				var seconds = Math.floor(diff_ms % 60);
				diff_ms = diff_ms/60;
				var minutes = Math.floor(diff_ms % 60);
				diff_ms = diff_ms/60;
				var hours = Math.floor(diff_ms % 24);
				
				if(hours > 0) {
					txt2_arr[txt2_arr.length]=hours+' '+(hours == 1 ? IntDataMessage[2] : IntDataMessage[3]);
				}
				if(minutes > 0) {
					txt2_arr[txt2_arr.length]=minutes+' '+(minutes == 1 ? IntDataMessage[4] : IntDataMessage[5]);
				}
				
				if(hours <= 0 && minutes <= 0) {
					txt2_arr[txt2_arr.length] = '0 '+IntDataMessage[5];
				}
			} else {
				txt2_arr[txt2_arr.length]='...';
			}
			txt2 = txt2_arr.join(" ");
			
			ActivityIconSrc = PathToExecuteDirectory+'images/AppDFM_icon_'+obj.type_activity+'.png';
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul').append($('<li>'));
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last').append($('<div>').addClass('AppDFM-TlEl'));
			
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-TlEl').append($('<div>').addClass('AppDFM-Tl-line'));
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-TlEl').append($('<div>').addClass('AppDFM-Tl-panel'));
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-TlEl').append($('<div>').addClass('AppDFM-Tl-badge'));
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-TlEl').append($('<div>').addClass('AppDFM-Tl-time'));
			
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-panel').append($('<div>').html(ExtDataMessage[obj.type_activity][0]+' <small>('+txt2+')</small>'));
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-badge').append($('<div>').addClass('AppDFM-Tl-icon'));
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-time').append($('<div>').text(txt1));
			
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-line').css({'background-color':FunctActColorArr[obj.type_activity]});
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-panel').css({'border-color':FunctActColorArr[obj.type_activity]});
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-icon').css({'background-color':FunctActColorArr[obj.type_activity], 'background-image':'url('+ActivityIconSrc+')'});
			
			ItemWidth=$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-panel div').width();
			while(ItemWidth > MaxItemWidth) {
				ItemFontSize1=parseInt($('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-panel div').css('font-size'));
				ItemFontSize2=parseInt($('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-panel div small').css('font-size'));
				
				$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-panel div').css({'font-size':(ItemFontSize1-1)});
				$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-panel div small').css({'font-size':(ItemFontSize2-1)});
				
				ItemWidth=$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-Tl-panel div').width();
			}
		});
		
		NumberItems=$('#AppDFM-ExtData .AppDFM-TlVisPart ul li').length;
		if(NumberItems > 0) {
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:first .AppDFM-TlEl').append('<div class="AppDFM-Tl-ArtStart"></div>');
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul li:last .AppDFM-TlEl').append('<div class="AppDFM-Tl-ArtEnd"></div>');
		} else {
			$('#AppDFM-ExtData .AppDFM-TlVisPart').empty();
			$('#AppDFM-ExtData .AppDFM-TlVisPart').append($('<div>').addClass('AppDFM-NoData'));
			$('#AppDFM-ExtData .AppDFM-TlVisPart .AppDFM-NoData').append($('<p>').text(IntDataMessage[8]));
		}
		
		ExtDataMoveParams['ObjectULHeight'] = AppObjectParams.TimelineSizes.OneItemHeight*NumberItems;
		ExtDataMoveParams['MaxPos'] = -(ExtDataMoveParams['ObjectULHeight']-AppObjectParams.TimelineSizes.VisPartHeight);
		ExtDataMoveParams['CurrentPos'] = 0;
		
		if(NumberItems > 0) {
			$('#AppDFM-ExtData .AppDFM-TlVisPart ul').css({'top':ExtDataMoveParams['CurrentPos'], 'height':ExtDataMoveParams['ObjectULHeight']});
		}
		
		if(myScrollVertical != null) {
			myScrollVertical.destroy();
			myScrollVertical=null;
		}
		if(NumberItems > AppObjectParams.TimelineSizes.VisibleItems) {
			myScrollVertical = new IScroll('#AppDFM-ExtData .AppDFM-TlVisPart', {scrollX:false, scrollY:true, momentum:false, mouseWheel:true, snap:'li'});
		}
		
		LoadSelectedDateInfo();
		
		CountFinishedRequests = CountFinishedRequests + 1;
		if(CountStartedRequests == CountFinishedRequests) {
			ViewOrHideOverlayElement(false);
		}
	}
	//******************************************************************************
	function LoadSelectedDateInfo() {
		var str_date='',selected_date=new Date(SliderSelectedObjectID);
		str_date = LongDaysArr[selected_date.getDay()]+'<br>';
		str_date += selected_date.getDate() < 10 ? '0'+selected_date.getDate() : selected_date.getDate();
		str_date += ' '+LongMonthsArr[selected_date.getMonth()]+' '+selected_date.getFullYear();
		
		$('#AppDFM-ExtData .AppDFM-TlInfo').empty();
		$('#AppDFM-ExtData .AppDFM-TlInfo').append($('<label>').html(str_date));
	}
	//==============================================================================
	//HELP FUNCTIONS
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
	function ReturnNextDateShiftDays(current_date, num_days) {
		if(!(current_date instanceof Date && !isNaN(current_date.valueOf()))) { 
			current_date = new Date(current_date);
		}
		
		var date_for_return = new Date(current_date.getFullYear(), current_date.getMonth(), (current_date.getDate()+num_days));
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
	function ReturnDateStrFormatForMood(date_for_format) {
		if(!(date_for_format instanceof Date && !isNaN(date_for_format.valueOf()))) { 
			date_for_format = new Date(date_for_format);
		}
		
		var arr_date = [date_for_format.getFullYear(), date_for_format.getMonth(), date_for_format.getDate()];
		var string_format = 'YYYY-MM-DD';
		var date_for_return = moment(arr_date).format(string_format);
		
		return date_for_return;
	}
	//==============================================================================
	function ReturnDateConvertFromServiceBrickWithT(date_for_convert) {
		var timestamp_arr = date_for_convert.substring(0, 19).split("T");
		var date_arr = timestamp_arr[0].split("-");
		var time_arr = timestamp_arr[1].split(":");
		return new Date((date_arr[0]*1), ((date_arr[1]*1)-1), (date_arr[2]*1), (time_arr[0]*1), (time_arr[1]*1), (time_arr[2]*1));
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
	function EmptyElementContent() {
		$('#AppDFM-ExtData .AppDFM-TlVisPart').empty();
		$('#AppDFM-ExtData .AppDFM-TlVisPart').removeClass('AppDFM-loading').addClass('AppDFM-loading');
		
		$('#AppDFM-ExtData .AppDFM-TlInfo').empty();
	}
	//==============================================================================
	function RemoveElementLoadingClass() {
		$('#AppDFM-ExtData .AppDFM-TlVisPart').removeClass('AppDFM-loading');
	}
	//==============================================================================
	function AddOrRemoveLoadingClass(flag_add) {
		if(flag_add) {
			$('#AppDFM_AppDataContainer').empty();
			$('#AppDFM_AppDataContainer').removeClass('AppDFM-loading').addClass('AppDFM-loading');
		} else {
			$('#AppDFM_AppDataContainer').removeClass('AppDFM-loading');
		}
	}
	//==============================================================================
	function ViewOrHideOverlayElement(flag_view) {
		if(flag_view) {
			$('#AppDFM_AppDataContainer .AppDFM-overlay').css({'display':'block'});
		} else {
			$('#AppDFM_AppDataContainer .AppDFM-overlay').css({'display':'none'});
		}
	}
	//==============================================================================
}//end of object