(function() {
    'use strict';
    angular
        .module('civilDefanceApp')
        .factory('Points', Points);

    Points.$inject = ['$resource'];

    function Points ($resource) {
        var resourceUrl =  'api/points/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
