var $jq = $;
$jq( document ).ready(function() {
    //console.log( "document loaded" );
    
    /* paper button effect 
    * always attache this event handler above the speciffic button event handler
    */
    
    $jq('.button').on('click', function (event) {
      event.preventDefault();
      
      var $div = $jq('<div/>'),
          btnOffset = $jq(this).offset(),
      		xPos = event.pageX - btnOffset.left,
      		yPos = event.pageY - btnOffset.top;

      $div
        .addClass('circle')
        .css({
          top: yPos - 15,
          left: xPos - 15
        }) 
        .appendTo($jq(this));

      window.setTimeout(function(){
        $div.remove();
      }, 1500);
    });
    
    /*
    * User logout handler
    */
    $jq('#user').on('click', function (event) {        
        event.preventDefault();
        ewallApp.logout();
    });
    
    /* Window general event handlers
    * Defines actions to be taken in order to show/hide/handle windows
    */
    $jq('.ewall-window-close-btn').on('click', function (event) {        
        event.preventDefault();
        
        var $window = $jq(this).parents('.ewall-window');
        /* wait until animation button shows */
        window.setTimeout(function(){
            // hide the window
            $window.hide();
            ewallApp.closeApp();
        },500);
        
    });
    
    // ewall window settings button behaviour
    $jq('.ewall-window-settings-btn').on('click', function (event) {        
        event.preventDefault();
        
       var $this = $jq(this);
        /* wait until animation button shows */
        window.setTimeout(function(){
            
            // do something.
            // the button object can be reached by using the $this variable.
            
        },500);
        
    });
    
    // ewall window-to-widget button behaviour
    $jq('.ewall-window-to-widget-btn').on('click', function (event) {        
        event.preventDefault();
        
        var $this = $jq(this);            
        /* wait until animation button shows */
        window.setTimeout(function(){
            
            // do something.
            // the button object can be reached by using the $this variable.
            
        },500);
        
    });
    // END Window general event handlers
    
    
    /* Notification general event handler 
    * Defines actions to be taken in order to show/hide/handle Notifications
    */
    
    // ewall notification close button behaviour
    $jq('.ewall-notification-close-btn').on('click', function (event) {
        event.preventDefault();
        
        var $notification = $jq(this).parents('.ewall-notification');
        /* wait until animation button shows */
        window.setTimeout(function(){
            // hide the window
            $notification.hide();
            
        },500);
    });
    // ewall notification accept/decline button behaviour
     $jq('.ewall-notification-footer-btn').on('click' , function (event) {
         event.preventDefault();
         
         var $thisBtn = $jq(this);
         if ($thisBtn.hasClass('accept')) {
             // do things if user accepts
             
         } else {
             if ($thisBtn.hasClass('decline')) {
                 
             }
             // do things if user declines
         } 
         
     });
    // ewall notification done button behaviour
    // TO DO - define chance in UI for DONe button
    
    
    // END Notification general event handler
    
    
    /* Main menu event handlers
    * Defines actions to be taken in order to show a group of applications
    */
	   $jq('#healthGroup').on('click', function (event) {
		   event.preventDefault();
           
           if($jq('#healthSubgroup').hasClass('active')){
               $jq('.active').addClass('not-active').removeClass('active');
           } else{
               $jq('.active').addClass('not-active').removeClass('active');
               setTimeout(function() {
				    $jq('#healthSubgroup').addClass('active').removeClass('not-active');
               },250);
           }
		   
	   });
	   
	   $jq('#contactsGroup').on('click', function (event) {
		   event.preventDefault();
           
           if($jq('#contactsSubgroup').hasClass('active')){
               $jq('.active').addClass('not-active').removeClass('active');
           }else{
               $jq('.active').addClass('not-active').removeClass('active');
               setTimeout(function() {
				    $jq('#contactsSubgroup').addClass('active').removeClass('not-active');
		       },250);
           }
		   
	   });
	   
	   $jq('#houseGroup').on('click', function (event) {
		   event.preventDefault();
           
           if($jq('#houseSubgroup').hasClass('active')){
               $jq('.active').addClass('not-active').removeClass('active');
           }else{
               $jq('.active').addClass('not-active').removeClass('active');
               setTimeout(function() {
				    $jq('#houseSubgroup').addClass('active').removeClass('not-active');
		       },250);
           }
		   
	   });
	   
	   $jq('#gamesGroup').on('click', function (event) {
		   event.preventDefault();
           
           if($jq('#gamesSubgroup').hasClass('active')){
               $jq('.active').addClass('not-active').removeClass('active');
           }else{
               $jq('.active').addClass('not-active').removeClass('active');
               setTimeout(function() {
                   $jq('#gamesSubgroup').addClass('active').removeClass('not-active');
               },250);
           }
		   
	   });
   
});