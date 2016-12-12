// ***
//  Memory game plugin for jQuery
//  Author: Yane Frenski
//  https://github.com/frenski/quizy-memorygame
//
//  (c) 2012-2013 http://yane.fr/
//  MIT licensed
// ***


// IE hack for indexOf
if(!Array.indexOf){
  Array.prototype.indexOf = function(obj){
   for(var i=0; i<this.length; i++){
    if(this[i]==obj){
     return i;
    }
   }
   return -1;
  }
}

/*function InitApp(){
	// get the UserLang from the input
	if(typeof ewallApp == 'object' && ewallApp != null) {
		UserName = ewallApp.currentSession.user.username;
		UserLang = ewallApp.preferedLanguage;
		
	}
}*/

var cardNums = 0;
var level = 0;

function levelEasy(){
    level = 1;
    cardNums = 4;
    startLevel();
}

function levelMedium(){
    level = 2;
    cardNums = 6;
    startLevel();
}

function levelDifficult(){
    level = 3;
    cardNums = 8;
    startLevel();
}

function getHelp(){
    
}

function startLevel(){
    document.getElementById('buttons').remove();
    $('#tutorial-memorygame').quizyMemoryGame('setup');
}

(function($) {
	var UserLang, UserName;
	
    var help = {
        bg: "Как се играе",
        da: "Hvordan man spiller",
        de: "Spiel erklären",
        en: "How to play",
        hr: "Kako igrati",
        it: "Come giocare",
        mk: "Како да се игра",
        nl: "Spel instructies",
        sr: "Kako igrati",
        ro: "Instrucțiuni"
    }
    
    var selectLevel = {
    	    bg: "",
    		da: "Vælg Sværhedsgrad:",
    		de: "Auswahl Schwierigkeitsgrad:",
    		en: "Select Level:",
    		hr: "Odaberi nivo:",
    		it: "Seleziona il livello:",
    		mk: "",
    		nl: "Selecteer Level:",
    		sr: "odaberite nivo:",
    		ro: "Alegeți nivelul:"
    	}

    var easy = {
        bg: "",
        da: "Nem",
        de: "leicht",
        en: "Easy",
        hr: "Lako",
        it: "Facile",
        mk: "",
        nl: "Makkelijk",
        sr: "lako",
        ro: "Ușor"   
    }
    
    var medium = {
        bg: "",
        da: "Medium",
        de: "mittel",
        en: "Medium",
        hr: "Srednje",
        it: "Medio",
        mk: "",
        nl: "Gemiddeld",
        sr: "srednje",
        ro: "Mediu"   
    }
    
    var hard = {
        bg: "",
        da: "Svær",
        de: "schwer",
        en: "Hard",
        hr: "Teško",
        it: "Difficile",
        mk: "",
        nl: "Moeilijk",
        sr: "tesko",
        ro: "Dificil"   
    }
    
    var gameTitle = {
        bg: "",
        da: "Vendespil",
        de: "Memory",
        en: "Memory Card Game",
        hr: "Igra memoriranja karata",
        it: "Memory",
        mk: "",
        nl: "Memory Kaart Spel",
        sr: "Igra memorisanja karata",
        ro: ""
    }
    
	var resetGameText = {
		bg: "Рестартирай Играта",
		da: "Nulstil spillet",
		de: "Spiel zurücksetzen",
		en: "Reset game",
		hr: "Resetiraj igru",
		it: "Ripristina il gioco",
		mk: "Ресетирај ја играта",
		nl: "Opnieuw beginnen",
		sr: "Resetuj igru",
		ro: "Resetare joc"
	};
	
	var gameSummary = {
		bg: "Резюме на играта ви",
		da: "Opsummering på dit spil",
		de: "Ihr Spielergebnis",
		en: "Your game summary",
		hr: "Sažetak vaše igre",
		it: "Riepilogo della partita",
		mk: "Резиме на Вашата игра",
		nl: "Spel samenvatting",
		sr: "Rezime vaše igre",
		ro: "Rezumatul jocului",
		replay: {
			bg: "Повтори",
			da: "Replay",
			de: "Nochmal spielen!",
			en: "Replay",
			hr: "Igraj ponovo",
			it: "Replay",
			mk: "Играј повторно",
			nl: "Opnieuw",
			sr: "Ponovo igraj",
			ro: "Reluare"
		},
		close: {
			bg: "Затвори",
			da: "Lukke",
			de: "Schließen",
			en: "Close",
			hr: "Zatvori",
			it: "Chiudi",
			mk: "Затвори",
			nl: "Sluiten",
			sr: "Zatvori",
			ro: "Închis"	
		},
		clicks: {
			bg: "Натисни",
			da: "klik",
			de: "klicks",
			en: "clicks",
			hr: "klikova",
			it: "click",
			mk: "Кликнувања",
			nl: "clicks",
			sr: "klikovi",
			ro: "clicuri"
		},
		seconds: {
			bg: "секунди",
			da: "sekunder",
			de: "sekunden",
			en: "seconds",
			hr: "sekundi",
			it: "secondi",
			mk: "секунди",
			nl: "seconden",
			sr: "sekundi",
			ro: "secunde"
		}
	};
		
  var initData = ''; // Later enable to change this on the fly
  var initOpts = {}
  
  var methods = { 
  
      init : function() {
            
            if(typeof ewallApp == 'object' && ewallApp != null) {
                UserName = ewallApp.currentSession.user.username;
                UserLang = ewallApp.preferedLanguage;
            }
          
            $("#game-body").append("<div class='block' id=buttons><div class='centered'>" +
            						"<div style='font-size:24pt'>"+selectLevel[UserLang]+"</div>" +
            						"<div><button class='button' id='but_easy' onclick='levelEasy()'></button></div>" +
                                    "<div><button class='button' id='but_medium' onclick='levelMedium()'></button></div>" +
                                    "<div><button class='button' id='but_difficult' onclick='levelDifficult()'></button></div></div></div>");
            document.getElementById('but_easy').innerHTML = easy[UserLang];
            document.getElementById('but_medium').innerHTML = medium[UserLang];
            document.getElementById('but_difficult').innerHTML = hard[UserLang];
            $("#game-header").html(gameTitle[UserLang]);
           
            $("#main").hide();
      },
      
      setup : function(){
    	// Get info from eWall mainscreen application  
    	if(typeof ewallApp == 'object' && ewallApp != null) {
    		UserName = ewallApp.currentSession.user.username;
    		UserLang = ewallApp.preferedLanguage;
    		
    	}
        
        var options = {
            itemWidth: 156,
            itemHeight: 156,
            itemsMargin: 40,
            colCount: 4,
            animType: 'fade',
            flipAnim: 'rl',
            animSpeed: 500,
            gameSummary: true,
            resultIcons: true,
            randomised: true,
            onFinishCall: '',
            nomOfMatches: cardNums,
            zoomLevel: 2
        };
        $("#main").show();
    	// modify the "Reset game" link according to selected language
    	$("#restart").html(resetGameText[UserLang]);
        $("#game-header").html(gameTitle[UserLang]);
        // VARIABLES **************************************************************
        // ************************************************************************
        
        // Keeps the record of the initial items and options
        initData = $(this).html();
        initOpts = options;

        // gets the parameters
        var opts = $.extend({}, $.fn.quizyMemoryGame.defaults, options);

        // keeps the number of cards
        var itemsNum = opts.nomOfMatches * 2;
        
        // keeps the total number of cards
        var totalItemsNum = $(this).children('ul').children('li').length;

        // we keep here the selected correct items (after we have a match)
        var correctItems = new Array();

        // keeps the matching ids = which elements match
        var matches = new Array();

        // keep the inner html of the elements(to hide them from web inspector, etc)
        var inHtml = new Array();

        // a selector class for the cards
        var itemsClass = 'quizy-mg-item';

        // keep the class and the id of the selected item
        var selItemClass = '';
        var selItemId = -1;

        // keeps the number of clicks for a turn - it can be 0, 1 or 2
        var numClicks = 0;

        // keeps the number of clicks in general
        var numTotalClicks = 0;

        // zoomLevel of the end result
        var zoomLevel = opts.zoomLevel;
        // keep the numer of matches and the number of seconds for the summary
        var numMatches = 0;
        var numSeconds = 0;

        // a timer variable
        var gameTimer;

        // variables from the plugin parameters (defined and described at the end)
        var delayShow = opts.openDelay;
        var w = opts.itemWidth;
        var h = opts.itemHeight;
        var m = opts.itemsMargin;
        var rowNum = Math.ceil(itemsNum/opts.colCount);
        var random = opts.randomised;
        
        // FUNCTIONS **************************************************************
        // ************************************************************************

        var resultId;
        // A function to handle the element click         
        var handleClick = function(){
          // starts the timer
          if(numTotalClicks==0) {
        	  gameTimer = setInterval(incTime,1000);
          	var quizResult = { "gameName": "memory-game", "username": userName, "type": "game", "level": level, "nrOfMoves": numTotalClicks, "elapsedTimeSecs": numSeconds, "nrOfLevels": 3, "minMoves": cardNums*2, "completed": "false"};
			ewallApp.ajax({
	        	type: 'post',
                    url : '../service-brick-logicgames/v1/result/memory-game/' + userName,
                    data: quizResult,
                    cache: false, 
                    error: function(xhr, ajaxOptions, thrownError) {
                        //alert(xhr.statusText);
                        //alert(thrownError);
                    },
					success: function(response) {
			            resultId = response.resultId;
			        }
                });

          } else if (resultId != null){
  	      	var quizResultUpdates = { "nrOfMoves": numTotalClicks, "elapsedTimeSecs": numSeconds, "minMoves": cardNums*2, "completed": "false"};
			ewallApp.ajax({
	        	type: 'post',
	                url : '../service-brick-logicgames/v1/result/'+resultId,
	                data: quizResultUpdates,
	                cache: false, 
	                error: function(xhr, ajaxOptions, thrownError) {
	                    //alert(xhr.statusText);
	                    //alert(thrownError);
	                }
	            });
        	  
          }
          // counts the clicks
          numTotalClicks ++;

          // keeps the class for the clicked item
          var tId = $(this).attr('id');
          var tdIdNum = parseInt(tId.substring(itemsClass.length,tId.length));
          var tClass = matches[tdIdNum];
          // calls the unbind function (makes the button inactive)
          unbindClick($(this));
          showItem($(this),tdIdNum);
          // if it's the first click out of two (turning the first card)
          if(numClicks==0){
            numClicks ++ ;
            selItemClass = tClass;
            selItemId = tId;
          // if it's the second click out of two (turning the second card)
          }else if(numClicks == 1){
            numClicks = 0;
            // if both have the same class = we HAVE a match
            if(tClass == selItemClass){
              showResIcon('correct');
              unbindClick($('.'+tClass));
              // Adds the turned cards to the correct items array
              correctItems.push(tId);
              correctItems.push(selItemId);
              //increases the number of correct matches
              numMatches ++ ;
              // If all of the cards are turned and the game is complete
              if(numMatches == itemsNum/2){
            	  
             	var quizResultUpdates = { "nrOfMoves": numTotalClicks, "elapsedTimeSecs": numSeconds, "minMoves": cardNums*2, "completed": "true"};
    			ewallApp.ajax({
    	        	type: 'post',
                        url : '../service-brick-logicgames/v1/result/'+resultId,
                        data: quizResultUpdates,
                        cache: false, 
                        error: function(xhr, ajaxOptions, thrownError) {
                            //alert(xhr.statusText);
                            //alert(thrownError);
                        }
                    });

    			ewallApp.logEWallet('GAMES', 10);

    			// removes the timer
                clearInterval(gameTimer);
                // if game summary is set, adds the info to it and shows it.
                if(opts.gameSummary){
                  $('div#quizy-game-summary').
                      children('div#gs-column2').
                      html(numSeconds+'<br>'+gameSummary.seconds[UserLang]);
                  $('div#quizy-game-summary').
                      children('div#gs-column3').
                      html(numTotalClicks+'<br>'+gameSummary.clicks[UserLang]);
                  $('div#quizy-game-summary').delay(2000).fadeIn(1000);
                }
                // if is set makes an AJAX call and sends the the necessary params
                if(opts.onFinishCall!=''){
                	console.log("calling onFinishCall");
                  opts.onFinishCall({ clicks: numTotalClicks, time: numSeconds } );
                }
              }
              else{
                  unbindClick($('div.'+itemsClass));
                  // after a certain time adds back the click event to the card
                  setTimeout( function(){
                    $('.'+itemsClass).each(function(){
                      var myId = $(this).attr('id');
                      if(correctItems.indexOf(myId) == -1){
                        bindClick($(this));
                      }
                    });
                  }, delayShow/3+opts.animSpeed+250);
              }
            // if they dont have the same class = WE DON'T HAVE a match
            }else{
              showResIcon('wrong');
              unbindClick($('div.'+itemsClass));
              // turns the cards back
              hideItem($('div#'+selItemId));
              hideItem($(this));
              // after a certain time adds back the click event to the card
              setTimeout( function(){
                $('.'+itemsClass).each(function(){
                  var myId = $(this).attr('id');
                  if(correctItems.indexOf(myId) == -1){
                    bindClick($(this));
                  }
                });
              }, delayShow+opts.animSpeed+250);
            }
          }
        }
        
        //removes a click from an element
        var unbindClick = function(el){
          el.unbind('click');
          el.css('cursor','default');
        }

        // adds a click to an element
        var bindClick = function(el){
          el.bind('click',handleClick);
          el.css('cursor','pointer');
        }

        // shows item with different animation/based on settings
        var showItem = function(el,id){
          var topId = el.children('div.quizy-mg-item-top').attr('id');
          switch(opts.animType){
            default:
            case 'fade':
              addInFullHTML(el,id);
              $('#'+topId).fadeOut(opts.animSpeed);
            break;
            case 'flip':
              el.flip({
                direction:opts.flipAnim,
                speed: opts.animSpeed,
                content: el.children('div.quizy-mg-item-bottom'),
                color:'#777',
                onEnd: function(){
                  addInHTML(el,id);
                }
              });
            break;
            case 'scroll':
              addInFullHTML(el,id);
              $('#'+topId).animate({height: 'toggle', opacity:0.3},opts.animSpeed);
            break;
          }
        }

        // hides item with different animation/based on settings
        var hideItem = function(el){
          var topId = el.children('div.quizy-mg-item-top').attr('id');
          switch(opts.animType){
            default:
            case 'fade':
              $('#'+topId).delay(delayShow).fadeIn(opts.animSpeed, function(){
                removeInHTML(el);
              });
            break;
            case 'flip':
              setTimeout( function(){
               el.revertFlip();
              }, delayShow);
              setTimeout( function(){
               removeInHTML(el);
              }, delayShow+opts.animSpeed*4);
            break;
            case 'scroll':
              $('#'+topId).delay(delayShow).
                          animate({height: 'toggle', opacity:1},opts.animSpeed, 
                          function(){
                            removeInHTML(el);
                          });
            break;
          }      
        }

        //shows and hids the correct/wrong message after an action
        var showResIcon = function(type){
          if(opts.resultIcons){
            var el;
            var time = Math.round(delayShow/3);
            if(type=='wrong'){
              el = $('div#quizy-mg-msgwrong');
            }else if(type=='correct'){
              el = $('div#quizy-mg-msgcorrect');
            }
            el.delay(time).fadeIn(time/2).delay(time/2).hide("explode", time/2);
          }
        }

        // time functions
        var incTime = function(){
          numSeconds ++;
        }

        // function for adding the inner HTMK
        var addInFullHTML = function(el,id){
          el.children('.quizy-mg-item-bottom')
            .children('.mgcard-show')
            .html(inHtml[id]);
        }

        var addInHTML = function(el,id){
          el.children('.mgcard-show')
            .html(inHtml[id]);
        }

        var removeInHTML = function(el){
          el.children('.quizy-mg-item-bottom').children('.mgcard-show').html('');
        }

        
        // MAIN INIT **************************************************************
        // ************************************************************************
        // hides the <li> items
        $(this).children('ul').hide();

        // makes the div wrapper big enough
        $(this).css({height:rowNum*(h+m)+'px'});

        // creates an array for randomising the items (if randomised is true)
        // and creates an empty inner html array
		if(random){
			var ranArr = Array();
	        for(var j=0; j< totalItemsNum; j++){
	          inHtml[j] = '';
	          ranArr.push(j);
	        }
		}
        // Generates all the elements, based on the data in the <li> elements
        var j=0;
        var i=0;
        while(i<itemsNum){

          // randozises the card - picks an item with a random key and
          // removes it from the random array (if randomised is true)
            if(random){
                var pick = Math.floor(Math.random()*ranArr.length);
                j = ranArr[pick];
                ranArr.splice(pick,1);
            }else{
                j = i;
            }

          // gets the data from each <li> element
          var inEl = $(this).children('ul').children('li').eq(j);
          if(inEl.index() % 8 >= itemsNum / 2){
              continue;
          }
          // calculates the position of each element
          var missingCol = (4 - opts.colCount) / 2;
          var xRatio = i%opts.colCount;
          var yRatio = Math.floor(i/opts.colCount);
          var l = xRatio*(w+m)+w*missingCol;
          var t = yRatio*(h+m);

          // Adds the innerHtml to the array
          inHtml[j] = inEl.html();

          // appends the cards to the element
          $(this).append('<div id="'+itemsClass+j+'" class="'+itemsClass+
          '" style="width:'+
          w+'px; height:'+h+'px; left:'+l+'px; top:'+t+'px">' +
          '<div class="quizy-mg-item-bottom"><div class="mgcard-show">'+
          '</div></div><div id="quizy-mg-item-top'+j+
          '" class="quizy-mg-item-top" style="width:'+
          w+'px; height:'+h+'px;"></div></div>');
          i++;

          // Adds the element match id to the array of matches
          matches[j] = inEl.attr('class');

        }
        // removes the initial <li> elements
        $(this).children('ul').remove();

        // adds the icons for the result after each match
        if(opts.resultIcons){
          $(this).append('<div id="quizy-mg-msgwrong"'+
          ' class="quizy-mg-notification-fly quizy-mg-notification-fly-neg"></div>'+
          '<div id="quizy-mg-msgcorrect" class="quizy-mg-notification-fly '+
          ' quizy-mg-notification-fly-pos"></div>');
          // positions the result icons in the middle of the div wrapper
          var xMid = $(this).width()/2 - 
                      $('div.quizy-mg-notification-fly').width()/2;
          var yMid = $(this).height()/2 - 
                      $('div.quizy-mg-notification-fly').height()/2 -
                      opts.itemsMargin/2;
          $('div.quizy-mg-notification-fly').css({top:yMid+'px',left:xMid+'px'});
        }
        
        // Appends game summary div if set in the opts.
        if(opts.gameSummary){
          
          var gameEl = $(this);
          gameEl.append('<div style="zoom:' + zoomLevel + ';" id="quizy-game-summary"><div class="gs-column" id="gs-column1">'+
                          gameSummary[UserLang]+
                          '</div><div class="gs-column" id="gs-column2"></div>'+
                          '<div class="gs-column" id="gs-column3"></div>'+
                          '<div class="quizy-game-clear"></div></div>');
          // positions the summary div in the middle of the div wrapper
          var xMid = (gameEl.width()/2 - 
                      $('div#quizy-game-summary').width()*zoomLevel/2)/zoomLevel;
          var yMid = (gameEl.height()/2 - 
                      $('div#quizy-game-summary').height()*zoomLevel/2)/zoomLevel -
                      opts.itemsMargin/2;
          $('div#quizy-game-summary').css({top:yMid+'px',left:xMid+'px'});
          
          // Appends replay but if set in the opts.
          if(opts.replayButton){
            $('#quizy-game-summary').append('<div id="gs-replaybut">'+
                                            gameSummary.replay[UserLang]+'</div>');
          }
          
          // Appends the close button
          $('#quizy-game-summary').append('<div id="gs-closebut">'+
                                          gameSummary.close[UserLang]+'</div>');
          
          // adds a click event to the close button to be removed on click
          $('div#gs-closebut').click(function(){
            $(this).parent().fadeOut();
          });
          
          // adds a click event to the replay button
          $('div#gs-replaybut').click(function(){
            gameEl.quizyMemoryGame('restart');
          });
          
        }
        
        
        // adds the click event to each element
        $('.quizy-mg-item').click(handleClick);
        

      },
      
      destroy : function( ) {
        $(this).empty();
      },
      
      restart: function( ){
        methods.destroy.apply( this );
        $(this).append(initData);
        methods.init.call( this, initOpts );
      }
      
      
  };
  
  
  $.fn.quizyMemoryGame = function(optionsMethods) {
    
    if ( methods[optionsMethods] ) {
        return methods[ optionsMethods ].apply( this, arguments);
    } else if ( typeof optionsMethods === 'object' || ! optionsMethods ) {
        return methods.init.apply( this, arguments );
    } else {
        $.error( 'Method ' +  optionsMethods + ' does not exist on jQuery.tooltip' );
    }
    
  }
  
  /**** plugin parameters *****************************************************
  *****************************************************************************
  
    * itemWidth:         The width of the card in pixels.
    * itemHeight:        The width of the card in pixels.
                         Don't forget to change the style of the element in
                         the CSS if you change one of this two paremeters
    * itemsMargin:       The right and bottom margin of the element defining
                         the margin between the cards.
    * colCount:          In how many columns the plugin should arrange the cards
    * animType:          The type of animation used when a card is clicked. 
                         Can be 'flip', 'fade' and 'scroll'.
    * flipAnim:          The direction of the flip animation, if chosen in the
                         previous property.Possible values: 'tb', 'bt', 'lr', 'rl'
    * animSpeed:         How fast the card turning animation should be (in ms)
    * openDelay:         For how long the card should stay turned (in ms)
    * resultIcons:       After turning each to pairs the plugin shows an icon
                         if it was correct or not. Can be true or false
    * gameSummary:       At the end of the game the plugin shows a short game
                         summary - how many clicks the user has done and how 
                         much time it took to complete the game. 
                         Can be true or false
    * textSummaryTitle:  The title of the summary text at the end of the game
    * textSummaryClicks: The same as the previous but used for the text 
                         indicating the clicks done.
    * textSummaryTime:   The same as the previous but used for the text 
                         indicating the time to complete.
    * replayButton:      At the end of the game the at the bottom of the summary
                         popup a replay button can be shown
                         Can be true or false
    * replayButtonText:  The text to appear on the replay button
    * closeButtonText:   The text to appear on the close button
    * onFinishCall:      The call back function
                         It sends two arguments: clicks and time.
                         
    ***** METHODS ************************************************************
    
    * init:             initializes the plug
    * destroy:          destroys the plug
    * reset:            resets the game
                         
  ****************************************************************************/
  
  $.fn.quizyMemoryGame.defaults = {itemWidth: 156, itemHeight: 156, itemsMargin:10, colCount:4, animType:'scroll', animSpeed:250, openDelay:2500, flipAnim:'rl', resultIcons:true, gameSummary:true, randomised:true, /*textSummaryTitle:'Your game summary', */replayButton:true, /*replayButtonText:'Replay', closeButtonText:'Close', textSummaryClicks:'clicks', textSummaryTime:'seconds', */onFinishCall:'', nomOfMatches:8}
  
  
})(jQuery);