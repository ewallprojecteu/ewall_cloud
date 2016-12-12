$( document ).ready(function() {
    //console.log( "document loaded" );
    
    $('.ewall-window-close-btn').on('click', function (event) {        
        event.preventDefault();
        
        var $window = $(this).parents('.ewall-window');
        /* wait until animation button shows */
        window.setTimeout(function(){
            // hide the window
            $window.hide();
            
        },500);
        
    });
    
    // ewall window settings button behaviour
    $('.ewall-window-settings-btn').on('click', function (event) {        
        event.preventDefault();
        
       var $this = $(this);
        /* wait until animation button shows */
        window.setTimeout(function(){
            
            // do something.
            // the button object can be reached by using the $this variable.
            
        },500);
        
    });
    
    // ewall window-to-widget button behaviour
    $('.ewall-window-to-widget-btn').on('click', function (event) {        
        event.preventDefault();
        
        var $this = $(this);            
        /* wait until animation button shows */
        window.setTimeout(function(){
            
            // do something.
            // the button object can be reached by using the $this variable.
            
        },500);
        
    });
 });  
