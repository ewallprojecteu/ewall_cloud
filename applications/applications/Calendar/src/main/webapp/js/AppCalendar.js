//==============================================================================
//TUS VERSION - June 2016
//==============================================================================
jQuery.support.cors = true;
function AppObjectCalendar() {
	var LanguageSupport = [['en','en-US'], ['da','da-Dk'], ['de','de-AT'], ['it'], ['nl']];
	
	var AppObjectParams = {
		'ObjectsID':{'ButtonCancel':'AppCalendar_BCancel', 'ButtonAdd':'AppCalendar_BAdd', 
					'ButtonEdit':'AppCalendar_BEdit', 'ButtonDelete':'AppCalendar_BDelete', 
					'ButtonDeleteAll':'AppCalendar_BDeleteAll', 
					'DialogTitle':'AppCalendar-Dialog-Title', 'DialogLocation':'AppCalendar-Dialog-Location', 
					'DialogRepeatInterval':'AppCalendar-Dialog-RepInt', 'DialogRepeatTimes':'AppCalendar-Dialog-RepTimes', 
					'DialogReminderTime':'AppCalendar-Dialog-RemTime', 
					'DialogTransportation':'AppCalendar-Dialog-Transportation', 
					'DialogTransportationStr':'AppCalendar-Dialog-TransportationStr', 
					'DialogMedExam':'AppCalendar-Dialog-MedExam', 
					'DialogInsCard':'AppCalendar-Dialog-InsCard', 
					'DialogOtherDocs':'AppCalendar-Dialog-OtherDocs', 
					'DialogDoctorName':'AppCalendar-Dialog-DoctorName',
					'DialogDoctorPhone':'AppCalendar-Dialog-DoctorPhone', 
					'DialogWallet':'AppCalendar-Dialog-Wallet', 
					'DialogKeys':'AppCalendar-Dialog-Keys', 
					'DialogOtherBelongings':'AppCalendar-Dialog-OtherBelongings', 
					'DialogTrainer':'AppCalendar-Dialog-Trainer', 
					'DialogDateStart':'AppCalendar-Dialog-DS', 
					'DialogDateEnd':'AppCalendar-Dialog-DE', 'DialogTimeStart':'AppCalendar-Dialog-TS', 
					'DialogTimeEnd':'AppCalendar-Dialog-TE', 
					'DialogContent':'AppCalendar-Dialog-Content'
					}
	};
	var AppObjectTextsData = {'DialogObj':{}};
	
	var AppCalendarEventSupport = {
		'Types': ['general', 'medical', 'measurements', 'eating', 'physical', 'cognitive', 'shower', 'socializing'], 
		'ExtEvents': ['medical', 'measurements', 'eating', 'physical', 'cognitive', 'shower', 'socializing'],
		'SB2AppTypes':{
			'PHYSICAL':'physical', 
			'COGNITIVE':'cognitive', 
			'BREAKFAST':'eating', 
			'LUNCH':'eating', 
			'DINNER':'eating', 
			'GROOM':'shower', 
			'SHOWER':'shower', 
			'BLOODPRESSURE':'measurements', 
			'SPO2':'measurements', 
			'HEARTRATE':'measurements'
		}, 
		'PartAppTypes2SB':{'physical':'PHYSICAL', 'cognitive':'COGNITIVE'}
	};
	
	var AppConfigData = {'Time':{}, 'Location':{}, 'Transportation':{}, 'RepeatInterval':{}, 'RepeatTimes':{}, 'ReminderTime':{}, 
			'TrainerPhysical':{}, 'TrainerCognitive':{}, 'TitleShower':{}, 'TitleMeasurements':{}, 'TitleEating':{}};
	
	var CalendarEvents = [], ReadyExtCalendarData = {}, EventDataFromCalendar = {}, EventDataForSend = {};
	var CalendarScrollVertical=null, CalendarScrollParams = {'eventReceiveTop':0, 'moveVertical':false};
	
	var UserName="",UserLang="",UserTimezoneOlson="",UserLangApp="en",UserIteractionData={};
	var PathToExecuteDirectory="",PathToSB_Calendar="";
	
	var ObjectLocalIndependentTest=null;
	//==============================================================================
	this.SetPathToExecuteDirectory = function(path) {
		PathToExecuteDirectory = path;
	}
	//==============================================================================
	this.SetObjectLocalIndependentTest = function(my_object) {
		ObjectLocalIndependentTest = my_object;
	}
	//==============================================================================
	this.InitAppObject = function() {
		if(!InitExtUserAndDomainPathParams()) {
			return false;
		}
		
		InitUserLang();
		
		$.ajax({
			url: PathToExecuteDirectory+"xml/AppCalendar_lang_"+UserLangApp+".xml",
			type: "GET",
			dataType: "xml",
			async: true,
			cache: true,
			error: function() {return false;},
			success: function(xml) {
				if(InitLangAppData(xml)) {
					InitAppConfigData();
					InitEventDataFromCalendar();
					InitReadyExtCalendarData();
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
		PathToSB_Calendar = "";
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
			PathToSB_Calendar = PathFromApiObj.protocol+'//'+PathFromApiObj.host;
			if(env.length > 0) {
				PathToSB_Calendar += '/applications-'+env;
			}
			PathToSB_Calendar += '/service-brick-calendar';
			
			return true;
		} else {
			return false;
		}
	}
	//******************************************************************************
	function InitUserLang() {
		$.each(LanguageSupport, function(key, lang_data) {
			if($.inArray(UserLang, lang_data) >= 0) {
				UserLangApp = UserLang;
				return false;
			}
		});
	}
	//******************************************************************************
	function InitLangAppData(xml) {
		var ItemName = "";
		$(xml).find("ItemGroup").each(function() {
			if($(this).attr("name") == "Dialog") {
				$(this).children('item').each(function (index) {
					ItemName = $(this).attr("name");
					AppObjectTextsData.DialogObj[ItemName] = {};
					$.each($(this).children(), function(key, obj) {
						AppObjectTextsData.DialogObj[ItemName][obj.nodeName] = $(this).text();
					});
				});
			}
		});
		return true;
	}
	//******************************************************************************
	function InitAppConfigData() {
		AppConfigData.Time = {'Labels':[], 'Hours':[], 'Minutes':[]};
		var str_label = "";
		for(var h = 0; h < 24; h++) {
			for(var m = 0; m < 60; m += 15) {
				str_label = h < 10 ? '0'+h : h;
				str_label+= ':';
				str_label+= m < 10 ? '0'+m : m;
				AppConfigData.Time.Labels[AppConfigData.Time.Labels.length] = str_label;
				AppConfigData.Time.Hours[AppConfigData.Time.Hours.length] = h;
				AppConfigData.Time.Minutes[AppConfigData.Time.Minutes.length] = m;
			}
		}
		AppConfigData.Time.Labels[AppConfigData.Time.Labels.length] = '24:00';
		AppConfigData.Time.Hours[AppConfigData.Time.Hours.length] = 24;
		AppConfigData.Time.Minutes[AppConfigData.Time.Minutes.length] = 0;
		
		AppConfigData.Location = {
				'INDOORS':AppObjectTextsData.DialogObj.Labels.Indoor, 
				'OUTDOORS':AppObjectTextsData.DialogObj.Labels.Outdoor
			};
		
		AppConfigData.RepeatInterval = {
				'HOURLY':AppObjectTextsData.DialogObj.Labels.Hourly, 
				'DAILY':AppObjectTextsData.DialogObj.Labels.Daily, 
				'WEEKLY':AppObjectTextsData.DialogObj.Labels.Weekly, 
				'MONTHLY':AppObjectTextsData.DialogObj.Labels.Monthly, 
				'YEARLY':AppObjectTextsData.DialogObj.Labels.Yearly
			};
		
		AppConfigData.RepeatTimes = {};
		for(var rt = 1; rt <= 60; rt++) {
			AppConfigData.RepeatTimes[rt] = rt;
		}
		
		AppConfigData.ReminderTime = {
				'15':AppObjectTextsData.DialogObj.Labels.ReminderTimeVal15, 
				'30':AppObjectTextsData.DialogObj.Labels.ReminderTimeVal30, 
				'120':AppObjectTextsData.DialogObj.Labels.ReminderTimeVal120, 
				'1440':AppObjectTextsData.DialogObj.Labels.ReminderTimeVal1440
		};
		
		AppConfigData.Transportation = {
				'FOOT':AppObjectTextsData.DialogObj.Labels.TransportationFoot, 
				'CAR':AppObjectTextsData.DialogObj.Labels.TransportationCar, 
				'TAXI':AppObjectTextsData.DialogObj.Labels.TransportationTaxi, 
				'OTHER':AppObjectTextsData.DialogObj.Labels.TransportationOther
		};
		
		AppConfigData.TrainerPhysical = {
				'NONE':AppObjectTextsData.DialogObj.Labels.No, 
				'VIDEO':AppObjectTextsData.DialogObj.Labels.Yes
		};
		
		AppConfigData.TrainerCognitive = {
				'NONE':AppObjectTextsData.DialogObj.Labels.No, 
				'GAMES':AppObjectTextsData.DialogObj.Labels.Yes
		};
		
		AppConfigData.TitleShower = {
				'SHOWER':AppObjectTextsData.DialogObj.Labels.Shower, 
				'GROOM':AppObjectTextsData.DialogObj.Labels.Groom
		};
		
		AppConfigData.TitleMeasurements = {
				'BLOODPRESSURE':AppObjectTextsData.DialogObj.Labels.Bloodpressure, 
				'SPO2':AppObjectTextsData.DialogObj.Labels.SPO2, 
				'HEARTRATE':AppObjectTextsData.DialogObj.Labels.Heartrate
		};
		
		AppConfigData.TitleEating = {
				'BREAKFAST':AppObjectTextsData.DialogObj.Labels.Breakfast, 
				'LUNCH':AppObjectTextsData.DialogObj.Labels.Lunch, 
				'DINNER':AppObjectTextsData.DialogObj.Labels.Dinner
		};
	}	
	//******************************************************************************
	function InitEventDataFromCalendar() {
		EventDataFromCalendar = {'IsNew':false, 'IsAllDay':false, 'ID':0, 'GroupID':0, 'AppEventType':'', 
			'Title':'', 'Start':{}, 'End':{}, 'StartArr':[], 'EndArr':[], 'StartTimeInx':0, 'EndTimeInx':0,   
			'RepeatInterval':'HOURLY', 'RepeatTimes':1, 'ReminderTime':15, 
			'IsRevertFunc':false, 'RevertFunc':{}, 'IsEventReceive':false, 'EventReceiveId':''};
	}
	//******************************************************************************
	function InitReadyExtCalendarData() {
		ReadyExtCalendarData = {'GroupIDs':{}, 'Data':{}};
		$.each(AppCalendarEventSupport.Types, function(key, val) {
			ReadyExtCalendarData.GroupIDs[val] = {};
			ReadyExtCalendarData.Data[val] = {};
		});
	}
	//******************************************************************************
	function InitPartReadyExtCalendarData(param) {
		ReadyExtCalendarData.GroupIDs[param] = {};
		ReadyExtCalendarData.Data[param] = {};
	}
	//******************************************************************************
	function InitUserIteractionData() {
		UserIteractionData = {'applicationName':'calendarApplication', 
			'iteractionData':{
				'eventReceive':{
					'general':{'buttonId':'addNewEvent', 'comment':'User lunched Add event dialog'}, 
					'medical':{'buttonId':'addNewMedicalCheckupEvent', 'comment':'User lunched Add Medical Checkup event dialog'},
					'measurements':{'buttonId':'addNewMeasurementEvent', 'comment':'User lunched Add Measurement event dialog'},
					'eating':{'buttonId':'addNewMealEvent', 'comment':'User lunched Add Meal event dialog'},
					'physical':{'buttonId':'addNewPhysicalExerciseEvent', 'comment':'User lunched Add Physical Exercise event dialog'},
					'cognitive':{'buttonId':'addNewCognitiveExerciseEvent', 'comment':'User lunched Add Cognitive Exercise event dialog'},
					'shower':{'buttonId':'addNewSelfCareEvent', 'comment':'User lunched Add Self Care event dialog'},
					'socializing':{'buttonId':'addNewSocializingEvent', 'comment':'User lunched Add Socializing event dialog'}
				}, 
				'select':{
					'general':{'buttonId':'addNewEvent', 'comment':'User lunched Add event dialog'}, 
					'medical':{'buttonId':'addNewMedicalCheckupEvent', 'comment':'User lunched Add Medical Checkup event dialog'},
					'measurements':{'buttonId':'addNewMeasurementEvent', 'comment':'User lunched Add Measurement event dialog'},
					'eating':{'buttonId':'addNewMealEvent', 'comment':'User lunched Add Meal event dialog'},
					'physical':{'buttonId':'addNewPhysicalExerciseEvent', 'comment':'User lunched Add Physical Exercise event dialog'},
					'cognitive':{'buttonId':'addNewCognitiveExerciseEvent', 'comment':'User lunched Add Cognitive Exercise event dialog'},
					'shower':{'buttonId':'addNewSelfCareEvent', 'comment':'User lunched Add Self Care event dialog'},
					'socializing':{'buttonId':'addNewSocializingEvent', 'comment':'User lunched Add Socializing event dialog'}
				}, 
				'eventClick':{
					'general':{'buttonId':'edit/deleteEvent', 'comment':'User lunched Edit/Delete event dialog'}, 
					'medical':{'buttonId':'edit/deleteMedicalCheckupEvent', 'comment':'User lunched Edit/Delete Medical Checkup event dialog'},
					'measurements':{'buttonId':'edit/deleteMeasurementEvent', 'comment':'User lunched Edit/Delete Measurement event dialog'},
					'eating':{'buttonId':'edit/deleteMealEvent', 'comment':'User lunched Edit/Delete Meal event dialog'},
					'physical':{'buttonId':'edit/deletePhysicalExerciseEvent', 'comment':'User lunched Edit/Delete Physical Exercise event dialog'},
					'cognitive':{'buttonId':'edit/deleteCognitiveExerciseEvent', 'comment':'User lunched Edit/Delete Cognitive Exercise event dialog'},
					'shower':{'buttonId':'edit/deleteSelfCareEvent', 'comment':'User lunched Edit/Delete Self Care event dialog'},
					'socializing':{'buttonId':'edit/deleteSocializingEvent', 'comment':'User lunched Edit/Delete Socializing event dialog'}
				}, 
				'eventResize':{
					'general':{'buttonId':'resizeEvent', 'comment':'User resized the event'}, 
					'medical':{'buttonId':'resizeMedicalCheckupEvent', 'comment':'User resized the Medical Checkup event'},
					'measurements':{'buttonId':'resizeMeasurementEvent', 'comment':'User resized the Measurement event'},
					'eating':{'buttonId':'resizeMealEvent', 'comment':'User resized the Meal event'},
					'physical':{'buttonId':'resizePhysicalExerciseEvent', 'comment':'User resized the Physical Exercise event'},
					'cognitive':{'buttonId':'resizeCognitiveExerciseEvent', 'comment':'User resized the Cognitive Exercise event'},
					'shower':{'buttonId':'resizeSelfCareEvent', 'comment':'User resized the Self Care event'},
					'socializing':{'buttonId':'resizeSocializingEvent', 'comment':'User resized the Socializing event'}
				}, 
				'eventDrop':{
					'general':{'buttonId':'dropEvent', 'comment':'User dropped the event'}, 
					'medical':{'buttonId':'dropMedicalCheckupEvent', 'comment':'User dropped the Medical Checkup event'},
					'measurements':{'buttonId':'dropMeasurementEvent', 'comment':'User dropped the Measurement event'},
					'eating':{'buttonId':'dropMealEvent', 'comment':'User dropped the Meal event'},
					'physical':{'buttonId':'dropPhysicalExerciseEvent', 'comment':'User dropped the Physical Exercise event'},
					'cognitive':{'buttonId':'dropCognitiveExerciseEvent', 'comment':'User dropped the Cognitive Exercise event'},
					'shower':{'buttonId':'dropSelfCareEvent', 'comment':'User dropped the Self Care event'},
					'socializing':{'buttonId':'dropSocializingEvent', 'comment':'User dropped the Socializing event'}
				}, 
				'buttonAdd':{
					'general':{'buttonId':'addButton', 'comment':'User selected Add button'}, 
					'medical':{'buttonId':'addMedicalCheckupButton', 'comment':'User selected Add Medical Checkup button'},
					'measurements':{'buttonId':'addMeasurementButton', 'comment':'User selected Add Measurement button'},
					'eating':{'buttonId':'addMealButton', 'comment':'User selected Add Meal button'},
					'physical':{'buttonId':'addPhysicalExerciseButton', 'comment':'User selected Add Physical Exercise button'},
					'cognitive':{'buttonId':'addCognitiveExerciseButton', 'comment':'User selected Add Cognitive Exercise button'},
					'shower':{'buttonId':'addSelfCareButton', 'comment':'User selected Add Self Care button'},
					'socializing':{'buttonId':'addSocializingButton', 'comment':'User selected Add Socializing button'}
				}, 
				'buttonEdit':{
					'general':{'buttonId':'editButton', 'comment':'User selected Edit button'}, 
					'medical':{'buttonId':'editMedicalCheckupButton', 'comment':'User selected Edit Medical Checkup button'},
					'measurements':{'buttonId':'editMeasurementButton', 'comment':'User selected Edit Measurement button'},
					'eating':{'buttonId':'editMealButton', 'comment':'User selected Edit Meal button'},
					'physical':{'buttonId':'editPhysicalExerciseButton', 'comment':'User selected Edit Physical Exercise button'},
					'cognitive':{'buttonId':'editCognitiveExerciseButton', 'comment':'User selected Edit Cognitive Exercise button'},
					'shower':{'buttonId':'editSelfCareButton', 'comment':'User selected Edit Self Care button'},
					'socializing':{'buttonId':'editSocializingButton', 'comment':'User selected Edit Socializing button'}
				}, 
				'buttonDelete':{
					'general':{'buttonId':'deleteButton', 'comment':'User selected Delete button'}, 
					'medical':{'buttonId':'deleteMedicalCheckupButton', 'comment':'User selected Delete Medical Checkup button'},
					'measurements':{'buttonId':'deleteMeasurementButton', 'comment':'User selected Delete Measurement button'},
					'eating':{'buttonId':'deleteMealButton', 'comment':'User selected Delete Meal button'},
					'physical':{'buttonId':'deletePhysicalExerciseButton', 'comment':'User selected Delete Physical Exercise button'},
					'cognitive':{'buttonId':'deleteCognitiveExerciseButton', 'comment':'User selected Delete Cognitive Exercise button'},
					'shower':{'buttonId':'deleteSelfCareButton', 'comment':'User selected Delete Self Care button'},
					'socializing':{'buttonId':'deleteSocializingButton', 'comment':'User selected Delete Socializing button'}
				}, 
				'buttonDeleteAll':{
					'general':{'buttonId':'deleteGroupButton', 'comment':'User selected Delete Group button'}, 
					'medical':{'buttonId':'deleteMedicalCheckupGroupButton', 'comment':'User selected Delete Medical Checkup Group button'},
					'measurements':{'buttonId':'deleteMeasurementGroupButton', 'comment':'User selected Delete Measurement Group button'},
					'eating':{'buttonId':'deleteMealGroupButton', 'comment':'User selected Delete Meal Group button'},
					'physical':{'buttonId':'deletePhysicalExerciseGroupButton', 'comment':'User selected Delete Physical Exercise Group button'},
					'cognitive':{'buttonId':'deleteCognitiveExerciseGroupButton', 'comment':'User selected Delete Cognitive Exercise Group button'},
					'shower':{'buttonId':'deleteSelfCareGroupButton', 'comment':'User selected Delete Self Care Group button'},
					'socializing':{'buttonId':'deleteSocializingGroupButton', 'comment':'User selected Delete Socializing Group button'}
				}, 
				'buttonCancelAdd':{
					'general':{'buttonId':'cancelButtonInNewWin', 'comment':'User selected Cancel button in new event window'}, 
					'medical':{'buttonId':'cancelMedicalCheckupButtonInNewWin', 'comment':'User selected Cancel Medical Checkup button in new event window'},
					'measurements':{'buttonId':'cancelMeasurementButtonInNewWin', 'comment':'User selected Cancel Measurement button in new event window'},
					'eating':{'buttonId':'cancelMealButtonInNewWin', 'comment':'User selected Cancel Meal button in new event window'},
					'physical':{'buttonId':'cancelPhysicalExerciseButtonInNewWin', 'comment':'User selected Cancel Physical Exercise button in new event window'},
					'cognitive':{'buttonId':'cancelCognitiveExerciseButtonInNewWin', 'comment':'User selected Cancel Cognitive Exercise button in new event window'},
					'shower':{'buttonId':'cancelSelfCareButtonInNewWin', 'comment':'User selected Cancel Self Care button in new event window'},
					'socializing':{'buttonId':'cancelSocializingButtonInNewWin', 'comment':'User selected Cancel Socializing button in new event window'}
				}, 
				'buttonCancelEdit':{
					'general':{'buttonId':'cancelButtonInEditWin', 'comment':'User selected Cancel button in edit/delete event window'}, 
					'medical':{'buttonId':'cancelMedicalCheckupButtonInEditWin', 'comment':'User selected Cancel Medical Checkup button in edit/delete event window'},
					'measurements':{'buttonId':'cancelMeasurementButtonInEditWin', 'comment':'User selected Cancel Measurement button in edit/delete event window'},
					'eating':{'buttonId':'cancelMealButtonInEditWin', 'comment':'User selected Cancel Meal button in edit/delete event window'},
					'physical':{'buttonId':'cancelPhysicalExerciseButtonInEditWin', 'comment':'User selected Cancel Physical Exercise button in edit/delete event window'},
					'cognitive':{'buttonId':'cancelCognitiveExerciseButtonInEditWin', 'comment':'User selected Cancel Cognitive Exercise button in edit/delete event window'},
					'shower':{'buttonId':'cancelSelfCareButtonInEditWin', 'comment':'User selected Cancel Self Care button in edit/delete event window'},
					'socializing':{'buttonId':'cancelSocializingButtonInEditWin', 'comment':'User selected Cancel Socializing button in edit/delete event window'}
				}
			}
		};
	}
	//==============================================================================
	//CREATE HTML ELEMENTS
	//==============================================================================
	function CreateAppBaseElements() {
		$('#AppCalendar-DataContainer').empty();
		$('#AppCalendar-DataContainer').append($('<div>').addClass('AppCalendar-Parts'));
		
		$('#AppCalendar-DataContainer .AppCalendar-Parts').append($('<div>').attr('id', 'AppCalendar-ExtEvents'));
		$('#AppCalendar-DataContainer .AppCalendar-Parts').append($('<div>').attr('id', 'AppCalendar-Calendar'));
		$('#AppCalendar-DataContainer .AppCalendar-Parts').append($('<div>').attr('id', 'AppCalendar-Dialog').addClass('ui-draggable ui-draggable-handle').css({'display':'none'}));
		$('#AppCalendar-DataContainer .AppCalendar-Parts').append($('<div>').addClass('AppCalendar-Load'));
		$('#AppCalendar-DataContainer .AppCalendar-Parts').append($('<div>').addClass('AppCalendar-overlay'));
		
		CreateHTMLExtEvents();
		CreateHTMLCalendar();
		CreateHTMLDialogElement();
		
	    GetExternalCalendarDataAll();
	}
	//******************************************************************************
	function CreateHTMLExtEvents() {
		$('#AppCalendar-ExtEvents').empty();
		
		$('#AppCalendar-ExtEvents').append($('<div>').addClass('AppCalendar-table'));
		$.each(AppCalendarEventSupport.ExtEvents, function(key, val) {
			$('#AppCalendar-ExtEvents .AppCalendar-table').append($('<div>').addClass('AppCalendar-cell AppCalendar-corner-all AppCalendar-gradient AppCalendar-icon'));
			$('#AppCalendar-ExtEvents .AppCalendar-table .AppCalendar-cell:last').append($('<div>').addClass('AppCalendar-icon-'+val));
			$('#AppCalendar-ExtEvents .AppCalendar-table .AppCalendar-cell:last').attr({'data-duration':'01:00', 'data-event':'{"title":"", "stick":true, "appEventType":"'+val+'"}'});
		});
		
		$('#AppCalendar-ExtEvents .AppCalendar-icon').draggable({containment:'#AppCalendar-DataContainer', scroll:false, revert:true, 
			revertDuration:0, cursorAt:{top:0}, 
			helper: function(event) {
				return $('<div class="AppCalendar-icon-clone AppCalendar-corner-all AppCalendar-gradient AppCalendar-icon"><div class="'+$(this).children().first().attr('class')+'"></div></div>');
			}, 
			stop: function(evt, ui) { 
				CalendarScrollParams.eventReceiveTop = ui.position.top;
	        }
		});
		$('#AppCalendar-ExtEvents .AppCalendar-icon').addTouch();
	}
	//******************************************************************************
	function CreateHTMLCalendar() {
		if(CalendarScrollVertical != null) {
			CalendarScrollVertical.destroy();
			CalendarScrollVertical=null;
		}
		$('#AppCalendar-Calendar').fullCalendar('destroy');
		
		$('#AppCalendar-Calendar').fullCalendar({
			header: {
			    left: 'prev, next today',
			    center: 'title',
			    right: 'agendaDay, agendaWeek, month'
			}, 
			theme: false, 
			defaultView: 'agendaDay',
			firstDay: 1,
			height: 775, 
			slotLabelFormat: 'HH:mm', 
			timeFormat: 'HH:mm', 
			scrollTime: "00:00:00",
			dragScroll: false, 
			lang: UserLangApp,
			selectable: true,
			selectHelper: true,
			editable: true, 
			droppable: true, 
			dropAccept: '.AppCalendar-icon', 
			eventLimit: true, 
			viewRender: function(view, element) {
				element.addTouch();
				if(CalendarScrollVertical != null) {
					CalendarScrollVertical.destroy();
					CalendarScrollVertical=null;
				}
				if(view.name != "month") {
					CalendarScrollVertical = new IScroll('#AppCalendar-Calendar .fc-scroller', {
						scrollbars:'custom', resizeScrollbars:false, scrollX:false, scrollY:true, 
						startY:0, interactiveScrollbars:true, disableTouch:true, disablePointer:true, 
						disableMouse:true, momentum:false, mouseWheel:true, snap:'.fc-scroller .fc-slats td'});
					CalendarScrollParams.moveVertical = true;
				} else {
					CalendarScrollParams.moveVertical = false;
				}
			}, 
			eventAfterAllRender: function(view) {
				if(CalendarScrollVertical != null) {
					if(CalendarScrollParams.moveVertical) {
						CalendarScrollVertical.goToPage(0, 12, 1000);
						CalendarScrollParams.moveVertical = false;
					}
				}
				$(window).trigger('resize');
			}
		});
		
		var MyCalendar = $('#AppCalendar-Calendar').fullCalendar('getCalendar');
		$(window).trigger('resize');
		
		MyCalendar.on('eventReceive', function(event) {
			InitEventDataFromCalendar();
			if(event.allDay || CalendarScrollParams.eventReceiveTop <= 248) {
				$('#AppCalendar-Calendar').fullCalendar('removeEvents', event._id);
			} else {
				EventDataFromCalendar.IsEventReceive = true;
				EventDataFromCalendar.EventReceiveId = event._id;
				EventDataFromCalendar.IsAllDay = false;
				EventDataFromCalendar.IsNew = true;
				EventDataFromCalendar.Title = '';
				EventDataFromCalendar.Start = event.start;
				EventDataFromCalendar.End = event.end;
				EventDataFromCalendar.AppEventType = event.appEventType;
				LogUserInteraction('eventReceive', EventDataFromCalendar.AppEventType);
				OpenDialog();
			}
			CalendarScrollParams.eventReceiveTop = 0;
		});
		
		MyCalendar.on('select', function(start, end, jsEvent, view) {
			InitEventDataFromCalendar();
			if(start.hasTime()) {
				EventDataFromCalendar.IsAllDay = false;
			} else {
				EventDataFromCalendar.IsAllDay = true;
			}
			EventDataFromCalendar.IsNew = true;
			EventDataFromCalendar.Title = '';
			EventDataFromCalendar.Start = start;
			EventDataFromCalendar.End = end;
			EventDataFromCalendar.AppEventType = AppCalendarEventSupport.Types[0];
			LogUserInteraction('select', EventDataFromCalendar.AppEventType);
			OpenDialog();
		});
		
		MyCalendar.on('eventClick', function(calEvent, jsEvent, view) {
			InitEventDataFromCalendar();
			EventDataFromCalendar.IsAllDay = calEvent.allDay;
			EventDataFromCalendar.IsNew = false;
			EventDataFromCalendar.ID = calEvent.id;
			EventDataFromCalendar.Title = calEvent.title;
			EventDataFromCalendar.Start = calEvent.start;
			if(calEvent.end == null) {
				if(EventDataFromCalendar.IsAllDay) {
					EventDataFromCalendar.End = calEvent.start.clone().add(1, 'days');
				} else {
					EventDataFromCalendar.End = calEvent.start.clone().add(15, 'minutes');
				}
			} else {
				EventDataFromCalendar.End = calEvent.end;
			}
			EventDataFromCalendar.AppEventType = calEvent.appEventType;
			EventDataFromCalendar.GroupID = calEvent.groupID;
			EventDataFromCalendar.RepeatInterval = calEvent.repeatInterval;
			EventDataFromCalendar.RepeatTimes = calEvent.repeatTimes;
			EventDataFromCalendar.ReminderTime = calEvent.reminderTime;
			LogUserInteraction('eventClick', EventDataFromCalendar.AppEventType);
			OpenDialog();
		});
		
		MyCalendar.on('eventResize', function(event, delta, revertFunc) {
			InitEventDataFromCalendar();
			EventDataFromCalendar.IsRevertFunc = true;
			EventDataFromCalendar.RevertFunc = revertFunc;
			EventDataFromCalendar.IsAllDay = event.allDay;
			EventDataFromCalendar.IsNew = false;
			EventDataFromCalendar.ID = event.id;
			EventDataFromCalendar.Title = event.title;
			EventDataFromCalendar.Start = event.start;
			if(event.end == null) {
				if(EventDataFromCalendar.IsAllDay) {
					EventDataFromCalendar.End = event.start.clone().add(1, 'days');
				} else {
					EventDataFromCalendar.End = event.start.clone().add(15, 'minutes');
				}
			} else {
				EventDataFromCalendar.End = event.end;
			}
			EventDataFromCalendar.AppEventType = event.appEventType;
			EventDataFromCalendar.GroupID = event.groupID;
			EventDataFromCalendar.RepeatInterval = event.repeatInterval;
			EventDataFromCalendar.RepeatTimes = event.repeatTimes;
			EventDataFromCalendar.ReminderTime = event.reminderTime;
			LogUserInteraction('eventResize', EventDataFromCalendar.AppEventType);
			OpenDialog();
		});
		
		MyCalendar.on('eventDrop', function(event, delta, revertFunc) {
			InitEventDataFromCalendar();
			EventDataFromCalendar.IsRevertFunc = true;
			EventDataFromCalendar.RevertFunc = revertFunc;
			EventDataFromCalendar.IsAllDay = event.allDay;
			EventDataFromCalendar.IsNew = false;
			EventDataFromCalendar.ID = event.id;
			EventDataFromCalendar.Title = event.title;
			EventDataFromCalendar.Start = event.start;
			if(event.end == null) {
				if(EventDataFromCalendar.IsAllDay) {
					EventDataFromCalendar.End = event.start.clone().add(1, 'days');
				} else {
					EventDataFromCalendar.End = event.start.clone().add(15, 'minutes');
				}
			} else {
				EventDataFromCalendar.End = event.end;
			}
			EventDataFromCalendar.AppEventType = event.appEventType;
			EventDataFromCalendar.GroupID = event.groupID;
			EventDataFromCalendar.RepeatInterval = event.repeatInterval;
			EventDataFromCalendar.RepeatTimes = event.repeatTimes;
			EventDataFromCalendar.ReminderTime = event.reminderTime;
			LogUserInteraction('eventDrop', EventDataFromCalendar.AppEventType);
			OpenDialog();
		});
	}
	//******************************************************************************
	function CreateHTMLDialogElement() {
		$('#AppCalendar-Dialog').append($('<div>').addClass('ui-dialog ui-widget ui-widget-content ui-corner-all'));
		$('#AppCalendar-Dialog').append($('<div>').addClass('AppCalendar-Dialog-Load'));
		ViewOrHideDialogLoadElement(false);
		
		$('#AppCalendar-Dialog .ui-dialog').append($('<div>').addClass('ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix'));
		$('#AppCalendar-Dialog .ui-dialog').append($('<div>').addClass('ui-dialog-content ui-widget-content'));
		$('#AppCalendar-Dialog .ui-dialog').append($('<div>').addClass('ui-dialog-buttonpane ui-widget-content ui-helper-clearfix'));
		
		$('#AppCalendar-Dialog .ui-dialog-titlebar').append($('<span>').addClass('ui-dialog-title').html('&nbsp;'));
		
		$('#AppCalendar-Dialog .ui-dialog-content').append($('<div>').attr({'id':AppObjectParams.ObjectsID.DialogContent}));
		
		$('#AppCalendar-Dialog .ui-dialog-content').append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column4-1'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column4-2'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column4-3'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column4-4'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last .AppCalendar-dialog-column4-1').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.StartDate));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last .AppCalendar-dialog-column4-1').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogDateStart, 'value':''}).prop("readonly", true).addClass('ui-widget-content ui-corner-all'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last .AppCalendar-dialog-column4-2').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.StartTime));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last .AppCalendar-dialog-column4-2').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTimeStart}));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last .AppCalendar-dialog-column4-3').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.EndDate));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last .AppCalendar-dialog-column4-3').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogDateEnd, 'value':''}).prop("readonly", true).addClass('ui-widget-content ui-corner-all'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last .AppCalendar-dialog-column4-4').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.EndTime));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last .AppCalendar-dialog-column4-4').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTimeEnd}));
				
		$('#AppCalendar-Dialog .ui-dialog-content').append($('<div>').addClass('AppCalendar-dialog-row dialog-times'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column3-1'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column3-2'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column3-3'));
		$('#AppCalendar-Dialog .AppCalendar-dialog-column3-1:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.RepeatInterval));
		$('#AppCalendar-Dialog .AppCalendar-dialog-column3-1:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogRepeatInterval}));
		$('#AppCalendar-Dialog .AppCalendar-dialog-column3-2:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.RepeatTimes));
		$('#AppCalendar-Dialog .AppCalendar-dialog-column3-2:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogRepeatTimes}));
		$('#AppCalendar-Dialog .AppCalendar-dialog-column3-3:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.ReminderTime));
		$('#AppCalendar-Dialog .AppCalendar-dialog-column3-3:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogReminderTime}));
		$('#AppCalendar-Dialog .dialog-times').hide();
		$.each(AppConfigData.RepeatInterval, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogRepeatInterval).append($('<option>').attr({'value':key}).text(val));
	    });
		$.each(AppConfigData.RepeatTimes, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogRepeatTimes).append($('<option>').attr({'value':key}).text(val));
	    });
		$.each(AppConfigData.ReminderTime, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogReminderTime).append($('<option>').attr({'value':key}).text(val));
	    });
				
		$('#AppCalendar-Dialog .ui-dialog-buttonpane').append($('<div>').addClass('ui-dialog-buttonset'));
		$('#AppCalendar-Dialog .ui-dialog-buttonpane .ui-dialog-buttonset').append($('<button>').attr({'id':AppObjectParams.ObjectsID.ButtonAdd, 'type':'button'}).addClass('ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only').css({'display':'none'}));
		$('#'+AppObjectParams.ObjectsID.ButtonAdd).append($('<span>').addClass('ui-button-text').text(AppObjectTextsData.DialogObj.Buttons.Add));
		$('#AppCalendar-Dialog .ui-dialog-buttonpane .ui-dialog-buttonset').append($('<button>').attr({'id':AppObjectParams.ObjectsID.ButtonEdit, 'type':'button'}).addClass('ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only').css({'display':'none'}));
		$('#'+AppObjectParams.ObjectsID.ButtonEdit).append($('<span>').addClass('ui-button-text').text(AppObjectTextsData.DialogObj.Buttons.Edit));
		$('#AppCalendar-Dialog .ui-dialog-buttonpane .ui-dialog-buttonset').append($('<button>').attr({'id':AppObjectParams.ObjectsID.ButtonDelete, 'type':'button'}).addClass('ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only').css({'display':'none'}));
		$('#'+AppObjectParams.ObjectsID.ButtonDelete).append($('<span>').addClass('ui-button-text').text(AppObjectTextsData.DialogObj.Buttons.Delete));
		$('#AppCalendar-Dialog .ui-dialog-buttonpane .ui-dialog-buttonset').append($('<button>').attr({'id':AppObjectParams.ObjectsID.ButtonDeleteAll, 'type':'button'}).addClass('ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only').css({'display':'none'}));
		$('#'+AppObjectParams.ObjectsID.ButtonDeleteAll).append($('<span>').addClass('ui-button-text').text(AppObjectTextsData.DialogObj.Buttons.DeleteAll));
		$('#AppCalendar-Dialog .ui-dialog-buttonpane .ui-dialog-buttonset').append($('<button>').attr({'id':AppObjectParams.ObjectsID.ButtonCancel, 'type':'button'}).addClass('ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'));
		$('#'+AppObjectParams.ObjectsID.ButtonCancel).append($('<span>').addClass('ui-button-text').text(AppObjectTextsData.DialogObj.Buttons.Cancel));
		
		$('#AppCalendar-Dialog').draggable({containment:'parent', scroll:false});
		$('#AppCalendar-Dialog .ui-dialog-titlebar').addTouch();
		
		$('#'+AppObjectParams.ObjectsID.DialogTimeStart).change(function() {
			ValidateStartEndTimes(0);
		});
		$('#'+AppObjectParams.ObjectsID.DialogTimeEnd).change(function() {
			ValidateStartEndTimes(1);
		});
		
		$('#'+AppObjectParams.ObjectsID.ButtonAdd).click(function() {
			if(CheckDialogParamsBeforeSend()) {
				LogUserInteraction('buttonAdd', EventDataFromCalendar.AppEventType);
				ViewOrHideDialogLoadElement(true);
				AddNewCalendarData();
			}
		});
		$('#'+AppObjectParams.ObjectsID.ButtonEdit).click(function() {
			if(CheckDialogParamsBeforeSend()) {
				LogUserInteraction('buttonEdit', EventDataFromCalendar.AppEventType);
				ViewOrHideDialogLoadElement(true);
				DeleteCalendarData(false, true);
			}
		});
		$('#'+AppObjectParams.ObjectsID.ButtonDelete).click(function() {
			LogUserInteraction('buttonDelete', EventDataFromCalendar.AppEventType);
			ViewOrHideDialogLoadElement(true);
			DeleteCalendarData(false, false);
		});
		$('#'+AppObjectParams.ObjectsID.ButtonDeleteAll).click(function() {
			LogUserInteraction('buttonDeleteAll', EventDataFromCalendar.AppEventType);
			ViewOrHideDialogLoadElement(true);
			DeleteCalendarData(true, false);
		});
		$('#'+AppObjectParams.ObjectsID.ButtonCancel).click(function() {
			if(EventDataFromCalendar.IsRevertFunc) {
				EventDataFromCalendar.RevertFunc();
			}
			if(EventDataFromCalendar.IsEventReceive) {
				$('#AppCalendar-Calendar').fullCalendar('removeEvents', EventDataFromCalendar.EventReceiveId);
			} else {
				$('#AppCalendar-Calendar').fullCalendar('unselect');
			}
			if(EventDataFromCalendar.IsNew) {
				LogUserInteraction('buttonCancelAdd', EventDataFromCalendar.AppEventType);
			} else {
				LogUserInteraction('buttonCancelEdit', EventDataFromCalendar.AppEventType);
			}
			CloseDialog();
		});
	}
	//==============================================================================
	// OPEN CLOSE DIALOG
	//==============================================================================
	function OpenDialog() {	
		$('#'+AppObjectParams.ObjectsID.DialogContent).empty();
		
		if(EventDataFromCalendar.IsNew) {
			$('#AppCalendar-Dialog .ui-dialog-title').text(AppObjectTextsData.DialogObj.Header.Add);
			$('#'+AppObjectParams.ObjectsID.ButtonAdd).css({'display':'inline'});
			$('#'+AppObjectParams.ObjectsID.ButtonEdit).css({'display':'none'});
			$('#'+AppObjectParams.ObjectsID.ButtonDelete).css({'display':'none'});
			$('#'+AppObjectParams.ObjectsID.ButtonDeleteAll).css({'display':'none'});
		} else {
			$('#AppCalendar-Dialog .ui-dialog-title').text(AppObjectTextsData.DialogObj.Header.Edit);
			$('#'+AppObjectParams.ObjectsID.ButtonAdd).css({'display':'none'});
			$('#'+AppObjectParams.ObjectsID.ButtonEdit).css({'display':'inline'});
			$('#'+AppObjectParams.ObjectsID.ButtonDelete).css({'display':'inline'});
			if(EventDataFromCalendar.AppEventType === "general") {
				$('#'+AppObjectParams.ObjectsID.ButtonDeleteAll).css({'display':'none'});
			} else {
				$('#'+AppObjectParams.ObjectsID.ButtonDeleteAll).css({'display':'inline'});
			}
		}
		
		var timeArrLen = AppConfigData.Time.Labels.length;
		var startMoment = EventDataFromCalendar.Start.clone(), endMoment = EventDataFromCalendar.End.clone(), 
		startMomentArr = startMoment.toArray(), endMomentArr = endMoment.toArray();
		var startTimeInx = 0, endTimeInx = 0;
		
		startMomentArr[4] = 15 * parseInt(startMomentArr[4] / 15);
		startMoment = moment.utc(startMomentArr);
		startTimeInx = $.inArray(startMoment.format('HH:mm'), AppConfigData.Time.Labels);
		if(startTimeInx < 0) {
			startTimeInx = 0;
			startMomentArr[3] = 0;
			startMomentArr[4] = 0;
			startMoment = moment.utc(startMomentArr);
		}
		EventDataFromCalendar.StartArr = startMomentArr;
		EventDataFromCalendar.StartTimeInx = startTimeInx;
		
		if(endMomentArr[3] == 0 && endMomentArr[4] == 0) {
			endMoment = moment.utc(endMomentArr).add(-1, 'minutes');
			endMomentArr = endMoment.toArray();
			endTimeInx = timeArrLen - 1;
		} else {
			endMomentArr[4] = 15 * parseInt(endMomentArr[4] / 15);
			endMoment = moment.utc(endMomentArr);
			endTimeInx = $.inArray(endMoment.format('HH:mm'), AppConfigData.Time.Labels);
			if(endTimeInx <= 0) { 
				endTimeInx = 1;
				endMomentArr[3] = 0;
				endMomentArr[4] = 1;
				endMoment = moment.utc(endMomentArr);
			}
		}
		EventDataFromCalendar.EndArr = endMomentArr;
		EventDataFromCalendar.EndTimeInx = endTimeInx;
		
		$('#'+AppObjectParams.ObjectsID.DialogDateStart).val(startMoment.format('DD MMMM YYYY'));
	    $('#'+AppObjectParams.ObjectsID.DialogDateEnd).val(endMoment.format('DD MMMM YYYY'));
	    
	    $('#'+AppObjectParams.ObjectsID.DialogTimeStart).empty();
	    $('#'+AppObjectParams.ObjectsID.DialogTimeEnd).empty();
	    
	    if(EventDataFromCalendar.IsAllDay) {
	    	$('#'+AppObjectParams.ObjectsID.DialogTimeStart).append($('<option>').attr({'value':startTimeInx}).text(AppConfigData.Time.Labels[startTimeInx]));
	    	$('#'+AppObjectParams.ObjectsID.DialogTimeEnd).append($('<option>').attr({'value':endTimeInx}).text(AppConfigData.Time.Labels[endTimeInx]));
	    } else {
	    	$.each(AppConfigData.Time.Labels, function(time_index, time_label) {
		    	if((time_index + 1) < timeArrLen) {
		    		$('#'+AppObjectParams.ObjectsID.DialogTimeStart).append($('<option>').attr({'value':time_index}).text(time_label));
		    	}
		    	$('#'+AppObjectParams.ObjectsID.DialogTimeEnd).append($('<option>').attr({'value':time_index}).text(time_label));
		    });
	    }
	    
	    $('#'+AppObjectParams.ObjectsID.DialogTimeStart).val(startTimeInx);
	    $('#'+AppObjectParams.ObjectsID.DialogTimeEnd).val(endTimeInx);
	    
		if(AppConfigData.RepeatInterval.hasOwnProperty(EventDataFromCalendar.RepeatInterval)) {
			$('#'+AppObjectParams.ObjectsID.DialogRepeatInterval).val(EventDataFromCalendar.RepeatInterval);
		}
		if(AppConfigData.RepeatTimes.hasOwnProperty(EventDataFromCalendar.RepeatTimes)) {
			$('#'+AppObjectParams.ObjectsID.DialogRepeatTimes).val(EventDataFromCalendar.RepeatTimes);
		}
		if(AppConfigData.ReminderTime.hasOwnProperty(EventDataFromCalendar.ReminderTime)) {
			$('#'+AppObjectParams.ObjectsID.DialogReminderTime).val(EventDataFromCalendar.ReminderTime);
		}
		
		switch(EventDataFromCalendar.AppEventType) {
			case "general":
				OpenDialogGeneral();
			break;
			case "medical":
				OpenDialogMedical();
			break;
			case "measurements":
				OpenDialogMeasurements();
			break;
			case "eating":
				OpenDialogEating();
			break;
			case "physical":
				OpenDialogPhysical();
			break;
			case "cognitive":
				OpenDialogCognitive();
			break;
			case "shower":
				OpenDialogShower();
			break;
			case "socializing":
				OpenDialogSocializing();
			break;
		}
		
		ViewOrHideOverlayElement(true);
	}
	//******************************************************************************
	function OpenDialogGeneral() {
		$('#AppCalendar-Dialog .dialog-times').hide();
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Title));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogTitle, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).val(EventDataFromCalendar.Title);
		
	    $('#AppCalendar-Dialog .ui-dialog').css({'width':970, 'height':320});
		$('#AppCalendar-Dialog').css({'display':'block', 'z-index':16, 'width':980, 'height':330, 'top':230, 'left':117, 'position':'relative'});
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
	}
	//******************************************************************************
	function OpenDialogMedical() {
		var localEventTitle = AppObjectTextsData.DialogObj.Labels.TitleMedicalVal, 
		localEventLocation = 'OUTDOORS', localMedExam = false, localInsCard = false, 
		localOtherDocs = '', localTransportation = 'FOOT', localTransportationStr = '', 
		localDoctorName = '', localDoctorPhone = '';
		
		if(!EventDataFromCalendar.IsNew) {
			localEventTitle = EventDataFromCalendar.Title;
			if(ReadyExtCalendarData.Data.medical.hasOwnProperty(EventDataFromCalendar.ID)) {
				localEventLocation = ReadyExtCalendarData.Data.medical[EventDataFromCalendar.ID].location;
				if(!AppConfigData.Location.hasOwnProperty(localEventLocation)) {
					localEventLocation = 'OUTDOORS';
				}
				localMedExam = ReadyExtCalendarData.Data.medical[EventDataFromCalendar.ID].medExam;
				localInsCard = ReadyExtCalendarData.Data.medical[EventDataFromCalendar.ID].insCard;
				localOtherDocs = ReadyExtCalendarData.Data.medical[EventDataFromCalendar.ID].otherDocs;
				localTransportation = ReadyExtCalendarData.Data.medical[EventDataFromCalendar.ID].transportation;
				if(!AppConfigData.Transportation.hasOwnProperty(localTransportation)) {
					localTransportationStr = localTransportation;
					localTransportation = 'OTHER';
				}
				localDoctorName = ReadyExtCalendarData.Data.medical[EventDataFromCalendar.ID].doctorName;
				localDoctorPhone = ReadyExtCalendarData.Data.medical[EventDataFromCalendar.ID].doctorPhone;
			}
		}
		
		$('#AppCalendar-Dialog .dialog-times').show();
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Title));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogTitle, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Location));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogLocation}));
		$.each(AppConfigData.Location, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogLocation).append($('<option>').attr({'value':key}).text(val));
	    });
				
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row-single-30 dialog-outdoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-30:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Question1));
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row-single-60 dialog-outdoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column3-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column3-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column3-3'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-1:last').append($('<input>').attr({'type':'checkbox', 'id':AppObjectParams.ObjectsID.DialogMedExam}));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-1:last').append($('<small>').text(AppObjectTextsData.DialogObj.Labels.MedExam));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-2:last').append($('<input>').attr({'type':'checkbox', 'id':AppObjectParams.ObjectsID.DialogInsCard}));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-2:last').append($('<small>').text(AppObjectTextsData.DialogObj.Labels.InsCard));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-3:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogOtherDocs, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogOtherDocs).attr({'placeholder':AppObjectTextsData.DialogObj.Labels.OtherDocs});
				
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row-single-30 dialog-outdoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-30:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Question2));
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row-single-60 dialog-outdoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column2-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column2-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column2-1:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTransportation}));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column2-2:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogTransportationStr, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).attr({'placeholder':AppConfigData.Transportation.OTHER});
		$.each(AppConfigData.Transportation, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogTransportation).append($('<option>').attr({'value':key}).text(val));
	    });
				
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column2-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column2-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column2-1:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.DoctorName));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column2-1:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogDoctorName, 'value':''}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column2-2:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.DoctorPhone));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column2-2:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogDoctorPhone, 'value':''}).addClass('text ui-corner-all'));
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).val(localEventTitle);
		$('#'+AppObjectParams.ObjectsID.DialogLocation).val(localEventLocation);
		
		if(localMedExam) {
			$('#'+AppObjectParams.ObjectsID.DialogMedExam).prop('checked', true);
		} else {
			$('#'+AppObjectParams.ObjectsID.DialogMedExam).removeProp('checked');
		}
		if(localInsCard) {
			$('#'+AppObjectParams.ObjectsID.DialogInsCard).prop('checked', true);
		} else {
			$('#'+AppObjectParams.ObjectsID.DialogInsCard).removeProp('checked');
		}
		
		$('#'+AppObjectParams.ObjectsID.DialogOtherDocs).val(localOtherDocs);
		$('#'+AppObjectParams.ObjectsID.DialogTransportation).val(localTransportation);
		$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).val(localTransportationStr);
		$('#'+AppObjectParams.ObjectsID.DialogDoctorName).val(localDoctorName);
		$('#'+AppObjectParams.ObjectsID.DialogDoctorPhone).val(localDoctorPhone);
		
		ViewOrHideLocationElements();
		$('#'+AppObjectParams.ObjectsID.DialogLocation).change(function() {
			ViewOrHideLocationElements();
		});
		
		$('#AppCalendar-Dialog .ui-dialog').css({'width':970, 'height':680});
		$('#AppCalendar-Dialog').css({'display':'block', 'z-index':16, 'width':980, 'height':690, 'top':126, 'left':117, 'position':'relative'});
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
	}
	//******************************************************************************
	function OpenDialogPhysical() {
		var localEventTitle = AppObjectTextsData.DialogObj.Labels.TitlePhysicalVal, 
		localEventLocation = 'INDOORS', localEventTrainer = 'VIDEO';
		
		if(!EventDataFromCalendar.IsNew) {
			localEventTitle = EventDataFromCalendar.Title;
			if(ReadyExtCalendarData.Data.physical.hasOwnProperty(EventDataFromCalendar.ID)) {
				localEventLocation = ReadyExtCalendarData.Data.physical[EventDataFromCalendar.ID].location;
				localEventTrainer = ReadyExtCalendarData.Data.physical[EventDataFromCalendar.ID].trainer;
				if(!AppConfigData.Location.hasOwnProperty(localEventLocation)) {
					localEventLocation = 'INDOORS';
				}
				if(!AppConfigData.TrainerPhysical.hasOwnProperty(localEventTrainer)) {
					localEventTrainer = 'VIDEO';
				}
			}
		}
		
		$('#AppCalendar-Dialog .dialog-times').show();
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Title));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogTitle, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Location));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogLocation}));
		$.each(AppConfigData.Location, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogLocation).append($('<option>').attr({'value':key}).text(val));
	    });
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row dialog-indoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Question3));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTrainer}));
		$.each(AppConfigData.TrainerPhysical, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogTrainer).append($('<option>').attr({'value':key}).text(val));
	    });
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).val(localEventTitle);
		$('#'+AppObjectParams.ObjectsID.DialogLocation).val(localEventLocation);
		$('#'+AppObjectParams.ObjectsID.DialogTrainer).val(localEventTrainer);
		
		ViewOrHideLocationElements();
		$('#'+AppObjectParams.ObjectsID.DialogLocation).change(function() {
			ViewOrHideLocationElements();
		});
		
		$('#AppCalendar-Dialog .ui-dialog').css({'width':970, 'height':500});
		$('#AppCalendar-Dialog').css({'display':'block', 'z-index':16, 'width':980, 'height':510, 'top':126, 'left':117, 'position':'relative'});
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
	}
	//******************************************************************************
	function OpenDialogCognitive() {
		var localEventTitle = AppObjectTextsData.DialogObj.Labels.TitleCognitiveVal, 
		localEventLocation = 'INDOORS', localEventTrainer = 'GAMES';
		
		if(!EventDataFromCalendar.IsNew) {
			localEventTitle = EventDataFromCalendar.Title;
			if(ReadyExtCalendarData.Data.cognitive.hasOwnProperty(EventDataFromCalendar.ID)) {
				localEventLocation = ReadyExtCalendarData.Data.cognitive[EventDataFromCalendar.ID].location;
				localEventTrainer = ReadyExtCalendarData.Data.cognitive[EventDataFromCalendar.ID].trainer;
				if(!AppConfigData.Location.hasOwnProperty(localEventLocation)) {
					localEventLocation = 'INDOORS';
				}
				if(!AppConfigData.TrainerCognitive.hasOwnProperty(localEventTrainer)) {
					localEventTrainer = 'GAMES';
				}
			}
		}
		
		$('#AppCalendar-Dialog .dialog-times').show();
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Title));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogTitle, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Location));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogLocation}));
		$.each(AppConfigData.Location, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogLocation).append($('<option>').attr({'value':key}).text(val));
	    });
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row dialog-indoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Question4));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTrainer}));
		$.each(AppConfigData.TrainerCognitive, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogTrainer).append($('<option>').attr({'value':key}).text(val));
	    });
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).val(localEventTitle);
		$('#'+AppObjectParams.ObjectsID.DialogLocation).val(localEventLocation);
		$('#'+AppObjectParams.ObjectsID.DialogTrainer).val(localEventTrainer);
		
		ViewOrHideLocationElements();
		$('#'+AppObjectParams.ObjectsID.DialogLocation).change(function() {
			ViewOrHideLocationElements();
		});
		
		$('#AppCalendar-Dialog .ui-dialog').css({'width':970, 'height':500});
		$('#AppCalendar-Dialog').css({'display':'block', 'z-index':16, 'width':980, 'height':510, 'top':126, 'left':117, 'position':'relative'});
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
	}
	//******************************************************************************
	function OpenDialogShower() {
		var localEventTitle = 'SHOWER';
		if(!EventDataFromCalendar.IsNew) {
			localEventTitle = EventDataFromCalendar.Title;
			if(ReadyExtCalendarData.Data.shower.hasOwnProperty(EventDataFromCalendar.ID)) {
				localEventTitle = ReadyExtCalendarData.Data.shower[EventDataFromCalendar.ID].type;
			}
			if(!AppConfigData.TitleShower.hasOwnProperty(localEventTitle)) {
				localEventTitle = 'SHOWER';
			}
		}
		
		$('#AppCalendar-Dialog .dialog-times').show();
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Title));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTitle}));
		$.each(AppConfigData.TitleShower, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogTitle).append($('<option>').attr({'value':key}).text(val));
	    });
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).val(localEventTitle);
		
		$('#AppCalendar-Dialog .ui-dialog').css({'width':970, 'height':410});
		$('#AppCalendar-Dialog').css({'display':'block', 'z-index':16, 'width':980, 'height':420, 'top':126, 'left':117, 'position':'relative'});
	}
	//******************************************************************************
	function OpenDialogMeasurements() {
		var localEventTitle = 'BLOODPRESSURE';
		if(!EventDataFromCalendar.IsNew) {
			localEventTitle = EventDataFromCalendar.Title;
			if(ReadyExtCalendarData.Data.measurements.hasOwnProperty(EventDataFromCalendar.ID)) {
				localEventTitle = ReadyExtCalendarData.Data.measurements[EventDataFromCalendar.ID].type;
			}
			if(!AppConfigData.TitleMeasurements.hasOwnProperty(localEventTitle)) {
				localEventTitle = 'BLOODPRESSURE';
			}
		}
		
		$('#AppCalendar-Dialog .dialog-times').show();
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Title));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTitle}));
		$.each(AppConfigData.TitleMeasurements, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogTitle).append($('<option>').attr({'value':key}).text(val));
	    });
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).val(localEventTitle);
		
		$('#AppCalendar-Dialog .ui-dialog').css({'width':970, 'height':410});
		$('#AppCalendar-Dialog').css({'display':'block', 'z-index':16, 'width':980, 'height':420, 'top':126, 'left':117, 'position':'relative'});
	}
	//******************************************************************************
	function OpenDialogEating() {
		var localEventTitle = 'BREAKFAST', localEventLocation = 'INDOORS';
		if(!EventDataFromCalendar.IsNew) {
			localEventTitle = EventDataFromCalendar.Title;
			if(ReadyExtCalendarData.Data.eating.hasOwnProperty(EventDataFromCalendar.ID)) {
				localEventTitle = ReadyExtCalendarData.Data.eating[EventDataFromCalendar.ID].type;
				localEventLocation = ReadyExtCalendarData.Data.eating[EventDataFromCalendar.ID].location;
			}
			if(!AppConfigData.TitleEating.hasOwnProperty(localEventTitle)) {
				localEventTitle = 'BREAKFAST';
			}
			if(!AppConfigData.Location.hasOwnProperty(localEventLocation)) {
				localEventLocation = 'INDOORS';
			}
		}
		
		$('#AppCalendar-Dialog .dialog-times').show();
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Title));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTitle}));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Location));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogLocation}));
		$.each(AppConfigData.TitleEating, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogTitle).append($('<option>').attr({'value':key}).text(val));
	    });
		$.each(AppConfigData.Location, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogLocation).append($('<option>').attr({'value':key}).text(val));
	    });
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).val(localEventTitle);
		$('#'+AppObjectParams.ObjectsID.DialogLocation).val(localEventLocation);
		
		$('#AppCalendar-Dialog .ui-dialog').css({'width':970, 'height':410});
		$('#AppCalendar-Dialog').css({'display':'block', 'z-index':16, 'width':980, 'height':420, 'top':126, 'left':117, 'position':'relative'});
	}
	//******************************************************************************
	function OpenDialogSocializing() {
		var localEventTitle = '', localEventLocation = 'OUTDOORS', localWallet = false, localKeys = false, 
		localOtherBelongings = '', localTransportation = 'FOOT', localTransportationStr = '';
		
		if(!EventDataFromCalendar.IsNew) {
			localEventTitle = EventDataFromCalendar.Title;
			if(ReadyExtCalendarData.Data.socializing.hasOwnProperty(EventDataFromCalendar.ID)) {
				localEventLocation = ReadyExtCalendarData.Data.socializing[EventDataFromCalendar.ID].location;
				if(!AppConfigData.Location.hasOwnProperty(localEventLocation)) {
					localEventLocation = 'OUTDOORS';
				}
				localWallet = ReadyExtCalendarData.Data.socializing[EventDataFromCalendar.ID].wallet;
				localKeys = ReadyExtCalendarData.Data.socializing[EventDataFromCalendar.ID].keys;
				localOtherBelongings = ReadyExtCalendarData.Data.socializing[EventDataFromCalendar.ID].otherBelongings;
				localTransportation = ReadyExtCalendarData.Data.socializing[EventDataFromCalendar.ID].transportation;
				if(!AppConfigData.Transportation.hasOwnProperty(localTransportation)) {
					localTransportationStr = localTransportation;
					localTransportation = 'OTHER';
				}
			}
		}
		
		$('#AppCalendar-Dialog .dialog-times').show();
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row:last').append($('<div>').addClass('AppCalendar-dialog-column32-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Title));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-1:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogTitle, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Location));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column32-2:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogLocation}));
		$.each(AppConfigData.Location, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogLocation).append($('<option>').attr({'value':key}).text(val));
	    });
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row-single-30 dialog-outdoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-30:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Question1));
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row-single-60 dialog-outdoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column3-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column3-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column3-3'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-1:last').append($('<input>').attr({'type':'checkbox', 'id':AppObjectParams.ObjectsID.DialogWallet}));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-1:last').append($('<small>').text(AppObjectTextsData.DialogObj.Labels.Wallet));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-2:last').append($('<input>').attr({'type':'checkbox', 'id':AppObjectParams.ObjectsID.DialogKeys}));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-2:last').append($('<small>').text(AppObjectTextsData.DialogObj.Labels.Keys));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column3-3:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogOtherBelongings, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogOtherBelongings).attr({'placeholder':AppObjectTextsData.DialogObj.Labels.OtherBelongings});
		
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row-single-30 dialog-outdoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-30:last').append($('<label>').text(AppObjectTextsData.DialogObj.Labels.Question2));
		$('#'+AppObjectParams.ObjectsID.DialogContent).append($('<div>').addClass('AppCalendar-dialog-row-single-60 dialog-outdoors'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column2-1'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-row-single-60:last').append($('<div>').addClass('AppCalendar-dialog-column2-2'));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column2-1:last').append($('<select>').attr({'id':AppObjectParams.ObjectsID.DialogTransportation}));
		$('#'+AppObjectParams.ObjectsID.DialogContent+' .AppCalendar-dialog-column2-2:last').append($('<input>').attr({'type':'text', 'id':AppObjectParams.ObjectsID.DialogTransportationStr, 'value':'', 'maxlength':100}).addClass('text ui-corner-all'));
		$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).attr({'placeholder':AppConfigData.Transportation.OTHER});
		$.each(AppConfigData.Transportation, function(key, val) {
			$('#'+AppObjectParams.ObjectsID.DialogTransportation).append($('<option>').attr({'value':key}).text(val));
	    });
				
		$('#'+AppObjectParams.ObjectsID.DialogTitle).val(localEventTitle);
		$('#'+AppObjectParams.ObjectsID.DialogLocation).val(localEventLocation);
		
		if(localWallet) {
			$('#'+AppObjectParams.ObjectsID.DialogWallet).prop('checked', true);
		} else {
			$('#'+AppObjectParams.ObjectsID.DialogWallet).removeProp('checked');
		}
		if(localKeys) {
			$('#'+AppObjectParams.ObjectsID.DialogKeys).prop('checked', true);
		} else {
			$('#'+AppObjectParams.ObjectsID.DialogKeys).removeProp('checked');
		}
		
		$('#'+AppObjectParams.ObjectsID.DialogOtherBelongings).val(localOtherBelongings);
		$('#'+AppObjectParams.ObjectsID.DialogTransportation).val(localTransportation);
		$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).val(localTransportationStr);
		
		ViewOrHideLocationElements();
		$('#'+AppObjectParams.ObjectsID.DialogLocation).change(function() {
			ViewOrHideLocationElements();
		});
		
		$('#AppCalendar-Dialog .ui-dialog').css({'width':970, 'height':590});
		$('#AppCalendar-Dialog').css({'display':'block', 'z-index':16, 'width':980, 'height':600, 'top':126, 'left':117, 'position':'relative'});
		
		$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
	}
	//******************************************************************************
	function CloseDialog() {
		InitEventDataFromCalendar();
		EventDataForSend = {};
		
		$('#AppCalendar-Dialog').css({'display':'none', 'z-index':10});
		ViewOrHideDialogLoadElement(false);
		ViewOrHideOverlayElement(false);
	}
	//==============================================================================
	//GET EXTERNAL DATA
	//==============================================================================
	function GetExternalCalendarDataAll() {
		ViewOrHideOverlayElement(true);
		ViewOrHideLoadElement(true);
		
		InitReadyExtCalendarData();
		CalendarEvents = [];
		
		GetExternalCalendarDataGeneral(false, true);
	}
	//******************************************************************************
	function GetExternalCalendarDataGeneral(FlagSingleReq, FlagReqNext) {
		if(FlagSingleReq) {
			ViewOrHideOverlayElement(true);
			ViewOrHideLoadElement(true);
			
			CalendarEvents = [];
			InitPartReadyExtCalendarData('general');
		}
		
		var url_calendar_data_general = PathToSB_Calendar+"/task";
		var ReqCalendarDataGeneral = ewallApp.ajax({
			url: url_calendar_data_general,
			type: "GET", 
			data: {username: UserName}, 
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqCalendarDataGeneral).done(function (ReceiveCalendarDataGeneral) {
			PrepareExternalCalendarData(ReceiveCalendarDataGeneral, 'general');
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			} 
			if(FlagReqNext) {
				GetExternalCalendarDataMedical(FlagSingleReq, FlagReqNext);
			}
		});
		$.when(ReqCalendarDataGeneral).fail(function () {
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			} 
			if(FlagReqNext) {
				GetExternalCalendarDataMedical(FlagSingleReq, FlagReqNext);
			}
		});
	}
	//******************************************************************************
	function GetExternalCalendarDataMedical(FlagSingleReq, FlagReqNext) {
		if(FlagSingleReq) {
			ViewOrHideOverlayElement(true);
			ViewOrHideLoadElement(true);
			
			CalendarEvents = [];
			InitPartReadyExtCalendarData('medical');
		}
		
		var url_calendar_data_medical = PathToSB_Calendar+"/medicalCheckup";
		var ReqCalendarDataMedical = ewallApp.ajax({
			url: url_calendar_data_medical,
			type: "GET", 
			data: {username: UserName},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqCalendarDataMedical).done(function (ReceiveCalendarDataMedical) {
			PrepareExternalCalendarData(ReceiveCalendarDataMedical, 'medical');
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			} 
			if(FlagReqNext) {
				GetExternalCalendarDataExercises(FlagSingleReq, FlagReqNext);
			}
		});
		$.when(ReqCalendarDataMedical).fail(function () {
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			} 
			if(FlagReqNext) {
				GetExternalCalendarDataExercises(FlagSingleReq, FlagReqNext);
			}
		});
	}
	//******************************************************************************
	function GetExternalCalendarDataExercises(FlagSingleReq, FlagReqNext) {
		if(FlagSingleReq) {
			ViewOrHideOverlayElement(true);
			ViewOrHideLoadElement(true);
			
			CalendarEvents = [];
			InitPartReadyExtCalendarData('physical');
			InitPartReadyExtCalendarData('cognitive');
		}
		
		var url_calendar_data_exercises = PathToSB_Calendar+"/exercises";
		var ReqCalendarDataExercises = ewallApp.ajax({
			url: url_calendar_data_exercises,
			type: "GET", 
			data: {username: UserName},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqCalendarDataExercises).done(function (ReceiveCalendarDataExercises) {
			PrepareExternalCalendarData(ReceiveCalendarDataExercises, ['physical', 'cognitive']);
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			}
			if(FlagReqNext) {
				GetExternalCalendarDataSelfCare(FlagSingleReq, FlagReqNext);
			}
		});
		$.when(ReqCalendarDataExercises).fail(function () {
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			}
			if(FlagReqNext) {
				GetExternalCalendarDataSelfCare(FlagSingleReq, FlagReqNext);
			}
		});
	}
	//******************************************************************************
	function GetExternalCalendarDataSelfCare(FlagSingleReq, FlagReqNext) {
		if(FlagSingleReq) {
			ViewOrHideOverlayElement(true);
			ViewOrHideLoadElement(true);
			
			CalendarEvents = [];
			InitPartReadyExtCalendarData('measurements');
			InitPartReadyExtCalendarData('eating');
			InitPartReadyExtCalendarData('shower');
		}
		
		var url_calendar_data_selfcare = PathToSB_Calendar+"/selfcare";
		var ReqCalendarDataSelfCare = ewallApp.ajax({
			url: url_calendar_data_selfcare,
			type: "GET", 
			data: {username: UserName},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqCalendarDataSelfCare).done(function (ReceiveCalendarDataSelfCare) {
			PrepareExternalCalendarData(ReceiveCalendarDataSelfCare, ['measurements', 'eating', 'shower']);
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			}
			if(FlagReqNext) {
				GetExternalCalendarDataSocializing(FlagSingleReq, false);
			}
		});
		$.when(ReqCalendarDataSelfCare).fail(function () {
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			}
			if(FlagReqNext) {
				GetExternalCalendarDataSocializing(FlagSingleReq, false);
			}
		});
	}
	//******************************************************************************
	function GetExternalCalendarDataSocializing(FlagSingleReq, FlagReqNext) {
		if(FlagSingleReq) {
			ViewOrHideOverlayElement(true);
			ViewOrHideLoadElement(true);
			
			CalendarEvents = [];
			InitPartReadyExtCalendarData('socializing');
		}
		
		var url_calendar_data_socializing = PathToSB_Calendar+"/socializing";
		var ReqCalendarDataSocializing = ewallApp.ajax({
			url: url_calendar_data_socializing,
			type: "GET", 
			data: {username: UserName},
			dataType: "json", 
			async: true,
			cache: true
		});
		
		$.when(ReqCalendarDataSocializing).done(function (ReceiveCalendarDataSocializing) {
			PrepareExternalCalendarData(ReceiveCalendarDataSocializing, 'socializing');
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			}
		});
		$.when(ReqCalendarDataSocializing).fail(function () {
			if(FlagSingleReq || !FlagReqNext) {
				CalendarEvents = CheckAndCorrectCalendarEventsParams();
				$('#AppCalendar-Calendar').fullCalendar('removeEvents');
				$('#AppCalendar-Calendar').fullCalendar('addEventSource', CalendarEvents);
				$('#AppCalendar-Calendar').fullCalendar('rerenderEvents');
				ViewOrHideOverlayElement(false);
				ViewOrHideLoadElement(false);
			}
		});
	}
	//==============================================================================
	//POST AND DELETE DATA
	//==============================================================================
	function AddNewCalendarData() {
		var url_calendar_send_data = "", localAppEventType = EventDataFromCalendar.AppEventType;
		
		switch(localAppEventType) {
			case "general":
				url_calendar_send_data = PathToSB_Calendar+"/task";
			break;
			
			case "medical":
				url_calendar_send_data = PathToSB_Calendar+"/medicalCheckup";
			break;
			
			case "physical":
			case "cognitive":
				url_calendar_send_data = PathToSB_Calendar+"/exercises";
			break;
			
			case "shower":
			case "measurements":
			case "eating":
				url_calendar_send_data = PathToSB_Calendar+"/selfcare";
			break;
						
			case "socializing":
				url_calendar_send_data = PathToSB_Calendar+"/socializing";
			break; 
		}
		
		if(url_calendar_send_data.length <= 0) {
			if(EventDataFromCalendar.IsRevertFunc) {
				EventDataFromCalendar.RevertFunc();
			}
			if(EventDataFromCalendar.IsEventReceive) {
				$('#AppCalendar-Calendar').fullCalendar('removeEvents', EventDataFromCalendar.EventReceiveId);
			} else {
				$('#AppCalendar-Calendar').fullCalendar('unselect');
			}
			CloseDialog();
			return false;
		}
		
		var AddCalendarNewData = ewallApp.ajax({
			url: url_calendar_send_data,
			type: "POST", 
			data: JSON.stringify(EventDataForSend),
			contentType: "application/json", 
			async: true,
			cache: true
		});
		
		$.when(AddCalendarNewData).done(function () {
			CloseDialog();
			
			switch(localAppEventType) {
				case "general":
					GetExternalCalendarDataGeneral(true, false);
				break;
				
				case "medical":
					GetExternalCalendarDataMedical(true, false);
				break;
				
				case "physical":
				case "cognitive":
					GetExternalCalendarDataExercises(true, false);
				break;
				
				case "shower":
				case "measurements":
				case "eating":
					GetExternalCalendarDataSelfCare(true, false);
				break;
							
				case "socializing":
					GetExternalCalendarDataSocializing(true, false);
				break; 
				
				default:
					GetExternalCalendarDataGeneral(true, false);
			}
		});
		
		$.when(AddCalendarNewData).fail(function () {
			if(EventDataFromCalendar.IsRevertFunc) {
				EventDataFromCalendar.RevertFunc();
			}
			if(EventDataFromCalendar.IsEventReceive) {
				$('#AppCalendar-Calendar').fullCalendar('removeEvents', EventDataFromCalendar.EventReceiveId);
			} else {
				$('#AppCalendar-Calendar').fullCalendar('unselect');
			}
			CloseDialog();
		});
	}
	//******************************************************************************
	function DeleteCalendarData(FlagDeleteByGroupID, FlagCallInsert) {
		var url_calendar_delete_data = "", localAppEventType = EventDataFromCalendar.AppEventType, 
		localCalendarEventID = EventDataFromCalendar.ID, localCalendarEventGroupID = EventDataFromCalendar.GroupID;
		
		switch(localAppEventType) {
			case "general":
				url_calendar_delete_data = PathToSB_Calendar+"/task?id="+localCalendarEventID;
			break;
			
			case "medical":
				url_calendar_delete_data = PathToSB_Calendar+"/medicalCheckup";
				if(FlagDeleteByGroupID) {
					url_calendar_delete_data+= "?groupID="+localCalendarEventGroupID;
				} else {
					url_calendar_delete_data+= "?id="+localCalendarEventID;
				}
			break;
			
			case "physical":
			case "cognitive":
				url_calendar_delete_data = PathToSB_Calendar+"/exercises";
				if(FlagDeleteByGroupID) {
					url_calendar_delete_data+= "?groupID="+localCalendarEventGroupID;
				} else {
					url_calendar_delete_data+= "?id="+localCalendarEventID;
				}
			break;
			
			case "shower":
			case "measurements":
			case "eating":
				url_calendar_delete_data = PathToSB_Calendar+"/selfcare";
				if(FlagDeleteByGroupID) {
					url_calendar_delete_data+= "?groupID="+localCalendarEventGroupID;
				} else {
					url_calendar_delete_data+= "?id="+localCalendarEventID;
				}
			break;
						
			case "socializing":
				url_calendar_delete_data = PathToSB_Calendar+"/socializing";
				if(FlagDeleteByGroupID) {
					url_calendar_delete_data+= "?groupID="+localCalendarEventGroupID;
				} else {
					url_calendar_delete_data+= "?id="+localCalendarEventID;
				}
			break; 
		}
		
		if(url_calendar_delete_data.length <= 0) {
			if(EventDataFromCalendar.IsRevertFunc) {
				EventDataFromCalendar.RevertFunc();
			}
			$('#AppCalendar-Calendar').fullCalendar('unselect');
			CloseDialog();
			return false;
		}
		
		var DeleteCalendarData = ewallApp.ajax({
			url: url_calendar_delete_data,
			type: "DELETE", 
			async: true,
			cache: true
		});
		
		$.when(DeleteCalendarData).done(function () {
			if(FlagCallInsert) {
				AddNewCalendarData();
			} else {
				if(FlagDeleteByGroupID) {
					if(ReadyExtCalendarData.GroupIDs.hasOwnProperty(localAppEventType)) {
						if(ReadyExtCalendarData.GroupIDs[localAppEventType].hasOwnProperty(localCalendarEventGroupID)) {
							$.each(ReadyExtCalendarData.GroupIDs[localAppEventType][localCalendarEventGroupID], function(ind, temp_id) {
								$('#AppCalendar-Calendar').fullCalendar('removeEvents', temp_id);
								
								if(ReadyExtCalendarData.Data[localAppEventType].hasOwnProperty(temp_id)) {
									delete ReadyExtCalendarData.Data[localAppEventType][temp_id];
								}
							});
							$('#AppCalendar-Calendar').fullCalendar('unselect');
							delete ReadyExtCalendarData.GroupIDs[localAppEventType][localCalendarEventGroupID];
						}
					}
				} else {
					$('#AppCalendar-Calendar').fullCalendar('removeEvents', localCalendarEventID);
					$('#AppCalendar-Calendar').fullCalendar('unselect');
					
					if(ReadyExtCalendarData.Data.hasOwnProperty(localAppEventType)) {
						if(ReadyExtCalendarData.Data[localAppEventType].hasOwnProperty(localCalendarEventID)) {
							delete ReadyExtCalendarData.Data[localAppEventType][localCalendarEventID];
						}
						
						if(ReadyExtCalendarData.GroupIDs[localAppEventType].hasOwnProperty(localCalendarEventGroupID)) {
							var inx_event_id = $.inArray(localCalendarEventID, ReadyExtCalendarData.GroupIDs[localAppEventType][localCalendarEventGroupID]);
							if(inx_event_id >= 0) {
								ReadyExtCalendarData.GroupIDs[localAppEventType][localCalendarEventGroupID].splice(inx_event_id, 1);
							}
						}
					}
				}
				CloseDialog();
			}
		});
		
		$.when(DeleteCalendarData).fail(function () {
			if(EventDataFromCalendar.IsRevertFunc) {
				EventDataFromCalendar.RevertFunc();
			}
			$('#AppCalendar-Calendar').fullCalendar('unselect');
			CloseDialog();
		});
	}
	//==============================================================================
	//PREPARE EXTERNAL DATA
	//==============================================================================
	function PrepareExternalCalendarData(DataFromSB, EventType) {
		if($.isArray(EventType)) {
			var ExtEvtType = '', LocalEvtType = '';
			$.each(EventType, function(ind, val) {
				InitPartReadyExtCalendarData(val);
			});
			$.each(DataFromSB, function(inx, calendar_data) {
				if(calendar_data.hasOwnProperty('endDate') && calendar_data.hasOwnProperty('id') && calendar_data.id.length > 0) {
					if(calendar_data.hasOwnProperty('type')) {
						ExtEvtType = calendar_data.type;
						if(ExtEvtType.length > 0 && AppCalendarEventSupport.SB2AppTypes.hasOwnProperty(ExtEvtType)) {
							LocalEvtType = AppCalendarEventSupport.SB2AppTypes[ExtEvtType];
							if(LocalEvtType.length > 0 && ReadyExtCalendarData.Data.hasOwnProperty(LocalEvtType)) {
								ReadyExtCalendarData.Data[LocalEvtType][calendar_data.id] = calendar_data;
								if(calendar_data.hasOwnProperty('groupID')) {
									if(!ReadyExtCalendarData.GroupIDs[LocalEvtType].hasOwnProperty(calendar_data.groupID)) {
										ReadyExtCalendarData.GroupIDs[LocalEvtType][calendar_data.groupID] = [];
									}
									ReadyExtCalendarData.GroupIDs[LocalEvtType][calendar_data.groupID][ReadyExtCalendarData.GroupIDs[LocalEvtType][calendar_data.groupID].length] = calendar_data.id;
								}
							}
						}
					}
				}
		    });
		} else {
			InitPartReadyExtCalendarData(EventType);
			$.each(DataFromSB, function(inx, calendar_data) {
				if(calendar_data.hasOwnProperty('endDate') && calendar_data.hasOwnProperty('id') && calendar_data.id.length > 0) {
					ReadyExtCalendarData.Data[EventType][calendar_data.id] = calendar_data;
					if(calendar_data.hasOwnProperty('groupID')) {
						if(!ReadyExtCalendarData.GroupIDs[EventType].hasOwnProperty(calendar_data.groupID)) {
							ReadyExtCalendarData.GroupIDs[EventType][calendar_data.groupID] = [];
						}
						ReadyExtCalendarData.GroupIDs[EventType][calendar_data.groupID][ReadyExtCalendarData.GroupIDs[EventType][calendar_data.groupID].length] = calendar_data.id;
					}
				}
		    });
		}
	}
	//******************************************************************************
	function CheckAndCorrectCalendarEventsParams() {
		var CalendarItems = [];
		$.each(ReadyExtCalendarData.Data, function(calendarEventType, calendarEventData) {
			$.each(calendarEventData, function(inx, calendar_data) {
				var EventItem = {};
				if(calendar_data.hasOwnProperty('endDate') && calendar_data.hasOwnProperty('id') && calendar_data.id.length > 0) {
					EventItem.appEventType = calendarEventType;
					EventItem.id = calendar_data.id;
					EventItem.title = calendar_data.title;
					EventItem.start = calendar_data.startDate;
					EventItem.end = calendar_data.endDate;
					
					if(calendar_data.hasOwnProperty('allDay')) {
						EventItem.allDay = calendar_data.allDay;
					} else {
						EventItem.allDay = false;
					}
					if(calendar_data.hasOwnProperty('groupID')) {
						EventItem.groupID = calendar_data.groupID;
					} else {
						EventItem.groupID = 0;
					}
					if(calendar_data.hasOwnProperty('repeatInterval')) {
						EventItem.repeatInterval = calendar_data.repeatInterval;
					} else {
						EventItem.repeatInterval = 'HOURLY';
					}
					if(calendar_data.hasOwnProperty('repeatTimes')) {
						EventItem.repeatTimes = calendar_data.repeatTimes;
					} else {
						EventItem.repeatTimes = 1;
					}
					if(calendar_data.hasOwnProperty('reminderTime')) {
						EventItem.reminderTime = calendar_data.reminderTime;
					} else {
						EventItem.reminderTime = 15;
					}
					CalendarItems[CalendarItems.length] = EventItem;
				}
			});
	    });
		return CalendarItems;
	}
	//==============================================================================
	//HELP FUNCTIONS
	//==============================================================================
	function ViewOrHideLocationElements() {
		if($('#'+AppObjectParams.ObjectsID.DialogLocation).length) {
			if($('#AppCalendar-Dialog .dialog-outdoors').length) {
				if($('#'+AppObjectParams.ObjectsID.DialogLocation).val() === 'INDOORS') {
					$('#AppCalendar-Dialog .dialog-outdoors').hide();
				} else {
					$('#AppCalendar-Dialog .dialog-outdoors').show();
				}
			}
			
			if($('#AppCalendar-Dialog .dialog-indoors').length) {
				if($('#'+AppObjectParams.ObjectsID.DialogLocation).val() === 'INDOORS') {
					$('#AppCalendar-Dialog .dialog-indoors').show();
				} else {
					$('#AppCalendar-Dialog .dialog-indoors').hide();
				}
			}
		}
	}
	//******************************************************************************
	function ValidateStartEndTimes(what_change) {
		var start_date_ms = ReturnDateMsInUTC(EventDataFromCalendar.StartArr);
		var end_date_ms = ReturnDateMsInUTC(EventDataFromCalendar.EndArr);
		
		var start_time_index = parseInt($('#'+AppObjectParams.ObjectsID.DialogTimeStart).val());
		var end_time_index = parseInt($('#'+AppObjectParams.ObjectsID.DialogTimeEnd).val());
		
		var prev_time_inx_range = EventDataFromCalendar.EndTimeInx - EventDataFromCalendar.StartTimeInx;
		var timeArrLen = AppConfigData.Time.Labels.length;
		
		if(EventDataFromCalendar.AppEventType === 'general') {
			if(start_date_ms == end_date_ms) {
				if(start_time_index >= end_time_index) {
					if(what_change == 0) /*start time*/ {
						end_time_index = start_time_index + prev_time_inx_range;
						if((end_time_index + 1) >= timeArrLen) {
							end_time_index = timeArrLen - 1;
						}
						$('#'+AppObjectParams.ObjectsID.DialogTimeEnd).val(end_time_index);
					} else /*end time*/ {
						start_time_index = end_time_index - prev_time_inx_range;
						if(start_time_index < 0) {
							start_time_index = 0;
						}
						if(end_time_index == 0) {
							end_time_index = 1;
						}
						$('#'+AppObjectParams.ObjectsID.DialogTimeStart).val(start_time_index);
						$('#'+AppObjectParams.ObjectsID.DialogTimeEnd).val(end_time_index);
					}
				}
			}
		} else {
			var max_range_time_inx = 60 / 15;
			var min_range_time_inx = 1;
			
			if(start_date_ms == end_date_ms) {
				if(what_change == 0) {
					var min_end_time_inx = start_time_index + min_range_time_inx;
					var max_end_time_inx = start_time_index + max_range_time_inx;
					if(max_end_time_inx > timeArrLen - 1) {
						max_end_time_inx = timeArrLen - 1;
					}
					if(end_time_index < min_end_time_inx || end_time_index > max_end_time_inx) {
						end_time_index = max_end_time_inx;
					}
					$('#'+AppObjectParams.ObjectsID.DialogTimeEnd).val(end_time_index);
				} else {
					var min_start_time_inx = end_time_index - max_range_time_inx;
					var max_start_time_inx = end_time_index - min_range_time_inx;
					if(min_start_time_inx < 0) {
						min_start_time_inx = 0;
					}
					if(start_time_index < min_start_time_inx || start_time_index > max_start_time_inx) {
						start_time_index = min_start_time_inx;
					}
					$('#'+AppObjectParams.ObjectsID.DialogTimeStart).val(start_time_index);
				}
			} else {
				var min_start_time_inx = (timeArrLen - 2) - max_range_time_inx + 1;
				var max_end_time_inx = max_range_time_inx - 1;
				
				if(what_change == 0) {
					if(start_time_index < min_start_time_inx) {
						start_time_index = min_start_time_inx;
					}
					if(end_time_index > start_time_index + max_range_time_inx - (timeArrLen - 1)) {
						end_time_index = start_time_index + max_range_time_inx - (timeArrLen - 1);
					}
					$('#'+AppObjectParams.ObjectsID.DialogTimeStart).val(start_time_index);
					$('#'+AppObjectParams.ObjectsID.DialogTimeEnd).val(end_time_index);
				} else {
					if(end_time_index > max_end_time_inx) {
						end_time_index = max_end_time_inx;
					}
					if(start_time_index < (timeArrLen - 1) + end_time_index - max_range_time_inx) {
						start_time_index = (timeArrLen - 1) + end_time_index - max_range_time_inx;
					}
					$('#'+AppObjectParams.ObjectsID.DialogTimeStart).val(start_time_index);
					$('#'+AppObjectParams.ObjectsID.DialogTimeEnd).val(end_time_index);
				}
			}
		}
		
		EventDataFromCalendar.StartTimeInx = start_time_index;
		EventDataFromCalendar.EndTimeInx = end_time_index;
	}
	//******************************************************************************
	function CheckDialogParamsBeforeSend() {
		EventDataForSend = {};
		var CountErrors = 0;
		
		EventDataForSend.username = UserName;
		EventDataForSend.startDate = ReturnDateTimeMsInUTC(EventDataFromCalendar.StartArr, EventDataFromCalendar.StartTimeInx);
		EventDataForSend.endDate = ReturnDateTimeMsInUTC(EventDataFromCalendar.EndArr, EventDataFromCalendar.EndTimeInx);
		
		if(EventDataFromCalendar.AppEventType === "general") {
			EventDataForSend.allDay = EventDataFromCalendar.IsAllDay;
		} else {
			EventDataForSend.repeatInterval = $('#'+AppObjectParams.ObjectsID.DialogRepeatInterval).val();
			EventDataForSend.repeatTimes = parseInt($('#'+AppObjectParams.ObjectsID.DialogRepeatTimes).val());
			EventDataForSend.reminderTime = parseInt($('#'+AppObjectParams.ObjectsID.DialogReminderTime).val());
		}
		
		switch(EventDataFromCalendar.AppEventType) {
			case "general":
				var localEventTitle = '';
				localEventTitle = $.trim($('#'+AppObjectParams.ObjectsID.DialogTitle).val());
				
				if($('#'+AppObjectParams.ObjectsID.DialogTitle).hasClass('AppCalendar-has-error')) {
					$('#'+AppObjectParams.ObjectsID.DialogTitle).removeClass('AppCalendar-has-error');
				}
				
				if(localEventTitle.length <= 0) {
					$('#'+AppObjectParams.ObjectsID.DialogTitle).addClass('AppCalendar-has-error');
					$('#'+AppObjectParams.ObjectsID.DialogTitle).val('');
					$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
					CountErrors = CountErrors + 1;
				}
				
				EventDataForSend.title = localEventTitle;
			break;
			
			case "medical":
				var localEventTitle = '', localEventLocation = '', localMedExam = false, localInsCard = false, 
				localOtherDocs = '', localTransportation = '', localTransportationStr = '', localTransportationSave = '', 
				localDoctorName = '', localDoctorPhone = '';
								
				localEventTitle = $.trim($('#'+AppObjectParams.ObjectsID.DialogTitle).val());
				localEventLocation = $('#'+AppObjectParams.ObjectsID.DialogLocation).val();
				localOtherDocs = $.trim($('#'+AppObjectParams.ObjectsID.DialogOtherDocs).val());
				localTransportation = $('#'+AppObjectParams.ObjectsID.DialogTransportation).val();
				localTransportationStr = $.trim($('#'+AppObjectParams.ObjectsID.DialogTransportationStr).val());
				localDoctorName = $.trim($('#'+AppObjectParams.ObjectsID.DialogDoctorName).val());
				localDoctorPhone = $.trim($('#'+AppObjectParams.ObjectsID.DialogDoctorPhone).val());
				
				if($('#'+AppObjectParams.ObjectsID.DialogTitle).hasClass('AppCalendar-has-error')) {
					$('#'+AppObjectParams.ObjectsID.DialogTitle).removeClass('AppCalendar-has-error');
				}
				if($('#'+AppObjectParams.ObjectsID.DialogTransportationStr).hasClass('AppCalendar-has-error')) {
					$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).removeClass('AppCalendar-has-error');
				}
				if($('#'+AppObjectParams.ObjectsID.DialogDoctorName).hasClass('AppCalendar-has-error')) {
					$('#'+AppObjectParams.ObjectsID.DialogDoctorName).removeClass('AppCalendar-has-error');
				}
				if($('#'+AppObjectParams.ObjectsID.DialogDoctorPhone).hasClass('AppCalendar-has-error')) {
					$('#'+AppObjectParams.ObjectsID.DialogDoctorPhone).removeClass('AppCalendar-has-error');
				}
				
				if(localEventTitle.length <= 0) {
					$('#'+AppObjectParams.ObjectsID.DialogTitle).addClass('AppCalendar-has-error');
					$('#'+AppObjectParams.ObjectsID.DialogTitle).val('');
					$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
					CountErrors = CountErrors + 1;
				}
				
				if(localEventLocation === 'INDOORS') {
					localTransportation = "";
					localMedExam = false; 
					localInsCard = false;
					localOtherDocs = "";
					localTransportation = "";
				} else {
					if($('#'+AppObjectParams.ObjectsID.DialogMedExam).prop('checked')) {
						localMedExam = true;
					}
					
					if($('#'+AppObjectParams.ObjectsID.DialogInsCard).prop('checked')) {
						localInsCard = true;
					}
				}
				
				if(localTransportation === 'OTHER') {
					localTransportationSave = localTransportationStr;
					if(localTransportationStr.length <= 0) {
						$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).addClass('AppCalendar-has-error');
						$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).val('');
						$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).focus();
						CountErrors = CountErrors + 1;
					}
				} else {
					localTransportationSave = localTransportation;
				}
				
				EventDataForSend.title = localEventTitle;
				EventDataForSend.doctorName = localDoctorName;
				EventDataForSend.doctorPhone = localDoctorPhone;
				EventDataForSend.location = localEventLocation;
				EventDataForSend.medExam = localMedExam;
				EventDataForSend.insCard = localInsCard;
				EventDataForSend.otherDocs = localOtherDocs;
				EventDataForSend.transportation = localTransportationSave;
			break;
			
			case "physical":
			case "cognitive":
				var localEventTitle = '', localEventLocation = '', localEventTrainer = '', localEventType = 'PHYSICAL';
				
				localEventTitle = $.trim($('#'+AppObjectParams.ObjectsID.DialogTitle).val());
				localEventLocation = $('#'+AppObjectParams.ObjectsID.DialogLocation).val();
				localEventTrainer = $('#'+AppObjectParams.ObjectsID.DialogTrainer).val();
								
				if($('#'+AppObjectParams.ObjectsID.DialogTitle).hasClass('AppCalendar-has-error')) {
					$('#'+AppObjectParams.ObjectsID.DialogTitle).removeClass('AppCalendar-has-error');
				}
				
				if(localEventTitle.length <= 0) {
					$('#'+AppObjectParams.ObjectsID.DialogTitle).addClass('AppCalendar-has-error');
					$('#'+AppObjectParams.ObjectsID.DialogTitle).val('');
					$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
					CountErrors = CountErrors + 1;
				}
				
				if(localEventLocation === 'OUTDOORS') {
					localEventTrainer = "NONE";
				}
				
				if(AppCalendarEventSupport.PartAppTypes2SB.hasOwnProperty(EventDataFromCalendar.AppEventType)) {
					localEventType = AppCalendarEventSupport.PartAppTypes2SB[EventDataFromCalendar.AppEventType];
				}				
				EventDataForSend.title = localEventTitle;
				EventDataForSend.location = localEventLocation;
				EventDataForSend.trainer = localEventTrainer;
				EventDataForSend.type = localEventType;
			break;
			
			case "shower":
			case "measurements":
			case "eating":
				var localEventTitle = '', localEventLocation = 'INDOORS', localEventType = '';
				
				localEventTitle = $('#'+AppObjectParams.ObjectsID.DialogTitle+' option:selected').text();
				localEventType = $('#'+AppObjectParams.ObjectsID.DialogTitle).val();
				if($('#'+AppObjectParams.ObjectsID.DialogLocation).length) {
					localEventLocation = $('#'+AppObjectParams.ObjectsID.DialogLocation).val();
				}
				
				EventDataForSend.title = localEventTitle;
				EventDataForSend.location = localEventLocation;
				EventDataForSend.type = localEventType;
			break;
			
			case "socializing":
				var localEventTitle = '', localEventLocation = '', localWallet = false, localKeys = false, 
				localOtherBelongings = '', localTransportation = '', localTransportationStr = '', 
				localTransportationSave = '';
				
				localEventTitle = $.trim($('#'+AppObjectParams.ObjectsID.DialogTitle).val());
				localEventLocation = $('#'+AppObjectParams.ObjectsID.DialogLocation).val();
				localOtherBelongings = $.trim($('#'+AppObjectParams.ObjectsID.DialogOtherBelongings).val());
				localTransportation = $('#'+AppObjectParams.ObjectsID.DialogTransportation).val();
				localTransportationStr = $.trim($('#'+AppObjectParams.ObjectsID.DialogTransportationStr).val());
				
				if($('#'+AppObjectParams.ObjectsID.DialogTitle).hasClass('AppCalendar-has-error')) {
					$('#'+AppObjectParams.ObjectsID.DialogTitle).removeClass('AppCalendar-has-error');
				}
				if($('#'+AppObjectParams.ObjectsID.DialogTransportationStr).hasClass('AppCalendar-has-error')) {
					$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).removeClass('AppCalendar-has-error');
				}
				
				if(localEventTitle.length <= 0) {
					$('#'+AppObjectParams.ObjectsID.DialogTitle).addClass('AppCalendar-has-error');
					$('#'+AppObjectParams.ObjectsID.DialogTitle).val('');
					$('#'+AppObjectParams.ObjectsID.DialogTitle).focus();
					CountErrors = CountErrors + 1;
				}
				
				if(localEventLocation === 'INDOORS') {
					localTransportation = "";
					localWallet = false; 
					localKeys = false;
					localOtherBelongings = "";
					localTransportation = "";
				} else {
					if($('#'+AppObjectParams.ObjectsID.DialogWallet).prop('checked')) {
						localWallet = true;
					}
					
					if($('#'+AppObjectParams.ObjectsID.DialogKeys).prop('checked')) {
						localKeys = true;
					}
				}
				
				if(localTransportation === 'OTHER') {
					localTransportationSave = localTransportationStr;
					if(localTransportationStr.length <= 0) {
						$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).addClass('AppCalendar-has-error');
						$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).val('');
						$('#'+AppObjectParams.ObjectsID.DialogTransportationStr).focus();
						CountErrors = CountErrors + 1;
					}
				} else {
					localTransportationSave = localTransportation;
				}
				
				EventDataForSend.title = localEventTitle;
				EventDataForSend.location = localEventLocation;
				EventDataForSend.wallet = localWallet;
				EventDataForSend.keys = localKeys;
				EventDataForSend.otherBelongings = localOtherBelongings;
				EventDataForSend.transportation = localTransportationSave;
			break; 
			
			default:
				CountErrors = 1;
		}
		
		if(CountErrors == 0) {
			return true;
		} else {
			EventDataForSend = {};
			return false;
		}
	}
	//==============================================================================
	function ReturnDateMsInUTC(date_arr) {
		return moment.utc([date_arr[0], date_arr[1], date_arr[2]]).valueOf();
	}
	//******************************************************************************
	function ReturnDateTimeMsInUTC(date_arr, time_key) {
		var moment_hours = AppConfigData.Time.Hours[time_key];
		var moment_minutes = AppConfigData.Time.Minutes[time_key];
		return moment.utc([date_arr[0], date_arr[1], date_arr[2], moment_hours, moment_minutes]).valueOf();
	}
	//==============================================================================
	function ViewOrHideOverlayElement(flag_view) {
		if(flag_view) {
			$('#AppCalendar-DataContainer .AppCalendar-Parts .AppCalendar-overlay').show();
		} else {
			$('#AppCalendar-DataContainer .AppCalendar-Parts .AppCalendar-overlay').hide();
		}
	}
	//******************************************************************************
	function ViewOrHideLoadElement(flag_view) {
		if(flag_view) {
			$('#AppCalendar-DataContainer .AppCalendar-Parts .AppCalendar-Load').show();
		} else {
			$('#AppCalendar-DataContainer .AppCalendar-Parts .AppCalendar-Load').hide();
		}
	}
	//******************************************************************************
	function ViewOrHideDialogLoadElement(flag_view) {
		if(flag_view) {
			$('#AppCalendar-Dialog .AppCalendar-Dialog-Load').show();
		} else {
			$('#AppCalendar-Dialog .AppCalendar-Dialog-Load').hide();
		}
	}
	//******************************************************************************
	function ElementAddOrRemoveLoading(obj_element_id, flag_add) {
		$(obj_element_id).removeClass('AppCalendar-loading');
		if(flag_add) $(obj_element_id).addClass('AppCalendar-loading');
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
	//==============================================================================
	function LogUserInteraction(on_calendar_event, app_event_type) {
		var param1 = '', param2 = '', param3 = '';
		
		if(UserIteractionData.hasOwnProperty('applicationName')) {
			param1 = UserIteractionData.applicationName;
		}
		if(UserIteractionData.iteractionData.hasOwnProperty(on_calendar_event)) {
			if(UserIteractionData.iteractionData[on_calendar_event].hasOwnProperty(app_event_type)) {
				if(UserIteractionData.iteractionData[on_calendar_event][app_event_type].hasOwnProperty('buttonId')) {
					param2 = UserIteractionData.iteractionData[on_calendar_event][app_event_type].buttonId;
				}
				if(UserIteractionData.iteractionData[on_calendar_event][app_event_type].hasOwnProperty('comment')) {
					param3 = UserIteractionData.iteractionData[on_calendar_event][app_event_type].comment;
				}
			}
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