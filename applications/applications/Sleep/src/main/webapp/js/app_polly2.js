 /* window.onload = function (e) {
                      var nav_days = document.getElementById('middle-nav-list-days');
                      nav_days.scrollLeft = nav_days.scrollWidth;

                      var nav_weeks = document.getElementById('middle-nav-list-weeks');
                      nav_weeks.scrollLeft = nav_weeks.scrollWidth;
                  }; */
 var sleepDataURL = '[protocol]//[hostname]//platform-[env]/idss-sleep-reasoner/sleepdata';
 var sleepLRURL = '[protocol]//[hostname]//platform-[env]/lr-sleep-monitor/sleepweekpattern';

 var userID = ewallApp.currentSession.user.username;
 var sleepLRData = {
     days: [null, null, null, null, null, null, null]
 };
 var chartOptions = {
     ///Boolean - Whether grid lines are shown across the chart
     scaleShowGridLines: true,

     //String - Colour of the grid lines
     scaleGridLineColor: "rgba(0,0,0,.05)",

     //Number - Width of the grid lines
     scaleGridLineWidth: 1,

     //Boolean - Whether to show horizontal lines (except X axis)
     scaleShowHorizontalLines: true,

     //Boolean - Whether to show vertical lines (except Y axis)
     scaleShowVerticalLines: true,

     scaleFontSize: 22,

     //Boolean - Whether the line is curved between points
     bezierCurve: true,

     //Number - Tension of the bezier curve between points
     bezierCurveTension: 0.4,

     //Boolean - Whether to show a dot for each point
     pointDot: true,

     //Number - Radius of each point dot in pixels
     pointDotRadius: 10,

     //Number - Pixel width of point dot stroke
     pointDotStrokeWidth: 1,

     //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
     pointHitDetectionRadius: 20,

     //Boolean - Whether to show a stroke for datasets
     datasetStroke: true,

     //Number - Pixel width of dataset stroke
     datasetStrokeWidth: 2,

     //Boolean - Whether to fill the dataset with a colour
     datasetFill: true,

     //String - A legend template
     legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].strokeColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"

 };

 var selectedTab = true; // true if day tab, false if weeks tab

 var nav_days_scrollW = 0;

 var nav_weeks_scrollW = 0;

 var weekSleepData = [];

 /*Added by Polly*/
 var SleepSliderScrollObject = null;
 var SleepSliderSizes = {
     'VisPartWidth': (162 * 7),
     'OneItemWidth': 162
 };
 var SleepSliderMoveParams = {
     'Days': {
         'CurrentPosition': 0,
         'NextPosition': 0,
         'MaxPosition': 0,
         'MinPosition': 0,
         'ObjectULWidth': 0,
         'IsMovedByFunc': false,
         'IsClickTriggered': false
     },
     'Weeks': {
         'CurrentPosition': 0,
         'NextPosition': 0,
         'MaxPosition': 0,
         'MinPosition': 0,
         'ObjectULWidth': 0,
         'IsMovedByFunc': false,
         'IsClickTriggered': false
     }
 };
 /*End Adding*/

 function setEWALLRelativeURL(url) {

     var hostname = window.location.hostname;
     var pathname = window.location.pathname;
     var protocol = window.location.protocol;

     console.log("Pathname: " + pathname);

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
     };

     var newURL = url.replace("[protocol]", protocol).replace("[hostname]", hostname).replace("[env]", env)

     return newURL;

 }

 function formatFromDate(date) {
     var d = new Date(date);
     d.setDate(date.getDate() - 1);
     /* var month = '' + (d.getMonth() + 1);
     var day = '' + d.getDate();
     var year = d.getFullYear();

     if (month.length < 2) month = '0' + month;
     if (day.length < 2) day = '0' + day;

     return [year, month, day].join('-'); */
     return d.toISOString();
 }

 function formatToDate(date) {
     var d = new Date(date);
     /*
     var month = '' + (d.getMonth() + 1),
     var day = '' + d.getDate(),
     var year = d.getFullYear();

     if (month.length < 2) month = '0' + month;
     if (day.length < 2) day = '0' + day;

     return [year, month, day].join('-');
     */
     return d.toISOString();
 }

 function getLRSleepData(userID, date) {
     if (date) {
         ewallApp.ajax({
             /* $.ajax({ */
             url: sleepLRURL,
             type: "GET",
             /* for localhost development only
             headers: {
                 'X-Auth-Token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiU1lTVEVNIiwiYWxsc2VydmljZXMiOnRydWUsImV4cCI6NDU4NDM0NDMzMSwic3ViIjoibHItc2xlZXAtbW9uaXRvciJ9.9D5w879LTQlKNsVCoVdfEZRVEKuZle0ympHx4dHhqYY'
             },
             */
             beforeSend: function (xhr) {
                 xhr.setRequestHeader("Content-type", "text/plain");

             },
             data: {
                 userid: userID,
                 date: date
             },
             dataType: "json",
             async: true,
             success: function (data) {
                 console.log("--- Sleep LR Data");
                 console.log(data);
                 for (i = 0; i < data.days.length; i++) {
                     sleepLRData.days[data.days[i].weekDay - 1] = data.days[i];
                 }
             }

         }).always(function () {
             $('#today-tab').trigger('click');
         });
     }
 };

 function getSleepData(userID, fromDate, toDate) {
     // initialize html placeholders
     $("#went-to-bed-data").html(undefinedData[language]);
     $("#went-to-bed-LR-data").html(undefinedData[language]);
     $("#wake-up-times-data").html(undefinedData[language]);
     $("#morning-wake-up-data").html(undefinedData[language]);
     $("#morning-woke-up-LR-data").html(undefinedData[language]);
     $("#sleep-efficiency-data").html(undefinedData[language]);
     $("#total-sleep-time-data").html(undefinedData[language]);

     console.log('userID: ' + userID + '  fromDate:' + fromDate + '  toDate:' + toDate);
     if (fromDate && toDate) {
         ewallApp.ajax({ // switch back to eWALL ajax call, once the toke problem is fixed.
             // $.ajax({
             url: sleepDataURL,
             type: "GET",
             /* comment the headers part , once the token issue is clarified */
             /*  
             headers: {
                 'X-Auth-Token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiU1lTVEVNIiwiYWxsc2VydmljZXMiOnRydWUsImV4cCI6NDU4NDM0NDMzMSwic3ViIjoibHItc2xlZXAtbW9uaXRvciJ9.9D5w879LTQlKNsVCoVdfEZRVEKuZle0ympHx4dHhqYY'
             }, 
             */
             beforeSend: function (xhr) {
                 xhr.setRequestHeader("Content-type", "text/plain");

             },
             data: {
                 username: userID,
                 from: fromDate,
                 to: toDate
             },
             dataType: "json",
             async: true,
             success: function (data) {
                 if (data) {
                     console.log(data);
                     // parse data to JSON
                     if (data.length > 0) { /*changed by Polly because give me an error - before: if(data)*/
                         // update UI with data
                         var wentToBedDate = new Date(data[0].bedOnTime); //the date to which the user went to bed
                         var wentToBedHours = wentToBedDate.getHours(); //the hour part of the time the user went to bed
                         wentToBedHours = ("0" + wentToBedHours).slice(-2);
                         var wentToBedMinutes = wentToBedDate.getMinutes(); //the minutes part of the time the user went to bed
                         wentToBedMinutes = ("0" + wentToBedMinutes).slice(-2);
                         $("#went-to-bed-data").html(wentToBedHours + ':' + wentToBedMinutes); // change when sleep reasoner interface is ready
                         if (sleepLRData) {
                             var lrDayNr = wentToBedDate.getDay();
                             // if it's Sunday then make lrDayNt = 6
                             if (lrDayNr == 0) {
                                 lrDayNr = 6
                             }
                             if (sleepLRData.days[lrDayNr]) {
                                 for (i = 0; i < sleepLRData.days[lrDayNr].parameters.length; i++) {
                                     if (sleepLRData.days[lrDayNr].parameters[i].parameter == 'bedOnTime') {
                                         var bedOnTimeLRAverage = sleepLRData.days[lrDayNr].parameters[i].weightedAverage;
                                         if (bedOnTimeLRAverage >= 0) {
                                             var bedOnTimeLRHour = Math.floor(bedOnTimeLRAverage / 60);
                                             if (bedOnTimeLRHour >= 24) {
                                                 bedOnTimeLRHour = bedOnTimeLRHour - 24;
                                             };
                                             bedOnTimeLRHour = ("0" + bedOnTimeLRHour).slice(-2);

                                             var bedOnTimeMinutes = bedOnTimeLRAverage % 60;
                                             bedOnTimeMinutes = ("0" + bedOnTimeMinutes).slice(-2);
                                             $('#went-to-bed-LR-data').html(bedOnTimeLRHour + ':' + bedOnTimeMinutes);
                                         } else {
                                             $('#went-to-bed-LR-data').html(undefinedData[language]);
                                         }
                                     }
                                 }
                             }
                         }

                         $("#wake-up-times-data").html(data[0].frequencyWakingUp);

                         var morningWakeUp = new Date(data[0].bedOffTime);
                         var morningWakeUpHours = morningWakeUp.getHours();
                         morningWakeUpHours = ("0" + morningWakeUpHours).slice(-2);
                         var morningWakeUpMinutes = morningWakeUp.getMinutes();
                         morningWakeUpMinutes = ("0" + morningWakeUpMinutes).slice(-2);
                         $("#morning-wake-up-data").html(morningWakeUpHours + ':' + morningWakeUpMinutes); // change when sleep reasoner interface is ready
                         if (sleepLRData) {
                             var lrDayNr = wentToBedDate.getDay();
                             // if it's Sunday then make lrDayNt = 6
                             if (lrDayNr == 0) {
                                 lrDayNr = 6
                             }
                             if (sleepLRData.days[lrDayNr]) {
                                 for (i = 0; i < sleepLRData.days[lrDayNr].parameters.length; i++) {
                                     if (sleepLRData.days[lrDayNr].parameters[i].parameter == 'bedOffTime') {
                                         var bedOffTimeLRAverage = sleepLRData.days[lrDayNr].parameters[i].weightedAverage;
                                         if (bedOffTimeLRAverage >= 0) {
                                             var bedOffTimeLRHour = Math.floor(bedOffTimeLRAverage / 60);
                                             if (bedOffTimeLRHour >= 24) {
                                                 bedOffTimeLRHour = bedOffTimeLRHour - 24;
                                             };
                                             bedOffTimeLRHour = ("0" + bedOffTimeLRHour).slice(-2);

                                             var bedOffTimeMinutes = bedOffTimeLRAverage % 60;
                                             bedOffTimeMinutes = ("0" + bedOffTimeMinutes).slice(-2);
                                             $('#morning-woke-up-LR-data').html(bedOffTimeLRHour + ':' + bedOffTimeMinutes);
                                         } else {
                                             $('#morning-woke-up-LR-data').html(undefinedData[language]);
                                         }
                                     }
                                 }
                             }

                         }

                         $("#sleep-efficiency-data").html(data[0].sleepEfficiency + '%');
                         var totalSleepHours = Math.floor(data[0].totalSleepTime / 60);
                         var totalSleepMinutes = data[0].totalSleepTime % 60;
                         $("#total-sleep-time-data").html(totalSleepHours + ' h ' + totalSleepMinutes + ' m ');
                     }


                 }
             }
         });
     }

 }

 function getSleepWeekData(userID, fromDate, toDate) {
     if (fromDate && toDate) {
         ewallApp.ajax({ // switch back to eWALL ajax call, once the toke problem is fixed.
             // $.ajax({
             url: sleepDataURL,
             type: "GET",
             /* comment the headers part , once the token issue is clarified */
             /*
             headers: {
                 'X-Auth-Token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiU1lTVEVNIiwiYWxsc2VydmljZXMiOnRydWUsImV4cCI6NDU4NDM0NDMzMSwic3ViIjoibHItc2xlZXAtbW9uaXRvciJ9.9D5w879LTQlKNsVCoVdfEZRVEKuZle0ympHx4dHhqYY'
             }, 
             */
             beforeSend: function (xhr) {
                 xhr.setRequestHeader("Content-type", "text/plain");

             },
             data: {
                 username: userID,
                 from: fromDate,
                 to: toDate
             },
             dataType: "json",
             async: true,
             success: function (data) {
                 if (data) {
                     weekSleepData = data;
                     drawSleepEfficiency(data);
                     console.log('--weekSleepData');
                     console.log(weekSleepData);
                 }
             },
             complete: function () {
                 $('#sleep-efficiency-week').trigger('click');
                 $('#sleep-window-book-filler-body').children('.visible').fadeIn();
             }
         });
     }

 }

 /*function addDaysPolly(lang) - added by Polly*/
 function addDaysPolly(lang) {
     var date = new Date();
     // append today
     var last_night = {
         'en': 'Last Night',
         'nl': 'Afgelopen Nacht',
         'da': 'Igår aftes',
         'de': 'Letzte Nacht',
         'it': 'Ieri sera'
     };

     var str_txt_1 = "",
         str_txt_2 = "";
     $('#middle-nav-list-days').empty();
     $('#middle-nav-list-days').append($('<ul>'));
     for (var i = 99; i >= 0; i--) {
         var passedDay = new Date();
         passedDay.setDate(date.getDate() - i);

         if (i == 0) str_txt_1 = last_night[lang];
         else str_txt_1 = passedDay.toLocaleString(lang, {
             weekday: 'long'
         });

         str_txt_2 = passedDay.getDate() + ' ' + passedDay.toLocaleString(lang, {
             month: "short"
         }) + ' ' + passedDay.getFullYear();

         $('#middle-nav-list-days ul').append($('<li>'));
         $('#middle-nav-list-days ul li:last').append($('<div>').addClass('gwd-div-1xks night').attr({
             'data-night-from': formatFromDate(passedDay),
             'data-night-to': formatToDate(passedDay)
         }));

         $('#middle-nav-list-days ul li:last .gwd-div-1xks').append($('<div>').addClass('el-txt-1'));
         $('#middle-nav-list-days ul li:last .gwd-div-1xks').append($('<div>').addClass('el-txt-2'));

         $('#middle-nav-list-days ul li:last .el-txt-1').append($('<label>').text(str_txt_1));
         $('#middle-nav-list-days ul li:last .el-txt-2').append($('<label>').text(str_txt_2));
     }

     var NumberItems = $('#middle-nav-list-days ul li').length;
     SleepSliderMoveParams.Days.ObjectULWidth = SleepSliderSizes.OneItemWidth * NumberItems;
     SleepSliderMoveParams.Days.MaxPosition = -(SleepSliderMoveParams.Days.ObjectULWidth - SleepSliderSizes.VisPartWidth);
     SleepSliderMoveParams.Days.CurrentPosition = SleepSliderMoveParams.Days.MaxPosition;

     $('#middle-nav-list-days ul').css({
         'width': SleepSliderMoveParams.Days.ObjectULWidth,
         'left': 0
     });
 };

 /*function addWeeksPolly(lang) - added by Polly*/
 function addWeeksPolly(lang) {
     var startDate = new Date();
     var endDate = new Date();
     endDate.setDate(startDate.getDate() - 7);
     var this_week = {
         'en': 'Last Week',
         'nl': 'Vorige week',
         'da': 'Sidste uge',
         'de': 'Letzte Woche',
         'it': 'Settimana scorsa'
     };
     var week_label = {
         'en': 'Week',
         'nl': 'Week',
         'da': 'Uge',
         'de': 'Woche',
         'it': 'Settimana'
     };

     var str_txt_1 = "",
         str_txt_2 = "";
     $('#middle-nav-list-weeks').empty();
     $('#middle-nav-list-weeks').append($('<ul>'));
     for (i = 11; i >= 0; i--) {
         var passedDay = new Date();
         passedDay.setDate(passedDay.getDate() - i * 7);

         passedDay.setTime(passedDay.getTime() - (passedDay.getDay() ? passedDay.getDay() : 7) * 24 * 60 * 60 * 1000);
         var toISO = passedDay.toISOString();
         var to = passedDay.getDate();
         var toMonth = passedDay.toLocaleString(lang, {
             month: "short"
         });
         passedDay.setTime(passedDay.getTime() - 7 * 24 * 60 * 60 * 1000);
         var fromISO = passedDay.toISOString();
         var from = passedDay.getDate();
         var fromMonth = passedDay.toLocaleString(lang, {
             month: "short"
         });

         if (i == 0) str_txt_1 = this_week[lang];
         else str_txt_1 = week_label[lang];

         str_txt_2 = from + '.' + fromMonth + ' - ' + to + '.' + toMonth;

         $('#middle-nav-list-weeks ul').append($('<li>'));
         $('#middle-nav-list-weeks ul li:last').append($('<div>').addClass('gwd-div-1xks week').attr({
             'data-week-from': fromISO,
             'data-week-to': toISO
         }));

         $('#middle-nav-list-weeks ul li:last .gwd-div-1xks').append($('<div>').addClass('el-txt-1'));
         $('#middle-nav-list-weeks ul li:last .gwd-div-1xks').append($('<div>').addClass('el-txt-2'));

         $('#middle-nav-list-weeks ul li:last .el-txt-1').append($('<label>').text(str_txt_1));
         $('#middle-nav-list-weeks ul li:last .el-txt-2').append($('<label>').text(str_txt_2));
     }

     var NumberItems = $('#middle-nav-list-weeks ul li').length;
     SleepSliderMoveParams.Weeks.ObjectULWidth = SleepSliderSizes.OneItemWidth * NumberItems;
     SleepSliderMoveParams.Weeks.MaxPosition = -(SleepSliderMoveParams.Weeks.ObjectULWidth - SleepSliderSizes.VisPartWidth);
     SleepSliderMoveParams.Weeks.CurrentPosition = SleepSliderMoveParams.Weeks.MaxPosition;

     $('#middle-nav-list-weeks ul').css({
         'width': SleepSliderMoveParams.Weeks.ObjectULWidth,
         'left': 0
     });
 };

 function addDays(lang) {

     var date = new Date();
     // append today
     var last_night = {
         'en': 'Last Night',
         'nl': 'Afgelopen Nacht',
         'da': 'Igår aftes',
         'de': 'Letzte Nacht',
         'it': 'Ieri sera'
     };

     var lastNight = new Date();
     //lastNight.setDate(date.getDate() - 1);
     var todayMarkup = '<div class="gwd-div-1xks night selected" data-night-from="' + formatFromDate(lastNight) + '" data-night-to="' + formatToDate(lastNight) + '">' + last_night[lang] + '<div class="date" style="display: table; margin-left: 20px; margin-top: 55px;">' + lastNight.getDate() + '.' + lastNight.toLocaleString(lang, {
         month: "short"
     }) + '.' + lastNight.getFullYear() + '</div>   </div>';
     $('#middle-nav-list-days').append(todayMarkup);

     // prepend the rest fo the days
     for (i = 1; i < 100; i++) {
         var passedDay = new Date();
         passedDay.setDate(date.getDate() - i);
         var passedMarkup = '<div class="gwd-div-1xks night" data-night-from="' + formatFromDate(passedDay) + '" data-night-to="' + formatToDate(passedDay) + '"> ' + passedDay.toLocaleString(lang, {
             weekday: 'long'
         }) + ' <div style="display: table; margin-left: 20px; margin-top: 55px;"> ' + passedDay.getDate() + '.' + passedDay.toLocaleString(lang, {
             month: "short"
         }) + passedDay.getFullYear() + ' </div> </div>';
         $('#middle-nav-list-days').prepend(passedMarkup);
     }
     console.log('finished drawing #middle-nav-list-days');



 };

 function addWeeks(lang) {
     var startDate = new Date();
     var endDate = new Date();
     endDate.setDate(startDate.getDate() - 7);
     var this_week = {
         'en': 'Last Week',
         'nl': 'Vorige week',
         'da': 'Sidste uge',
         'de': 'Letzte Woche',
         'it': 'Settimana scorsa'
     };
     var week_label = {
         'en': 'Week',
         'nl': 'Week',
         'da': 'Uge',
         'de': 'Woche',
         'it': 'Settimana'
     };

     var d = new Date();
     d.setTime(d.getTime() - (d.getDay() ? d.getDay() : 7) * 24 * 60 * 60 * 1000);
     var toISO = d.toISOString();
     var to = d.getDate();
     var toMonth = d.toLocaleString(lang, {
         month: "short"
     });
     d.setTime(d.getTime() - 6 * 24 * 60 * 60 * 1000);
     var fromISO = d.toISOString();
     var from = d.getDate();
     var fromMonth = d.toLocaleString(lang, {
         month: "short"
     });

     // append today
     var todayMarkup = '<div class="gwd-div-1xks selected week" data-week-from="' + fromISO + '" data-week-to="' + toISO + '">' + this_week[lang] + '<div class="date" style="display: table; margin-left: 20px; margin-top: 55px;"> ' + from + '.' + fromMonth + ' - ' + to + '.' + toMonth + ' </div>   </div>';
     $('#middle-nav-list-weeks').append(todayMarkup);

     // prepend the rest fo the days
     for (i = 1; i < 12; i++) {
         var passedDay = new Date();
         passedDay.setDate(passedDay.getDate() - i * 7);

         passedDay.setTime(passedDay.getTime() - (passedDay.getDay() ? passedDay.getDay() : 7) * 24 * 60 * 60 * 1000);
         var toISO = passedDay.toISOString();
         var to = passedDay.getDate();
         var toMonth = passedDay.toLocaleString(lang, {
             month: "short"
         });
         passedDay.setTime(passedDay.getTime() - 6 * 24 * 60 * 60 * 1000);
         var fromISO = passedDay.toISOString();
         var from = passedDay.getDate();
         var fromMonth = passedDay.toLocaleString(lang, {
             month: "short"
         });
         var passedMarkup = '<div class="gwd-div-1xks week" data-week-from="' + fromISO + '" data-week-to="' + toISO + '"> ' + week_label[lang] + ' <div style="display: table; margin-left: 20px; margin-top: 55px;">' + from + '.' + fromMonth + ' - ' + to + '.' + toMonth + ' </div> </div>';
         $('#middle-nav-list-weeks').prepend(passedMarkup);
     }
     console.log('finished drawing weeks');

 };

 function drawSleepEfficiency(data) {
     for (index in ui) {
         if (ui[index].lang == language) {
             $('#chart-title').html(ui[index].elements.week.efficiency.btn + '<br>' + ui[index].elements.week.efficiency.definition);
         }
     };
     var sleepEfficiencyCurrentDataSet = [0, 0, 0, 0, 0, 0, 0];
     for (i = 0; i < data.length; i++) {
         var d = new Date(data[i].timestamp);

         if (d.getDay() == 0) { // Sunday is considered 0 by the javascript getDay() and & 6 in our 0-based set
             sleepEfficiencyCurrentDataSet[6] = data[i].sleepEfficiency;
         } else {
             sleepEfficiencyCurrentDataSet[d.getDay() - 1] = data[i].sleepEfficiency;
         }
     };


     var sleepEfficiencyLRDataSet = [0, 0, 0, 0, 0, 0, 0];
     for (i = 0; i < sleepLRData.days.length; i++) {
         if (sleepLRData.days[i]) {
             for (j = 0; j < sleepLRData.days[i].parameters.length; j++) {
                 if (sleepLRData.days[i].parameters[j].parameter == 'sleepEfficiency') {
                     sleepEfficiencyLRDataSet[i] = sleepLRData.days[i].parameters[j].weightedAverage;
                 }
             }
         } else {
             sleepEfficiencyLRDataSet[i] = 0;
         }
     };

     var myData = {
         labels: [monday[language], tuesday[language], wednesday[language], thursday[language], friday[language], saturday[language], sunday[language]],
         datasets: [
             {
                 fillColor: "rgba(220,220,220,0.2)",
                 strokeColor: "rgba(220,220,220,1)",
                 pointColor: "rgba(220,220,220,1)",
                 pointStrokeColor: "#fff",
                 pointHighlightFill: "#fff",
                 pointHighlightStroke: "rgba(220,220,220,1)",
                 data: sleepEfficiencyLRDataSet
                    },
             {
                 fillColor: "rgba(151,187,205,0.2)",
                 strokeColor: "rgba(151,187,205,1)",
                 pointColor: "rgba(151,187,205,1)",
                 pointStrokeColor: "#fff",
                 pointHighlightFill: "#fff",
                 pointHighlightStroke: "rgba(151,187,205,1)",
                 data: sleepEfficiencyCurrentDataSet
                    }
                  ]
     };

     $('#chart-area').remove();
     $('#dashboard-week').append('<canvas id="chart-area" width="1000" height="400" class="gwd-img-1a3b"></canvas>');
     var MyLineChart = new Chart(document.getElementById('chart-area').getContext("2d")).Line(myData, chartOptions);

 };

 function drawSleepTime(data) {
     for (index in ui) {
         if (ui[index].lang == language) {
             $('#chart-title').html(ui[index].elements.week.sleep_time.btn + '<br>' + ui[index].elements.week.sleep_time.definition);
         }
     };

     var sleepTimeCurrentDataSet = [0, 0, 0, 0, 0, 0, 0];
     for (i = 0; i < data.length; i++) {
         var d = new Date(data[i].timestamp);
         if (d.getDay() == 0) { // Sunday is considered 0 by the javascript getDay() and & 6 in our 0-based set
             sleepTimeCurrentDataSet[6] = parseInt(data[i].totalSleepTime / 60);
         } else {
             sleepTimeCurrentDataSet[d.getDay() - 1] = parseInt(data[i].totalSleepTime / 60);
         }

     }

     var sleepTimeLRDataSet = [0, 0, 0, 0, 0, 0, 0];
     for (i = 0; i < sleepLRData.days.length; i++) {
         if (sleepLRData.days[i]) {
             for (j = 0; j < sleepLRData.days[i].parameters.length; j++) {
                 if (sleepLRData.days[i].parameters[j].parameter == 'totalSleepTime') {
                     sleepTimeLRDataSet[i] = parseInt(sleepLRData.days[i].parameters[j].weightedAverage / 60);
                 }
             }
         } else {
             sleepTimeLRDataSet[i] = 0;
         }
     };

     var myData = {
         labels: [monday[language], tuesday[language], wednesday[language], thursday[language], friday[language], saturday[language], sunday[language]],
         datasets: [
             {
                 fillColor: "rgba(220,220,220,0.2)",
                 strokeColor: "rgba(220,220,220,1)",
                 pointColor: "rgba(220,220,220,1)",
                 pointStrokeColor: "#fff",
                 pointHighlightFill: "#fff",
                 pointHighlightStroke: "rgba(220,220,220,1)",
                 data: sleepTimeLRDataSet
                    },
             {
                 fillColor: "rgba(151,187,205,0.2)",
                 strokeColor: "rgba(151,187,205,1)",
                 pointColor: "rgba(151,187,205,1)",
                 pointStrokeColor: "#fff",
                 pointHighlightFill: "#fff",
                 pointHighlightStroke: "rgba(151,187,205,1)",
                 data: sleepTimeCurrentDataSet
                    }
                  ]
     };
     $('#chart-area').remove();
     $('#dashboard-week').append('<canvas id="chart-area" width="1000" height="400" class="gwd-img-1a3b"></canvas>');
     var MyLineChart = new Chart(document.getElementById('chart-area').getContext("2d")).Line(myData, chartOptions);
     console.log(MyLineChart);

 };

 function drawSleepAwakenings(data) {
     for (index in ui) {
         if (ui[index].lang == language) {
             $('#chart-title').html(ui[index].elements.week.awakenings.btn + '<br>' + ui[index].elements.week.awakenings.definition);
         }
     };
     var sleepAwakeningsCurrentDataSet = [0, 0, 0, 0, 0, 0, 0];;
     for (i = 0; i < data.length; i++) {
         var d = new Date(data[i].timestamp);
         if (d.getDay() == 0) { // Sunday is considered 0 by the javascript getDay() and & 6 in our 0-based set
             sleepAwakeningsCurrentDataSet[6] = data[i].frequencyWakingUp;
         } else {
             sleepAwakeningsCurrentDataSet[d.getDay() - 1] = data[i].frequencyWakingUp;
         }

     }

     var sleepAwakeningsLRDataSet = [0, 0, 0, 0, 0, 0, 0];
     for (i = 0; i < sleepLRData.days.length; i++) {
         if (sleepLRData.days[i]) {
             for (j = 0; j < sleepLRData.days[i].parameters.length; j++) {
                 if (sleepLRData.days[i].parameters[j].parameter == 'frequencyWakingUp') {
                     sleepAwakeningsLRDataSet[i] = sleepLRData.days[i].parameters[j].weightedAverage;
                 }
             }
         } else {
             sleepAwakeningsLRDataSet[i] = 0;
         }
     };

     var myData = {
         labels: [monday[language], tuesday[language], wednesday[language], thursday[language], friday[language], saturday[language], sunday[language]],
         datasets: [
             {
                 fillColor: "rgba(220,220,220,0.2)",
                 strokeColor: "rgba(220,220,220,1)",
                 pointColor: "rgba(220,220,220,1)",
                 pointStrokeColor: "#fff",
                 pointHighlightFill: "#fff",
                 pointHighlightStroke: "rgba(220,220,220,1)",
                 data: sleepAwakeningsLRDataSet
                    },
             {
                 fillColor: "rgba(151,187,205,0.2)",
                 strokeColor: "rgba(151,187,205,1)",
                 pointColor: "rgba(151,187,205,1)",
                 pointStrokeColor: "#fff",
                 pointHighlightFill: "#fff",
                 pointHighlightStroke: "rgba(151,187,205,1)",
                 data: sleepAwakeningsCurrentDataSet
                    }
                  ]
     };
     $('#chart-area').remove();
     $('#dashboard-week').append('<canvas id="chart-area" width="1000" height="400" class="gwd-img-1a3b"></canvas>');
     var MyLineChart = new Chart(document.getElementById('chart-area').getContext("2d")).Line(myData, chartOptions);

 };

 function drawSleepSnoring(data) {
     $('#chart-title').html('Snoring Time');
     var myData = {
         labels: [monday[language], tuesday[language], wednesday[language], thursday[language], friday[language], saturday[language], sunday[language]],
         datasets: [
             {
                 label: "Usually",
                 fillColor: "rgba(220,220,220,0.2)",
                 strokeColor: "rgba(220,220,220,1)",
                 pointColor: "rgba(220,220,220,1)",
                 pointStrokeColor: "#fff",
                 pointHighlightFill: "#fff",
                 pointHighlightStroke: "rgba(220,220,220,1)",
                 data: [10, 15, 20, 10, 21, 11, 12]
                    },
             {
                 label: "This Week",
                 fillColor: "rgba(151,187,205,0.2)",
                 strokeColor: "rgba(151,187,205,1)",
                 pointColor: "rgba(151,187,205,1)",
                 pointStrokeColor: "#fff",
                 pointHighlightFill: "#fff",
                 pointHighlightStroke: "rgba(151,187,205,1)",
                 data: [12, 21, 20, 10, 20, 18, 25]
                    }
                  ]
     };
     $('#chart-area').remove();
     $('#dashboard-week').append('<canvas id="chart-area" width="1000" height="400" class="gwd-img-1a3b"></canvas>');
     var MyLineChart = new Chart(document.getElementById('chart-area').getContext("2d")).Line(myData, chartOptions);
 };


 /*new functions added by Polly*/
 //******************************************************************************
 function SleepSliderMoveLeftRightIScroll(direction, NavList) {
     if (SleepSliderScrollObject == null) return false;

     SleepSliderMoveParams[NavList].CurrentPosition = SleepSliderScrollObject.getScrollX();
     if (direction == 0) {
         if (SleepSliderMoveParams[NavList].CurrentPosition > SleepSliderMoveParams[NavList].MaxPosition) {
             SleepSliderMoveParams[NavList].NextPosition = SleepSliderMoveParams[NavList].CurrentPosition - SleepSliderSizes.VisPartWidth;
             if (SleepSliderMoveParams[NavList].NextPosition < SleepSliderMoveParams[NavList].MaxPosition) SleepSliderMoveParams[NavList].NextPosition = SleepSliderMoveParams[NavList].MaxPosition;
             SleepSliderMoveParams[NavList].CurrentPosition = SleepSliderMoveParams[NavList].NextPosition;
             SleepSliderMoveParams[NavList].IsMovedByFunc = true;
             SleepSliderScrollObject.scrollTo(SleepSliderMoveParams[NavList].NextPosition, 0, 400, IScroll.utils.ease.quadratic);
         }
     }
     if (direction == 1) {
         if (SleepSliderMoveParams[NavList].CurrentPosition < SleepSliderMoveParams[NavList].MinPosition) {
             SleepSliderMoveParams[NavList].NextPosition = SleepSliderMoveParams[NavList].CurrentPosition + SleepSliderSizes.VisPartWidth;
             if (SleepSliderMoveParams[NavList].NextPosition > SleepSliderMoveParams[NavList].MinPosition) SleepSliderMoveParams[NavList].NextPosition = SleepSliderMoveParams[NavList].MinPosition;
             SleepSliderMoveParams[NavList].CurrentPosition = SleepSliderMoveParams[NavList].NextPosition;
             SleepSliderMoveParams[NavList].IsMovedByFunc = true;
             SleepSliderScrollObject.scrollTo(SleepSliderMoveParams[NavList].NextPosition, 0, 400, IScroll.utils.ease.quadratic);
         }
     }
 }
 //******************************************************************************
 function SleepSliderCenterOneItemIScroll(current_position, NavList) {
     if (SleepSliderScrollObject == null) return false;

     var new_position = -(current_position - ((SleepSliderSizes.VisPartWidth / 2) - (SleepSliderSizes.OneItemWidth / 2)));

     if (new_position > 0) new_position = 0;
     if (new_position < -(SleepSliderMoveParams[NavList].ObjectULWidth - SleepSliderSizes.VisPartWidth)) new_position = -(SleepSliderMoveParams[NavList].ObjectULWidth - SleepSliderSizes.VisPartWidth);

     SleepSliderMoveParams[NavList].NextPosition = new_position;
     SleepSliderMoveParams[NavList].CurrentPosition = SleepSliderMoveParams[NavList].NextPosition;

     SleepSliderMoveParams[NavList].IsMovedByFunc = true;
     SleepSliderScrollObject.scrollTo(SleepSliderMoveParams[NavList].NextPosition, 0, 400, IScroll.utils.ease.quadratic);
 }
 //******************************************************************************
 function SleepSliderOnElementClickIScroll(Item, NavList) {
     if (SleepSliderScrollObject == null) return false;

     var SliderElement = Item;
     var SliderElementPosLeft = SliderElement.position().left - 3; /* -3 because for .sleep-sl-vp ul li .gwd-div-1xks left is 3px;*/
     if (SleepSliderMoveParams[NavList].IsClickTriggered) {
         SleepSliderMoveParams[NavList].IsClickTriggered = false;
         if (SliderElementPosLeft != (SleepSliderMoveParams[NavList].ObjectULWidth - SleepSliderSizes.OneItemWidth)) {
             setTimeout(function () {
                 SleepSliderScrollObject.refresh();
                 SliderElementPosLeft = SliderElement.position().left - 3; /* -3 because for .sleep-sl-vp ul li .gwd-div-1xks left is 3px;*/
                 SleepSliderCenterOneItemIScroll(SliderElementPosLeft, NavList);
             }, 300);
         } else {
             SleepSliderCenterOneItemIScroll(SliderElementPosLeft, NavList);
         }
     } else {
         SleepSliderCenterOneItemIScroll(SliderElementPosLeft, NavList);
     }
 }
 //******************************************************************************
 function SleepSliderOnScrollEndIScroll(NavList) {
     if (SleepSliderScrollObject == null) return false;

     if (SleepSliderMoveParams[NavList].IsMovedByFunc) {
         SleepSliderMoveParams[NavList].IsMovedByFunc = false;
     } else {
         if (SleepSliderMoveParams[NavList].CurrentPosition > SleepSliderScrollObject.getScrollX()) {
             SleepSliderMoveParams[NavList].CurrentPosition = Math.floor(SleepSliderScrollObject.getScrollX() / SleepSliderSizes.OneItemWidth) * SleepSliderSizes.OneItemWidth;
         } else {
             SleepSliderMoveParams[NavList].CurrentPosition = Math.ceil(SleepSliderScrollObject.getScrollX() / SleepSliderSizes.OneItemWidth) * SleepSliderSizes.OneItemWidth;
         }
         SleepSliderScrollObject.scrollTo(SleepSliderMoveParams[NavList].CurrentPosition, 0);
     }
 }
 //******************************************************************************
 function SleepSliderRefreshPartMoveParamsIScroll(NavList) {
     SleepSliderMoveParams[NavList].NextPosition = 0;
     SleepSliderMoveParams[NavList].MinPosition = 0;
     SleepSliderMoveParams[NavList].IsMovedByFunc = false;
     SleepSliderMoveParams[NavList].IsClickTriggered = false;
 }
 //******************************************************************************
 /*End Adding*/


 $(document).ready(function () {
     
     //log user
     userInteractionLogger.sendInteractionWithComment("sleepApplication","applicationLoaded","Sleep application loaded");
     
     /* for delpoyment - uncomment before deploy*/
     
     sleepDataURL = setEWALLRelativeURL(sleepDataURL);     
     sleepLRURL = setEWALLRelativeURL(sleepLRURL);
     /* */ 
     
     
     /* for local host only - comment before deploy*/ 
     /*
     sleepDataURL = 'http://localhost/idss-sleep-reasoner/sleepdata';
     sleepLRURL = 'http://localhost/lr-sleep-monitor/sleepweekpattern';
     */
    
     addDaysPolly(language); /*Added by Polly*/     
     addWeeksPolly(language); /*Added by Polly*/



     $('#today-tab').click(function (e) {
         e.preventDefault();
         // hide all
         $('#dashboard-week').hide();
         $('#dashboard-top').hide();
         $('#middle-nav-list-days').hide();
         $('#middle-nav-list-weeks').hide();
         // deselect tabs
         $('#today-tab').removeClass('tab-selected');
         $('#week-tab').removeClass('tab-selected');

         // show night
         $('#middle-nav-list-days').show();
         $('#dashboard-top').fadeIn();
         $('#sleep-window-book-filler-body').children().removeClass('visible');
         $('#dashboard-top').addClass('visible');
         $('#today-tab').addClass('tab-selected');

         selectedTab = true;

         // scroll to the latest date
         /*Comment by Polly
         var nav_days = $('#middle-nav-list-days');
         */

         //nav_days_scrollW = nav_days.get(0).scrollWidth - 300;
         //nav_days_scrollW = nav_days.outerWidth(true);

         /*Comment by Polly
         nav_days_scrollW = 15200;
         console.log('today scroll: ' + nav_days_scrollW);

         nav_days.animate({
             scrollLeft: nav_days_scrollW
         });
         */

         /*Added by Polly*/
         if (SleepSliderScrollObject != null) {
             SleepSliderScrollObject.destroy();
             SleepSliderScrollObject = null;
             SleepSliderRefreshPartMoveParamsIScroll('Days');
         }
         SleepSliderScrollObject = new IScroll('#middle-nav-list-days', {
             scrollbars: 'custom',
             resizeScrollbars: false,
             scrollX: true,
             scrollY: false,
             interactiveScrollbars: true,
             momentum: false,
             mouseWheel: true,
             startX: 0,
             click: true
         });
         SleepSliderScrollObject.on('scrollEnd', function () {
             SleepSliderOnScrollEndIScroll('Days');
         });
         /*End Adding*/

         console.log($('.gwd-div-1xks.night').last());

         SleepSliderMoveParams.Days.IsClickTriggered = true; /*Added by Polly*/
         $('.gwd-div-1xks.night').last().trigger('click');
         
         //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","nightViewTab","User selects Night view");

     });

     $('#week-tab').click(function (e) {
         e.preventDefault();
         // hide all
         $('#dashboard-top').hide();
         $('#dashboard-week').hide();
         $('#middle-nav-list-days').hide();
         $('#middle-nav-list-weeks').hide();
         // deselect tabs
         $('#today-tab').removeClass('tab-selected');
         $('#week-tab').removeClass('tab-selected');

         // show week
         $('#middle-nav-list-weeks').show();
         $('#dashboard-week').fadeIn();
         $('#sleep-window-book-filler-body').children().removeClass('visible');
         $('#dashboard-week-ctrl').addClass('visible');
         $('#week-tab').addClass('tab-selected');
         removeSelectedChartBtns();
         $('#sleep-efficiency-week').addClass('selected');

         selectedTab = false;

         // scroll to the latest week
         /*Comment by Polly
         var nav_weeks = $('#middle-nav-list-weeks');
         */

         //nav_weeks_scrollW = nav_weeks.get(0).scrollWidth - 300;
         /*Comment by Polly
         nav_weeks_scrollW = 1400;
         console.log('week scroll: ' + nav_weeks_scrollW);

         nav_weeks.animate({
             scrollLeft: nav_weeks_scrollW
         });
         */

         /*Added by Polly*/
         if (SleepSliderScrollObject != null) {
             SleepSliderScrollObject.destroy();
             SleepSliderScrollObject = null;
             SleepSliderRefreshPartMoveParamsIScroll('Weeks');
         }
         SleepSliderScrollObject = new IScroll('#middle-nav-list-weeks', {
             scrollbars: 'custom',
             resizeScrollbars: false,
             scrollX: true,
             scrollY: false,
             interactiveScrollbars: true,
             momentum: false,
             mouseWheel: true,
             startX: 0,
             click: true
         });
         SleepSliderScrollObject.on('scrollEnd', function () {
             SleepSliderOnScrollEndIScroll('Weeks');
         });
         /*End Adding*/

         console.log($('.gwd-div-1xks.week').last());

         SleepSliderMoveParams.Weeks.IsClickTriggered = true; /*Added by Polly*/
         //$('.gwd-div-1xks.week').last().trigger('click');
         //setTimeout(function () {
         $('.gwd-div-1xks.week').last().trigger('click');
         //}, 300);
         
         //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","weekViewTab","User selects Week view");

     });

     $('#left-nav').click(function (e) {
         e.preventDefault();
         if (selectedTab) {
             /* Comment by Polly
             var nav_days = $('#middle-nav-list-days');
             nav_days_scrollW = nav_days.scrollLeft();
             nav_days_scrollW -= 300;
             nav_days.animate({
                 scrollLeft: nav_days_scrollW
             });*/
             SleepSliderMoveLeftRightIScroll(1, 'Days'); /*Added by Polly*/


         } else {
             /* Comment by Polly
         	 var nav_weeks = $('#middle-nav-list-weeks');
             nav_weeks_scrollW = nav_weeks.scrollLeft();
             nav_weeks_scrollW -= 300;
             nav_weeks.animate({
                 scrollLeft: nav_weeks_scrollW
             });*/
             SleepSliderMoveLeftRightIScroll(1, 'Weeks'); /*Added by Polly*/
         }
     });

     $('#right-nav').click(function (e) {
         e.preventDefault();
         if (selectedTab) {
             /* Comment by Polly
             var nav_days = $('#middle-nav-list-days');
             nav_days_scrollW = nav_days.scrollLeft();
             nav_days_scrollW += 300;
             nav_days.animate({
                 scrollLeft: nav_days_scrollW
             });*/
             SleepSliderMoveLeftRightIScroll(0, 'Days'); /*Added by Polly*/

         } else {
             /* Comment by Polly
         	 var nav_weeks = $('#middle-nav-list-weeks');
             nav_weeks_scrollW = nav_weeks.scrollLeft();
             nav_weeks_scrollW += 300;
             nav_weeks.animate({
                 scrollLeft: nav_weeks_scrollW
             });*/
             SleepSliderMoveLeftRightIScroll(0, 'Weeks'); /*Added by Polly*/
         }
     });

     // weekly dashboard controlls
     function removeSelectedChartBtns() {
         $('#sleep-efficiency-week').removeClass('selected');
         $('#sleep-time-week').removeClass('selected');
         $('#sleep-awakenings-week').removeClass('selected');
         $('#sleep-snoring-week').removeClass('selected');

     };

     $('#sleep-efficiency-week').click(function (e) {
         e.preventDefault();
         // $('#chart-area').attr('src', 'assets/sleep-efficiency.jpg');
         drawSleepEfficiency(weekSleepData);
         removeSelectedChartBtns();
         $(this).addClass('selected');
         
          //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","efficiencyWeekView","User selects Efficiency in Week view");
     });

     $('#sleep-time-week').click(function (e) {
         e.preventDefault();
         // $('#chart-area').attr('src', 'assets/sleep-time.jpg');
         drawSleepTime(weekSleepData);
         removeSelectedChartBtns();
         $(this).addClass('selected');
         
         //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","sleepTimeWeekView","User selects Sleep Time in Week view");
     });

     $('#sleep-awakenings-week').click(function (e) {
         e.preventDefault();
         // $('#chart-area').attr('src', 'assets/sleep-awakenings.jpg');
         drawSleepAwakenings(weekSleepData);
         removeSelectedChartBtns();
         $(this).addClass('selected');
         
         //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","awakeningsWeekView","User selects Awakening in Week view");
     });

     $('#sleep-snoring-week').click(function (e) {
         e.preventDefault();
         // $('#chart-area').attr('src', 'assets/sleep-snoring.jpg');
         drawSleepSnoring(weekSleepData);
         removeSelectedChartBtns();
         $(this).addClass('selected');
     });

     // night dashboard controlls

     // day/week navigation controlls
     function removeSelectedNavBox() {
         //Comment by Polly $('.gwd-div-11mb').children().removeClass('selected');
         $('.sleep-sl-vp ul li').children().removeClass('el-selected'); /*Added by Polly*/
     }

     $('.gwd-div-1xks.night').click(function (e) {
         e.preventDefault();
         removeSelectedNavBox();
         $(this).addClass('el-selected'); /* Changed by Polly old: selected; new:el-selected*/

         /*Added by Polly*/
         SleepSliderOnElementClickIScroll($(this), 'Days');
         /*End Adding*/

         // if the NIGHT tab is selected
         //if (selectedTab) {
         var selectedFromDate = $(this).data('nightFrom');
         console.log('get sleep data from:' + selectedFromDate);
         var selectedToDate = $(this).data('nightTo');
         console.log('get sleep data to:' + selectedToDate)
             //console.log(ewallApp.currentSession);
         getSleepData(userID, selectedFromDate, selectedToDate);

         $('#sleep-window-book-filler-body').children('.visible').hide();
         $('#sleep-window-book-filler-body').children('.visible').fadeIn();
         
         //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","nightButton","User selects " + selectedFromDate + " night in Night view");

     });
     $('.gwd-div-1xks.week').click(function (e) {
         /*Added by Polly - like in  $('.gwd-div-1xks.night').click(function (e) { */
         e.preventDefault();
         removeSelectedNavBox();
         $(this).addClass('el-selected');
         /*End Adding*/

         /*Added by Polly*/
         SleepSliderOnElementClickIScroll($(this), 'Weeks');
         /*End Adding*/

         //} else {
         //do UI updates for weeks
         var selectedFromDate = $(this).data('weekFrom');
         console.log('get sleep WEEK data from:' + selectedFromDate);
         var selectedToDate = $(this).data('weekTo');
         console.log('get sleep WEEK data to:' + selectedToDate)
             //console.log(ewallApp.currentSession);
         $('#sleep-window-book-filler-body').children('.visible').hide();
         getSleepWeekData(userID, selectedFromDate, selectedToDate);

         //}
         
         //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","weekButton","User selects week between" + selectedFromDate + " - " +selectedToDate+ " in Week view");

     });
     
      $('#went-to-bed').click(function (e) {
         e.preventDefault();
          
          //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","block1NightMode","User taps on block went-to-bed in Night page");
          
      });
     
      $('#wake-up-times').click(function (e) {
         e.preventDefault();
          
          //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","block2NightMode","User taps on block wake-up-times in Night page");
          
      });
     
      $('#morning-wake-up').click(function (e) {
         e.preventDefault();
          
          //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","block3NightMode","User taps on block morning-wake-up in Night page");
          
      });
     
     $('#conclusion').click(function (e) {
         e.preventDefault();
          
          //log user
         userInteractionLogger.sendInteractionWithComment("sleepApplication","block4NightMode","User taps on block conclusion in Night page");
          
      });

     var todayLR = new Date();
     var todayLRString = todayLR.toISOString();
     todayLRString = todayLRString.split("T")[0];
     console.log("LR Date: " + todayLRString);
     getLRSleepData(userID, todayLRString);

     // show last night of sleep on application launch

     //$('.gwd-div-1xks.selected').trigger('click');


 });