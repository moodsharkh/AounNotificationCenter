(function() {
    'use strict';
    angular
        .module('civilDefanceApp')
        .factory('Notification', Notification);

    Notification.$inject = ['$resource', 'DateUtils'];

    function Notification ($resource, DateUtils) {
        var resourceUrl =  'api/notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.sendingDate = DateUtils.convertDateTimeFromServer(data.sendingDate);
                        data.expiryDate = DateUtils.convertDateTimeFromServer(data.expiryDate);
                    }
                    return data;
                }
            },
            'update': {
                method:'PUT'
                // ,
                // transformRequest: function (data) {
                //     var copy = angular.fromJson(data);
                //     copy.expiryDate = DateUtils.convertLocalDateToServer(data.expiryDate);
                //     return copy;
                // }
            }
        });
    }
})();
