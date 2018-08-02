(function() {
    'use strict';
    angular
        .module('civilDefanceApp')
        .factory('Border', Border);

    Border.$inject = ['$resource', 'DateUtils'];

    function Border ($resource, DateUtils) {
        var resourceUrl =  'api/borders/:id';
      // console.log(resourceUrl);
        return $resource(resourceUrl, {}, {

            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.insertionDate = DateUtils.convertLocalDateFromServer(data.insertionDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.insertionDate = DateUtils.convertLocalDateToServer(copy.insertionDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.insertionDate = DateUtils.convertLocalDateToServer(copy.insertionDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
