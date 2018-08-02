(function() {
    'use strict';
    angular
        .module('civilDefanceApp')
        .factory('Languages', Languages);

    Languages.$inject = ['$resource'];

    function Languages ($resource) {
        var resourceUrl =  'api/languages/:id';

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
