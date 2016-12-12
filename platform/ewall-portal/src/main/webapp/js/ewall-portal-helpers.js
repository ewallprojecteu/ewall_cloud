'use strict';

/* Services */

var ewallPortalHelpers = angular.module('ewallPortalHelpers', ['ui.router', 'ngTable', 'ngLocalize']);

ewallPortalHelpers.directive('deleteModal', function() {
  return {
    restrict: 'E',
    scope: {
      name: '=name',
      deleteAction: '&deleteAction'
    },
    templateUrl: 'partials/delete-modal.html'
  }
});

ewallPortalHelpers.directive('devicesSetConfirmModal', function() {
  return {
    restrict: 'E',
    scope: {
      addDevicesSetConfirmedAction: '&addDevicesSetConfirmedAction'
    },
    templateUrl: 'partials/devices-set-confirm-modal.html'
  }
});

ewallPortalHelpers.directive('errorModal', function() {
	  return {
	    restrict: 'E',
	    templateUrl: 'partials/error-modal.html'
	  }
	});


ewallPortalHelpers.directive('loadingModal', function() {
  return {
    restrict: 'E',
    templateUrl: 'partials/loading-modal.html'
  }
});

ewallPortalHelpers.directive('successModal', function() {
	  return {
	    restrict: 'E',
	    templateUrl: 'partials/success-modal.html'
	  }
	});

ewallPortalHelpers.factory('DeleteManager', ['locale', function(locale) {
  var objectToBeDeleted = {};
  var defaultDeleteMsg = locale.ready('common').then(function() {
    return locale.getString('common.deleteError');
  });
  this.objectName = '';
  this.displayName = '';

  this.remove = function(scope, objects, service, paramName, tableParams, msg) {
    if (!msg) {
      msg = defaultDeleteMsg;
    }
    var params = {};
    params[paramName] = this.objectName;
    service.remove(params, function() {
      objects.splice($.inArray(objectToBeDeleted[paramName], $.map(objects, function(el) {
        return el[paramName];
      })), 1);
      tableParams.reload();
    }, function() {
      scope.showError(msg);
    });
  };

  this.removeDevice = function(scope, objects, service) {
    var paramName = 'uuid';
    var params = {
      sensEnvId: objectToBeDeleted.sensingEnvironmentUUID,
      deviceId: objectToBeDeleted.uuid
    };
    service.remove(params, function() {
      objects.splice($.inArray(objectToBeDeleted[paramName], $.map(objects, function(el) {
        return el[paramName];
      })), 1);
    }, function() {
      scope.showError(defaultDeleteMsg);
    });
  };

  this.showDeleteModal = function(object, objectName, displayName) {
    objectToBeDeleted = object;
    this.objectName = objectName;
    this.displayName = displayName;
    $('#deleteModal').modal('show');
  };
  return this;
}]);

ewallPortalHelpers.factory('SubmitManager', ['$state', 'locale', function($state, locale) {
  this.objectName = '';

  this.submit = function(service, scope, object, objectParamName, objectNameToUpdate, nextState) {
    var params = {};
    if (objectNameToUpdate) {
      params[objectParamName] = objectNameToUpdate;
    }
    service.save(params, object, function() {
  		successEvent($state, nextState, locale, scope);
    }, function(res) {
      errorEvent(res, locale, scope);
    });
  };

  this.putDevice = function(service, scope, object, nextState) {
    var params = {
      sensEnvId: object.sensingEnvironmentUUID,
      deviceId: object.uuid
    };
    service.update(params, object, function() {
      successEvent($state, nextState, locale, scope);
    }, function(res) {
      errorEvent(res, locale, scope);
    });
  };

  this.postDevice = function(service, scope, object, nextState) {
    var params = {
      sensEnvId: object.sensingEnvironmentUUID
    };
    service.save(params, object, function() {
      successEvent($state, nextState, locale, scope);
    }, function(res) {
      errorEvent(res, locale, scope);
    });
  };

  this.showDevicesSetConfirmModal = function(objectName) {
    this.objectName = objectName;
    $('#devicesSetConfirmModal').modal('show');
  };

  this.postDefaultDevicesSet = function(scope, service, callback) {
    var params = {
      sensEnvId: this.objectName
    };
    service.save(params, null, function() {
      callback();
      locale.ready('common').then(function() {
        scope.showSuccessMessage(locale.getString('common.submitSuccessed'));
        }
      );
    }, function(res) {
      callback();
      locale.ready('common').then(function() {
        scope.showError(locale.getString('common.submitError'));
        }
      );
    });
  };

  function successEvent($state, nextState, locale, scope) {
    locale.ready('common').then(function() {
        scope.showSuccessMessage(locale.getString('common.submitSuccessed'));
        }
      );
      $state.go(nextState);
  }

  function errorEvent(res, locale, scope) {
    locale.ready('common').then(function() {
      if (res.status === 409) { // Conflict
        scope.showError(locale.getString('common.submitError') + ' ' + res.statusText);
      }
      else if (res.data.message){
        scope.showError(locale.getString('common.submitError') + ' ' + res.data.message);
      }
      else {
        scope.showError(locale.getString('common.submitError') + ' ' + res.statusText);
      }
    });
  }

  return this;
}]);

