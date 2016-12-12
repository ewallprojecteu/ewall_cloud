var photoFrameWidget = angular.module("photoFrameWidget", []);

//assign angular controller to module
photoFrameWidget.controller('photoFrameWidgetController', ['$scope', '$stateParams', '$interval', function ($scope, $stateParams, $interval) {
	var testObjectIntervalMs = 10*1000;
	var testObject = new WidgetObjectPhotoFrame();
	console.log("Loaded photoFrameWidgetController #####" + testObject.getId());
	testObject.SetPathToExecuteDirectory('widgets/photo-frame/');
	testObject.SetPathToFusionerFlickr('../fusioner-flickr');
	//testObject.SetPathToFusionerFlickr('http://localhost/fusioner-flickr');
	testObject.InitWidgetObject();
	
    var stopRecurringPhotoFrameRequest = function (promise) {
    	console.log("Stopping Photo Frame Widget scheduled requests ##### " + testObject.getId());
        $interval.cancel(promise);
    }
    
    ewallApp.currentSession.stopRecurringPhotoFrameRequest = stopRecurringPhotoFrameRequest;

    ewallApp.currentSession.recurringPhotoFrameRequest = $interval(function() {
    	testObject.StartLoadPhotos();
    }, testObjectIntervalMs);
    
    ewallApp.currentSession.testObject = testObject;
}]);

