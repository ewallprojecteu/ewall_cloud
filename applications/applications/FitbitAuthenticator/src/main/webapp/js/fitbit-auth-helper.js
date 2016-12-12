'use strict';
function FitbitAuthHelper() {
	//******************************************************************************
	this.Translate = function(dictionary) {
		$("[data-translate]").text(function () {
	        var key = $(this).data("translate");
	        if (dictionary.hasOwnProperty(key)) {
	            return dictionary[key];
	        }
	    });
	    
	    $("[data-translate-placeholder]").attr('placeholder', function () {
	        var key = $(this).data("translate-placeholder");
	        if (dictionary.hasOwnProperty(key)) {
	            return dictionary[key];
	        }
	    });
	}
	//******************************************************************************
	this.TranslateDynamicContent = function(dictionary) {
		$("#dynamicContent [data-translate]").text(function () {
	        var key = $(this).data("translate");
	        if (dictionary.hasOwnProperty(key)) {
	            return dictionary[key];
	        }
	    });
	    
	    $("#dynamicContent [data-translate-placeholder]").attr('placeholder', function () {
	        var key = $(this).data("translate-placeholder");
	        if (dictionary.hasOwnProperty(key)) {
	            return dictionary[key];
	        }
	    });
	}
	//******************************************************************************
}//end of object