ewallPortalHelpers.service('TableManager', ['$filter', 'ngTableParams', function($filter, ngTableParams) {
  var handlePages = function($defer, params, orderedData) {
    params.total(orderedData.length);
    if (params.page() > 1 && params.count() * (params.page() - 1) >= params.total()) {
      params.page(Math.ceil(params.total() / params.count()));
    }
    $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
  };

  this.generateTableParams = function(scope, rows, search, initalSort, addFilter) {
    var config = {
      page: 1, // show first page
      count: 10, // count per page
      sorting: {}
    };
    config.sorting[initalSort] = 'asc' // initial sorting
    return new ngTableParams(config, {
      total: rows.length, // length of data
      getData: function($defer, params) {
        // use build-in angular filter
        var orderedData = params.sorting() ? $filter('orderBy')(rows, params.orderBy()) : rows;

        orderedData = $filter('filter')(orderedData, scope[search]);
        if (addFilter) {
          orderedData = addFilter(orderedData);
        }

        handlePages($defer, params, orderedData);
      }
    });
  };

  this.headerClass = function(tableParams, name) {
    if (tableParams) return {
      'sort-asc': tableParams.isSortBy(name, 'asc'),
      'sort-desc': tableParams.isSortBy(name, 'desc')
    };
    return "";
  };

  this.headerClick = function(tableParams, name) {
    var sort = {};
    sort[name] = tableParams.isSortBy(name, 'asc') ? 'desc' : 'asc';
    tableParams.sorting(sort);
  };

}]);

ewallPortalHelpers.service('Utils', ['locale', function(locale) {

  this.setDuallistbox = function(id) {
    var settings = {
      "filterTextClear": "setFilterTextClear",
      "filterPlaceHolder": "setFilterPlaceHolder",
      "moveSelectedLabel": "setMoveSelectedLabel",
      "moveAllLabel": "setMoveAllLabel",
      "removeSelectedLabel": "setRemoveSelectedLabel",
      "removeAllLabel": "setRemoveAllLabel",
      "infoText": "setInfoText",
      "infoTextFiltered": "setInfoTextFiltered",
      "infoTextEmpty": "setInfoTextEmpty"
    };
    locale.ready('duallistbox').then(function() {
      for ( var key in settings) {
        $('#' + id).bootstrapDualListbox(settings[key], locale.getString('duallistbox.' + key), true);
      }
    });
    // $('#' + id).bootstrapDualListbox('refresh');
  };

  this.filterFrom = function(totalList, targetList, key) {
	    var stringList = $.map(targetList, function(app) {
	      return app[key];
	    });
	    return $.grep(totalList, function(app) {
	      return $.inArray(app[key], stringList) != -1;
	    });
	  };
	  
	  
  this.filterList = function(totalList, targetList) {
	    return $.grep(totalList, function(app) {
	      return $.inArray(app, targetList) != -1;
	    });
	  };
		  
		  
/**
 * Parses a list of objects and returns a list of values corresponding to a
 * specific field (with name 'key') in the object.
 * E.g.: form a list of services (JSON objects), gets the list of services names  
 */  
  this.filterValues = function(totalList, key) {
	    return $.map( totalList, function(app) {
		      return app[key];
	    });
	  };
		  

}]);