function WidgetObjectPhotoFrame() {
	var ObjectsHtmlID = {'PhotoFrameID':'photoFrame', 'PhotoFrameBasicID':'photoFrameBasic', 
	'PhotoFrameAdditionalID':'photoFrameAdditional'};
	
	var PhotosData = {'MaxNumber':20, 'PhotoByDefault':'', 'isRequestFinished':false, 
	'LoadedPhotos':[], 'LocalData':[], 'ReadyData':[], 'PrevImgIndex':0, 'ImgIndex':-1};
	
	var PathToExecuteDirectory="", PathToFusionerFlickr="", UserName="";
	
	var id = Math.random();
	//******************************************************************************
	this.getId = function() {
		return id;
	}
	//******************************************************************************
	this.SetPathToExecuteDirectory = function(path) {
		PathToExecuteDirectory = path;
	}
	//******************************************************************************
	this.SetPathToFusionerFlickr = function(path) {
		PathToFusionerFlickr = path;
	}
	//******************************************************************************
	this.InitWidgetObject = function() {
		if(typeof ewallApp == 'object' && ewallApp != null) {
			UserName = ewallApp.currentSession.user.username;
			
			InitPartPhotosData();
			InitPhotoElementsAndLoadDefaultPhoto();
			GetExternalPhotoData();
		}
	}
	//******************************************************************************
	this.StartLoadPhotos = function() {
		if($('#'+ObjectsHtmlID.PhotoFrameID).length && PhotosData.isRequestFinished) {
			PhotosData.ImgIndex++;
			if(PhotosData.ImgIndex == PhotosData.ReadyData.length) {
				PhotosData.ImgIndex = -1;
				PhotosData.PrevImgIndex = 0;
				GetExternalPhotoData();
			} else {
				if(PhotosData.ReadyData[PhotosData.ImgIndex].isLoaded) {
					ViewLoadedPhoto(PhotosData.ReadyData[PhotosData.PrevImgIndex].src, PhotosData.ReadyData[PhotosData.ImgIndex].src, true);
					PhotosData.PrevImgIndex = PhotosData.ImgIndex;
				} else {
					PhotosData.ImgIndex--;
				}
			}
		}
	}
	//==============================================================================
	function InitPartPhotosData() {
		PhotosData.LocalData = ['photo-1.jpg', 'photo-2.jpg', 'photo-3.jpg', 'photo-4.jpg', 'photo-5.jpg', 'photo-default.jpg'];
		PhotosData.PhotoByDefault = 'photo-default.jpg';
		PhotosData.isRequestFinished = false;
		PhotosData.ReadyData = [];
		PhotosData.PrevImgIndex = 0;
		PhotosData.ImgIndex = -1;		
	}
	//******************************************************************************
	function InitPhotoElementsAndLoadDefaultPhoto() {
		if($('#'+ObjectsHtmlID.PhotoFrameID).length) {
			$('#'+ObjectsHtmlID.PhotoFrameID).empty();
			$('#'+ObjectsHtmlID.PhotoFrameID).append($('<div>').attr('id', ObjectsHtmlID.PhotoFrameBasicID));
			$('#'+ObjectsHtmlID.PhotoFrameID).append($('<div>').attr('id', ObjectsHtmlID.PhotoFrameAdditionalID));
			
			$('#'+ObjectsHtmlID.PhotoFrameBasicID).css({'width':'100%', 'height':'100%', 'position':'absolute', 'top':0, 'left':0});
			$('#'+ObjectsHtmlID.PhotoFrameAdditionalID).css({'width':'100%', 'height':'100%', 'position':'absolute', 'top':0, 'left':0});
			
			var objImage = new Image();
			objImage.onload = function() {
				PhotosData.LoadedPhotos[PhotosData.LoadedPhotos.length] = objImage.src;
				ViewLoadedPhoto('', objImage.src, false);
			}
			objImage.src = PathToExecuteDirectory+"photos/"+PhotosData.PhotoByDefault;
		}
	}
	//==============================================================================
	function GetExternalPhotoData() {
		PhotosData.isRequestFinished = false;
		var url_photos = PathToFusionerFlickr+"/photos/"+UserName;
		var photo_privacy="PUBLIC", photo_size="MEDIUM";
		
		ewallApp.ajax({
			url: url_photos, 
			type: "GET", 
			data: {privacy_status: photo_privacy, size: photo_size}, 
			dataType: "json", 
			async: true, 
			cache: true, 
			success: function(DataPhotoWidget) {
				ProcessExternalPhotoData(DataPhotoWidget);
			}, 
			error: function() {
				ProcessExternalPhotoData([]);
			}
		});
	}
	//******************************************************************************
	function ProcessExternalPhotoData(ExtPhotosData) {
		PhotosData.ReadyData = [];
		var count_photos = ExtPhotosData.length;
		if(count_photos > 0) {
			ExtPhotosData.sort(function() {
				return .5 - Math.random();
			});
			$.each(ExtPhotosData, function(inx, photo_data) {
				PhotosData.ReadyData[inx] = {'src':photo_data.url, 'isLoaded':false};
				if($.inArray(PhotosData.ReadyData[inx].src, PhotosData.LoadedPhotos) > -1) {
					PhotosData.ReadyData[inx].isLoaded = true;
				}
				if(PhotosData.MaxNumber > 0 && PhotosData.MaxNumber <= inx+1) {
					return false;
				}
		    });
		} else {
			$.each(PhotosData.LocalData, function(inx, photo_name) {
				PhotosData.ReadyData[inx] = {'src':PathToExecuteDirectory+"photos/"+photo_name, 'isLoaded':false};
				if($.inArray(PhotosData.ReadyData[inx].src, PhotosData.LoadedPhotos) > -1) {
					PhotosData.ReadyData[inx].isLoaded = true;
				}
				if(PhotosData.MaxNumber > 0 && PhotosData.MaxNumber <= inx+1) {
					return false;
				}
		    });
		}
		
		PhotosData.isRequestFinished = true;
		PreloadPhotos();
	}
	//==============================================================================
	function ViewLoadedPhoto(img_src_prev, img_src, flag_animation) {
		if($('#'+ObjectsHtmlID.PhotoFrameBasicID).length && $('#'+ObjectsHtmlID.PhotoFrameAdditionalID).length) {
			if(flag_animation) {
				$('#'+ObjectsHtmlID.PhotoFrameBasicID).css({'background':'url('+img_src_prev+')', 
				'background-position':'center', 'background-repeat':'no-repeat', 'background-size':'cover'});
				$('#'+ObjectsHtmlID.PhotoFrameAdditionalID).fadeOut("slow", function () {
					$(this).css({'background':'url('+img_src+')', 
					'background-position':'center', 'background-repeat':'no-repeat', 'background-size':'cover'});
					$(this).fadeIn("slow");
				});
			} else {
				$('#'+ObjectsHtmlID.PhotoFrameAdditionalID).css({'background':'url('+img_src+')', 
				'background-position':'center', 'background-repeat':'no-repeat', 'background-size':'cover'});
			}
		}
	}
	//******************************************************************************
	function PreloadPhotos() {
		$.each(PhotosData.ReadyData, function(inx, photoData) {
			if(!photoData.isLoaded) {
				var objImage = new Image();
				objImage.onload = function() {
					PhotosData.LoadedPhotos[PhotosData.LoadedPhotos.length] = objImage.src;
					PhotosData.ReadyData[inx].isLoaded = true;
				}
				objImage.src = PhotosData.ReadyData[inx].src;
			}
		});
	}
	//==============================================================================
};// end of object