$( document ).ready(function() {
    //console.log( "document loaded" );
    
    /* paper button effect 
    * always attache this event handler above the speciffic button event handler
    */
    
    $('.button').on('click', function (event) {
      event.preventDefault();
      
      var $div = $('<div/>'),
          btnOffset = $(this).offset(),
      		xPos = event.pageX - btnOffset.left,
      		yPos = event.pageY - btnOffset.top;

      $div
        .addClass('circle')
        .css({
          top: yPos - 15,
          left: xPos - 15
        }) 
        .appendTo($(this));

      window.setTimeout(function(){
        $div.remove();
      }, 1500);
    });

});
    