(function() {
    'use strict';
    angular
        .module('civilDefanceApp')
        .factory('Messages', Messages);

    Messages.$inject = ['$resource'];

    function Messages ($resource) {
        var resourceUrl =  'api/messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        // data.sendingDate = DateUtils.convertDateTimeFromServer(data.sendingDate);
                        // data.expiryDate = DateUtils.convertDateTimeFromServer(data.expiryDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
